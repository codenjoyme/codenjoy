package net.tetris.dom;

import org.junit.Ignore;
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

    @Test
    public void shouldRotateWhenRotateOverZeroCenter() {
        TetrisFigure figure = new TetrisFigure(0, 0, "#", "#");

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 0, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11});
    }

    @Test
    @Ignore
    public void shouldRotateWhenRotateOverShiftedYCenter() {
        TetrisFigure figure = new TetrisFigure(0, 1, "#", "#");

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 0, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11});
    }

    private void assertDimensions(TetrisFigure figure, int expectedWidth, int expectedBottom, int expectedTop, int expectedLeft, int expectedRight) {
        assertEquals(expectedWidth, figure.getWidth());
        assertEquals(expectedBottom, figure.getBottom());
        assertEquals(expectedTop, figure.getTop());
        assertEquals(expectedLeft, figure.getLeft());
        assertEquals(expectedRight, figure.getRight());
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
