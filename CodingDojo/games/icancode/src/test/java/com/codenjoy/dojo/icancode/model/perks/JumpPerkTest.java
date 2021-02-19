package com.codenjoy.dojo.icancode.model.perks;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JumpPerkTest extends AbstractGameTest {

    @Test
    public void jumpPerkShouldBeOnBoard() {
        // given
        SettingsWrapper.data.canJump(false);

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
    public void shouldBeAbleToJump_whenHeroPicksUpJumpPerk() {
        // given
        SettingsWrapper.data.canJump(false);

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
