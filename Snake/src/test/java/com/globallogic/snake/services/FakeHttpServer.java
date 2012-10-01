package com.globallogic.snake.services;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 4:31 AM
 */
public class FakeHttpServer {

    private String response;
    private StringWriter requestWriter = new StringWriter();
    private Server jettyServer = new Server();
    private Map<String, String[]> parameters;
    private int port;
    private ReentrantLock lock = new ReentrantLock();
    private Condition requestProcessed = lock.newCondition();

    public FakeHttpServer(int port) throws IOException {
        this.port = port;
    }

    public void start() throws Exception {
        SelectChannelConnector connector = new SelectChannelConnector();
        connector.setPort(port);
        jettyServer.addConnector(connector);
        ServletContextHandler root = new ServletContextHandler(jettyServer, "/");
        root.addServlet(new ServletHolder(new HttpServlet(){
            @Override
            protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                lock.lock();
                try {
                    parameters = req.getParameterMap();
                    resp.getWriter().print(response);
                    requestProcessed.signal();
                } finally {
                    lock.unlock();
                }
            }
        }), "/*");

        jettyServer.start();
    }

    public void willResponse(String response) {
        this.response = response;
    }

    public void stop() throws Exception {
        jettyServer.stop();
    }

    public String getRequestParameter(String name) {
        return parameters.get(name)[0];
    }

    public void waitForRequest() throws InterruptedException {
        lock.lock();
        try {
            while (parameters == null) {
                requestProcessed.await();
            }
        } finally {
            lock.unlock();
        }

    }
}
