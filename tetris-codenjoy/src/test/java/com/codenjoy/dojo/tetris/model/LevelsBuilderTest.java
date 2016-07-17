package com.codenjoy.dojo.tetris.model;

import org.junit.Test;

import static junit.framework.Assert.assertSame;
import static org.junit.Assert.assertEquals;

public class LevelsBuilderTest {

    private Levels levels;

    @Test
    public void shouldGetLevelsDataWhenCallLevelsReader() {
        levels = new MockLevels(new PlayerFigures());

        gotoNextLevel();

        Levels.LevelsReader reader = levels.new LevelsReader();
        assertEquals(1, reader.getCurrentLevel());
        assertEquals(4, reader.getTotalRemovedLines());
        assertEquals("MockLevels", reader.getLevelsName());
    }

    @Test
    public void shouldSetLevelsPropertiesWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.getLevels();

        assertEquals(1, levels.getCurrentLevelNumber());
        assertEquals(4, levels.getTotalRemovedLines());
        assertEquals(MockLevels.class, levels.getClass());
    }

    @Test
    public void shouldLevelsHaseOneQueueWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.getLevels();

        FigureQueue queue = levels.getCurrentLevel().getFigureQueue();

        gotoNextLevel();

        assertEquals(2, levels.getCurrentLevelNumber());
        assertSame(queue, levels.getCurrentLevel().getFigureQueue());
    }

    private Levels.LevelsBuilder getBuilder(int currentLevel, int totalRemovedLines) {
        Levels.LevelsBuilder builder = new Levels.LevelsBuilder();
        builder.setCurrentLevel(currentLevel);
        builder.setLevelsName(MockLevels.class.getSimpleName());
        builder.setTotalRemovedLines(totalRemovedLines);
        return builder;
    }

    @Test
    public void shouldApplyCurrentLevelWhenBuildLevels() {
        Levels.LevelsBuilder builder = getBuilder(1, 4);

        levels = builder.getLevels();

        FigureQueue queue = levels.getCurrentLevel().getFigureQueue();
        retrieveAllFiguresFromPreviousLevel(queue);
        assertEquals(Figure.Type.O, queue.next().getType());
        assertEquals(Figure.Type.O, queue.next().getType());
        assertEquals(Figure.Type.O, queue.next().getType());
    }

    private void retrieveAllFiguresFromPreviousLevel(FigureQueue queue) {
        for (int i = 0; i < PlayerFigures.DEFAULT_FUTURE_COUNT; i++) {
            queue.next();
        }
    }

    private void gotoNextLevel() {
        levels.linesRemoved(MockLevels.LINES_REMOVED_FOR_NEXT_LEVEL);
    }


}
