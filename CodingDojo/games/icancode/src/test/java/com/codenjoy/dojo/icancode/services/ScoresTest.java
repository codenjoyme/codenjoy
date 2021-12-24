package com.codenjoy.dojo.icancode.services;

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


import com.codenjoy.dojo.icancode.TestGameSettings;
import com.codenjoy.dojo.services.PlayerScores;
import com.codenjoy.dojo.services.event.ScoresImpl;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.model.ICanCode.CONTEST;
import static com.codenjoy.dojo.icancode.model.ICanCode.TRAINING;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public boolean single() {
        return TRAINING;
    }

    public boolean multiple() {
        return CONTEST;
    }

    public void loseSingle() {
        scores.event(new Event(single()));
    }

    public void winSingle() {
        scores.event(Event.WIN(0, single()));
    }

    public void winMultiple() {
        scores.event(Event.WIN(0, multiple()));
    }

    public void winSingle(int goldCount) {
        scores.event(Event.WIN(goldCount, single()));
    }

    public void killHeroSingle(int count) {
        scores.event(Event.KILL_HERO(count, single()));
    }

    public void killZombieSingle(int count) {
        scores.event(Event.KILL_ZOMBIE(count, single()));
    }

    public void killHeroMultiple(int count) {
        scores.event(Event.KILL_HERO(count, multiple()));
    }

    public void killZombieMultiple(int count) {
        scores.event(Event.KILL_ZOMBIE(count, multiple()));
    }

    @Before
    public void setup() {
        settings = new TestGameSettings();
    }

    private void givenScores(int score) {
        scores = new ScoresImpl<>(score, new Scores(settings));
    }

    @Test
    public void shouldCollectScores() {
        // given
        givenScores(140);

        // when
        winSingle();
        winSingle();
        winSingle();
        winSingle();

        loseSingle();

        // then
        assertEquals(140
                    + 4 * settings.integer(WIN_SCORE)
                    + settings.integer(LOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_ignoreOnMultiple() {
        // given
        givenScores(140);

        // when
        winMultiple();
        winMultiple();
        winMultiple();
        winMultiple();

        // then
        assertEquals(140, scores.getScore());
    }

    @Test
    public void shouldWithWithGold() {
        // given
        givenScores(140);

        // when
        winSingle(0);
        winSingle(1);
        winSingle(2);

        // then
        assertEquals(140
                    + 3 * settings.integer(WIN_SCORE)
                    + 3 * settings.integer(GOLD_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        // given
        givenScores(0);

        // when
        loseSingle();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        // given
        givenScores(0);

        winSingle();

        // when
        scores.clear();

        // then
        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldNotCountZombieKillInSingleMode() {
        // given
        givenScores(140);
        settings.bool(ENABLE_KILL_SCORE, true);

        // when
        killZombieSingle(1);
        killHeroSingle(1);

        // then
        assertEquals(140, scores.getScore());
    }

    @Test
    public void disabledKillsShouldNotCountKillZombiesAndHero() {
        // given
        givenScores(140);
        settings.bool(ENABLE_KILL_SCORE, false);

        // when
        killZombieMultiple(1);
        killHeroMultiple(1);

        // then
        assertEquals(140, scores.getScore());
    }

    @Test
    public void shouldCountKills() {
        // given
        givenScores(140);
        int zombie = 2;
        int heroes = 1;
        settings.bool(ENABLE_KILL_SCORE, true);

        // when
        killZombieMultiple(zombie);
        killHeroMultiple(heroes);

        // then
        assertEquals(140
                    + settings.integer(KILL_HERO_SCORE) * heroes
                    + settings.integer(KILL_ZOMBIE_SCORE) * zombie,
                scores.getScore());
    }
}