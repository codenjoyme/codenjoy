package com.codenjoy.dojo.icancode.model;

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

        new Shooter(game).fire(Direction.UP, new PointImpl(2, 0), mock(LaserMachine.class));

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--↑---" +
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

        new Shooter(game).fire(Direction.DOWN, hero.getPosition(), hero.getItem());

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