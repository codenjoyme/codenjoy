package com.codenjoy.dojo.loderunner.services;

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
import com.codenjoy.dojo.games.loderunner.Board;
import com.codenjoy.dojo.games.loderunner.Element;
import com.codenjoy.dojo.loderunner.model.Loderunner;
import com.codenjoy.dojo.loderunner.model.Player;
import com.codenjoy.dojo.loderunner.services.ai.AISolver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType<GameSettings> {

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        return new Scores(Integer.valueOf(score.toString()), settings);
    }

    @Override
    public GameField createGame(int levelNumber, GameSettings settings) {
        return new Loderunner(settings.level(getDice()), getDice(), settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return v(settings.level(getDice()).getSize());
    }

    @Override
    public String name() {
        return "loderunner";
    }

    @Override
    public CharElement[] getPlots() {
        return Element.values();
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
        if (settings.bool(ROUNDS_ENABLED)) {
            return MultiplayerType.TEAM.apply(
                    settings.integer(ROUNDS_PLAYERS_PER_ROOM),
                    MultiplayerType.DISPOSABLE);
        } else {
            return MultiplayerType.MULTIPLE;
        }
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, int teamId, String playerId, GameSettings settings) {
        return new Player(listener, settings).inTeam(teamId);
    }
}
