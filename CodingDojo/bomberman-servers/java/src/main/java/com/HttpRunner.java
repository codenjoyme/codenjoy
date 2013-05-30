package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * User: sanja
 * Date: 31.05.13
 * Time: 0:44
 */
public class HttpRunner {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new HttpServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
