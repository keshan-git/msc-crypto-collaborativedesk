package msc.ai.collaborativedesk.core.managers;

import communication.manager.core.CommunicationClient;
import communication.manager.core.CommunicationManagerListener;
import communication.manager.core.MessageReceiveListener;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;
import msc.ai.collaborativedesk.ui.CollaborativeDesk;
import msc.ai.collaborativedesk.ui.panel.LoginPanel;

/**
 *
 * @author Keshan De Silva
 */
public class ClientCommunicationManager
{
    private static ClientCommunicationManager instance;
    private CommunicationClient communicationClient;
    private LoginPanel loginPanel;
    
    public static final String PARAM_SEPARATOR = "~";
    public static final String COMPONENT_SEPARATOR = "!";
    public static final String LIST_SEPARATOR = "@";
    public static final String BYTE_ARRAY_SEPARATOR = ":";
    public static final String MESSAGE_SEPARATOR = "#";
    
    public static final String AUTHENTICATION_REQUEST = "%";
    public static final String AUTHENTICATION_RESPONSE = "^";
    public static final String MEMBER_LIST_UPDATE = "&";
    public static final String NEW_TASK_ADDED = "}";
    public static final String TASK_LIST_UPDATE = "(";
         
    public static ClientCommunicationManager getIntance()
    {
        if (instance == null)
        {
            instance = new ClientCommunicationManager();
        }
        return instance;
    }
    
    public void start(String serverIPAddress)
    {
//        if ((communicationClient != null) && (communicationClient.isConnectionAvailable(0)))
//        {
//            communicationClient.stopCommunication();
//        }
        
        communicationClient = new CommunicationClient();
        communicationClient.startCommunication(serverIPAddress, 1111);
        
        communicationClient.addMessageReceiveListener(new MessageReceiveListener()
        {
            @Override
            public void onMessageReceived(String message, int clientID)
            {
                Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Client: {0} | {1}", new Object[]{clientID, message});
                String[] components;
                switch (message.substring(0, 1))
                {
                    case AUTHENTICATION_RESPONSE :
                        components = message.split(MESSAGE_SEPARATOR);
                        onUserAuthenticationReceived(clientID, components[1], Boolean.parseBoolean(components[2]));
                        break;
                        
                    case MEMBER_LIST_UPDATE:
                        components = message.split(MESSAGE_SEPARATOR);
                        onMemberListUpdateReceived(clientID, components[1]);
                        break;
                        
                    case NEW_TASK_ADDED:
                        components = message.split(MESSAGE_SEPARATOR);
                        onTaskReceived(components[1]);
                        break;
                        
                    case TASK_LIST_UPDATE:
                        components = message.split(MESSAGE_SEPARATOR);
                        onTaskListUpdateReceived(clientID, components[1]);
                        break;
                }
            }
        });
        
        communicationClient.addCommunicationManagerListener(new CommunicationManagerListener()
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
            }
        });
    }
    
    public boolean isConnected()
    {
        return communicationClient.isConnectionAvailable(0);
    }
    
    public void sendTask(Task task)
    {
        SecurityManager sm = SecurityManager.getIntance();
        byte[] encTask = sm.encryptTaskDetails(task, sm.getServersPublicKey());

        String result = NEW_TASK_ADDED + MESSAGE_SEPARATOR + Utils.getString(encTask);
        communicationClient.sendString(0, result);
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "New Task Detail Sent");
    }

    public void onTaskReceived(String taskDetails)
    {
        Task task = SecurityManager.getIntance().deccryptTaskDetails(taskDetails, SecurityManager.getIntance().getPrivateKey());
        DeskManager.getIntance().addTask(task);
        JOptionPane.showMessageDialog(null, "New Task Added by : " + task.getCreatedBy().getUserName(), "New Task", JOptionPane.INFORMATION_MESSAGE);            
    }
    
    public void onMemberDetailReceived(byte[] memberDetails)
    {
        User user = SecurityManager.getIntance().deccryptMemberDetails(memberDetails, null);
        DeskManager.getIntance().addUser(user);
        JOptionPane.showMessageDialog(null, "Member Details Updated Successfully", "Member", JOptionPane.INFORMATION_MESSAGE);            
    }

    public void sendUserAuthentication(String userID, String password)
    {
        String passwordHash = SecurityManager.getIntance().generatePasswordHash(userID, password);
        String message = AUTHENTICATION_REQUEST + MESSAGE_SEPARATOR + userID + MESSAGE_SEPARATOR + passwordHash;
        communicationClient.sendString(0, message);
    }
    
    private void onUserAuthenticationReceived(int clientID, String userID, boolean valid)
    {
        if (loginPanel != null)
        {
            if (valid)
            {
                //JOptionPane.showMessageDialog(null, "Loging Successfully");
                loginPanel.setVisible(false);
                new CollaborativeDesk().setVisible(true);
                DeskManager.getIntance().setUserID(userID);
            }
            else
            {
                JOptionPane.showMessageDialog(null, "Invalid User Name / Password", "Loging Fail", JOptionPane.ERROR_MESSAGE);
            } 
        } 
    }

    public void setLoginPanel(LoginPanel loginPanel)
    {
        this.loginPanel = loginPanel;
    }
    
    private void onMemberListUpdateReceived(int clientID, String encDetails)
    {
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "MemberListUpdateReceived");    
        
        String plainDetail = SecurityManager.getIntance().decryptDetails(Utils.getByteArray(encDetails), SecurityManager.getIntance().getPrivateKey());
        if (plainDetail != null)
        {
            for (String userString : plainDetail.split(LIST_SEPARATOR))
            {
                String[] userInfo = userString.split(MESSAGE_SEPARATOR);
                User user = new User(userInfo[0], userInfo[1], userInfo[2]);

                DeskManager.getIntance().getUserList().add(user);
            }

            PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.MEMBER_LIST_PANEL).update();
        }
        else
        {
            Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Invalid MemberListUpdateReceived : Decryption Fail");    
        }
    }
    
    private void onTaskListUpdateReceived(int clientID, String encDetails)
    {
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "TaskListUpdateReceived");    
        
        String plainDetail = SecurityManager.getIntance().decryptDetails(Utils.getByteArray(encDetails), SecurityManager.getIntance().getPrivateKey());
        if (plainDetail != null)
        {
            for (String taskString : plainDetail.split(LIST_SEPARATOR))
            {
                Task task = new Task(taskString);
                DeskManager.getIntance().addTask(task);
            }

            PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.TASK_LIST_PANEL).update();
        }
        else
        {
            Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Invalid TaskListUpdateReceived : Decryption Fail");    
        }
    }
    
    public void sendTaskList()
    {
        StringBuilder sb = new StringBuilder();
        for (Task task : DeskManager.getIntance().getTaskList())
        {
            sb.append(task.serializedString()).append(LIST_SEPARATOR);
        }
        
        String taskDetails = sb.toString();
        byte[] content = SecurityManager.getIntance().encryptDetails(taskDetails, 
                    SecurityManager.getIntance().getServersPublicKey());
        
        String result = TASK_LIST_UPDATE + MESSAGE_SEPARATOR + Utils.getString(content);
        communicationClient.sendString(0, result);
        Logger.getLogger(ServerCommunicationManager.class.getName()).log(Level.INFO, "Task List Update Sent to : {0}", "Server");
    }

    public void stop()
    {
        if ((communicationClient != null) && (communicationClient.isConnectionAvailable(0)))
        {
            communicationClient.stopCommunication();
        }
    }
}
