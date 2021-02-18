package com.codenjoy.dojo.icancode.model.perk;

import com.codenjoy.dojo.icancode.model.AbstractGameTest;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import org.junit.BeforeClass;
import org.junit.Test;

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
}
