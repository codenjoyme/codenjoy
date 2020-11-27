package com.codenjoy.dojo.tetris.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.Figures;
import com.codenjoy.dojo.tetris.model.Levels;
import com.codenjoy.dojo.tetris.model.levels.level.*;

import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

/**
 * Created by Sergii_Zelenin on 7/17/2016.
 */
public class LevelsFactory {

    public List<String> allLevels() {
        return allLevelsClasses().stream()
                .map(clazz -> clazz.getSimpleName())
                .sorted()
                .collect(toList());
    }

    private Set<Class<? extends Levels>> allLevelsClasses() {
        return new HashSet<Class<? extends Levels>>(){{
            add(EasyLevels.class);
            add(HardLevels.class);
            add(ProbabilityWithoutOverflownLevels.class);
            add(ProbabilityLevels.class);
            add(AllFigureLevels.class);
        }};
    }

    public Levels createLevels(String level, Dice dice, FigureQueue playerQueue) {
        String className = LevelsFactory.class.getPackage().getName() + ".level." + level;
        try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
            Constructor<?> constructor = aClass.getConstructor(Dice.class, FigureQueue.class);
            Object result = constructor.newInstance(dice, playerQueue);
            return (Levels)result;
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Error during load game levels", e);
        }
    }

}
