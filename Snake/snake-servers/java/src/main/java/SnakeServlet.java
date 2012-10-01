import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class SnakeServlet extends HttpServlet {

    public static final String UP = "up";
    public static final String DOWN = "down";
    public static final String LEFT = "left";
    public static final String RIGHT = "right";
    public static final int BOARD_SIZE = 15;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String board = req.getParameter("board");
        int x = Integer.parseInt(req.getParameter("x"));
        int y = Integer.parseInt(req.getParameter("y"));
        System.out.println(String.format("Snake head coordinates: (%d, %d)\n Board:\n %s", x, y, board));

        resp.getWriter().write(answer(x, y, board));
    }

    static int count = 0;

    String answer(int x, int y, String board) {
        //add "left", "right", "up" or "down" for tur Sanke

        count++;
        switch (count % 4) {
            case 0: return LEFT;
            case 1: return DOWN;
            case 2: return RIGHT;
            case 3: return UP;
        }
        return UP;
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(8888);
        ServletContextHandler context = new ServletContextHandler(server, "/");
        context.addServlet(new ServletHolder(new SnakeServlet()), "/*");
        server.setHandler(context);
        server.start();
    }
}
