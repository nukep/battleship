package battleship.clientgui;

import javax.swing.SwingUtilities;

import battleship.client.NetClientDispatcher;
import battleship.common.MessageToClient;
import battleship.netmessages.MessageNetClient;

/**
 * NetClientDispatchToSwing ensures that client messages are received on
 * Swing's Event Dispatcher thread.
 * 
 * This is necessary because the messages are used to further access
 * Swing components.
 *
 */
public class NetClientDispatchToSwing implements NetClientDispatcher {
    private MessageToClient m2c;
    
    public NetClientDispatchToSwing(MessageToClient m2c)
    {
        this.m2c = m2c;
    }

    @Override
    public void dispatch(final MessageNetClient client)
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()
            {
                client.toClient(m2c);
            }
        });
    }
}
