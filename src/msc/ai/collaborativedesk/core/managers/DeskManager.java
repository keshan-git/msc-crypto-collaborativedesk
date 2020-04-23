package msc.ai.collaborativedesk.core.managers;

import java.util.ArrayList;
import msc.ai.collaborativedesk.model.dao.CollaborativeDeskData;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public final class DeskManager
{
    private CollaborativeDeskData deskDate = new CollaborativeDeskData();
    private ArrayList<User> userList = new ArrayList<>();
    private String userID;
    private static DeskManager instance;
    
    public static DeskManager getIntance()
    {
        if (instance == null)
        {
            instance = new DeskManager();
        }
        return instance;
    }

    public String getUserID()
    {
        return userID;
    }

    public User getUser()
    {
        for (User user : userList)
        {
            if (user.getUserID().equals(this.userID))
            {
                return user;
            }
        }
        return null;
    }
        
    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public void addTask(Task task)
    {
        for (Task t : DeskManager.getIntance().getTaskList())
        {
            if (t.getTitle().equals(task.getTitle()))
            {
                deskDate.getTaskList().remove(task);
            }
        }
        
        deskDate.getTaskList().add(task);
        PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.TASK_LIST_PANEL).update();
    }
    
    public void deleteTask(Task task)
    {
        deskDate.getTaskList().remove(task);
        PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.TASK_LIST_PANEL).update();
    }
    
    public ArrayList<Task> getTaskList()
    {
        return deskDate.getTaskList();
    }
     
    public void addUser(User user)
    {
        userList.add(user);
        DatabaseManager.getIntance().addUser(user);
        PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.USER_LIST_PANEL).update();
    }
    
    public void syncUserList(ArrayList<User> userList)
    {
        this.userList = userList;
        PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.USER_LIST_PANEL).update();
    }
    
    public void syncTaskList(ArrayList<Task> taskList)
    {
        this.deskDate.setTaskList(taskList);
    }
        
    public ArrayList<User> getUserList()
    {
        return userList;
    }
    
    public User getUser(String userID)
    {
        for (User user : getUserList())
        {
            if (user.getUserID().equals(userID))
            {
                return user;
            }
        }
        
        return null;
    }
}
