package com.codenjoy.dojo.bomberman.client.ai;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;

public class RandomSolver implements Solver<Board> {
    private Dice dice;

    public RandomSolver(Dice dice) {
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

        } while (board.isAt(toX, toY, Elements.WALL) || board.isAt(toX, toY, Elements.DESTROY_WALL));

        String command = direction.toString();
        if (!command.equalsIgnoreCase("act") && !command.equalsIgnoreCase("stop")) {
            if (dice.next(5) == 0) {
                command = "act, " + command;
            }
        }
        return command;
    }

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new RandomSolver(new RandomDice()),
                new Board());
    }
}
