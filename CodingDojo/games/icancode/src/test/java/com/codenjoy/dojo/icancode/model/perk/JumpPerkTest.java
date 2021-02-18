package com.codenjoy.dojo.icancode.model.perk;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class JumpPerkTest extends AbstractGameTest {

    @BeforeClass
    public static void init() {
        AbstractGameTest.init();
        SettingsWrapper.data.canJumpByDefault(false);
    }

    @Test
    public void jumpPerkShouldBeOnBoard() {
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
        assertE("------" +
                "-☺j---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldBeAbleToJump_whenHeroPicksUpJumpPerk() {
        // given
        givenFl("╔════┐" +
                "║Sj..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.jump();
        game.tick();

        // then
        assertE("------" +
                "-☺j---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.isCanJump());
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        hero.down();
        hero.jump();
        game.tick();

        assertE("------" +
                "------" +
                "------" +
                "------" +
                "------" +
                "------");

        assertF("------" +
                "------" +
                "--*---" +
                "------" +
                "------" +
                "------");

        game.tick();
        assertE("------" +
                "------" +
                "------" +
                "--☺---" +
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
