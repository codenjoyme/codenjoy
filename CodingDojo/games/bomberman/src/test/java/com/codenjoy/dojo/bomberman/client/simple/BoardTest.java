package com.codenjoy.dojo.bomberman.client.simple;

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
