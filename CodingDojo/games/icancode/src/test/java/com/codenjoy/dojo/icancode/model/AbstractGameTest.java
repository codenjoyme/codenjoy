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
import com.codenjoy.dojo.icancode.model.items.perks.Perk;
import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.icancode.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.Printer;
import com.codenjoy.dojo.services.printer.layeredview.LayeredViewPrinter;
import com.codenjoy.dojo.services.printer.layeredview.PrinterData;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;
import static com.codenjoy.dojo.icancode.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.Direction.STOP;
import static com.codenjoy.dojo.services.PointImpl.pt;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public abstract class AbstractGameTest {

    public static final int FIRE_TICKS = 6;
    private static final int COUNT_LAYERS = 3;

    public boolean mode;
    protected ICanCode game;
    private Printer<PrinterData> printer;

    protected Hero hero;
    protected Dice dice;
    private EventListener listener;
    protected Player player;
    private Player otherPlayer;
    protected GameSettings settings;
    protected EventsListenersAssert events;

    @Before
    public void setup() {
        mode = ICanCode.TRAINING;

        settings = spy(new GameSettings())
                .integer(PERK_ACTIVITY, 10)
                .integer(PERK_AVAILABILITY, 10)
                .integer(PERK_DROP_RATIO, 100)
                .integer(DEATH_RAY_PERK_RANGE, 10)
                .integer(GUN_RECHARGE, 0)
                .integer(GUN_REST_TIME, 0)
                .integer(GUN_SHOT_QUEUE, 0)
                .string(DEFAULT_PERKS, "ajm,ajm");

        listener = mock(EventListener.class);
        events = new EventsListenersAssert(() -> Arrays.asList(listener), Events.class);
        dice = mock(Dice.class);
    }

    @After
    public void tearDown() {
        events.verifyNoEvents();
    }

    protected void ticks(int count) {
        for (int i = 0; i < count; i++) {
            game.tick();
        }
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
        settings.integer(VIEW_SIZE, viewSize);
        Level level = createLevels(new String[]{board}).get(0);
        game = new ICanCode(level, dice, mode, settings);
        player = new Player(listener, settings);
        game.newGame(player);
        this.hero = game.getHeroes().get(0);

        // логика добавления нового героя на start позиции для 'X' символа
        level.getItems(HeroItem.class)
                .forEach(item -> {
                    HeroItem heroItem = (HeroItem) item;
                    if (heroItem.getHero() == null) {
                        Player player = new Player(mock(EventListener.class), settings);
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
        return (int)Math.sqrt(board.length());
    }

    protected List<Level> createLevels(String[] boards) {
        List<Level> levels = new LinkedList<>();
        for (String board : boards) {
            Level level = new LevelImpl(board, settings);
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
        ZombieBrain brain = mock(ZombieBrain.class);
        when(settings.zombieBrain()).thenReturn(brain);
        return when(brain.whereToGo(any(Point.class), any(Field.class)));
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

    protected void has(Class<? extends Perk> perkClass) {
        assertEquals(true, hero.has(perkClass));
    }

    protected void hasNot(Class<? extends Perk> perkClass) {
        assertEquals(false, hero.has(perkClass));
    }

    protected void zombieAt(int x, int y) {
        givenZombie().thenReturn(STOP);
        Zombie zombie = new Zombie(true);
        zombie.setField(game);
        game.move(zombie, pt(x, y));
    }
}
