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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SampleTest {

    private Lemonade game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private GameSettings gameSettings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void initGame(String... questionAnswers) {
        SettingsImpl settings = new SettingsImpl();
        settings.addEditBox("Limit days").type(Integer.class).def(30).update(0);
        gameSettings = new GameSettings(settings);
        game = new Lemonade(gameSettings);
        listener = mock(EventListener.class);
        player = new Player(listener, 1, gameSettings);
        game.newGame(player);
        hero = player.hero;
        hero.init(game);
    }

    private void thenHistory(String expected) {
        assertEquals(expected, player.getHistoryJson().toString().replace('\"', '\''));
    }

    @Test
    public void shouldNoAnswersAtStart() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        thenHistory("[]");
    }

    @Test
    public void shouldNoAnswersAtStartAfterTick() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_invalid() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer1");
        game.tick();

        hero.message("wrong-answer2");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_valid() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_valid_tick() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_valid_valid() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        hero.message("answer2");
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid_valid_tick_valid_tick() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        game.tick();

        hero.message("answer2");
        game.tick();

        game.tick();

        thenHistory("[]");
    }

    @Test
    public void shouldAfterLastQuestion() {
        initGame("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("answer1");
        game.tick();

        hero.message("answer2");
        game.tick();

        hero.message("answer3");
        game.tick();

        hero.message("answer4");
        game.tick();

        thenHistory("[]");
    }

    private final double expectedAssets = 2.0;

    @Test
    public void clearScoresShouldResetSimulator() {
        initGame();

        Assert.assertEquals(expectedAssets, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);
        hero.message("go 10, 0, 10");
        Assert.assertNotEquals(expectedAssets, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);
        player.clearScore();
        Assert.assertEquals(expectedAssets, player.getNextQuestion().getDouble("assets"), Double.MIN_VALUE);

        thenHistory("[]");
    }
}
