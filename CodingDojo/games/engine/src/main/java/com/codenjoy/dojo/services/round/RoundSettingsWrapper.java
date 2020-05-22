package com.codenjoy.dojo.services.round;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class RoundSettingsWrapper {

    private Parameter<Boolean> roundsEnabled;
    private Parameter<Integer> timePerRound;
    private Parameter<Integer> timeForWinner;
    private Parameter<Integer> timeBeforeStart;
    private Parameter<Integer> roundsPerMatch;
    private Parameter<Integer> minTicksForWin;

    protected RoundSettingsWrapper() {
        // do nothing, for testing only
    }

    public RoundSettingsWrapper(Settings settings) {
        roundsEnabled = settings.addCheckBox("[Rounds] Enabled").type(Boolean.class).def(true);
        timePerRound = settings.addEditBox("[Rounds] Time per Round").type(Integer.class).def(300);
        timeForWinner = settings.addEditBox("[Rounds] Time for Winner").type(Integer.class).def(1);
        timeBeforeStart = settings.addEditBox("[Rounds] Time before start Round").type(Integer.class).def(5);
        roundsPerMatch = settings.addEditBox("[Rounds] Rounds per Match").type(Integer.class).def(1);
        minTicksForWin = settings.addEditBox("[Rounds] Min length for win").type(Integer.class).def(40);
    }

    public Parameter<Integer> timeBeforeStart() {
        return timeBeforeStart;
    }

    public Parameter<Integer> roundsPerMatch() {
        return roundsPerMatch;
    }

    public Parameter<Integer> minTicksForWin() {
        return minTicksForWin;
    }

    public Parameter<Integer> timePerRound() {
        return timePerRound;
    }

    public Parameter<Integer> timeForWinner() {
        return timeForWinner;
    }

    public Parameter<Boolean> roundsEnabled() {
        return roundsEnabled;
    }
}
