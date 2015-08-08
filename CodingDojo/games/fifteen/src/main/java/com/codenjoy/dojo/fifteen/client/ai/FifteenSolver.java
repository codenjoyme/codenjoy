package com.codenjoy.dojo.fifteen.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.fifteen.client.Board;
import com.codenjoy.dojo.fifteen.services.GameRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.Point;

import java.util.*;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random.
 * Для его запуска воспользуйся методом {@see FifteenSolver#main}
 */
public class FifteenSolver implements Solver<Board> {

    private final Dice dice;

    public FifteenSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(final Board board) {
        String[] directions = {
                Direction.DOWN.toString(),
                Direction.UP.toString(),
                Direction.RIGHT.toString(),
                Direction.LEFT.toString()};

        int random = 0;
        do {
            random = dice.next(directions.length);
        } while (!checkDirection(directions[random], board));

        return directions[random];
    }

    private boolean checkDirection(String direction, Board board) {
        Point me = board.getMe();

        int newX = me.getX();
        int newY = me.getY();

        if(direction.equals("UP")){
            newY--;
        } else if(direction.equals("DOWN")){
            newY++;
        } else if(direction.equals("LEFT")){
            newX--;
        } else if(direction.equals("RIGHT")) {
            newX++;
        }

        return !board.isBarrierAt(newX, newY);
    }

    /**
     * Метод для запуска игры с текущим ботом. Служит для отладки.
     */
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new FifteenSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new FifteenSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
