package com.epam.dojo.icancode.services;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ICanCode;
import com.epam.dojo.icancode.model.Single;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;

/**
 * Генератор игор - реализация {@see GameType}
 * Обрати внимание на {@see GameRunner#SINGLE} - там реализовано переключение в режимы "все на одном поле"/"каждый на своем поле"
 */
public class GameRunner extends AbstractGameType implements GameType  {

    private Parameter<Integer> isTrainingMode;
    private ICanCode multiple;

    public GameRunner() {
        setupSettings();
        multiple = new ICanCode(Levels.collectMultiple(), new RandomDice(), ICanCode.MULTIPLE);
    }

    private void setupSettings() {
        new Scores(0, settings);
        isTrainingMode = settings.addEditBox("Is training mode").type(Integer.class).def(1);
    }

    private ICanCode newSingleGame() {
        return new ICanCode(Levels.collectSingle(), new RandomDice(), ICanCode.SINGLE);
    }

    @Override
    public PlayerScores getPlayerScores(int score) {
        return new Scores(score, settings);
    }

    @Override
    public Game newGame(EventListener listener, PrinterFactory factory, String save) {
        if (isTrainingMode.getValue() == 0) {
            int total = Levels.collectSingle().size();
            save = "{'total':" + total + ",'current':0,'lastPassed':" + (total - 1) + ",'multiple':true}";
        }
        Game single = new Single(newSingleGame(), multiple, listener, factory, save);
        single.newGame();
        return single;
    }

    @Override
    public Parameter<Integer> getBoardSize() {
        return v(Levels.size());
    }

    @Override
    public String name() {
        return "icancode";
    }

    @Override
    public Enum[] getPlots() {
        return Elements.values();
    }

}
