package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.multiplayer.Single;

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

    public void board(List<Info> infos, List<Single> singles) {
        for (int index = 0; index < singles.size(); index++) {
            boardSeparator();

            Single single = singles.get(index);
            String info = infos.get(index).all().toString();
            String prefix = String.format("| (%s) ", countFromOne(index));

            String board = single.getBoardAsString().toString();
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