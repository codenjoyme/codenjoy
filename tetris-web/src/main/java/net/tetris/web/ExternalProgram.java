package net.tetris.web;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.io.File;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

public class ExternalProgram {

    private Server server;
    private int port;
    private ServletContext servletContext;
    private WebApplicationContext applicationContext;

    private static final long DEFAULT_TIMEOUT = 1000;
    private String serverUrl;


    public static void main(String[] args) throws Exception {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(12345);

        final WebAppContext webContext = new WebAppContext(new File("D:\\workspace\\projects\\tetris-servers\\tetris-servers\\java\\target\\tetris-jetty-1.0-SNAPSHOT.war").getAbsolutePath(),"");
        Resource.setDefaultUseCaches(false);
        webContext.setCopyWebDir(false);
        webContext.setCopyWebInf(false);
        webContext.setTempDirectory(new File(System.getProperty("user.home", "tetris")));
        webContext.setExtractWAR(false);
        server.addConnector(connector);
        server.setHandler(webContext);

        Policy.setPolicy(new Policy() {
            @Override
            public boolean implies(ProtectionDomain domain, Permission permission) {
                boolean calledByWebapp = webContext.getClassLoader() == domain.getClassLoader();
                return !(calledByWebapp &&
                        RuntimePermission.class.equals(permission.getClass()) &&
                        permission.getName().startsWith("exitVM"));
            }
        });
        System.setSecurityManager(new SecurityManager());

        try {
            server.start();
            int localPort = server.getConnectors()[0].getLocalPort();
            System.out.println("localPort = " + localPort);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Test reading properties w & w/out security manager */

        String s;

        try {


            System.out.println("About to get os.name property value");

            s = System.getProperty("os.name", "not specified");
            System.out.println("  The name of your operating system is: " + s);

            System.out.println("About to get java.version property value");

            s = System.getProperty("java.version", "not specified");
            System.out.println("  The version of the JVM you are running is: " + s);

            System.out.println("About to get user.home property value");

            s = System.getProperty("user.home", "not specified");
            System.out.println("  Your user home directory is: " + s);

            System.out.println("About to get java.home property value");

            s = System.getProperty("java.home", "not specified");
            System.out.println("  Your JRE installation directory is: " + s);


        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }

    }

}