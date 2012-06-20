package net.tetris.services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FakeHttpServer {

    private String response;
//    private final HttpServer server;
    private StringWriter requestWriter = new StringWriter();
    private Server jettyServer = new Server();
    private Map<String, String[]> parameters;
    private int port;
    private ReentrantLock lock = new ReentrantLock();
    private Condition gotRequest = lock.newCondition();

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
                    gotRequest.signal();
                } finally {
                    lock.unlock();
                }
            }
        }), "/*");

        jettyServer.start();

/*
        server.createContext("/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                URI requestURI = exchange.getRequestURI();
                System.out.println("requestURI = " + requestURI);
                PrintWriter printWriter = new PrintWriter(requestWriter);
//                if (!StringUtils.isBlank(requestWriter.toString())) {
//                    printWriter.print("&");
//                }
//                printWriter.print(IOUtils.toString(exchange.getRequestBody()));

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
                        0);
//                IOUtils.write(response, exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
        });
        server.start();
*/
    }

    public void setResponse(String response) {
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
            gotRequest.await();
        } finally {
            lock.unlock();
        }

    }
}
