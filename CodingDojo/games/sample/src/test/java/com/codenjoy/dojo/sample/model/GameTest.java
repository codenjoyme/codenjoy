package com.codenjoy.dojo.sample.model;

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


import org.junit.Test;

public class GameTest extends AbstractGameTest {

    @Test
    public void heroOnTheField() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanWalk_toTheLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanWalk_toTheRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanWalk_toTheUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanWalk_toTheDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void ifHeroDoesNotReceiveCommands_heDoesNotGoAnywhere() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        hero().left();
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCannotGoThroughTheBorder_toTheLeft() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺☼☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺☼☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCannotGoThroughTheBorder_toTheRight() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☼☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().left();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☼☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCannotGoThroughTheBorder_toTheUp() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼ ☼ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼ ☼ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCannotGoThroughTheBorder_toTheDown() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanLeaveTheBombUnderHim() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().bomb();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanLeaveTheBombUnderHim_andAtTheSameMomentMoveToTheSide() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().bomb();
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void thereIsNoDifferenceInWhatOrderTheMovementAndTheActCommandAreExecuted() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().down(); // different order than in the previous test
        hero().bomb();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    // на бомбе я взрываюсь
    @Test
    public void heroWillBlowUpOnABomb_IfHeWalksOnIt() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");

        assertEquals(true, hero().isAlive());

        // when
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ X ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[LOSE]");
        assertEquals(false, hero().isAlive());
    }

    @Test
    public void heroOnTheFieldCanLeaveAsManyBombsAsHeWants() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().bomb();
        hero().down();
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().bomb();
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ x☺☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCannotLeaveTwoBombsInOneCell() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().bomb();
        tick();

        hero().bomb();
        hero().down();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼☼☼☼☼\n");

        // when
        dice(1, 2);
        hero().up();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ X ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[LOSE]");

        // when
        field().newGame(player());
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void heroCanPickUpGoldOnTheMap_afterWhichItWillAppearInANewPlace() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺$☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        dice(1, 3);
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼$  ☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[WIN]");
    }

    @Test(timeout = 1000)
    public void ifThereIsNoPlaceForGold_thenTheProgramDoesNotFreeze() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺$☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        dice(0, 0); // there is no space in this cell because of board
        hero().right();
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents("[WIN]");
    }

    @Test
    public void shouldClearField_whenClearScoresOnGame() {
        // given
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺$☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when then
        assertWalkThenClearScores();
    }

    private void assertWalkThenClearScores() {
        hero().right();
        hero().bomb();
        dice(1, 1);
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ x☺☼\n" +
                "☼$  ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents(
                "[WIN]");

        assertEquals(30, hero(0).scores());

        hero().up();
        tick();

        assertF("☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼ x ☼\n" +
                "☼$  ☼\n" +
                "☼☼☼☼☼\n");

        // when
        dice(1, 2); // new hero position
        field().clearScore();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺ $☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertEquals(0, hero(0).scores());
    }

    @Test
    public void shouldHeroCanWalk_whenClearScoresOnGame() {
        // given
        shouldClearField_whenClearScoresOnGame();

        // when
        hero(0).right();
        tick();

        hero(0).right();
        dice(1, 3);
        tick();

        // then
        assertF("☼☼☼☼☼\n" +
                "☼$  ☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyAllEvents(
                "[WIN]");
    }

    @Test
    public void shouldSameLevel_whenClearScoresOnGame_andSeveralLevelsInTheSettings() {
        // given
        dice(1); // second level selected
        givenFl("☼☼☼☼☼\n" +
                "☼ $ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼ $ ☼\n" +
                "☼☼☼☼☼\n",

                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺$☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n",

                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when then
        assertWalkThenClearScores();
    }
}