package com.codenjoy.dojo.clifford.services;

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


import com.codenjoy.dojo.services.PlayerScores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.clifford.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void heroDie() {
        scores.event(Events.HERO_DIE);
    }

    public void killRobber() {
        scores.event(Events.KILL_ROBBER);
    }

    public void suicide() {
        scores.event(Events.SUICIDE);
    }

    public void knifeClue() {
        scores.event(Events.GET_KNIFE_CLUE);
    }

    public void ringClue() {
        scores.event(Events.GET_RING_CLUE);
    }

    public void gloveClue() {
        scores.event(Events.GET_GLOVE_CLUE);
    }

    @Before
    public void setup() {
        settings = new GameSettings()
                .integer(SUICIDE_PENALTY, 13)
                .integer(HERO_DIE_PENALTY, 30)

                .integer(KILL_ROBBER_SCORE, 10)

                .integer(CLUE_SCORE_GLOVE, 2)
                .integer(CLUE_SCORE_GLOVE_INCREMENT, 1)

                .integer(CLUE_SCORE_KNIFE, 20)
                .integer(CLUE_SCORE_KNIFE_INCREMENT, 10)

                .integer(CLUE_SCORE_RING, 200)
                .integer(CLUE_SCORE_RING_INCREMENT, 100);
        
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        killRobber();
        killRobber();
        killRobber();

        knifeClue();
        ringClue();
        ringClue();
        ringClue();
        gloveClue();
        gloveClue();
        knifeClue();
        knifeClue();
        knifeClue();
        knifeClue();

        heroDie();

        assertEquals(140
                + 3 * settings.integer(KILL_ROBBER_SCORE)

                + 5 * settings.integer(CLUE_SCORE_KNIFE)
                + (1 + 2 + 3 + 4) * settings.integer(CLUE_SCORE_KNIFE_INCREMENT)

                + 3 * settings.integer(CLUE_SCORE_RING)
                + (1 + 2) * settings.integer(CLUE_SCORE_RING_INCREMENT)

                + 2 * settings.integer(CLUE_SCORE_GLOVE)
                + (1) * settings.integer(CLUE_SCORE_GLOVE_INCREMENT)

                - settings.integer(HERO_DIE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        heroDie();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        knifeClue();
        ringClue();
        ringClue();
        gloveClue();
        gloveClue();
        gloveClue();

        assertEquals(529, scores.getScore());
        assertEquals("Scores{score=529, ring=200, glove=3, knife=10}",
                scores.toString());

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
        assertEquals("Scores{score=0, ring=0, glove=0, knife=0}",
                scores.toString());
    }

    @Test
    public void shouldIncreaseForNextClue() {
        scores = new Scores(0, settings);

        knifeClue();
        knifeClue();
        knifeClue();
        knifeClue();

        assertEquals(4 * settings.integer(CLUE_SCORE_KNIFE)
                + (1 + 2 + 3) * settings.integer(CLUE_SCORE_KNIFE_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfGameOver() {
        // given
        scores = new Scores(0, settings);

        knifeClue();
        knifeClue();
        knifeClue();

        // when
        heroDie();

        // then
        Integer score = (Integer) scores.getScore();
        assertEquals(3 * settings.integer(CLUE_SCORE_KNIFE)
                    + (1 + 2) * settings.integer(CLUE_SCORE_KNIFE_INCREMENT)
                    - settings.integer(HERO_DIE_PENALTY),
                (int)score);

        // when
        knifeClue();
        knifeClue();

        // then
        assertEquals(score
                        + 2 * settings.integer(CLUE_SCORE_KNIFE)
                        + 1 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfClean() {
        // given
        scores = new Scores(0, settings);

        knifeClue();
        knifeClue();
        knifeClue();

        // when
        scores.clear();

        assertEquals(0, scores.getScore());

        // then
        knifeClue();
        knifeClue();

        assertEquals(2 * settings.integer(CLUE_SCORE_KNIFE)
                        + 1 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfSuicide() {
        // given
        scores = new Scores(0, settings);

        knifeClue();
        knifeClue();
        knifeClue();

        // when
        suicide();

        int saved = - settings.integer(SUICIDE_PENALTY)
                + 3 * settings.integer(CLUE_SCORE_KNIFE)
                + 3 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT);
        assertEquals(saved,
                scores.getScore());

        // then
        knifeClue();
        knifeClue();

        assertEquals(saved
                        + 2 * settings.integer(CLUE_SCORE_KNIFE)
                        + 1 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT),
                scores.getScore());
    }

    @Test
    public void shouldCleanIncreasedIfHeroDie() {
        // given
        scores = new Scores(0, settings);

        knifeClue();
        knifeClue();
        knifeClue();

        // when
        heroDie();

        int saved = - settings.integer(HERO_DIE_PENALTY)
                + 3 * settings.integer(CLUE_SCORE_KNIFE)
                + 3 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT);
        assertEquals(saved,
                scores.getScore());

        // then
        knifeClue();
        knifeClue();

        assertEquals(saved
                        + 2 * settings.integer(CLUE_SCORE_KNIFE)
                        + 1 * settings.integer(CLUE_SCORE_KNIFE_INCREMENT),
                scores.getScore());
    }


}
