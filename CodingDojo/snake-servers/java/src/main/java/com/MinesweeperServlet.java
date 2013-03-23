package com;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

public class MinesweeperServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String board = req.getParameter("board");
        String answer = answer(board);
        print(board, answer);
        resp.getWriter().write(answer);
    }

    static boolean a;
    String answer(String board) {
        a = !a;
        if (a){
            return "act, " + getDirection(random(4));
        } else {
            return getDirection(random(4));
        }
    }

    private void print(String board, String answer) {
        System.out.println("--------------------------");
        System.out.println("Answer:" + answer);
        System.out.println(split(board, BOARD_SIZE));
    }

    private static final int BOARD_SIZE = 15;

    private String getDirection(int num) {
        switch (num) {
            case 0 : return "up";
            case 1 : return "down";
            case 2 : return "left";
            case 3 : return "right";
            default : return "up";
        }
    }

    private String split(String board, int size) {
        StringBuffer result = new StringBuffer();
        for (int index = 0; index < size; index++) {
            result.append(board.substring(index*size, (index + 1)*size)).append("\n");
        }
        return result.toString();
    }

    private int random(int n) {
        return new Random().nextInt(n);
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new MinesweeperServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
