package msc.ai.collaborativedesk.model.dao;

import java.util.Date;

/**
 *
 * @author Keshan De Silva
 */
public class User
{
    private boolean online;
    private String userID;
    private String userName;
    private String gender;
    private Date lastLoggin;

    public User()
    {
    }

    public User(String userID, String userName, String gender)
    {
        this.userID = userID;
        this.userName = userName;
        this.gender = gender;
    }
    
    public String getUserID()
    {
        return userID;
    }
        
    public String getDefaultPassword()
    {
        return userID;
    }
    
    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getGender()
    {
        return gender;
    }

    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public boolean isOnline()
    {
        return online;
    }

    public void setOnline(boolean online)
    {
        this.online = online;
    }

    public Date getLastLoggin()
    {
        return lastLoggin;
    }

    public void setLastLoggin(Date lastLoggin)
    {
        this.lastLoggin = lastLoggin;
    }

    @Override
    public String toString()
    {
        return userName;
    }
}
