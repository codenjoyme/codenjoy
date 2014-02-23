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
        String answer = answer(board);
        print(board, answer);
        resp.getWriter().write(answer);
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(split(board, BOARD_SIZE));
    }

    private static final int BOARD_SIZE = 15;

    private String split(String board, int size) {
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index*size, (index + 1)*size)).append("\n");
        }
        return result.toString();
    }

    public final static char APPLE = '☺';
    public final static char STONE = '☻';
    public final static char BODY = '○';
    public final static char TAIL = '●';
    public final static char HEAD_LEFT = '◄';
    public final static char HEAD_RIGHT = '►';
    public final static char HEAD_UP = '▲';
    public final static char HEAD_DOWN = '▼';
    public final static char WALL = '☼';
    public static final char SPACE = ' ';

    public String answer(String board) {
        return Direction.RIGHT;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new SnakeServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
