package com.codenjoy.dojo.a2048.client.ai;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.util.Arrays;

/**
 * User: your name
 */
public class ApofigSolver implements Solver<Board> {

    private static final String USER_NAME = "apofig@gmail.com";

    private static Step[] path = new Step[102400];
    private static int length = 0;
    private static int deepIndex = 0;

    class Step {
        String directions;
        char[][] field;
        int free;
        int sum;
    }

    static char[][] fieldPrevPrev;
    static char[][] fieldPrev;

    private boolean fixLiveLock(char[][] field) {
        boolean lock = Arrays.deepEquals(fieldPrevPrev, field);

        fieldPrevPrev = fieldPrev;
        fieldPrev = field;

        return lock;
    }

    @Override
    public String get(Board board) {
        char[][] field = board.getField();

        calculateFor(field, "");

        String best = findBest();
        length = 0;

        if (fixLiveLock(field)) {
            return Direction.valueOf(best).clockwise().toString();
        }

        return best;
    }

    private String findBest() {
        int maxFree = 0;
        for (int index = 0; index < length; index++) {
            Step step = path[index];
            if (step.free > maxFree) {
                maxFree = step.free;
            }
        }

//        int maxSum = 0;
//        for (int index = 0; index < length; index++) {
//            Step step = path[index];
//            if (step.free != maxFree) continue;
//
//            if (step.sum > maxSum) {
//                maxFree = step.sum;
//            }
//        }

        int fast = 100;
        int fastIndex = 0;
        for (int index = 0; index < length; index++) {
            Step step = path[index];
            if (step.free < maxFree /*|| step.sum < maxSum*/) continue;

            int count = step.directions.split(",").length;
            if (count < fast) {
                fastIndex = index;
                fast = count;
            }
        }

        String best = path[fastIndex].directions;

        return best.split(",")[0];
    }

    private void calculateFor(char[][] field, String directions) {
        deepIndex++;
        if (deepIndex <= 7) {
            for (Direction direction : new Direction[] {Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP}) {
                Step step = new Step();
                step.directions = addComma(directions) + direction.name();

                CharNumbers numbers = new CharNumbers(field);
                numbers.move(invertY(direction));
                numbers.addNew();

                step.field = numbers.get();
                if (changed(field, step.field)) {
                    step.free = numbers.freeSpace();
                    step.sum = numbers.getSum();
                    path[length] = step;
                    length++;

                    calculateFor(step.field, step.directions);
                }
            }
        }
        deepIndex--;
    }

    private Direction invertY(Direction direction) {
        if (direction == Direction.UP) {
            return direction.DOWN;
        }

        if (direction == Direction.DOWN) {
            return direction.UP;
        }

        return  direction;
    }

    private boolean changed(char[][] from, char[][] to) {
        for (int x = 0; x < from.length; x++) {
            for (int y = 0; y < from.length; y++) {
                if (from[x][y] != to[x][y]) return true;
            }
        }
        return false;
    }

    private String addComma(String directions) {
        return (directions.length() > 0)?(directions + ","):"";
    }

    public static void main(String[] args) {
        start(USER_NAME, WebSocketRunner.Host.REMOTE);
    }

    public static void start(String name, WebSocketRunner.Host server) {
        try {
            WebSocketRunner.run(server, name,
                    new ApofigSolver(),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
