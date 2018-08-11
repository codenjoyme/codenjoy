package com.codenjoy.dojo.sudoku.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.sudoku.client.Board;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AISolver implements Solver<Board> {

    private static final int SIZE = 9;

    public AISolver(Dice dice) {
    }

    @Override
    public String get(Board board) {
        Map<Point, Set<Integer>> deprecated = new TreeMap<>();

        for (int x = 1; x <= SIZE; x++) {
            for (int y = 1; y <= SIZE; y++) {
                Set<Integer> numbers = new HashSet<>();
                deprecated.put(pt(x, y), numbers);
            }
        }

        for (int x = 1; x <= SIZE; x++) {
            for (int y = 1; y <= SIZE; y++) {
                Integer current = board.getAt(Board.toAbsolute(x), Board.toAbsolute(y)).value();

                for (int xx = 1; xx <= SIZE; xx++) {
                    Set<Integer> list = deprecated.get(pt(xx, y));
                    if (xx == x && current != 0) {
                        list.addAll(invert(Arrays.asList(current)));
                    } else {
                        list.add(current);
                    }
                }

                for (int yy = 1; yy <= SIZE; yy++) {
                    Set<Integer> list = deprecated.get(pt(x, yy));
                    if (yy == y && current != 0) {
                        list.addAll(invert(Arrays.asList(current)));
                    } else {
                        list.add(current);
                    }
                }

                int rx = (x - 1) / 3;
                int ry = (y - 1) / 3;
                for (int tx = 0; tx < 3; tx++) {
                    for (int ty = 0; ty < 3; ty++) {
                        int xxx = rx * 3 + 1 + tx;
                        int yyy = ry * 3 + 1 + ty;

                        Set<Integer> list = deprecated.get(pt(xxx, yyy));

                        if (xxx == x && yyy == y && current != 0) {
                            list.addAll(invert(Arrays.asList(current)));
                        } else {
                            list.add(current);
                        }
                    }
                }
            }
        }

        Map<Point, Set<Integer>> accepted = new TreeMap<>();
        for (int x = 1; x <= SIZE; x++) {
            for (int y = 1; y <= SIZE; y++) {
                Point key = pt(x, y);
                accepted.put(key, invert(deprecated.get(key)));
            }
        }

        Map<Point, Set<Integer>> toSet = new TreeMap<>();
        for (int x = 1; x <= SIZE; x++) {
            for (int y = 1; y <= SIZE; y++) {
                Integer current = board.getAt(Board.toAbsolute(x), Board.toAbsolute(y)).value();
                Point key = pt(x, y);

                Set<Integer> numbers = accepted.get(key);
                if (current == 0 && numbers.size() == 1) {
                    toSet.put(key, numbers);
                }
            }
        }

        if (toSet.keySet().isEmpty()) {
            return "ACT(0)";
        }

        Point key = toSet.keySet().iterator().next();
        Set<Integer> numbers = toSet.get(key);

        return String.format("ACT(%s,%s,%s)",
                key.getX(),
                key.getY(),
                numbers.iterator().next());
    }

    private Set<Integer> invert(Collection<Integer> source) {
        Set<Integer> result = new HashSet<>();

        for (int n = 1; n <= SIZE; n++) {
            if (!source.contains(n)) {
                result.add(n);
            }
        }

        return result;
    }

    public static void main(String[] args) {
        start(WebSocketRunner.DEFAULT_USER, new RandomDice());
    }

    public static void start(String name, Dice dice) {
        WebSocketRunner.runAI(name,
                new AISolver(dice),
                new Board());
    }
}
