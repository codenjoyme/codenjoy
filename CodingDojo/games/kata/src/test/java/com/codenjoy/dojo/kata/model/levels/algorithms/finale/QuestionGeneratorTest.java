package com.codenjoy.dojo.kata.model.levels.algorithms.finale;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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



import com.codenjoy.dojo.kata.model.levels.algorithms.QuestionGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEqualCollection;

public class QuestionGeneratorTest {
    
    @Test
    public void shouldGenerateCorrectSequence() {
        int from = 26;
        int count = 25;
        List<String> expected = calculateExpectations(from, count);

        List<String> generated = new QuestionGenerator(from, count).generate();
        Assert.assertTrue(isEqualCollection(expected, generated));
    }

    private List<String> calculateExpectations(int from, int count) {
        List<String> expected = new ArrayList<>();
        int initial = from;
        for (int i = 0; i < count; i++) {
            expected.add(String.valueOf(initial++));
        }
        return expected;
    }
}
