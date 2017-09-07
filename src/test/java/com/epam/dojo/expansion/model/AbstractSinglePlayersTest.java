package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.QDirection;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.model.levels.*;
import com.epam.dojo.expansion.services.PrinterData;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public abstract class AbstractSinglePlayersTest {

    public static final int PLAYER1 = 0;
    public static final int PLAYER2 = 1;
    public static final int PLAYER3 = 2;
    public static final int PLAYER4 = 3;
    public static final int PLAYER5 = 4;
    public static final int PLAYER6 = 5;
    public static final int PLAYER7 = 6;
    public static final int PLAYER8 = 7;
    public static final int PLAYER9 = 8;
    public static final int PLAYER10 = 9;
    public static final int PLAYER11 = 10;
    public static final int PLAYER12 = 11;
    public static final int PLAYER13 = 12;
    public static final int PLAYER14 = 13;
    public static final int PLAYER15 = 14;
    public static final int PLAYER16 = 15;

    private Dice dice;
    private List<EventListener> listeners;
    private List<Single> singles;
    private List<Hero> heroes;

    private LinkedList<String> levelsMaps;

    private String multipleLevelsMaps;
    private GameFactory gameFactory;
    protected Ticker ticker;
    private int size = LevelsTest.LEVEL_SIZE;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        listeners = new LinkedList<>();
        singles = new LinkedList<>();
        heroes = new LinkedList<>();
        ticker = new Ticker();
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected void givenSize(int size) {
        this.size = size;
    }

    protected void givenFl(String... boards) {
        setupMaps(boards);
        gameFactory = getGameFactory();
    }

    protected void givenForces(String forces, String layer2) {
        Expansion current = singles.get(PLAYER1).getProgressBar().getCurrent();
        LevelImpl level = (LevelImpl)current.getCurrentLevel();
        level.fillForces(layer2, heroes.toArray(new Hero[0]));
        level.fillForcesCount(forces);
    }

    protected void createPlayers(int count) {
        for (int i = 0; i < count; i++) {
            createOneMorePlayer();
        }
    }

    private void setupMaps(String[] boards) {
        levelsMaps = new LinkedList<>(Arrays.asList(boards));
        multipleLevelsMaps = levelsMaps.removeLast();
    }

    protected void createOneMorePlayer() {
        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        Single game = new Single(gameFactory, listener, null, ticker, null);
        singles.add(game);
        game.newGame();
        heroes.add(game.getPlayer().getHero());
    }

    private GameFactory getGameFactory() {
        LevelsFactory single = Levels.collectYours(size, levelsMaps.toArray(new String[0]));
        LevelsFactory multiple = Levels.collectYours(size, multipleLevelsMaps);
        return getGameFactory(single, multiple);
    }

    protected GameFactory getGameFactory(LevelsFactory single, LevelsFactory multiple) {
        return new OneMultipleGameFactory(dice, single, multiple);
    }

    protected void tickAll() {
        for (Single single : singles) {
            single.tick();
        }
    }

    // делает удобным перемещение героя, что очень надо для этого легаси теста
    @NotNull
    private Hero getOnlyMovingJoystick(Single single, final int x, final int y) {
        final Hero hero = (Hero) single.getJoystick();
        final Point pt = pt(x, y);
        final int INCREASE = 2;
        final int MOVE = 1;
        return new Hero() {
            @Override
            public void down() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.DOWN)
                );
            }

            @Override
            public void up() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.UP)
                );
            }

            @Override
            public void left() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.LEFT)
                );
            }

            @Override
            public void right() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.RIGHT)
                );
            }

            @Override
            public void act(int... p) {
                hero.act(p);
            }

            @Override
            public void message(String command) {
                hero.message(command);
            }

            @Override
            public void loadLevel(int level) {
                hero.loadLevel(level);
            }
        };
    }

    protected Hero hero(int index, int x, int y) {
        return getOnlyMovingJoystick(single(index), x, y);
    }

    public Hero hero(int index) {
        return (Hero) single(index).getJoystick();
    }

    protected Single single(int index) {
        return singles.get(index);
    }

    protected void destroy(int index) {
        single(index).destroy();
    }

    protected void assertL(String expected, int index) {
        Single single = single(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getBoardAsString(single).getLayers().get(0)));
    }

    private PrinterData getBoardAsString(Single single) {
        return single.getPrinter().getBoardAsString(single.getPlayer());
    }

    protected void assertE(String expected, int index) {
        Single single = single(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getBoardAsString(single).getLayers().get(1)));
    }

    protected void assertF(String expected, int index) {
        Single single = single(index);
        assertEquals(expected,
                TestUtils.injectNN(getBoardAsString(single).getForces()));
    }

    protected EventListener verify(int index) {
        return Mockito.verify(listener(index));
    }

    private EventListener listener(int index) {
        return listeners.get(index);
    }

    protected void reset(int index) {
        Mockito.reset(listener(index));
    }

    protected void verifyNoMoreInteractions(int index) {
        Mockito.verifyNoMoreInteractions(listener(index));
    }

    protected void assertBoardData(String levelProgress, String heroes,
                                   boolean onlyMyName, String layer1, String layer2,
                                   String forces, Point myBase, int index)
    {
        JSONObject json = getBoardAsString(index);

        assertEquals(levelProgress,
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("levelProgress"))));

        assertEquals(heroes,
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("offset"))));

        assertEquals(TestUtils.injectN(layer1),
                TestUtils.injectN(json.getJSONArray("layers").getString(0)));

        assertEquals(TestUtils.injectN(layer2),
                TestUtils.injectN(json.getJSONArray("layers").getString(1)));

        assertEquals(forces,
                TestUtils.injectNN(json.getString("forces")));

        assertEquals(true,
                json.getBoolean("showName"));

        assertEquals(JsonUtils.clean(JsonUtils.toStringSorted(myBase)),
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("myBase"))));

        assertEquals(onlyMyName,
                json.getBoolean("onlyMyName"));
    }

    protected void assertBoardData(int index, String expected) {
        JSONObject json = getBoardAsString(index);
        assertEquals(JsonUtils.clean(JsonUtils.toStringSorted(expected)),
                JsonUtils.clean(JsonUtils.toStringSorted(json)));
    }

    protected JSONObject getBoardAsString(int index) {
        return single(index).getBoardAsString();
    }
}
