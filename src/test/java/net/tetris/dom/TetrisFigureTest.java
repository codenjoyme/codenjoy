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
    public void shouldRotateFigureI() {
        TetrisFigure figure = new TetrisFigure(0, 1, Figure.Type.I, "#", "#", "#", "#");
        assertDimensions(figure, 1, 2, 1, 0, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b1, 0b1, 0b1, 0b1});

        figure.rotate(1);

        assertDimensions(figure, 4, 0, 0, 2, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b1111});

        figure.rotate(1);

        assertDimensions(figure, 1, 1, 2, 0, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b1, 0b1, 0b1, 0b1});

        figure.rotate(1);

        assertDimensions(figure, 4, 0, 0, 1, 2);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b1111});
    }

    @Test
    public void shouldRotateFigureJ() {
        TetrisFigure figure = new TetrisFigure(1, 1, Figure.Type.J, " #", " #", "##");
        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b01, 0b01, 0b11});

        figure.rotate(1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b100, 0b111});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b10, 0b10});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b111, 0b001});
    }

    @Test
    public void shouldRotateFigureL() {
        TetrisFigure figure = new TetrisFigure(0, 1, Figure.Type.L, "# ", "# ", "##");
        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b10, 0b10, 0b11});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b111, 0b100});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b01, 0b01});

        figure.rotate(1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b001, 0b111});
    }

    @Test
    public void shouldRotateFigureO() {
        TetrisFigure figure = new TetrisFigure(0, 0, Figure.Type.O, "##", "##");
        assertDimensions(figure, 2, 1, 0, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b11});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 0, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b11});

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b11});

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11, 0b11});
    }

    @Test
    public void shouldRotateFigureS() {
        TetrisFigure figure = new TetrisFigure(1, 0, Figure.Type.S, "## ", " ##");
        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b110, 0b011});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b01, 0b11, 0b10});

        figure.rotate(1);

        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b110, 0b011});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b01, 0b11, 0b10});
    }

    @Test
    public void shouldRotateFigureT() {
        TetrisFigure figure = new TetrisFigure(1, 1, Figure.Type.S, " # ", "###");
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b010, 0b111});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b10, 0b11, 0b10});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b111, 0b010});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b01, 0b11, 0b01});
    }

    @Test
    public void shouldRotateFigureZ() {
        TetrisFigure figure = new TetrisFigure(1, 1, Figure.Type.Z, " ##", "## ");
        assertDimensions(figure, 3, 0, 1, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b011, 0b110});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b10, 0b11, 0b01});

        figure.rotate(1);

        assertDimensions(figure, 3, 1, 0, 1, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b011, 0b110});

        figure.rotate(1);

        assertDimensions(figure, 2, 1, 1, 1, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b10, 0b11, 0b01});
    }

    @Test
    public void shouldRotateWhenRotateOverShiftedYCenter() {
        TetrisFigure figure = new TetrisFigure(0, 1, "#", "#");

        figure.rotate(1);

        assertDimensions(figure, 2, 0, 0, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b11});
    }

    @Test
    public void shouldRotateWhenRotateOverShiftedXCenter() {
        TetrisFigure figure = new TetrisFigure(1, 0, "##");

        figure.rotate(1);

        assertDimensions(figure, 1, 0, 1, 0, 0);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b1, 0b1});
    }

    @Test
    public void shouldRotateWhenSeveralTimes() {
        TetrisFigure figure = new TetrisFigure(1, 1, " #", "# ", " #");

        figure.rotate(2);

        assertDimensions(figure, 2, 1, 1, 0, 1);
        assertThat(figure.getRowCodes()).isEqualTo(new int[]{0b10, 0b01, 0b10});
    }

    @Test(timeout = 1000)
    public void shouldRotateWhenOverflowTimes() {
        TetrisFigure figure = new TetrisFigure(1, 1, " #", "# ", " #");

        figure.rotate(Integer.MAX_VALUE);
    }

    private void assertDimensions(TetrisFigure figure, int expectedWidth, int expectedBottom, int expectedTop, int expectedLeft, int expectedRight) {
        assertEquals(expectedWidth, figure.getWidth());
        assertEquals(expectedBottom, figure.getBottom());
        assertEquals(expectedTop, figure.getTop());
        assertEquals(expectedLeft, figure.getLeft());
        assertEquals(expectedRight, figure.getRight());
    }

    private int[] rowCodes(String... rows) {
        return new TetrisFigure(0, 0, rows).getRowCodes();
    }

    private void assertFigure(TetrisFigure figure, int left, int right, int top, int bottom) {
        assertEquals(left, figure.getLeft());
        assertEquals(right, figure.getRight());
        assertEquals(top, figure.getTop());
        assertEquals(bottom, figure.getBottom());
    }
}
