package com.codenjoy.dojo.tetris.model;

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
import org.fest.reflect.core.Reflection;
import org.reflections.Reflections;

import java.util.Set;

/**
 * Created by Sergii_Zelenin on 7/17/2016.
 */
public class LevelsFactory {
    public Set<Class<? extends Levels>> getAllLevelsInPackage() {
        Reflections reflections = new Reflections(LevelsFactory.class.getPackage().getName());

        return reflections.getSubTypesOf(Levels.class);
    }

    public Levels getGameLevels(Dice dice, FigureQueue playerQueue, String levels) {
        String className = LevelsFactory.class.getPackage().getName() + '.' + levels;
        try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
            return (Levels) Reflection.constructor()
                    .withParameterTypes(Dice.class, PlayerFigures.class)
                    .in(aClass)
                    .newInstance(dice, playerQueue);
        } catch (ClassNotFoundException e) {
            return throwRuntime(e);
        }
    }

    private Levels throwRuntime(Exception e) {
        throw new RuntimeException("Error during load game levels", e);
    }
}
