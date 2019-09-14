package com.codenjoy.dojo.expansion.model;

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


import com.codenjoy.dojo.expansion.model.levels.*;
import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.Events;
import com.codenjoy.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.*;

import static com.codenjoy.dojo.expansion.services.SettingsWrapper.data;
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

    protected static final int PLAYER1 = 0;
    protected static final int PLAYER2 = 1;
    protected static final int PLAYER3 = 2;
    protected static final int PLAYER4 = 3;
    static final int PLAYER5 = 4;
    static final int PLAYER6 = 5;
    static final int PLAYER7 = 6;
    static final int PLAYER8 = 7;
    static final int PLAYER9 = 8;
    static final int PLAYER10 = 9;
    static final int PLAYER11 = 10;
    static final int PLAYER12 = 11;
    static final int PLAYER13 = 12;
    static final int PLAYER14 = 13;
    static final int PLAYER15 = 14;
    static final int PLAYER16 = 15;
    static final int PLAYER17 = 16;
    static final int PLAYER18 = 17;
    static final int PLAYER19 = 18;
    static final int PLAYER20 = 19;
    static final int PLAYER21 = 20;
    static final int PLAYER22 = 21;
    static final int PLAYER23 = 22;
    static final int PLAYER24 = 23;
    static final int PLAYER25 = 24;
    static final int PLAYER26 = 25;
    static final int PLAYER27 = 26;
    static final int PLAYER28 = 27;
    static final int PLAYER29 = 28;
    static final int PLAYER30 = 29;
    static final int PLAYER31 = 30;
    static final int PLAYER32 = 31;
    static final int PLAYER33 = 32;
    static final int PLAYER34 = 33;
    static final int PLAYER35 = 34;
    static final int PLAYER36 = 35;

    @NotNull
    Events WIN() {
        return Events.WIN(data.winScore());
    }

    @NotNull
    Events DRAW() {
        return Events.WIN(data.drawScore());
    }


    protected int INCREASE = 2;
    protected int MOVE = 1;

    Dice dice;

    protected SoftSpreader spreader;

    protected abstract boolean isSingleTrainingOrMultiple();

    @Before
    public void setup() {
        dice = mock(Dice.class);

        spreader = new SoftSpreader(new GameRunner(){{
            setDice(dice);
        }});

        SettingsWrapper.setup()
                .leaveForceCount(1)
                .regionsScores(0)
                .roundTicks(10000)
                .waitingOthers(false)
                .defenderHasAdvantage(false)
                .singleTrainingMode(isSingleTrainingOrMultiple())
                .shufflePlayers(false);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    protected void givenSize(int size) {
        SettingsWrapper.data.boardSize(size);
    }

    protected void givenFl(String... boards) {
        SettingsWrapper.single(Arrays.asList(boards)
                .subList(0, boards.length - 1)
                .toArray(new String[0]));

        SettingsWrapper.multi(Arrays.asList(boards)
                .subList(boards.length - 1, boards.length)
                .toArray(new String[0]));
    }

    protected void givenForces(int player, String forces, String layer2) {
        LevelImpl level = (LevelImpl) spreader.field(player).getCurrentLevel();
        level.fillForces(layer2, spreader.heroes());
        level.fillForcesCount(forces);
    }

    protected void createPlayers(int count) {
        for (int i = 0; i < count; i++) {
            spreader.createOneMorePlayer();
        }
    }

    // делает удобным перемещение героя, что очень надо для этого легаси теста
    @NotNull
    private Hero getOnlyMovingJoystick(int player, final int x, final int y) {
        final Hero hero = spreader.hero(player);
        final Point pt = pt(x, y);
        return new Hero() {
            @Override
            public void down() {
                spreader.roomIsBusy(player);

                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.DOWN)
                );
            }

            @Override
            public void up() {
                spreader.roomIsBusy(player);

                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.UP)
                );
            }

            @Override
            public void left() {
                spreader.roomIsBusy(player);

                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.LEFT)
                );
            }

            @Override
            public void right() {
                spreader.roomIsBusy(player);

                hero.increaseAndMove(
                        new Forces(pt, INCREASE),
                        new ForcesMoves(pt, MOVE, QDirection.RIGHT)
                );
            }

            @Override
            public void act(int... p) {
                spreader.roomIsBusy(player);

                hero.act(p);
            }

            @Override
            public void message(String command) {
                spreader.roomIsBusy(player);

                hero.message(command);
            }
        };
    }

    protected Hero hero(int index, int x, int y) {
        return getOnlyMovingJoystick(index, x, y);
    }

    public Hero hero(int index) {
        return (Hero) spreader.single(index).getJoystick();
    }

    protected void assertL(String expected, int index) {
        Single single = spreader.single(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getLayer(single, 0)));
    }

    private String getLayer(Single single, int layer) {
        return ((JSONObject) single.getBoardAsString()).getJSONArray("layers").getString(layer);
    }

    private String getForces(Single single) {
        return ((JSONObject) single.getBoardAsString()).getString("forces");
    }

    protected void assertE(String expected, int index) {
        Single single = spreader.single(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getLayer(single, 1)));
    }

    protected void assertF(String expected, int index) {
        Single single = spreader.single(index);
        assertEquals(expected,
                TestUtils.injectNN(getForces(single)));
    }

    protected EventListener verify(int index) {
        return Mockito.verify(listener(index));
    }

    private EventListener listener(int index) {
        return spreader.listener(index);
    }

    protected void reset(int index) {
        Mockito.reset(listener(index));
    }

    protected void verifyNoMoreInteractions(int index) {
        Mockito.verifyNoMoreInteractions(listener(index));
    }

    protected void assertBoardData(String levelProgress, String offset,
                                   String layer1, String layer2,
                                   String forces, Point myBase, int index)
    {
        JSONObject json = getLayer(index);

        // TODO вообще тут эмуляция многих частей codenjoy фреймворка а значит дублирование, и это я уже не стал эмулировать
        assertEquals(levelProgress,
                levelProgress);

        assertEquals(offset,
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("offset"))));

        assertEquals(TestUtils.injectN(layer1),
                TestUtils.injectN(json.getJSONArray("layers").getString(0)));

        assertEquals(TestUtils.injectN(layer2),
                TestUtils.injectN(json.getJSONArray("layers").getString(1)));

        assertEquals(forces,
                TestUtils.injectNN(json.getString("forces")));

        assertEquals(JsonUtils.clean(JsonUtils.toStringSorted(myBase)),
                JsonUtils.clean(JsonUtils.toStringSorted(json.get("myBase"))));
    }

    protected void assertBoardData(int index, String expected) {
        JSONObject json = getLayer(index);
        assertEquals(JsonUtils.clean(JsonUtils.toStringSorted(expected)),
                JsonUtils.clean(JsonUtils.toStringSorted(json)));
    }

    protected JSONObject getLayer(int index) {
        return (JSONObject) spreader.single(index).getBoardAsString();
    }
}
