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
import com.google.common.collect.Multimap;
import lombok.experimental.FieldNameConstants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.PlayerGame.by;
import static java.util.stream.Collectors.toList;

@Component
@FieldNameConstants
public class PlayerGames implements Iterable<PlayerGame>, Tickable {

    public static final boolean ALL = true;
    public static final boolean ACTIVE = !ALL;

    private List<PlayerGame> all = new LinkedList<>();

    private Consumer<PlayerGame> onAdd;
    private Consumer<PlayerGame> onRemove;
    private ReadWriteLock lock;
    private Spreader spreader = new Spreader();

    @Autowired
    protected RoomService roomService;

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
        int index = all.indexOf(player);
        if (index == -1) return;
        PlayerGame game = all.remove(index);
        GameType gameType = game.getGameType();
        MultiplayerType type = gameType.getMultiplayerType();

        if (reloadAlone) {
            removeWithResetAlone(game.getGame(), type.shouldReloadAlone());
        }

        game.remove(onRemove);
        game.getGame().on(null);
    }

    private void removeWithResetAlone(Game game, boolean reloadAlone) {
        List<PlayerGame> alone = removeAndLeaveAlone(game);

        if (reloadAlone) {
            alone.forEach(gp -> play(gp.getGame(), gp.getRoomName(),
                    gp.getGameType(), gp.getGame().getSave()));
        }
    }

    public PlayerGame get(String id) {
        return all.stream()
                .filter(pg -> pg.getPlayer().getId().equals(id))
                .findFirst()
                .orElse(NullPlayerGame.INSTANCE);
    }

    public PlayerGame get(GamePlayer player) {
        return all.stream()
                .filter(pg -> pg.getGame().getPlayer().equals(player))
                .findFirst()
                .orElse(null);
    }

    private void play(Game game, String roomName, GameType gameType, JSONObject save) {
        game.close();

        MultiplayerType type = gameType.getMultiplayerType();
        int roomSize = type.loadProgress(game, save);
        LevelProgress progress = game.getProgress();
        int levelNumber = progress.getCurrent();
        GameField field = spreader.fieldFor(game.getPlayer(),
                roomName,
                type,
                roomSize,
                levelNumber,
                () -> {
                    game.getPlayer().setProgress(progress);
                    return gameType.createGame(levelNumber);
                });

        game.on(field);

        game.newGame();
        if (save != null && !save.keySet().isEmpty()) {
            game.loadSave(save);
        }
    }

    public PlayerGame add(Player player, String roomName, PlayerSave save) {
        GameType gameType = player.getGameType();

        Single single = buildSingle(player, gameType);

        Game game = new LockedGame(lock).wrap(single);

        play(game, roomName, gameType, parseSave(save));

        PlayerGame playerGame = new PlayerGame(player, game, roomName);
        if (onAdd != null) {
            onAdd.accept(playerGame);
        }
        all.add(playerGame);
        return playerGame;
    }

    private Single buildSingle(Player player, GameType gameType) {
        GamePlayer gamePlayer = gameType.createPlayer(player.getEventListener(),
                player.getId());

        return new Single(gamePlayer,
                gameType.getPrinterFactory(),
                gameType.getMultiplayerType());
    }

    private JSONObject parseSave(PlayerSave save) {
        if (save == null || PlayerSave.isSaveNull(save.getSave())) {
            return new JSONObject();
        }
        return new JSONObject(save.getSave());
    }

    private List<PlayerGame> removeAndLeaveAlone(Game game) {
        if (!spreader.contains(game.getPlayer())) {
            return Arrays.asList();
        }

        List<GamePlayer> alone = spreader.remove(game.getPlayer());

        return alone.stream()
                .map(p -> get(p))       // GamePlayer
                .filter(p -> p != null) // PlayerGame
                .collect(toList());
    }

    public boolean isEmpty() {
        return all.isEmpty();
    }

    @Override
    public Iterator<PlayerGame> iterator() {
        return all.iterator();
    }

    public List<Player> players() {
        return all.stream()
                .map(PlayerGame::getPlayer)
                .collect(toList());
    }

    public int size() {
        return all.size();
    }

    public void clear() {
        players().forEach(this::remove);
    }

    public List<PlayerGame> getAll(Predicate<PlayerGame> predicate) {
        return all.stream()
                .filter(predicate)
                .collect(toList());
    }

    public static Predicate<PlayerGame> withType(String gameType) {
        return pg -> pg.getPlayer().getGameName().equals(gameType);
    }

    public static Predicate<PlayerGame> withAll() {
        return pg -> true;
    }

    public static Predicate<PlayerGame> withRoom(String room) {
        return pg -> pg.getRoomName().equals(room);
    }

    public Predicate<PlayerGame> withActive() {
        return playerGame -> roomService.isActive(playerGame.getRoomName());
    }

    public List<GameType> getGameTypes() {
        List<GameType> result = new LinkedList<>();

        for (PlayerGame playerGame : all) {
            GameType gameType = playerGame.getGameType();
            if (!result.contains(gameType)) {
                result.add(gameType);
            }
        }

        return result;
    }

    @Override
    public void tick() {
        List<PlayerGame> active = active();

        // по всем джойстикам отправили сообщения играм
        active.forEach(PlayerGame::quietTick);

        // если в TRAINING кто-то isWin то мы его относим на следующий уровень
        // если в DISPOSABLE уровнях кто-то shouldLeave то мы его перезагружаем - от этого он появится на другом поле
        // а для всех остальных, кто уже isGameOver - создаем новые игры на том же поле
        for (PlayerGame playerGame : active) {
            Game game = playerGame.getGame();
            String roomName = playerGame.getRoomName();

            MultiplayerType type = playerGame.getGameType().getMultiplayerType();
            if (game.isGameOver()) {
                quiet(() -> {
                    JSONObject level = game.getSave();

                    if (type.isLevels() && game.isWin()) {
                        level = LevelProgress.goNext(level);
                        if (level != null) {
                            reload(game, roomName, level);
                            playerGame.fireOnLevelChanged();
                            return;
                        }
                    }

                    if (type.isDisposable() && game.shouldLeave()) {
                        reload(game, roomName, level);
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
        //      а так же котмнаты которых активны
        active.stream()
                .filter(playerGame -> playerGame.getField() != null) // TODO разобраться почему так случается при переключении уровней icancode
                .map(PlayerGame::getField)
                .distinct()
                .filter(spreader::isRoomStaffed)
                .forEach(GameField::quietTick);

        // ну и тикаем все GameRunner мало ли кому надо на это подписаться
        getGameTypes().forEach(GameType::quietTick);
    }

    // перевод текущего игрока в новую комнату
    // без обслуживания последнего оставшегося на той же карте
    public void reloadCurrent(PlayerGame playerGame) {
        Game game = playerGame.getGame();
        String roomName = playerGame.getRoomName();
        reload(game, roomName, game.getSave(), false);
    }

    private void reload(Game game, String roomName, JSONObject save, boolean reloadAlone) {
        PlayerGame playerGame = getPlayerGame(game);
        playerGame.setRoomName(roomName);
        GameType gameType = playerGame.getGameType();
        MultiplayerType type = gameType.getMultiplayerType();

        if (reloadAlone) {
            removeWithResetAlone(game, type.shouldReloadAlone());
        }

        play(game, roomName, gameType, save);
    }

    // перевод текущего игрока в новую комнату
    // с обслуживанием последнего оставшегося на той же карте
    public void reload(Game game, String roomName, JSONObject save) {
        reload(game, roomName, save, true);
    }

    // переводим всех игроков на новые борды
    // при этом если надо перемешиваем их
    public void reloadAll(boolean shuffle) {
        reloadAll(shuffle, withAll());
    }

    public void reloadAll(boolean shuffle, Predicate<PlayerGame> predicate) {
        List<PlayerGame> games = getAll(predicate);

        if (shuffle) {
            Collections.shuffle(games);
        }

        games.forEach(pg -> reloadCurrent(pg));
    }

    private PlayerGame getPlayerGame(Game game) {
        return all.stream()
                .filter(pg -> pg.equals(by(game)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
    }

    private void quiet(Runnable runnable) {
        ((Tickable)() -> runnable.run()).quietTick();
    }

    // for testing only
    void clean() {
        new LinkedList<>(all)
                .forEach(pg -> remove(pg.getPlayer()));
    }

    public List<Player> getPlayers(String gameName) {
        return all.stream()
                .map(playerGame -> playerGame.getPlayer())
                .filter(player -> player.getGameName().equals(gameName))
                .collect(toList());
    }

    public void changeLevel(String playerId, int level) {
        PlayerGame playerGame = get(playerId);
        String roomName = playerGame.getRoomName();
        Game game = playerGame.getGame();
        JSONObject save = game.getSave();
        LevelProgress progress = new LevelProgress(save);
        if (progress.canChange(level)) {
            progress.change(level);
            reload(game, roomName, progress.saveTo(new JSONObject()));
            playerGame.fireOnLevelChanged();
        }
    }

    public void setLevel(String playerId, JSONObject save) {
        if (save == null) {
            return;
        }
        PlayerGame playerGame = get(playerId);
        String roomName = playerGame.getRoomName();
        Game game = playerGame.getGame();
        reload(game, roomName, save);
        playerGame.fireOnLevelChanged();
    }

    public void changeRoom(String playerId, String roomName) {
        if (roomName == null) {
            return;
        }
        PlayerGame playerGame = get(playerId);
        JSONObject save = playerGame.getGame().getSave();
        Game game = playerGame.getGame();
        reload(game, roomName, save);
    }

    public PlayerGame get(int index) {
        return all.get(index);
    }

    public List<PlayerGame> all() {
        return all;
    }

    /**
     * @return Отдает только те игры, для которых комната не находится на паузе
     */
    public List<PlayerGame> active() {
        return getAll(withActive());
    }

    public Stream<PlayerGame> stream() {
        return all.stream();
    }

    public Multimap<String, Room> rooms() {
        return spreader.rooms();
    }

    /**
     * @param isAll включать все комнаты или только активные
     * @return Все найденные комнаты для всех играющих
     */
    public List<String> getRooms(boolean isAll) {
        return all.stream()
                .filter(((Predicate<PlayerGame>) pg -> isAll).or(withActive()))
                .map(PlayerGame::getRoomName)
                .distinct()
                .collect(toList());
    }
}
