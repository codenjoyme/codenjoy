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


import com.codenjoy.dojo.sample.model.check.AbstractGameCheckTest;
import org.junit.Test;

public class MultiplayerTest extends AbstractGameCheckTest {

    @Test
    public void severalHeroesCanAppearOnTheMap() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼☺ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼☻ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼☻ ☻$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 2);
    }

    @Test
    public void eachHeroCanBeControlledIndependentlyInOneTickOfTheGame() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺ ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(0).act();
        hero(0).down();
        hero(1).down();
        hero(2).right();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼x   ☼\n" +
                "☼☺ ☻ ☼\n" +
                "☼  ☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void heroesCanBeRemovedFromTheGame() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺ ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        game(1).close();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼☺   ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void heroesCanBeRestartedInTheGame() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺ ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        dice(4, 1);
        game(1).newGame();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼☺   ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼   ☻☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void anyOfTheHeroesCanExplodeOnABomb() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺ ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        hero(0).down();
        hero(0).act();
        hero(1).left();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼x☻  ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        // when
        hero(1).left();
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼Y   ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        events().verifyAllEvents(
                "listener(1) => [LOSE]\n");

        assertEquals(true, game(1).isGameOver());

        dice(4, 1);
        game(1).newGame();

        tick();

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼☺   ☼\n" +
                "☼ ☻  ☼\n" +
                "☼   ☻☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }

    @Test
    public void anyOfTheHeroesCanPickUpGold() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺$☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(1).right();

        dice(1, 2);

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼☺  ☻☼\n" +
                "☼    ☼\n" +
                "☼$☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        events().verifyAllEvents(
                "listener(1) => [WIN]\n");
    }

    @Test
    public void heroesCannotWalkThroughOneAnother() {
        // given
        givenFl("☼☼☼☼☼☼\n" +
                "☼☺ ☺ ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        hero(0).right();
        hero(1).left();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼ ☺☻ ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);
    }
}