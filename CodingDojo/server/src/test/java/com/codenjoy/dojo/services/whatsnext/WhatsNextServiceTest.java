package com.codenjoy.dojo.services.whatsnext;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
 * %%WhatsNextServiceTest
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

import com.codenjoy.dojo.sample.services.GameRunner;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.utils.smart.SmartAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

public class WhatsNextServiceTest {

    private GameType gameType;
    private WhatsNextService whatsNext;

    @Before
    public void setup() {
        gameType = new GameRunner();
        whatsNext = new WhatsNextService();
    }

    @After
    public void after() {
        SmartAssert.checkResult();
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
                "|     setup      \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼ ☺ ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
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
                "|     setup      \n" +
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
                "| (1) Events:[LOSE => -20]\n" +
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
                "|     setup      \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼☺ ☻☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼☻ ☺☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
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
        // about 4 sec
        for (int i = 0; i < 10000; i++) {
            whatsNx("☼☼☼☼☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼   ☼\n" +
                    "☼☺ ☺☼\n" +
                    "☼☼☼☼☼\n",

                    "(4)->[ACT, LEFT]",

                    "+----------------\n" +
                    "|     setup      \n" +
                    "+----------------\n" +
                    "|                \n" +
                    "| (1) Board:     \n" +
                    "| (1) ☼☼☼☼☼      \n" +
                    "| (1) ☼☺ ☻☼      \n" +
                    "| (1) ☼   ☼      \n" +
                    "| (1) ☼☻ ☻☼      \n" +
                    "| (1) ☼☼☼☼☼      \n" +
                    "| (1) Events:[]  \n" +
                    "|                \n" +
                    "| (2) Board:     \n" +
                    "| (2) ☼☼☼☼☼      \n" +
                    "| (2) ☼☻ ☺☼      \n" +
                    "| (2) ☼   ☼      \n" +
                    "| (2) ☼☻ ☻☼      \n" +
                    "| (2) ☼☼☼☼☼      \n" +
                    "| (2) Events:[]  \n" +
                    "|                \n" +
                    "| (3) Board:     \n" +
                    "| (3) ☼☼☼☼☼      \n" +
                    "| (3) ☼☻ ☻☼      \n" +
                    "| (3) ☼   ☼      \n" +
                    "| (3) ☼☺ ☻☼      \n" +
                    "| (3) ☼☼☼☼☼      \n" +
                    "| (3) Events:[]  \n" +
                    "|                \n" +
                    "| (4) Board:     \n" +
                    "| (4) ☼☼☼☼☼      \n" +
                    "| (4) ☼☻ ☻☼      \n" +
                    "| (4) ☼   ☼      \n" +
                    "| (4) ☼☻ ☺☼      \n" +
                    "| (4) ☼☼☼☼☼      \n" +
                    "| (4) Events:[]  \n" +
                    "|                \n" +
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
                "|     setup      \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼☺  ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼  ☻☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼☻  ☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼  ☺☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
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
                "(1)->[DOWN]&(2)->[UP]",

                "+----------------\n" +
                "|     setup      \n" +
                "+----------------\n" +
                "|                \n" +
                "| (1) Board:     \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) ☼☺  ☼      \n" +
                "| (1) ☼   ☼      \n" +
                "| (1) ☼  ☻☼      \n" +
                "| (1) ☼☼☼☼☼      \n" +
                "| (1) Events:[]  \n" +
                "|                \n" +
                "| (2) Board:     \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) ☼☻  ☼      \n" +
                "| (2) ☼   ☼      \n" +
                "| (2) ☼  ☺☼      \n" +
                "| (2) ☼☼☼☼☼      \n" +
                "| (2) Events:[]  \n" +
                "|                \n" +
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
                "|           setup            \n" +
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
                "| (1) ☼       ☺       ☼      \n" +
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

                expectedResult());
    }

    @Test
    public void shouldIgnoreLastActionSeparator_caseOne() {
        String willIgnore = "&";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                "(1)->[ACT]" + willIgnore,

                expectedResult());
    }

    @Test
    public void shouldIgnoreLastActionSeparator_caseSeveral() {
        String willIgnore = "&&&";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                "(1)->[ACT]" + willIgnore,

                expectedResult());
    }

    @Test
    public void shouldIgnoreFirstActionSeparator_caseOne() {
        String willIgnore = "&";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                willIgnore + "(1)->[ACT]",

                expectedResult());
    }

    @Test
    public void shouldIgnoreFirstActionSeparator_caseSeveral() {
        String willIgnore = "&&&";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                willIgnore + "(1)->[ACT]",

                expectedResult());
    }

    public String expectedResult() {
        return  "+--------------\n" +
                "|    setup     \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
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
                "+--------------\n";
    }

    @Test
    public void shouldNotIgnoreLastTickSeparator_caseOne() {
        String willNotIgnore = ";";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                "(1)->[ACT]" + willNotIgnore,

                "+--------------\n" +
                "|    setup     \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
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
                "+--------------\n" +
                "|    tick 2    \n" +
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

    @Test
    public void shouldNotIgnoreLastTickSeparator_caseSeveral() {
        String willNotIgnore = ";;;";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                "(1)->[ACT]" + willNotIgnore,

                "+--------------\n" +
                "|    setup     \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
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
                "+--------------\n" +
                "|    tick 2    \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
                "+--------------\n" +
                "|    tick 3    \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
                "+--------------\n" +
                "|    tick 4    \n" +
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

    @Test
    public void shouldNotIgnoreFirstTickSeparator_caseOne() {
        String willNotIgnore = ";";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                willNotIgnore + "(1)->[ACT]",

                "+--------------\n" +
                "|    setup     \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
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
                "+--------------\n" +
                "|    tick 2    \n" +
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

    @Test
    public void shouldNotIgnoreFirstTickSeparator_caseSeveral() {
        String willNotIgnore = ";;;";

        whatsNx("☼☼☼\n" +
                "☼☺☼\n" +
                "☼☼☼\n",

                willNotIgnore + "(1)->[ACT]",

                "+--------------\n" +
                "|    setup     \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
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
                "+--------------\n" +
                "|    tick 2    \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
                "+--------------\n" +
                "|    tick 3    \n" +
                "+--------------\n" +
                "|              \n" +
                "| (1) Board:   \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) ☼☺☼      \n" +
                "| (1) ☼☼☼      \n" +
                "| (1) Events:[]\n" +
                "|              \n" +
                "+--------------\n" +
                "|    tick 4    \n" +
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
