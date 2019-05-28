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


import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AlgorithmLevelImplTest {

    @Test
    public void testAlgorithmAnswersPrepared() {
        // given when
        AlgorithmLevelImpl level =
                new AlgorithmLevelImpl("question1", "question2", "question3") {
                    @Override
                    public int complexity() {
                        return 0;
                    }

                    @Override
                    public String get(String input) {
                        return "answer" + input.substring("question".length());
                    }

                    @Override
                    public String description() {
                        return "BlaBlaDescription";
                    }

                    @Override
                    public String author() {
                        return null;
                    }
                };

        // then
        assertEquals(3, level.size());
        assertEquals("[answer1, answer2, answer3]", level.getAnswers().toString());
    }

    @Test
    public void testIntFormat() {
        // given
        AlgorithmLevelImpl level = mock(AlgorithmLevelImpl.class);

        when(level.get(anyString())).thenCallRealMethod();

        // when
        level.get("1");

        // then
        verify(level).get(1);
    }

    @Test
    public void testIntsFormat() {
        // given
        AlgorithmLevelImpl level = mock(AlgorithmLevelImpl.class);

        when(level.get(anyString())).thenCallRealMethod();

        // when
        level.get("1, 2, 3");

        // then
        verify(level).get(1, 2, 3);
    }

    @Test
    public void testStringsFormat() {
        // given
        AlgorithmLevelImpl level = mock(AlgorithmLevelImpl.class);

        when(level.get(anyString())).thenCallRealMethod();

        // when
        level.get("qwe, asd, zxc");

        // then
        verify(level).get("qwe", "asd", "zxc");
    }

    @Test
    public void testStringFormat() {
        // given
        AlgorithmLevelImpl level = mock(AlgorithmLevelImpl.class);

        when(level.get(anyString())).thenCallRealMethod();

        // when
        try {
            level.get("qwe");
            fail("expected exception");
        } catch (IllegalStateException e) {
            // then
            assertEquals("You should override one of 'get' methods", e.getMessage());
        }
    }

    static class TestAlgorithm extends AlgorithmLevelImpl {

        public TestAlgorithm(String... input) {
            super(input);
        }

        @Override
        public String description() {
            return null;
        }

        @Override
        public String author() {
            return null;
        }

        @Override
        public int complexity() {
            return 0;
        }
    }

    @Test
    public void shouldConstruct_inputIsEmpty_noOverride() {
        TestAlgorithm algorithm = new TestAlgorithm();

        assertEquals("[1, 2, 3, 4, 5, 6, 7, 8, 9, 10, " +
                "11, 12, 13, 14, 15, 16, 17, 18, 19, " +
                "20, 21, 22, 23, 24, 25]",
                algorithm.getQuestions().toString());
    }

    @Test
    public void shouldConstruct_inputIsEmpty_withOverride() {
        TestAlgorithm algorithm = new TestAlgorithm() {
            @Override
            public List<String> getQuestions() {
                return Arrays.asList("1", "2");
            }
        };

        assertEquals("[1, 2]",
                algorithm.getQuestions().toString());
    }

    @Test
    public void shouldConstruct_inputIsNotEmpty_withOverride() {
        TestAlgorithm algorithm = new TestAlgorithm("1", "2", "3", "4") {
            @Override
            public List<String> getQuestions() {
                return Arrays.asList("5", "6");
            }
        };

        assertEquals("[5, 6]",
                algorithm.getQuestions().toString());
    }

    @Test
    public void shouldConstruct_inputIsNotEmpty_noOverride() {
        TestAlgorithm algorithm = new TestAlgorithm("1", "2", "3", "4");

        assertEquals("[1, 2, 3, 4]",
                algorithm.getQuestions().toString());
    }

}
