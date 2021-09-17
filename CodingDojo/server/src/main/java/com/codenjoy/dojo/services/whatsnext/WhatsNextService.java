package com.codenjoy.dojo.services.whatsnext;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerCommand;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.info.Information;
import com.codenjoy.dojo.services.info.ScoresCollector;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.settings.Settings;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;

@Component
public class WhatsNextService {

    // не хотелось рефакторить этот метод, дабы в нем было видно что делает сервер,
    // чтобы создать игру
    public String calculate(GameType gameType, String board, String allActions) {
        Settings settings = gameType.getSettings();
        GameField game = gameType.createGame(0, settings);
        List<Information> infos = new LinkedList<>();
        List<GamePlayer> players  = game.load(board, () -> {
            PlayerScores scores = gameType.getPlayerScores(1000, settings);
            Information listener = new ScoresCollector(scores);
            infos.add(listener);
            GamePlayer player = gameType.createPlayer(
                    listener, DEFAULT_TEAM_ID,
                    StringUtils.EMPTY, settings);
            return player;
        });

        List<Single> singles = new LinkedList<>();
        players.forEach(player -> {
            Single single = new Single(player, gameType.getPrinterFactory());
            single.on(game);
            single.newGame();
            singles.add(single);
        });

        ResultPrinter printer = new ResultPrinter(game.reader().size());
        printer.initialHeader();
        printer.board(infos, singles);

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

            printer.tickHeader(tick);
            printer.board(infos, singles);
        }
        printer.breakLine();
        return printer.toString();
    }

    public static int countFromOne(int number) {
        return number + 1;
    }

}
