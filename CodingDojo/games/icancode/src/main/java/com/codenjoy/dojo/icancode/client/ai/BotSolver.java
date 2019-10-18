package com.codenjoy.dojo.icancode.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.icancode.client.AbstractSolver;
import com.codenjoy.dojo.icancode.client.Board;
import com.codenjoy.dojo.icancode.client.Command;

import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;

public class BotSolver extends AbstractSolver {

    private BotBoard board;
    private Point scoutTarget;
    private boolean wasExit = false;
    private DirectionJump previousCommand;

    public BotSolver(Dice dice) {
        super(dice);
    }

    @Override
    public Command whatToDo(Board board) {
        this.board = (BotBoard) new BotBoard().forString(board.getLayersString().toArray(new String[0]));

        DirectionJump result = programm();
        previousCommand = result;

        if (result == null) {
            return Command.doNothing();
        }

        if (result.jump) {
            if (result.direction != null) {
                return Command.jump(result.direction);
            } else {
                return Command.jump();
            }
        } else if (result.direction != null) {
            return Command.go(result.direction);
        }

        return Command.doNothing();
    }

    private DirectionJump programm() {
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

    private DirectionJump scout(Point me) {

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

    private DirectionJump find(Point me, Point nearGold) {
        if (nearGold != null) {
            if (nearGold.equals(me)) {
                return null;
            }
            wasExit = false;
            return goTo(me, nearGold);
        } else {
            Point exit = board.getExits().size() != 0 ? board.getExits().get(0) : null;
            if (exit == null || exit.equals(me)) {
                wasExit = true;
                return null;
            }
            return goTo(me, exit);
        }
    }

    private DirectionJump goTo(Point me, Point target) {
        return goTo(me, target, null);
    }

    private DirectionJump goTo(Point me, Point target, List<Direction> path) {
        if (path == null) {
            path = board.findPath(me, target);
        }

        if (path == null) {
            return null;
        }

        Point toCell = path.get(0).change(new PointImpl(me.getX(), me.getY()));
        Point fromCell = me;

        DirectionJump command = new DirectionJump(path.get(0), false);

        //add lisers

        Point exit = board.getExits().size() != 0 ? board.getExits().get(0) : null;
        if (exit != null && exit.equals(toCell) && !target.equals(toCell)) {
            command = bypass(fromCell, toCell, command.direction);
        }

        if (board.isHoleAt(toCell.getX(), toCell.getY())) {
            command = bypass(fromCell, toCell, command.direction);
        }

        return command;
    }

    private DirectionJump bypass(Point fromCell, Point toCell, Direction direction) {
        DirectionJump result = new DirectionJump(direction, false);

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
}