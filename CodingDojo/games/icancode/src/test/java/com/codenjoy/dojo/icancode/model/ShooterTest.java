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
import com.codenjoy.dojo.icancode.model.perks.DeathRayPerk;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class ShooterTest extends AbstractGameTest {

    @Test
    public void fireWhenLaserMachineIsOwner() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        new Shooter(game).fire(Direction.UP,
                new PointImpl(2, 0),
                mock(LaserMachine.class));

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--↑---" +
                "------");
    }

    @Test
    public void LaserMachineShouldNotShootThroughTheBarrier() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║..B.│" +
                "║....│" +
                "║.B..│" +
                "└────┘");

        new Shooter(game).fire(Direction.UP,
                new PointImpl(2, 0),
                mock(LaserMachine.class));

        new Shooter(game).fire(Direction.UP,
                new PointImpl(3, 0),
                mock(LaserMachine.class));

        //first laser shouldn't go
        //the second one must stuck in box
        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B↑--" +
                "------");
        game.tick();

        assertE("------" +
                "--☺---" +
                "---B--" +
                "---↑--" +
                "--B---" +
                "------");
        game.tick();

        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B---" +
                "------");

        game.tick();
        assertE("------" +
                "--☺---" +
                "---B--" +
                "------" +
                "--B---" +
                "------");
    }

    @Test
    public void fireRegularLaserWhenHeroIsOwner() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertEquals(0, game.getLevel().getItems(Laser.class).size());

        new Shooter(game).fire(Direction.DOWN,
                hero.getPosition(),
                hero.getItem());

        assertEquals(1, game.getLevel().getItems(Laser.class).size());
        assertTrue(hero.getItem().getCell().items().stream()
                .anyMatch(item -> item instanceof Laser));

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void fireDeathRayLasersWhenHeroIsOwner() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10)
                .deathRayRange(3);

        givenFl("╔══════┐" +
                "║.S....│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        game.move(new DeathRayPerk(Elements.DEATH_RAY_PERK), 2, 5);

        assertL("╔══════┐" +
                "║.S....│" +
                "║.r....│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        hero.down();
        game.tick();

        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--------" +
                "--------" +
                "--------" +
                "--------" +
                "--------");
        assertTrue(hero.hasDeathRayPerk());

        assertEquals(0, game.getLevel().getItems(Laser.class).size());

        new Shooter(game).fire(Direction.DOWN, hero.getItem().getCell(), hero.getItem());

        assertEquals(3, game.getLevel().getItems(Laser.class).size());

        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--↓-----" +
                "--↓-----" +
                "--↓-----" +
                "--------" +
                "--------");

        assertTrue(game.getLevel().getItems(Laser.class).stream()
                .map(item -> (Laser) item)
                .allMatch(laser -> laser.isDeathRay() && laser.getTicks() == 0));

        game.tick();

        assertE("--------" +
                "--------" +
                "--☺-----" +
                "--↓-----" +
                "--↓-----" +
                "--↓-----" +
                "--------" +
                "--------");

        game.tick();

        assertEquals(0, game.getLevel().getItems(Laser.class).size());
    }
}