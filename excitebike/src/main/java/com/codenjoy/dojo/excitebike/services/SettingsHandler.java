package com.codenjoy.dojo.excitebike.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.services.generation.GenerationOption;
import com.codenjoy.dojo.excitebike.services.generation.WeightedRandomBag;
import com.codenjoy.dojo.services.settings.SettingsImpl;

import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.NOTHING;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.OBSTACLE_CHAIN;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.SINGLE_ELEMENT;
import static com.codenjoy.dojo.excitebike.services.generation.GenerationOption.SPRINGBOARD;

/**
 * Created by Pavel Bobylev 7/22/2019
 */
public class SettingsHandler {

    private static final String GENERATION_WEIGHT_NOTHING = "Spawn weight: nothing";
    private static final String GENERATION_WEIGHT_SINGLE_ELEMENT = "Spawn weight: single element";
    private static final String GENERATION_WEIGHT_SPRINGBOARD = "Spawn weight: springboard";
    private static final String GENERATION_WEIGHT_OBSTACLE_CHAIN = "Spawn weight: obstacle chain";
    private static final String WIN_SCORE = "Win score";
    private static final String LOSE_SCORE = "Lose score";
    private static final int DEFAULT_GENERATION_WEIGHT_NOTHING = 10;
    private static final int DEFAULT_GENERATION_WEIGHT_SINGLE_ELEMENT = 5;
    private static final int DEFAULT_GENERATION_WEIGHT_SPRINGBOARD = 2;
    private static final int DEFAULT_GENERATION_WEIGHT_OBSTACLE_CHAIN = 2;
    private static final int DEFAULT_WIN_SCORE = 1;
    private static final int DEFAULT_LOSE_SCORE = 1;
    private final SettingsImpl settings = new SettingsImpl();

    public SettingsHandler() {
        settings.addEditBox(GENERATION_WEIGHT_NOTHING).type(Integer.class).def(DEFAULT_GENERATION_WEIGHT_NOTHING);
        settings.addEditBox(GENERATION_WEIGHT_SINGLE_ELEMENT).type(Integer.class).def(DEFAULT_GENERATION_WEIGHT_SINGLE_ELEMENT);
        settings.addEditBox(GENERATION_WEIGHT_SPRINGBOARD).type(Integer.class).def(DEFAULT_GENERATION_WEIGHT_SPRINGBOARD);
        settings.addEditBox(GENERATION_WEIGHT_OBSTACLE_CHAIN).type(Integer.class).def(DEFAULT_GENERATION_WEIGHT_OBSTACLE_CHAIN);
        settings.addEditBox(WIN_SCORE).type(Integer.class).def(DEFAULT_WIN_SCORE);
        settings.addEditBox(LOSE_SCORE).type(Integer.class).def(DEFAULT_LOSE_SCORE);
    }

    public SettingsImpl getSettings() {
        return settings;
    }

    public WeightedRandomBag<GenerationOption> getWeightedRandomBag() {
        WeightedRandomBag<GenerationOption> weightedRandomBag = new WeightedRandomBag<>();
        weightedRandomBag.addEntry(NOTHING, (Integer) settings.getParameter(GENERATION_WEIGHT_NOTHING).getValue());
        weightedRandomBag.addEntry(SINGLE_ELEMENT, (Integer) settings.getParameter(GENERATION_WEIGHT_SINGLE_ELEMENT).getValue());
        weightedRandomBag.addEntry(SPRINGBOARD, (Integer) settings.getParameter(GENERATION_WEIGHT_SPRINGBOARD).getValue());
        weightedRandomBag.addEntry(OBSTACLE_CHAIN, (Integer) settings.getParameter(GENERATION_WEIGHT_OBSTACLE_CHAIN).getValue());
        return weightedRandomBag;
    }

    public Integer getWinScore() {
        return (Integer) settings.getParameter(WIN_SCORE).getValue();
    }

    public Integer getLoseScore() {
        return (Integer) settings.getParameter(LOSE_SCORE).getValue();
    }
}
