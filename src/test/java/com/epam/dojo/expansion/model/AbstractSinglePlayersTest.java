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
import com.epam.dojo.expansion.client.Board;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.services.Events;
import com.epam.dojo.expansion.services.Levels;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
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

    private Dice dice;
    private List<EventListener> listeners;
    private List<Single> singles;
    private List<Expansion> gamesSingle;
    private List<List<ILevel>> levelsSingle;

    private List<ILevel> levelMultiple;
    private Expansion gameMultiple;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        listeners = new LinkedList<>();
        singles = new LinkedList<>();
        gamesSingle = new LinkedList<>();
        levelsSingle = new LinkedList<>();
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected void givenFl(int count, String... boards) {
        Levels.VIEW_SIZE = Levels.VIEW_SIZE_TESTING;
        Deque<String> strings = new LinkedList<>(Arrays.asList(boards));

        String multiple = strings.removeLast();
        levelMultiple = createLevels(Arrays.asList(multiple));
        gameMultiple = new Expansion(levelMultiple, dice, Expansion.MULTIPLE);

        for (int i = 0; i < count; i++) {
            List<ILevel> levels = createLevels(strings);
            levelsSingle.add(levels);
            Expansion expansion = new Expansion(levels, dice, Expansion.SINGLE);
            gamesSingle.add(expansion);

            EventListener listener = mock(EventListener.class);
            listeners.add(listener);
            Single game = new Single(expansion, gameMultiple, listener, null, null);
            singles.add(game);
            game.newGame();
        }
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
        Single single = singles.get(index);
        return getOnlyMovingJoystick(single, x, y);
    }

    private List<ILevel> createLevels(Collection<String> boards) {
        List<ILevel> levels = new LinkedList<ILevel>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    protected void destroy(int index) {
        singles.get(index).destroy();
    }

    protected void assertL(int index, String expected) {
        Single single = singles.get(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(single.getPrinter().getBoardAsString(1, single.getPlayer()).getLayers().get(0)));
    }

    protected void assertE(int index, String expected) {
        Single single = singles.get(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(single.getPrinter().getBoardAsString(2, single.getPlayer()).getLayers().get(1)));
    }

    protected void assertF(int index, String expected) {
        Single single = singles.get(index);
        assertEquals(expected, single.getPrinter().getBoardAsString(2, single.getPlayer()).getForces().toString().replace('"', '\''));
    }

    protected EventListener verify(int index) {
        EventListener listener = listeners.get(index);
        return Mockito.verify(listener);
    }

    protected void reset(int index) {
        EventListener listener = listeners.get(index);
        Mockito.reset(listener);
    }

    protected void verifyNoMoreInteractions(int index) {
        EventListener listener = listeners.get(index);
        Mockito.verifyNoMoreInteractions(listener);
    }

    protected void assertBoardData(int index, String levelProgress, String heroes,
                                 boolean onlyMyName, String layer1, String layer2,
                                 String forces, Elements myForcesColor)
    {
        Single single = singles.get(index);
        JSONObject json = single.getBoardAsString();

        assertEquals(levelProgress,
                JsonUtils.toStringSorted(json.get("levelProgress")).replace('"', '\''));

        assertEquals(heroes,
                JsonUtils.toStringSorted(json.get("offset")).replace('"', '\''));

        assertEquals(TestUtils.injectN(layer1),
                TestUtils.injectN(json.getJSONArray("layers").getString(0)));

        assertEquals(TestUtils.injectN(layer2),
                TestUtils.injectN(json.getJSONArray("layers").getString(1)));

        assertEquals(forces,
                Board.parseForces(json).toString().replace('"', '\''));

        assertEquals(true,
                json.getBoolean("showName"));

        assertEquals(myForcesColor.toString(),
                json.get("myForcesColor"));

        assertEquals(onlyMyName,
                json.getBoolean("onlyMyName"));
    }
}
