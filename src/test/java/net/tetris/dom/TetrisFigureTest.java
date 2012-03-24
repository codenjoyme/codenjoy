package net.tetris.dom;

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
        assertThat(rowCodes("#")).isEqualTo(new int[]{0b1});
        assertThat(rowCodes("##")).isEqualTo(new int[]{0b11});
        assertThat(rowCodes(" # ")).isEqualTo(new int[]{0b010});
    }

    @Test
    public void shouldProvideValidBinaryCodesWhenColDefined() {
        assertThat(rowCodes("#", "#")).isEqualTo(new int[]{0b1, 0b1});
        assertThat(rowCodes("#", "#")).isEqualTo(new int[]{0b1, 0b1});
        assertThat(rowCodes(" ", "#", " ")).isEqualTo(new int[]{0, 1, 0});
    }

    private int[] rowCodes(String ... rows) {
        return new TetrisFigure(0,0, rows).getRowCodes();
    }

    private void assertFigure(TetrisFigure figure, int left, int right, int top, int bottom) {
        assertEquals(left, figure.getLeft());
        assertEquals(right, figure.getRight());
        assertEquals(top, figure.getTop());
        assertEquals(bottom, figure.getBottom());
    }
}
