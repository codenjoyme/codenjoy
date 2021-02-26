package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class RoomGameType implements GameType {

    private final GameType type;
    private final Settings settings;

    public RoomGameType(GameType type) {
        this.type = type;
        this.settings = type.getSettings();
    }

    public GameType getWrapped() {
        return type;
    }

    @Override
    public Settings getSettings() {
        return settings;
    }

    @Override
    public String toString() {
        return "RoomGameType{" +
                "type=" + type +
                ", settings=" + settings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        // не переопределять
        return this == o;
    }

    @Override
    public int hashCode() {
        // не переопределять
        return super.hashCode();
    }

    @Override
    public PlayerScores getPlayerScores(Object score, Settings settings) {
        return type.getPlayerScores(score, this.settings);
    }

    @Override
    public GameField createGame(int levelNumber, Settings settings) {
        return type.createGame(levelNumber, this.settings);
    }

    @Override
    public Parameter<Integer> getBoardSize(Settings settings) {
        return type.getBoardSize(this.settings);
    }

    @Override
    public String name() {
        return type.name();
    }

    @Override
    public CharElements[] getPlots() {
        return type.getPlots();
    }

    @Override
    public Class<? extends Solver> getAI() {
        return type.getAI();
    }

    @Override
    public Class<? extends ClientBoard> getBoard() {
        return type.getBoard();
    }

    @Override
    public String getVersion() {
        return type.getVersion();
    }

    @Override
    public MultiplayerType getMultiplayerType(Settings settings) {
        return type.getMultiplayerType(this.settings);
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId, Settings settings) {
        return type.createPlayer(listener, playerId, this.settings);
    }

    @Override
    public Dice getDice() {
        return type.getDice();
    }

    @Override
    public PrinterFactory getPrinterFactory() {
        return type.getPrinterFactory();
    }

    @Override
    public void tick() {
        type.tick();
    }
}