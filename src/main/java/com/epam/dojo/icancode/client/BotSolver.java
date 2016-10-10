package com.epam.dojo.icancode.client;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.epam.dojo.icancode.client.finder.CrudePathFinder;
import com.epam.dojo.icancode.client.finder.PathGrid;

import java.util.List;

import static com.codenjoy.dojo.client.Direction.*;

/**
 * Your AI
 */
public class BotSolver implements Solver<Board> {

    /**
     * Your email entered at http://dojo.lab.epam.com/codenjoy-contest/resources/icancode/registration.html
     */
    private static final String USER_NAME = "user@gmail.com";
    /**
     * Server url
     */
    private static final String HOST = "127.0.0.1:8080";

    private Dice dice;
    private Board board;

    private Point scoutTarget;
    private boolean wasExit = false;

    /**
     * @param dice wrapper on Random, used for unit testing
     */
    public BotSolver(Dice dice) {
        this.dice = dice;
    }

    /**
     * @param board use it for find elements on board
     * @return what hero should do in this tick (for this board)
     */
    @Override
    public String get(Board board) {
        this.board = board;
        if (!board.isMeAlive()) return doNothing();

        Point me = board.getMe();
        Point nearGold = getNear(me, board.getGold());

        /*if (!wasExit || nearGold != null) {
            scoutTarget = null;
            find(me, nearGold);
        } else {
            //scout(robot, scanner, me);
        }*/

        if (!board.isBarrierAt(me.getX() + 1, me.getY())) {
            return go(RIGHT);
        } else if (!board.isBarrierAt(me.getX(), me.getY() + 1)) {
            return go(DOWN);
        } else if (!board.isBarrierAt(me.getX() - 1, me.getY())) {
            return go(LEFT);
        }

        CrudePathFinder crudePathFinder = new CrudePathFinder(1000);
        crudePathFinder.findPath(new PathGrid(board), me.getX(), me.getY(), nearGold.getX(), nearGold.getY());
        System.out.println(crudePathFinder);

        return doNothing();
    }

    private void find(Point me, Point nearGold) {
        if (nearGold != null) {
            if (nearGold.toString() == me.toString()) {
                return;
            }
            wasExit = false;
            goTo(nearGold);
        } else {
            Point exit = board.getExit().get(0);
            if (exit.toString() == me.toString()) {
                wasExit = true;
                return;
            }
            goTo(exit);
        }
    }

    private void goTo(Point target) {
        /*var path = scanner.getShortestWay(target);
        var toCell = path[1];
        var fromCell = scanner.getMe();
        var command = {
                direction:getDirection(fromCell, toCell),
                jump:false
        };

        if (scanner.isNear(toCell.x, toCell.y, LASERS)
                || scanner.isAnyOfAt(toCell.x, toCell.y, LASERS))
        {
            return;
        }

        if (scanner.getExit()[0].toString() == toCell.toString() && target.toString() != toCell.toString()) {
            command = bypass(fromCell, toCell, command.direction, scanner);
        }

        if (scanner.at(toCell) == "HOLE") {
            command = bypass(fromCell, toCell, command.direction, scanner);
        }

        if (command.jump) {
            robot.jump(command.direction);
        } else {
            robot.go(command.direction);
        }*/
    }

    private Point getNear(Point start, List<Point> list) {
        if (list.size() == 0) {
            return null;
        }

        int distance = 1000;
        Point result = list.get(0);
        for (int i = 0; i < list.size(); ++i) {
            int curDistance = heuristic(start, list.get(i));
            if (curDistance < distance) {
                distance = curDistance;
                result = list.get(i);
            }
        }

        return result;
    }

    private int heuristic(Point pos0, Point pos1) {
        int d1 = Math.abs(pos1.getX() - pos0.getX());
        int d2 = Math.abs(pos1.getY() - pos0.getY());
        return d1 + d2;
    }

    /**
     * Says to Hero do nothing
     */
    private String doNothing() {
        return "";
    }

    /**
     * Reset current level
     */
    private String resetLevel() {
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

    public String go(Direction direction) {
        return direction.toString();
    }

    /**
     * Run this method for connect to Server
     */
    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run("ws://" + HOST + "/codenjoy-contest/ws", name,
                    new BotSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}