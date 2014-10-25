package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.battlecity.model.BattlecityPrinter;
import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.Printer;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:36
 */
public class LevelTest {

    @Test
    public void test() {
        Level level = new Level();

        assertEquals(34, level.size());

        Player player = mock(Player.class);
        Printer printer = new Printer(level.size(), new BattlecityPrinter(level, player));

        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼\n" +
                "☼                                ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬☼☼╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬☼☼╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬            ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬            ╬╬╬  ╬╬╬  ☼\n" +
                "☼            ╬╬╬  ╬╬╬            ☼\n" +
                "☼            ╬╬╬  ╬╬╬            ☼\n" +
                "☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼\n" +
                "☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼\n" +
                "☼                                ☼\n" +
                "☼            ╬╬╬  ╬╬╬            ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬                      ╬╬╬  ☼\n" +
                "☼  ╬╬╬                      ╬╬╬  ☼\n" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼\n" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼\n" +
                "☼            ╬╬    ╬╬            ☼\n" +
                "☼            ╬╬    ╬╬            ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n",
                printer.toString());
    }

}
