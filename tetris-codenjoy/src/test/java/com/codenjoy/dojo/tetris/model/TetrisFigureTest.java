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

import static junit.framework.Assert.assertEquals;
import static org.fest.assertions.Assertions.assertThat;

public class TetrisFigureTest {

    @Test
    public void shouldGetDimensionsWhenDefaultFigure() {
        assertFigure(new TetrisFigure(), 0, 0, 0, 0);
    }

    @Test
    public void shouldGetXDimensionsWhenOneRow() {
        assertFigure(new TetrisFigure(1, 0, "##"), 1, 0, 0, 0);
        assertFigure(new TetrisFigure(0, 0, "##"), 0, 1, 0, 0);
        assertFigure(new TetrisFigure(1, 0, "####"), 1, 2, 0, 0);
    }

    @Test
    public void shouldGetYDimensionsWhenOneColumn() {
        assertFigure(new TetrisFigure(0, 1, "#", "#"), 0, 0, 1, 0);
        assertFigure(new TetrisFigure(0, 0, "#", "#"), 0, 0, 0, 1);
        assertFigure(new TetrisFigure(0, 1, "#", "#", "#", "#"), 0, 0, 1, 2);
    }

    @Test
    public void shouldProvideValidBinaryCodesWhenRowDefined() {
        assertThat(rowCodes("#")).isEqualTo(new int[]{0b001});
        assertThat(rowCodes("##")).isEqualTo(new int[]{0b001001});
        assertThat(rowCodes(" # ")).isEqualTo(new int[]{0b000001000});
    }

    @Test
    public void shouldProvideValidBinaryCodesWhenColDefined() {
        assertThat(rowCodes("#", "#")).isEqualTo(new int[]{0b001, 0b001});
        assertThat(rowCodes("#", "#")).isEqualTo(new int[]{0b001, 0b001});
        assertThat(rowCodes(" ", "#", " ")).isEqualTo(new int[]{0b000, 0b001, 0b000});
    }

    @Test
    public void shouldRotateFigureI() {
        Figure figure = Figure.Type.I.createNewFigure();
        assertDimensions(figure, 1, 2, 1, 0, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001, 0b001, 0b001, 0b001});

        figure.rotate(1);

        assertDimensions(figure, 4, 0, 0, 2, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001001001001});

        figure.rotate(1);

        assertDimensions(figure, 1, 1, 2, 0, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001, 0b001, 0b001, 0b001});

        figure.rotate(1);

        assertDimensions(figure, 4, 0, 0, 1, 2);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001001001001});
    }

    @Test
    public void shouldRotateFigureJ() {
        Figure figure = Figure.Type.J.createNewFigure();
        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b0000010, 0b000010, 0b010010});

        figure.rotate(1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b010000000, 0b010010010});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b010010, 0b010000, 0b010000});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b010010010, 0b000000010});
    }

    @Test
    public void shouldRotateFigureL() {
        Figure figure = Figure.Type.L.createNewFigure();
        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b011000, 0b011000, 0b011011});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b011011011, 0b011000000});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b011011, 0b000011, 0b000011});

        figure.rotate(1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b011, 0b011011011});
    }

    @Test
    public void shouldRotateFigureO() {
        Figure figure = Figure.Type.O.createNewFigure();
        assertDimensions(figure, 2, 1, 0, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b100100, 0b100100});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 0, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b100100, 0b100100});

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b100100, 0b100100});

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b100100, 0b100100});
    }

    @Test
    public void shouldRotateFigureS() {
        Figure figure = Figure.Type.S.createNewFigure();
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000101101, 0b101101000});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b101000, 0b101101, 0b000101});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000101101, 0b101101000});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b101000, 0b101101, 0b000101});
    }

    @Test
    public void shouldRotateFigureT() {
        Figure figure = Figure.Type.T.createNewFigure();
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000110000, 0b110110110});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b110000, 0b110110, 0b110000});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b110110110, 0b000110000});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000110, 0b110110, 0b000110});
    }

    @Test
    public void shouldRotateFigureT_NoNegativeRotatesSupported() {
        Figure figure = Figure.Type.T.createNewFigure();
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000110000, 0b110110110});

        figure.rotate(-1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000110000, 0b110110110});
    }

    @Test
    public void shouldRotateFigureZ() {
        Figure figure = Figure.Type.Z.createNewFigure();
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b111111000, 0b000111111});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000111, 0b111111, 0b111000});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b111111000, 0b000111111});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b000111, 0b111111, 0b111000});
    }

    @Test
    public void shouldRotateWhenRotateOverShiftedYCenter() {
        TetrisFigure figure = new TetrisFigure(0, 1, "#", "#");

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 0, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001001});
    }

    @Test
    public void shouldRotateWhenRotateOverShiftedXCenter() {
        TetrisFigure figure = new TetrisFigure(1, 0, "##");

        figure.rotate(1);

        assertDimensions(figure, 1, 0, 1, 0, 0);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b1, 0b1});
    }

    @Test
    public void shouldRotateWhenSeveralTimes() {
        TetrisFigure figure = new TetrisFigure(1, 1, " #", "# ", " #");

        figure.rotate(2);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b001000, 0b000001, 0b001000});
    }

    @Test
    public void shouldParseRowCodesWhenColorCyan() {
        TetrisFigure figure = new TetrisFigure(0, 0, Figure.Type.J, "#");
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{0b010});
    }

    @Test
    public void shouldIgnoreColorCodeWhenAsked() {
        assertThat(rowCodes(true, "#")).isEqualTo(new int[]{0b111});
        assertThat(rowCodes(true, "##")).isEqualTo(new int[]{0b111111});
        assertThat(rowCodes(true, " # ")).isEqualTo(new int[]{0b000111000});
    }

    @Test(timeout = 1000)
    public void shouldRotateWhenOverflowTimes() {
        TetrisFigure figure = new TetrisFigure(1, 1, " #", "# ", " #");

        figure.rotate(Integer.MAX_VALUE);
    }

    private void assertDimensions(Figure figure, int expectedWidth, int expectedBottom, int expectedTop, int expectedLeft, int expectedRight) {
        assertEquals(expectedWidth, figure.getWidth());
        assertEquals(expectedBottom, figure.getBottom());
        assertEquals(expectedTop, figure.getTop());
        assertEquals(expectedLeft, figure.getLeft());
        assertEquals(expectedRight, figure.getRight());
    }

    private int[] rowCodes(String... rows) {
        return rowCodes(false, rows);
    }

    private int[] rowCodes(boolean ignoreColors, String... rows) {
        return new TetrisFigure(0, 0, rows).getRowCodes(ignoreColors);
    }

    private void assertFigure(TetrisFigure figure, int left, int right, int top, int bottom) {
        assertEquals(left, figure.getLeft());
        assertEquals(right, figure.getRight());
        assertEquals(top, figure.getTop());
        assertEquals(bottom, figure.getBottom());
    }
}
