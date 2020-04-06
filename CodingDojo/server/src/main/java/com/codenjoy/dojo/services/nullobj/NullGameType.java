package com.codenjoy.dojo.services.nullobj;

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
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public final class NullGameType implements GameType {

    public static final GameType INSTANCE = new NullGameType();

    private NullGameType() {
        // do nothing
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        throw exception();
    }

    private UnsupportedOperationException exception() {
        return new UnsupportedOperationException("Oops! Trying to use NullGameType...");
    }

    @Override
    public GameField createGame(int levelNumber) {
        throw exception();
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        throw exception();
    }

    @Override
    public String name() {
        return getClass().getSimpleName();
    }

    @Override
    public CharElements[] getPlots() {
        throw exception();
    }

    @Override
    public Settings getSettings() {
        throw exception();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return null;
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return null;
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        throw exception();
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        throw exception();
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        throw exception();
    }

    @Override
    public Dice getDice() {
        return null;
    }

    @Override
    public String getVersion() {
        throw exception();
    }

    @Override
    public void tick() {
        // do nothing
    }
}
