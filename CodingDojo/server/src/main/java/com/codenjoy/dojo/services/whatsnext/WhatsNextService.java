package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.InformationCollector;
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

import static java.util.stream.Collectors.*;

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

        List<String> results = new LinkedList<>();

        ResultPrinter printer = new ResultPrinter(game.reader().size());
        results.add(printer.printInitialHeader());
        printer.printBoard(infos, singles, results);

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

            results.add(printer.printTickHeader(tick));

            printer.printBoard(infos, singles, results);
        }
        return printer.breakLine() +
                results.stream().collect(joining(""));
    }

    public static int countFromOne(int number) {
        return number + 1;
    }

    private Map<Integer, String> command(String tick) {
        List<String> actionsString = split(tick, "&", false);
        return actionsString.stream()
                .map(actionString -> {
                    Matcher matcher = ACTION_PATTERN.matcher(actionString);
                    if (matcher.matches()) {
                        String index = matcher.group(2);
                        String action = matcher.group(3);
                        return new HashMap.SimpleEntry<>(Integer.valueOf(index), action);
                    } else {
                        throw new RuntimeException("Unparsed action: " + actionString);
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
