package com.codenjoy.dojo.kata.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.kata.model.levels.Level;
import com.codenjoy.dojo.kata.model.levels.LevelsPoolImpl;
import com.codenjoy.dojo.kata.model.levels.QuestionAnswerLevelImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KataTest {

    private Kata game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private Level level;

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
        level = new QuestionAnswerLevelImpl(questionAnswers){
            @Override
            public int complexity() {
                return 0;
            }
        };
        game = new Kata(dice);
        listener = mock(EventListener.class);
        LevelsPoolImpl pool = new LevelsPoolImpl(Arrays.asList(level));
        player = new Player(listener, pool);
        game.newGame(player);
        hero = player.hero;
        hero.init(game);
    }

    private void thenHistory(String expected) {
        assertEquals(expected, JsonUtils.prettyPrint(player.getHistory()).replace('\"', '\''));
    }

    private void thenQuestions(String expected) {
        assertEquals(expected, JsonUtils.prettyPrint(player.getNextQuestion()).replace('\"', '\''));
    }

    @Test
    public void shouldNoAnswersAtStart() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        thenHistory("[]");

        thenQuestions("[\n" +
                "  'question1'\n" +
                "]");
    }

    @Test
    public void shouldNoAnswersAtStartAfterTick() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        game.tick();

        thenHistory("[]");

        thenQuestions("[\n" +
                "  'question1'\n" +
                "]");
    }

    @Test
    public void should_invalid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'wrong-answer',\n" +
                "        'valid': false\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1'\n" +
                "]");
    }

    @Test
    public void should_invalid_invalid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer1']");
        game.tick();

        hero.message("['wrong-answer2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'wrong-answer1',\n" +
                "        'valid': false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'wrong-answer2',\n" +
                "        'valid': false\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1'\n" +
                "]");
    }

    @Test
    public void should_invalid_valid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer']");
        game.tick();

        hero.message("['answer1']");
        game.tick();

        thenHistory(
            "[\n" +
            "  {\n" +
            "    'questionAnswers': [\n" +
            "      {\n" +
            "        'question': 'question1',\n" +
            "        'answer': 'wrong-answer',\n" +
            "        'valid': false\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    'questionAnswers': [\n" +
            "      {\n" +
            "        'question': 'question1',\n" +
            "        'answer': 'answer1',\n" +
            "        'valid': true\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2'\n" +
                "]");
    }

    @Test
    public void should_invalid_valid_tick() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer']");
        game.tick();

        hero.message("['answer1']");
        game.tick();

        game.tick();

        thenHistory(
            "[\n" +
            "  {\n" +
            "    'questionAnswers': [\n" +
            "      {\n" +
            "        'question': 'question1',\n" +
            "        'answer': 'wrong-answer',\n" +
            "        'valid': false\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    'questionAnswers': [\n" +
            "      {\n" +
            "        'question': 'question1',\n" +
            "        'answer': 'answer1',\n" +
            "        'valid': true\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2'\n" +
                "]");
    }

    @Test
    public void should_invalid_valid_valid() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer']");
        game.tick();

        hero.message("['answer1']");
        game.tick();

        hero.message("['answer1','answer2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'wrong-answer',\n" +
                "        'valid': false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': 'answer2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2',\n" +
                "  'question3'\n" +
                "]");
    }

    @Test
    public void should_invalid_valid_tick_valid_tick() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['wrong-answer']");
        game.tick();

        hero.message("['answer1']");
        game.tick();

        game.tick();

        hero.message("['answer1','answer2']");
        game.tick();

        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'wrong-answer',\n" +
                "        'valid': false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': 'answer2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2',\n" +
                "  'question3'\n" +
                "]");
    }

    @Test
    public void shouldAfterLastQuestion() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['answer1']");
        game.tick();

        hero.message("['answer1','answer2']");
        game.tick();

        hero.message("['answer1','answer2','answer3']");
        game.tick();

        hero.message("['answer1','answer2','answer3','answer4']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': 'answer2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': 'answer2',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question3',\n" +
                "        'answer': 'answer3',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'Congratulations!! Mo more questions!'\n" +
                "]");
    }

    @Test
    public void should_stringAnswers() {
        givenQA("question1=answer1",
                "question2=answer2");

        // when
        hero.message("['answer1']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2'\n" +
                "]");

        // when
        hero.message("['answer1', 'answer2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': 'answer1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': 'answer2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'Congratulations!! Mo more questions!'\n" +
                "]");
    }

    @Test
    public void should_integerAnswers() {
        givenQA("question1=1",
                "question2=2");

        // when
        hero.message("['1']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2'\n" +
                "]");

        // when
        hero.message("['1', '2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': '2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'Congratulations!! Mo more questions!'\n" +
                "]");
    }

    @Test
    public void should_integersAnswers() {
        givenQA("question1=1, 2",
                "question2=3, 4");

        // when
        hero.message("['1, 2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1, 2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'question1',\n" +
                "  'question2'\n" +
                "]");

        // when
        hero.message("['1, 2', '3, 4']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1, 2',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers': [\n" +
                "      {\n" +
                "        'question': 'question1',\n" +
                "        'answer': '1, 2',\n" +
                "        'valid': true\n" +
                "      },\n" +
                "      {\n" +
                "        'question': 'question2',\n" +
                "        'answer': '3, 4',\n" +
                "        'valid': true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[\n" +
                "  'Congratulations!! Mo more questions!'\n" +
                "]");
    }
}
