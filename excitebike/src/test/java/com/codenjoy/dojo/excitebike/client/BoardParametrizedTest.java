package com.codenjoy.dojo.excitebike.client;

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

import com.codenjoy.dojo.excitebike.model.items.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.bike.BikeType;
import com.codenjoy.dojo.excitebike.model.items.springboard.SpringboardElementType;
import com.codenjoy.dojo.services.printer.CharElements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by Pavel Bobylev 6/18/2019
 */

@RunWith(Parameterized.class)
public class BoardParametrizedTest {

    private char elementChar;
    private String elementName;
    private Class elementClass;

    public <T extends Enum & CharElements> BoardParametrizedTest(T element) {
        elementChar = element.ch();
        elementName = element.name();
        elementClass = element.getClass();
    }

    @Parameterized.Parameters(name = "{0}")
    public static Collection data() {
        return Stream.of(
                Arrays.stream(GameElementType.values()),
                Arrays.stream(SpringboardElementType.values()),
                Arrays.stream(BikeType.values())
        ).flatMap(Function.identity()).collect(Collectors.toList());
    }

    @Test
    public void valueOf__shouldReturnCorrectElement() {
        //given
        Board board = new Board();

        //when
        CharElements result = board.valueOf(elementChar);

        //then
        assertThat(result, is(Enum.valueOf(elementClass, elementName)));
    }
}
