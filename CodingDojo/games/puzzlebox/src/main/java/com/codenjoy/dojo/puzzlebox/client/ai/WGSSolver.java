package com.codenjoy.dojo.puzzlebox.client.ai;

import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.puzzlebox.client.Board;
import com.codenjoy.dojo.puzzlebox.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;

import java.util.Random;

/**
 * Это алгоритм твоего бота. Он будет запускаться в игру с первым
 * зарегистрировавшимся игроком, чтобы ему не было скучно играть самому.
 * Реализуй его как хочешь, хоть на Random.
 * Для его запуска воспользуйся методом {@see WGSSolver#main}
 */
public class WGSSolver implements Solver<Board> {

    public WGSSolver(Dice dice) {
        // TODO
    }

    @Override
    public String get(final Board board) {
        String result = "";
        Random random = new Random(4);
        int r = (int) ((Math.random() * 4) +1);
        switch (r) {
            case 1:
                result = "UP";
                break;
            case 2:
                result = "RIGHT";
                break;
            case 3:
                result = "DOWN";
            break;
            case 4:
                result = "LEFT";
            break;
            default: break;
        }
        result += ", ACT";
        return result;
    }

    /**
     * Метод для запуска игры с текущим ботом. Служит для отладки.
     */
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new WGSSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new WGSSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
