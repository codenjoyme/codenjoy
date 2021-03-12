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
import com.codenjoy.dojo.icancode.model.items.perks.UnlimitedFirePerk;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.GUN_RECHARGE;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.PERK_ACTIVITY;

public class UnlimitedFirePerkTest extends AbstractGameTest {

    @Test
    public void shouldHeroHasPerk_whenPickItUp() {
        // given
        givenFl("╔════┐" +
                "║.S..│" +
                "║.f..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertL("╔════┐" +
                "║.S..│" +
                "║.f..│" +
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

        has(UnlimitedFirePerk.class);
    }

    @Test
    public void shouldFire_withoutPerk() {
        // given
        settings.integer(GUN_RECHARGE, 2);

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

        hasNot(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        hasNot(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "--☺----" +
                "-------" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------");

        hasNot(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "--☺----" +
                "-------" +
                "-------" +
                "--↓----" +
                "-------" +
                "-------");

        hasNot(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "--↓----" +
                "-------");

        hasNot(UnlimitedFirePerk.class);
    }

    @Test
    public void shouldFire_withPerk() {
        // given
        settings.integer(PERK_ACTIVITY, 2)
                .integer(GUN_RECHARGE, 3);

        givenFl("╔═════┐" +
                "║.S...│" +
                "║.f...│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.down();
        game.tick();

        // then
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

        has(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "--☺----" +
                "--↓----" +
                "-------" +
                "-------" +
                "-------");
        
        has(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "--☺----" +
                "--↓----" +
                "--↓----" +
                "-------" +
                "-------");
        
        hasNot(UnlimitedFirePerk.class);

        // when
        hero.fire();
        hero.down();
        game.tick();

        // then
        assertE("-------" +
                "-------" +
                "--☺----" +
                "-------" +
                "--↓----" +
                "--↓----" +
                "-------");
    }

    @Test
    public void shouldNotPickPerk_whenJumpOverIt() {
        // given
        givenFl("╔════┐" +
                "║Sf..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        hasNot(UnlimitedFirePerk.class);

        assertL("╔════┐" +
                "║Sf..│" +
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
        hasNot(UnlimitedFirePerk.class);

        assertL("╔════┐" +
                "║Sf..│" +
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
