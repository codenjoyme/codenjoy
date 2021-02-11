package com.codenjoy.dojo.bomberman.client.simple;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorTest extends AbstractRuleReaderTest {

    public static final String SEP = FileSystems.getDefault().getSeparator();

    private Processor processor;
    private Board board;
    private List<Message> messages;

    @Before
    public void setup() {
        // given 
        super.setup();

        messages = new LinkedList<>();
        board = mock(Board.class);
        
        processor = new Processor("", mock(Dice.class), this.messages::add) {
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
        assertEquals("[]", messages.toString());
    }

    @Test
    public void shouldSeveralDirections_whenOneRule() {
        // given
        cleanLns();
        loadLns("   ",
                "   ",
                "   ",
                "RIGHT,LEFT,DOWN,UP,UP,LEFT");

        when(board.isNearMe(any(Pattern.class))).thenReturn(true);

        // when then
        assertEquals(Direction.RIGHT, processor.next(board));
        assertEquals(Direction.LEFT, processor.next(board));
        assertEquals(Direction.DOWN, processor.next(board));
        assertEquals(Direction.UP, processor.next(board));
        assertEquals(Direction.UP, processor.next(board));
        assertEquals(Direction.LEFT, processor.next(board));
        
        assertEquals("[[MESSAGE] Mach rule: : '[\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [RIGHT, LEFT, DOWN, UP, UP, LEFT]]']", messages.toString());
        messages.clear();
    }
    
    @Test
    public void shouldSeveralDirections_whenOneRule_caseSeveralTimes() {
        // given when then
        shouldSeveralDirections_whenOneRule();
        shouldSeveralDirections_whenOneRule();
        shouldSeveralDirections_whenOneRule();
        
        assertEquals("[]", messages.toString());
    }

    @Test
    public void shouldGetErrors_whenbadRule() {
        // given
        loadLns("   ",
                "   ",
                "   ",
                "BAD RULE");

        when(board.isNearMe(any(Pattern.class))).thenReturn(true);

        // when 
        Direction direction = processor.next(board);
        
        // then
        assertEquals(Direction.STOP, direction);

        assertEquals("[[ERROR] Pattern is not valid: '         BAD RULE' at " + SEP + "main.rule:5]", messages.toString());
    }

    @Test
    public void shouldLoadRules_whenRuleDirective() {
        // given
        loadLns("?☼?",
                "?☺?",
                "???",
                "DOWN",
                "",
                "????????",
                "????????",
                "????????",
                "☺",
                "????????",
                "????????",
                "????????",
                "RULE right",
                "",
                "???",
                "☼☺☼",
                "?#?",
                "LEFT");

        loadLns("?☼?",
                "?☺ ",
                "?☼?",
                "RIGHT",
                "",
                "?☼?",
                "?☺ ",
                "?#?",
                "DOWN",
                "",
                "?#?",
                "?☺ ",
                "?☼?",
                "UP",
                "",
                "?#?",
                "?☺ ",
                "?#?",
                "LEFT");

        Pattern pattern = new Pattern("?#??☺ ?☼?", null);
        when(board.isNearMe(eq(pattern))).thenReturn(true);

        // when
        Direction direction = processor.next(board);

        // then 
        assertEquals(Direction.UP, direction);
        assertEquals("[]", reader.errors().toString());
    }

}
