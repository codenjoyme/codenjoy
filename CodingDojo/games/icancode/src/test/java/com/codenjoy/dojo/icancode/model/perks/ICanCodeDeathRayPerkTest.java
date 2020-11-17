package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Field;
import com.codenjoy.dojo.icancode.model.items.Box;
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;
import org.mockito.Mockito;

import static com.codenjoy.dojo.icancode.model.Elements.*;
import static com.codenjoy.dojo.services.Direction.STOP;
import static org.junit.Assert.assertTrue;

public class ICanCodeDeathRayPerkTest extends AbstractGameTest {

    @Test
    public void testDeathRayPerk() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10)
                .deathRayRange(10);

        givenFl("╔══════════┐" +
                "║..S.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        game.move(new DeathRayPerk(DEATH_RAY_PERK), 3, 9);
        game.move(new Box(BOX), 3, 5);

        assertL("╔══════════┐" +
                "║..S.......│" +
                "║..r.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        assertE("------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");

        hero.down();
        game.tick();

        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof DeathRayPerk));

        hero.down();
        hero.fire();
        game.tick();

        assertE("------------" +
                "------------" +
                "---☺--------" +
                "---↓--------" +
                "---↓--------" +
                "---↓--------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof DeathRayPerk));

        game.tick();

        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof DeathRayPerk));
    }

    @Test
    public void deathRayAndUnstoppableLaser() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10)
                .deathRayRange(10);

        givenFl("╔══════════┐" +
                "║..S.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");
        game.move(new DeathRayPerk(DEATH_RAY_PERK), 3, 9);
        game.move(new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK), 3, 8);

        game.move(new Box(BOX), 3, 4);
        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(Mockito.mock(Field.class));
        game.move(zombie, 3, 2);

        assertL("╔══════════┐" +
                "║..S.......│" +
                "║..r.......│" +
                "║..l.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "└──────────┘");

        hero.down();
        game.tick();

        assertE("------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "---♂--------" +
                "------------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof DeathRayPerk));

        hero.down();
        game.tick();

        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "---♂--------" +
                "------------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));

        hero.down();
        hero.fire();
        game.tick();

        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "---↓--------" +
                "---↓--------" +
                "---↓--------" +
                "---B--------" +
                "---↓--------" +
                "---✝--------" +
                "---↓--------" +
                "------------");
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof UnstoppableLaserPerk));
        assertTrue(hero.getPerks().stream()
                .anyMatch(perk -> perk instanceof DeathRayPerk));

        game.tick();

        assertE("------------" +
                "------------" +
                "------------" +
                "---☺--------" +
                "------------" +
                "------------" +
                "------------" +
                "---B--------" +
                "------------" +
                "------------" +
                "------------" +
                "------------");
    }

    @Test
    public void heroHasDeathRayPerk() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(10)
                .perkActivity(10);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new DeathRayPerk(DEATH_RAY_PERK), 2, 3);

        assertL("╔════┐" +
                "║.S..│" +
                "║.r..│" +
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
        assertTrue(hero.hasDeathRayPerk());
    }
}
