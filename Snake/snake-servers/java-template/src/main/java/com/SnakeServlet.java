package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class SnakeServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
        String board = req.getParameter("board");
        board = split(board, BOARD_SIZE);
        System.out.println(board);
        resp.getWriter().write(answer(board));
    }

    private static final int BOARD_SIZE = 15;

    private String split(String board, int size) {
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index*size, (index + 1)*size)).append("\n");
        }
        return result.toString();
    }

    private static boolean a;
    String answer(String board) {
        a = !a;
        return (a)?Direction.RIGHT:Direction.LEFT;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new SnakeServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
