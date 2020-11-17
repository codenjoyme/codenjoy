package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.Level;
import com.codenjoy.dojo.icancode.model.items.Floor;
import com.codenjoy.dojo.icancode.model.items.Gold;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import org.junit.Test;

import java.util.Optional;
import java.util.Random;

import static com.codenjoy.dojo.icancode.model.Elements.*;
import static com.codenjoy.dojo.icancode.model.ICanCode.TRAINING;
import static com.codenjoy.dojo.services.Direction.UP;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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

    @Test
    public void doNotDropNextPerk() {
        // Given
        game = new ICanCode(mock(Level.class), dice, TRAINING);
        SettingsWrapper.setup(new SettingsImpl())
                .perkDropRatio(0);
        when(dice.next(anyInt())).thenReturn(100);

        // When
        Optional<AbstractPerk> nextPerk = game.dropNextPerk();

        // Then
        assertFalse(nextPerk.isPresent());
    }

    @Test
    public void doDropNextPerk() {
        // Given
        game = new ICanCode(mock(Level.class), dice, TRAINING);
        SettingsWrapper.setup(new SettingsImpl())
                .perkDropRatio(100);
        when(dice.next(anyInt()))
                .thenReturn(0)
                .thenReturn(new Random().nextInt(Elements.getPerks().size()));

        // When
        Optional<AbstractPerk> nextPerk = game.dropNextPerk();

        // Then
        assertTrue(nextPerk.isPresent());
    }

    @Test
    public void pickPerk() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);

        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        game.move(new UnstoppableLaserPerk(Elements.UNSTOPPABLE_LASER_PERK), 2, 1);

        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║.l..│" +
                "└────┘");

        assertTrue(game.pickPerk(2, 1).isPresent());
        assertFalse(game.pickPerk(2, 4).isPresent());
    }

    @Test
    public void perksOnBoard() {
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);

        givenFl("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║.lrlrlr..│" +
                "└─────────┘");

        assertEquals(6, game.perks().size());
    }

    @Test
    public void goldStateWithUnstoppableLaserPerk() {
        // given
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);
        Gold gold = new Gold(Elements.GOLD);

        // when
        Elements element = gold.state(player, new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK));

        // then
        assertEquals(UNSTOPPABLE_LASER_PERK, element);
    }

    @Test
    public void goldStateWithDeathRayPerk() {
        // given
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);
        Gold gold = new Gold(Elements.GOLD);

        // when
        Elements element = gold.state(player, new DeathRayPerk(DEATH_RAY_PERK));

        // then
        assertEquals(DEATH_RAY_PERK, element);
    }

    @Test
    public void floorStateWithUnstoppableLaserPerk() {
        // given
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);
        Floor floor = new Floor(FLOOR);

        // when
        Elements element = floor.state(player, new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK));

        // then
        assertEquals(UNSTOPPABLE_LASER_PERK, element);
    }

    @Test
    public void floorStateWithDeathRayPerk() {
        // given
        SettingsWrapper.setup(new SettingsImpl())
                .perkAvailability(5)
                .perkActivity(10)
                .perkDropRatio(100);
        Floor floor = new Floor(FLOOR);

        // when
        Elements element = floor.state(player, new DeathRayPerk(DEATH_RAY_PERK));

        // then
        assertEquals(DEATH_RAY_PERK, element);
    }
}
