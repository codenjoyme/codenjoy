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
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.RandomDice;
import com.epam.dojo.icancode.client.finder.CrudePathFinder;
import com.epam.dojo.icancode.client.finder.PathGrid;
import com.epam.dojo.icancode.model.Elements;

import java.util.List;

import static com.codenjoy.dojo.client.Direction.*;
import static com.epam.dojo.icancode.model.Elements.HOLE;

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
    private BotBoard board;

    private Point scoutTarget;
    private boolean wasExit = false;
    private Command previousCommand;

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
        this.board = (BotBoard) board;

        Command result = programm();
        previousCommand = result;

        if (result == null) {
            return doNothing();
        }

        if (result.jump) {
            if (result.direction != null) {
                return jumpTo(result.direction);
            } else {
                return jump();
            }
        } else if (result.direction != null) {
            return go(result.direction);
        }

        return doNothing();
    }

    private Command programm() {
        if (!board.isMeAlive()) {
            return null;
        }

        Point me = board.getMe();
        Point nearGold = board.getNearInList(me, board.getGold());
        nearGold = nearGold != null && board.findPath(me, nearGold) != null ? nearGold : null;

        if (!wasExit || nearGold != null) {
            scoutTarget = null;
            return find(me, nearGold);
        } else {
            return scout(me);
        }
    }

    private Command scout(Point me) {

        Point targetCell;
        List<Direction> path;

        if (scoutTarget == null) {
            Direction pd = previousCommand != null ? previousCommand.direction : Direction.random();

            if (pd == LEFT || pd == RIGHT) {
                for (int y = 0; y < board.size(); ++y) {
                    if (!board.isBarrierAt(pd == LEFT ? 0 : board.size() - 1, y)) {
                        targetCell = new PointImpl(pd == LEFT ? 0 : board.size() - 1, y);
                        path = board.findPath(me, targetCell);
                        if (path != null) {
                            scoutTarget = targetCell;
                            return goTo(me, targetCell, path);
                        }
                    }
                }
            }

            if (pd == UP || pd == DOWN) {
                for (int x = 0; x < board.size(); ++x) {
                    if (!board.isBarrierAt(x, pd == UP ? 0 : board.size() - 1)) {
                        targetCell = new PointImpl(x, pd == UP ? 0 : board.size() - 1);
                        path = board.findPath(me, targetCell);
                        if (path != null) {
                            scoutTarget = targetCell;
                            return goTo(me, targetCell, path);
                        }
                    }
                }
            }
        } else if (me.equals(scoutTarget)) {
            scoutTarget = null;
        } else {
            path = board.findPath(me, scoutTarget);
            if (path != null) {
                return goTo(me, scoutTarget, path);
            } else {
                scoutTarget = null;
            }
        }

        return null;
    }

    private Command find(Point me, Point nearGold) {
        if (nearGold != null) {
            if (nearGold.equals(me)) {
                return null;
            }
            wasExit = false;
            return goTo(me, nearGold);
        } else {
            Point exit = board.getExit().size() != 0 ? board.getExit().get(0) : null;
            if (exit == null || exit.equals(me)) {
                wasExit = true;
                return null;
            }
            return goTo(me, exit);
        }
    }

    private Command goTo(Point me, Point target) {
        return goTo(me, target, null);
    }

    private Command goTo(Point me, Point target, List<Direction> path) {
        if (path == null) {
            path = board.findPath(me, target);
        }

        if (path == null) {
            return null;
        }

        Point toCell = path.get(0).change(new PointImpl(me.getX(), me.getY()));
        Point fromCell = me;

        Command command = new Command(path.get(0), false);

        //add lisers

        Point exit = board.getExit().size() != 0 ? board.getExit().get(0) : null;
        if (exit != null && exit.equals(toCell) && !target.equals(toCell)) {
            command = bypass(fromCell, toCell, command.direction);
        }

        if (board.isHoleAt(toCell.getX(), toCell.getY())) {
            command = bypass(fromCell, toCell, command.direction);
        }

        return command;
    }

    private Command bypass(Point fromCell, Point toCell, Direction direction) {
        Command result = new Command(direction, false);

        int xDiff = direction == LEFT ? -2 : (direction == RIGHT ? 2 : 0);
        int yDiff = direction == DOWN ? 2 : (direction == UP ? -2 : 0);
        result.jump = !board.isBarrierAt(fromCell.getX() + xDiff, fromCell.getY() + yDiff) && (previousCommand == null || previousCommand.direction != direction.inverted());

        if (result.jump) {
            return result;
        }

        switch (direction) {
            case DOWN:
            case UP:
                result.direction = !board.isBarrierAt(fromCell.getX() - 1, fromCell.getY()) ? LEFT : RIGHT;
                break;
            case LEFT:
            case RIGHT:
                result.direction = !board.isBarrierAt(fromCell.getX(), fromCell.getY() - 1) ? UP : DOWN;
                break;
        }

        return result;
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
                    new BotBoard());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Command {
        Direction direction;
        boolean jump;

        public Command(Direction direction, boolean jump) {
            this.direction = direction;
            this.jump = jump;
        }
    }
}