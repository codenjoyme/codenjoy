package com.codenjoy.dojo.icancode.model;

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

import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.services.Events;
import org.junit.Test;

import static org.mockito.Mockito.verify;

public class ICanCodePullPushBoxTest extends AbstractGameTest {

    @Test
    public void shouldMovableBoxOnBoard() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItLeft() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║..BS│" +
                "└────┘");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B☺--" +
                "------");

        // when
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B☺--" +
                "------");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "-B☺---" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItRight_case1() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║BS..│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B☺--" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--B-☺-" +
                "------");

        // when
        hero.left();
        game.tick();

        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "---B☺-" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItRight_case2() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║B...│" +
                "║.S..│" +
                "└────┘");

        hero.up();
        game.tick();

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "--B☺--" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "--B-☺-" +
                "------" +
                "------");

        // when
        hero.left();
        game.tick();

        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║.S..│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "---B☺-" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItRight() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺B-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItLeft_case1() {
        // given
        givenFl("╔════┐" +
                "║..SB│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺-B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItLeft_case2() {
        // given
        givenFl("╔════┐" +
                "║..S.│" +
                "║...B│" +
                "║....│" +
                "║....│" +
                "└────┘");

        hero.down();
        game.tick();

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺B--" +
                "------" +
                "------" +
                "------");

        // when
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺-B--" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺B---" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItDown() {
        // given
        givenFl("╔════┐" +
                "║S...│" +
                "║B...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.down();
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
                "-☺----" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.down();
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
                "-☺----" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.down();
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
                "------" +
                "-☺----" +
                "-B----" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItUp_case1() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║B...│" +
                "└────┘");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺----" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "------" +
                "-B----" +
                "------" +
                "------");

        // when
        hero.down();
        game.tick();

        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "-B----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItUp_case2() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║.B..│" +
                "└────┘");

        hero.down();
        game.tick();
        hero.right();
        game.tick();

        // when
        hero.pull();
        hero.up();
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
                "--☺---" +
                "--B---" +
                "------" +
                "------");

        // when
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺---" +
                "------" +
                "--B---" +
                "------" +
                "------");

        // when
        hero.down();
        game.tick();

        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺---" +
                "--B---" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItUp() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║....│" +
                "║...B│" +
                "║...S│" +
                "└────┘");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------");

        // when
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║....│" +
                "║....│" +
                "║...S│" +
                "└────┘");

        assertE("------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItDown_case1() {
        // given
        givenFl("╔════┐" +
                "║...B│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------" +
                "------");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "----B-" +
                "------" +
                "----☺-" +
                "------");

        // when
        hero.up();
        game.tick();

        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "----B-" +
                "----☺-" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullItDown_case2() {
        // given
        givenFl("╔════┐" +
                "║..B.│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        hero.left();
        game.tick();

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "---B--" +
                "---☺--" +
                "------" +
                "------");

        // when
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "---B--" +
                "------" +
                "---☺--" +
                "------");

        // when
        hero.up();
        game.tick();

        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "------" +
                "---B--" +
                "---☺--" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnWall() {
        // given
        givenFl("╔════┐" +
                "║..SB│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺B-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnOtherBox() {
        // given
        givenFl("╔════┐" +
                "║SBB.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺BB--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnZombie() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        game.move(new Zombie(true),3,4);
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertE("------" +
                "-☺B♂--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B♂--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnStartPoint() {
        // given
        givenFl("╔════┐" +
                "║SBS.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertL("╔════┐" +
                "║S.S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPullItOnStartPoint() {
        // given
        givenFl("╔════┐" +
                "║.BS.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");
        assertE("------" +
                "--B☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B-☺-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnGold() {
        // given
        givenFl("╔════┐" +
                "║SB$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItOnGold_whenGoldIsHidden() {
        // given
        givenFl("╔════┐" +
                "║.B$S│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.left();
        game.tick();

        hero.down();
        game.tick();

        hero.left();
        game.tick();

        hero.left();
        game.tick();

        hero.up();
        game.tick();

        // then
        assertL("╔════┐" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║...S│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnLaserMachine() {
        // given
        givenFl("╔════┐" +
                "║SB˃.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.˃.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnOtherHero() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║...X│" + // X - значит на S появится новый герой-соперник
                "└────┘");

        hero.down();
        game.tick();

        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-XB---" +
                "-☺----" +
                "------" +
                "------" +
                "------");

        hero.right();
        game.tick();
        hero.right();
        game.tick();
        hero.up();
        game.tick();

        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-XB☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-XB☺--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithDestroyIt_caseOnLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB..│" +
                "║..↑.│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
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
                "--☺B--" +
                "------" +
                "------" +
                "------");

        // when
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
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithDestroyIt_caseBeforeLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB..│" +
                "║....│" +
                "║..↑.│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
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
                "--☺B--" +
                "---↑--" +
                "------" +
                "------");

        // when
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
                "--☺B--" +
                "------" +
                "------" +
                "------");

        // when
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
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxOnLaserWithoutDestroyIt_caseAfterLaser() {
        // given
        givenFl("╔════┐" +
                "║....│" +
                "║SB↑.│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║....│" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---↑--" +
                "--☺B--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItOnHoleSoWeHaveHiddenHole() {
        // given
        givenFl("╔════┐" +
                "║SBO.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.O.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.LOOSE());

        assertL("╔════┐" +
                "║S.O.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---oB-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBox_whenHeroPushItOnEdit() {
        // given
        givenFl("╔════┐" +
                "║SBE.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.E.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBox_whenHeroPushItOnStart() {
        // given
        givenFl("╔════┐" +
                "║SBS.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldJumpOverBoxDoNotMoveIt() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B---" +
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
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B☺--" +
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

    @Test
    public void shouldMovedBox_whenHeroPullUpright_case1() {
        // given
        givenFl("╔═════┐" +
                "║..B..│" +
                "║.BSB.│" +
                "║.B.B.│" +
                "║.BBB.│" +
                "║.....│" +
                "└─────┘");

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--BBB--" +
                "--B☺B--" +
                "--BBB--" +
                "-------" +
                "-------");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "---B---" +
                "--B☺B--" +
                "--B-B--" +
                "--BBB--" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullUpright_case2() {
        // given
        givenFl("╔═════┐" +
                "║..B..│" +
                "║.B.B.│" +
                "║.BSB.│" +
                "║.BBB.│" +
                "║.....│" +
                "└─────┘");

        hero.up();
        game.tick();

        // when
        hero.pull();
        hero.down();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "--BBB--" +
                "--B☺B--" +
                "--BBB--" +
                "-------" +
                "-------");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔═════┐" +
                "║.....│" +
                "║.....│" +
                "║..S..│" +
                "║.....│" +
                "║.....│" +
                "└─────┘");

        assertE("-------" +
                "---B---" +
                "--B☺B--" +
                "--B-B--" +
                "--BBB--" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldMovedBox_whenHeroPullHorizontally_case1() {
        // given
        givenFl("╔══════┐" +
                "║......│" +
                "║..BB..│" +
                "║.BS.B.│" +
                "║..BB..│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "---B☺B--" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "--B☺-B--" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");
    }


    @Test
    public void shouldMovedBox_whenHeroPullHorizontally_case2() {
        // given
        givenFl("╔══════┐" +
                "║......│" +
                "║..BB..│" +
                "║B.S.B.│" +
                "║..BB..│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        hero.left();
        game.tick();

        // when
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "--B☺-B--" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");

        hero.right();
        game.tick();

        // when
        hero.pull();
        hero.left();
        game.tick();

        // then
        assertL("╔══════┐" +
                "║......│" +
                "║......│" +
                "║..S...│" +
                "║......│" +
                "║......│" +
                "║......│" +
                "└──────┘");

        assertE("--------" +
                "--------" +
                "---BB---" +
                "--B☺B---" +
                "---BB---" +
                "--------" +
                "--------" +
                "--------");
    }

    @Test
    public void shouldForgetPullCommand_whenNoBoxOnThisTick() {
        // given
        givenFl("╔════┐" +
                "║S.B.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.pull(); // холостой
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
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

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
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

    @Test
    public void shouldCantMovedBox_whenJump_case1() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B---" +
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

        // when I'm flying
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B☺--" +
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

    @Test
    public void shouldCantMovedBox_whenJump_case2() {
        // given
        givenFl("╔════┐" +
                "║S.B.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---B--" +
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

        // when I'm flying
        hero.pull();
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--☺B--" +
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

    @Test
    public void shouldCantMovedBox_whenJump_case3() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.jump();
        hero.pull(); // there is
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B---" +
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

        // when I'm flying
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "--B☺--" +
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

    @Test
    public void shouldJumpOverHole_thenMoveBoxOnIt() {
        // given
        givenFl("╔═══┐" +
                "║SO.│" +
                "║.B.│" +
                "║...│" +
                "└───┘");

        // when
        hero.jump();
        hero.right();

        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--B--" +
                "-----" +
                "-----");

        assertF("-----" +
                "--*--" +
                "-----" +
                "-----" +
                "-----");

        // when
        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "---☺-" +
                "--B--" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero.down();
        game.tick();

        hero.down();
        game.tick();

        hero.left();
        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--B--" +
                "--☺--" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "--B--" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldMoveBoxOnHole() {
        // given
        givenFl("╔═══┐" +
                "║SO.│" +
                "║.B.│" +
                "║...│" +
                "└───┘");

        // when
        hero.down();
        game.tick();

        hero.down();
        game.tick();

        hero.right();
        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "-----" +
                "--B--" +
                "--☺--" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero.pull();
        hero.up();
        game.tick();

        // then
        assertL("╔═══┐" +
                "║SO.│" +
                "║...│" +
                "║...│" +
                "└───┘");

        assertE("-----" +
                "--B--" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("-----" +
                "-----" +
                "-----" +
                "-----" +
                "-----");
    }
}
