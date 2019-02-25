package com.codenjoy.dojo.sampletext.model;

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
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SampleTest {

    private SampleText game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private LevelImpl level;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenQA(String... questionAnswers) {
        level = new LevelImpl(questionAnswers);
        game = new SampleText(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        hero = player.hero;
        hero.init(game);
    }

    private void thenHistory(String expected) {
        assertEquals(expected, player.getHistory().toString().replace('\"', '\''));
    }

    @Test
    public void shouldNoAnswersAtStart() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        thenHistory("[]");
    }

    @Test
    public void shouldNoAnswersAtStartAfterTick() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        game.tick();

        thenHistory("[]");
    }

    @Test
    public void should_invalid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        thenHistory("[{'answer':'wrong-answer','question':'question1','valid':false}]");
    }

    @Test
    public void should_invalid_invalid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer1");
        game.tick();

        hero.message("wrong-answer2");
        game.tick();

        thenHistory("[{'answer':'wrong-answer1','question':'question1','valid':false}, " +
                "{'answer':'wrong-answer2','question':'question1','valid':false}]");
    }

    @Test
    public void should_invalid_valid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        thenHistory("[{'answer':'wrong-answer','question':'question1','valid':false}, " +
                "{'answer':'answer1','question':'question1','valid':true}]");
    }

    @Test
    public void should_invalid_valid_tick() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        game.tick();

        thenHistory("[{'answer':'wrong-answer','question':'question1','valid':false}, " +
                "{'answer':'answer1','question':'question1','valid':true}]");
    }

    @Test
    public void should_invalid_valid_valid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("wrong-answer");
        game.tick();

        hero.message("answer1");
        game.tick();

        hero.message("answer2");
        game.tick();

        thenHistory("[{'answer':'wrong-answer','question':'question1','valid':false}, " +
                "{'answer':'answer1','question':'question1','valid':true}, " +
                "{'answer':'answer2','question':'question2','valid':true}]");
    }

    @Test
    public void should_invalid_valid_tick_valid_tick() {
        givenQA("question1=answer1",
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

        thenHistory("[{'answer':'wrong-answer','question':'question1','valid':false}, " +
                "{'answer':'answer1','question':'question1','valid':true}, " +
                "{'answer':'answer2','question':'question2','valid':true}]");
    }

    @Test
    public void shouldAfterLastQuestion() {
        givenQA("question1=answer1",
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

        thenHistory("[{'answer':'answer1','question':'question1','valid':true}, " +
                "{'answer':'answer2','question':'question2','valid':true}, " +
                "{'answer':'answer3','question':'question3','valid':true}]");
    }
}
