package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.model.Elements.UNLIMITED_FIRE_PERK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ICanCodeUnlimitedFirePerkTest extends AbstractGameTest {

    @Test
    public void heroHasUnlimitedFirePerk() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new UnlimitedFirePerk(UNLIMITED_FIRE_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.f..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");
        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertTrue(hero.hasUnlimitedFirePerk());
    }

    @Test
    public void testFireWithoutUnlimitedFirePerk() {
        settings.gunRecharge(3);

        givenFl("╔═════┐" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "--☺----" +
                "-------" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "--↓----" +
                "-------" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "--↓----" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());
    }

    @Test
    public void testFireWithUnlimitedFirePerk() {
        settings.perkActivity(2)
                .gunRecharge(3);

        givenFl("╔═════┐" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");
        game.move(new UnlimitedFirePerk(UNLIMITED_FIRE_PERK), 2, 4);

        assertL("╔═════┐" +
                "║.S...│" +
                "║.f...│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
        assertL("╔═════┐" +
                "║.S...│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");
        assertTrue(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------");
        assertTrue(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "--☺----" +
                "--↓----" +
                "--↓----" +
                "-------" +
                "-------");
        assertFalse(hero.hasUnlimitedFirePerk());

        hero.fire();
        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "--↓----" +
                "--↓----" +
                "-------");
    }
}
