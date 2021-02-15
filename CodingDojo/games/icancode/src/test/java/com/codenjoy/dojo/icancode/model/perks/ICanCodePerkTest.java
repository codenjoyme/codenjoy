package com.codenjoy.dojo.icancode.model.perks;

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

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.Elements;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.Level;
import com.codenjoy.dojo.icancode.model.items.Floor;
import com.codenjoy.dojo.icancode.model.items.Gold;
import com.codenjoy.dojo.icancode.model.items.ZombiePot;
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

public class ICanCodePerkTest extends AbstractGameTest {

    @Test
    public void perkAppear_afterZombieDie() {
        // given
        ZombiePot.TICKS_PER_NEW_ZOMBIE = 4;
        givenZombie().thenReturn(UP);

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

        // when
        hero.down();
        hero.fire();
        game.tick();

        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "--♂---" +
                "------");

        // then
        game.tick();

        assertE("------" +
                "--☺---" +
                "------" +
                "--✝---" +
                "------" +
                "------");

        // then
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
        // given
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

        // when
        hero.down();
        game.tick();

        // then
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

        has(UnstoppableLaserPerk.class);
    }

    @Test
    public void perkAvailabilityTest() {
        // given
        settings.perkAvailability(3);

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

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║.S..│" +
                "║.l..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
    }

    @Test
    public void perkActivityTest() {
        // given
        settings.perkActivity(3);

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

        // when
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

        has(UnstoppableLaserPerk.class);

        // then
        game.tick();

        has(UnstoppableLaserPerk.class);

        game.tick();

        has(UnstoppableLaserPerk.class);

        game.tick();

        hasNot(UnstoppableLaserPerk.class);
    }

    @Test
    public void doNotDropNextPerk() {
        // given
        game = new ICanCode(mock(Level.class), dice, TRAINING);
        settings.perkDropRatio(0);
        dice(100);

        // when
        Optional<AbstractPerk> nextPerk = game.dropNextPerk();

        // then
        assertFalse(nextPerk.isPresent());
    }

    @Test
    public void doDropNextPerk() {
        // given
        game = new ICanCode(mock(Level.class), dice, TRAINING);
        dice(0, 1);

        // when
        Optional<AbstractPerk> nextPerk = game.dropNextPerk();

        // then
        assertTrue(nextPerk.isPresent());
    }

    @Test
    public void pickPerk() {
        // given
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

        // when then
        assertTrue(game.pickPerk(2, 1).isPresent());
        assertFalse(game.pickPerk(2, 4).isPresent());
    }

    @Test
    public void perksOnBoard() {
        // given
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

        // when then
        assertEquals(6, game.perks().size());
    }

    @Test
    public void goldState_withUnstoppableLaserPerk() {
        // given
        Gold gold = new Gold(Elements.GOLD);

        // when
        Elements element = gold.state(player, new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK));

        // then
        assertEquals(UNSTOPPABLE_LASER_PERK, element);
    }

    @Test
    public void goldState_withDeathRayPerk() {
        // given
        Gold gold = new Gold(Elements.GOLD);

        // when
        Elements element = gold.state(player, new DeathRayPerk(DEATH_RAY_PERK));

        // then
        assertEquals(DEATH_RAY_PERK, element);
    }

    @Test
    public void goldState_withUnlimitedFirePerk() {
        // given
        Gold gold = new Gold(Elements.GOLD);

        // when
        Elements element = gold.state(player, new UnlimitedFirePerk(UNLIMITED_FIRE_PERK));

        // then
        assertEquals(UNLIMITED_FIRE_PERK, element);
    }

    @Test
    public void floorState_withUnstoppableLaserPerk() {
        // given
        Floor floor = new Floor(FLOOR);

        // when
        Elements element = floor.state(player, new UnstoppableLaserPerk(UNSTOPPABLE_LASER_PERK));

        // then
        assertEquals(UNSTOPPABLE_LASER_PERK, element);
    }

    @Test
    public void floorState_withDeathRayPerk() {
        // given
        Floor floor = new Floor(FLOOR);

        // when
        Elements element = floor.state(player, new DeathRayPerk(DEATH_RAY_PERK));

        // then
        assertEquals(DEATH_RAY_PERK, element);
    }

    @Test
    public void floorState_withUnlimitedFirePerk() {
        // given
        Floor floor = new Floor(FLOOR);

        // when
        Elements element = floor.state(player, new UnlimitedFirePerk(UNLIMITED_FIRE_PERK));

        // then
        assertEquals(UNLIMITED_FIRE_PERK, element);
    }
}
