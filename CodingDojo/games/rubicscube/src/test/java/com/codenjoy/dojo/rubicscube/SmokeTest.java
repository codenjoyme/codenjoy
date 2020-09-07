package com.codenjoy.dojo.rubicscube;

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


import com.codenjoy.dojo.client.local.LocalGameRunner;
import com.codenjoy.dojo.rubicscube.client.Board;
import com.codenjoy.dojo.rubicscube.client.ai.AISolver;
import com.codenjoy.dojo.rubicscube.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SmokeTest {

    private Dice dice;

    @Test
    public void test() {
        // given
        List<String> messages = new LinkedList<>();

        LocalGameRunner.timeout = 0;
        LocalGameRunner.out = (e) -> messages.add(e);
        LocalGameRunner.countIterations = 20;
        LocalGameRunner.printConversions = false;
        LocalGameRunner.printBoardOnly = true;
        LocalGameRunner.printDice = false;
        LocalGameRunner.printTick = true;

        Dice dice = LocalGameRunner.getDice(
                0, 4, 3, 1, // random numbers
                0, 4, 2, 3,
                2, 1, 4, 2,
                0, 4, 3, 3,
                2, 4, 2, 1,
                4, 1, 1, 3);

        GameRunner gameType = new GameRunner() {
            @Override
            public Dice getDice() {
                return dice;
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new AISolver(dice),
                new Board());

        // then
        assertEquals("1: 1:   WWW      \n" +
                    "1: 1:   WWW      \n" +
                    "1: 1:   WWW      \n" +
                    "1: 1:BBBOOOGGGRRR\n" +
                    "1: 1:BBBOOOGGGRRR\n" +
                    "1: 1:BBBOOOGGGRRR\n" +
                    "1: 1:   YYY      \n" +
                    "1: 1:   YYY      \n" +
                    "1: 1:   YYY      \n" +
                    "1: 1:            \n" +
                    "1: 1:            \n" +
                    "1: 1:            \n" +
                    "1: 1:\n" +
                    "1: 1:Scores: 0\n" +
                    "1: 1:Answer: ACT(5, 1)\n" +
                    "------------------------------------------\n" +
                    "2: 1:   WWW      \n" +
                    "2: 1:   WWW      \n" +
                    "2: 1:   WWW      \n" +
                    "2: 1:OOOGGGRRRBBB\n" +
                    "2: 1:BBBOOOGGGRRR\n" +
                    "2: 1:BBBOOOGGGRRR\n" +
                    "2: 1:   YYY      \n" +
                    "2: 1:   YYY      \n" +
                    "2: 1:   YYY      \n" +
                    "2: 1:            \n" +
                    "2: 1:            \n" +
                    "2: 1:            \n" +
                    "2: 1:\n" +
                    "2: 1:Scores: 0\n" +
                    "2: 1:Answer: ACT(2, 1)\n" +
                    "------------------------------------------\n" +
                    "3: 1:   WWW      \n" +
                    "3: 1:   WWW      \n" +
                    "3: 1:   BBO      \n" +
                    "3: 1:OOYOOGWRRBBB\n" +
                    "3: 1:BBYOOGWGGRRR\n" +
                    "3: 1:BBYOOGWGGRRR\n" +
                    "3: 1:   GGR      \n" +
                    "3: 1:   YYY      \n" +
                    "3: 1:   YYY      \n" +
                    "3: 1:            \n" +
                    "3: 1:            \n" +
                    "3: 1:            \n" +
                    "3: 1:\n" +
                    "3: 1:Scores: 0\n" +
                    "3: 1:Answer: ACT(5, -1)\n" +
                    "------------------------------------------\n" +
                    "4: 1:   WWO      \n" +
                    "4: 1:   WWB      \n" +
                    "4: 1:   WWB      \n" +
                    "4: 1:BBBOOYOOGWRR\n" +
                    "4: 1:BBYOOGWGGRRR\n" +
                    "4: 1:BBYOOGWGGRRR\n" +
                    "4: 1:   GGR      \n" +
                    "4: 1:   YYY      \n" +
                    "4: 1:   YYY      \n" +
                    "4: 1:            \n" +
                    "4: 1:            \n" +
                    "4: 1:            \n" +
                    "4: 1:\n" +
                    "4: 1:Scores: 0\n" +
                    "4: 1:Answer: ACT(4, -1)\n" +
                    "------------------------------------------\n" +
                    "5: 1:   BBB      \n" +
                    "5: 1:   WWB      \n" +
                    "5: 1:   WWB      \n" +
                    "5: 1:YBBOOYOOWRRR\n" +
                    "5: 1:YBYOOGWGWRRR\n" +
                    "5: 1:YBYOOGWGOWRR\n" +
                    "5: 1:   GGR      \n" +
                    "5: 1:   YYY      \n" +
                    "5: 1:   GGG      \n" +
                    "5: 1:            \n" +
                    "5: 1:            \n" +
                    "5: 1:            \n" +
                    "5: 1:\n" +
                    "5: 1:Scores: 0\n" +
                    "5: 1:Answer: ACT(2, 2)\n" +
                    "------------------------------------------\n" +
                    "6: 1:   BBB      \n" +
                    "6: 1:   WWB      \n" +
                    "6: 1:   RGG      \n" +
                    "6: 1:YBWGOOYOWRRR\n" +
                    "6: 1:YBWGOOYGWRRR\n" +
                    "6: 1:YBOYOOBGOWRR\n" +
                    "6: 1:   BWW      \n" +
                    "6: 1:   YYY      \n" +
                    "6: 1:   GGG      \n" +
                    "6: 1:            \n" +
                    "6: 1:            \n" +
                    "6: 1:            \n" +
                    "6: 1:\n" +
                    "6: 1:Scores: 0\n" +
                    "6: 1:Answer: ACT(3, 1)\n" +
                    "------------------------------------------\n" +
                    "7: 1:   BBO      \n" +
                    "7: 1:   WWO      \n" +
                    "7: 1:   RGO      \n" +
                    "7: 1:YBWGOWBYYGRR\n" +
                    "7: 1:YBWGOYGGOBRR\n" +
                    "7: 1:YBOYOGOWWBRR\n" +
                    "7: 1:   BWW      \n" +
                    "7: 1:   YYR      \n" +
                    "7: 1:   GGR      \n" +
                    "7: 1:            \n" +
                    "7: 1:            \n" +
                    "7: 1:            \n" +
                    "7: 1:\n" +
                    "7: 1:Scores: 0\n" +
                    "7: 1:Answer: ACT(5, 1)\n" +
                    "------------------------------------------\n" +
                    "8: 1:   RWB      \n" +
                    "8: 1:   GWB      \n" +
                    "8: 1:   OOO      \n" +
                    "8: 1:GOWBYYGRRYBW\n" +
                    "8: 1:YBWGOYGGOBRR\n" +
                    "8: 1:YBOYOGOWWBRR\n" +
                    "8: 1:   BWW      \n" +
                    "8: 1:   YYR      \n" +
                    "8: 1:   GGR      \n" +
                    "8: 1:            \n" +
                    "8: 1:            \n" +
                    "8: 1:            \n" +
                    "8: 1:\n" +
                    "8: 1:Scores: 0\n" +
                    "8: 1:Answer: ACT(4, -1)\n" +
                    "------------------------------------------\n" +
                    "9: 1:   YYG      \n" +
                    "9: 1:   GWB      \n" +
                    "9: 1:   OOO      \n" +
                    "9: 1:GOWBYYGRRWRR\n" +
                    "9: 1:GBWGOYGGWBRR\n" +
                    "9: 1:RBOYOGOWBYBB\n" +
                    "9: 1:   BWW      \n" +
                    "9: 1:   YYR      \n" +
                    "9: 1:   WOR      \n" +
                    "9: 1:            \n" +
                    "9: 1:            \n" +
                    "9: 1:            \n" +
                    "9: 1:\n" +
                    "9: 1:Scores: 0\n" +
                    "9: 1:Answer: ACT(5, -1)\n" +
                    "------------------------------------------\n" +
                    "10: 1:   GBO      \n" +
                    "10: 1:   YWO      \n" +
                    "10: 1:   YGO      \n" +
                    "10: 1:WRRGOWBYYGRR\n" +
                    "10: 1:GBWGOYGGWBRR\n" +
                    "10: 1:RBOYOGOWBYBB\n" +
                    "10: 1:   BWW      \n" +
                    "10: 1:   YYR      \n" +
                    "10: 1:   WOR      \n" +
                    "10: 1:            \n" +
                    "10: 1:            \n" +
                    "10: 1:            \n" +
                    "10: 1:\n" +
                    "10: 1:Scores: 0\n" +
                    "10: 1:Answer: ACT(2, 2)\n" +
                    "------------------------------------------\n" +
                    "11: 1:   GBO      \n" +
                    "11: 1:   YWO      \n" +
                    "11: 1:   WWB      \n" +
                    "11: 1:WROGOYOYYGRR\n" +
                    "11: 1:GBGYOGWGWBRR\n" +
                    "11: 1:RBBWOGRWBYBB\n" +
                    "11: 1:   OGY      \n" +
                    "11: 1:   YYR      \n" +
                    "11: 1:   WOR      \n" +
                    "11: 1:            \n" +
                    "11: 1:            \n" +
                    "11: 1:            \n" +
                    "11: 1:\n" +
                    "11: 1:Scores: 0\n" +
                    "11: 1:Answer: ACT(2, 2)\n" +
                    "------------------------------------------\n" +
                    "12: 1:   GBO      \n" +
                    "12: 1:   YWO      \n" +
                    "12: 1:   YGO      \n" +
                    "12: 1:WRRGOWBYYGRR\n" +
                    "12: 1:GBWGOYGGWBRR\n" +
                    "12: 1:RBOYOGOWBYBB\n" +
                    "12: 1:   BWW      \n" +
                    "12: 1:   YYR      \n" +
                    "12: 1:   WOR      \n" +
                    "12: 1:            \n" +
                    "12: 1:            \n" +
                    "12: 1:            \n" +
                    "12: 1:\n" +
                    "12: 1:Scores: 0\n" +
                    "12: 1:Answer: ACT(4, 1)\n" +
                    "------------------------------------------\n" +
                    "13: 1:   YWB      \n" +
                    "13: 1:   YWO      \n" +
                    "13: 1:   YGO      \n" +
                    "13: 1:ORRGOWBYRYBG\n" +
                    "13: 1:BBWGOYGGOBRR\n" +
                    "13: 1:GBOYOGOWWBRR\n" +
                    "13: 1:   BWW      \n" +
                    "13: 1:   YYR      \n" +
                    "13: 1:   WGR      \n" +
                    "13: 1:            \n" +
                    "13: 1:            \n" +
                    "13: 1:            \n" +
                    "13: 1:\n" +
                    "13: 1:Scores: 0\n" +
                    "13: 1:Answer: ACT(5, 1)\n" +
                    "------------------------------------------\n" +
                    "14: 1:   YYY      \n" +
                    "14: 1:   GWW      \n" +
                    "14: 1:   OOB      \n" +
                    "14: 1:GOWBYRYBGORR\n" +
                    "14: 1:BBWGOYGGOBRR\n" +
                    "14: 1:GBOYOGOWWBRR\n" +
                    "14: 1:   BWW      \n" +
                    "14: 1:   YYR      \n" +
                    "14: 1:   WGR      \n" +
                    "14: 1:            \n" +
                    "14: 1:            \n" +
                    "14: 1:            \n" +
                    "14: 1:\n" +
                    "14: 1:Scores: 0\n" +
                    "14: 1:Answer: ACT(2, 1)\n" +
                    "------------------------------------------\n" +
                    "15: 1:   YYY      \n" +
                    "15: 1:   GWW      \n" +
                    "15: 1:   OWW      \n" +
                    "15: 1:GOBYGBOBGORR\n" +
                    "15: 1:BBWOOYOGOBRR\n" +
                    "15: 1:GBWGYRBWWBRR\n" +
                    "15: 1:   OGY      \n" +
                    "15: 1:   YYR      \n" +
                    "15: 1:   WGR      \n" +
                    "15: 1:            \n" +
                    "15: 1:            \n" +
                    "15: 1:            \n" +
                    "15: 1:\n" +
                    "15: 1:Scores: 0\n" +
                    "15: 1:Answer: ACT(5, -1)\n" +
                    "------------------------------------------\n" +
                    "16: 1:   YWW      \n" +
                    "16: 1:   YWW      \n" +
                    "16: 1:   YGO      \n" +
                    "16: 1:ORRGOBYGBOBG\n" +
                    "16: 1:BBWOOYOGOBRR\n" +
                    "16: 1:GBWGYRBWWBRR\n" +
                    "16: 1:   OGY      \n" +
                    "16: 1:   YYR      \n" +
                    "16: 1:   WGR      \n" +
                    "16: 1:            \n" +
                    "16: 1:            \n" +
                    "16: 1:            \n" +
                    "16: 1:\n" +
                    "16: 1:Scores: 0\n" +
                    "16: 1:Answer: ACT(4, -1)\n" +
                    "------------------------------------------\n" +
                    "17: 1:   GBO      \n" +
                    "17: 1:   YWW      \n" +
                    "17: 1:   YGO      \n" +
                    "17: 1:WRRGOBYGYGRR\n" +
                    "17: 1:GBWOOYOGWBRR\n" +
                    "17: 1:RBWGYRBWWOBB\n" +
                    "17: 1:   OGY      \n" +
                    "17: 1:   YYR      \n" +
                    "17: 1:   WOB      \n" +
                    "17: 1:            \n" +
                    "17: 1:            \n" +
                    "17: 1:            \n" +
                    "17: 1:\n" +
                    "17: 1:Scores: 0\n" +
                    "17: 1:Answer: ACT(2, 2)\n" +
                    "------------------------------------------\n" +
                    "18: 1:   GBO      \n" +
                    "18: 1:   YWW      \n" +
                    "18: 1:   YGO      \n" +
                    "18: 1:WRBRYGWGYGRR\n" +
                    "18: 1:GBOYOOWGWBRR\n" +
                    "18: 1:RBYBOGRWWOBB\n" +
                    "18: 1:   OGY      \n" +
                    "18: 1:   YYR      \n" +
                    "18: 1:   WOB      \n" +
                    "18: 1:            \n" +
                    "18: 1:            \n" +
                    "18: 1:            \n" +
                    "18: 1:\n" +
                    "18: 1:Scores: 0\n" +
                    "18: 1:Answer: ACT(3, 1)\n" +
                    "------------------------------------------\n" +
                    "19: 1:   GBG      \n" +
                    "19: 1:   YWO      \n" +
                    "19: 1:   YGG      \n" +
                    "19: 1:WRBRYYRWWORR\n" +
                    "19: 1:GBOYORWGGWRR\n" +
                    "19: 1:RBYBOBWWYOBB\n" +
                    "19: 1:   OGO      \n" +
                    "19: 1:   YYB      \n" +
                    "19: 1:   WOG      \n" +
                    "19: 1:            \n" +
                    "19: 1:            \n" +
                    "19: 1:            \n" +
                    "19: 1:\n" +
                    "19: 1:Scores: 0\n" +
                    "19: 1:Answer: ACT(5, 1)\n" +
                    "------------------------------------------\n" +
                    "20: 1:   YYG      \n" +
                    "20: 1:   GWB      \n" +
                    "20: 1:   GOG      \n" +
                    "20: 1:RYYRWWORRWRB\n" +
                    "20: 1:GBOYORWGGWRR\n" +
                    "20: 1:RBYBOBWWYOBB\n" +
                    "20: 1:   OGO      \n" +
                    "20: 1:   YYB      \n" +
                    "20: 1:   WOG      \n" +
                    "20: 1:            \n" +
                    "20: 1:            \n" +
                    "20: 1:            \n" +
                    "20: 1:\n" +
                    "20: 1:Scores: 0\n" +
                    "20: 1:Answer: ACT(4, -1)\n" +
                    "------------------------------------------",
                String.join("\n", messages));

    }
}
