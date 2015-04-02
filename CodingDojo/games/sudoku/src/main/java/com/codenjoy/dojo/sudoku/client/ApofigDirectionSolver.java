package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.sudoku.client.utils.BoardImpl;
import com.codenjoy.dojo.sudoku.client.utils.Point;

import java.util.*;

/**
 * Created by Sanja on 11.02.14.
 */
public class ApofigDirectionSolver implements DirectionSolver {

    public static final int SIZE = 9;

    @Override
    public String get(BoardImpl board) {
        Map<Integer, List<Integer>> forY = new HashMap<Integer, List<Integer>>();
        Map<Integer, List<Integer>> forX = new HashMap<Integer, List<Integer>>();
        Map<Point, List<Integer>> forC = new TreeMap<Point, List<Integer>>();
        Map<Point, List<Integer>> forPoint = new TreeMap<Point, List<Integer>>();

        for (int n = 1; n <= SIZE; n++) {
            forY.put(n, invert(board.getY(n)));
            forX.put(n, invert(board.getX(n)));
        }

        for (int tx = 1; tx <= 3; tx++) {
            for (int ty = 1; ty <= 3; ty++) {

                List<Integer> result = board.getC(tx, ty);
                forC.put(new Point(tx, ty), invert(result));
            }
        }

        for (int x = 1; x <= 9; x++) {
            for (int y = 1; y <= 9; y++) {
                Point pt = new Point(x, y);

                List<Integer> xx = forX.get(x);
                List<Integer> yy = forY.get(y);
                List<Integer> cc = forC.get(new Point((x - 1) / 3 + 1, (y - 1) / 3 + 1));

                List<Integer> result = new LinkedList<Integer>(xx);
                for (Integer n : result.toArray(new Integer[0])) {
                    if (!yy.contains(n) || !cc.contains(n)) {
                        result.remove(n);
                    }
                }

                if (board.getAt(x, y) == Element.NONE) {
                    forPoint.put(pt, result);
                }
            }
        }

        for (Map.Entry<Point, List<Integer>> entry : forPoint.entrySet()) {
            List<Integer> values = entry.getValue();
            if (values.size() == 1) {
                Point pt = entry.getKey();
                return String.format("ACT(%s,%s,%s)",
                        pt.getX(),
                        pt.getY(),
                        values.iterator().next());
            }
        }

        return "ACT(0)";
    }

    private List<Integer> invert(List<Integer> source) {
        List<Integer> result = new LinkedList<Integer>();

        for (int n = 1; n <= SIZE; n++) {
            if (!source.contains(n)) {
                result.add(n);
            }
        }

        return result;
    }

}
