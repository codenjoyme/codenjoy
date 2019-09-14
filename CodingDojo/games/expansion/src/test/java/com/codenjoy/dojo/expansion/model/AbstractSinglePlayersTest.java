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
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.utils.JsonUtils;
import com.codenjoy.dojo.utils.TestUtils;
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

    private List<Expansion> currents;
    private GameRunner gameRunner;
    private List<Integer> levelNumbers;
    private Map<String, Integer> fullness;

    protected abstract boolean isSingleTrainingOrMultiple();

    @Before
    public void setup() {
        GameRunner.SINGLE_TRAINING_MODE = isSingleTrainingOrMultiple();

        dice = mock(Dice.class);

        gameRunner = new GameRunner();
        gameRunner.setDice(dice);
        levelNumbers = new LinkedList<>();
        fullness = new HashMap<>();
        currents = new LinkedList<>();

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

        createNewLevelIfNeeded(player);

        frameworkShouldReloadLevel(player);
    }

    protected void frameworkShouldReloadLevel(int player) {
        removeFromBoard(player);

        Single game = games.get(player);
        game.on(currents.get(player));
        game.newGame();

        heroes.set(player, game.getPlayer().getHero());
    }

    protected void createOneMorePlayer() {
        int playerIndex = games.size();
        createNewLevelIfNeeded(playerIndex);

        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        String playerName = String.format("demo%s@codenjoy.com", playerIndex + 1);
        Player player = (Player) gameRunner.createPlayer(listener, playerName);
        Single game = new Single(player, gameRunner.getPrinterFactory());
        game.on(currents.get(playerIndex));
        game.newGame();
        games.add(game);

        heroes.add(game.getPlayer().getHero());
    }

    // TODO этот ужас надо забыть как страшный сон
    // но пока что он эмулирует работу фреймфорка по принципу:
    // все начинают со своих независимых single уровней пока не доберутся до multiple (последний)
    // тогда они объединяются на одном поле, но если случится переполнение и баз свободных нет
    // тогда новому игроку ничего не останется, кроме как занять новое свободное multiple поле
    // и быть там первым
    private void createNewLevelIfNeeded(int player) {
        // это нового плеера мы пытаемся сейчас создать или игрок уже играл?
        boolean newPlayer = (player == currents.size());

        // если это новый пользователь или базы на текущем multiple уровне не свободны (или были заняты в прошлом)
        // то надо потрудиться и разместить его как-то
        if (newPlayer || fullness.get(currents.get(player).id()) == currents.get(player).allBases()) {
            // это у нас индекс уровня на котором уже multiple
            int multipleIndex = gameRunner.getMultiplayerType().getLevelsCount();
            // текущий юзер будет переходить на single или уже на multiple
            boolean willGoOnSingle = newPlayer || levelNumbers.get(player) + 1 < multipleIndex;
            // есть ли хоть один другой юзер который уже перешел на multiple?
            boolean hasAnotherMultiple = multipleIndex == levelNumbers.stream().max(Integer::compareTo).orElseGet(() -> 0);

            // надо ли создавать новую комнату?
            boolean createNew = false;
            if (willGoOnSingle || !hasAnotherMultiple) {
                // надо если мы будем переходить на single
                // или переход будет на multiple но ни одного игрока добравшегося сюда нет
                createNew = true;
            } else { // а тут у нас переход на multiple и по идее надо найти комнату и попасть в нее
                // мы самовыпилимся из комнаты где мы сейчас, чтобы если мы на multiple то не мешать другим туда попасть
                removeFromBoard(player);

                // мы пытаемся найти комнату multiple со свободными базами
                boolean found = false;
                for (int i = 0; i < levelNumbers.size(); i++) {
                    // одна из комнат, мы не знаем свободна ли, на multiple ли
                    Expansion current = currents.get(i);
                    // сколько там юзеров было максимально
                    Integer max = fullness.get(current.id());
                    // она и свободная и multiple - нам подходит
                    if (levelNumbers.get(i) == multipleIndex && max < 4) {
                        currents.set(player, current);
                        // в комнату куда пришел игрок, стало на 1 больше
                        fullness.put(current.id(), max + 1);
                        found = true;
                        break;
                    }
                }
                // если не нашли
                if (!found) {
                    // то создаем
                    createNew = true;
                    // и если мы переходим из multiple на multiple
                    if (levelNumbers.get(player) == multipleIndex) {
                        // нам надо сделать вид, будьто бы мы с single на multiple перешли
                        // потому что скрипт ниже так заточен что переходит на n+1 уровнень
                        levelNumbers.set(player, levelNumbers.get(player) - 1);
                    }
                }
            }
            // если надо создать новую комнату - создаем
            if (createNew) {
                if (newPlayer) {
                    // для нового игрока просто доабвляем level = 0
                    levelNumbers.add(0);
                } else {
                    // для существущего level++
                    levelNumbers.set(player, levelNumbers.get(player) + 1);
                }

                // создаем комнату с этим уровнем
                Expansion current = (Expansion) gameRunner.createGame(levelNumbers.get(player));

                if (newPlayer) {
                    // для нового игрока добавляем его комнату
                    currents.add(current);
                } else {
                    // для существующего заменяем старую
                    currents.set(player, current);
                }
                // он в своей комнате один
                fullness.put(current.id(), 1);
            }
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
        removeFromBoard(player);
        games.set(player, null);
    }

    private void removeFromBoard(int player) {
        Game game = games.get(player);
        game.getField().remove(game.getPlayer());
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
        return (JSONObject) single(index).getBoardAsString();
    }
}
