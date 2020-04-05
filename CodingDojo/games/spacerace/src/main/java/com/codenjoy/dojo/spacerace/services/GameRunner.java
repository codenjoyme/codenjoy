package com.codenjoy.dojo.spacerace.services;

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
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.client.ai.AISolver;
import com.codenjoy.dojo.spacerace.model.*;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private final Level level;
    private final Parameter<Integer> ticksToRecharge;
    private final Parameter<Integer> bulletsCount;

    public GameRunner() {
        new Scores(0, settings);

        ticksToRecharge = settings.addEditBox("Ticks to recharge").type(Integer.class).def(30);
        bulletsCount = settings.addEditBox("Bullets count").type(Integer.class).def(10);

        level = new LevelImpl(getMap());
    }

    protected String getMap() {
        return "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼                            ☼" +
                "☼              ☺             ☼";
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public GameField createGame(int levelNumber) {
        return new Spacerace(level, getDice(), ticksToRecharge.getValue(), bulletsCount.getValue());
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.getSize());
    }

    @Override
    public String name() {
        return "spacerace";
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
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return new Player(listener);
    }
}
