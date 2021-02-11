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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

    private Board board;
    private Synonyms synonyms;

    public static Board board(String boardString) {
        return (Board) new Board().forString(boardString);
    }

    @Before
    public void before() {
        board = board(
                "☼☼☼☼☼☼☼☼☼" +
                "☼1 ♣   ♠☼" +
                "☼#2  &  ☼" +
                "☼# 3 ♣ ♠☼" +
                "☼☺  4   ☼" +
                "☼   ♥ H☻☼" +
                "☼x H ҉҉҉☼" +
                "☼& &    ☼" +
                "☼☼☼☼☼☼☼☼☼");
        synonyms = new Synonyms();
    }

    @Test
    public void shouldIsNearMe_case1() {
        // same
        asrtMsk("☼# " +
                "☼☺ " +
                "☼  ", true);

        asrtMsk("☼#?" +
                "☼☺?" +
                "☼  ", true);

        asrtMsk("☼#?" +
                "?☺?" +
                "☼  ", true);

        asrtMsk("☼#?" +
                "?☺?" +
                "☼ ?", true);

        asrtMsk("???" +
                "?☺?" +
                "???", true);

        // not same
        asrtMsk("☼# " +
                "☼☺☻" +
                "☼  ", false);

        asrtMsk("☼#?" +
                "☼☺☻" +
                "☼ ?", false);

        asrtMsk("???" +
                "?☺☻" +
                "???", false);
    }

    private void asrtMsk(String pattern, boolean expected) {
        assertEquals(expected, board.isNearMe(new Pattern(pattern, synonyms)));
    }

    @Test
    public void shouldIsNearMe_case2() {
        // same
        asrtMsk("☼#" +
                "☼☺", true);

        asrtMsk("☼?" +
                "☼☺", true);

        asrtMsk("??" +
                "☼☺", true);

        asrtMsk("??" +
                "?☺", true);

        // not same
        asrtMsk("☼☻" +
                "☼☺", false);

        asrtMsk("?☻" +
                "☼☺", false);

        asrtMsk("?☻" +
                "?☺", false);
    }

    @Test
    public void shouldIsNearMe_case3() {
        // same
        asrtMsk("#2 " +
                "# 3" +
                "☺  ", true);

        asrtMsk("#2 " +
                "? 3" +
                "☺? ", true);

        asrtMsk("#? " +
                "???" +
                "☺? ", true);

        asrtMsk("???" +
                "???" +
                "☺??", true);

        // not same
        asrtMsk("#2☻" +
                "# 3" +
                "☺  ", false);

        asrtMsk("#?☻" +
                "# ?" +
                "☺  ", false);

        asrtMsk("??☻" +
                "? ?" +
                "☺  ", false);

        asrtMsk("??☻" +
                "???" +
                "☺??", false);
    }

    @Test
    public void shouldIsNearMe_case4() {
        // same
        asrtMsk("☼# 3" +
                "☼☺  " +
                "☼   " +
                "☼x H", true);

        asrtMsk("?? 3" +
                "?☺? " +
                "☼?? " +
                "☼x H", true);

        asrtMsk("????" +
                "?☺??" +
                "☼???" +
                "????", true);

        // not same
        asrtMsk("☼# 3" +
                "☼☺  " +
                "☼   " +
                "☼x ☻", false);

        asrtMsk("☼# 3" +
                "☼☺ ☻" +
                "☼   " +
                "☼x H", false);

        asrtMsk("☼# 3" +
                "☼☺  " +
                "☼☻  " +
                "☼x H", false);

        asrtMsk("????" +
                "?☺??" +
                "?☻??" +
                "????", false);
    }

    @Test
    public void shouldIsNearMe_case5() {
        // same
        asrtMsk("?☼#2 " +
                "?☼# 3" +
                "?☼☺  " +
                "?☼   " +
                "?☼x H", true);

        asrtMsk("?????" +
                "????3" +
                "??☺  " +
                "?☼   " +
                "?☼x H", true);

        asrtMsk("?????" +
                "?????" +
                "??☺??" +
                "?????" +
                "????H", true);

        // not same
        asrtMsk("☻☼#2 " +
                "?☼# 3" +
                "?☼☺  " +
                "?☼   " +
                "?☼x H", false);

        asrtMsk("?☼#2 " +
                "?☼# 3" +
                "☻☼☺  " +
                "?☼   " +
                "?☼x H", false);

        asrtMsk("?☼#2 " +
                "?☼# 3" +
                "?☼☺  " +
                "?☼   " +
                "☻☼x H", false);

        asrtMsk("?????" +
                "?????" +
                "??☺ ☻" +
                "?☼   " +
                "?☼x H", false);

        asrtMsk("?????" +
                "?????" +
                "??☺?☻" +
                "?????" +
                "?????", false);
    }

    @Test
    public void shouldIsNearMe_case6() {
        // same
        asrtMsk("?☼☺  4 " +
                "?☼   ♥ " +
                "?☼x H ҉" +
                "?☼& &  " +
                "?☼☼☼☼☼☼" +
                "???????" +
                "???????", true);

        asrtMsk("?☼☺ ???" +
                "?☼  ???" +
                "?☼x ???" +
                "?☼& ???" +
                "?☼☼☼???" +
                "???????" +
                "???????", true);

        asrtMsk("??☺????" +
                "???????" +
                "???????" +
                "???????" +
                "???????" +
                "???????" +
                "???????", true);

        // not same
        asrtMsk("?☼☺  4 " +
                "?☼   ♥ " +
                "?☼x H ҉" +
                "?☼& &  " +
                "?☼☼☼☼☼☼" +
                "???????" +
                "☻??????", false);

        asrtMsk("?☼☺  4 " +
                "?☼ ☻ ♥ " +
                "?☼x H ҉" +
                "?☼& &  " +
                "?☼☼☼☼☼☼" +
                "???????" +
                "???????", false);

        asrtMsk("??☺????" +
                "???????" +
                "???????" +
                "?????☻?" +
                "???????" +
                "???????" +
                "???????", false);

    }

    @Test
    public void shouldIsNearMe_case7() {
        board = board(
                "☼☼☼" +
                "☼☺☼" +
                "☼☼☼");
        // same
        asrtMsk("?????" +
                "?☼☼☼?" +
                "?☼☺☼?" +
                "?☼☼☼?" +
                "?????", true);

        asrtMsk("????" +
                "☼☼☼?" +
                "☼☺☼?" +
                "☼☼☼?", true);

        // not same
        asrtMsk("?????" +
                "?☼☼☼?" +
                "?☼☺☼?" +
                "?☼☼☻?" +
                "?????", 
                false);
    }

}
