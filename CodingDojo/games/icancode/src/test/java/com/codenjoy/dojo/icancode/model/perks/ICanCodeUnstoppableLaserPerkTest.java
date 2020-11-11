package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Field;
import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.icancode.model.Elements.BOX;
import static com.codenjoy.dojo.icancode.model.Elements.UNSTOPPABLE_LASER_PERK;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertTrue;

public class ICanCodeUnstoppableLaserPerkTest extends AbstractGameTest {

    @Test
    public void heroFireThroughBoxWithUnstoppableLaser() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10);

        givenFl("╔═════┐" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 4);
        game.move(new Box(BOX), 3, 2);

        assertL("╔═════┐" +
                "║..S..│" +
                "║..l..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "-------" +
                "-------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        hero.down();
        hero.fire();
        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "---↓---" +
                "---B---" +
                "-------" +
                "-------");

        game.tick();


        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "-------" +
                "-------");

        game.tick();

        assertE("-------" +
                "-------" +
                "---☺---" +
                "-------" +
                "---B---" +
                "---↓---" +
                "-------");
    }

    @Test
    public void heroKillsTwoZombiesWithUnstoppableLaser() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10);

        givenFl("╔═══════┐" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 6);
        givenZombie().thenReturn(STOP);
        Zombie zombie1 = new Zombie(true);
        zombie1.setField(Mockito.mock(Field.class));
        game.move(zombie1, 2, 4);
        Zombie zombie2 = new Zombie(true);
        zombie2.setField(Mockito.mock(Field.class));
        game.move(zombie2, 2, 2);

        assertL("╔═══════┐" +
                "║.S.....│" +
                "║.l.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");
        assertE("---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--♂------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------");

        hero.down();
        game.tick();

        assertL("╔═══════┐" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");
        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "--♂------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        hero.down();
        hero.fire();
        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "--↓------" +
                "--♂------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "--✝------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--↓------" +
                "--♂------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--↓------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
    }
}
