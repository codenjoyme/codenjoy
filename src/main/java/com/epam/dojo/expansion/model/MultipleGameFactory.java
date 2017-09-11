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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.RandomDice;
import com.epam.dojo.expansion.model.levels.Level;
import com.epam.dojo.expansion.model.levels.Levels;
import com.epam.dojo.expansion.model.levels.LevelsFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static java.util.stream.Collectors.toList;

/**
 * Created by Oleksandr_Baglai on 2017-09-01.
 */
// TODO test me
public class MultipleGameFactory implements GameFactory {

    private static Logger logger = DLoggerFactory.getLogger(MultipleGameFactory.class);

    private List<Expansion> rooms = new LinkedList<>();

    private LevelsFactory singleFactory;
    private LevelsFactory multipleFactory;

    private boolean waitingOthers = false;
    private Dice dice;

    public MultipleGameFactory(Dice dice,
                               LevelsFactory singleFactory,
                               LevelsFactory multipleFactory)
    {
        this.dice = dice;
        this.singleFactory = singleFactory;
        this.multipleFactory = multipleFactory;
    }

    @Override
    @NotNull
    public PlayerBoard get(boolean isMultiple, Predicate<? super Level> choseLevel) {
        if (isMultiple) {
            Expansion game = findFreeRandomMultiple(choseLevel);
            if (logger.isDebugEnabled()) {
                logger.debug("Try find free random multiple room {}", game);
            }

            if (game == null) {
                game = createNewMultiple(choseLevel);
                if (logger.isDebugEnabled()) {
                    logger.debug("Create new random multiple room {}", game);
                }
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

    @Nullable
    private Expansion findFreeRandomMultiple(Predicate<? super Level> choseLevel) {
        List<Expansion> free = getFreeMultipleRooms(choseLevel);
        if (free.isEmpty()) {
            return null;
        }
        int index = dice.next(free.size());
        Expansion expansion = free.get(index);
        return expansion;
    }

    @NotNull
    private List<Expansion> getFreeMultipleRooms(Predicate<? super Level> choseLevel) {
        return rooms.stream()
                .filter(Expansion::isFree)
                .filter(expansion -> choseLevel.test(expansion.getCurrentLevel()))
                .collect(toList());
    }

    @NotNull
    private Expansion createNewMultiple(Predicate<? super Level> choseLevel) {
        Level level = null;
        int counter = 10;
        while (level == null) {
            level = selectRandomLevelType();
            if (level == null && ++counter > 10) { //TODO think about it
                logger.error("Something wrong with levels. There are no levels loaded from *.lev. Used default!");
                level = Levels.collectMultiple(20, "MULTI1").get().get(0);
            }
        }
        Expansion game = new Expansion(Arrays.asList(level),
                new RandomDice(), Expansion.MULTIPLE);

        rooms.add(game);
        return game;
    }

    @NotNull
    private Level selectRandomLevelType() {
        List<Level> levels = multipleFactory.get();
        int index = dice.next(levels.size());
        Level level = levels.get(index);
        return level;
    }

    // это опция сеттинговая, она раз на всю игру
    public void setWaitingOthers(boolean waitingOthers) {
        this.waitingOthers = waitingOthers;
    }
}
