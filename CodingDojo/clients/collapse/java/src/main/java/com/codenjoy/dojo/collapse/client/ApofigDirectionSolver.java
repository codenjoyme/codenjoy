package com.codenjoy.dojo.collapse.client;

import com.codenjoy.dojo.collapse.client.utils.BoardImpl;
import com.codenjoy.dojo.collapse.client.utils.Dice;

public class ApofigDirectionSolver implements DirectionSolver {

    private BoardImpl board;
    private Dice dice;

    public ApofigDirectionSolver(Dice dice) {
        this.dice = dice;
    }

    @Override
    public String get(BoardImpl board) {
        this.board = board;

        int x = dice.next(board.size());
        int y = dice.next(board.size());
        Direction direction = Direction.valueOf(dice.next(Direction.values().length));

        return String.format("ACT(%s,%s),%s", x, y, direction);
    }

}
