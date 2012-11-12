package net.tetris.online.service;

import org.eclipse.jetty.server.AbstractConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * User: serhiy.zelenin
 * Date: 10/17/12
 * Time: 11:45 AM
 */
public class WebApp {
    private static Logger logger = LoggerFactory.getLogger(WebApp.class);

    private File appFile;
    private ServiceConfiguration configuration;
    private Server server;
    private WebAppContext webContext;

    public WebApp(File appFile, ServiceConfiguration configuration) {
        this.appFile = appFile;
        this.configuration = configuration;
    }

    public int deploy() {
        try {
            logger.info("Deploying war {}", appFile.getAbsolutePath());
            server = new Server();

//        SelectChannelConnector connector = new SelectChannelConnector();
            AbstractConnector connector = new SocketConnector();
//        connector.setPort(12345);

            webContext = new WebAppContext(appFile.getAbsolutePath(), "");
            Resource.setDefaultUseCaches(false);
            webContext.setCopyWebDir(true);
            webContext.setCopyWebInf(true);
            File tmpDir = new File(configuration.getTmpDir(), appFile.getName());
            tmpDir.mkdirs();
            logger.info("War Temp Directory {}", tmpDir.getAbsolutePath());
            webContext.setTempDirectory(tmpDir);
            webContext.setExtractWAR(true);
//            webContext.setClassLoader(new WebAppClassLoader(System.class.getClassLoader(), webContext));
//        webContext.setAttribute("org.eclipse.jetty.servlet.Default.useFileMappedBuffer", false);
            server.addConnector(connector);
            server.setHandler(webContext);
            Policy.setPolicy(new Policy() {
                private RestrictedPermissions restrictions = new RestrictedPermissions();

                @Override
                public boolean implies(ProtectionDomain domain, Permission permission) {
                    boolean calledByWebapp = webContext.getClassLoader() == domain.getClassLoader();
                    return !calledByWebapp || restrictions.implyAny(permission);
//                    return true;
                }
            });
            System.setSecurityManager(new SecurityManager());
            server.start();
            int localPort = server.getConnectors()[0].getLocalPort();
            logger.info("Webapp started on port {}", localPort);

            return localPort;
        } catch (Exception e) {
            logger.error("Unable to deploy application: " + appFile.getAbsolutePath(), e);
            throw new RuntimeException(e);
        }
    }

    public void shutDown() {
        try {
            webContext.stop();
            webContext.destroy();
            server.stop();
            server.destroy();
        } catch (Exception e) {
            logger.error("Unable to stop Jetty server! Application: " + appFile.getAbsolutePath(), e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        File file = new File("C:\\Users\\serhiy.zelenin\\AppData\\Local\\Temp\\.tetris\\vasya.war");
        WebApp webApp = new WebApp(file, new ServiceConfiguration());
        webApp.deploy();
        Thread.sleep(10000);
        webApp.shutDown();
//        boolean delete = file.delete();
    }
}
