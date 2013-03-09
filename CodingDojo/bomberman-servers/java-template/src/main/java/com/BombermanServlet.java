package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BombermanServlet extends HttpServlet {


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

    public final static char BOMBERMAN = '☺';
    public final static char BOMB_BOMBERMAN = '☻';
    public final static char DEAD_BOMBERMAN = 'Ѡ';
    public final static char BOOM = '҉';
    public final static String BOMBS = "012345";
    public final static char WALL = '☼';
    public final static char DESTROY_WALL = '#';
    public final static char MEAT_CHOPPER = '&';
    public final static char DEAD_MEAT_CHOPPER = 'x';
    public static final char SPACE = ' ';

    public String answer(String board) {
        return Direction.BOMB;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new BombermanServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
