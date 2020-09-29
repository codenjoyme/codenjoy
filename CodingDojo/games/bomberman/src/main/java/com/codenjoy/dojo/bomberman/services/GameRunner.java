package com.codenjoy.dojo.bomberman.services;

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


import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.bomberman.client.ai.AISolver;
import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.bomberman.model.Elements;
import com.codenjoy.dojo.bomberman.model.GameSettings;
import com.codenjoy.dojo.bomberman.model.Player;
import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;

public class GameRunner extends AbstractGameType implements GameType {

    public static final String GAME_NAME = "bomberman";
    private GameSettings gameSettings;

    public GameRunner() {
        gameSettings = getGameSettings();
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, gameSettings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Bomberman(gameSettings);
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return gameSettings.getBoardSize();
    }

    @Override
    public String name() {
        return GAME_NAME;
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
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
    public MultiplayerType getMultiplayerType() {
        if (gameSettings.isMultiple().getValue()) {
            return MultiplayerType.MULTIPLE;
        } else {
            return MultiplayerType.TEAM.apply(
                    gameSettings.getPlayersPerRoom().getValue(),
                    MultiplayerType.DISPOSABLE
            );
        }
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener, gameSettings.getRoundSettings().roundsEnabled());
    }

    protected GameSettings getGameSettings() {
        return new OptionGameSettings(settings, getDice());
    }
}
