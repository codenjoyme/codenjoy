package com.codenjoy.dojo.icancode.model.perk;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FirePerkTest extends AbstractGameTest {


    @BeforeClass
    public static void init() {
        AbstractGameTest.init();
        SettingsWrapper.data.canFireByDefault(false);
    }

    @Test
    public void firePerkShouldBeOnBoard() {
        // given
        givenFl("╔════┐" +
                "║Sf..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertE("------" +
                "-☺f---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldBeAbleToFire_whenHeroPicksUpFirePerk() {
        // given
        givenFl("╔════┐" +
                "║Sf..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.fire();
        game.tick();

        // then
        assertE("------" +
                "-☺f---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.isCanFire());
        assertE("------" +
                "--☺---" +
                "------" +
                "------" +
                "------" +
                "------");

        hero.down();
        hero.fire();
        game.tick();

        assertE("------" +
                "--☺---" +
                "--↓---" +
                "------" +
                "------" +
                "------");

        game.tick();
        assertE("------" +
                "--☺---" +
                "------" +
                "--↓---" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotPickUpFirePerk_whenJumpOverIt() {
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
        game.tick();

        // then
        assertE("------" +
                "--f☺--" +
                "------" +
                "------" +
                "------" +
                "------");

        assertFalse(hero.isCanFire());
    }
}
