package com.codenjoy.dojo.expansion.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.expansion.model.levels.items.Hero;
import com.codenjoy.dojo.expansion.services.GameRunner;
import com.codenjoy.dojo.expansion.services.GameSettings;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.multiplayer.Single;

import java.util.*;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class SoftSpreader {

    public static boolean NEW_GAME_WHEN_GAME_OVER;

    private List<EventListener> listeners;
    private List<Single> games;
    private List<Hero> heroes;

    private GameRunner gameRunner;
    private List<Expansion> currents;
    private List<Integer> levelNumbers;
    private Map<String, Integer> fullness;

    private final GameSettings settings;

    public SoftSpreader(GameRunner gameRunner) {
        NEW_GAME_WHEN_GAME_OVER = true;
        this.gameRunner = gameRunner;
        settings = gameRunner.getSettings();

        levelNumbers = new LinkedList<>();
        fullness = new HashMap<>();
        currents = new LinkedList<>();

        listeners = new LinkedList<>();
        games = new LinkedList<>();
        heroes = new LinkedList<>();
    }

    public void reloadLevelForWinner(int player) {
        Single game = games.get(player);
        assertEquals(true, game.isWin());

        reloadLevel(player);
    }

    public void reloadLevel(int player) {
        createNewLevelIfNeeded(player);
        removeFromBoard(player);

        Single game = games.get(player);
        game.on(currents.get(player));
        game.newGame();

        heroes.set(player, (Hero) game.getPlayer().getHero());
    }

    protected void createOneMorePlayer() {
        int playerIndex = games.size();
        createNewLevelIfNeeded(playerIndex);

        EventListener listener = mock(EventListener.class);
        listeners.add(listener);

        String id = String.format("demo%s", playerIndex + 1);
        Player player = (Player) gameRunner.createPlayer(listener, DEFAULT_TEAM_ID, id, settings);
        Single game = new Single(player, gameRunner.getPrinterFactory());
        game.on(currents.get(playerIndex));
        game.newGame();
        games.add(game);

        Hero hero = (Hero) game.getPlayer().getHero();
        hero.manual(true);
        heroes.add(hero);
    }

    protected void destroy(int player) {
        removeFromBoard(player);
        games.set(player, null);
    }

    private void removeFromBoard(int player) {
        if (player == games.size()) {
            return;
        }
        Game game = games.get(player);
        game.getField().remove(game.getPlayer());
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
            // он = 0 если нет single уровней
            int multipleIndex = multipleIndex();
            // текущий юзер будет переходить на single или уже на multiple
            boolean willGoOnSingle = (newPlayer  || levelNumbers.get(player) + 1 < multipleIndex)
                                    && multipleIndex != 0;
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
                        if (newPlayer) {
                            currents.add(current);
                            levelNumbers.add(levelNumbers.get(i));
                        } else {
                            currents.set(player, current);
                        }
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
                    // но это не первая комната в кейзе без single
                    if (!newPlayer && levelNumbers.get(player) == multipleIndex) {
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
                Expansion current = (Expansion) gameRunner.createGame(levelNumbers.get(player), settings);

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

    private int multipleIndex() {
        MultiplayerType type = gameRunner.getMultiplayerType(settings);
        if (type.isTraining()) {
            return type.getLevelsCount();
        } else {
            return 0;
        }
    }

    public void tickAll() {
        tickAll(games);
    }

    public void newGameForAllGameOver() {
        newGameForAllGameOver(games);
    }

    public static void tickAll(List<? extends Game> games) {
        games.stream()
                .filter(Objects::nonNull)
                .map(Game::getField)
                .distinct()
                .filter(Objects::nonNull)
                .forEach(GameField::tick);

        if (NEW_GAME_WHEN_GAME_OVER) {
            newGameForAllGameOver(games);
        }
    }

    public static void newGameForAllGameOver(List<? extends Game> games) {
        games.stream()
                .filter(Objects::nonNull)
                .filter(Game::isGameOver)
                .forEach(single -> {
                    GameField field = single.getField();
                    single.close();
                    single.on(field);
                    single.newGame();
                });
    }

    public Field field(int player) {
        return (Field) games.get(player).getField();
    }

    public List<Hero> heroes() {
        return heroes;
    }

    public Single single(int index) {
        return games.get(index);
    }

    public EventListener listener(int index) {
        return listeners.get(index);
    }

    public int count() {
        return games.size();
    }

    public void clear() {
        games.clear();
        heroes.clear();
        listeners.clear();
        currents.clear();
        levelNumbers.clear();
        fullness.clear();
    }

    public String gameId(int index) {
        return player(index).getField().id();
    }

    public Player player(int index) {
        return (Player)single(index).getPlayer();
    }

    public String playerId(int index) {
        return player(index).lg.id();
    }

    public Hero hero(int index) {
        return (Hero)single(index).getJoystick();
    }

    // если игрок походил, то в его комнате считаем, что уже все заполнено
    public void roomIsBusy(int index) {
        Expansion current = currents.get(index);
        fullness.put(current.id(), current.allBases());
    }
}
