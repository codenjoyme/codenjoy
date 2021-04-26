package com.codenjoy.dojo.services.whatsnext;

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

import static java.util.stream.Collectors.*;

@Component
public class WhatsNextService {

    // не хотелось рефакторить этот метод, дабы в нем было видно что делает сервер,
    // чтобы создать игру
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
        results.add(printer.printBoard(infos, singles));

        ActionsParser parser = new ActionsParser(allActions);
        List<Map<Integer, String>> ticksActions = parser.getTicksActions();
        for (int tick = 0; tick < ticksActions.size(); tick++) {
            Map<Integer, String> actions = ticksActions.get(tick);
            for (int index = 0; index < singles.size(); index++) {
                Single single = singles.get(index);
                String action = actions.get(countFromOne(index));
                if (StringUtils.isNotEmpty(action)) {
                    new PlayerCommand(single.getJoystick(), action).execute();
                }
            }

            game.tick();

            results.add(printer.printTickHeader(tick));

            results.add(printer.printBoard(infos, singles));
        }
        return printer.breakLine() +
                results.stream().collect(joining(""));
    }

    public static int countFromOne(int number) {
        return number + 1;
    }

}
