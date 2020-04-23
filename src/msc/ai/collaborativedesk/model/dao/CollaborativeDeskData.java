package msc.ai.collaborativedesk.model.dao;

import java.util.ArrayList;

/**
 *
 * @author Keshan De Silva
 */
public final class CollaborativeDeskData
{
    private ArrayList<Task> taskList = new ArrayList<>();

    public ArrayList<Task> getTaskList()
    {
        return taskList;
    }

    public void setTaskList(ArrayList<Task> taskList)
    {
        this.taskList = taskList;
    }
   
}
