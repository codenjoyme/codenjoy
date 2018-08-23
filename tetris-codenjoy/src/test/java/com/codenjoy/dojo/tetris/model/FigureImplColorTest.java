package com.codenjoy.dojo.tetris.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.fest.assertions.Assertions.assertThat;

/**
 * User: serhiy.zelenin
 * Date: 9/14/12
 * Time: 6:31 PM
 */
@RunWith(Parameterized.class)
public class FigureImplColorTest {
    private int expectedColorCode;
    private Type figureType;

    public FigureImplColorTest(Type figureType, int expectedColorCode) {
        this.figureType = figureType;
        this.expectedColorCode = expectedColorCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { Type.I, 0b001},
                { Type.J, 0b010},
                { Type.L, 0b011},
                { Type.O, 0b100},
                { Type.S, 0b101},
                { Type.T, 0b110},
                { Type.Z, 0b111},
        };
        return Arrays.asList(data);
    }

    @Test
    public void shouldReturnValidColorCode() {
        FigureImpl figure = new FigureImpl(0, 0, figureType, "#");
        assertThat(figure.rowCodes(false)).isEqualTo(new int[]{expectedColorCode});

    }
}
