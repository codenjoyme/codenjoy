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

    private int maxLength;

    public ResultPrinter(int boardSize) {
        maxLength = boardSize + 12;
    }

    public String printBoard(List<Info> infos, List<Single> singles) {
        StringBuilder result = new StringBuilder();
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
        result.append("|" + repeat(' ', maxLength - 1) + "\n");
        result.append(breakLine());
        return result.toString();
    }

    private String formatSpaces(int index, String result) {
        List<String> lines = new ArrayList<>();
        lines.addAll(Arrays.asList(result.split("\n")));

        String prefix = String.format("| (%s) ", countFromOne(index));
        lines = lines.stream()
                .map(line -> prefix + line)
                .collect(toList());

        lines.add(0, "|");

        lines = lines.stream()
                .map(line -> line + repeat(' ', maxLength - line.length()))
                .collect(toList());

        return lines.stream()
                .collect(joining("\n")) + "\n";
    }

    public String printTickHeader(int tick) {
        return printHeader("tick " + countFromOne(tick));
    }

    public String printInitialHeader() {
        return printHeader("setup ");
    }

    private String printHeader(String tickInfo) {
        int spacesLength = (maxLength - tickInfo.length()) / 2;
        String spaces = repeat(' ', spacesLength);
        return String.format("|%s%s%s\n%s",
                spaces, tickInfo, spaces,
                breakLine());
    }

    public String breakLine() {
        return "+" + repeat('-', maxLength - 1) + "\n";
    }
}