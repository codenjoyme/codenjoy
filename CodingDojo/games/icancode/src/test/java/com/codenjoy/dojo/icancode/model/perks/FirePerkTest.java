package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.ICanCode;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.Test;

import static org.junit.Assert.*;

public class FirePerkTest extends AbstractGameTest {

    @Test
    public void shouldPerkOnField_whenStart() {
        // given
        givenFl("╔════┐" +
                "║Sa..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║Sa..│" +
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
    public void shouldBeAbleToFire_withoutFirePerk_whenContest() {
        // given
        mode = ICanCode.CONTEST;
        SettingsWrapper.data.defaultPerks(",a");

        givenFl("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺----" +
                "-↓----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldBeAbleToFire_whenHeroPicksUpFirePerk_whenTraining() {
        // given
        mode = ICanCode.TRAINING;
        SettingsWrapper.data.defaultPerks("");

        givenFl("╔════┐" +
                "║S...│" +
                "║.a..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.fire(); // will be ignored
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║.a..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertEquals(true, hero.canFire());

        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "--↓---" +
                "------");
    }

    @Test
    public void shouldNotPickUpFirePerk_whenJumpOverIt() {
        // given
        SettingsWrapper.data.defaultPerks("j,j");

        givenFl("╔════┐" +
                "║Sa..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertL("╔════┐" +
                "║Sa..│" +
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

        assertEquals(true, hero.canJump());
        assertEquals(false, hero.canFire());

        // when
        hero.jump();
        hero.right();
        game.tick();

        // then
        assertEquals(true, hero.isFlying());

        assertL("╔════┐" +
                "║Sa..│" +
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

        game.tick();

        // then
        assertL("╔════┐" +
                "║Sa..│" +
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

        assertEquals(false, hero.canFire());
    }
}
