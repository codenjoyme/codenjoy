package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.*;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.PlayerGame.by;
import static java.util.stream.Collectors.toList;

@Component
public class PlayerGames implements Iterable<PlayerGame>, Tickable {

    private List<PlayerGame> playerGames = new LinkedList<>();

    private Consumer<PlayerGame> onAdd;
    private Consumer<PlayerGame> onRemove;
    private ReadWriteLock lock;
    private Spreader spreader = new Spreader();

    public void onAdd(Consumer<PlayerGame> consumer) {
        this.onAdd = consumer;
    }

    public void onRemove(Consumer<PlayerGame> consumer) {
        this.onRemove = consumer;
    }

    public void init(ReadWriteLock lock) {
        this.lock = lock;
    }

    public PlayerGames() {
        lock = new ReentrantReadWriteLock();
    }

    // удаление текущего игрока
    // с обслуживанием последнего оставшегося на той же карте
    public void removeCurrent(Player player) {
        remove(player, false);
    }

    // удаление текущего игрока
    // без обслуживания последнего оставшегося на той же карте
    public void remove(Player player) {
        remove(player, true);
    }

    private void remove(Player player, boolean reloadAlone) {
        int index = playerGames.indexOf(player);
        if (index == -1) return;
        PlayerGame game = playerGames.remove(index);

        if (reloadAlone) {
            removeWithResetAlone(game.getGame());
        }

        game.remove(onRemove);
        game.getGame().on(null);
    }

    private void removeWithResetAlone(Game game) {
        List<PlayerGame> alone = removeAndLeaveAlone(game);
        alone.forEach(gp -> spreader.play(gp.getGame(), gp.getGameType(), gp.getGame().getSave()));
    }

    public PlayerGame get(String playerName) {
        return playerGames.stream()
                .filter(pg -> pg.getPlayer().getName().equals(playerName))
                .findFirst()
                .orElse(NullPlayerGame.INSTANCE);
    }

