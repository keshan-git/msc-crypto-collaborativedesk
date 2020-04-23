package msc.ai.collaborativedesk.model.dao;

import java.util.Date;
import java.util.Objects;
import msc.ai.collaborativedesk.core.managers.ClientCommunicationManager;
import msc.ai.collaborativedesk.core.managers.DeskManager;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.communication.StringSerializable;

/**
 *
 * @author Keshan De Silva
 */
public class Task implements StringSerializable<Task>
{
    public enum Status { NOT_STARTED, IN_PROGRESS, DONE };
            
    private Date createdDate;
    private User createdBy;
    private String title;
    private String description;
    private User assignTo;
    private Date startDate;
    private Date endDate;
    private Status status;

    public Task()
    {

    }

    public Task(String serializedString)
    {
        String components[] = serializedString.split(ClientCommunicationManager.PARAM_SEPARATOR);
        this.createdDate = Utils.getDate(components[0]);
        this.createdBy = DeskManager.getIntance().getUser(components[1]);
        this.title = components[2];
        this.description = components[3];
        this.assignTo = DeskManager.getIntance().getUser(components[4]);
        this.startDate = Utils.getDate(components[5]);
        this.endDate = Utils.getDate(components[6]);
        this.status = Status.values()[Integer.parseInt(components[7])];
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public User getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(User createdBy)
    {
        this.createdBy = createdBy;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public User getAssignTo()
    {
        return assignTo;
    }

    public void setAssignTo(User assignTo)
    {
        this.assignTo = assignTo;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public Status getStatus()
    {
        return status;
    }

    public void setStatus(Status status)
    {
        this.status = status;
    }
    
    @Override
    public String serializedString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Utils.getDateString(createdDate)).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(createdBy.getUserID()).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(title).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(description).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(assignTo.getUserID()).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(Utils.getDateString(startDate)).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(Utils.getDateString(endDate)).append(ClientCommunicationManager.PARAM_SEPARATOR);
        stringBuilder.append(status.ordinal());  
        return stringBuilder.toString();
    }

    @Override
    public int hashCode()
    {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.title);
        return hash;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        final Task other = (Task) obj;
        return Objects.equals(this.title, other.title);
    }
}
