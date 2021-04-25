package com.codenjoy.dojo.services.whatsnext;

import com.codenjoy.dojo.sample.services.GameRunner;
import com.codenjoy.dojo.services.GameType;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WatsNextServiceTest {

    private GameType gameType;
    private WhatsNextService whatsNext;

    @Before
    public void setUp() {
        gameType = new GameRunner();
        whatsNext = new WhatsNextService();
    }

    @Test
    public void shouldHeroMove_whenSendCommand() {
        whatsNx("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(1)->[ACT, LEFT]",

                "+----------------\n" +
                "|     tick 1     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☺x ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "+----------------\n");
    }

    @Test
    public void shouldCatchEvents_whenGameFiredIt() {
        whatsNx("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺x ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(1)->[RIGHT]",

                "+----------------\n" +
                "|     tick 1     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼ X ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[LOSE]\n" +
                "|                \n" +
                "+----------------\n");
    }

    @Test
    public void shouldMultiplayer_onlyOnePlayerGo() {
        whatsNx("☼☼☼☼☼\n" +
                "☼☺ ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "(2)->[ACT, LEFT]",

                "+----------------\n" +
                "|     tick 1     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼☺☻x☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼☻☺x☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
                "+----------------\n");
    }

    @Test(timeout = 10000)
    public void shouldMethodIsFast() {
        // about 2.3 sec
        for (int i = 0; i < 10000; i++) {
            whatsNx("☼☼☼☼☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼   ☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼☼☼☼☼\n",

                    "(4)->[ACT, LEFT]",

                    "+----------------\n" +
                    "|     tick 1     \n" +
                    "+----------------\n" +
                    "|                \n" +
                    "| (1) Board:     \n" +
                    "| (1) ☼☼☼☼☼      \n" +
                    "| (1) ☼☺ ☻☼      \n" +
                    "| (1) ☼   ☼      \n" +
                    "| (1) ☼☻☻x☼      \n" +
                    "| (1) ☼☼☼☼☼      \n" +
                    "| (1) Events:[]  \n" +
                    "|                \n" +
                    "| (2) Board:     \n" +
                    "| (2) ☼☼☼☼☼      \n" +
                    "| (2) ☼☻ ☺☼      \n" +
                    "| (2) ☼   ☼      \n" +
                    "| (2) ☼☻☻x☼      \n" +
                    "| (2) ☼☼☼☼☼      \n" +
                    "| (2) Events:[]  \n" +
                    "|                \n" +
                    "| (3) Board:     \n" +
                    "| (3) ☼☼☼☼☼      \n" +
                    "| (3) ☼☻ ☻☼      \n" +
                    "| (3) ☼   ☼      \n" +
                    "| (3) ☼☺☻x☼      \n" +
                    "| (3) ☼☼☼☼☼      \n" +
                    "| (3) Events:[]  \n" +
                    "|                \n" +
                    "| (4) Board:     \n" +
                    "| (4) ☼☼☼☼☼      \n" +
                    "| (4) ☼☻ ☻☼      \n" +
                    "| (4) ☼   ☼      \n" +
                    "| (4) ☼☻☺x☼      \n" +
                    "| (4) ☼☼☼☼☼      \n" +
                    "| (4) Events:[]  \n" +
                    "|                \n" +
                    "+----------------\n");
        }
    }

    @Test
    public void shouldMultiplayer_everyHeroCanGo() {
        whatsNx("☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n",

                "(2)->[ACT, UP]&(1)->[ACT, DOWN]",

                "+----------------\n" +
                "|     tick 1     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼x  ☼      \n" +
                "| (1) ☼☺ ☻☼      \n" +
                "| (1) ☼  x☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼x  ☼      \n" +
                "| (2) ☼☻ ☺☼      \n" +
                "| (2) ☼  x☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
                "+----------------\n");
    }

    @Test
    public void shouldSeveralTicksOnMultiplayer() {
        whatsNx("☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n",

                "(2)->[ACT, UP]&(1)->[ACT, DOWN];" +
                "(1)->[DOWN]&(2)->[UP];",

                "+----------------\n" +
                "|     tick 1     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼x  ☼      \n" +
                "| (1) ☼☺ ☻☼      \n" +
                "| (1) ☼  x☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼x  ☼      \n" +
                "| (2) ☼☻ ☺☼      \n" +
                "| (2) ☼  x☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
                "+----------------\n" +
                "|     tick 2     \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼x ☻☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☺ x☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼x ☺☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼☻ x☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
                "+----------------\n");
    }

    @Test
    public void shouldPrintBigBoard() {
        whatsNx("☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼       ☺       ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼               ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n",

                "(1)->[ACT, LEFT]",

                "+----------------------------\n" +
                "|           tick 1           \n" +
                "+----------------------------\n" +
                "|                            \n" +
                "| (1) Board:                 \n" +
                "| (1) ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼      ☺x       ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼               ☼      \n" +
                "| (1) ☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼      \n" +
                "| (1) Events:[]              \n" +
                "|                            \n" +
                "+----------------------------\n");
    }

    @Test
    public void shouldPrintSmallBoard() {
        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                "(1)->[ACT]",

                "+--------------\n" +
                "|    tick 1    \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
                "+--------------\n");
    }

    private void whatsNx(String actions, String command, String expected) {
        String board = whatsNext.calculate(gameType, actions, command);
        assertEquals(expected, board);
    }

}
