package com.codenjoy.dojo.icancode.model.perk;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

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

}
