package com.codenjoy.dojo.lunolet.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.lunolet.model.*;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.hero.GameMode;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

public class GameRunner extends AbstractGameType implements GameType {

    private LunoletGame game;
    private Level level;

    public GameRunner() {
        new Scores(0, settings);

        level = new Level();//TODO
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new Scores((Integer)score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String s, String s1) {
        game = new LunoletGame(level);

        Game game = new Single(this.game, listener, factory);
        game.newGame();
        return game;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(0);
    }

    @Override
    public String name() {
        return "lunolet";
    }

    @Override
    public Enum[] getPlots() {
        return new Enum[0];
    }

    @Override
    public boolean isSingleBoard() {
        return GameMode.NOT_SINGLE_MODE;
    }
}
