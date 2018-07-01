package com.codenjoy.dojo.battlecity.services;

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


import com.codenjoy.dojo.battlecity.client.ai.ApofigSolver;
import com.codenjoy.dojo.battlecity.model.Battlecity;
import com.codenjoy.dojo.battlecity.model.Elements;
import com.codenjoy.dojo.battlecity.model.Single;
import com.codenjoy.dojo.battlecity.model.Tank;
import com.codenjoy.dojo.battlecity.model.levels.Level;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private Battlecity tanks;
    private Level level;

    public GameRunner() {
        new Scores(0, settings); // TODO сеттринги разделены по разным классам, продумать архитектуру

        level = new Level();
    }

    private Battlecity newTank() {
        return new Battlecity(level.size(),
                level.getConstructions(),
                level.getBorders(),
                level.getTanks().toArray(new Tank[0]));
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer) score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save, String playerName) {
        if (getMultiplayerType().isSingle() || tanks == null) {
            tanks = newTank();
        }
        Game game = new Single(tanks, listener, factory, new RandomDice());
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(level.size());
    }

    @Override
    public String name() {
        return "battlecity";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        return MultiplayerType.MULTIPLE;
    }

    @Override
    public boolean newAI(String aiName) {
        ApofigSolver.start(aiName, WebSocketRunner.Host.REMOTE_LOCAL);
        return true;
    }
}
