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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.algs.DeikstraFindWay;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

@UtilityClass
public class TestUtils {

    public static final int COUNT_NUMBERS = 3;

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
            result.append(string.substring((index - 1)*position, index*position)).append(substring);
        }
        result.append(string.substring((string.length() / position) * position, string.length()));
        return result.toString();
    }

    public static Game  buildGame(GameType gameType, EventListener listener, PrinterFactory factory) {
        GameField gameField = gameType.createGame(LevelProgress.levelsStartsFrom1);
        GamePlayer gamePlayer = gameType.createPlayer(listener, null);
        Game game = new Single(gamePlayer, factory);
        game.on(gameField);
        game.newGame();
        return game;
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
     * @param assertor так как assertEquals нельзя использовать в prod code, а этот класс нельзя переместить в test и затянуть потом как дупенденси, тут лямбда )
     * @param expectedAll что должно быть
     * @param actualAll что реально пришло
     */
    public static void assertSmoke(boolean allFirst, BiConsumer<Object, Object> assertor, String expectedAll, String actualAll) {
        String[] expected = expectedAll.split(LocalGameRunner.SEP);
        String[] actual = actualAll.split(LocalGameRunner.SEP);

        if (allFirst) {
            assertor.accept(expectedAll, actualAll);
        }

        for (int i = 0; i < Math.min(expected.length, actual.length); i++) {
            assertor.accept(expected[i], actual[i]);
        }
        assertor.accept(expected.length, actual.length);

        if (!allFirst) {
            assertor.accept(expectedAll, actualAll);
        }
    }

}
