package net.tetris.web.test;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * User: serhiy.zelenin
 * Date: 6/24/12
 * Time: 12:02 PM
 */
@WebServlet(name = "test", urlPatterns = "/test/")
public class TestServlet extends HttpServlet {
    private static Integer left;
    private static Integer right;
    private static Integer rotate;
    private static Boolean drop;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        left = getIntValue("left", req);
        right = getIntValue("right", req);
        rotate = getIntValue("rotate", req);
        drop = req.getParameter("drop") != null;
    }

    private Integer getIntValue(String name, HttpServletRequest req) {
        String parameter = req.getParameter(name);
        if (parameter != null) {
            return Integer.parseInt(parameter);
        }
        return null;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getParameter("test")!=null){
            left = getIntValue("left", req);
            right = getIntValue("right", req);
            rotate = getIntValue("rotate", req);
            drop = req.getParameter("drop") != null;
        }else{
            StringBuilder sb = new StringBuilder();
            if (left != null) {
                sb.append("left=").append(left).append(" ");
                left = null;
            }
            if (right != null) {
                sb.append("right=").append(right).append(" ");
                right = null;
            }
            if (rotate != null) {
                sb.append("rotate=").append(rotate).append(" ");
                rotate = null;
            }
            if (drop != null && drop) {
                sb.append("drop=").append(drop).append(" ");
                drop = null;
            }
            resp.getWriter().print(sb.toString());
        }
    }
}
