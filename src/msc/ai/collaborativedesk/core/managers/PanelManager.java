package msc.ai.collaborativedesk.core.managers;

import java.awt.BorderLayout;
import java.util.HashMap;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import msc.ai.collaborativedesk.model.dao.User;
import msc.ai.collaborativedesk.ui.panel.CreateTaskPanel;
import msc.ai.collaborativedesk.ui.panel.CreateUserPanel;
import msc.ai.collaborativedesk.ui.panel.InternalPanel;
import msc.ai.collaborativedesk.ui.panel.TaskListPanel;
import msc.ai.collaborativedesk.ui.panel.TeamChatPanel;
import msc.ai.collaborativedesk.ui.panel.TeamMembersPanel;
import msc.ai.collaborativedesk.ui.panel.UserListPanel;

/**
 *
 * @author Keshan De Silva
 */
public final class PanelManager
{
    private static PanelManager instance;

    public enum InternalPanelID
    {
        NEW_TASK_PANEL, TASK_LIST_PANEL,
        NEW_USER_PANEL, USER_LIST_PANEL,
        MEMBER_LIST_PANEL
    };
    
    private HashMap<InternalPanelID, JInternalFrame> frameMap = new HashMap<>();
    private HashMap<InternalPanelID, InternalPanel> panelMap = new HashMap<>();
    private HashMap<User, TeamChatPanel> chatPanelMap = new HashMap<>();
    private HashMap<User, JInternalFrame> chatFrameMap = new HashMap<>();
        
    private JDesktopPane desktopPane;
    
    public static PanelManager getInstance()
    {
        if (instance == null)
        {
            instance = new PanelManager();
        }
        return instance;
    }
    
    public void init(JDesktopPane desktopPane)
    {
        this.desktopPane = desktopPane;
        
        JInternalFrame newTaskFrame = new JInternalFrame("Create Task", true, true, true, true );
        newTaskFrame.setSize(400, 200);
        newTaskFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        CreateTaskPanel createTaskPanel = CreateTaskPanel.getInstance();
        createTaskPanel.init();
        newTaskFrame.setLayout(new BorderLayout());
        newTaskFrame.add(createTaskPanel, BorderLayout.CENTER);
        desktopPane.add(newTaskFrame);
        frameMap.put(InternalPanelID.NEW_TASK_PANEL, newTaskFrame);
        panelMap.put(InternalPanelID.NEW_TASK_PANEL, createTaskPanel);
        
        JInternalFrame taskFrame = new JInternalFrame("Task Details", true, true, true, true );
        taskFrame.setSize(700, 400);
        taskFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        TaskListPanel taskListPanel = TaskListPanel.getInstance();
        taskListPanel.init();
        taskFrame.setLayout(new BorderLayout());
        taskFrame.add(taskListPanel, BorderLayout.CENTER);
        desktopPane.add(taskFrame);
        frameMap.put(InternalPanelID.TASK_LIST_PANEL, taskFrame);
        panelMap.put(InternalPanelID.TASK_LIST_PANEL, taskListPanel);
        
        JInternalFrame newUserFrame = new JInternalFrame("Create User", true, true, true, true );
        newUserFrame.setSize(400, 200);
        newUserFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        CreateUserPanel createUserPanel = CreateUserPanel.getInstance();
        createUserPanel.init();
        newUserFrame.setLayout(new BorderLayout());
        newUserFrame.add(createUserPanel, BorderLayout.CENTER);
        desktopPane.add(newUserFrame);
        frameMap.put(InternalPanelID.NEW_USER_PANEL, newUserFrame);
        panelMap.put(InternalPanelID.NEW_USER_PANEL, createUserPanel);
        
        JInternalFrame userFrame = new JInternalFrame("User Details", true, true, true, true );
        userFrame.setSize(700, 400);
        userFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        UserListPanel userListPanel = UserListPanel.getInstance();
        userListPanel.init();
        userFrame.setLayout(new BorderLayout());
        userFrame.add(userListPanel, BorderLayout.CENTER);
        desktopPane.add(userFrame);
        frameMap.put(InternalPanelID.USER_LIST_PANEL, userFrame);
        panelMap.put(InternalPanelID.USER_LIST_PANEL, userListPanel);
        
        JInternalFrame memberFrame = new JInternalFrame("Team Members Details", true, true, true, true );
        memberFrame.setSize(700, 400);
        memberFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        TeamMembersPanel teamMembersPanel = TeamMembersPanel.getInstance();
        teamMembersPanel.init();
        memberFrame.setLayout(new BorderLayout());
        memberFrame.add(teamMembersPanel, BorderLayout.CENTER);
        desktopPane.add(memberFrame);
        frameMap.put(InternalPanelID.MEMBER_LIST_PANEL, memberFrame);
        panelMap.put(InternalPanelID.MEMBER_LIST_PANEL, teamMembersPanel);
    }
    
    public TeamChatPanel getChatPanel(User selectedUser, User user)
    {
        if (chatPanelMap.get(selectedUser) == null)
        {
            TeamChatPanel chatPanel = new TeamChatPanel(user, selectedUser);
            chatPanelMap.put(selectedUser, chatPanel);
            
            JInternalFrame chatFrame = new JInternalFrame("Team Members Chat", true, true, true, true );
            chatFrame.setSize(700, 400);
            chatFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            chatPanel.init();
            chatFrame.setLayout(new BorderLayout());
            chatFrame.add(chatPanel, BorderLayout.CENTER);
            desktopPane.add(chatFrame);
            chatFrameMap.put(user, chatFrame);
        }
        return chatPanelMap.get(selectedUser);
    }
    
    public JInternalFrame getChatFrame(User selectedUser, User user)
    {
        return chatFrameMap.get(selectedUser);
    }
        
    public InternalPanel getPanel(InternalPanelID id)
    {
        if (panelMap.containsKey(id))
        {
            return panelMap.get(id);
        }
        
        return null;
    }
    
    public void showPanel(InternalPanelID id, boolean show)
    {
        if (frameMap.containsKey(id))
        {
            frameMap.get(id).setVisible(show);
        }
    }
    
    public void showPanel(InternalPanelID id, boolean show, boolean initialize)
    {
        if (frameMap.containsKey(id))
        {
            if (initialize)
            {
                panelMap.get(id).init();
            }
            frameMap.get(id).setVisible(show);
        }
    }
}
