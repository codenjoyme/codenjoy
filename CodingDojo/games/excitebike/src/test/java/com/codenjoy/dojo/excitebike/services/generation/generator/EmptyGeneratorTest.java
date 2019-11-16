package com.codenjoy.dojo.excitebike.services.generation.generator;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by Pavel Bobylev 7/19/2019
 */
public class EmptyGeneratorTest {

    @Test
    public void generate__shouldReturnNull() {
        //given
        EmptyGenerator emptyGenerator = new EmptyGenerator();

        //when
        Map<? extends CharElements, List<Shiftable>> result = emptyGenerator.generate();

        //then
        assertThat(result, nullValue());
    }

    @Test
    public void generationLockSize__shouldReturnZero() {
        //given
        EmptyGenerator emptyGenerator = new EmptyGenerator();
        emptyGenerator.generate();

        //when
        int result = emptyGenerator.generationLockSize();

        //then
        assertThat(result, is(0));
    }
}
