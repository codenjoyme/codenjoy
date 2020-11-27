package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.icancode.services.GameRunner;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER1;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER2;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER3;
import static com.codenjoy.dojo.utils.TestUtils.injectN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Single single1;
    private Single single2;
    private List<Level> singles1;
    private List<Level> singles2;
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

    void givenFl(String... boards) {
        givenFl(viewSize(boards[0]), boards);
    }

    int viewSize(String board) {
        return (int)Math.sqrt(board.length());
    }

    void givenFl(int viewSize, String... boards) {
        Levels.VIEW_SIZE = viewSize;
        Deque<String> strings = new LinkedList<>(Arrays.asList(boards));
        String multiple = strings.removeLast();
        singles1 = createLevels(strings);
        singles2 = createLevels(strings);

        Level levelMultiple = createLevels(Arrays.asList(multiple)).get(0);
        gameMultiple = new ICanCode(levelMultiple, dice, ICanCode.CONTEST);

        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);
        GameRunner gameRunner = new GameRunner();

        GamePlayer player1 = gameRunner.createPlayer(listener1, null);
        GamePlayer player2 = gameRunner.createPlayer(listener2, null);

        MultiplayerType type = MultiplayerType.TRAINING.apply(boards.length);
        single1 = new Single(player1, gameRunner.getPrinterFactory(), type);
        player1TryLoadLevel(LevelProgress.levelsStartsFrom1);

        single2 = new Single(player2, gameRunner.getPrinterFactory(), type);
        player2TryLoadLevel(LevelProgress.levelsStartsFrom1);
    }

    private Hero hero1() {
        return (Hero)single1.getJoystick();
    }

    private Hero hero2() {
        return (Hero)single2.getJoystick();
    }

    private List<Level> createLevels(Collection<String> boards) {
        List<Level> levels = new LinkedList<>();
        for (String board : boards) {
            Level level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    private void assertL(Single single, String expected) {
        assertA(single, expected, LAYER1);
    }

    private void assertA(Single single, String expected, int index) {
        assertEquals(injectN(expected),
                injectN(getLayer(single, index)));
    }

    private String getLayer(Single single, int index) {
        return (String)((JSONObject)single.getBoardAsString()).getJSONArray("layers").get(index);
    }

    private void assertE(Single single, String expected) {
        assertA(single, expected, LAYER2);
    }

    private void assertF(Single single, String expected) {
        assertA(single, expected, LAYER3);
    }

    @Test
    public void shouldNextLevel_whenFinishCurrent() {
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

    @Test
    public void shouldSeveralPlayersAtOneField_checkDrawing() {
        // given
        shouldSeveralPlayersCollectionAtLastLevel();

        allAtFloor();

        // when
        hero2().jump();
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

        assertF(single1,
                "----" +
                "-^--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "-X--" +
                "----" +
                "----");


        assertF(single2,
                "----" +
                "-*--" +
                "----" +
                "----");

        // when
        tick();

        // then
        allAtFloor();

        // when
        hero1().jump();
        hero2().jump();
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
                "----" +
                "----");

        assertF(single1,
                "----" +
                "-*--" +
                "----" +
                "----");

        assertL(single2,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single2,
                "----" +
                "----" +
                "----" +
                "----");


        assertF(single2,
                "----" +
                "-*--" +
                "----" +
                "----");

        // when
        tick();

        // then
        allAtFloor();

        // when
        hero1().jump();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║S.│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-X--" +
                "----" +
                "----");

        assertF(single1,
                "----" +
                "-*--" +
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


        assertF(single2,
                "----" +
                "-^--" +
                "----" +
                "----");

        // when
        tick();

        // then
        allAtFloor();

    }

    private void allAtFloor() {
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

        assertF(single1,
                "----" +
                "----" +
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


        assertF(single2,
                "----" +
                "----" +
                "----" +
                "----");
    }

    int count = 0;
    private void tick(Single single) {
        ICanCode field = (ICanCode)single.getField();
        if (single.isGameOver()) {
            if (single.isWin()) {
                nextLevel(single, field);
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

    private void nextLevel(Single single, ICanCode field) {
        List<Level> levels = this.singles1;
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
        int level = index + LevelProgress.levelsStartsFrom1;
        level++;
        loadLevel(single, levels, level, false);
    }

    private boolean loadLevel(Single single, List<Level> levels, int level, boolean ask) {
        LevelProgress progress = single.getProgress();
        if (ask) {
            if (!progress.canChange(level)) {
                return false;
            }
        }
        progress.change(level, Math.max(progress.getPassed(), level - 1));
        if (!progress.isValid()) {
            return false;
        }

        if (level == levels.size() + 1) {
            single.on(gameMultiple);
        } else {
            int index = level - LevelProgress.levelsStartsFrom1;
            ICanCode gameSingle = new ICanCode(levels.get(index), dice, ICanCode.TRAINING);
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
                "-BB--" +
                "-----" +
                "-----");

        assertF(single1,
                "-----" +
                "-----" +
                "-*^--" +
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
                "-BB--" +
                "-----" +
                "-----");

        assertF(single2,
                "-----" +
                "-----" +
                "-^*--" +
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

        assertF(single1,
                "-----" +
                "-----" +
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

        assertF(single2,
                "-----" +
                "-----" +
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
                "--X--" +
                "-----" +
                "-----" +
                "-----");

        assertF(single1,
                "-----" +
                "-*---" +
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
                "--☺--" +
                "-----" +
                "-----" +
                "-----");

        assertF(single2,
                "-----" +
                "-^---" +
                "-----" +
                "-----" +
                "-----");

        // when
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
                "-☺X--" +
                "-----" +
                "-----" +
                "-----");

        assertF(single1,
                "-----" +
                "-----" +
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

        assertF(single2,
                "-----" +
                "-----" +
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
                "-☺---" +
                "-----" +
                "-----" +
                "-----");

        assertF(single1,
                "-----" +
                "--^--" +
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
                "-X---" +
                "-----" +
                "-----" +
                "-----");

        assertF(single2,
                "-----" +
                "--*--" +
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
    public void shouldSelectLevel_whenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone();

        // when try to change level 1  - success from multiple to single
        player1TryLoadLevel(1);
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
        player1TryLoadLevel(2);
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
        player1TryLoadLevel(3);
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
        player1TryLoadLevel(4);
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
        player1TryLoadLevel(500);
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
        player1TryLoadLevel(2);
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

    void player1TryLoadLevel(int level) {
        if (loadLevel(single1, singles1, level, true)) {
            single1.newGame();
        }
    }

    void player2TryLoadLevel(int index) {
        if (loadLevel(single2, singles2, index, true)) {
            single2.newGame();
        }
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        player1TryLoadLevel(4);
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
        player1TryLoadLevel(3);
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
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':3}\n" +
                "\n" +
                "╔═══┐\n" +
                "║SE.│\n" +
                "║...│\n" +
                "║...│\n" +
                "└───┘\n" +
                "\n" +
                "-----\n" +
                "-☺---\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n", single1);

        // when then
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':3}\n" +
                "\n" +
                "╔═══┐\n" +
                "║SE.│\n" +
                "║...│\n" +
                "║...│\n" +
                "└───┘\n" +
                "\n" +
                "-----\n" +
                "-☺---\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n", single2);

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
        assertBoardData("levelProgress: {'current':2,'lastPassed':1,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':2,'y':3}\n" +
                "\n" +
                "╔═══┐\n" +
                "║S..│\n" +
                "║...│\n" +
                "║..E│\n" +
                "└───┘\n" +
                "\n" +
                "-----\n" +
                "--☺--\n" +
                "-X---\n" +
                "-----\n" +
                "-----\n" +
                "\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n", single1);

        // when then
        assertBoardData("levelProgress: {'current':2,'lastPassed':1,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':2}\n" +
                "\n" +
                "╔═══┐\n" +
                "║S..│\n" +
                "║...│\n" +
                "║..E│\n" +
                "└───┘\n" +
                "\n" +
                "-----\n" +
                "--X--\n" +
                "-☺---\n" +
                "-----\n" +
                "-----\n" +
                "\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n" +
                "-----\n", single2);
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
        givenFl(Levels.VIEW_SIZE_TESTING,
                field, field);

        // when then
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':4}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':14}\n" +
                "\n" +
                "╔═══════════════\n" +
                "║S..............\n" +
                "║...............\n" +
                "║....┌──╗.......\n" +
                "║....│  ║.......\n" +
                "║..┌─┘  └─╗.....\n" +
                "║..│      ║.....\n" +
                "║..│      ║.....\n" +
                "║..╚═┐  ╔═╝.....\n" +
                "║....│  ║.......\n" +
                "║....╚══╝.......\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "\n" +
                "----------------\n" +
                "-☺--------------\n" +
                "---------------B\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "--B-------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single1);

        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':4}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':14}\n" +
                "\n" +
                "╔═══════════════\n" +
                "║S..............\n" +
                "║...............\n" +
                "║....┌──╗.......\n" +
                "║....│  ║.......\n" +
                "║..┌─┘  └─╗.....\n" +
                "║..│      ║.....\n" +
                "║..│      ║.....\n" +
                "║..╚═┐  ╔═╝.....\n" +
                "║....│  ║.......\n" +
                "║....╚══╝.......\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "\n" +
                "----------------\n" +
                "-☺--------------\n" +
                "---------------B\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "--B-------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single2);

        // when
        for (int i = 0; i < 17; i++) {
            hero1().right();
            hero2().down();
            tick(single1);
            tick(single2);
        }

        // then
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':4,'y':4}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':14,'y':14}\n" +
                "\n" +
                "═══════════════┐\n" +
                "...............│\n" +
                "...............│\n" +
                ".┌──╗..........│\n" +
                ".│  ║..........│\n" +
                "─┘  └─╗........│\n" +
                "      ║........│\n" +
                "      ║........│\n" +
                "═┐  ╔═╝........│\n" +
                ".│  ║..........│\n" +
                ".╚══╝..........│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "\n" +
                "----------------\n" +
                "--------------☺-\n" +
                "-----------B----\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single1);

        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':1,'y':1}\n" +
                "\n" +
                "║....│  ║.......\n" +
                "║..┌─┘  └─╗.....\n" +
                "║..│      ║.....\n" +
                "║..│      ║.....\n" +
                "║..╚═┐  ╔═╝.....\n" +
                "║....│  ║.......\n" +
                "║....╚══╝.......\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "└───────────────\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "--B-------------\n" +
                "----------------\n" +
                "----------------\n" +
                "-☺--------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single2);

        // when
        for (int i = 0; i < 10; i++) {
            hero1().down();
            hero2().right();
            tick(single1);
            tick(single2);
        }
        for (int i = 0; i < 10; i++) {
            hero1().left();
            hero2().up();
            tick(single1);
            tick(single2);
        }

        // then
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':4,'y':4}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':4,'y':4}\n" +
                "\n" +
                "═══════════════┐\n" +
                "...............│\n" +
                "...............│\n" +
                ".┌──╗..........│\n" +
                ".│  ║..........│\n" +
                "─┘  └─╗........│\n" +
                "      ║........│\n" +
                "      ║........│\n" +
                "═┐  ╔═╝........│\n" +
                ".│  ║..........│\n" +
                ".╚══╝..........│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "...............│\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "-----------B----\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----☺-----------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single1);

        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':0}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':11,'y':11}\n" +
                "\n" +
                "║....│  ║.......\n" +
                "║..┌─┘  └─╗.....\n" +
                "║..│      ║.....\n" +
                "║..│      ║.....\n" +
                "║..╚═┐  ╔═╝.....\n" +
                "║....│  ║.......\n" +
                "║....╚══╝.......\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "└───────────────\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "-----------☺----\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "--B-------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single2);

        // when
        // one more time should move view
        hero1().left();
        hero2().up();
        tick(single1);
        tick(single2);

        // then
        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':3,'y':4}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':4,'y':4}\n" +
                "\n" +
                "════════════════\n" +
                "................\n" +
                "................\n" +
                "..┌──╗..........\n" +
                "..│  ║..........\n" +
                "┌─┘  └─╗........\n" +
                "│      ║........\n" +
                "│      ║........\n" +
                "╚═┐  ╔═╝........\n" +
                "..│  ║..........\n" +
                "..╚══╝..........\n" +
                "................\n" +
                "................\n" +
                "................\n" +
                "................\n" +
                "................\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "------------B---\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----☺-----------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single1);

        assertBoardData("levelProgress: {'current':1,'lastPassed':0,'total':2}\n" +
                "offset: {'x':0,'y':1}\n" +
                "levelFinished: false\n" +
                "heroPosition: {'x':11,'y':11}\n" +
                "\n" +
                "║....┌──╗.......\n" +
                "║....│  ║.......\n" +
                "║..┌─┘  └─╗.....\n" +
                "║..│      ║.....\n" +
                "║..│      ║.....\n" +
                "║..╚═┐  ╔═╝.....\n" +
                "║....│  ║.......\n" +
                "║....╚══╝.......\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "║...............\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "-----------☺----\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "--B-------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n" +
                "----------------\n", single2);
    }

    private void assertBoardData(String expected, Single single) {
        JSONObject json = (JSONObject) single.getBoardAsString();

        JSONArray layers = json.getJSONArray("layers");
        String actual = String.format(
                "levelProgress: %s\n" +
                "offset: %s\n" +
                "levelFinished: %s\n" +
                "heroPosition: %s\n\n" +
                "%s\n" +
                "%s\n" +
                "%s",
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("levelProgress").toString())),
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("offset").toString())),
                json.get("levelFinished"),
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("heroPosition").toString())),
                injectN(layers.getString(0)),
                injectN(layers.getString(1)),
                injectN(layers.getString(2))
        );

        assertEquals(expected, actual);
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
        player2TryLoadLevel(1);
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

    @Test
    public void shouldAllLevelsAreDone_case2() {
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
                "╔══┐" +
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

        // when done 1 level - go to 2
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

        // when done 2 level - go to 3
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

        // when done 3 level - go to 4
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

        // when done 4 level - start 4 again
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

        // when done 4 level - start 4 again
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

    @Test
    public void shouldChangeLevel_whenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone_case2();

        // when try to change level 1  - success
        player1TryLoadLevel(LevelProgress.levelsStartsFrom1);
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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

        // when try to change level 2  - success
        player1TryLoadLevel(2);
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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

        // when try to change level 3  - success
        player1TryLoadLevel(3);
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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
        player1TryLoadLevel(500);
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

        // when try to change level 2 - success
        player1TryLoadLevel(2);
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

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother() {
        // given
        shouldAllLevelsAreDone_case2();

        // when win on level then try to change to last - success
        player1TryLoadLevel(2);
        tick();
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseWhenGoToMultiple() {
        // given
        shouldAllLevelsAreDone_case2();

        // when win on level then try to change to last - success
        player1TryLoadLevel(3);
        tick();
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple_case2() {
        // given
        shouldAllLevelsAreDone_case2();

        // when win on level then try to change to last - success
        player1TryLoadLevel(4);
        tick();
        hero1().right();
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
                "--☺-" +
                "----");

        // when try to change level 3 (previous) - success
        player1TryLoadLevel(3);
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
    public void shouldResetLevel_whenAllLevelsAreDone() {
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
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘"
        );

        hero1().down();
        tick();

        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero1().reset();
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

        // when done 1 level - go to 2
        hero1().right();
        tick();
        tick();
        hero1().left();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().reset();
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

        // when done 2 level - go to 3
        hero1().down();
        tick();
        tick();
        hero1().up();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when
        hero1().reset();
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

        // when done 3 level - go to 4
        hero1().left();
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

        // when
        hero1().reset();
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

        // when done 4 level - start 4 again
        hero1().up();
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

        // when
        hero1().reset();
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

        // when try to change level 1  - success
        player1TryLoadLevel(1);
        tick();
        tick();
        hero1().down();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE(single1,
                "----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero1().reset();
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

        // when try to change level 2  - success
        player1TryLoadLevel(2);
        tick();
        tick();
        hero1().left();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().reset();
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

        // when try to change level 3  - success
        player1TryLoadLevel(3);
        tick();
        tick();
        hero1().up();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE(single1,
                "----" +
                "--☺-" +
                "----" +
                "----");

        // when
        hero1().reset();
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

        // when try to change level 4 - success
        player1TryLoadLevel(4);
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

        // when
        hero1().reset();
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
        tick();
        player1TryLoadLevel(500);
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

        // when
        hero1().reset();
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

        // when try to change level 2 - success
        player1TryLoadLevel(2);
        tick();
        tick();
        hero1().left();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero1().reset();
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

    @Test
    public void shouldSelectLevel_whenNotAllLevelsAreDone() {
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
                "╔══┐" +
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

        // when done level 1 - go to level 2
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

        // when try to change level to 1 - success
        player1TryLoadLevel(1);
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

        // when try to change level to 2 - success
        player1TryLoadLevel(2);
        tick();
        hero1().left();
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level to 3 - fail
        player1TryLoadLevel(3);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level 4 - fail
        player1TryLoadLevel(4);
        tick();

        // then
        assertL(single1,
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE(single1,
                "----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level to 1 - success
        player1TryLoadLevel(1);
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
    }

    @Test
    public void shouldRunEventAfterKillHero() {
        givenFl("╔════┐" +
                "║.S..│" +
                "║....│" +
                "║....│" +
                "║.E..│" +
                "└────┘");
        hero1().down();
        tick();
        hero1().down();
        tick();

        assertE(single1,
                "------" +
                "--X---" +
                "------" +
                "--☺---" +
                "------" +
                "------");

        hero1().fire();
        hero1().up();
        tick();

        assertE(single1,
                "------" +
                "--X---" +
                "--↑---" +
                "--☺---" +
                "------" +
                "------");
        tick();

        assertE(single1,
                "------" +
                "--&---" +
                "------" +
                "--☺---" +
                "------" +
                "------");
        verify(listener1).event(Events.KILL_HERO(1, true));
    }
}
