package hudson.plugins.jmx;

import hudson.Plugin;
import hudson.model.Hudson;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import javax.management.MBeanServer;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;

/**
 * Entry point of the plugin. This is responsible for registering listeners
 * with Hudson and create/find the MBeanServer.
 *
 * @author Renaud Bruyeron
 * @version $Id: PluginImpl.java,v 1.3 2007/01/20 07:10:24 kohsuke Exp $
 * @plugin
 */
public class PluginImpl extends Plugin {
    public static final int JMX_PORT = 9876;
    private MBeanServer server;
    JmxJobListener jjl = null;
    
    public void start() throws Exception {
        server = getJMXConnectorServer();
        jjl = new JmxJobListener(server);
        Hudson.getInstance().addListener(jjl);
    }
    
    /**
     * @see hudson.Plugin#stop()
     */
    @Override
    public void stop() throws Exception {
        Hudson.getInstance().removeListener(jjl);
        jjl.unregister();
        jjl = null;
    }
    
    private MBeanServer getJMXConnectorServer() {
        MBeanServer mbeanServer = null;
        try     {
            MBeanServer server = java.lang.management.ManagementFactory.getPlatformMBeanServer();
            LocateRegistry.createRegistry(JMX_PORT);
            JMXServiceURL url = new javax.management.remote.JMXServiceURL(
                    "service:jmx:rmi:///jndi/rmi://" + getHostName() + ":" + JMX_PORT + "/hudson");
            JMXConnectorServer connectorServer = javax.management.remote.JMXConnectorServerFactory.newJMXConnectorServer(url,
                    null,
                    server);
            
            connectorServer.start();
            mbeanServer = connectorServer.getMBeanServer();
        } catch (MalformedURLException ex) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE,
                    ex.getMessage(), ex);
        } catch (RemoteException ex) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE,
                    ex.getMessage(), ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger("global").log(java.util.logging.Level.SEVERE,
                    ex.getMessage(), ex);
        }
        return mbeanServer;
    }
    
    private static String getHostName() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostName();
    }
    
}
