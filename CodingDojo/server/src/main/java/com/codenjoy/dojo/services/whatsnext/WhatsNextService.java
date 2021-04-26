package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.*;
import static org.apache.commons.lang3.StringUtils.repeat;

@Component
public class WhatsNextService {

    private static Pattern ACTION_PATTERN = Pattern.compile("(\\((\\d)\\)->\\[(.*)\\])", Pattern.CASE_INSENSITIVE);

    public static class Info extends InformationCollector {
        public Info(PlayerScores playerScores) {
            super(playerScores);
        }

        @Override
        public void event(Object event) {
            pool.add(event.toString());
            super.event(event);
        }
    }

    public String calculate(GameType gameType, String board, String allActions) {
        Settings settings = gameType.getSettings();
        GameField game = gameType.createGame(0, settings);
        List<Info> infos = new LinkedList<>();
        List<GamePlayer> players  = game.load(board, () -> {
            PlayerScores scores = gameType.getPlayerScores(1000, settings);
            Info listener = new Info(scores);
            infos.add(listener);
            GamePlayer player = gameType.createPlayer(listener, StringUtils.EMPTY, settings);
            return player;
        });

        List<Single> singles = new LinkedList<>();
        players.forEach(player -> {
            Single single = new Single(player, gameType.getPrinterFactory());
            single.on(game);
            single.newGame();
            singles.add(single);
        });

        int maxLength = game.reader().size() + 12;
        List<String> results = new LinkedList<>();

        printBoard(infos, singles, maxLength, results);

        List<String> ticks = split(allActions, ";", true);
        for (int tick = 0; tick < ticks.size(); tick++) {
            String tickActions = ticks.get(tick);
            Map<Integer, String> actions = command(tickActions);
            for (int index = 0; index < singles.size(); index++) {
                Single single = singles.get(index);
                String action = actions.get(countFromOne(index));
                if (StringUtils.isNotEmpty(action)) {
                    new PlayerCommand(single.getJoystick(), action).execute();
                }
            }

            game.tick();

            String tickHeader = printHeader(maxLength, tick);
            results.add(tickHeader);

            printBoard(infos, singles, maxLength, results);
        }
        return breakLine(maxLength) +
                results.stream()
                        .collect(joining(""));
    }

    private void printBoard(List<Info> infos, List<Single> singles, int maxLength, List<String> results) {
        for (int index = 0; index < singles.size(); index++) {
            Single single = singles.get(index);
            Info info = infos.get(index);
            String result = String.format(
                    "Board:\n%s" +
                    "Events:%s\n",
                    single.getBoardAsString().toString(),
                    info.all().toString()
            );
            result = formatSpaces(maxLength, index, result);
            results.add(result);
        }
        results.add("|" + repeat(' ', maxLength - 1) + "\n");
        results.add(breakLine(maxLength));
    }

    private String formatSpaces(int maxLength, int index, String result) {
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

    private String printHeader(int maxLength, int tick) {
        String tickInfo = "tick " + countFromOne(tick);
        int spacesLength = (maxLength - tickInfo.length()) / 2;
        String spaces = repeat(' ', spacesLength);
        return String.format("|%s%s%s\n%s",
                spaces, tickInfo, spaces,
                breakLine(maxLength));
    }

    private String breakLine(int maxLength) {
        return "+" + repeat('-', maxLength - 1) + "\n";
    }

    private int countFromOne(int number) {
        return number + 1;
    }

    private Map<Integer, String> command(String tick) {
        List<String> actions = split(tick, "&", false);
        return actions.stream()
                .map(action -> {
                    Matcher matcher = ACTION_PATTERN.matcher(action);
                    if (matcher.matches()) {
                        String group1 = matcher.group(2);
                        String group2 = matcher.group(3);
                        return new HashMap.SimpleEntry<>(Integer.valueOf(group1), group2);
                    } else {
                        throw new RuntimeException("Unparsed action: " + action);
                    }
                })
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private List<String> split(String tick, String regex, boolean empty) {
        return Arrays.stream(tick.split(regex, -1))
                .filter(line -> empty || !StringUtils.isEmpty(line))
                .collect(toList());
    }

}
