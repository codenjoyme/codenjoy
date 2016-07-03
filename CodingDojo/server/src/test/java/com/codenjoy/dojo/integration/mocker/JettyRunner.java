package com.codenjoy.dojo.integration.mocker;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class JettyRunner {

    private ServletContext servletContext;
    protected WebApplicationContext applicationContext;
    private List<SpringContextInitEvent> springContextInitListeners = new LinkedList<SpringContextInitEvent>();
    private List<ServletContextInitEvent> servletContextInitListeners = new LinkedList<ServletContextInitEvent>();
    private String webApp;
    private String contextPath;
    private int port;

    private Server server;

    public JettyRunner(String webApp, String contextPath) {
        this.webApp = webApp;
        this.contextPath = contextPath;
    }

    public void addListener(SpringContextInitEvent listener) {
        this.springContextInitListeners.add(listener);
    }

    public void addListener(ServletContextInitEvent listener) {
        this.servletContextInitListeners.add(listener);
    }

    public Server getServer() {
        return server;
    }

    interface SpringContextInitEvent {
        void contextInit(WebApplicationContext context);
    }

    interface ServletContextInitEvent {
        void contextInit(WebAppContext context, ServletContext servletContext);
    }

    public int start(int givenPort) throws Exception {
        stop();

        server = new Server(givenPort);
        final WebAppContext context = loadWebContext();
        context.addEventListener(new ServletContextListener() {
            @Override
            public void contextInitialized(ServletContextEvent sce) {
                servletContext = sce.getServletContext();

                for (ServletContextInitEvent listener : servletContextInitListeners) {
                    listener.contextInit(context, servletContext);
                }

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
        port = server.getConnectors()[0].getLocalPort();

        return port;
    }

    private WebAppContext loadWebContext() throws IOException {
        WebAppContext context = new WebAppContext(webApp, contextPath);
        Resource resource = context.newResource(context.getWar());
        if (resource.exists()) {
            return context;
        }
        throw new RuntimeException("Webapp not found!");
    }

    public String getContextPath() {
        return contextPath;
    }

    public String getUrl() {
        return "http://localhost:" + port + contextPath;
    }

    public void stop() throws Exception {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    public void join() {
        System.out.println(getUrl());
        try {
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
