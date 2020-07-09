package com.codenjoy.dojo.sokoban.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.sokoban.client.Board;
import com.codenjoy.dojo.sokoban.client.ai.ApofigSolver;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.sokoban.helper.TextIOHelper;
import com.codenjoy.dojo.sokoban.model.game.Sokoban;
import com.codenjoy.dojo.sokoban.model.items.Level;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Elements;
import com.codenjoy.dojo.sokoban.model.itemsImpl.LevelImpl;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 */
public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;

    public GameRunner() {
        new Scores(0, settings);
        level = new LevelImpl(getMap());
    }

    protected String getMap() {
        if (Storage.levels.size()>0)
        return TextIOHelper.getStringFromResourcesRtf(Storage.levels.get("PlayerFirst")); //TODO player has to be real, not PlayerFirst as dummy
        else
            return TextIOHelper.getStringFromResourcesRtf(1);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Sokoban(level, getDice());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "sokoban";
    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener, playerId);
    }

    @Override
    public Class<? extends Solver> getAI() {
        return ApofigSolver.class;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return Board.class;
    }
}
