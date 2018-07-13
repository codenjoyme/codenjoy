package com.codenjoy.dojo.snake.battle;

import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.LocalGameRunner;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.snake.battle.client.Board;
import com.codenjoy.dojo.snake.battle.client.ai.AISolver;
import com.codenjoy.dojo.snake.battle.services.GameRunner;
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
        LocalGameRunner.countIterations = 10;

        Dice dice = LocalGameRunner.getDice(
                0, 1, 2, 3, 4, // random numbers
                0, 2, 2, 3, 1,
                0, 1, 4, 1, 2,
                1, 3, 2, 4, 1,
                1, 3, 0, 0, 1,
                1, 3, 0, 0, 1,
                0, 0, 1, 0, 2,
                0, 0, 1, 0, 2,
                1, 3, 2, 4, 1,
                1, 3, 2, 4, 1,
                0, 1, 1, 3, 3,
                0, 1, 1, 3, 3,
                0, 2, 0, 0, 2,
                0, 2, 0, 0, 2,
                0, 1, 2, 2, 1,
                0, 1, 2, 2, 1,
                0, 1, 2, 2, 1,
                0, 1, 2, 2, 1,
                0, 1, 2, 2, 1,
                1, 3, 4, 3, 4,
                1, 3, 2, 4, 1,
                0, 1, 3, 1, 1);

        GameRunner gameType = new GameRunner() {
            @Override
            protected Dice getDice() {
                return dice;
            }

            @Override
            protected String getMap() {
                return  "☼☼☼☼☼☼☼☼☼" +
                        "☼☼      ☼" +
                        "☼#      ☼" +
                        "☼☼ ☼# ○ ☼" +
                        "☼☼○    $☼" +
                        "☼☼   ●  ☼" +
                        "☼#      ☼" +
                        "☼☼  ●   ☼" +
                        "☼☼☼☼☼☼☼☼☼";
            }
        };

        // when
        LocalGameRunner.run(gameType,
                new LinkedList<Solver>(){{
                    add(new AISolver(dice));
                    add(new AISolver(dice));
                }},
                new LinkedList<ClientBoard>(){{
                    add(new Board());
                    add(new Board());
                }});

        // then
        assertEquals("DICE:0\n" +
                        "DICE:1\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:ᓕ⬢      ☼\n" +
                        "1:☼☼ ᓚ⬡ ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:3\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:ᓚ⬡      ☼\n" +
                        "2:☼☼ ᓕ⬢ ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:3\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "DICE:2\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:ᓚ⬡      ☼\n" +
                        "1:☼☼ ☼# ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:ᓕ⬢      ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 4 :0\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:ᓕ⬢      ☼\n" +
                        "2:☼☼ ☼# ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:ᓚ⬡      ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:3\n" +
                        "DICE:0\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:0\n" +
                        "DICE:2\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼ ᓕ⬢ ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼   ●  ☼\n" +
                        "1:ᓚ⬡○     ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼ ᓚ⬡ ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼   ●  ☼\n" +
                        "2:ᓕ⬢○     ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "2:Answer: RIGHT\n" +
                        "DICE:2\n" +
                        "DICE:2\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:4\n" +
                        "DICE:3\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE_CORRECTED < 3 :0\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:2\n" +
                        "DICE:4\n" +
                        "DICE_CORRECTED < 3 :1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:ᓕ⬢      ☼\n" +
                        "1:☼☼ ᓚ⬡ ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:☼#○     ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:0\n" +
                        "DICE:1\n" +
                        "DICE:3\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:ᓚ⬡      ☼\n" +
                        "2:☼☼ ᓕ⬢ ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:☼#○     ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: DOWN\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:ᓚ⬡      ☼\n" +
                        "1:☼☼ ☼# ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:ᓕ⬢○     ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:ᓕ⬢      ☼\n" +
                        "2:☼☼ ☼# ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:ᓚ⬡○     ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: RIGHT\n" +
                        "Fire Event: START\n" +
                        "Fire Event: APPLE\n" +
                        "Fire Event: START\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼⇒>     ☼\n" +
                        "1:☼☼ ☼# ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:→═►     ☼\n" +
                        "1:☼☼○ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: DOWN, ACT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼→►     ☼\n" +
                        "2:☼☼ ☼# ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:⇒—∨     ☼\n" +
                        "2:☼☼○ ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: DOWN, ACT\n" +
                        "Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#⇓     ☼\n" +
                        "1:☼☼∨☼# ○ ☼\n" +
                        "1:☼☼○    $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:→═╗     ☼\n" +
                        "1:☼☼▼ ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: RIGHT\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#↓     ☼\n" +
                        "2:☼☼▼☼# ○ ☼\n" +
                        "2:☼☼○    $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:⇒—╕     ☼\n" +
                        "2:☼☼> ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: DOWN\n" +
                        "Fire Event: APPLE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#⇓     ☼\n" +
                        "1:☼☼|☼# ○ ☼\n" +
                        "1:☼☼∨    $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:☼→╗     ☼\n" +
                        "1:☼☼╚►●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#↓     ☼\n" +
                        "2:☼☼║☼# ○ ☼\n" +
                        "2:☼☼▼    $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:☼⇒╕     ☼\n" +
                        "2:☼☼╙∧●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: DOWN, ACT\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼⇓☼# ○ ☼\n" +
                        "1:☼☼|    $☼\n" +
                        "1:☼☼∨ ○●  ☼\n" +
                        "1:☼#↓▲    ☼\n" +
                        "1:☼☼╚╝●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: UP\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼↓☼# ○ ☼\n" +
                        "2:☼☼║    $☼\n" +
                        "2:☼☼▼ ○●  ☼\n" +
                        "2:☼#⇓∧    ☼\n" +
                        "2:☼☼╙╜●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: RIGHT\n" +
                        "Fire Event: DIE\n" +
                        "Fire Event: DIE\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "------------------------------------------\n" +
                        "1:Board:\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:☼☼      ☼\n" +
                        "1:ᓚ⬡      ☼\n" +
                        "1:☼☼ ᓕ⬢ ○ ☼\n" +
                        "1:☼☼     $☼\n" +
                        "1:☼☼  ○●  ☼\n" +
                        "1:☼#      ☼\n" +
                        "1:☼☼  ●   ☼\n" +
                        "1:☼☼☼☼☼☼☼☼☼\n" +
                        "1:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "1:Answer: DOWN\n" +
                        "2:Board:\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:☼☼      ☼\n" +
                        "2:ᓕ⬢      ☼\n" +
                        "2:☼☼ ᓚ⬡ ○ ☼\n" +
                        "2:☼☼     $☼\n" +
                        "2:☼☼  ○●  ☼\n" +
                        "2:☼#      ☼\n" +
                        "2:☼☼  ●   ☼\n" +
                        "2:☼☼☼☼☼☼☼☼☼\n" +
                        "2:\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:Answer: RIGHT\n" +
                        "1:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "DICE:1\n" +
                        "2:PLAYER_GAME_OVER -> START_NEW_GAME\n" +
                        "DICE:1\n" +
                        "------------------------------------------",
                String.join("\n", messages));

    }
}
