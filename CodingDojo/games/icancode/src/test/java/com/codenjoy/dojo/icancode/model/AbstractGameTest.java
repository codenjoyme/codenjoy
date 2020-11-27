package com.codenjoy.dojo.icancode.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2020 Codenjoy
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

import com.codenjoy.dojo.icancode.model.items.HeroItem;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.icancode.services.Levels;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER1;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER2;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER3;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractGameTest {

    public static final int FIRE_TICKS = 6;
    private static final int COUNT_LAYERS = 3;
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
        givenFl(viewSize(board), board);
    }

    void givenFl(int viewSize, String board) {
        Levels.VIEW_SIZE = viewSize;
        Level level = createLevels(new String[]{board}).get(0);
        game = new ICanCode(level, dice, ICanCode.TRAINING);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        this.hero = game.getHeroes().get(0);

        // логика добавления нового героя на start позиции для 'X' символа
        level.getItems(HeroItem.class)
                .forEach(item -> {
                    HeroItem heroItem = (HeroItem) item;
                    if (heroItem.getHero() == null) {
                        Player player = new Player(mock(EventListener.class));
                        game.newGame(player);
                        Hero hero = player.getHero();
                        heroItem.init(hero);
                        item.removeFromCell();
                    }
                });

        printer = new LayeredViewPrinter(
                () -> game.layeredReader(),
                () -> player,
                COUNT_LAYERS);
    }

    int viewSize(String board) {
        return (int)Math.sqrt(board.length());
    }

    List<Level> createLevels(String[] boards) {
        List<Level> levels = new LinkedList<>();
        for (String board : boards) {
            Level level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    void assertL(String expected) {
        assertA(expected, LAYER1);
    }

    private void assertA(String expected, int index) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.print().getLayers().get(index)));
    }

    void assertE(String expected) {
        assertA(expected, LAYER2);
    }

    void assertF(String expected) {
        assertA(expected, LAYER3);
    }
}
