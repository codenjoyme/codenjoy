package com.codenjoy.dojo.kata.model.levels;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by indigo on 2017-03-05.
 */
public class LevelsPoolImplTest {

    private Level level1 = mock(Level.class);
    private Level level2 = mock(Level.class);

    @Before
    public void setUp() throws Exception {
        setupLevel(level1, "q1", "q2", "q3");
        setupLevel(level2, "q4", "q5");
    }

    private void setupLevel(Level level, String... data) {
        when(level.getQuestions()).thenReturn(Arrays.asList(data));
        when(level.size()).thenReturn(data.length);
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

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
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

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
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

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
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
        pool.nextQuestion();

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
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLastQuestion());
    }

    @Test
    public void shouldWork_isLastQuestion_caseTwoLevels() {
        // given
        List<Level> levels = Arrays.asList(level1, level2);
        LevelsPoolImpl pool = new LevelsPoolImpl(levels);
        pool.firstLevel();

        // then
        assertEquals("[q1]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q1, q2, q3]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[q4, q5]", pool.getQuestions().toString());
        assertEquals(false, pool.isLastQuestion());

        // when
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(true, pool.isLastQuestion());
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
        pool.nextQuestion();

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
        pool.nextQuestion();

        // then
        assertEquals("[]", pool.getQuestions().toString());
        assertEquals(2, pool.getLevelIndex());
        assertEquals(0, pool.getQuestionIndex());
    }
}