package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.model.Elements.BOX;
import static com.codenjoy.dojo.icancode.model.Elements.UNSTOPPABLE_LASER;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ICanCodeUnstoppableLaserTest extends AbstractGameTest {

    @Test
    public void perkAppearAfterZombieDie() {
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 4;
        givenZombie().thenReturn(UP);
        SettingsWrapper.setup(new SettingsImpl())
                .perkDropRatio(100);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║.Z..│" +
                "└────┘");

        generateMale();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "--♂---" +
                "------");

        hero.down();
        hero.fire();
        game.tick();

        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "--♂---" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "--✝---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "--l---" +
                "------" +
                "------");
    }

    @Test
    public void heroTakesPerk() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new UnstoppableLaser(UNSTOPPABLE_LASER), 2, 2);

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "--l---" +
                "------" +
                "------");

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "------" +
                "--☺---" +
                "------" +
                "------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaser));
    }

    @Test
    public void unstoppableLaserTest() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10);

        givenFl("╔══════┐" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");
        game.move(new UnstoppableLaser(UNSTOPPABLE_LASER), 3, 5);
        game.move(new Box(BOX), 3, 3);

        assertE("--------" +
                "---☺----" +
                "---l----" +
                "--------" +
                "---B----" +
                "--------" +
                "--------" +
                "--------");

        hero.down();
        game.tick();

        assertE("--------" +
                "--------" +
                "---☺----" +
                "--------" +
                "---B----" +
                "--------" +
                "--------" +
                "--------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaser));

        hero.down();
        hero.fire();
        game.tick();

        assertE("--------" +
                "--------" +
                "---☺----" +
                "---↓----" +
                "---B----" +
                "--------" +
                "--------" +
                "--------");

        game.tick();

        assertE("--------" +
                "--------" +
                "---☺----" +
                "--------" +
                "---B----" +
                "--------" +
                "--------" +
                "--------");

        game.tick();

        assertE("--------" +
                "--------" +
                "---☺----" +
                "--------" +
                "---B----" +
                "---↓----" +
                "--------" +
                "--------");
    }

    @Test
    public void perkAvailabilityTest() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(3)
                .perkActivity(10);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new UnstoppableLaser(UNSTOPPABLE_LASER), 2, 2);

        assertE("------" +
                "--☺---" +
                "------" +
                "--l---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "--l---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "--l---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void perkActivityTest() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(3);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new UnstoppableLaser(UNSTOPPABLE_LASER), 2, 2);

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "--l---" +
                "------" +
                "------");

        hero.down();
        game.tick();

        assertE("------" +
                "------" +
                "------" +
                "--☺---" +
                "------" +
                "------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaser));

        game.tick();

        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaser));

        game.tick();

        assertFalse(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaser));
    }
}
