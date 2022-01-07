package com.codenjoy.dojo.services.whatsnext;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.info.Information;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.whatsnext.WhatsNextService.countFromOne;
import static org.apache.commons.lang3.StringUtils.repeat;

public class ResultPrinter {

    private String breakLine;
    private int width;
    private StringBuilder result;

    public ResultPrinter(int boardSize) {
        width = boardSize + 12;
        result = new StringBuilder();
        breakLine = "+" + repeat('-', width - 1) + "\n";
    }

    public void board(List<Information> infos, List<Game> games) {
        for (int index = 0; index < games.size(); index++) {
            boardSeparator();

            Game game = games.get(index);
            String info = infos.get(index).getAllMessages();
            String prefix = String.format("| (%s) ", countFromOne(index));

            String board = game.getBoardAsString().toString();
            List<String> lines = Arrays.asList(board.split("\n"));

            printLine(() -> result.append(prefix).append("Board:"));

            lines.forEach(line -> {
                printLine(() -> result.append(prefix).append(line));
            });

            printLine(() -> result.append(prefix).append("Events:").append(info));
        }
        boardSeparator();
    }

    private void printLine(Runnable line) {
        int pos = length();

        line.run();

        int len = length() - pos;
        result.append(repeat(' ', width - len)).append("\n");
    }

    private int length() {
        return result.length();
    }

    public void boardSeparator() {
        printLine(() -> result.append("|"));
    }

    public void tickHeader(int tick) {
        printHeader("tick " + countFromOne(tick));
    }

    public void initialHeader() {
        printHeader("setup ");
    }

    private void printHeader(String message) {
        int spacesLength = (width - message.length()) / 2;
        String spaces = repeat(' ', spacesLength);
        breakLine();
        result.append("|").append(spaces).append(message).append(spaces).append("\n");
        breakLine();
    }

    public void breakLine() {
        result.append(breakLine);
    }

    @Override
    public String toString() {
        return result.toString();
    }
}