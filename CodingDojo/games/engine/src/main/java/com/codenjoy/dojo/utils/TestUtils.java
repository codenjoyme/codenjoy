package com.codenjoy.dojo.utils;

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


import com.codenjoy.dojo.client.AbstractBoard;
import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.utils.events.MockitoJunitTesting;
import com.codenjoy.dojo.utils.events.Testing;
import lombok.experimental.UtilityClass;

import java.util.*;
import java.util.function.Function;

import static com.codenjoy.dojo.services.PointImpl.pt;

@UtilityClass
public class TestUtils {

    public static final int COUNT_NUMBERS = 3;

    private static final Testing TESTING = new MockitoJunitTesting();

    public static String injectN(String expected) {
        int size = (int) Math.sqrt(expected.length());
        return inject(expected, size, "\n");
    }

    public static String injectNN(String expected) {
        int size = (int) Math.sqrt(expected.length()/COUNT_NUMBERS)*COUNT_NUMBERS;
        return inject(expected, size, "\n");
    }

    public static String inject(String string, int position, String substring) {
        StringBuilder result = new StringBuilder();
        for (int index = 1; index < string.length() / position + 1; index++) {
            result.append(string, (index - 1)*position, index*position).append(substring);
        }
        result.append(string.substring((string.length() / position) * position));
        return result.toString();
    }

    public static Game  buildGame(GameType gameType, EventListener listener, PrinterFactory factory) {
        Settings settings = gameType.getSettings();
        GameField gameField = gameType.createGame(LevelProgress.levelsStartsFrom1, settings);
        GamePlayer gamePlayer = gameType.createPlayer(listener, null, settings);
        Game game = new Single(gamePlayer, factory);
        game.on(gameField);
        game.newGame();
        return game;
    }

    public static String getWay(String inputBoard,
                                Function<Character, CharElements> elements,
                                Function<AbstractBoard, DeikstraFindWay.Possible> possible)
    {
        AbstractBoard board = getBoard(elements);
        board = (AbstractBoard) board.forString(inputBoard);

        Map<Point, List<Direction>> ways = new DeikstraFindWay().getPossibleWays(board.size(), possible.apply(board)).toMap();

        Map<Point, List<Direction>> map = new TreeMap<>();
        for (Map.Entry<Point, List<Direction>> entry : ways.entrySet()) {
            List<Direction> value = entry.getValue();
            if (!value.isEmpty()) {
                map.put(entry.getKey(), value);
            }
        }

        return map.toString().replace("], [", "],\n[");
    }

    public static AbstractBoard getBoard(Function<Character, CharElements> elements) {
        return new AbstractBoard() {
                @Override
                public CharElements valueOf(char ch) {
                    return elements.apply(ch);
                }

                @Override
                protected int inversionY(int y) {
                    return size - 1 - y;
                }
            };
    }

    public static String drawPossibleWays(int delta,
                                          Map<Point, List<Direction>> possibleWays,
                                          int size,
                                          Function<Point, Character> getAt)
    {
        char[][] chars = new char[size * delta][size * delta];
        for (int x = 0; x < chars.length; x++) {
            Arrays.fill(chars[x], ' ');
        }

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                int cx = x * delta + 1;
                int cy = y * delta + 1;

                char ch = getAt.apply(pt(x, y));
                chars[cx][cy] = (ch == ' ') ? '.' : ch;
                try {
                    for (Direction direction : possibleWays.get(pt(x, y))) {
                        chars[direction.changeX(cx)][direction.changeY(cy)] = directionChar(direction);
                    }
                } catch (NullPointerException e) {
                    // do nothing
                }
            }
        }

        return toString(chars);
    }

    private static char directionChar(Direction direction) {
        switch (direction) {
            case UP: return '↑';
            case LEFT: return '←';
            case RIGHT: return '→';
            case DOWN: return '↓';
            default: throw new IllegalArgumentException();
        }
    }

    public static String drawShortestWay(Point from,
                                        List<Direction> shortestWay,
                                        int size,
                                        Function<Point, Character> getAt)
    {
        Map<Point, List<Direction>> map = new HashMap<>();

        Point current = from;
        while (!shortestWay.isEmpty()) {
            Direction direction = shortestWay.remove(0);
            map.put(current, Arrays.asList(direction));
            current = direction.change(current);
        }

        return drawPossibleWays(2, map, size, getAt);
    }

    private static String toString(char[][] chars) {
        StringBuffer buffer = new StringBuffer();
        for (int x = 0; x < chars.length; x++) {
            for (int y = 0; y < chars.length; y++) {
                buffer.append(chars[y][chars.length - 1 - x]);
            }
            buffer.append('\n');
        }

        return buffer.toString();
    }

    public static String printWay(String expected,
                                  CharElements from, CharElements to,
                                  CharElements none, CharElements wayChar,
                                  AbstractBoard board,
                                  Function<AbstractBoard, DeikstraFindWay.Possible> possible)
    {
        expected = expected.replace(wayChar.ch(), none.ch())
                    .replaceAll("\n", "");
        board = (AbstractBoard) board.forString(expected);
        List<Point> starts = board.get(from);
        Point start = starts.get(0);
        List<Point> goals = board.get(to);
        List<Direction> way = new DeikstraFindWay()
                .getShortestWay(board.size(),
                        start, goals,
                        possible.apply(board));

        Point current = start;
        for (int index = 0; index < way.size(); index++) {
            Direction direction = way.get(index);
            current = direction.change(current);

            CharElements element = (index == way.size() - 1) ? to : wayChar;
            board.set(current.getX(), current.getY(), element.ch());
        }

        return board.boardAsString();
    }

    /**
     * проверяем порционно, потому что в 'mvn test'
     * не видно на больших данных, где именно отличие и это проблема в отладке
     * @param allFirst true - если проверяем все сразу, false - если сперва порционно тик за тиком
     * @param expectedAll что должно быть
     * @param actualAll что реально пришло
     */
    public static void assertSmoke(boolean allFirst, String expectedAll, String actualAll) {
        String[] expected = expectedAll.split(LocalGameRunner.SEP);
        String[] actual = actualAll.split(LocalGameRunner.SEP);

        if (allFirst) {
            assertEquals(expectedAll, actualAll);
        }

        for (int i = 0; i < Math.min(expected.length, actual.length); i++) {
            assertEquals(expected[i], actual[i]);
        }
        assertEquals(expected.length, actual.length);

        if (!allFirst) {
            assertEquals(expectedAll, actualAll);
        }
    }

    private static void assertEquals(Object o1, Object o2) {
        TESTING.assertEquals(o1, o2);
    }

}
