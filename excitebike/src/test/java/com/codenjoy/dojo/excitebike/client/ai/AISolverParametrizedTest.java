package com.codenjoy.dojo.excitebike.client.ai;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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

import com.codenjoy.dojo.excitebike.client.Board;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.google.common.collect.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Random;

import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.Direction.STOP;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class AISolverParametrizedTest {

    private Dice dice;
    private AISolver solver;
    private String boardString;
    private Direction expectedDirection;

    public AISolverParametrizedTest(String caseName, String board, Direction expectedDirection) {
        this.boardString = board;
        this.expectedDirection = expectedDirection;
        dice = mock(Dice.class);
        solver = new AISolver(dice);
    }

    @Parameterized.Parameters(name = "Case: {0}, Board: {1}, expected direction: {1}")
    public static List<Object[]> data() {
        return Lists.newArrayList(
                new Object[]{"1. avoid obstacle - random choice",
                        "■■■■■" +
                        "     " +
                        "  B| " +
                        "     " +
                        "■■■■■",
                        STOP},

                new Object[]{"2. avoid obstacle - choose not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  B| " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"3. avoid obstacle - choose enemy bike below, not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  B| " +
                        "   Ḃ " +
                        "■■■■■",
                        DOWN},

                new Object[]{"4. no way to survive - no action",
                        "■■■■■" +
                        "   ■ " +
                        "  B| " +
                        "   ḃ " +
                        "■■■■■",
                        null},

                new Object[]{"5. avoid obstacle - choose not fence below",
                        "■■■■■" +
                        "     " +
                        "  B| " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"6. avoid obstacle - choose enemy bike above, not fence below",
                        "■■■■■" +
                        "   Ḃ " +
                        "  B| " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"7. no way to survive - no action",
                        "■■■■■" +
                        "   ḃ " +
                        "  B| " +
                        "   ■ " +
                        "■■■■■",
                        null},

                new Object[]{"8. avoid other bike - random choice",
                        "■■■■■" +
                        "     " +
                        "  BḂ " +
                        "     " +
                        "■■■■■",
                        STOP},

                new Object[]{"9. avoid other bike - choose not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  BḂ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"10. avoid other bike - choose enemy bike below, not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  BḂ " +
                        "   Ḃ " +
                        "■■■■■",
                        DOWN},

                new Object[]{"11. no way to avoid other bike - no action",
                        "■■■■■" +
                        "   ■ " +
                        "  BḂ " +
                        "   ḃ " +
                        "■■■■■",
                        null},

                new Object[]{"12. avoid other bike - choose not fence below",
                        "■■■■■" +
                        "     " +
                        "  BḂ " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"13. avoid other bike - choose enemy bike above, not fence below",
                        "■■■■■" +
                        "   Ḃ " +
                        "  BḂ " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"14. no way to avoid other bike  - no action",
                        "■■■■■" +
                        "   ḃ " +
                        "  BḂ " +
                        "   ■ " +
                        "■■■■■",
                        null},

                new Object[]{"15. avoid fallen bike - random choice",
                        "■■■■■" +
                        "     " +
                        "  Bḃ " +
                        "     " +
                        "■■■■■",
                        STOP},

                new Object[]{"16. avoid fallen bike - choose not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  Bḃ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"17. avoid fallen bike - choose enemy bike below, not fence above",
                        "■■■■■" +
                        "   ■ " +
                        "  Bḃ " +
                        "   Ḃ " +
                        "■■■■■",
                        DOWN},

                new Object[]{"18. no way to avoid fallen bike - no action",
                        "■■■■■" +
                        "   ■ " +
                        "  Bḃ " +
                        "   ḃ " +
                        "■■■■■",
                        null},

                new Object[]{"19. avoid fallen bike - choose not fence below",
                        "■■■■■" +
                        "     " +
                        "  Bḃ " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"20. avoid fallen bike - choose enemy bike above, not fence below",
                        "■■■■■" +
                        "   Ḃ " +
                        "  Bḃ " +
                        "   ■ " +
                        "■■■■■",
                        UP},

                new Object[]{"21. no way to avoid other bike - no action",
                        "■■■■■" +
                        "   ḃ " +
                        "  Bḃ " +
                        "   ■ " +
                        "■■■■■",
                        null},

                new Object[]{"22. hit the bike below",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ  " +
                        "■■■■■",
                        DOWN},

                new Object[]{"23. hit the bike above",
                        "■■■■■" +
                        "  Ḃ  " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        UP},

                new Object[]{"24. don't hit the bike below if there is obstacle after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ| " +
                        "■■■■■",
                        null},

                new Object[]{"25. don't hit the bike below if there is another fallen bike at accelerator after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃā " +
                        "■■■■■",
                        null},

                new Object[]{"26. don't hit the bike below if there is another fallen bike at inhibitor after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃī " +
                        "■■■■■",
                        null},

                new Object[]{"27. don't hit the bike below if there is another fallen bike at line changer up after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃū " +
                        "■■■■■",
                        null},

                new Object[]{"28. don't hit the bike below if there is another fallen bike at line changer up after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃū " +
                        "■■■■■",
                        null},

                new Object[]{"29. don't hit the bike below if there is another fallen bike at line changer down after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃď " +
                        "■■■■■",
                        null},

                new Object[]{"30. don't hit the bike below if there is another fallen bike at obstacle after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃō " +
                        "■■■■■",
                        null},

                new Object[]{"31. don't hit the bike below if there is an accelerator and obstacle after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ>|" +
                        "■■■■■",
                        null},

                new Object[]{"32. don't hit the bike below if there is an accelerator and another bike after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ>Ḃ" +
                        "■■■■■",
                        null},

                new Object[]{"33. don't hit the bike below if there is an accelerator and fallen bike after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ>ḃ" +
                        "■■■■■",
                        null},

                new Object[]{"34. don't hit the bike below if there is bike at inhibitor after it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  ḂĪ " +
                        "■■■■■",
                        null},

                new Object[]{"35. don't hit the bike above if there is obstacle after it",
                        "■■■■■" +
                        "  Ḃ| " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"36. don't hit the bike above if there is another fallen bike at accelerator after it",
                        "■■■■■" +
                        "  Ḃā " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"37. don't hit the bike above if there is another fallen bike at inhibitor after it",
                        "■■■■■" +
                        "  Ḃī " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"38. don't hit the bike above if there is another fallen bike at line changer up after it",
                        "■■■■■" +
                        "  Ḃū " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"39. don't hit the bike above if there is another fallen bike at line changer up after it",
                        "■■■■■" +
                        "  Ḃū " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"40. don't hit the bike above if there is another fallen bike at line changer down after it",
                        "■■■■■" +
                        "  Ḃď " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"41. don't hit the bike above if there is another fallen bike at obstacle after it",
                        "■■■■■" +
                        "  Ḃō " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"42. don't hit the bike above if there is an accelerator and obstacle after it",
                        "■■■■■" +
                        "  Ḃ>|" +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"43. don't hit the bike above if there is an accelerator and another bike after it",
                        "■■■■■" +
                        "  Ḃ>Ḃ" +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"44. don't hit the bike above if there is an accelerator and fallen bike after it",
                        "■■■■■" +
                        "  Ḃ>ḃ" +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"45. don't hit the bike above if there is bike at inhibitor after it",
                        "■■■■■" +
                        "  ḂĪ " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"46. neutralize line changer up if there is fence above and nothing in front",
                        "■■■■■" +
                        "  U  " +
                        "     " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"47. neutralize line changer up if there is obstacle above in front and nothing in front",
                        "■■■■■" +
                        "   | " +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"48. neutralize line changer up if there is fallen bike above in front and nothing in front",
                        "■■■■■" +
                        "   ḃ " +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"49. neutralize line changer up if there is another bike at inhibitor above in front and nothing in front",
                        "■■■■■" +
                        "   Ī " +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"50. neutralize line changer up if there is accelerator and obstacle after it above in front and nothing in front",
                        "■■■■■" +
                        "   >|" +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"51. neutralize line changer up if there is accelerator and fallen bike after it above in front and nothing in front",
                        "■■■■■" +
                        "   >ḃ" +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"52. neutralize line changer up if there is other bike in front and fallen bike above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   | " +
                        "  UḂ " +
                        "■■■■■",
                        DOWN},

                new Object[]{"53. neutralize line changer down if there is fence below and nothing in front",
                        "■■■■■" +
                        "     " +
                        "     " +
                        "  D  " +
                        "■■■■■",
                        UP},

                new Object[]{"54. neutralize line changer down if there is obstacle below in front and nothing in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"55. neutralize line changer down if there is fallen bike below in front and nothing in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "   ḃ " +
                        "■■■■■",
                        UP},

                new Object[]{"56. neutralize line changer down if there is another bike at inhibitor below in front and nothing in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "   Ī " +
                        "■■■■■",
                        UP},

                new Object[]{"57. neutralize line changer down if there is accelerator and obstacle after it below in front and nothing in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "   >|" +
                        "■■■■■",
                        UP},

                new Object[]{"58. neutralize line changer down if there is accelerator and fallen bike after it below in front and nothing in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "   >ḃ" +
                        "■■■■■",
                        UP},

                new Object[]{"59. neutralize line changer down if there is other bike in front and fallen bike above in front and nothing above it",
                        "■■■■■" +
                        "  DḂ " +
                        "   | " +
                        "     " +
                        "■■■■■",
                        UP},

                new Object[]{"60. do nothing when at line changer up and there is nothing above in front",
                        "■■■■■" +
                        "     " +
                        "  U  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"61. do nothing when at line changer down and there is nothing below in front",
                        "■■■■■" +
                        "     " +
                        "  D  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"62. force line changer up if there is obstacle in front and another obstacle above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   | " +
                        "  U| " +
                        "■■■■■",
                        UP},

                new Object[]{"63. force line changer up if there is fallen bike in front and another fallen bike above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   ḃ " +
                        "  Uḃ " +
                        "■■■■■",
                        UP},

                new Object[]{"64. force line changer up if there is obstacle in front and fallen bike above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   ḃ " +
                        "  U| " +
                        "■■■■■",
                        UP},

                new Object[]{"65. force line changer up if there is obstacle in front and fallen bike above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   | " +
                        "  Uḃ " +
                        "■■■■■",
                        UP},

                new Object[]{"66. force line changer up if there is obstacle in front and another bike at inhibitor above in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   Ī " +
                        "  U| " +
                        "■■■■■",
                        UP},

                new Object[]{"67. force line changer up if there is obstacle in front, accelerator and obstacle after it above it in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   >|" +
                        "  U| " +
                        "■■■■■",
                        UP},

                new Object[]{"68. force line changer up if there is obstacle in front, accelerator and fallen bike after it above it in front and nothing above it",
                        "■■■■■" +
                        "     " +
                        "   >ḃ" +
                        "  U| " +
                        "■■■■■",
                        UP},

                new Object[]{"69. force line changer down if there is obstacle in front and below in front ant nothing below it",
                        "■■■■■" +
                        "  D| " +
                        "   | " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"70. force line changer down if there is fallen bike in front and below in front and nothing below it",
                        "■■■■■" +
                        "  Dḃ " +
                        "   ḃ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"71. force line changer down if there is fallen bike in front and bike at inhibitor below in front and nothing below it",
                        "■■■■■" +
                        "  Dḃ " +
                        "   Ī " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"72. force line changer down if there is obstacle in front and accelerator and obstacle below in front and nothing below it",
                        "■■■■■" +
                        "  D| " +
                        "   >|" +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"73. force line changer down if there is fallen bike in front and accelerator and fallen bike after it below in front and nothing below it",
                        "■■■■■" +
                        "  Dḃ " +
                        "   >ḃ" +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"74. do nothing when at line changer up and there is obstacle in front and fallen bike above in front and fence above it",
                        "■■■■■" +
                        "   ḃ " +
                        "  U| " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"75. do nothing when at line changer down and there is fallen bike in front and obstacle below in front and fallen bike at ;ine changer down below it",
                        "■■■■■" +
                        "  Dḃ " +
                        "   | " +
                        "   ď " +
                        "■■■■■",
                        null},

                new Object[]{"76. go down if there is another bike at accelerator in front and obstacle above",
                        "■■■■■" +
                        "   | " +
                        "  BĀ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"77. go down if there is another bike at inhibitor in front and obstacle above",
                        "■■■■■" +
                        "   | " +
                        "  BĪ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"78. go down if there is another bike at line changer up in front and obstacle above",
                        "■■■■■" +
                        "   | " +
                        "  BŪ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"79. go down if there is another bike at line changer down in front and obstacle above",
                        "■■■■■" +
                        "   | " +
                        "  BĎ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"80. go down if there is another bike at killed bike in front and obstacle above",
                        "■■■■■" +
                        "   | " +
                        "  BḰ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"81. go down if there is another bike in front and fallen bike at fence obstacle above",
                        "■ḟḟ■■" +
                        "  Bḃ " +
                        "     " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"82. go up if there is fallen bike at accelerator in front and obstacle below",
                        "■■■■■" +
                        "     " +
                        "  Bā " +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"83. go up if there is fallen bike at inhibitor in front and obstacle below",
                        "■■■■■" +
                        "     " +
                        "  Bī " +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"84. go up if there is fallen bike at line changer up in front and obstacle below",
                        "■■■■■" +
                        "     " +
                        "  Bū " +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"85. go up if there is fallen bike at line changer down in front and obstacle below",
                        "■■■■■" +
                        "     " +
                        "  Bď " +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"86. go up if there is fallen bike at obstacle in front and fallen bike at fence below",
                        "■■■■■" +
                        "     " +
                        "     " +
                        "  Bō " +
                        "■■ḟ■■",
                        UP},

                new Object[]{"87. go down if there is line changer up in front and fence above it",
                        "■■■■■" +
                        "  B▲ " +
                        "     " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"88. go down if there is line changer up in front and fallen bike above it",
                        "■■■■■" +
                        "   ḃ " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"89. go down if there is line changer up in front and fallen bike at accelerator above it",
                        "■■■■■" +
                        "   ā " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"90. go down if there is line changer up in front and fallen bike at inhibitor above it",
                        "■■■■■" +
                        "   ī " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"91. go down if there is line changer up in front and fallen bike at line changer up above it",
                        "■■■■■" +
                        "   ū " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"92. go down if there is line changer up in front and fallen bike at line changer down above it",
                        "■■■■■" +
                        "   ď " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"93. go down if there is line changer up in front and fallen bike at killed bike above it",
                        "■■■■■" +
                        "   Ḱ " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"94. go down if there is line changer up in front and fallen bike at fence above it",
                        "■■■■■" +
                        "   ḟ " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"95. go down if there is line changer up in front and fallen bike at obstacle above it",
                        "■■■■■" +
                        "   ō " +
                        "  B▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"96. go up if there is line changer down in front and fence below it",
                        "■■■■■" +
                        "     " +
                        "     " +
                        "  B▼ " +
                        "■■■■■",
                        UP},

                new Object[]{"97. go up if there is line changer down in front and fallen bike above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ḃ " +
                        "■■■■■",
                        UP},

                new Object[]{"98. go up if there is line changer down in front and fallen bike at accelerator above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ā " +
                        "■■■■■",
                        UP},

                new Object[]{"99. go up if there is line changer down in front and fallen bike at inhibitor above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ī " +
                        "■■■■■",
                        UP},

                new Object[]{"100. go up if there is line changer down in front and fallen bike at line changer up above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ū " +
                        "■■■■■",
                        UP},

                new Object[]{"101. go up if there is line changer down in front and fallen bike at line changer down above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ď " +
                        "■■■■■",
                        UP},

                new Object[]{"102. go up if there is line changer down in front and fallen bike at killed bike above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   Ḱ " +
                        "■■■■■",
                        UP},

                new Object[]{"103. go up if there is line changer down in front and fallen bike at fence above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ḟ " +
                        "■■■■■",
                        UP},

                new Object[]{"104. go up if there is line changer down in front and fallen bike at obstacle above it",
                        "■■■■■" +
                        "     " +
                        "  B▼ " +
                        "   ō " +
                        "■■■■■",
                        UP},

                new Object[]{"105. go up if there is obstacle above and accelerator in front and fallen bike in front of it",
                        "■■■■■" +
                        "     " +
                        "  B>ḃ" +
                        "   | " +
                        "■■■■■",
                        UP},

                new Object[]{"106. go down if there is fallen bike at another bike below and accelerator in front and fallen bike in front of it",
                        "■■■■■" +
                        "   Ḱ " +
                        "  B>ḃ" +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"107. go down if there is bike above and obstacle in front",
                        "■■■■■" +
                        "  Ḃ  " +
                        "  B| " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"108. go up if there is bike below and fallen bike at inhibitor in front",
                        "■■■■■" +
                        "     " +
                        "  Bī " +
                        "  Ḃ  " +
                        "■■■■■",
                        UP},

                new Object[]{"109. go up if there is fallen bike at line changer up below and accelerator leading to line changer down leading to obstacle in front",
                        "■■■■■" +
                        "     " +
                        " B>▼ " +
                        "  ū |" +
                        "■■■■■",
                        UP},

                new Object[]{"110. go down if there is fallen bike at obstacle above and accelerator leading to line changer up leading to other bike at killed bike in front",
                        "■■■■■" +
                        "  ō Ḱ" +
                        " B>▲ " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"111. go to random up/down if there is obstacle in 2 cells in front and obstacles below and above in 1 cell",
                        "■■■■■" +
                        "    |" +
                        "  B |" +
                        "    |" +
                        "■■■■■",
                        STOP},

                new Object[]{"112. go to up if there is other bike below and line changer up in front of it",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ḃ▲ " +
                        "■■■■■",
                        UP},

                new Object[]{"113. go to up if there is other bike at line changer up below",
                        "■■■■■" +
                        "     " +
                        "  B  " +
                        "  Ū  " +
                        "■■■■■",
                        UP},

                new Object[]{"114. go down if there is other bike at killed bike above and line changer down in front of it",
                        "■■■■■" +
                        "  Ḱ▼ " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"115. go down if there is other bike at line changer down above",
                        "■■■■■" +
                        "  Ď  " +
                        "  B  " +
                        "     " +
                        "■■■■■",
                        DOWN},

                new Object[]{"117. not go down or up at springboard left if there is another bike in front",
                        "■╔═╗■" +
                        "  /═\\" +
                        "  LḂ\\" +
                        "  ╚═╝" +
                        "■■■■■",
                        null},

                new Object[]{"118. not go down or up at springboard right if there is another bike in front",
                        "■╔═╗■" +
                        "  /═\\" +
                        " /═RḂ" +
                        "  ╚═╝" +
                        "■■■■■",
                        null},

                new Object[]{"119. not go down at lowest line when enemy bike above at line changer down",
                        "■■■■■" +
                        "     " +
                        "  Ď  " +
                        "  B  " +
                        "■■■■■",
                        null},

                new Object[]{"120. not go up at highest line when enemy bike below at line changer up",
                        "■■■■■" +
                        "  B  " +
                        "  Ū  " +
                        "     " +
                        "■■■■■",
                        null},

                new Object[]{"121. go up at lowest springboard top line when enemy bike in front and upper",
                        "■╔══╗" +
                        " /══\\" +
                        " /═Ḃ\\" +
                        " ╚BḂ╝" +
                        "■■■■■",
                        UP},

                new Object[]{"122. go down at highest springboard top line when enemy bike in front and downer",
                        "■╔BḂ╗" +
                        " /═Ḃ\\" +
                        " /══\\" +
                        "  ╚═╝" +
                        "■■■■■",
                        DOWN}
        );
    }

    @Test
    public void get__shouldReturnAppropriateDirection__accordingToGameElementTypeAround() {
        //given
        Board board = toBoard(boardString);
        if (expectedDirection == STOP) {
            when(dice.next(2)).then((Answer<Integer>) invocationOnMock -> {
                int randomInt = new Random().nextInt(2);
                expectedDirection = randomInt == 1 ? DOWN : UP;
                return randomInt;
            });
        }

        //when
        String result = solver.get(board);

        //then
        assertThat(result, is(expectedDirection != null ? expectedDirection.toString() : ""));
    }

    private Board toBoard(String board) {
        return (Board) new Board().forString(board);
    }

}
