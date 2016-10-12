package com.epam.dojo.icancode.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

/**
 * Created by indigo on 2016-10-12.
 */
public abstract class AbstractSolver implements Solver<Board>  {

    @Override
    public String get(Board board) {
        return whatToDo(board);
    }

    public abstract String whatToDo(Board board);

    /**
     * Says to Hero do nothing
     */
    public String doNothing() {
        return "";
    }

    /**
     * Reset current level
     */
    public String die() {
        return "ACT(0)";
    }

    /**
     * Says to Hero jump to direction
     */
    public String jumpTo(Direction direction) {
        return "ACT(1)" + "," + direction.toString();
    }

    /**
     * Says to Hero pull box on this direction
     */
    public String pullTo(Direction direction) {
        return "ACT(2)" + "," + direction.toString();
    }

    /**
     * Says to Hero jump in place
     */
    public String jump() {
        return "ACT(1)";
    }

    /**
     * Says to Hero go to direction
     */
    public String go(Direction direction) {
        return direction.toString();
    }

    /**
     * @param host Server url
     * @param userName Your email entered at http://dojo.lab.epam.com/codenjoy-contest/resources/icancode/registration.html
     */
    public static void start(String host, String userName) {
        try {
            WebSocketRunner.run("ws://" + host + "/codenjoy-contest/ws", userName,
                    new YourSolver(),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
