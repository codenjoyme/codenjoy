package net.tetris.services;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URI;

public class FakeHttpServer {

    private String response;
    private final HttpServer server;
    private StringWriter requestWriter = new StringWriter();

    public FakeHttpServer(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
    }

    public void start() throws IOException {
        server.createContext("/", new HttpHandler() {
            public void handle(HttpExchange exchange) throws IOException {
                URI requestURI = exchange.getRequestURI();
                System.out.println("requestURI = " + requestURI);
                PrintWriter printWriter = new PrintWriter(requestWriter);
                if (!StringUtils.isBlank(requestWriter.toString())) {
                    printWriter.print("&");
                }
                printWriter.print(IOUtils.toString(exchange.getRequestBody()));

                exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,
                        response.length());
                IOUtils.write(response, exchange.getResponseBody());
                exchange.getResponseBody().close();
            }
        });
        server.start();
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void stop() {
        server.stop(0);
    }

    public String getRequest() {
        return requestWriter.toString();
    }
}
