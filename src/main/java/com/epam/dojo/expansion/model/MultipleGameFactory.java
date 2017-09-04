package com.epam.dojo.expansion.model;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.RandomDice;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
// TODO test me
public class MultipleGameFactory implements GameFactory {

    private List<Expansion> rooms = new LinkedList<>();

    private LevelsFactory singleFactory;
    private LevelsFactory multipleFactory;

    private boolean waitingOthers = false;

    public MultipleGameFactory(LevelsFactory singleFactory,
                               LevelsFactory multipleFactory)
    {
        this.singleFactory = singleFactory;
        this.multipleFactory = multipleFactory;
    }

    @Override
    public Expansion get(boolean isMultiple) {
        if (isMultiple) {
            Expansion game = findFreeMultiple();
            if (game == null) {
                game = createNewMultiple();
            }
            if (waitingOthers) {
                game.waitingOthers();
            }
            return game;
        } else {
            return new Expansion(singleFactory.get(),
                    new RandomDice(), Expansion.SINGLE);
        }
    }

    private Expansion findFreeMultiple() {
        for (Expansion game : rooms) {
            if (game.isNotBusy()) {
                return game;
            }
        }
        return null;
    }

    @NotNull
    private Expansion createNewMultiple() {
        Expansion game = new Expansion(multipleFactory.get(),
                new RandomDice(), Expansion.MULTIPLE);
        rooms.add(game);
        return game;
    }

    // это опция сеттинговая, она раз на всю игру
    public void setWaitingOthers(boolean waitingOthers) {
        this.waitingOthers = waitingOthers;
    }
}
