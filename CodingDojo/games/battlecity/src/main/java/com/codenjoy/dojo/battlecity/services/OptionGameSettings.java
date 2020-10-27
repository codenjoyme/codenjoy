package com.codenjoy.dojo.battlecity.services;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.EditBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class OptionGameSettings implements GameSettings {

    private final Dice dice;

    private final Parameter<Integer> spawnAiPrize;
    private final Parameter<Integer> hitKillsAiPrize;
    private final Parameter<Integer> prizeOnField;
    private final Parameter<Integer> prizeWorking;
    private final Parameter<Integer> killYourTankPenalty;
    private final Parameter<Integer> killOtherHeroTankScore;
    private final Parameter<Integer> killOtherAITankScore;

    public OptionGameSettings(Settings settings, Dice dice) {
        this.dice = dice;

        killYourTankPenalty = settings.addEditBox("Kill your tank penalty").type(Integer.class).def(0);
        killOtherHeroTankScore = settings.addEditBox("Kill other hero tank score").type(Integer.class).def(50);
        killOtherAITankScore = settings.addEditBox("Kill other AI tank score").type(Integer.class).def(25);

        spawnAiPrize = settings.addEditBox("Count spawn for AI Tank with prize").type(Integer.class).def(4);
        hitKillsAiPrize = settings.addEditBox("Hits to kill AI Tank with prize").type(Integer.class).def(3);
        prizeOnField = settings.addEditBox("Prize must be on the field").type(Integer.class).def(3);
        prizeWorking = settings.addEditBox("Working time of the prize").type(Integer.class).def(3);
    }

    @Override
    public Dice getDice() {
        return dice;
    }

    @Override
    public Parameter<Integer> spawnAiPrize() {
        return spawnAiPrize;
    }

    @Override
    public Parameter<Integer> hitKillsAiPrize() {
        return hitKillsAiPrize;
    }

    @Override
    public Parameter<Integer> killYourTankPenalty() {
        return killYourTankPenalty;
    }

    @Override
    public Parameter<Integer> killOtherHeroTankScore() {
        return killOtherHeroTankScore;
    }

    @Override
    public Parameter<Integer> killOtherAITankScore() {
        return killOtherAITankScore;
    }

    @Override
    public Parameter<Integer> prizeOnField() {
        return prizeOnField;
    }

    @Override
    public Parameter<Integer> prizeWorking() {
        return prizeWorking;
    }
}
