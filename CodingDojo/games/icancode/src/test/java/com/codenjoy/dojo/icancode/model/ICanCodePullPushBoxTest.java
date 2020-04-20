package com.codenjoy.dojo.icancode.model;

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
    public void shouldMovedBox_whenHeroPullItRight() {
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
    public void shouldMovedBox_whenHeroPullItLeft() {
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
    public void shouldMovedBox_whenHeroPullItUp() {
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
    public void shouldMovedBox_whenHeroPullItDown() {
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
    public void shouldNotMovedBoxWhenHeroPushItOnOtherBox() {
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
    public void shouldNotMovedBoxWhenHeroPushItOnGold() {
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
    public void shouldMovedBoxWhenHeroPushItOnGold_whenGoldIsHidden() {
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
    public void shouldNotMovedBoxWhenHeroPushItOnLaserMachine() {
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
    public void shouldNotMovedBoxWhenHeroPushItOnOtherHero() {
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
    public void shouldMovedBoxWhenHeroPushItOnHoleSoWeHaveHiddenHole() {
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
    public void shouldMovedBoxWhenHeroPushItOnEdit() {
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
    public void shouldMovedBoxWhenHeroPushItOnStart() {
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
                "--☺B--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMovedBoxWhenHeroPullItRight() {
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
    public void shouldMovedBoxWhenHeroPullItLeft() {
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
    public void shouldMovedBoxWhenHeroPullItUp() {
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
    public void shouldMovedBoxWhenHeroPullItDown() {
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
    public void shouldMovedBoxWhenHeroPullUpright() {
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
    public void shouldMovedBoxWhenHeroPullHorizontally() {
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
}
