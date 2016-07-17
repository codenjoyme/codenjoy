package com.codenjoy.dojo.tetris.model;

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

    public Levels getGameLevels(FigureQueue playerQueue, String levels) {
        String className = LevelsFactory.class.getPackage().getName() + '.' + levels;
        try {
            Class<?> aClass = this.getClass().getClassLoader().loadClass(className);
            return (Levels) Reflection.constructor().withParameterTypes(PlayerFigures.class).in(aClass).newInstance(playerQueue);
        } catch (ClassNotFoundException e) {
            return throwRuntime(e);
        }
    }

    private Levels throwRuntime(Exception e) {
        throw new RuntimeException("Error during load game levels", e);
    }
}
