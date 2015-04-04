package com.codenjoy.dojo.minesweeper.client.ai.vaa25;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.minesweeper.client.Board;

/**
 * @author Alexander Vlasov
 */
public class BugResolver {
    private Board board;
    private Direction direction;

    public BugResolver(Board board, Direction direction) {
        this.direction = direction;
        this.board = board;
    }

    public boolean isBug(Board board) {
        int newSum = countAroundSum(board);
        int oldSum = countAroundSum(this.board);
        return newSum == oldSum;
    }

    public Direction getDirection() {
        return direction;
    }

    private int countAroundSum(Board board) {
        int sum = 0;
        int x = direction.changeX(board.getMe().getX());
        int y = direction.changeY(board.getMe().getY());
        y = Direction.UP.changeY(y);
        sum += addSum(board, x, y);
        x = Direction.RIGHT.changeX(x);
        sum += addSum(board, x, y);
        y = Direction.DOWN.changeY(y);
        sum += addSum(board, x, y);
        y = Direction.DOWN.changeY(y);
        sum += addSum(board, x, y);
        x = Direction.LEFT.changeX(x);
        sum += addSum(board, x, y);
        x = Direction.LEFT.changeX(x);
        sum += addSum(board, x, y);
        y = Direction.UP.changeY(y);
        sum += addSum(board, x, y);
        y = Direction.UP.changeY(y);
        sum += addSum(board, x, y);
        return sum;
    }

    private int addSum(Board board, int x, int y) {
        char ch = board.getAt(x, y).ch();
        if (ch > '0' && ch <= '8') return Character.getNumericValue(ch);
        else return 0;
    }
}
