package net.tetris.services;

import net.tetris.dom.Levels;
import net.tetris.levels.AllFigureLevels;
import net.tetris.levels.EasyLevels;
import net.tetris.levels.HardLevels;
import net.tetris.levels.LevelsFactory;
import org.junit.Test;

import java.util.LinkedList;
import java.util.Set;

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertSame;

public class LevelsFactoryTest {

    private LevelsFactory factory = new LevelsFactory();

    @Test
    public void shouldReturnAllLevels(){
        Set<Class<? extends Levels>> levels = factory.getAllLevelsInPackage();

        assertThat(levels).contains(EasyLevels.class, HardLevels.class, AllFigureLevels.class);
    }

    @Test
    public void shouldLoadClass(){
        Levels loaded = factory.getGameLevels("HardLevels", new PlayerFigures());
        assertEquals(HardLevels.class, loaded.getClass());
    }

}
