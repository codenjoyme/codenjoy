package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
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
import com.codenjoy.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.expansion.services.SettingsWrapper;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.junit.Before;
import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    public static final int PLAYER17 = 16;
    public static final int PLAYER18 = 17;
    public static final int PLAYER19 = 18;
    public static final int PLAYER20 = 19;
    public static final int PLAYER21 = 20;
    public static final int PLAYER22 = 21;
    public static final int PLAYER23 = 22;
    public static final int PLAYER24 = 23;
    public static final int PLAYER25 = 24;
    public static final int PLAYER26 = 25;
    public static final int PLAYER27 = 26;
    public static final int PLAYER28 = 27;
    public static final int PLAYER29 = 28;
    public static final int PLAYER30 = 29;
    public static final int PLAYER31 = 30;
    public static final int PLAYER32 = 31;
    public static final int PLAYER33 = 32;
    public static final int PLAYER34 = 33;
    public static final int PLAYER35 = 34;
    public static final int PLAYER36 = 35;

    public int INCREASE = 2;
    public int MOVE = 1;

    protected Dice dice;
    protected List<EventListener> listeners;
    protected List<Single> games;
    protected List<PlayerHero> heroes;

    protected Ticker ticker;
    private int size = LevelsTest.LEVEL_SIZE;

    private Expansion current;
    private GameRunner gameRunner;
    private int levelNumber;

    @Before
    public void setup() {
        dice = mock(Dice.class);

        gameRunner = new GameRunner();
        gameRunner.setDice(dice);
        levelNumber = 0;

        listeners = new LinkedList<>();
        games = new LinkedList<>();
        heroes = new LinkedList<>();
        ticker = new Ticker();
        SettingsWrapper.setup()
                .leaveForceCount(1)
                .regionsScores(0)
                .roundTicks(10000)
                .waitingOthers(false)
                .defenderHasAdvantage(false)
                .shufflePlayers(false);
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
        SettingsWrapper.single(boards);
    }

    protected void givenForces(String forces, String layer2) {
        IField current = (IField) games.get(PLAYER1).getField();
        LevelImpl level = (LevelImpl) current.getCurrentLevel();
        level.fillForces(layer2, heroes.toArray(new Hero[0]));
        level.fillForcesCount(forces);
    }

    protected void createPlayers(int count) {
        for (int i = 0; i < count; i++) {
            createOneMorePlayer();
        }
    }

    protected void frameworkShouldGoNextLevelForWinner(int player) {
        Single game = games.get(player);
        assertEquals(true, game.isWin());

        createNewLevelIfNeeded();

        game.getField().remove(game.getPlayer());

        game.on(current);
        game.newGame();

        heroes.set(player, game.getPlayer().getHero());
    }

    protected void createOneMorePlayer() {
        createNewLevelIfNeeded();

        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        String playerName = String.format("demo%s@codenjoy.com", games.size() + 1);
        Player player = (Player) gameRunner.createPlayer(listener, playerName);
        Single game = new Single(player, gameRunner.getPrinterFactory());
        game.on(current);
        game.newGame();
        games.add(game);

        heroes.add(game.getPlayer().getHero());
    }

    private void createNewLevelIfNeeded() {
        if ((current == null || current.freeBases() == 0)
                && levelNumber < gameRunner.getMultiplayerType().getLevelsCount())
        {
            current = (Expansion) gameRunner.createGame(levelNumber);
            levelNumber++;
        }
    }

    protected void tickAll() {
        for (Single single : games) {
            if (single != null) {
                single.getField().tick();
            }
        }
    }

    // делает удобным перемещение героя, что очень надо для этого легаси теста
    @NotNull
    private Hero getOnlyMovingJoystick(Single single, final int x, final int y) {
        final Hero hero = (Hero) single.getJoystick();
        final Point pt = pt(x, y);
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
        };
    }

    protected Hero hero(int index, int x, int y) {
        return getOnlyMovingJoystick(single(index), x, y);
    }

    public Hero hero(int index) {
        return (Hero) single(index).getJoystick();
    }

    protected Single single(int index) {
        return games.get(index);
    }

    protected void destroy(int player) {
        Game game = games.get(player);
        game.getField().remove(game.getPlayer());
        games.set(player, null);
    }

    protected void assertL(String expected, int index) {
        Single single = single(index);
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
        Single single = single(index);
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(getLayer(single, 1)));
    }

    protected void assertF(String expected, int index) {
        Single single = single(index);
        assertEquals(expected,
                TestUtils.injectNN(getForces(single)));
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
        JSONObject json = getLayer(index);

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
        JSONObject json = getLayer(index);
        assertEquals(JsonUtils.clean(JsonUtils.toStringSorted(expected)),
                JsonUtils.clean(JsonUtils.toStringSorted(json)));
    }

    protected JSONObject getLayer(int index) {
        return (JSONObject) single(index).getBoardAsString();
    }
}
