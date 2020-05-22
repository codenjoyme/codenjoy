package com.codenjoy.dojo.snakebattle.model.board.round;

import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class RoundSettingsWrapper {

    private final Parameter<Integer> timeBeforeStart;
    private final Parameter<Integer> roundsPerMatch;

    private final Parameter<Integer> minTicksForWin;
    private final Parameter<Integer> timePerRound;
    private final Parameter<Integer> timeForWinner;

    public RoundSettingsWrapper(Settings settings) {
        timePerRound = settings.addEditBox("Time per Round").type(Integer.class).def(300);
        timeForWinner = settings.addEditBox("Time for Winner").type(Integer.class).def(1);
        timeBeforeStart = settings.addEditBox("Time before start Round").type(Integer.class).def(5);
        roundsPerMatch = settings.addEditBox("Rounds per Match").type(Integer.class).def(1);
        minTicksForWin = settings.addEditBox("Min length for win").type(Integer.class).def(40);
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

}
