package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Field;
import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.icancode.model.Elements.BOX;
import static com.codenjoy.dojo.icancode.model.Elements.UNSTOPPABLE_LASER_PERK;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;

public class ICanCodeUnstoppableLaserPerkTest extends AbstractGameTest {

    @Test
    public void heroFireThroughBoxWithUnstoppableLaser() {
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
    public void heroFireThroughOtherHeroWithUnstoppableLaser() {
        givenFl("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║..X..│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "---☺---" +
                "-------" +
                "-------" +
                "-------");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 2);

        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║..l..│" +
                "║.....│" +
                "└─────┘");

        hero.down();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        hero.up();
        hero.fire();
        game.tick();

        assertE("-------" +
                "-------" +
                "---X---" +
                "---↑---" +
                "---☺---" +
                "-------" +
                "-------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("-------" +
                "-------" +
                "---&---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
        verify(listener).event(Events.KILL_HERO(1, true));

        game.tick();

        assertE("-------" +
                "---↑---" +
                "---X---" +
                "-------" +
                "---☺---" +
                "-------" +
                "-------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
    }

    @Test
    public void heroFireThroughZombieWithUnstoppableLaser() {
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
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 2, 4);

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
                "---------" +
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
                "---------" +
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
                "---------" +
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
                "---------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        game.tick();

        assertE("---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------" +
                "--↓------" +
                "---------" +
                "---------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
    }

    @Test
    public void heroKillsZombieAndOtherHeroWithUnstoppableLaser() {
        givenFl("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.X.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "└───────┘");

        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();
        hero.down();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------" +
                "---------");

        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 2, 1);

        assertL("╔═══════┐" +
                "║.......│" +
                "║.S.....│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.......│" +
                "║.l.....│" +
                "└───────┘");

        hero.down();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 2, 4);

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));


        hero.up();
        hero.fire();
        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "---------" +
                "--↑------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--♂------" +
                "--↑------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "---------" +
                "--✝------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
        verify(listener).event(Events.KILL_ZOMBIE(1, true));

        game.tick();

        assertE("---------" +
                "---------" +
                "--X------" +
                "--↑------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        game.tick();

        assertE("---------" +
                "---------" +
                "--&------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
        verify(listener).event(Events.KILL_HERO(1, true));

        game.tick();

        assertE("---------" +
                "--↑------" +
                "--X------" +
                "---------" +
                "---------" +
                "---------" +
                "---------" +
                "--☺------" +
                "---------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
    }

    @Test
    public void heroHasUnstoppableLaserPerk() {
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
        assertTrue(hero.hasUnstoppableLaserPerk());
    }
}
