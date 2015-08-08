package com.codenjoy.dojo.spacerace.client.ai;

import java.util.List;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.model.Elements;
import com.codenjoy.dojo.spacerace.services.GameRunner;

public class AlAnSolver implements Solver<Board> {

    private int delay = 0;
    private boolean vpravo = true;

    public AlAnSolver(Dice dice) {
    }

    @Override
    public String get(final Board board) {
        String result = "";
        int x = board.getMe().getX();

		if (vpravo && (x < board.size() - 5)||(x < 5)){
            result = Direction.RIGHT.toString();
            vpravo = true;
        }else {
            vpravo = false;
            result = Direction.LEFT.toString();
        }

        delay++;
        if(delay >= 3){
            result += Direction.UP.ACT.toString();
            delay = 0;
        }

        return result;
    }

    /**
     * Метод для запуска игры с текущим ботом. Служит для отладки.
     */
    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new AlAnSolver(new RandomDice()),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new AlAnSolver(new RandomDice()),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
