package com.codenjoy.dojo.icancode.model.perk;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MoveBoxesPerkTest extends AbstractGameTest {

    @BeforeClass
    public static void init() {
        AbstractGameTest.init();
        SettingsWrapper.data.canMoveBoxesByDefault(false);
    }

    @Test
    public void moveBoxesPerkShouldBeOnBoard() {
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
        assertE("------" +
                "-☺m---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldMoveBoxes_onlyWhenPickedUpSuchPerk() {
        // given
        givenFl("╔════┐" +
                "║Sm..│" +
                "║BB..│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.down();
        hero.pull();
        game.tick();

        // then
        assertE("------" +
                "-☺m---" +
                "-BB---" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.isCanMoveBoxes());
        assertE("------" +
                "--☺---" +
                "-BB---" +
                "------" +
                "------" +
                "------");

        hero.down();
        hero.pull();
        game.tick();

        assertE("------" +
                "------" +
                "-B☺---" +
                "--B---" +
                "------" +
                "------");
    }
}
