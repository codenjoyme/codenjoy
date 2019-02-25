package com.codenjoy.dojo.kata.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software:you can redistribute it and/or modify
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
import com.codenjoy.dojo.kata.model.levels.LevelsPool;
import com.codenjoy.dojo.kata.model.levels.LevelsPoolImpl;
import com.codenjoy.dojo.kata.model.levels.QuestionAnswerLevelImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.utils.JsonUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KataTest {

    private Kata game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private LevelsPool pool;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i :ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenQA(String... questionAnswers) {
        givenGame(qa(questionAnswers));
    }

    private Level qa(String... questionAnswers) {
        return new QuestionAnswerLevelImpl(questionAnswers){
            @Override
            public int complexity() {
                return 0;
            }

            @Override
            public String description() {
                return "description";
            }
        };
    }

    private void givenGame(Level... levels) {
        game = new Kata(dice);
        listener = mock(EventListener.class);
        pool = new LevelsPoolImpl(Arrays.asList(levels));
        player = new Player(listener, pool);
        game.newGame(player);
        hero = player.hero;
        hero.init(game);
    }

    private void thenHistory(String expected) {
        assertEquals(expected, JsonUtils.prettyPrint(player.getHistory()));
    }

    private void thenQuestions(String expected) {
        assertEquals(expected, JsonUtils.prettyPrint(player.getQuestions()));
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'wrong-answer',\n" +
                "        'question':'question1',\n" +
                "        'valid':false\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'wrong-answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'wrong-answer2',\n" +
                "        'question':'question1',\n" +
                "        'valid':false\n" +
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
            "    'questionAnswers':[\n" +
            "      {\n" +
            "        'answer':'wrong-answer',\n" +
            "        'question':'question1',\n" +
            "        'valid':false\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    'questionAnswers':[\n" +
            "      {\n" +
            "        'answer':'answer1',\n" +
            "        'question':'question1',\n" +
            "        'valid':true\n" +
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
            "    'questionAnswers':[\n" +
            "      {\n" +
            "        'answer':'wrong-answer',\n" +
            "        'question':'question1',\n" +
            "        'valid':false\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  {\n" +
            "    'questionAnswers':[\n" +
            "      {\n" +
            "        'answer':'answer1',\n" +
            "        'question':'question1',\n" +
            "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'wrong-answer',\n" +
                "        'question':'question1',\n" +
                "        'valid':false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'wrong-answer',\n" +
                "        'question':'question1',\n" +
                "        'valid':false\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer3',\n" +
                "        'question':'question3',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[]\n" +
                "  }\n" +
                "]");

        thenQuestions("[]");
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[]");
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[]");
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1, 2',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
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
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1, 2',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'1, 2',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'3, 4',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "]");

        thenQuestions("[]");
    }

    @Test
    public void shouldCleanHistoryWhenLastLevel() {
        givenQA("question1=answer1");

        // when
        hero.message("['answer1']");
        game.tick();

        thenHistory(
                "[\n" +
                    "  {\n" +
                    "    'questionAnswers':[\n" +
                    "      {\n" +
                    "        'answer':'answer1',\n" +
                    "        'question':'question1',\n" +
                    "        'valid':true\n" +
                    "      }\n" +
                    "    ]\n" +
                    "  }\n" +
                    "]");

        thenQuestions("[]");

        // when
        hero.message("['blabla']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[]\n" +
                "  }\n" +
                "]");

        thenQuestions("[]");
    }

    @Test
    public void should_unansweredQuestion() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        hero.message("['answer1']");
        game.tick();

        hero.message("['answer1','answer2']");
        game.tick();

        // when
        hero.message("['answer1','answer2']");
        game.tick();

        thenHistory(
                "[\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      }\n" +
                "    ]\n" +
                "  },\n" +
                "  {\n" +
                "    'questionAnswers':[\n" +
                "      {\n" +
                "        'answer':'answer1',\n" +
                "        'question':'question1',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'answer2',\n" +
                "        'question':'question2',\n" +
                "        'valid':true\n" +
                "      },\n" +
                "      {\n" +
                "        'answer':'???',\n" +
                "        'question':'question3',\n" +
                "        'valid':false\n" +
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
    public void shouldPlayerHasLevels() {
        givenGame(qa("question1=answer1"),
                qa("question2=answer2",
                    "question3=answer3"),
                qa("question4=answer4",
                    "question5=answer5"));

        // then
        assertStillOnLevel(0);

        // when
        hero.message("['answer1']");
        game.tick();

        // then
        assertGoToNextLevel(1);

        // when
        hero.message("['answer2']");
        game.tick();

        // then
        assertStillOnLevel(1);

        hero.message("['answer2','answer3']");
        game.tick();

        // then
        assertGoToNextLevel(2);

        hero.message("['answer4']");
        game.tick();

        // then
        assertStillOnLevel(2);

        hero.message("['answer4','answer5']");
        game.tick();

        // then
        assertGoToNextLevel(3);
    }

    private void assertStillOnLevel(int expected) {
        assertEquals(expected, player.getLevel());
        assertEquals(false, pool.isWaitNext());
    }

    private void assertGoToNextLevel(int level) {
        // then
        assertWaitAfter(level - 1);

        // when
        hero.message(Elements.START_NEXT_LEVEL);
        game.tick();

        // then
        assertStillOnLevel(level);
    }

    @Test
    public void shouldPlayerAskNextLevelOnlyIfNowWeAreWaiting() {
        givenGame(qa("question1=answer1"),
                qa("question2=answer2",
                        "question3=answer3"),
                qa("question4=answer4",
                        "question5=answer5"));

        // then
        int sameLevel = 0;
        assertStillOnLevel(sameLevel);

        // when
        // try to NextLevel
        hero.message(Elements.START_NEXT_LEVEL);
        game.tick();

        // then
        // unsuccessful
        assertStillOnLevel(sameLevel);

        // when
        hero.message("['answer1']");
        game.tick();
        
        // then
        assertGoToNextLevel(1);
    }

    @Test
    public void shouldPlayerSkipLevelOnlyIfNowWeArePlaying() {
        givenGame(qa("question1=answer1"),
                qa("question2=answer2",
                        "question3=answer3"),
                qa("question4=answer4",
                        "question5=answer5"));

        // then
        assertStillOnLevel(0);

        // when
        hero.message("['answer1']");
        game.tick();

        // then
        // WaitLevel
        int sameLevel = 0;
        assertWaitAfter(sameLevel);

        // when
        // try to skip
        hero.message(Elements.SKIP_THIS_LEVEL);
        game.tick();

        // then
        // unsuccessful
        assertWaitAfter(sameLevel);

        // when
        // start next
        hero.message(Elements.START_NEXT_LEVEL);
        game.tick();

        // then
        assertStillOnLevel(1);

        // when
        // try to skip
        hero.message(Elements.SKIP_THIS_LEVEL);
        game.tick();

        // then
        assertWaitAfter(1);
    }

    private void assertWaitAfter(int level) {
        assertEquals(level, player.getLevel());
        assertEquals(true, pool.isWaitNext());
    }

    @Test
    public void shouldPlayerSkipFirstTwoLevels_thenAnswer_case2() {
        givenGame(qa("question1=answer1"),
                qa("question2=answer2",
                        "question3=answer3"),
                qa("question4=answer4",
                        "question5=answer5"));

        // then
        assertStillOnLevel(0);

        // when
        hero.message(Elements.SKIP_THIS_LEVEL);
        game.tick();

        // then
        assertGoToNextLevel(1);

        // when
        hero.message(Elements.SKIP_THIS_LEVEL);
        game.tick();

        // then
        assertGoToNextLevel(2);

        // when
        hero.message("['answer4']");
        game.tick();

        // then
        assertStillOnLevel(2);

        hero.message("['answer4','answer5']");
        game.tick();

        // then
        assertGoToNextLevel(3);
    }

    @Test
    public void should_ignoreBadCommand_caseTwoParameters() {
        givenQA("question1=answer1",
                "question2=answer2",
                "question3=answer3");

        // when
        hero.message("['StartNextLevel']");
        game.tick();

        // then
        assertStillOnLevel(0);
    }
}