    public PlayerGame get(GamePlayer player) {
        return playerGames.stream()
                .filter(pg -> pg.getGame().getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    public PlayerGame add(Player player, PlayerSave save) {
        GameType gameType = player.getGameType();

        GamePlayer gamePlayer = gameType.createPlayer(player.getEventListener(),
                player.getName());

        Single single = new Single(gamePlayer,
                gameType.getPrinterFactory(),
                gameType.getMultiplayerType());

        spreader.play(single, gameType, parseSave(save));

        Game game = new LockedGame(lock).wrap(single);

        PlayerGame playerGame = new PlayerGame(player, game);
        if (onAdd != null) {
            onAdd.accept(playerGame);
        }
        playerGames.add(playerGame);
        return playerGame;
    }

    JSONObject parseSave(PlayerSave save) {
        if (save == null || PlayerSave.isSaveNull(save.getSave())) {
            return new JSONObject();
        }
        return new JSONObject(save.getSave());
    }

    private List<PlayerGame> removeAndLeaveAlone(Game game) {
        if (spreader.contains(game)) {
            List<GamePlayer> alone = spreader.remove(game);
            List<PlayerGame> result = alone.stream()
                    .map(p -> get(p))
                    .collect(toList());
            while (result.contains(null)) { // TODO как-то странно так делать
                result.remove(null);
            }
            return result;
        } else {
            return Arrays.asList();
        }
    }

    public boolean isEmpty() {
        return playerGames.isEmpty();
    }

    @Override
    public Iterator<PlayerGame> iterator() {
        return playerGames.iterator();
    }

    public List<Player> players() {
        return playerGames.stream()
                .map(PlayerGame::getPlayer)
                .collect(toList());
    }

    public int size() {
        return playerGames.size();
    }

    public void clear() {
        players().forEach(this::remove);
    }

    public List<PlayerGame> getAll(String gameType) {
        return playerGames.stream()
                .filter(pg -> pg.getPlayer().getGameName().equals(gameType))
                .collect(toList());
    }

    public List<GameType> getGameTypes() {
        List<GameType> result = new LinkedList<>();

        for (PlayerGame playerGame : playerGames) {
            GameType gameType = playerGame.getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }

    @Override
    public void tick() {
        // по всем джойстикам отправили сообщения играм
        playerGames.forEach(PlayerGame::quietTick);

        // если в TRAINING кто-то isWin то мы его относим на следующий уровень
        // если в DISPOSABLE уровнях кто-то shouldLeave то мы его перезагружаем - от этого он появится на другом поле
        // а для всех остальных, кто уже isGameOver - создаем новые игры на том же поле
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            MultiplayerType multiplayerType = playerGame.getGameType().getMultiplayerType();
            if (game.isGameOver()) {
                quiet(() -> {
                    JSONObject level = game.getSave();

                    // TODO ##2 попробовать какой-то другой тип с несколькими уровнями, а не только isTraining
                    if (game.isWin() && multiplayerType.isTraining()) {
                        level = LevelProgress.winLevel(level);
                        if (level != null) {
                            reload(game, level);
                            // TODO test me
                            playerGame.getPlayer().getEventListener().levelChanged(game.getProgress());
                            return;
                        }
                    }

                    if (game.shouldLeave() && multiplayerType.isDisposable()) {
                        reload(game, level);
                        return;
                    }

                    game.newGame();
                });
            }
        }

        // собираем все уникальные борды
        // независимо от типа игры нам нужно тикнуть все
        //      но только те, которые не DISPOSABLE и одновременно
        //      недокомплектованные пользователями
        playerGames.stream()
                .map(PlayerGame::getField)
                .distinct()
                .filter(this::isMatchCanBeStarted)
                .forEach(GameField::quietTick);

        // ну и тикаем все GameRunner мало ли кому надо на это подписаться
        getGameTypes().forEach(GameType::quietTick);
    }

    private boolean isMatchCanBeStarted(GameField field) {
        return spreader.isRoomStaffed(field);
    }

    // перевод текущего игрока в новую комнату
    // без обслуживания последнего оставшегося на той же карте
    public void reloadCurrent(PlayerGame playerGame) {
        Game game = playerGame.getGame();
        reload(game, game.getSave(), false);
    }

    private void reload(Game game, JSONObject save, boolean reloadAlone) {
        GameType gameType = getPlayer(game).getGameType();
        if (reloadAlone) {
            removeWithResetAlone(game);
        }

        spreader.play(game, gameType, save);
    }

    // перевод текущего игрока в новую комнату
    // с обслуживанием последнего оставшегося на той же карте
    public void reload(Game game, JSONObject save) {
        reload(game, save, true);
    }

    // переводим всех игроков на новые борды
    // при этом если надо перемешиваем их
    public void reloadAll(boolean shuffle) {
        new LinkedList<PlayerGame>(){{
            addAll(playerGames);
            if (shuffle) {
                Collections.shuffle(this);
            }
        }}.forEach(pg -> reloadCurrent(pg));
    }

    private Player getPlayer(Game game) {
        return playerGames.stream()
                .filter(pg -> pg.equals(by(game)))
                .findFirst()
                .orElseThrow(IllegalStateException::new)
                .getPlayer();
    }

    private void quiet(Runnable runnable) {
        ((Tickable)() -> runnable.run()).quietTick();
    }


    public void clean() {
        new LinkedList<>(playerGames)
                .forEach(pg -> remove(pg.getPlayer()));
    }

    public List<Player> getPlayers(String gameName) {
        return playerGames.stream()
                .map(playerGame -> playerGame.getPlayer())
                .filter(player -> player.getGameName().equals(gameName))
                .collect(toList());
    }

    public void changeLevel(String playerName, int level) {
        PlayerGame playerGame = get(playerName);
        Game game = playerGame.getGame();
        JSONObject save = game.getSave();
        LevelProgress progress = new LevelProgress(save);
        if (progress.canChange(level)) {
            progress.change(level);
            reload(game, progress.saveTo(new JSONObject()));
        }
    }

    public void setLevel(String playerName, JSONObject save) {
        if (save == null) {
            return;
        }
        PlayerGame playerGame = get(playerName);
        Game game = playerGame.getGame();
        reload(game, save);
    }

    public PlayerGame get(int index) {
        return playerGames.get(index);
    }

    public List<PlayerGame> all() {
        return playerGames;
    }

    public Stream<PlayerGame> stream() {
        return playerGames.stream();
    }
}
