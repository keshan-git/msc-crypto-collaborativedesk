package msc.ai.collaborativedesk.ui.rendere;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.Task.Status;


/**
 *
 * @author Keshan De Silva
 */
public class TaskStatusRenderer extends DefaultTableCellRenderer
{
    JLabel label = new JLabel();

    ImageIcon notStartedIcon = new ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/New.png"));
    ImageIcon inProgressIcon = new ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/Start.PNG"));
    ImageIcon doneIcon = new ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/OK.jpg"));
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        if (value instanceof Task.Status)
        {
            if (value.equals(Status.NOT_STARTED))
            {
                label.setText("Not Started");
                label.setIcon(notStartedIcon);
            }
            else if (value.equals(Status.IN_PROGRESS))
            {
                label.setText("In Progress");
                label.setIcon(inProgressIcon);
            }
            else if (value.equals(Status.DONE))
            {
                label.setText("Done");
                label.setIcon(doneIcon);
            }
        }
        return label;
    }
}
