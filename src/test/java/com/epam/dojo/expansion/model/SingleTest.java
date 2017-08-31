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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.services.Events;
import com.epam.dojo.expansion.services.Levels;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class SingleTest {

    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Single single1;
    private Single single2;

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String... boards) {
        Levels.VIEW_SIZE = Levels.VIEW_SIZE_TESTING;
        Deque<String> strings = new LinkedList<>(Arrays.asList(boards));
        String multiple = strings.removeLast();
        List<ILevel> levelsSingle1 = createLevels(strings);
        List<ILevel> levelsSingle2 = createLevels(strings);
        List<ILevel> levelMultiple = createLevels(Arrays.asList(multiple));

        Expansion gameSingle1 = new Expansion(levelsSingle1, dice, Expansion.SINGLE);
        Expansion gameSingle2 = new Expansion(levelsSingle2, dice, Expansion.SINGLE);
        Expansion gameMultiple = new Expansion(levelMultiple, dice, Expansion.MULTIPLE);
        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);

        single1 = new Single(gameSingle1, gameMultiple, listener1, null, null);
        single1.newGame();

        single2 = new Single(gameSingle2, gameMultiple, listener2, null, null);
        single2.newGame();
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
                        new Forces(pt, MOVE, DoubleDirection.DOWN)
                );
            }

            @Override
            public void up() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new Forces(pt, MOVE, DoubleDirection.UP)
                );
            }

            @Override
            public void left() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new Forces(pt, MOVE, DoubleDirection.LEFT)
                );
            }

            @Override
            public void right() {
                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new Forces(pt, MOVE, DoubleDirection.RIGHT)
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

    private Hero hero1(int x, int y) {
        return getOnlyMovingJoystick(single1, x, y);
    }

    private Hero hero2(int x, int y) {
        return getOnlyMovingJoystick(single2, x, y);
    }

    private List<ILevel> createLevels(Collection<String> boards) {
        List<ILevel> levels = new LinkedList<ILevel>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    private void assertL(Single single, String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(single.getPrinter().getBoardAsString(1, single.getPlayer()).getLayers().get(0)));
    }

    private void assertE(Single single, String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(single.getPrinter().getBoardAsString(2, single.getPlayer()).getLayers().get(1)));
    }

    private void assertF(Single single, String expected) {
        assertEquals(expected, single.getPrinter().getBoardAsString(2, single.getPlayer()).getLayers().get(2).replace('"', '\''));
    }

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1, "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero1(1, 2).right();
        single1.tick();

        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':1}]");

        // when
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero1(1, 2).down();
        single1.tick();

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║E.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        // when
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero1(1, 2).down();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        // when
        hero1(1, 1).right();
        single1.tick();

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "-☺☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':1,'y':1},'count':2}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        // when
        single1.tick();

        // then
        verifyNoMoreInteractions(listener1);

        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");
    }

    @Test
    public void shouldEveryHeroHasTheirOwnStartBase() {
        // given
        givenFl("╔═════┐" +
                "║1.E..│" +
                "║.....│" +
                "║E...E│" +
                "║.....│" +
                "║..E..│" +
                "└─────┘",
                "╔═════┐" +
                "║4.E.1│" +
                "║.....│" +
                "║E...E│" +
                "║.....│" +
                "║3.E.2│" +
                "└─────┘");

        // level 1 - single for everyone

        assertE(single1,
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(single1,
                "[{'region':{'x':1,'y':5},'count':10}]");

        assertE(single2,
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(single2,
                "[{'region':{'x':1,'y':5},'count':10}]");

        // when
        // hero1 goes to multiple level
        hero1(1, 5).right();
        single1.tick();
        single2.tick();

        assertF(single1,
                "[{'region':{'x':1,'y':5},'count':11}," +
                " {'region':{'x':2,'y':5},'count':1}]");

        hero1(2, 5).right();
        single1.tick();
        single2.tick();

        assertF(single1,
                "[{'region':{'x':1,'y':5},'count':11}," +
                " {'region':{'x':2,'y':5},'count':2}," +
                " {'region':{'x':3,'y':5},'count':1}]");

        verify(listener1).event(Events.WIN(0));
        reset(listener1);
        verifyNoMoreInteractions(listener2);

        single1.tick();
        single2.tick();

        // then
        // hero1 on their own start base
        assertE(single1,
                "-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(single1,
                "[{'region':{'x':5,'y':5},'count':10}]");

        // for hero2 nothing will be changed
        assertE(single2,
                "-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");

        assertF(single2,
                "[{'region':{'x':1,'y':5},'count':10}]");

    }

    @Test
    public void shouldSeveralPlayersCollectionAtLastLevel() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");
        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero1(1, 2).right();
        single1.tick();
        single2.tick();

        verify(listener1).event(Events.WIN(0));
        reset(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':1}]");

        assertL(single2,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero2(1, 2).right();
        single1.tick(); // goes multiple
        single2.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");

        assertL(single2,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':1}]");

        // when
        hero1(1, 2).down();
        single1.tick();
        single2.tick(); // goes multiple

        // then
        verifyNoMoreInteractions(listener1);
        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "-♥--" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        // when
        hero1(1, 1).right(); // finished
        single1.tick();
        single2.tick();

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "-☺☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':2}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "-♥♥-" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':2}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        // when
        hero2(2, 2).down();
        single1.tick(); // started
        single2.tick(); // finished

        // then
        verifyNoMoreInteractions(listener1);
        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "--♦-" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':11}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "--☺-" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':11}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        // when
        single1.tick();
        single2.tick(); // started

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':10}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "----" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':10}]");

        // when
        hero1(1, 2).down();
        single1.tick();
        single2.tick();

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "-♥--" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':10}," +
                " {'region':{'x':1,'y':1},'count':1}]");

        // when
        hero1(1, 1).right();
        single1.tick(); // finished
        single2.tick();

        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        hero2(2, 2).down();
        single1.tick(); // started
        single2.tick(); // finished

        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        single1.tick();
        single2.tick(); // started

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        // then
        assertL(single1,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺♦-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':10}]");

        assertL(single2,
                "╔══┐" +
                "║12│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-♥☺-" +
                "----" +
                "----");

        assertF(single2,
                "[{'region':{'x':1,'y':2},'count':10}," +
                " {'region':{'x':2,'y':2},'count':10}]");
    }

    @Test
    public void shouldAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘",
                "╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘",
                "╔══┐" + // multiple
                "║E.│" +
                "║1.│" +
                "└──┘"
        );

        assertL(single1,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when done 1 level - go to 2 (single)
        hero1(1, 2).right();
        single1.tick();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':2},'count':10}]");

        // when done 2 level - go to 3 (single)
        hero1(2, 2).down();
        single1.tick();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':1},'count':10}]");

        // when done 3 level - go to 4 (multiple)
        hero1(2, 1).left();
        single1.tick();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':10}]");

        // when done 4 level - start 4 again (multiple)
        hero1(1, 1).up();
        single1.tick();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':10}]");

        // when done 4 level - start 4 again multiple)
        hero1(1, 1).up();
        single1.tick();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':10}]");
    }

    @Test
    public void shouldSelectLevelWhenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone();

        // when try to change level 1  - success from multiple to single
        hero1(1, 1).loadLevel(0);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':2},'count':10}]");

        // when try to change level 2  - success from single to single
        hero1(1, 2).loadLevel(1);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':2},'count':10}]");

        // when try to change level 3  - success from single to single
        hero1(2, 2).loadLevel(2);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':1},'count':10}]");

        // when try to change level 4 - success from single to multiple
        hero1(2, 1).loadLevel(3);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':10}]");

        // when try to change level 500 - fail
        hero1(1, 1).right();
        single1.tick();
        hero1(1, 1).loadLevel(500);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':11}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        // when try to change level 2 - success from multiple to single
        hero1(2, 1).loadLevel(1);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.1│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':2},'count':10}]");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero1(1, 1).loadLevel(3);
        single1.tick();
        single1.tick();

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':10}]");

        hero1(1, 1).right();
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':1,'y':1},'count':11}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        // when try to change level 3 (previous) - success
        hero1(2, 1).loadLevel(2);
        single1.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║E1│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        assertF(single1,
                "[{'region':{'x':2,'y':1},'count':10}]");
    }

    @Ignore
    @Test
    public void shouldResetOnMultipleWillResetOnlyMultipleLevel() {
        // given
        shouldAllLevelsAreDone();

        // when
        hero1(1, 2).reset();
        single1.tick();
        single1.tick();
        hero1(1, 2).right();
        single1.tick();

        // then
        assertL(single1, // still multiple
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero1(2, 1).reset();
        single1.tick();

        // then
        assertL(single1, // still multiple
                "╔══┐" +
                "║E.│" +
                "║1.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Ignore
    @Test
    public void testGetBoardAsString() {
        // given
        givenFl("╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║1..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        // when then
        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':0}",
                true,
                "['╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘'," +
                "'-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----']", single1);

        // when then
        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':0}",
                true,
                "['╔═══┐" +
                "║1E.│" +
                "║...│" +
                "║...│" +
                "└───┘'," +
                "'-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----']",
                single2);

        // go to next level
        hero1(1, 4).right();
        hero2(1, 4).right();
        single1.tick();
        single2.tick();

        single1.tick();
        single2.tick();

        // then select different way
        hero1(2, 4).right();
        hero2(2, 4).down();
        single1.tick();
        single2.tick();

        // then
        assertL(single1,
                "╔═══┐" +
                "║1..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "--☺--" +
                "-☻---" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║1..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "--☻--" +
                "-☺---" +
                "-----" +
                "-----");

        // when then
        assertBoardData("{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}",
                "{'x':0,'y':0}",
                false,
                "['╔═══┐" +
                "║1..│" +
                "║...│" +
                "║..E│" +
                "└───┘'," +
                "'-----" +
                "--☺--" +
                "-☻---" +
                "-----" +
                "-----']",
                single1);

        // when then
        assertBoardData("{'current':0,'lastPassed':0,'multiple':true,'scores':true,'total':1}",
                "{'x':0,'y':0}",
                false,
                "['╔═══┐" +
                "║1..│" +
                "║...│" +
                "║..E│" +
                "└───┘'," +
                "'-----" +
                "--☻--" +
                "-☺---" +
                "-----" +
                "-----']",
                single2);
    }

    @Test
    public void testGetBoardAsString_whenBigFrame() {
        // given
        String field =
                "╔══════════════════┐" +
                "║1.................│" +
                "║..............B...│" +
                "║....┌──╗..........│" +
                "║....│  ║..........│" +
                "║..┌─┘  └─╗........│" +
                "║..│      ║........│" +
                "║..│      ║........│" +
                "║..╚═┐  ╔═╝........│" +
                "║....│  ║..........│" +
                "║....╚══╝..........│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║.B................│" +
                "║..................│" +
                "║..................│" +
                "║.................E│" +
                "└──────────────────┘";
        givenFl(field, field);

        // when then
        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':4}",
                true,
                "['╔═══════════════" +
                "║1.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............'," +
                "'----------------" +
                "-☺--------------" +
                "---------------B" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "--B-------------'," +
                "'[{'region':{'x':1,'y':18},'count':10}]']", single1);

        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':4}",
                true,
                "['╔═══════════════" +
                "║1.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............'," +
                "'----------------" +
                "-☺--------------" +
                "---------------B" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "--B-------------'," +
                "'[{'region':{'x':1,'y':18},'count':10}]']", single2);

        // when
        for (int i = 0; i < 17; i++) {
            hero1(i + 1, 18).right();
            hero2(1, 18 - i).down();
            single1.tick();
            single2.tick();
        }

        // then
        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':4,'y':4}",
                true,
                "['═══════════════┐" +
                "...............│" +
                "...............│" +
                ".┌──╗..........│" +
                ".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│'," +
                "'----------------" +
                "☺☺☺☺☺☺☺☺☺☺☺☺☺☺☺-" +
                "-----------B----" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------'," +
                "'[{'region':{'x':4,'y':18},'count':2}," +
                " {'region':{'x':5,'y':18},'count':2}," +
                " {'region':{'x':6,'y':18},'count':2}," +
                " {'region':{'x':7,'y':18},'count':2}," +
                " {'region':{'x':8,'y':18},'count':2}," +
                " {'region':{'x':9,'y':18},'count':2}," +
                " {'region':{'x':10,'y':18},'count':2}," +
                " {'region':{'x':11,'y':18},'count':2}," +
                " {'region':{'x':12,'y':18},'count':2}," +
                " {'region':{'x':13,'y':18},'count':2}," +
                " {'region':{'x':14,'y':18},'count':2}," +
                " {'region':{'x':15,'y':18},'count':2}," +
                " {'region':{'x':16,'y':18},'count':2}," +
                " {'region':{'x':17,'y':18},'count':2}," +
                " {'region':{'x':18,'y':18},'count':1}]']", single1);

        assertBoardData("{'current':0,'lastPassed':-1,'multiple':false,'scores':true,'total':1}",
                "{'x':0,'y':0}",
                true,
                "['║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "└───────────────'," +
                "'-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺B-------------" +
                "-☺--------------" +
                "-☺--------------" +
                "-☺--------------" +
                "----------------'," +
                "'[{'region':{'x':1,'y':15},'count':2}," +
                " {'region':{'x':1,'y':14},'count':2}," +
                " {'region':{'x':1,'y':13},'count':2}," +
                " {'region':{'x':1,'y':12},'count':2}," +
                " {'region':{'x':1,'y':11},'count':2}," +
                " {'region':{'x':1,'y':10},'count':2}," +
                " {'region':{'x':1,'y':9},'count':2}," +
                " {'region':{'x':1,'y':8},'count':2}," +
                " {'region':{'x':1,'y':7},'count':2}," +
                " {'region':{'x':1,'y':6},'count':2}," +
                " {'region':{'x':1,'y':5},'count':2}," +
                " {'region':{'x':1,'y':4},'count':2}," +
                " {'region':{'x':1,'y':3},'count':2}," +
                " {'region':{'x':1,'y':2},'count':2}," +
                " {'region':{'x':1,'y':1},'count':1}]']", single2);
    }

    private void assertBoardData(String levelProgress, String heroes, boolean onlyMyName, String levels, Single single) {
        JSONObject json = single.getBoardAsString();

        assertEquals(levelProgress,
                JsonUtils.toStringSorted(json.get("levelProgress")).replace('"', '\''));

        assertEquals(heroes,
                JsonUtils.toStringSorted(json.get("offset")).replace('"', '\''));

        assertEquals(levels,
                JsonUtils.toStringSorted(json.get("layers")).replace('"', '\''));

        assertEquals(true,
                json.getBoolean("showName"));

        assertEquals(onlyMyName,
                json.getBoolean("onlyMyName"));
    }

    @Ignore
    @Test
    public void shouldRemoveOnePlayerFromMultiple() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertF(single1, "[{'region':{'x':1,'y':2},'count':10}]");
        assertF(single2, "[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero1(1, 2).right();
        hero2(1, 2).right();
        single1.tick();
        single2.tick();

        single1.tick();
        single2.tick();

        hero1(2, 2).right();
        hero2(2, 2).down();
        single1.tick();
        single2.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "-☻--" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--☻-" +
                "-☺--" +
                "----");

        // when
        single2.destroy();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--☻-" +
                "----" +
                "----");
    }

    @Ignore
    @Test
    public void shouldChangeLevelToSingleFromMultiple_thenOtherPlayerShouldNotHide() {
        // given
        givenFl("╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        // when
        hero1(1, 2).right();
        hero2(1, 2).right();
        single1.tick();
        single2.tick();

        single1.tick();
        single2.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero2(1, 2).loadLevel(0);
        single1.tick();
        single2.tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║1.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║1E│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");
    }

}
