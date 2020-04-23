package msc.ai.collaborativedesk.ui.panel;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;
import msc.ai.collaborativedesk.core.managers.ClientCommunicationManager;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.MESSAGE_SEPARATOR;
import static msc.ai.collaborativedesk.core.managers.ClientCommunicationManager.NEW_TASK_ADDED;
import msc.ai.collaborativedesk.core.managers.DeskManager;
import msc.ai.collaborativedesk.core.managers.PanelManager;
import msc.ai.collaborativedesk.core.managers.ServerCommunicationManager;
import msc.ai.collaborativedesk.core.utils.Utils;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.Task.Status;
import msc.ai.collaborativedesk.model.dao.User;

/**
 *
 * @author Keshan De Silva
 */
public class CreateTaskPanel extends javax.swing.JPanel implements InternalPanel
{
    private static CreateTaskPanel instance;
    private Task task;
    
    private CreateTaskPanel()
    {
        initComponents();
    }
    
    public static CreateTaskPanel getInstance()
    {
        if (instance == null)
        {
            instance = new CreateTaskPanel();
        }
        return instance;
            
    }
    
    @Override
    public void init()
    {
        txtTitle.setText("");
        txtDescription.setText("");
        cmbAssignTo.setSelectedIndex(0);
        txtCreated.setText(new Date().toString());
        cmbAssignTo.removeAllItems();
        cmbAssignTo.addItem("--Not Assign--");
        for(User user : DeskManager.getIntance().getUserList())
        {
            cmbAssignTo.addItem(user);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTitle = new javax.swing.JTextField();
        txtDescription = new javax.swing.JTextField();
        cmbAssignTo = new javax.swing.JComboBox();
        txtCreated = new javax.swing.JFormattedTextField();
        btnCancel = new javax.swing.JButton();
        btnUpdateTask = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Create Task"));

        jLabel1.setText("Title :");

        jLabel2.setText("Description :");

        jLabel3.setText("Assign To :");

        jLabel4.setText("Created On :");

        cmbAssignTo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-- Not Assign --" }));

        txtCreated.setText("2015/04/29");
        txtCreated.setEnabled(false);

        btnCancel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/Cancel.jpg"))); // NOI18N
        btnCancel.setText("Cancel");
        btnCancel.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnCancel.setMinimumSize(new java.awt.Dimension(75, 25));
        btnCancel.setPreferredSize(new java.awt.Dimension(75, 25));
        btnCancel.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCancelActionPerformed(evt);
            }
        });

        btnUpdateTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/OK.jpg"))); // NOI18N
        btnUpdateTask.setText("Create");
        btnUpdateTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnUpdateTask.setMinimumSize(new java.awt.Dimension(75, 25));
        btnUpdateTask.setPreferredSize(new java.awt.Dimension(75, 25));
        btnUpdateTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnUpdateTaskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtTitle))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtDescription))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cmbAssignTo, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUpdateTask, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtCreated))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {jLabel1, jLabel2, jLabel3, jLabel4});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtDescription, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(cmbAssignTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtCreated, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdateTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnUpdateTaskActionPerformed
    {//GEN-HEADEREND:event_btnUpdateTaskActionPerformed
        task = new Task();
        task.setCreatedDate(new Date());
        task.setTitle(txtTitle.getText());
        task.setDescription(txtDescription.getText());
        
        if (cmbAssignTo.getSelectedItem() instanceof User)
        {
            task.setAssignTo((User)cmbAssignTo.getSelectedItem());
        }
        task.setCreatedBy(DeskManager.getIntance().getUser());
        task.setStatus(Status.NOT_STARTED);
        
        SwingWorker swingWorker = new SwingWorker()
        {
            @Override
            protected Object doInBackground() throws Exception
            {
                DeskManager.getIntance().addTask(task);
                JOptionPane.showMessageDialog(null, "New Task Added Successfully", "New Task", JOptionPane.INFORMATION_MESSAGE);
                
                ClientCommunicationManager.getIntance().sendTask(task);
                return null;
            } 
        };
        swingWorker.execute();
        PanelManager.getInstance().showPanel(PanelManager.InternalPanelID.NEW_TASK_PANEL, false);   
    }//GEN-LAST:event_btnUpdateTaskActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCancelActionPerformed
    {//GEN-HEADEREND:event_btnCancelActionPerformed
        PanelManager.getInstance().showPanel(PanelManager.InternalPanelID.NEW_TASK_PANEL, false);
    }//GEN-LAST:event_btnCancelActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnUpdateTask;
    private javax.swing.JComboBox cmbAssignTo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JFormattedTextField txtCreated;
    private javax.swing.JTextField txtDescription;
    private javax.swing.JTextField txtTitle;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update()
    {

    }
}
