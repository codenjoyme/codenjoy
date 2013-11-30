package com.codenjoy.dojo;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 11/18/12
 * Time: 4:47 PM
 */
public class JettyRunner {

    private ServletContext servletContext;
    protected WebApplicationContext applicationContext;
    private List<SpringContextInitEvent> springContextInitListeners = new LinkedList<SpringContextInitEvent>();
    private String[] webContextPlaces;

    private Server server;

    public JettyRunner(String... webContextPlaces) {
        this.webContextPlaces = webContextPlaces;
    }

    public void addSpringContextInitListener(SpringContextInitEvent listener) {
        this.springContextInitListeners.add(listener);
    }

    interface SpringContextInitEvent {
        void contextInit(WebApplicationContext context);
    }

    public int start(int port) throws Exception {
        stop();

        server = new Server(port);
        final WebAppContext context = loadWebContext();
        context.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                servletContext = sce.getServletContext();

                context.addEventListener(new ServletContextListener() {
                    @Override
                    public void contextInitialized(ServletContextEvent sce) {
                        applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

                        for (SpringContextInitEvent listener : springContextInitListeners) {
                            listener.contextInit(applicationContext);
                        }
                    }

                    @Override
                    public void contextDestroyed(ServletContextEvent sce) {
                    }
                });
            }

            @Override
            public void contextDestroyed(ServletContextEvent sce) {
            }
        });
        server.setHandler(context);
        server.start();
        return server.getConnectors()[0].getLocalPort();
    }

    private  WebAppContext loadWebContext() throws IOException {
        Collection<String> urls = Arrays.asList(webContextPlaces);
        for (String url : urls) {
            WebAppContext context = new WebAppContext(url, "");
            Resource resource = context.newResource(context.getWar());
            if (resource.exists()) {
                return context;
            }
        }
        throw new RuntimeException("Webapp not found!");
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    public static void main(String[] args) throws Exception {
        JettyRunner runner = new JettyRunner("src/main/webapp");
        int port = runner.start(8080);

        String url = "http://localhost:" + port + "/admin31415";
        System.out.println(url);
    }
}
