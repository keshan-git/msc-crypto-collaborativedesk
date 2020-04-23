package msc.ai.collaborativedesk.core.managers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class DatabaseManager
{
    private static DatabaseManager instance;
    private Connection connect;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet = null;
    public static final String CONNECTION_STRING = "jdbc:mysql://localhost/cdesk?user=keshan&password=keshan321";
    
    public static DatabaseManager getIntance()
    {
        if (instance == null)
        {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    private DatabaseManager()
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addUser(User user)
    {
        try
        {
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement(
                                "INSERT INTO cdesk.user (user_id, user_name, gender, password) values (?, ?, ?, ?)");
            preparedStatement.setString(1, user.getUserID());
            preparedStatement.setString(2, user.getUserName());
            preparedStatement.setString(3, user.getGender());
            
            String passwordHash = SecurityManager.getIntance().generatePasswordHash(user, user.getDefaultPassword());
            preparedStatement.setString(4, passwordHash);  
            
            preparedStatement.executeUpdate();
            close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public ArrayList<User> getUsers()
    {
        ArrayList<User> userList = new ArrayList<>();
        
        try
        {  
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement("SELECT * FROM cdesk.user");
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                User user = new User();
                user.setUserID(resultSet.getString("user_id"));
                user.setUserName(resultSet.getString("user_name"));
                user.setGender(resultSet.getString("gender"));
                user.setLastLoggin(resultSet.getDate("last_loggin"));
                userList.add(user);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return userList;
    }
    
    public void addTask(String taskDetails)
    {
        try
        {
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement("INSERT INTO cdesk.task (task) values (?)");
            preparedStatement.setString(1, taskDetails);
            preparedStatement.executeUpdate();
            close();
        }
        catch (SQLException ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
    
    public ArrayList<Task> getTask()
    {
        ArrayList<Task> taskList = new ArrayList<>();
        try
        {  
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement("SELECT task FROM cdesk.task");
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                String encTask = resultSet.getString("task");
                Task task = SecurityManager.getIntance().deccryptTaskDetails(encTask, SecurityManager.getIntance().getServersPrivateKey());
                taskList.add(task);   
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return taskList;
    }
    
    public boolean authenticateUser(String userID, String hashPassword)
    {
        try
        {  
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement("SELECT password FROM cdesk.user WHERE user_id='" + userID + "'");
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next())
            {
                String dbPassword = resultSet.getString("password");
                return dbPassword.equals(hashPassword);
            }
        }
        catch (Exception ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    private void close()
    {
        try
        {
            if (resultSet != null)
            {
                resultSet.close();
            }

            if (connect != null)
            {
                connect.close();
            }
        }
        catch (Exception e)
        {

        }
    }

    public void clearTasks()
    {
        try
        {  
            connect = DriverManager.getConnection(CONNECTION_STRING);
            preparedStatement = connect.prepareStatement("TRUNCATE TABLE cdesk.task");
            preparedStatement.executeUpdate();
        }
        catch (Exception ex)
        {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
