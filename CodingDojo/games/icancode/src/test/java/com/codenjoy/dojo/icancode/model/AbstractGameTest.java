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
import com.codenjoy.dojo.icancode.model.items.Zombie;
import com.codenjoy.dojo.icancode.model.items.ZombieBrain;
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.icancode.services.SettingsWrapper;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.services.settings.SettingsImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractGameTest {

    public static final int FIRE_TICKS = 6;
    private static final int COUNT_LAYERS = 3;
    protected ICanCode game;
    private Printer<PrinterData> printer;

    protected Hero hero;
    protected Dice dice;
    protected EventListener listener;
    protected Player player;
    private Player otherPlayer;
    protected SettingsWrapper settings;

    @Before
    public void setup() {
        settings = SettingsWrapper.setup(new SettingsImpl())
                .perkActivity(10)
                .perkAvailability(10)
                .perkDropRatio(100)
                .deathRayRange(10)
                .gunRecharge(0);
        dice = mock(Dice.class);
    }

    protected OngoingStubbing<Integer> dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
        return when;
    }

    protected void givenFl(String board) {
        givenFl(viewSize(board), board);
    }

    protected void givenFl(int viewSize, String board) {
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

    protected int viewSize(String board) {
        return (int) Math.sqrt(board.length());
    }

    protected List<Level> createLevels(String[] boards) {
        List<Level> levels = new LinkedList<>();
        for (String board : boards) {
            Level level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    protected OngoingStubbing<Integer> generateFemale() {
        return dice(1);
    }

    protected OngoingStubbing<Integer> generateMale() {
        return dice(0);
    }

    protected OngoingStubbing<Direction> givenZombie() {
        Zombie.BRAIN = mock(ZombieBrain.class);
        return when(Zombie.BRAIN.whereToGo(any(Point.class), any(Field.class)));
    }

    protected void assertL(String expected) {
        assertA(expected, LAYER1);
    }

    private void assertA(String expected, int index) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.print().getLayers().get(index)));
    }

    protected void assertE(String expected) {
        assertA(expected, LAYER2);
    }

    protected void assertF(String expected) {
        assertA(expected, LAYER3);
    }
}
