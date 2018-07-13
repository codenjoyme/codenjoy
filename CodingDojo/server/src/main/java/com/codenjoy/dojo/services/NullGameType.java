package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class NullGameType implements GameType {

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
    public GameField createGame() {
        throw exception();
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        throw exception();
    }

    @Override
    public String name() {
        throw exception();
    }

    @Override
    public Enum[] getPlots() {
        throw exception();
    }

    @Override
    public Settings getSettings() {
        throw exception();
    }

    @Override
    public MultiplayerType getMultiplayerType() {
        throw exception();
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String save, String playerName) {
        throw exception();
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        throw exception();
    }

    @Override
    public boolean newAI(String aiName) {
        throw exception();
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
