package msc.ai.collaborativedesk;

import java.awt.EventQueue;
import msc.ai.collaborativedesk.core.managers.ServerCommunicationManager;
import msc.ai.collaborativedesk.ui.CollaborativeDesk;
import msc.ai.collaborativedesk.ui.CollaborativeDeskAdministrator;
import msc.ai.collaborativedesk.ui.panel.LoginPanel;

/**
 *
 * @author Keshan De Silva
 */
public class CDesk
{
    public static void main(String args[])
    {
        if ((args.length > 0) && ("server".equals(args[0])))
        {
            EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    new CollaborativeDeskAdministrator().setVisible(true);
                }
            });
            ServerCommunicationManager.getIntance().start();
        }
        else
        {
            EventQueue.invokeLater(new Runnable()
            {
                @Override
                public void run()
                {
                    new LoginPanel().setVisible(true);
                }
            });
        }

        

    }
}
