package com.codenjoy.dojo.bomberman.client;

import com.codenjoy.dojo.bomberman.client.utils.Board;
import com.codenjoy.dojo.bomberman.client.utils.Dice;
import com.codenjoy.dojo.bomberman.client.utils.Point;

/**
 * Created by Sanja on 25.03.14.
 */
public class RandomDirectionSolver implements DirectionSolver {
    private Dice dice;

    public RandomDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        Direction direction = null;
        int toX = 0;
        int toY = 0;
        do {
            direction = Direction.values()[dice.next(5)];

            Point bomberman = board.getBomberman();
            toX = direction.changeX(bomberman.getX());
            toY = direction.changeY(bomberman.getY());

        } while (board.isAt(toX, toY, Element.WALL) || board.isAt(toX, toY, Element.DESTROY_WALL));

        String command = direction.toString();
        if (!command.equalsIgnoreCase("act") && !command.equalsIgnoreCase("stop")) {
            if (dice.next(5) == 0) {
                command = "act, " + command;
            }
        }
        return command;
    }
}
