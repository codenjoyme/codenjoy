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
import com.codenjoy.dojo.icancode.model.items.perks.DeathRayPerk;
import com.codenjoy.dojo.icancode.model.items.perks.UnstoppableLaserPerk;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEATH_RAY_PERK_RANGE;
import static com.codenjoy.dojo.services.Direction.STOP;

public class DeathRayPerkTest extends AbstractGameTest {

    @Test
    public void shouldDrawPickAndFire_withDeathRayPerk() {
        // given
        settings.integer(DEATH_RAY_PERK_RANGE, 10);

        givenFl("╔══════┐" +
                "║..S...│" +
                "║..r...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║..B...│" +
                "└──────┘");

        assertL("╔══════┐" +
                "║..S...│" +
                "║..r...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "---☺----" +
                "--------" +
                "--------" +
                "--------" +
                "--------" +
                "---B----" +
                "--------");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---☺----" +
                "--------" +
                "--------" +
                "--------" +
                "---B----" +
                "--------");

        has(DeathRayPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("--------" +
                "--------" +
                "---☺----" +
                "---↓----" +
                "---↓----" +
                "---↓----" +
                "---B----" +
                "--------");

        has(DeathRayPerk.class);

        // when
        game.tick();

        // then
        assertE("--------" +
                "--------" +
                "---☺----" +
                "--------" +
                "--------" +
                "--------" +
                "---B----" +
                "--------");

        has(DeathRayPerk.class);
    }

    @Test
    public void shouldUseTogetherDeathRay_andUnstoppableLaser() {
        // given
        settings.integer(DEATH_RAY_PERK_RANGE, 10);

        givenFl("╔══════════┐" +
                "║..S.......│" +
                "║..r.......│" +
                "║..l.......│" +
                "║..........│" +
                "║..........│" +
                "║..........│" +
                "║..B.......│" +
                "║..........│" +
                "║..♂.......│" +
                "║..........│" +
                "└──────────┘");
        
        givenZombie().thenReturn(STOP);

        // when
        hero.down();
        game.tick();

        // then
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

        has(DeathRayPerk.class);

        // when
        hero.down();
        game.tick();

        // then
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

        has(UnstoppableLaserPerk.class);

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        events.verifyAllEvents("[KILL_ZOMBIE(gold=0, kill=1, single)]");

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

        has(UnstoppableLaserPerk.class);

        has(DeathRayPerk.class);

        // when
        game.tick();

        // then
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
    public void shouldHero_hasDeathRayPerk() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║.r..│" +
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

        has(DeathRayPerk.class);
    }

    @Test
    public void shouldNotPickDeathRayPerk_whenJumpOverIt() {
        // given
        givenFl("╔════┐" +
                "║Sr..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        hasNot(DeathRayPerk.class);

        assertL("╔════┐" +
                "║Sr..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "--*---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        game.tick();

        // then
        hasNot(DeathRayPerk.class);

        assertL("╔════┐" +
                "║Sr..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");
    }
}
