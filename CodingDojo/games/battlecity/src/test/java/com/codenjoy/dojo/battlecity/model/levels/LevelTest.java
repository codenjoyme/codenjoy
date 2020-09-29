package com.codenjoy.dojo.battlecity.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.battlecity.model.Player;
import com.codenjoy.dojo.battlecity.services.GameRunner;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
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
        LevelImpl level = new LevelImpl(new GameRunner().getMap(), mock(Dice.class));

        assertEquals(34, level.size());

        Player player = mock(Player.class);
        Printer printer = printerFactory.getPrinter(
                level.reader(), player);

        assertEquals(
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼ ¿    ¿    ¿        ¿    ¿    ¿ ☼\n" +
                "☼                                ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬██╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬☼☼╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬☼☼╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬ █╬╬╬  ╬╬╬█ ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬ █╬╬╬█ ☼\n" +
                "☼ █╬╬╬█ ╬╬╬            ╬╬╬ █╬╬╬█ ☼\n" +
                "☼  ╬╬╬  ╬╬╬   ▓    ▓   ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ▓▓▓       ╬╬╬  ╬╬╬       ▓▓▓  ☼\n" +
                "☼  ▓▓        ╬╬╬  ╬╬╬        ▓▓  ☼\n" +
                "☼     ╬╬╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬╬╬     ☼\n" +
                "☼☼☼   ╬╬╬╬╬            ╬╬╬╬╬   ☼☼☼\n" +
                "☼ ▓▓          ▒▒▒▒▒▒          ▓▓ ☼\n" +
                "☼           ▓╬╬╬▒▒╬╬╬▓           ☼\n" +
                "☼  ╬╬╬  ╬╬╬ ▓╬╬╬▒▒╬╬╬▓ ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼  ╬╬╬▓ ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬ ▓╬╬╬  ☼\n" +
                "☼  ╬╬╬  ╬╬╬  ╬╬╬╬╬╬╬╬  ╬╬╬  ╬╬╬  ☼\n" +
                "☼ ▒╬╬╬  ╬╬╬  ╬╬╬▒▒╬╬╬  ╬╬╬  ╬╬╬▒ ☼\n" +
                "☼ ▒╬╬╬  ╬╬╬▓ ╬╬╬▒▒╬╬╬ ▓╬╬╬  ╬╬╬▒ ☼\n" +
                "☼ ▒╬╬╬  ╬╬╬▓ ╬╬╬▒▒╬╬╬ ▓╬╬╬  ╬╬╬▒ ☼\n" +
                "☼ ▒╬╬╬ ▓╬╬╬  ╬╬╬▒▒╬╬╬  ╬╬╬▓ ╬╬╬▒ ☼\n" +
                "☼ ▒╬╬╬  ▒▒▒            ▒▒▒  ╬╬╬▒ ☼\n" +
                "☼  ╬╬╬  ▒▒▒    ▓▓▓▓    ▒▒▒  ╬╬╬  ☼\n" +
                "☼  ╬╬╬  ▒▒▒  ╬╬╬╬╬╬╬╬  ▒▒▒  ╬╬╬  ☼\n" +
                "☼  ╬╬╬       ╬╬╬╬╬╬╬╬       ╬╬╬  ☼\n" +
                "☼            ╬╬    ╬╬            ☼\n" +
                "☼  ▒▒▒▒▒▒    ╬╬    ╬╬    ▒▒▒▒▒▒  ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼☼\n",
                printer.print());
    }

}
