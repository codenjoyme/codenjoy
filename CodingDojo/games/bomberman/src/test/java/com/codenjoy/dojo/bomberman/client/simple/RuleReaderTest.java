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

import com.codenjoy.dojo.services.Direction;
import org.junit.Test;

import java.nio.file.FileSystems;
import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.services.Direction.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RuleReaderTest extends AbstractRuleReaderTest {

    public static final String SEP = FileSystems.getDefault().getSeparator();

    @Test
    public void shouldNoRules_whenEmptyFile() {
        // given
        loadLns("");

        // when
        reader.load(rules, file);
        
        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals("[]", rules.toString());

    }

    @Test
    public void shouldSeveralRules_whenSingleFile() {
        // given
        loadLns("???",
                "♥☺?",
                "???",
                "RIGHT",
                "",
                "???",
                "?☺♥",
                "???",
                "LEFT",
                "",
                "?♥?",
                "?☺?",
                "???",
                "DOWN",
                "",
                "?☼?",
                "☼☺?",
                "?♥?",
                "RIGHT",
                "",
                "???",
                "?☺?",
                "?♥?",
                "UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "???\n" +
                "♥☺?\n" +
                "???\n" +
                "synonyms: {} \n" +
                " >>> [RIGHT]], [\n" +
                "???\n" +
                "?☺♥\n" +
                "???\n" +
                "synonyms: {} \n" +
                " >>> [LEFT]], [\n" +
                "?♥?\n" +
                "?☺?\n" +
                "???\n" +
                "synonyms: {} \n" +
                " >>> [DOWN]], [\n" +
                "?☼?\n" +
                "☼☺?\n" +
                "?♥?\n" +
                "synonyms: {} \n" +
                " >>> [RIGHT]], [\n" +
                "???\n" +
                "?☺?\n" +
                "?♥?\n" +
                "synonyms: {} \n" +
                " >>> [UP]]]", rules.toString());
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

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "?☼?\n" +
                "?☺?\n" +
                "???\n" +
                "synonyms: {} \n" +
                " >>> [DOWN]], [\n" +
                "???????\n" +
                "???????\n" +
                "???????\n" +
                "???☺???\n" +
                "???????\n" +
                "???????\n" +
                "???????\n" +
                "synonyms: {} > [[\n" +
                "?☼?\n" +
                "?☺ \n" +
                "?☼?\n" +
                "synonyms: {} \n" +
                " >>> [RIGHT]], [\n" +
                "?☼?\n" +
                "?☺ \n" +
                "?#?\n" +
                "synonyms: {} \n" +
                " >>> [DOWN]], [\n" +
                "?#?\n" +
                "?☺ \n" +
                "?☼?\n" +
                "synonyms: {} \n" +
                " >>> [UP]], [\n" +
                "?#?\n" +
                "?☺ \n" +
                "?#?\n" +
                "synonyms: {} \n" +
                " >>> [LEFT]]]], [\n" +
                "???\n" +
                "☼☺☼\n" +
                "?#?\n" +
                "synonyms: {} \n" +
                " >>> [LEFT]]]", rules.toString());
    }

    @Test
    public void shouldCheckBoardWithSubRules_whenRuleDirective() {
        // given
        shouldLoadRules_whenRuleDirective();
        
        Board board = mock(Board.class);
        Pattern pattern = new Pattern("?#??☺ ?☼?", null);
        when(board.isNearMe(eq(pattern))).thenReturn(true);

        // when
        List<Direction> directions = rules.process(board);

        // then 
        assertEquals("[UP]", directions.toString());
    }
    
    @Test
    public void shouldSeveralDirections_whenOneRule() {
        // given
        loadLns("   ",
                "   ",
                "   ",
                "RIGHT,LEFT,DOWN",
                "",
                "   ",
                "   ",
                "   ",
                "  LEFT  , RIGHT,   UP",
                "",
                "   ",
                "   ",
                "   ",
                "DOWN, UP  ",
                "",
                "   ",
                "   ",
                "   ",
                "RIGHT",
                "",
                "   ",
                "   ",
                "   ",
                "    UP ,  DOWN, LEFT, RIGHT,RIGHT");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [RIGHT, LEFT, DOWN]], [\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [LEFT, RIGHT, UP]], [\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [DOWN, UP]], [\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [RIGHT]], [\n" +
                "   \n" +
                "   \n" +
                "   \n" +
                "synonyms: {} \n" +
                " >>> [UP, DOWN, LEFT, RIGHT, RIGHT]]]",
                rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenTwoCommasInside() {
        // given
        loadLns("???",
                "???",
                "???",
                "RIGHT,,LEFT,DOWN");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Direction 'RIGHT,,LEFT,DOWN' is not valid for pattern: '?????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenOnlyOneComma() {
        // given
        loadLns("???",
                "???",
                "???",
                ",");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Direction ',' is not valid for pattern: '?????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenCommaAtLast() {
        // given
        loadLns("???",
                "???",
                "???",
                "UP, ");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Direction 'UP, ' is not valid for pattern: '?????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenCommaAtFirst() {
        // given
        loadLns(
                "???",
                "???",
                "???",
                " , UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Direction ' , UP' is not valid for pattern: '?????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenDirectionIsEmptyForLastPattern() {
        // given
        loadLns(
                "???",
                "???",
                "???",
                "UP",
                "???",
                "???",
                "???",
                "");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Directions is empty for pattern: '?????????' at directory" + SEP + "main.rule:9]",
                reader.errors().toString());

        assertEquals("[[\n" +
                "???\n" +
                "???\n" +
                "???\n" +
                "synonyms: {} \n" +
                " >>> [UP]]]", rules.toString());
    }

    @Test
    public void shouldError_whenPatternIsNotValid() {
        // given
        loadLns(
                "??",
                "???",
                "???",
                "UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Pattern is not valid: '????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInDirectionsList_whenCommasAtFirstAndLast() {
        // given
        loadLns(
                "???",
                "???",
                "???",
                ",DOWN,");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Direction ',DOWN,' is not valid for pattern: '?????????' at directory" + SEP + "main.rule:4]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldSetSynonyms_whenLetCommandInLine() {
        // given
        loadLns("LET B=54321҉",
                "???",
                "B☺?",
                "???",
                "RIGHT,RIGHT,RIGHT",
                "",
                "???",
                "?☺B",
                "???",
                "LEFT,LEFT,LEFT",
                "",
                "?B?",
                "?☺?",
                "???",
                "DOWN,DOWN,DOWN",
                "",
                "???",
                "?☺?",
                "?B?",
                "UP,UP,UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "???\n" +
                "B☺?\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3, 2, 1, ҉]} \n" +
                " >>> [RIGHT, RIGHT, RIGHT]], [\n" +
                "???\n" +
                "?☺B\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3, 2, 1, ҉]} \n" +
                " >>> [LEFT, LEFT, LEFT]], [\n" +
                "?B?\n" +
                "?☺?\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3, 2, 1, ҉]} \n" +
                " >>> [DOWN, DOWN, DOWN]], [\n" +
                "???\n" +
                "?☺?\n" +
                "?B?\n" +
                "synonyms: {B=[5, 4, 3, 2, 1, ҉]} \n" +
                " >>> [UP, UP, UP]]]", rules.toString());
    }

    @Test
    public void shouldCheckBoardWithSynonyms_whenLetDirective() {
        // given
        shouldSetSynonyms_whenLetCommandInLine();

        // when then 
        asrtBrd(" 3 " +
                " ☺ " +
                "   ", 
                DOWN, DOWN, DOWN);

        asrtBrd("   " +
                "1☺ " +
                "   ", 
                RIGHT, RIGHT, RIGHT);

        asrtBrd("   " +
                " ☺ " +
                " 2 ", 
                UP, UP, UP);

        asrtBrd("   " +
                " ☺5" +
                "   ",
                LEFT, LEFT, LEFT);
    }

    @Test
    public void shouldSeveralSynonyms_whenLetCommandsInLine() {
        // given
        loadLns("LET B=543",
                "",
                "???",
                "B☺?",
                "???",
                "RIGHT,RIGHT,RIGHT",
                "",
                "???",
                "?☺B",
                "???",
                "LEFT,LEFT,LEFT",
                "",
                "LET C=21҉",
                "",
                "?C?",
                "?☺?",
                "???",
                "DOWN,DOWN,DOWN",
                "",
                "???",
                "?☺?",
                "?C?",
                "UP,UP,UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "???\n" +
                "B☺?\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3], C=[2, 1, ҉]} \n" +
                " >>> [RIGHT, RIGHT, RIGHT]], [\n" +
                "???\n" +
                "?☺B\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3], C=[2, 1, ҉]} \n" +
                " >>> [LEFT, LEFT, LEFT]], [\n" +
                "?C?\n" +
                "?☺?\n" +
                "???\n" +
                "synonyms: {B=[5, 4, 3], C=[2, 1, ҉]} \n" +
                " >>> [DOWN, DOWN, DOWN]], [\n" +
                "???\n" +
                "?☺?\n" +
                "?C?\n" +
                "synonyms: {B=[5, 4, 3], C=[2, 1, ҉]} \n" +
                " >>> [UP, UP, UP]]]", rules.toString());
    }

    @Test
    public void shouldSeveralSynonyms_whenLetNotCommandsInLine() {
        // given
        loadLns("LET B!=543",
                "",
                "???",
                "B☺?",
                "???",
                "RIGHT,RIGHT,RIGHT",
                "",
                "???",
                "?☺B",
                "???",
                "LEFT,LEFT,LEFT",
                "",
                "LET C!=21҉",
                "",
                "?C?",
                "?☺?",
                "???",
                "DOWN,DOWN,DOWN",
                "",
                "???",
                "?☺?",
                "?C?",
                "UP,UP,UP");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[]", reader.errors().toString());
        assertEquals(
                "[[\n" +
                "???\n" +
                "B☺?\n" +
                "???\n" +
                "synonyms: {B=[☺, ☻, Ѡ, ♥, ♠, ♣, 2, 1, ҉, ☼, #, H, &, x, +, c, r, i,  ], C=[☺, ☻, Ѡ, ♥, ♠, ♣, 5, 4, 3, ☼, #, H, &, x, +, c, r, i,  ]} \n" +
                " >>> [RIGHT, RIGHT, RIGHT]], [\n" +
                "???\n" +
                "?☺B\n" +
                "???\n" +
                "synonyms: {B=[☺, ☻, Ѡ, ♥, ♠, ♣, 2, 1, ҉, ☼, #, H, &, x, +, c, r, i,  ], C=[☺, ☻, Ѡ, ♥, ♠, ♣, 5, 4, 3, ☼, #, H, &, x, +, c, r, i,  ]} \n" +
                " >>> [LEFT, LEFT, LEFT]], [\n" +
                "?C?\n" +
                "?☺?\n" +
                "???\n" +
                "synonyms: {B=[☺, ☻, Ѡ, ♥, ♠, ♣, 2, 1, ҉, ☼, #, H, &, x, +, c, r, i,  ], C=[☺, ☻, Ѡ, ♥, ♠, ♣, 5, 4, 3, ☼, #, H, &, x, +, c, r, i,  ]} \n" +
                " >>> [DOWN, DOWN, DOWN]], [\n" +
                "???\n" +
                "?☺?\n" +
                "?C?\n" +
                "synonyms: {B=[☺, ☻, Ѡ, ♥, ♠, ♣, 2, 1, ҉, ☼, #, H, &, x, +, c, r, i,  ], C=[☺, ☻, Ѡ, ♥, ♠, ♣, 5, 4, 3, ☼, #, H, &, x, +, c, r, i,  ]} \n" +
                " >>> [UP, UP, UP]]]", rules.toString());
    }

    @Test
    public void shouldCheckBoardWithSynonyms_whenSeveralLetDirectives() {
        // given
        shouldSeveralSynonyms_whenLetCommandsInLine();

        // when then 
        asrtBrd(" 3 " +
                " ☺ " +
                "   ",
                STOP);

        asrtBrd(" 2 " +
                " ☺ " +
                "   ",
                DOWN, DOWN, DOWN);

        // when then
        asrtBrd("   " +
                " ☺ " +
                " 5 ",
                STOP);

        asrtBrd("   " +
                " ☺ " +
                " 1 ",
                UP, UP, UP);
        
        // when then
        asrtBrd("   " +
                "1☺ " +
                "   ",
                STOP);

        asrtBrd("   " +
                "4☺ " +
                "   ",
                RIGHT, RIGHT, RIGHT);

        // when then
        asrtBrd("   " +
                " ☺2" +
                "   ",
                STOP);

        asrtBrd("   " +
                " ☺5" +
                "   ",
                LEFT, LEFT, LEFT);
    }

    private void asrtBrd(String given, Direction... expected) {
        Board board = (Board) new Board().forString(given);
        assertEquals(Arrays.asList(expected).toString(), 
                rules.process(board).toString());
    }

    @Test
    public void shouldErrorInLetDirective_case1() {
        // given
        loadLns("LET bad");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET bad' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInLetDirective_case2() {
        // given
        loadLns("LET bad=bad");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET bad=bad' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInLetDirective_case3() {
        // given
        loadLns("LET bad=bad=bad");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET bad=bad=bad' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInLetDirective_case4() {
        // given
        loadLns("LET b=");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET b=' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInLetDirective_case5() {
        // given
        loadLns("LET  b=c");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET  b=c' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

    @Test
    public void shouldErrorInLetDirective_case6() {
        // given
        loadLns("LET b=c");

        // when
        reader.load(rules, file);

        // then
        assertEquals("[[ERROR] Synonym is not valid: 'LET b=c' at directory" + SEP + "main.rule:1]",
                reader.errors().toString());

        assertEquals("[]", rules.toString());
    }

}
