package com.codenjoy.dojo.battlecity.services;

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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;

public class OptionGameSettings implements GameSettings {

    private final Dice dice;

    private final Parameter<Integer> spawnAiPrize;
    private final Parameter<Integer> hitKillsAiPrize;
    private final Parameter<Integer> prizeOnField;
    private final Parameter<Integer> prizeWorking;
    private final Parameter<Integer> slipperiness;
    private final Parameter<Integer> aiTicksPerShoot;
    private final Parameter<Integer> tankTicksPerShoot;
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
        prizeOnField = settings.addEditBox("The period of prize validity on the field after the appearance").type(Integer.class).def(3);
        prizeWorking = settings.addEditBox("Working time of the prize after catch up").type(Integer.class).def(3);
        aiTicksPerShoot = settings.addEditBox("Ticks until the next AI Tank shoot").type(Integer.class).def(10);
        tankTicksPerShoot = settings.addEditBox("Ticks until the next Tank shoot").type(Integer.class).def(4);
        slipperiness = settings.addEditBox("Value of tank sliding on ice").type(Integer.class).def(3);
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

    @Override
    public Parameter<Integer> slipperiness() {
        return slipperiness;
    }

    @Override
    public Parameter<Integer> aiTicksPerShoot() {
        return aiTicksPerShoot;
    }

    @Override
    public Parameter<Integer> tankTicksPerShoot() {
        return tankTicksPerShoot;
    }
}
