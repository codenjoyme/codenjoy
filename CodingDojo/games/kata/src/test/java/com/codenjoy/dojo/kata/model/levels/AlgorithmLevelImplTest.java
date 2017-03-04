package com.codenjoy.dojo.kata.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 Codenjoy
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

import static org.junit.Assert.assertEquals;

/**
 * Created by indigo on 2017-03-04.
 */
public class AlgorithmLevelImplTest {

    @Test
    public void testAlgorithm() {
        // given when
        AlgorithmLevelImpl level =
                new AlgorithmLevelImpl("question1", "question2", "question3") {
                    @Override
                    public String get(String input) {
                        return "answer" + input.substring("question".length());
                    }

                    @Override
                    public String getDescription() {
                        return "BlaBlaDescription";
                    }
                };

        // then
        assertEquals(3, level.size());
        assertEquals("[answer1, answer2, answer3]", level.getAnswers().toString());
    }

}