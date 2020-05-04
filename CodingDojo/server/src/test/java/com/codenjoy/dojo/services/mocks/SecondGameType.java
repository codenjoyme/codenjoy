package com.codenjoy.dojo.services.mocks;

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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.CharElements;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.services.settings.SimpleParameter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecondGameType extends AbstractGameType {

    private final SettingsImpl result;

    public SecondGameType() {
        result = new SettingsImpl();
        result.addEditBox("Parameter 3").type(Integer.class).def(43);
        result.addCheckBox("Parameter 4").type(Boolean.class).def(false).update(true);
    }

    @Override
    public PlayerScores getPlayerScores(Object score) {
        return new FakePlayerScores(score);
    }

    @Override
    public GameField createGame(int levelNumber) {
        GameField field = mock(GameField.class);
        BoardReader reader = mock(BoardReader.class);
        when(field.reader()).thenReturn(reader);
        return field;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return new SimpleParameter<>(56);
    }

    @Override
    public String name() {
        return "second";
    }

    public enum Elements implements CharElements {

        NONE(' '),
        RED('R'),
        GREEN('G'),
        BLUE('B');

        final char ch;

        Elements(char ch) {
            this.ch = ch;
        }

        @Override
        public char ch() {
            return ch;
        }

        @Override
        public String toString() {
            return String.valueOf(ch);
        }

    }

    @Override
    public CharElements[] getPlots() {
        return Elements.values();
    }

    @Override
    public Settings getSettings() {
        return result;
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
        return MultiplayerType.TRAINING.apply(10);
    }

    @Override
    public GamePlayer createPlayer(EventListener listener, String playerId) {
        return mock(GamePlayer.class);
    }
    
    @Override
    public String getVersion() {
        return "version 12";
    }
}
