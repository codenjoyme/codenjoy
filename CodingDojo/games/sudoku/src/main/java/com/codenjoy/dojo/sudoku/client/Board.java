package com.codenjoy.dojo.sudoku.client;

import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.sudoku.model.Elements;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 */
public class Board extends AbstractBoard<Elements> {

    @Override
    public Elements valueOf(char ch) {
        return Elements.valueOf(ch);
    }

    public List<Point> get(Elements... elements) {
        List<Point> result = super.get(elements);
        Collections.sort(result);
        return result;
    }

    @Override
    public String toString() {
       return boardAsString();
    }

    public List<Integer> getY(int ty) {
        List<Integer> result = new LinkedList<Integer>();

        for (int x = 0; x < size; x++) {
            if (Arrays.asList(0, 4, 8, 12).contains(x)) continue;

            result.add(getAt(x, toAbsolute(ty)).value());
        }

        return result;
    }

    public List<Integer> getX(int tx) {
        List<Integer> result = new LinkedList<Integer>();

        for (int y = 0; y < size; y++) {
            if (Arrays.asList(0, 4, 8, 12).contains(y)) continue;

            result.add(getAt(toAbsolute(tx), y).value());
        }

        return result;
    }

    public static int toAbsolute(int a) {
        a--;
        return a + a/3 + 1;
    }

    public List<Integer> getC(int tx, int ty) {
        List<Integer> result = new LinkedList<Integer>();
        for (int dy = -1; dy <= 1; dy++) {
            for (int dx = -1; dx <= 1; dx++) {
                int x = 2 + 4 * (tx - 1) + dx;
                int y = 2 + 4 * (ty - 1) + dy;

                result.add(getAt(x, y).value());
            }
        }
        return result;
    }

    public void set(int tx, int ty, int n) {
        super.set(toAbsolute(tx), toAbsolute(ty), String.valueOf(n).charAt(0));
    }
}