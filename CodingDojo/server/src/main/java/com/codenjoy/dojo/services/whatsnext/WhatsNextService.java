package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.EventListenerCollector;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

@Component
public class WhatsNextService {

    private static Pattern ACTION_PATTERN = Pattern.compile("(\\((\\d)\\)->\\[(.*)\\])", Pattern.CASE_INSENSITIVE);

    public String calculate(GameType gameType, String board, String allActions) {
        Settings settings = gameType.getSettings();
        GameField game = gameType.createGame(0, settings);
        List<EventListenerCollector> infos = new LinkedList<>();
        List<GamePlayer> players  = game.load(board, () -> {
            PlayerScores scores = gameType.getPlayerScores(0, settings);
            EventListenerCollector listener = new EventListenerCollector(scores);
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

        List<String> results = new LinkedList<>();
        List<String> ticks = Arrays.asList(allActions.split(";"));
        for (int tick = 0; tick < ticks.size(); tick++) {
            String tickActions = ticks.get(tick);
            Map<Integer, String> actions = command(tickActions);
            for (int index = 0; index < singles.size(); index++) {
                Single single = singles.get(index);
                String action = actions.get(index);
                if (StringUtils.isNotEmpty(action)) {
                    new PlayerCommand(single.getJoystick(), action).execute();
                }
            }

            game.tick();

            String tickHeader =
                    "|       tick " + countFromOne(tick) + "       \n" +
                    "+--------------------\n";
            results.add(tickHeader);

            for (int index = 0; index < singles.size(); index++) {
                Single single = singles.get(index);
                EventListenerCollector info = infos.get(index);
                String result = String.format(
                        "Board:\n%s" +
                        "Events:%s\n",
                        single.getBoardAsString().toString(),
                        info.getMessage()
                );
                List<String> lines = Arrays.asList(result.split("\n"));
                String prefix = String.format("| (%s) ", countFromOne(index));
                result = lines.stream()
                        .map(line -> prefix + line)
                        .collect(joining("\n")) + "\n";
                results.add("|\n");
                results.add(result);
            }
            results.add("|\n");
            results.add("+--------------------\n");
        }
        return "+--------------------\n" +
                results.stream()
                        .collect(joining(""));
    }

    private int countFromOne(int number) {
        return number + 1;
    }

    private Map<Integer, String> command(String tick) {
        List<String> actions = Arrays.asList(tick.split("&"));
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

}
