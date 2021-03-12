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


import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.services.PlayerScores;
import org.junit.Before;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;

public class ScoresTest {

    private PlayerScores scores;
    private GameSettings settings;

    public void loose() {
        scores.event(new Events());
    }

    public void win() {
        scores.event(Events.WIN(0));
    }

    public void winMultiple() {
        scores.event(Events.WIN(0, ICanCode.CONTEST));
    }

    public void win(int goldCount) {
        scores.event(Events.WIN(goldCount));
    }

    public void killHero(int count) {
        scores.event(Events.KILL_HERO(count, ICanCode.TRAINING));
    }

    public void killZombie(int count) {
        scores.event(Events.KILL_ZOMBIE(count, ICanCode.TRAINING));
    }

    public void killHeroMultiple(int count) {
        scores.event(Events.KILL_HERO(count, ICanCode.CONTEST));
    }

    public void killZombieMultiple(int count) {
        scores.event(Events.KILL_ZOMBIE(count, ICanCode.CONTEST));
    }

    @Before
    public void setup() {
        settings = new GameSettings();
        scores = new Scores(0, settings);
    }

    @Test
    public void shouldCollectScores() {
        scores = new Scores(140, settings);

        win();
        win();
        win();
        win();

        loose();

        assertEquals(140
                + 4 * settings.integer(WIN_SCORE)
                - settings.integer(LOOSE_PENALTY),
                scores.getScore());
    }

    @Test
    public void shouldCollectScores_ignoreOnMultiple() {
        scores = new Scores(140, settings);

        winMultiple();
        winMultiple();
        winMultiple();
        winMultiple();

        assertEquals(140, scores.getScore());
    }

    @Test
    public void shouldWithWithGold() {
        scores = new Scores(0, settings);

        win(0);  //+
        win(1);  //+
        win(2);  //+

        assertEquals(3 * settings.integer(WIN_SCORE)
                + 3 * settings.integer(GOLD_SCORE),
                scores.getScore());
    }

    @Test
    public void shouldStillZeroAfterDead() {
        loose();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldClearScore() {
        win();

        scores.clear();

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldNotCountZombieKillInSingleMode() {
        settings.bool(ENABLE_KILL_SCORE, true);

        killZombie(1);
        killHero(1);

        assertEquals(0, scores.getScore());
    }

    @Test
    public void disabledKillsShouldNotCountKillZombiesAndHero() {
        settings.bool(ENABLE_KILL_SCORE, false);

        killZombieMultiple(1);
        killHeroMultiple(1);

        assertEquals(0, scores.getScore());
    }

    @Test
    public void shouldCountKills() {
        int zombie = 2;
        int heroes = 1;

        settings.bool(ENABLE_KILL_SCORE, true);

        killZombieMultiple(zombie);
        killHeroMultiple(heroes);

        assertEquals(settings.integer(KILL_HERO_SCORE) * heroes
                + settings.integer(KILL_ZOMBIE_SCORE) * zombie,
                scores.getScore());
    }
}
