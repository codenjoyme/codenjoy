package com.codenjoy.dojo.icancode.model;

import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class ICanCodePerksTest extends AbstractGameTest {


    @BeforeClass
    public static void init() {
        AbstractGameTest.init();
        SettingsWrapper.data.canFireByDefault(false);
    }

    @Test
    public void firePerkShouldBeOnBoard() {
        //given
        givenFl("╔════┐" +
                "║Sƒ..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        game.tick();

        // then
        assertE("------" +
                "-☺ƒ---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldBeAbleToFire_whenHeroPicksUpFirePerk() {
        // given
        givenFl("╔════┐" +
                "║Sƒ..│" +
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
                "-☺ƒ---" +
                "------" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.getCanFire());
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
}
