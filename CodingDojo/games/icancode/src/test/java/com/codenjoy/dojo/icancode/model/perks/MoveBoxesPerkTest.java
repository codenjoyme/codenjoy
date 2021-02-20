package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.model.items.perks.MoveBoxesPerk;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.Test;

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
        SettingsWrapper.data.defaultPerks(",m");

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
        SettingsWrapper.data.defaultPerks("");

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
        SettingsWrapper.data.defaultPerks(",m");

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
        SettingsWrapper.data.defaultPerks("");

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
        SettingsWrapper.data.defaultPerks("j,j");

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
