package net.tetris.web.controller;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.File;
import java.security.Permission;
import java.security.Policy;
import java.security.ProtectionDomain;

/**
 * User: serhiy.zelenin
 * Date: 10/3/12
 * Time: 1:32 PM
 */
@Controller
@RequestMapping("/external")
public class ExternalAppController {

    @RequestMapping(value = "/deploy", method = RequestMethod.GET)
    public void deploy() {
        Server server = new Server();

        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(12345);

        final WebAppContext webContext = new WebAppContext(new File("c:\\myApp.war").getAbsolutePath(),"");
        Resource.setDefaultUseCaches(false);
        webContext.setCopyWebDir(false);
        webContext.setCopyWebInf(false);
        webContext.setTempDirectory(new File(System.getProperty("user.home", "myApp")));
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
    }
}
