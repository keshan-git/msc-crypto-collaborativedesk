package msc.ai.collaborativedesk.ui.model;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class TaskListTableModel extends AbstractTableModel
{
    private ArrayList<Task> taskList = new ArrayList<>();
    private final String[] columnNames = new String[]
            {"Status", "Title", "Description", "Created By", "Assign To", "Created Date", "Start Date", "End Date"};
    private final Class[] columnClasses = new Class[]
            {String.class, String.class, String.class, User.class, User.class, Date.class, Date.class, Date.class};
        
    @Override
    public int getRowCount()
    {
        return taskList.size();
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
        Task task = taskList.get(row);
        switch (column)
        {
            case 0 : return task.getStatus();
            case 1 : return task.getTitle();
            case 2 : return task.getDescription();
            case 3 : return task.getCreatedBy();
            case 4 : return task.getAssignTo();
            case 5 : return task.getCreatedDate();
            case 6 : return task.getStartDate();
            case 7: return task.getEndDate();
        }
        return "-";
    }
    
    public void setTaskList(ArrayList<Task> taskList)
    {
        this.taskList = taskList;
        fireTableDataChanged();
    }
        
    public void addTask(Task task)
    {
        if (taskList == null)
        {
            taskList = new ArrayList<>();
        }
        taskList.add(task);
        fireTableDataChanged();
    }
    
    public ArrayList<Task> getTaskList()
    {
        return taskList;
    }
}
