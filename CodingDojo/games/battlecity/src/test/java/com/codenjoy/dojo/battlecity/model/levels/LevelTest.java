package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:36
 */
public class LevelTest {

    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    @Test
    public void test() {
        Level level = new Level();

        assertEquals(34, level.size());

        Player player = mock(Player.class);
        Printer printer = printerFactory.getPrinter(
                level.reader(), player);

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
                printer.print());
    }

}
