package com.codenjoy.dojo.bomberman.client.simple;

import com.codenjoy.dojo.bomberman.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.File;

import static com.codenjoy.dojo.bomberman.client.simple.RuleReader.MAIN_RULE_FILE_NAME;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorTest extends AbstractRuleReaderTest {

    private Processor processor;
    private Board board;

    @Before
    public void setup() {
        // given 
        super.setup();

        board = mock(Board.class);
        processor = new Processor("/", mock(Dice.class)) {
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
    }

}