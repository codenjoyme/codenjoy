package com.codenjoy.dojo.icancode.model;

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

import com.codenjoy.dojo.icancode.model.items.Laser;
import com.codenjoy.dojo.icancode.model.items.LaserMachine;
import com.codenjoy.dojo.icancode.model.items.perks.DeathRayPerk;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ShooterTest extends AbstractGameTest {

    @Test
    public void shouldFire_whenLaserMachineIsOwner() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        new Shooter(game).fire(Direction.UP,
                new PointImpl(2, 0),
                mock(LaserMachine.class));

        // then
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--↑---" +
                "------");
    }

    @Test
    public void shouldNotLaserMachineShoot_throughTheBarrier() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║..B.│" +
                "║....│" +
                "║.B..│" +
                "└────┘");

        // when
        new Shooter(game).fire(Direction.UP,
                new PointImpl(2, 0),
                mock(LaserMachine.class));

        new Shooter(game).fire(Direction.UP,
                new PointImpl(3, 0),
                mock(LaserMachine.class));

        // then
        // first laser shouldn't go
        // the second one must stuck in box
        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B↑--" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "---B--" +
                "---↑--" +
                "--B---" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B---" +
                "------");
        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B---" +
                "------");
    }

    @Test
    public void shoudlFireRegularLaser_whenHeroIsOwner() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertEquals(0, game.getLevel().items(Laser.class).size());

        new Shooter(game).fire(Direction.DOWN,
                hero.getPosition(),
                hero.getItem());

        assertEquals(1, game.getLevel().items(Laser.class).size());
        // TODO refactoring needed
        assertTrue(hero.getItem().getCell().items().stream()
                .anyMatch(item -> item instanceof Laser));

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldFireDeathRayLasers_whenHeroIsOwner() {
        // given
        settings.integer(PERK_AVAILABILITY, 10)
                .integer(PERK_ACTIVITY, 10)
                .integer(DEATH_RAY_PERK_RANGE, 3);

        givenFl("╔══════┐" +
                "║.S....│" +
                "║.r....│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        // when
        hero.down();
        game.tick();

        // then
        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--------" +
                "--------" +
                "--------" +
                "--------" +
                "--------");

        has(DeathRayPerk.class);

        assertEquals(0, game.getLevel().items(Laser.class).size());

        // when
        new Shooter(game).fire(Direction.DOWN, hero.getItem().getCell(), hero.getItem());

        // then
        assertEquals(3, game.getLevel().items(Laser.class).size());

        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--↓-----" +
                "--↓-----" +
                "--↓-----" +
                "--------" +
                "--------");

        // TODO refactoring needed
        assertTrue(game.getLevel().items(Laser.class).stream()
                .map(item -> (Laser) item)
                .allMatch(laser -> laser.deathRay() && laser.getTicks() == 0));

        // when
        game.tick();

        // then
        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--↓-----" +
                "--↓-----" +
                "--↓-----" +
                "--------" +
                "--------");

        // when
        game.tick();

        // then
        assertEquals(0, game.getLevel().items(Laser.class).size());
    }
}