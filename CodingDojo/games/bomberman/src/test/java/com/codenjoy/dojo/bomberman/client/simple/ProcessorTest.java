package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorTest extends AbstractRuleReaderTest {

    private Processor processor;
    private Board board;
    private List<ErrorMessage> errors;

    @Before
    public void setup() {
        // given 
        super.setup();

        errors = new LinkedList<>();
        board = mock(Board.class);
        
        processor = new Processor("/", mock(Dice.class), this.errors::add) {
            @Override
            protected RuleReader getReader() {
                return reader;
            }
        };
    }
    
    @Test
    public void shouldStop_whenBombermanDie() {
        // given
        when(board.isMyBombermanDead()).thenReturn(true);
        
        // when 
        Direction direction = processor.next(board);
        
        // then
        assertEquals(Direction.STOP, direction);
        assertEquals("[]", errors.toString());
    }

    @Test
    public void shouldSeveralDirections_whenOneRule() {
        // given
        lines2 = load(
                "   ",
                "   ",
                "   ",
                "RIGHT,LEFT,DOWN,UP,UP,LEFT");

        when(board.isNearMe(anyString())).thenReturn(true);

        // when then
        assertEquals(Direction.RIGHT, processor.next(board));
        assertEquals(Direction.LEFT, processor.next(board));
        assertEquals(Direction.DOWN, processor.next(board));
        assertEquals(Direction.UP, processor.next(board));
        assertEquals(Direction.UP, processor.next(board));
        assertEquals(Direction.LEFT, processor.next(board));
        
        assertEquals("[]", errors.toString());
    }

    @Test
    public void shouldSeveralDirections_whenOneRule_caseSeveralTimes() {
        // given when then
        shouldSeveralDirections_whenOneRule();
        shouldSeveralDirections_whenOneRule();
        shouldSeveralDirections_whenOneRule();
        
        assertEquals("[]", errors.toString());
    }

}