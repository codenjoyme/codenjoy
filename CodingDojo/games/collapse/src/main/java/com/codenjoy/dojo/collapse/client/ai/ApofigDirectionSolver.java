package com.codenjoy.dojo.collapse.client.ai;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.DirectionSolver;
import com.codenjoy.dojo.collapse.client.Board;
import com.codenjoy.dojo.services.Dice;

public class ApofigDirectionSolver implements DirectionSolver<Board> {

    private Board board;
    private Dice dice;

    public ApofigDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(Board board) {
        this.board = board;

        int x = dice.next(board.size());
        int y = dice.next(board.size());
        Direction direction = Direction.random(dice);

        return String.format("ACT(%s,%s),%s", x, y, direction);
    }

}
