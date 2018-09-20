package com.epam.dojo.icancode.model;

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
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.services.Events;
import com.epam.dojo.icancode.services.GameRunner;
import com.epam.dojo.icancode.services.Levels;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Single single1;
    private Single single2;
    private List<ILevel> singles1;
    private List<ILevel> singles2;
    private ICanCode gameMultiple;

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
        singles1 = createLevels(strings);
        singles2 = createLevels(strings);

        ILevel levelMultiple = createLevels(Arrays.asList(multiple)).get(0);
        gameMultiple = new ICanCode(levelMultiple, dice, ICanCode.MULTIPLE);

        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);
        GameRunner gameRunner = new GameRunner();

        GamePlayer player1 = gameRunner.createPlayer(listener1, null);
        GamePlayer player2 = gameRunner.createPlayer(listener2, null);

        MultiplayerType type = MultiplayerType.TRAINING.apply(boards.length - 1);
        single1 = new Single(player1, gameRunner.getPrinterFactory(), type);
        player1LoadLevel(0);

        single2 = new Single(player2, gameRunner.getPrinterFactory(), type);
        player2LoadLevel(0);
    }

    private Hero hero1() {
        return (Hero)single1.getJoystick();
    }

    private Hero hero2() {
        return (Hero)single2.getJoystick();
    }

    private List<ILevel> createLevels(Collection<String> boards) {
        List<ILevel> levels = new LinkedList<>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    private void assertL(Single single, String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getLayer(single, 0)));
    }

    private String getLayer(Single single, int index) {
        return (String)((JSONObject)single.getBoardAsString()).getJSONArray("layers").get(index);
    }

    private void assertE(Single single, String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getLayer(single, 1)));
    }

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        // when
        hero1().right();
        tick();

        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().down();
        tick();

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().down();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero1().right();
        tick();

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when
        tick();

        // then
        verifyNoMoreInteractions(listener1);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldSeveralPlayersCollectionAtLastLevel() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        // when
        hero1().right();
        tick(single1);
        tick(single2);

        verify(listener1).event(Events.WIN(0));
        reset(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero2().right();
        tick(single1); // goes multiple
        tick(single2);

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when
        hero1().down();
        tick(single1);
        tick(single2); // goes multiple

        // then
        verifyNoMoreInteractions(listener1);
        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-X--" +
                "-☺--" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "-X--" +
                "----");

        // when
        hero1().right(); // finished
        hero2().right();
        tick(single1);
        tick(single2);

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--X-" +
                "--☺-" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--☺-" +
                "--X-" +
                "----");

        // when
        hero2().down();
        tick(single1); // started
        tick(single2); // finished

        // then
        verifyNoMoreInteractions(listener1);
        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "--X-" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-X--" +
                "--☺-" +
                "----");

        // when
        tick(single1);
        tick(single2); // started

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().down();
        hero2().right();
        tick(single1);
        tick(single2);

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--X-" +
                "-☺--" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--☺-" +
                "-X--" +
                "----");

        // when
        hero1().right();
        hero2().down();
        tick(single1); // finished
        tick(single2); // finished

        // then
        verify(listener1).event(Events.WIN(0));
        reset(listener1);

        verify(listener2).event(Events.WIN(0));
        reset(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when
        tick(single1); // started
        tick(single2); // started

        // then
        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);

        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");
    }

    int count = 0;
    private void tick(Single single) {
        ICanCode field = (ICanCode)single.getField();
        if (single.isGameOver()) {
            if (single.isWin()) {
                nextlevel(single, field);
            }
            single.newGame();
        }


        if (field != gameMultiple) {
            field.tick();
        } else {
            int countOnMultiple = (single1.getField() == gameMultiple)?1:0;
            countOnMultiple += (single2.getField() == gameMultiple)?1:0;

            if (++count % countOnMultiple == 0) { // miltiple надо тикать 1 раз
                field.tick();
                count = 0;
            }
        }
    }

    private void nextlevel(Single single, ICanCode field) {
        List<ILevel> levels = this.singles1;
        if (levels.indexOf(field.getLevel()) == -1) {
            levels = this.singles2;
            if (levels.indexOf(field.getLevel()) == -1) {
                if (field == gameMultiple) {
                    return;
                } else {
                    throw new IllegalStateException();
                }
            }
        }

        int index = levels.indexOf(field.getLevel());
        loadLevel(single, levels, index + 1);
    }

    private boolean loadLevel(Single single, List<ILevel> levels, int index) {
        LevelProgress progress = single.getProgress();
        progress.change(index, index - 1);
        if (!progress.isValid()) {
            return false;
        }

        if (index == levels.size()) {
            single.on(gameMultiple);
        } else {
            ICanCode gameSingle = new ICanCode(levels.get(index), dice, ICanCode.SINGLE);
            single.on(gameSingle);
        }
        return true;
    }

    @Test
    public void shouldDrawLaserDeath() {
        // given
        givenFl("╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        // go to next level
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        hero2().right();
        tick(single1);
        tick(single2);

        reset(listener1);
        reset(listener2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X☺--" +
                "-----" +
                "-----" +
                "-----");

        // when
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        tick(single1); // laser machine is ready
        tick(single2);

        tick(single1); // fire
        tick(single2);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺X--" +
                "-↑↑--" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X☺--" +
                "-↑↑--" +
                "-----" +
                "-----");

        // when
        tick(single1);
        tick(single2);

        // then
        verify(listener1).event(Events.LOOSE());
        verify(listener2).event(Events.LOOSE());

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☻&--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-&☻--" +
                "-----" +
                "-----" +
                "-----");

        // when
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero1().right(); // other hero goes right
        tick(single2);
        tick(single1);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-X☺--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║˄˄E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    @Test
    public void shouldDrawOverBox() {
        // given
        givenFl("╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║S..│" +
                "║BB.│" +
                "║..E│" +
                "└───┘");

        // go to next level
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        hero2().right();
        tick(single1);
        tick(single2);

        reset(listener1);
        reset(listener2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺X--" +
                "-BB--" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X☺--" +
                "-BB--" +
                "-----" +
                "-----");
        // when
        hero1().jump();
        hero1().down();
        hero2().jump();
        hero2().down();
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-----" +
                "-№%--" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-----" +
                "-%№--" +
                "-----" +
                "-----");

        // when
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-----" +
                "-BB--" +
                "-☺X--" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-----" +
                "-BB--" +
                "-X☺--" +
                "-----");
    }

    @Test
    public void shouldDrawJump() {
        // given
        givenFl("╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        // go to next level
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        hero2().right();
        tick(single1);
        tick(single2);

        reset(listener1);
        reset(listener2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X☺--" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero1().act();
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-*X--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-^☺--" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero2().act();
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺^--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X*--" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldFallingInHoleJump() {
        // given
        givenFl("╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        // go to next level
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        hero2().right();
        tick(single1);
        tick(single2);

        reset(listener1);
        reset(listener2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X☺--" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero1().down();
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "--X--" +
                "-o---" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "--☺--" +
                "-x---" +
                "-----" +
                "-----");

        // when
        hero2().down();
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺---" +
                "--x--" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-X---" +
                "--o--" +
                "-----" +
                "-----");

        // when
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----");

        // when
        hero1().right();
        tick(single1);
        tick(single2);

        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "-X☺--" +
                "-----" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║OO.│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘",
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘",
                "╔══┐" + // multiple
                "║E.│" +
                "║S.│" +
                "└──┘"
        );

        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when done 1 level - go to 2 (single)
        hero1().right();
        tick();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when done 2 level - go to 3 (single)
        hero1().down();
        tick();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when done 3 level - go to 4 (multiple)
        hero1().left();
        tick();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when done 4 level - start 4 again (multiple)
        hero1().up();
        tick();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when done 4 level - start 4 again multiple)
        hero1().up();
        tick();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");
    }

    private void tick() {
        tick(single1);
        tick(single2);
    }

    @Test
    public void shouldSelectLevelWhenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone();

        // when try to change level 1  - success from multiple to single
        player1LoadLevel(0);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level 2  - success from single to single
        player1LoadLevel(1);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when try to change level 3  - success from single to single
        player1LoadLevel(2);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 4 - success from single to multiple
        player1LoadLevel(3);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 500 - fail
        hero1().right();
        tick();
        player1LoadLevel(500);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 2 - success from multiple to single
        player1LoadLevel(1);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

    }

    void player1LoadLevel(int index) {
        if (loadLevel(single1, singles1, index)) {
            single1.newGame();
        }
    }

    void player2LoadLevel(int index) {
        if (loadLevel(single2, singles2, index)) {
            single2.newGame();
        }
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        player1LoadLevel(3);
        tick();
        tick();
        hero1().right();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 3 (previous) - success
        player1LoadLevel(2);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");
    }

    @Test
    public void shouldResetOnMultipleWillResetOnlyMultipleLevel() {
        // given
        shouldAllLevelsAreDone();

        // when
        hero1().reset();
        tick();
        tick();
        hero1().right();
        tick();

        // then
        assertL(single1, // still multiple
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero1().reset();
        tick();

        // then
        assertL(single1, // still multiple
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE(single1, // but at start
                "----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Test
    public void testGetBoardAsString() {
        // given
        givenFl("╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘",
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        // when then
        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':0,'y':0}",
                "['╔═══┐" +
                "║SE.│" +
                "║...│" +
                "║...│" +
                "└───┘'," +
                "'-----" +
                "-☺---" +
                "-----" +
                "-----" +
                "-----']", single1);

        // when then
        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':0,'y':0}",
                "['╔═══┐" +
                "║SE.│" +
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
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        // then select different way
        hero1().right();
        hero2().down();
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single1,
                "-----" +
                "--☺--" +
                "-X---" +
                "-----" +
                "-----");

        assertL(single2,
                "╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘");

        assertE(single2,
                "-----" +
                "--X--" +
                "-☺---" +
                "-----" +
                "-----");

        // when then
        assertBoardData("{'current':1,'lastPassed':0,'total':1}",
                "{'x':0,'y':0}",
                "['╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘'," +
                "'-----" +
                "--☺--" +
                "-X---" +
                "-----" +
                "-----']",
                single1);

        // when then
        assertBoardData("{'current':1,'lastPassed':0,'total':1}",
                "{'x':0,'y':0}",
                "['╔═══┐" +
                "║S..│" +
                "║...│" +
                "║..E│" +
                "└───┘'," +
                "'-----" +
                "--X--" +
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
                "║S.................│" +
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
        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':0,'y':4}",
                "['╔═══════════════" +
                "║S.............." +
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
                "--B-------------']", single1);

        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':0,'y':4}",
                "['╔═══════════════" +
                "║S.............." +
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
                "--B-------------']", single2);

        // when
        for (int i = 0; i < 17; i++) {
            hero1().right();
            hero2().down();
            tick(single1);
            tick(single2);
        }

        // then
        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':4,'y':4}",
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
                "--------------☺-" +
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
                "----------------']", single1);

        assertBoardData("{'current':0,'lastPassed':-1,'total':1}",
                "{'x':0,'y':0}",
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
                "'----------------" +
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
                "--B-------------" +
                "----------------" +
                "----------------" +
                "-☺--------------" +
                "----------------']", single2);
    }

    private void assertBoardData(String levelProgress, String heroes, String levels, Single single) {
        JSONObject json = (JSONObject) single.getBoardAsString();

        assertEquals(levelProgress,
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("levelProgress").toString())));

        assertEquals(heroes,
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("offset").toString())));

        assertEquals(levels,
                JsonUtils.clean(json.get("layers").toString()));
    }

    @Test
    public void shouldRemoveOnePlayerFromMultiple() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        // when
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        hero1().right();
        hero2().down();
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "-X--" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "--X-" +
                "-☺--" +
                "----");

        // when
        single2.close();

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");


        try {
            assertL(single2,
                    "╔══┐" +
                    "║S.│" +
                    "║.E│" +
                    "└──┘");

            assertE(single2,
                    "----" +
                    "--X-" +
                    "----" +
                    "----");
            fail();
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }
    }

    @Test
    public void shouldChangeLevelToSingleFromMultiple_thenOtherPlayerShouldNotHide() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        // when
        hero1().right();
        hero2().right();
        tick(single1);
        tick(single2);

        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        player2LoadLevel(0);
        tick(single1);
        tick(single2);

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-☺--" +
                "----" +
                "----");
    }

}
