package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.model.ICanCode;
import org.junit.Test;

import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.DEFAULT_PERKS;
import static org.junit.Assert.assertEquals;

public class JumpPerkTest extends AbstractGameTest {

    @Test
    public void shouldPerkOnField_whenStart() {
        // given
        givenFl("╔════┐" +
                "║Sj..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertL("╔════┐" +
                "║Sj..│" +
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
    public void shouldBeAbleToJump_withoutJumpPerk_whenContest() {
        // given
        mode = ICanCode.CONTEST;
        settings.string(DEFAULT_PERKS, ",j");

        givenFl("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.jump();
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
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "-*----" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldBeAbleToJump_whenHeroPicksUpJumpPerk_whenTraining() {
        // given
        mode = ICanCode.TRAINING;
        settings.string(DEFAULT_PERKS, "");

        givenFl("╔════┐" +
                "║S...│" +
                "║.j..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.jump(); // will be ignored
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║.j..│" +
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
        assertEquals(true, hero.canJump());

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
        hero.jump();
        game.tick();

        // then
        assertE("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "--*---" +
                "------" +
                "------");

        game.tick();

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "--☺---" +
                "------");

        assertF("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");
    }
}
