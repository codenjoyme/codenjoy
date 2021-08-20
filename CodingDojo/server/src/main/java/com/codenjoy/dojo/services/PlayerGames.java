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
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
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
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Spreader spreader = new Spreader();

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected TimeService timeService;

    public void onAdd(Consumer<PlayerGame> consumer) {
        this.onAdd = consumer;
    }

    public void onRemove(Consumer<PlayerGame> consumer) {
        this.onRemove = consumer;
    }

    public void init(ReadWriteLock lock) {
        this.lock = lock;
    }

    public void remove(String id, Sweeper sweeper) {
        int index = all.indexOf(new Player(id));
        if (index == -1) return;
        PlayerGame playerGame = all.remove(index);

        removeInRoom(playerGame, sweeper);

        playerGame.remove(onRemove);
        playerGame.getGame().on(null);
    }

    private void removeInRoom(PlayerGame playerGame, Sweeper sweeper) {
        sweeper.of(playerGame.getType());

        Game game = playerGame.getGame();
        List<PlayerGame> alone = removeGame(game, sweeper);

        if (sweeper.isResetOther()) {
            alone.forEach(gp -> play(gp.getGame(), gp.getRoom(),
                    gp.getGameType(), gp.getGame().getSave()));
        }
    }

    // TODO по хорошему тут тоже надо optional
    public PlayerGame get(String id) {
        return get(pg -> Objects.equals(id, pg.getPlayerId()))
                .orElse(NullPlayerGame.INSTANCE);
    }

    public Optional<PlayerGame> get(Predicate<PlayerGame> filter) {
        return all.stream()
                .filter(filter)
                .findFirst();
    }

    public Optional<PlayerGame> get(GamePlayer player) {
        return get(pg -> Objects.equals(player, pg.getGame().getPlayer()));
    }

    private void play(Game game, String room, GameType gameType, JSONObject save) {
        game.close();

        MultiplayerType type = gameType.getMultiplayerType(gameType.getSettings());
        int roomSize = type.loadProgress(game, save);
        LevelProgress progress = game.getProgress();
        int level = progress.getCurrent();
        GameField field = spreader.fieldFor(game.getPlayer(),
                room,
                type,
                roomSize,
                level,
                () -> {
                    game.getPlayer().setProgress(progress);
                    return gameType.createGame(level, gameType.getSettings());
                });

        game.on(field);

        game.newGame();
        if (save != null && !save.keySet().isEmpty()) {
            game.loadSave(save);
        }
    }

    public PlayerGame add(Player player, String room, PlayerSave save) {
        GameType gameType = player.getGameType();

        Single single = buildSingle(player, save, gameType);

        Game game = new LockedGame(lock).wrap(single);

        play(game, room, gameType, parseSave(save));

        PlayerGame playerGame = new PlayerGame(player, game, room);
        if (onAdd != null) {
            onAdd.accept(playerGame);
        }
        all.add(playerGame);
        return playerGame;
    }

    private Single buildSingle(Player player, PlayerSave save, GameType gameType) {
        if (save != null && save != PlayerSave.NULL) {
            player.setTeamId(save.getTeamId());
        }
        GamePlayer gamePlayer = gameType.createPlayer(player.getEventListener(),
                player.getTeamId(), player.getId(), gameType.getSettings());
        return new Single(gamePlayer,
                gameType.getPrinterFactory(),
                gameType.getMultiplayerType(gameType.getSettings()));
    }

    private JSONObject parseSave(PlayerSave save) {
        if (save == null || PlayerSave.isSaveNull(save.getSave())) {
            return new JSONObject();
        }
        return new JSONObject(save.getSave());
    }

    private List<PlayerGame> removeGame(Game game, Sweeper sweeper) {
        List<GamePlayer> alone = spreader.remove(game.getPlayer(), sweeper);

        return alone.stream()
                .map(this::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
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
        players().forEach(player -> remove(player.getId(), Sweeper.off()));
    }

    public List<PlayerGame> getAll(Predicate<PlayerGame> predicate) {
        return all.stream()
                .filter(predicate)
                .collect(toList());
    }

    public static Predicate<PlayerGame> withType(String gameType) {
        return pg -> pg.getPlayer().getGame().equals(gameType);
    }

    public static Predicate<PlayerGame> withAll() {
        return pg -> ALL;
    }

    public static Predicate<PlayerGame> withRoom(String room) {
        return pg -> pg.getRoom() != null && pg.getRoom().equals(room);
    }

    public static Predicate<PlayerGame> exclude(List<String> ids) {
        return pg -> !ids.contains(pg.getPlayerId());
    }

    public Predicate<PlayerGame> withActive() {
        return playerGame -> roomService.isActive(playerGame.getRoom());
    }

    /**
     * @return Возвращает уникальные (недублирующиеся) GameType в которые сейчас играют.
     */
    public List<GameType> getGameTypes() {
        Map<String, GameType> result = new LinkedHashMap<>();

        for (PlayerGame playerGame : all) {
            GameType gameType = playerGame.getGameType();
            gameType = RoomGameType.unwrap(gameType);
            if (!result.containsKey(gameType.name())) {
                result.put(gameType.name(), gameType);
            }
        }

        return new ArrayList<>(result.values());
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
            String id = playerGame.getPlayerId();

            GameType gameType = playerGame.getGameType();
            Settings settings = gameType.getSettings();
            MultiplayerType type = gameType.getMultiplayerType(settings);
            if (game.isGameOver()) {
                quiet(() -> {

                    if (type.isLevels() && game.isWin()) {
                        if (setLevel(id, LevelProgress.goNext(game.getSave()))) {
                            return;
                        }
                    }

                    if (type.isDisposable() && game.shouldLeave()) {
                        reload(id, Sweeper.on().lastAlone());
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

    public void reload(String id, Sweeper sweeper) {
        reload(id, null, sweeper);
    }

    private void reload(String id, JSONObject save, Sweeper sweeper) {
        PlayerGame playerGame = get(id);
        Game game = playerGame.getGame();
        String room = playerGame.getRoom();
        if (save == null) {
            save = game.getSave();
        }

        GameType gameType = playerGame.getGameType();

        removeInRoom(playerGame, sweeper);

        play(game, room, gameType, save);
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

        games.forEach(pg -> spreader.remove(pg.getGame().getPlayer(), Sweeper.off()));
        games.forEach(pg -> reload(pg.getPlayerId(), Sweeper.off()));
    }

    private void quiet(Runnable runnable) {
        ((Tickable)() -> runnable.run()).quietTick();
    }

    public List<Player> getPlayersByGame(String game) {
        return all.stream()
                .map(playerGame -> playerGame.getPlayer())
                .filter(player -> player.getGame().equals(game))
                .collect(toList());
    }

    public List<Player> getPlayersByRoom(String room) {
        return all.stream()
                .map(playerGame -> playerGame.getPlayer())
                .filter(player -> player.getRoom().equals(room))
                .collect(toList());
    }

    public void changeLevel(String id, int level) {
        PlayerGame playerGame = get(id);
        Game game = playerGame.getGame();
        JSONObject save = game.getSave();
        LevelProgress progress = new LevelProgress(save);
        if (progress.canChange(level)) {
            progress.change(level);
            reload(id, progress.saveTo(new JSONObject()), Sweeper.on().lastAlone());
            playerGame.fireOnLevelChanged();
        }
    }

    public boolean setLevel(String id, JSONObject save) {
        if (save == null) {
            return false;
        }
        reload(id, save, Sweeper.on().lastAlone());
        get(id).fireOnLevelChanged();
        return true;
    }

    // TODO #3d4w убери меня
    public void setTeam(String id, int teamId) {
        PlayerGame playerGame = get(id);

        playerGame.setTeamId(teamId);

        reload(id, Sweeper.on().lastAlone());
    }

    public void changeRoom(String id, String gameName, String newRoom) {
        if (Validator.isEmpty(newRoom) || Validator.isEmpty(gameName)) {
            return;
        }
        PlayerGame playerGame = get(id);
        if (!playerGame.getPlayer().getGame().equals(gameName)) {
            return;
        }
        playerGame.setRoom(newRoom);
        reload(id, Sweeper.on().lastAlone());
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

    public Multimap<String, GameRoom> rooms() {
        return spreader.rooms();
    }

    /**
     * @param isAll включать все комнаты или только активные
     * @return Все найденные комнаты для всех играющих
     */
    public List<String> getRooms(boolean isAll) {
        return all.stream()
                .filter(((Predicate<PlayerGame>) pg -> isAll).or(withActive()))
                .map(PlayerGame::getRoom)
                .distinct()
                .collect(toList());
    }
}
