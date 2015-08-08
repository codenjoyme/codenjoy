package com.codenjoy.dojo.fifteen.client;

import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.fifteen.client.ai.FifteenSolver;
import com.codenjoy.dojo.services.Dice;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SolverTest {

    private Dice dice;
    private Solver ai;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        ai = new FifteenSolver(dice);
    }

    private Board board(String board) {
        return (Board) new Board().forString(board);
    }

    @Test
    public void downTest() {
        when(dice.next(anyInt())).thenReturn(0);
        asertAI("******" +
                "*abcd*" +
                "*efgh*" +
                "*ij+k*" +
                "*mnol*" +
                "******", Direction.DOWN);
    }

    @Test
    public void upTest() {
        when(dice.next(anyInt())).thenReturn(1);
        asertAI("******" +
                "*abcd*" +
                "*efgh*" +
                "*ij+k*" +
                "*mnol*" +
                "******", Direction.UP);
    }

    @Test
    public void rightTest() {
        when(dice.next(anyInt())).thenReturn(2);
        asertAI("******" +
                "*abcd*" +
                "*efgh*" +
                "*ij+k*" +
                "*mnol*" +
                "******", Direction.RIGHT);
    }

    @Test
    public void leftTest() {
        when(dice.next(anyInt())).thenReturn(3);
        asertAI("******" +
                "*abcd*" +
                "*efgh*" +
                "*ij+k*" +
                "*mnol*" +
                "******", Direction.LEFT);
    }

    private void asertAI(String board, Direction expected) {
        String actual = ai.get(board(board));
        assertEquals(expected.toString(), actual);
    }
}
