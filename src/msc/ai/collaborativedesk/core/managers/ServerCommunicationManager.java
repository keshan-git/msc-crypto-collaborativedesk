package msc.ai.collaborativedesk.core.managers;

import communication.manager.core.CommunicationManagerListener;
import communication.manager.core.CommunicationServer;
import communication.manager.core.MessageReceiveListener;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.AUTHENTICATION_REQUEST;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.AUTHENTICATION_RESPONSE;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.LIST_SEPARATOR;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.MEMBER_LIST_UPDATE;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.MESSAGE_SEPARATOR;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.NEW_TASK_ADDED;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.TASK_LIST_UPDATE;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class ServerCommunicationManager
{
    private static ServerCommunicationManager instance;
    private CommunicationServer communicationServer;
    private HashMap<Integer, String> clientMap = new HashMap<>();
    
    public static ServerCommunicationManager getIntance()
    {
        if (instance == null)
        {
            instance = new ServerCommunicationManager();
        }
        return instance;
    }
    
    public void start()
    {
        communicationServer = new CommunicationServer();
        communicationServer.startCommunication(1111);
        communicationServer.addMessageReceiveListener(new MessageReceiveListener()
        {
            @Override
            public void onMessageReceived(String message, int clientID)
            {
                Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Client: {0} | {1}", new Object[]{clientID, message});
                String[] components;
                switch (message.substring(0, 1))
                {
                    case AUTHENTICATION_REQUEST :
                        components = message.split(MESSAGE_SEPARATOR);
                        onUserAuthenticationReceived(clientID, components[1], components[2]);
                        break;
                        
                    case NEW_TASK_ADDED:
                        components = message.split(MESSAGE_SEPARATOR);
                        onTaskReceived(components[1]);
                        break;
                        
                    case TASK_LIST_UPDATE:
                        components = message.split(MESSAGE_SEPARATOR);
                        onTaskListUpdateReceived(components[1]);
                        break;
                }
            }
        });
        
        communicationServer.addCommunicationManagerListener(new CommunicationManagerListener()
        {

            @Override
            public void onConnected(int clientID, Socket communicationSocket)
            {
                Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Connected : {0} : {1}", new Object[]{clientID, communicationSocket.getInetAddress().getHostName()});
            }

            @Override
            public void onDisconnected(int clientID, Socket communicationSocket)
            {
                Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Disconnected : {0} : {1}", new Object[]{clientID, communicationSocket.getInetAddress().getHostName()});
                
                String userID = clientMap.get(clientID);
                if (userID != null)
                {
                    for (User user : DeskManager.getIntance().getUserList())
                    {
                        if (user.getUserID().equals(userID))
                        {
                            user.setOnline(false);
                            PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.USER_LIST_PANEL).update();
                            clientMap.remove(clientID);
                            break;
                        }
                    }
                }
            
            }
        });
    }
    
    public void sendTask(String endTaskDetails, User user)
    {
        // From server to other users
        // Dec
        SecurityManager securityManager = SecurityManager.getIntance();
        String taskDetails = securityManager.decryptDetails(Utils.getByteArray(endTaskDetails), securityManager.getServersPrivateKey());
        
        // Enc
        byte[] taskEncDetails = securityManager.encryptDetails(taskDetails, securityManager.getUsersPublicKey(user)); 
        String result = NEW_TASK_ADDED + MESSAGE_SEPARATOR + Utils.getString(taskEncDetails);
        communicationServer.sendString(getClientID(user.getUserID()), result);
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Task Added Sent to : {0}", getClientID(user.getUserID())); 
    }

    public void onTaskReceived(String taskDetails)
    {
        // Update Database  
        DatabaseManager.getIntance().addTask(taskDetails);
        
        // Update other users
        for (User user : DeskManager.getIntance().getUserList())
        {
            //if (user.isOnline())
            //{
                ServerCommunicationManager.getIntance().sendTask(taskDetails, user);
            //}
        }
    }
    
    public void onUserAuthenticationReceived(int clientID, String userID, String hashPassword)
    {
        boolean isValid = DatabaseManager.getIntance().authenticateUser(userID, hashPassword);
        ServerCommunicationManager.getIntance().sendUserAuthenticationResult(clientID, userID, isValid);
        
        if (isValid)
        {
            for (User user : DeskManager.getIntance().getUserList())
            {
                if (user.getUserID().equals(userID))
                {
                    user.setOnline(true);
                    PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.USER_LIST_PANEL).update();
                    clientMap.put(clientID, userID);
                    break;
                }
            }
            sendMemberList(clientID);
            sendTaskList(clientID);
        }
        
    }

    private void sendUserAuthenticationResult(int clientID, String userID, boolean valid)
    {
        String result = AUTHENTICATION_RESPONSE + MESSAGE_SEPARATOR + userID + MESSAGE_SEPARATOR + valid;
        communicationServer.sendString(clientID, result);
    }

    private void sendMemberList(int clientID)
    {
        StringBuilder sb = new StringBuilder();
        for (User user : DeskManager.getIntance().getUserList())
        {
            sb.append(user.getUserID()).append(MESSAGE_SEPARATOR);
            sb.append(user.getUserName()).append(MESSAGE_SEPARATOR);
            sb.append(user.getGender()).append(LIST_SEPARATOR);
        }
        
        String memberDetails = sb.toString();
        String userID = clientMap.get(clientID);
        
        byte[] content = SecurityManager.getIntance().encryptDetails(memberDetails, 
                    SecurityManager.getIntance().getUsersPublicKey(userID));
        
        String result = MEMBER_LIST_UPDATE + MESSAGE_SEPARATOR + Utils.getString(content);
        communicationServer.sendString(clientID, result);
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Member List Update Sent to : {0}", userID);
    }
    
    private void sendTaskList(int clientID)
    {
        StringBuilder sb = new StringBuilder();
        for (Task task : DeskManager.getIntance().getTaskList())
        {
            sb.append(task.serializedString()).append(LIST_SEPARATOR);
        }
        
        String taskDetails = sb.toString();
        String userID = clientMap.get(clientID);
        
        if (userID != null)
        {
            byte[] content = SecurityManager.getIntance().encryptDetails(taskDetails, 
                        SecurityManager.getIntance().getUsersPublicKey(userID));

            String result = TASK_LIST_UPDATE + MESSAGE_SEPARATOR + Utils.getString(content);
            communicationServer.sendString(clientID, result);
            Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Task List Update Sent to : {0}", userID);
        }
    }
        
    private int getClientID(String userID)
    {
        for (int i : clientMap.keySet())
        {
            if (clientMap.get(i).equals(userID))
            {
                return i;
            }
        }
        
        return -1;
    }
    
    private void onTaskListUpdateReceived(String encDetails)
    {
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "TaskListUpdateReceived");    
        
        String plainDetail = SecurityManager.getIntance().decryptDetails(Utils.getByteArray(encDetails), SecurityManager.getIntance().getServersPrivateKey());
        if (plainDetail != null)
        {
            DatabaseManager.getIntance().clearTasks();
            for (String taskString : plainDetail.split(LIST_SEPARATOR))
            {
                Task task = new Task(taskString);
                DeskManager.getIntance().getTaskList().add(task);
                
                SecurityManager sm = SecurityManager.getIntance();
                byte[] encTask = sm.encryptTaskDetails(task, sm.getServersPublicKey());
                DatabaseManager.getIntance().addTask(Utils.getString(encTask));
            }

            for (User user : DeskManager.getIntance().getUserList())
            {
                sendTaskList(getClientID(user.getUserID()));    
            }
        }
        else
        {
            Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Invalid TaskListUpdateReceived : Decryption Fail");    
        }
    }
}
