package com.codenjoy.dojo.kata.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LevelsPoolImplTest {

    private Level level1 = mock(Level.class);
    private Level level2 = mock(Level.class);

    @Before
    public void setUp() throws Exception {
        setupLevel(level1, "d1", "q1", "q2", "q3");
        setupLevel(level2, "d2", "q4", "q5");
    }

    private void setupLevel(Level level, String description, String... data) {
        when(level.getQuestions()).thenReturn(Arrays.asList(data));
        when(level.size()).thenReturn(data.length);
        when(level.description()).thenReturn(description);
    }

    @Test
    public void shouldQuestionOneByOne_whenSingleLevel() {
        // given
        List<Level> levels = Arrays.asList(level1);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());
    }

    @Test
    public void shouldQuestionOneByOne_whenMultipleLevel() {
        // given
        List<Level> levels = Arrays.asList(level1, level2);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());
    }

    @Test
    public void shouldQuestionOneByOne_whenMultipleLevel_reverseOrder() {
        // given
        List<Level> levels = Arrays.asList(level2, level1);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());
    }

    @Test
    public void shouldGetAnswersFromLevel() {
        // given
        List<Level> levels = Arrays.asList(level1);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        when(level1.getAnswers()).thenReturn(Arrays.asList("a1", "a2", "a3"));

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals("[a1]", pool.getAnswers().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals("[a1, a2]", pool.getAnswers().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals("[a1, a2, a3]", pool.getAnswers().toString());

        // when
        assertEquals(true, pool.isLevelFinished());
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals("[]", pool.getAnswers().toString());
    }

    @Test
    public void shouldWork_isLastQuestion_caseOneLevel() {
        // given
        List<Level> levels = Arrays.asList(level1);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());
    }

    @Test
    public void shouldWork_isLastQuestion_caseTwoLevels() {
        // given
        List<Level> levels = Arrays.asList(level1, level2);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());
        assertEquals(false, pool.isLevelFinished());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());

        // when
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLevelFinished());
    }

    @Test
    public void shouldWork_getLevelIndex_getQuestionIndex() {
        // given
        List<Level> levels = Arrays.asList(level1, level2);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals(0, pool.getLevelIndex());
        assertEquals(0, pool.getQuestionIndex());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(0, pool.getLevelIndex());
        assertEquals(1, pool.getQuestionIndex());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(0, pool.getLevelIndex());
        assertEquals(2, pool.getQuestionIndex());

        // when
        assertEquals(true, pool.isLevelFinished());
        pool.nextLevel();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());
        assertEquals(1, pool.getLevelIndex());
        assertEquals(0, pool.getQuestionIndex());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals(1, pool.getLevelIndex());
        assertEquals(1, pool.getQuestionIndex());

        // when
        assertEquals(true, pool.isLevelFinished());
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(2, pool.getLevelIndex());
        assertEquals(0, pool.getQuestionIndex());
    }

    @Test
    public void shouldgetDescriptionFromLevel() {
        // given
        List<Level> levels = Arrays.asList(level1, level2);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals("d1", pool.getDescription());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals("d1", pool.getDescription());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals("d1", pool.getDescription());

        // when
        assertEquals(true, pool.isLevelFinished());
        pool.nextLevel();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());
        assertEquals("d2", pool.getDescription());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals("d2", pool.getDescription());

        // when
        assertEquals(true, pool.isLevelFinished());
        pool.nextLevel();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals("No more Levels. You win!", pool.getDescription());
    }
}
