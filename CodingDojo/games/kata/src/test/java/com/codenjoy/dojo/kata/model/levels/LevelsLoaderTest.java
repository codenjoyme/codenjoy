package com.codenjoy.dojo.kata.model.levels;

import javassist.Modifier;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by indigo on 2017-03-05.
 */
public class LevelsLoaderTest {
    @Test
    public void test() {
        List<Level> levels = LevelsLoader.getAlgorithmsClasses();
        assertTrue(!levels.isEmpty());

        for (Level level : levels) {
            Class<? extends Level> aClass = level.getClass();

            assertFalse(Modifier.isAbstract(aClass.getModifiers()));
            assertFalse(Modifier.isInterface(aClass.getModifiers()));
        }
    }
}