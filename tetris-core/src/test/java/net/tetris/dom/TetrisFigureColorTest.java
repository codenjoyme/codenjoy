package net.tetris.dom;

import com.codenjoy.dojo.tetris.model.Figure;
import com.codenjoy.dojo.tetris.model.TetrisFigure;
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
public class TetrisFigureColorTest {
    private int expectedColorCode;
    private Figure.Type figureType;

    public TetrisFigureColorTest(Figure.Type figureType, int expectedColorCode) {
        this.figureType = figureType;
        this.expectedColorCode = expectedColorCode;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                { Figure.Type.I, 0b001},
                { Figure.Type.J, 0b010},
                { Figure.Type.L, 0b011},
                { Figure.Type.O, 0b100},
                { Figure.Type.S, 0b101},
                { Figure.Type.T, 0b110},
                { Figure.Type.Z, 0b111},
        };
        return Arrays.asList(data);
    }

    @Test
    public void shouldReturnValidColorCode() {
        TetrisFigure figure = new TetrisFigure(0, 0, figureType, "#");
        assertThat(figure.getRowCodes(false)).isEqualTo(new int[]{expectedColorCode});

    }
}
