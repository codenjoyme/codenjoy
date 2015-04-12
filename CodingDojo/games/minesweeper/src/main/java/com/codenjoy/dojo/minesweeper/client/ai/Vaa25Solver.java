package com.codenjoy.dojo.minesweeper.client.ai;

import com.YourDirectionSolver;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.minesweeper.client.Board;
import com.codenjoy.dojo.minesweeper.services.GameRunner;

public class Vaa25Solver extends YourDirectionSolver implements Solver<Board> {

    public Vaa25Solver(String name) {
        super(new com.utils.RandomDice(), name);
    }

    public static void main(String[] args) {
        LocalGameRunner.run(new GameRunner(),
                new Vaa25Solver("test"),
                new Board());
//        start(WebSocketRunner.DEFAULT_USER, WebSocketRunner.Host.LOCAL);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new Vaa25Solver(name),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String get(Board board) {
        return super.get(new BoardAdapter(board));
    }
}
