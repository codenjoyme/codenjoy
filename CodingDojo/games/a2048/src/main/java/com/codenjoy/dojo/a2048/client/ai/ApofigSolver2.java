package com.codenjoy.dojo.a2048.client.ai;

import com.codenjoy.dojo.a2048.client.Board;
import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: your name
 */
public class ApofigSolver2 implements Solver<Board> {

    private static final String USER_NAME = "apofig@gmail.com";
    static ExecutorService excecutor = Executors.newFixedThreadPool(1);

    private static Step[] path = new Step[102400];
    private static int length = 0;
    private static int deepIndex = 0;

    class Step {
        String directions;
        char[][] field;
        int free;
        int sum;
        int deep;
    }

    static List<String> bestPath = new CopyOnWriteArrayList<>();
    static char[][] bestField;

    static char[][] fieldPrevPrev;
    static char[][] fieldPrev;

    private boolean fixLiveLock(char[][] field) {
        boolean lock = Arrays.deepEquals(fieldPrevPrev, field) ||
                Arrays.deepEquals(fieldPrev, field);

        fieldPrevPrev = fieldPrev;
        fieldPrev = field;

        return lock;
    }

    @Override
    public String get(Board board) {
        char[][] field = board.getField();

        if (bestPath.isEmpty()) {
            startCalculating(field);
        }
        while (bestPath.isEmpty()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // do nothing
            }
        }

        String best = bestPath.remove(0);

        length = 0;

        if (fixLiveLock(field)) {
            bestPath.clear();
            return Direction.valueOf(best).clockwise().toString();
        }

        return best;
    }

    private void startCalculating(final char[][] field) {
        excecutor.submit(new Runnable() {
            @Override
            public void run() {
                while (bestPath.size() < 1000) {
                    if (bestPath.isEmpty()) {
                        calculateFor(field, "");
                    } else {
                        calculateFor(bestField, "");
                    }
                    calculateBest();
                }
            }
        });
    }

    private void calculateBest() {
        int bestIndex = findBest();

        String best = path[bestIndex].directions;
        bestField = path[bestIndex].field;

        bestPath.addAll(Arrays.asList(best.split(",")));
    }

    private int findBest() {
//        int maxDeep = 0;
//        for (int index = 0; index < length; index++) {
//            Step step = path[index];
//            if (step.deep > maxDeep) {
//                maxDeep = step.deep;
//            }
//        }
//
        int maxFree = 0;
//        while (maxFree == 0 && maxDeep != 0) {
            for (int index = 0; index < length; index++) {
                Step step = path[index];
//                if (step.deep != maxDeep) continue;

                if (step.free > maxFree) {
                    maxFree = step.free;
                }
            }
//            maxDeep--;
//        }

        int fast = 100;
        int fastIndex = 0;
        for (int index = 0; index < length; index++) {
            Step step = path[index];
//            if (step.deep != maxDeep) continue;
            if (step.free < maxFree /*|| step.sum < maxSum*/) continue;

            int count = step.directions.split(",").length;
            if (count < fast) {
                fastIndex = index;
                fast = count;
            }
        }

        return fastIndex;
    }

    private void calculateFor(char[][] field, String directions) {
        deepIndex++;
        if (deepIndex <= 4) {
            for (Direction direction : new Direction[] {Direction.RIGHT, Direction.DOWN, Direction.LEFT, Direction.UP}) {
                Step step = new Step();
                step.directions = addComma(directions) + direction.name();
                step.deep = deepIndex;

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
                    new ApofigSolver2(),
                    new Board());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
