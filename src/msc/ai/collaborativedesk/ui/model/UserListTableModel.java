package msc.ai.collaborativedesk.ui.model;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class UserListTableModel extends AbstractTableModel
{
    private ArrayList<User> userList = new ArrayList<>();
    private final String[] columnNames = new String[]
            {"Status", "User ID", "User Name", "Gender", "Last Loggin"};
    private final Class[] columnClasses = new Class[]
            {Boolean.class, String.class, String.class, String.class, Date.class};
        
    @Override
    public int getRowCount()
    {
        return userList.size();
    }

    @Override
    public int getColumnCount()
    {
        return columnNames.length;
    }

    @Override
    public Class<?> getColumnClass(int column)
    {
        return columnClasses[column];
    }

    @Override
    public String getColumnName(int column)
    {
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int row, int column)
    {
        User user = userList.get(row);
        switch (column)
        {
            case 0 : return user.isOnline();
            case 1 : return user.getUserID();
            case 2 : return user.getUserName();
            case 3 : return user.getGender();
            case 4 : return user.getLastLoggin();
        }
        return "-";
    }
    
    public void setUserList(ArrayList<User> userList)
    {
        this.userList = userList;
        fireTableDataChanged();
    }
        
    public void addUser(User user)
    {
        if (userList == null)
        {
            userList = new ArrayList<>();
        }
        userList.add(user);
        fireTableDataChanged();
    }
    
    public ArrayList<User> getUserList()
    {
        return userList;
    }
}
