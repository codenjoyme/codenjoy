package com;

import com.apofig.Direction;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class DummyBombermanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String board = req.getParameter("board");
        resp.getWriter().write(answer(board));
    }

    String answer(String board) {
        return "act, " + Direction.valueOf(new Random().nextInt(4));
    }

    public static void main(String[] args) throws Exception {
        for (int port = 8885; port <= 8909; port++) {
            startServerOn(port);
        }
    }

    private static void startServerOn(int port) throws Exception {
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new DummyBombermanServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
