package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.model.Elements.UNSTOPPABLE_LASER_PERK;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ICanCodeAbstractPerkTest extends AbstractGameTest {

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

        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║.l..│" +
                "║.Z..│" +
                "└────┘");
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
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
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
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
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
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.tick();

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.tick();

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.tick();

        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
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
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
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
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertFalse(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
    }
}
