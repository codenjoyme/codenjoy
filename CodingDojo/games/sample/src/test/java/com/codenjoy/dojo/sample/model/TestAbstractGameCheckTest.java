package com.codenjoy.dojo.sample.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import org.junit.Assert;
import org.junit.Test;

import static com.codenjoy.dojo.services.level.LevelsSettings.Keys.LEVELS_MAP;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;

public class TestAbstractGameCheckTest extends AbstractGameCheckTest {

    @Override
    protected void checkFile() {
        // do nothing
    }

    @Override
    public void assertEquals(Object expected, Object actual) {
        // do nothing
    }

    @Override
    public void after() {
        // do nothing
    }

    @Override
    public void setupSettings() {
        settings().bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 5);
    }

    @Test
    public void testGivenAssert() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertF("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testGivenAssert()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼   ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    assertF(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼   ☼\n" +
                "        ☼ X ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼,\n" +
                "        0)");
    }

    private void assertMessages(String expected) {
        Assert.assertEquals(expected, manager.messages());
    }

    @Test
    public void testHero() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        hero().left();
        hero().right();
        hero().up();
        hero().down();
        hero().act();
        hero(1).act(1);
        hero(0).act(1, 2);
        hero(1).act(1, 2, 3);
        hero().isAlive();
        hero().isActive();
        hero().scores();
        hero().addScore(100);
        hero(0).scores();
        hero(1).scores();
        tick();

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testHero()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,3])\n" +
                "            dice(2, 3)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    hero(0).left()\n" +
                "    hero(0).right()\n" +
                "    hero(0).up()\n" +
                "    hero(0).down()\n" +
                "    hero(0).act()\n" +
                "    hero(1).act(1)\n" +
                "    hero(0).act(1, 2)\n" +
                "    hero(1).act(1, 2, 3)\n" +
                "    hero(0).isAlive() = true\n" +
                "    hero(0).isActive() = false\n" +
                "    hero(0).scores() = 0\n" +
                "    hero(0).addScore(100)\n" +
                "    hero(0).scores() = 100\n" +
                "    hero(1).scores() = 0\n" +
                "    tick()");
    }

    @Test
    public void testPlayer() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        player().shouldLeave();
        player().shouldLeave();
        player(0).shouldLeave();
        player(1).wantToStay();
        tick();

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testPlayer()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,3])\n" +
                "            dice(2, 3)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    player(0).shouldLeave() = false\n" +
                "    player(0).shouldLeave() = false\n" +
                "    player(0).shouldLeave() = false\n" +
                "    player(1).wantToStay() = true\n" +
                "    tick()");
    }

    @Test
    public void testSettings() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        settings().bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 5);

        settings().bool(ROUNDS_ENABLED, false);
        settings().integer(ROUNDS_TIME_FOR_WINNER, 10);

        settings().string(LEVELS_MAP, "☺");

        settings().string(LEVELS_MAP,
                "☺☺☺\n" +
                "☺ ☺\n" +
                "☺☺☺\n");

        tick();

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testSettings()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,3])\n" +
                "            dice(2, 3)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "    settings().bool(ROUNDS_ENABLED, false)\n" +
                "    settings().integer(ROUNDS_TIME_FOR_WINNER, 10)\n" +
                "    settings().string(LEVELS_MAP, ☺)\n" +
                "    settings().string(\n" +
                "        LEVELS_MAP,\n" +
                "        ☺☺☺\n" +
                "        ☺ ☺\n" +
                "        ☺☺☺)\n" +
                "    tick()");
    }

    @Test
    public void testFieldDice() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when
        dice(1, 1);
        field().newGame(player(0));

        dice(1, 2);
        field().newGame(player());

        dice(1, 3);
        field().newGame(player(1));

        dice(2, 1,
            2, 2);
        field().clearScore();

        tick();

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testFieldDice()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,3])\n" +
                "            dice(2, 3)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    dice(1, 1)\n" +
                "    field().newGame(player(0))\n" +
                "    dice(1, 2)\n" +
                "    field().newGame(player(0))\n" +
                "    dice(1, 3)\n" +
                "    field().newGame(player(1))\n" +
                "    dice(2, 1, 2, 2)\n" +
                "    field().clearScore()\n" +
                "    tick()");
    }

    @Test
    public void testEvents() {
        // when
        givenFl("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        // when

        events().verifyNoEvents();
        events().verifyNoEvents(0, 1);
        events().verifyNoEvents(1);
        events().verifyNoEvents(0);
        events().verifyAllEvents("");
        events().verifyAllEvents("", 0, 1);
        events().verifyAllEvents("", 0);
        events().verifyAllEvents("", 1);
        tick();

        // then
        assertMessages("\n" +
                "before()\n" +
                "    settings().bool(ROUNDS_ENABLED, true)\n" +
                "    settings().integer(ROUNDS_TIME_BEFORE_START, 5)\n" +
                "\n" +
                "testEvents()\n" +
                "    givenFl(\n" +
                "        ☼☼☼☼☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼ ☺ ☼\n" +
                "        ☼   ☼\n" +
                "        ☼☼☼☼☼)\n" +
                "        givenPlayer([2,3])\n" +
                "            dice(2, 3)\n" +
                "        givenPlayer([2,2])\n" +
                "            dice(2, 2)\n" +
                "    events().verifyNoEvents([])\n" +
                "    events().verifyNoEvents([0, 1])\n" +
                "    events().verifyNoEvents([1])\n" +
                "    events().verifyNoEvents([0])\n" +
                "    events().verifyAllEvents(\n" +
                "        listener(0) => []\n" +
                "        listener(1) => [],\n" +
                "        [])\n" +
                "    events().verifyAllEvents(\n" +
                "        listener(0) => []\n" +
                "        listener(1) => [],\n" +
                "        [0, 1])\n" +
                "    events().verifyAllEvents(listener(0) => [], [0])\n" +
                "    events().verifyAllEvents(listener(1) => [], [1])\n" +
                "    tick()");
    }
}
