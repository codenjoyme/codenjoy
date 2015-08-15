package com.codenjoy.dojo.spacerace.client.ai;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.spacerace.client.Board;
import com.codenjoy.dojo.spacerace.client.YourSolver;

/**
 * User: sanja
 * Date: 05.10.13
 * Time: 11:56
 */
public class AlAnTestSolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new AlAnTestSolver(dice);
    }

    private Board board(String board) {

        return (Board) new Board().forString(board);
    }

    @Test
    public void shouldUP() {
        assertA("☼   ☼" +
                "☼ 7 ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldRight() {
        assertA("☼   ☼" +
                "☼☺ 7☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.RIGHT);
    }

    @Test
    public void shouldLeft() {
        assertA("☼   ☼" +
                "☼7 ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
    }

    @Test
    public void shouldDown() {
        assertA("☼   ☼" +
                "☼  ☺☼" +
                "☼   ☼" +
                "☼  7☼" +
                "☼   ☼"
                , Direction.DOWN);
    }

    @Test
    public void shouldStopWhenStoneIsLeftUp() {
        assertA("☼ 0 ☼" +
                "☼7 ☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.STOP);

        assertA("☼   ☼" +
                "☼70☺☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
        
        assertA("☼   ☼" +
                "☼7☺ ☼" +
                "☼ 0 ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.LEFT);
    }

    @Test
    public void shouldStopWhenStoneIsRightUp() {
        assertA("☼ 0 ☼" +
                "☼☺ 7☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.STOP);

        assertA("☼   ☼" +
                "☼☺07☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼   ☼"
                , Direction.RIGHT);

    }

    @Test
    public void shouldLeftWhenStoneIsUp() {
        assertA("☼ 7 ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.LEFT);

        assertA("☼   ☼" +
                "☼ 7 ☼" +
                "☼   ☼" +
                "☼☺0 ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldLeftWhenStoneIsUp2() {
        assertA("☼ 7 ☼" +
                "☼ 0 ☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼"
                , Direction.LEFT);

        assertA("☼ 7 ☼" +
                "☼   ☼" +
                "☼ 0 ☼" +
                "☼☺  ☼" +
                "☼   ☼"
                , Direction.UP);
    }

    @Test
    public void shouldRightWhenRightAndBombIsUp() {
        assertA("☼  ♣  ☼" +
                "☼     ☼" +
                "☼    7☼" +
                "☼  ☺  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.RIGHT);

        assertA("☼     ☼" +
                "☼  ♣  ☼" +
                "☼    7☼" +
                "☼   ☺ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.RIGHT);

        assertA("☼     ☼" +
                "☼     ☼" +
                "☼  ♣ 7☼" +
                "☼    ☺☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼"
                , Direction.UP);


    }

    @Test
    public void shouldRightWhenUpAndBombIsUp() {
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.RIGHT);

        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ☺ ☼"
                , Direction.RIGHT);

        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼    ☺☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣ ☺☼" +
                "☼     ☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼    ☺☼" +
                "☼     ☼" +
                "☼  ♣  ☼"
                , Direction.UP);

    }

    @Ignore  // todo очень временно (доделать подсчет дистанций)
    @Test
    public void shouldLefttWhenUpAndBombIsUp2() {
        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.LEFT);

        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ☺   ☼"
                , Direction.LEFT);

        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ♣  ☼" +
                "☼     ☼" +
                "☼☺    ☼"
                , Direction.UP);
        assertA("☼ 7   ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☺ ♣  ☼" +
                "☼     ☼"
                , Direction.UP);
        assertA("☼  7  ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼☺    ☼" +
                "☼     ☼" +
                "☼  ♣  ☼"
                , Direction.UP);

    }

    @Test
    public void shouldLefttWhenRightAndBombIsUp3() {
        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ♣ ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼  ☺  ☼"
                , Direction.LEFT);

        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼   ♣ ☼" +
                "☼     ☼" +
                "☼ ☺   ☼"
                , Direction.UP);

        assertA("☼    7☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼     ☼" +
                "☼ ☺ ♣ ☼" +
                "☼     ☼"
                , Direction.UP);

    }


    private void assertA(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }

    private void dice(Direction direction) {
        when(dice.next(anyInt())).thenReturn(direction.value());
    }
}
