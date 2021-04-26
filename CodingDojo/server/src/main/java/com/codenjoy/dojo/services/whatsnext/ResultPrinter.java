package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.multiplayer.Single;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.whatsnext.WhatsNextService.countFromOne;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
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
            Single single = singles.get(index);
            Info info = infos.get(index);
            String boardInfo = String.format(
                    "Board:\n%s" +
                            "Events:%s\n",
                    single.getBoardAsString().toString(),
                    info.all().toString()
            );
            result.append(formatSpaces(index, boardInfo));
        }
        result.append("|").append(repeat(' ', width - 1)).append("\n");
    }

    // TODO to use common StringBuilder here
    private String formatSpaces(int index, String result) {
        List<String> lines = new ArrayList<>();
        lines.addAll(Arrays.asList(result.split("\n")));

        String prefix = String.format("| (%s) ", countFromOne(index));
        lines = lines.stream()
                .map(line -> prefix + line)
                .collect(toList());

        lines.add(0, "|");

        lines = lines.stream()
                .map(line -> line + repeat(' ', width - line.length()))
                .collect(toList());

        return lines.stream()
                .collect(joining("\n")) + "\n";
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