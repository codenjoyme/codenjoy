package com.epam.dojo.icancode.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 EPAM
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

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.model.items.HeroItem;
import com.epam.dojo.icancode.services.Levels;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractGameTest {

    public static final int FIRE_TICKS = 6;
    private static final int COUNT_LAYERS = 2;
    ICanCode game;
    private Printer<PrinterData> printer;

    Hero hero;
    Dice dice;
    EventListener listener;
    Player player;
    private Player otherPlayer;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    OngoingStubbing<Integer> dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
        return when;
    }

    void givenFl(String board) {
        Levels.VIEW_SIZE = Levels.VIEW_SIZE_TESTING;
        ILevel level = createLevels(new String[]{board}).get(0);
        game = new ICanCode(level, dice, ICanCode.SINGLE);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        this.hero = game.getHeroes().get(0);
        level.getItems(HeroItem.class) //
                .forEach(item -> {
                    HeroItem heroItem = (HeroItem) item;
                    if (heroItem.getHero() == null) {
                        Player player = new Player(mock(EventListener.class));
                        game.newGame(player);
                        Hero hero = player.getHero();
                        heroItem.init(hero);
                    }
                });

        printer = new LayeredViewPrinter(
                game.reader().size(),
                () -> game.layeredReader(),
                () -> player,
                Levels.size(),
                COUNT_LAYERS);
    }

    List<ILevel> createLevels(String[] boards) {
        List<ILevel> levels = new LinkedList<>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    void assertL(String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.print().getLayers().get(0)));
    }

    void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.print().getLayers().get(1)));
    }
}
