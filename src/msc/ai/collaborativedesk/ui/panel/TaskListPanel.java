package msc.ai.collaborativedesk.ui.panel;

import java.util.ArrayList;
import java.util.Date;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import msc.ai.collaborativedesk.core.managers.ClientCommunicationManager;
import msc.ai.collaborativedesk.core.managers.DeskManager;
import msc.ai.collaborativedesk.core.managers.PanelManager;
import msc.ai.collaborativedesk.model.dao.Task;
import msc.ai.collaborativedesk.model.dao.User;
import msc.ai.collaborativedesk.ui.model.TaskListTableModel;
import msc.ai.collaborativedesk.ui.rendere.TaskStatusRenderer;

/**
 *
 * @author Keshan De Silva
 */
public class TaskListPanel extends javax.swing.JPanel implements InternalPanel
{
    private static TaskListPanel instance;
    private TaskListTableModel tableModel;
    
    private TaskListPanel()
    {
        initComponents();
        btnDeleteTask.setVisible(false);
    }
    
    public static TaskListPanel getInstance()
    {
        if (instance == null)
        {
            instance = new TaskListPanel();
        }
        return instance;      
    }
    
    @Override
    public void init()
    {
        tableModel = new TaskListTableModel();
        table.setModel(tableModel);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
        {
            @Override
            public void valueChanged(ListSelectionEvent event)
            {
                boolean selected = table.getSelectedRow() != -1;
                
                if (selected)
                {
                    Task task = tableModel.getTaskList().get(table.getSelectedRow());
                    boolean assign = task.getAssignTo().getUserID().equals(DeskManager.getIntance().getUserID());
                    
                    if (assign)
                    {
                        if (task.getStatus().equals(Task.Status.NOT_STARTED))
                        {
                            btnStartTask.setEnabled(true);
                            btnDoneTask.setEnabled(false);
                        }
                        else if (task.getStatus().equals(Task.Status.IN_PROGRESS))
                        {
                            btnStartTask.setEnabled(false);
                            btnDoneTask.setEnabled(true);
                        }
                        else
                        {
                            btnStartTask.setEnabled(false);
                            btnDoneTask.setEnabled(false);
                        }
                    }
                }
                else
                {
                    btnStartTask.setEnabled(false);
                    btnDoneTask.setEnabled(false);
                }
            }
        });
        table.getColumnModel().getColumn(0).setCellRenderer(new TaskStatusRenderer());
    }

    @Override
    public void update()
    {
        DeskManager deskManager = DeskManager.getIntance();
        ArrayList<Task> taskList = deskManager.getTaskList();
        tableModel.setTaskList(taskList);   
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        btnCreateTask = new javax.swing.JButton();
        btnDeleteTask = new javax.swing.JButton();
        btnStartTask = new javax.swing.JButton();
        btnDoneTask = new javax.swing.JButton();
        btnExportTask = new javax.swing.JButton();

        setBorder(javax.swing.BorderFactory.createTitledBorder("Task Details"));

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][]
            {
                {"Not Started", "Create Documentt", "--None--", "Keshan", "2015-04-29", "--", "2015-05-02"},
                {"Not Started", "Implementation - C++", "--None--", "Isuru", "2015-04-29", "--", "2015-05-02"},
                {"In Progress", "Analize Requirement", "--None--", "Isuru", "2015-04-29", "2015-04-29", "2015-04-30"},
                {"Not Started", "Implementation - Java", "--None--", "Keshan", "2015-04-29", "--", "2015-05-02"}
            },
            new String []
            {
                "Status", "Title", "Description", "Assign To", "Create Date", "Start Date", "End Date"
            }
        ));
        jScrollPane1.setViewportView(table);

        btnCreateTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/New.png"))); // NOI18N
        btnCreateTask.setText("New Task");
        btnCreateTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnCreateTask.setMaximumSize(new java.awt.Dimension(110, 25));
        btnCreateTask.setMinimumSize(new java.awt.Dimension(110, 25));
        btnCreateTask.setPreferredSize(new java.awt.Dimension(110, 25));
        btnCreateTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnCreateTaskActionPerformed(evt);
            }
        });

        btnDeleteTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/delete.png"))); // NOI18N
        btnDeleteTask.setText("Delete Task");
        btnDeleteTask.setEnabled(false);
        btnDeleteTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnDeleteTask.setMaximumSize(new java.awt.Dimension(110, 25));
        btnDeleteTask.setMinimumSize(new java.awt.Dimension(110, 25));
        btnDeleteTask.setPreferredSize(new java.awt.Dimension(110, 25));
        btnDeleteTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDeleteTaskActionPerformed(evt);
            }
        });

        btnStartTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/Start.PNG"))); // NOI18N
        btnStartTask.setText("Start Task");
        btnStartTask.setEnabled(false);
        btnStartTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnStartTask.setMaximumSize(new java.awt.Dimension(110, 25));
        btnStartTask.setMinimumSize(new java.awt.Dimension(110, 25));
        btnStartTask.setPreferredSize(new java.awt.Dimension(110, 25));
        btnStartTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnStartTaskActionPerformed(evt);
            }
        });

        btnDoneTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/OK.jpg"))); // NOI18N
        btnDoneTask.setText("Done Task");
        btnDoneTask.setEnabled(false);
        btnDoneTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnDoneTask.setMaximumSize(new java.awt.Dimension(110, 25));
        btnDoneTask.setMinimumSize(new java.awt.Dimension(110, 25));
        btnDoneTask.setPreferredSize(new java.awt.Dimension(110, 25));
        btnDoneTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnDoneTaskActionPerformed(evt);
            }
        });

        btnExportTask.setIcon(new javax.swing.ImageIcon(getClass().getResource("/msc/ai/collaborativedesk/resources/Export.jpg"))); // NOI18N
        btnExportTask.setText("Refresh");
        btnExportTask.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        btnExportTask.setMaximumSize(new java.awt.Dimension(110, 25));
        btnExportTask.setMinimumSize(new java.awt.Dimension(110, 25));
        btnExportTask.setPreferredSize(new java.awt.Dimension(110, 25));
        btnExportTask.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(java.awt.event.ActionEvent evt)
            {
                btnExportTaskActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jSeparator1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCreateTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnStartTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDoneTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 163, Short.MAX_VALUE)
                        .addComponent(btnExportTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 176, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCreateTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDeleteTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnStartTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDoneTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnExportTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnStartTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnStartTaskActionPerformed
    {//GEN-HEADEREND:event_btnStartTaskActionPerformed
        Task task = tableModel.getTaskList().get(table.getSelectedRow());
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setStartDate(new Date());
        tableModel.fireTableDataChanged();
        
        ClientCommunicationManager.getIntance().sendTaskList();
    }//GEN-LAST:event_btnStartTaskActionPerformed

    private void btnDoneTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDoneTaskActionPerformed
    {//GEN-HEADEREND:event_btnDoneTaskActionPerformed
        Task task = tableModel.getTaskList().get(table.getSelectedRow());
        task.setStatus(Task.Status.DONE);
        task.setEndDate(new Date());
        tableModel.fireTableDataChanged();
        
        ClientCommunicationManager.getIntance().sendTaskList();
    }//GEN-LAST:event_btnDoneTaskActionPerformed

    private void btnExportTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnExportTaskActionPerformed
    {//GEN-HEADEREND:event_btnExportTaskActionPerformed
        PanelManager.getInstance().getPanel(PanelManager.InternalPanelID.TASK_LIST_PANEL).update();
    }//GEN-LAST:event_btnExportTaskActionPerformed

    private void btnCreateTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnCreateTaskActionPerformed
    {//GEN-HEADEREND:event_btnCreateTaskActionPerformed
        PanelManager.getInstance().showPanel(PanelManager.InternalPanelID.NEW_TASK_PANEL, true, true);
    }//GEN-LAST:event_btnCreateTaskActionPerformed

    private void btnDeleteTaskActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_btnDeleteTaskActionPerformed
    {//GEN-HEADEREND:event_btnDeleteTaskActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteTaskActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCreateTask;
    private javax.swing.JButton btnDeleteTask;
    private javax.swing.JButton btnDoneTask;
    private javax.swing.JButton btnExportTask;
    private javax.swing.JButton btnStartTask;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}