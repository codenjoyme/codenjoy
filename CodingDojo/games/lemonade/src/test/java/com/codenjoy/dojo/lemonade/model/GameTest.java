package com.codenjoy.dojo.lemonade.model;

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


import com.codenjoy.dojo.lemonade.TestGameSettings;
import com.codenjoy.dojo.lemonade.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static com.codenjoy.dojo.utils.JsonUtils.clean;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

// TODO а точно этот тест что-то полезное тестит?
public class GameTest {

    private Lemonade game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        settings = new TestGameSettings();
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void initGame() {
        game = new Lemonade(settings);
        listener = mock(EventListener.class);
        player = new Player(listener, 1, settings);
        game.newGame(player);
        hero = player.getHero();
        hero.init(game);
    }

    private void thenHistory(String expected) {
        assertEquals(expected, clean(player.getHistoryJson().toString()));
    }

    @Test
    public void shouldNoAnswersAtStart() {
        initGame();

        thenHistory("[]");
    }

    @Test
    public void shouldNoAnswersAtStartAfterTick() {
        initGame();

        // when
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid() {
        initGame();

        // when
        hero.message("wrong-answer");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_invalid() {
        initGame();

        // when
        hero.message("wrong-answer1");
        game.tick();

        hero.message("wrong-answer2");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_valid() {
        initGame();

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void clearScoresShouldResetSimulator() {
        initGame();

        assertEquals(2.0d, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);

        hero.message("go 10, 0, 10");

        assertNotEquals(2.0d, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);

        player.clearScore();

        assertEquals(2.0d, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);

        thenHistory("[]");
    }
}
