package com.codenjoy.dojo.lemonade.services;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.games.lemonade.Board;
import com.codenjoy.dojo.games.lemonade.WeatherForecast;
import com.codenjoy.dojo.lemonade.model.Lemonade;
import com.codenjoy.dojo.lemonade.model.Player;
import com.codenjoy.dojo.lemonade.services.ai.AISolver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import org.json.JSONObject;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType<GameSettings> {

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public Lemonade createGame(int levelNumber, GameSettings settings) {
        return new Lemonade(settings);
    }

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        return new Scores(Integer.valueOf(score.toString()), settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return v(25);
    }

    @Override
    public String name() {
        return "lemonade";
    }

    @Override
    public CharElement[] getPlots() {
        return WeatherForecast.values();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return AISolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }

    @Override
    public MultiplayerType getMultiplayerType(GameSettings settings) {
        return MultiplayerType.SINGLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, int teamId, String playerId, GameSettings settings) {
        return new Player(listener, getDice().next(Integer.MAX_VALUE), settings).inTeam(teamId);
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return PrinterFactory.get((BoardReader reader, Player player) -> {
            JSONObject result = player.getNextQuestion();
            result.put("history", player.getHistoryJson());
            return result;
        });
    }
}
