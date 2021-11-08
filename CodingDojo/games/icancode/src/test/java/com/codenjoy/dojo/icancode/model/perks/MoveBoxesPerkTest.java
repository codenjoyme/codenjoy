package com.codenjoy.dojo.icancode.model.perks;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.items.perks.MoveBoxesPerk;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEFAULT_PERKS;
import static org.junit.Assert.assertEquals;

public class MoveBoxesPerkTest extends AbstractGameTest {

    @Test
    public void shouldPerkOnField_whenStart() {
        // given
        givenFl("╔════┐" +
                "║Sm..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║Sm..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldPushBoxes_onlyWhenPickedUpSuchPerk_whenContest() {
        // given
        mode = ICanCode.CONTEST;
        settings.string(DEFAULT_PERKS, ",m");

        givenFl("╔════┐" +
                "║S...│" +
                "║BB..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.pull();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺B---" +
                "-B----" +
                "------" +
                "------");
    }

    @Test
    public void shouldPushBoxes_onlyWhenPickedUpSuchPerk_whenTraining() {
        // given
        mode = ICanCode.TRAINING;
        settings.string(DEFAULT_PERKS, "");

        givenFl("╔════┐" +
                "║Sm..│" +
                "║BB..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.pull(); // will be ignored
        game.tick();

        // then
        assertL("╔════┐" +
                "║Sm..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "-BB---" +
                "------" +
                "------" +
                "------");
        
        // when
        hero.right();
        game.tick();

        // then
        assertEquals(true, hero.canMoveBoxes());
        
        assertE("------" +
                "--☺---" +
                "-BB---" +
                "------" +
                "------" +
                "------");

        // when
        hero.down();
        hero.pull();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "-B☺---" +
                "--B---" +
                "------" +
                "------");
    }

    @Test
    public void shouldPullBoxes_onlyWhenPickedUpSuchPerk_whenContest() {
        // given
        mode = ICanCode.CONTEST;
        settings.string(DEFAULT_PERKS, ",m");

        givenFl("╔════┐" +
                "║....│" +
                "║S...│" +
                "║BB..│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.pull();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "-☺B---" +
                "-B----" +
                "------");
    }

    @Test
    public void shouldPullBoxes_onlyWhenPickedUpSuchPerk_whenTraining() {
        // given
        mode = ICanCode.TRAINING;
        settings.string(DEFAULT_PERKS, "");

        givenFl("╔════┐" +
                "║....│" +
                "║Sm..│" +
                "║BB..│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.pull(); // will be ignored
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║Sm..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺----" +
                "-BB---" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertEquals(true, hero.canMoveBoxes());

        assertE("------" +
                "------" +
                "--☺---" +
                "-BB---" +
                "------" +
                "------");

        // when
        hero.up();
        hero.pull();
        game.tick();

        // then
        assertE("------" +
                "--☺---" +
                "--B---" +
                "-B----" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotPickMoveBoxesPerk_whenJumpOverIt() {
        // given
        settings.string(DEFAULT_PERKS, "j,j");

        givenFl("╔════┐" +
                "║Sm..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        hasNot(MoveBoxesPerk.class);

        assertL("╔════┐" +
                "║Sm..│" +
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
        hasNot(MoveBoxesPerk.class);

        assertL("╔════┐" +
                "║Sm..│" +
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
