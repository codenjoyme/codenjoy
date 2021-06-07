package com.codenjoy.dojo.snake.services;

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
import com.codenjoy.dojo.services.AbstractGameType;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.games.snake.Board;
import com.codenjoy.dojo.snake.services.ai.AISolver;
import com.codenjoy.dojo.games.snake.Element;
import com.codenjoy.dojo.snake.model.Player;
import com.codenjoy.dojo.snake.model.Snake;
import com.codenjoy.dojo.snake.model.artifacts.BasicWalls;
import com.codenjoy.dojo.snake.model.artifacts.RandomArtifactGenerator;

import static com.codenjoy.dojo.snake.services.GameSettings.Keys.BOARD_SIZE;
import static com.codenjoy.dojo.snake.services.GameSettings.Keys.MAX_SCORE_MODE;

public class GameRunner extends AbstractGameType<GameSettings> {

    @Override
    public PlayerScores getPlayerScores(Object score, GameSettings settings) {
        // TODO этa настройка работает только, если удалить всех игроков и снова загрузить а надо сделать, чтобы reset all boards кнопка триггерила это тоже
        if (settings.bool(MAX_SCORE_MODE)) {
            return new MaxScores((Integer) score, settings);
        } else {
            return new Scores(Integer.valueOf(score.toString()), settings);
        }
    }

    @Override
    public GameField createGame(int levelNumber, GameSettings settings) {
        int size = settings.integer(BOARD_SIZE);

        return new Snake(new RandomArtifactGenerator(getDice()),
                new BasicWalls(size),
                size,
                settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(GameSettings settings) {
        return settings.integerValue(BOARD_SIZE);
    }

    @Override
    public String name() {
        return "snake";
    }

    @Override
    public CharElements[] getPlots() {
        return Element.values();
    }

    @Override
    public GameSettings getSettings() {
        return new GameSettings();
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
    public GamePlayer createPlayer(EventListener listener, String playerId, GameSettings settings) {
        return new Player(listener, settings);
    }
}
