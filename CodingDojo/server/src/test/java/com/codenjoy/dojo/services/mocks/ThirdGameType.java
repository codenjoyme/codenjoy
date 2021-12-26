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


import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.printer.CharElement;
import com.codenjoy.dojo.services.settings.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER5;
import static com.codenjoy.dojo.services.mocks.ThirdGameSettings.Keys.PARAMETER6;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_ENABLED;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_PLAYERS_PER_ROOM;

public class ThirdGameType extends FakeGameType {

    @Override
    public SettingsImpl getSettings() {
        return new ThirdGameSettings();
    }

    @Override
    public Parameter<Integer> getBoardSize(Settings settings) {
        return new SimpleParameter<>(111);
    }

    @Override
    public String name() {
        return "third";
    }

    public enum Element implements CharElement {

        ZERRO('0'),
        ONE('1'),
        TWO('2'),
        THREE('3');

        final char ch;

        Element(char ch) {
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
    public CharElement[] getPlots() {
        return Element.values();
    }

    @Override
    public MultiplayerType getMultiplayerType(Settings settings) {
        SettingsReader reader = (SettingsReader) settings;
        if (reader.bool(ROUNDS_ENABLED)) {
            return MultiplayerType.TEAM.apply(
                    reader.integer(ROUNDS_PLAYERS_PER_ROOM),
                    MultiplayerType.DISPOSABLE);
        } else {
            if (reader.bool(PARAMETER6)){
                return MultiplayerType.ALL_SINGLE.apply(reader.integer(PARAMETER5));
            } else {
                return MultiplayerType.MULTIPLE;
            }
        }
    }

    @Override
    public Point heroAt() {
        int x = getBoardSize(getSettings()).getValue() - 1;
        return pt(x, x);
    }

    @Override
    public CharElement getHeroElement() {
        return Element.ONE;
    }

    @Override
    public String getVersion() {
        return "version 45";
    }
}
