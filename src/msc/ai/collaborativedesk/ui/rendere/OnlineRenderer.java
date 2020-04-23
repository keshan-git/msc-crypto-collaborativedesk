package msc.ai.collaborativedesk.ui.rendere;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 *
 * @author Keshan De Silva
 */
public class OnlineRenderer extends DefaultTableCellRenderer
{
    JLabel label = new JLabel();

    ImageIcon onlineIcon = new ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/online.png"));
    ImageIcon offlineIcon = new ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/offline.png"));
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                   boolean hasFocus, int row, int column)
    {
        if (value instanceof Boolean)
        {
            if (value.equals(Boolean.TRUE))
            {
                label.setText("Online");
                label.setIcon(onlineIcon);
            }
            else
            {
                label.setText("Offline");
                label.setIcon(offlineIcon);
            }
        }
        return label;
    }
}
