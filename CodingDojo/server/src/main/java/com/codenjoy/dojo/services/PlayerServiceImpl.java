
package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.controller.chat.ChatController;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.multiplayer.Sweeper;
import com.codenjoy.dojo.services.nullobj.NullDeal;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.services.semifinal.SemifinalStatus;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.whatsnext.WhatsNextService;
import com.codenjoy.dojo.web.rest.pojo.PTeam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.fest.reflect.core.Reflection;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.function.Supplier;

import static com.codenjoy.dojo.services.Deals.withRoom;
import static java.util.stream.Collectors.toMap;

@Component("playerService")
@Slf4j
public class PlayerServiceImpl implements PlayerService {
    
    @Autowired
    @Qualifier("chatController")
    protected ChatController chatController;

    @Autowired protected LockService lockService;
    @Autowired protected DealsService dealsService;
    @Autowired protected GameService gameService;
    @Autowired protected AutoSaver autoSaver;
    @Autowired protected GameSaver saver;
    @Autowired protected ChatService chat;
    @Autowired protected ActionLogger actionLogger;
    @Autowired protected Registration registration;
    @Autowired protected ConfigProperties config;
    @Autowired protected RoomService roomService;
    @Autowired protected SemifinalService semifinal;
    @Autowired protected InactivityService inactivity;
    @Autowired protected SimpleProfiler profiler;
    @Autowired protected TimeService time;
    @Autowired protected WhatsNextService whatsNext;
    @Autowired protected TeamService team;
    @Autowired protected ScoresCleaner scoresCleaner;
    @Autowired protected StatisticService statistic;

    @Value("${game.ai}")
    protected boolean isAiNeeded;

    @PostConstruct
    public void init() {
        dealsService.onAdd(deal -> {
            chatController.register(deal);
        });
        dealsService.onRemove(deal -> {
            chatController.unregister(deal);
        });
    }

    @Override
    public Player register(String id, String game, String room, String ip) {
        lock().writeLock().lock();
        try {
            log.debug("Registered user {} in game {}", id, game);

            if (!isRegistrationOpened(room)) {
                return NullPlayer.INSTANCE;
            }

            registerAIIfNeeded(id, game, room);

            // TODO test me
            PlayerSave save = saver.loadGame(id);
            if (save != PlayerSave.NULL 
                    && game.equals(save.getGame())
                    && room.equals(save.getRoom())) // TODO ROOM test me
            {
                save.setCallbackUrl(ip);
            } else {
                save = new PlayerSave(id, save.getTeamId(), ip, game, room, 0, null);
            }
            Player player = register(new PlayerSave(id, save.getTeamId(), ip, game, room, save.getScore(), save.getSave()));

            return player;
        } finally {
            lock().writeLock().unlock();
        }
    }

    private ReadWriteLock lock() {
        return lockService.get();
    }

    @Override
    public void reloadAI(String id) {
        lock().writeLock().lock();
        try {
            Deal deal = dealsService.get(id);
            registerAI(id, deal.getGameType().name(), deal.getRoom()); // TODO ROOM test me
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public SemifinalStatus getSemifinalStatus(String room) {
        lock().writeLock().lock();
        try {
            return semifinal.getSemifinalStatus(room);
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public String whatsNext(String room, String board, String allActions) {
        lock().writeLock().lock();
        try {
            if (!roomService.exists(room)) {
                throw new IllegalArgumentException("Room not exists: " + room);
            }
            GameType gameType = roomService.gameType(room);
            return whatsNext.calculate(gameType, board, allActions);
        } finally {
            lock().writeLock().unlock();
        }
    }

    private void registerAIIfNeeded(String forPlayer, String game, String room) {
        if (isAI(forPlayer)) return;
        if (!isAiNeeded) return;

        GameType gameType = gameService.getGameType(game, room);

        // если в эту игру ai еще не играет
        String aiId = game + WebSocketRunner.BOT_ID_SUFFIX;
        Deal deal = dealsService.get(aiId);

        if (deal instanceof NullDeal) {
            registerAI(aiId, game, room);
        }
    }

    private String gerCodeForAI(String id) {
        return Hash.getCode(id, id);
    }

    private void registerAI(String id, String game, String room) {
        String code = isAI(id) ?
                gerCodeForAI(id) :
                registration.getCodeById(id);

        setupPlayerAI(getPlayerSupplier(id, game, room),
                id, code, game, room);
    }

    private Supplier<Player> getPlayerSupplier(String id, String game, String room) {
        return () -> getPlayer(new PlayerSave(id,
                            "127.0.0.1", game, room,
                0, null), game, room);
    }

    private void setupPlayerAI(Supplier<Player> getPlayer, String id, String code, String game, String room) {
        Closeable ai = createAI(id, code, game, room);
        if (ai != null) {
            Player player = getPlayer.get();
            player.setReadableName(StringUtils.capitalize(game) + " SuperAI");
            player.setAi(ai);
        }
    }

    @Override
    public Player register(PlayerSave save) {
        lock().writeLock().lock();
        try {
            return justRegister(save);
        } finally {
            lock().writeLock().unlock();
        }
    }

    private Player justRegister(PlayerSave save) {
        String id = save.getId();
        String game = save.getGame();
        String room = save.getRoom();

        if (!gameService.exists(game)) {
            return NullPlayer.INSTANCE; // TODO test me
        }

        Player player = getPlayer(save, game, room);

        if (isAI(id)) {
            setupPlayerAI(() -> player,
                    id, gerCodeForAI(id), game, room);
        }

        return player;
    }

    private boolean isAI(String id) {
        return id.endsWith(WebSocketRunner.BOT_ID_SUFFIX);
    }

    private Closeable createAI(String id, String code, String game, String room) {
        GameType<Settings> gameType = gameService.getGameType(game, room);
        Class<? extends Solver> ai = gameType.getAI();
        if (ai == null) {
            return null;
        }

        try {
            Solver solver;

            try {
                solver = Reflection.constructor()
                        .withParameterTypes(Dice.class)
                        .in(ai)
                        .newInstance(gameType.getDice());
            } catch (Exception e) {
                log.warn("Cant find constructor with Dice parameter for: " + ai.toString());
                solver = Reflection.constructor()
                        .withParameterTypes()
                        .in(ai)
                        .newInstance(); // without dice
            }

            ClientBoard board = Reflection.constructor()
                    .in(gameType.getBoard())
                    .newInstance();

            WebSocketRunner runner = runAI(id, code, solver, board);
            return runner;
        } catch (Exception e) {
            log.error("Cant find constructor with/without Dice parameter for: " + ai.toString(), e);
            return null;
        }
    }

    protected WebSocketRunner runAI(String id, String code, Solver solver, ClientBoard board) {
        return WebSocketRunner.runAI(id, code, solver, board);
    }

    private Player getPlayer(PlayerSave save, String game, String room) {
        String id = save.getId();
        String callbackUrl = save.getCallbackUrl();

        GameType gameType = gameService.getGameType(game, room);
        Player player = getPlayer(id);
        Deal oldDeal = dealsService.get(id);

        boolean newPlayer = (player instanceof NullPlayer) 
                || !game.equals(player.getGame())
                || !room.equals(oldDeal.getRoom()); // TODO ROOM test me
        if (newPlayer) {
            Deal deal = dealsService.deal(save, room, id, callbackUrl, gameType, time.now());

            player = deal.getPlayer();

            // TODO N+1 проблема во время загрузки приложения
            player.setReadableName(registration.getNameById(player.getId()));

            log.debug("Player {} starting new game {}", id, deal.getGame());
        } else {
          // do nothing
        }
        return player;
    }

    @Override
    public void tick() {
        lock().writeLock().lock();
        try {
            profiler.start("PSI.tick()");

            statistic.tick();

            actionLogger.log(dealsService.getAll(roomService::isRoomActive));
            autoSaver.tick();

            statistic.log(dealsService.tick(roomService::isRoomActive));

            chatController.tick();

            inactivity.tick();
            semifinal.tick();

            statistic.dealsCount(dealsService.size());
            statistic.tickDuration(profiler.end());
        } catch (Error e) {
            e.printStackTrace();
            log.error("PlayerService.tick() throws", e);
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public List<Player> getAll() {
        lock().readLock().lock();
        try {
            return Collections.unmodifiableList(dealsService.players());
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public List<Player> getAll(String game) {
        lock().readLock().lock();
        try {
            return dealsService.getPlayersByGame(game);
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public List<Player> getAllInRoom(String room) {
        lock().readLock().lock();
        try {
            return dealsService.getPlayersByRoom(room);
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public void remove(String id) {
        lock().writeLock().lock();
        try {
            Player player = getPlayer(id);

            log.debug("Unregistered user {} from game {}",
                    player.getId(), player.getGame());

            dealsService.remove(player.getId(), Sweeper.on());
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public void updateAll(List<? extends Player> players) {
        lock().writeLock().lock();
        try {
            if (players == null) {
                return;
            }
            players.stream()
                    // TODO почему тут могут быть id null посмотреть git history?
                    .filter(player -> player.getId() != null)
                    .forEach(player -> updatePlayer(found(player), player));
        } finally {
            lock().writeLock().unlock();
        }
    }

    private Deal found(Player player) {
        Deal deal = dealsService.get(player.getId());
        if (deal == NullDeal.INSTANCE) {
            throw new IllegalArgumentException("Player not found by id: " + player.getId());
        }
        return deal;
    }

    @Override
    public void update(Player player) {
        lock().writeLock().lock();
        try {
            updatePlayer(found(player), player);
        } finally {
            lock().writeLock().unlock();
        }
    }

    private void updatePlayer(Deal deal, Player input) {
        Player updated = deal.getPlayer();

        boolean updateCallbackUrl = StringUtils.isNotEmpty(input.getCallbackUrl());
        if (updateCallbackUrl) {
            updated.setCallbackUrl(input.getCallbackUrl());
        }

        boolean updateReadableName = StringUtils.isNotEmpty(input.getReadableName());
        if (updateReadableName) {
            updated.setReadableName(input.getReadableName());
            registration.updateReadableName(input.getId(), input.getReadableName());
        }

        boolean updateEmail = StringUtils.isNotEmpty(input.getEmail());
        if (updateEmail) {
            updated.setEmail(input.getEmail());
            registration.updateEmail(input.getId(), input.getEmail());
        }

        boolean updateId = !deal.getPlayer().getId().equals(input.getId());
        if (updateId) {
            updated.setId(input.getId());
            registration.updateId(input.getReadableName(), input.getId());
        }

        try {
            boolean updateScore = input.getScore() != null;
            if (updateScore) {
                updated.getScores().update(input.getScore());
            }
        } catch (Exception e) {
            // do nothing
        }

        boolean updateRoomName = input.getRoom() != null
                && !deal.getRoom().equals(input.getRoom());
        if (updateRoomName) {
            String id = input.getId();
            String callbackUrl = updated.getCallbackUrl();
            String newRoom = input.getRoom();
            String newGame = input.getGame();
            String game = updated.getGame();
            changeRoom(id, game, newGame, newRoom, callbackUrl);
        }
        
        Game game = deal.getGame();
        boolean saveExists = game != null && (game.getSave() != null || updateRoomName);
        if (saveExists) {
            String oldSave = game.getSave().toString();
            String newSave = input.getData();
            boolean updateSave = !PlayerSave.isSaveNull(newSave) && !newSave.equals(oldSave);
            if (updateSave) {
                dealsService.setLevel(
                        input.getId(),
                        new JSONObject(newSave));
            }
        }

        boolean updateTeam = deal.getTeamId() != input.getTeamId();
        if (updateTeam) {
            // TODO #3d4w этот метод используется для апдейта так же всех юзеров, и тогда получается, что я обновляю их по очереди каждого независимо
            team.distributePlayersByTeam(deal.getRoom(),
                    Arrays.asList(new PTeam(input.getTeamId(), deal.getPlayerId())));
        }
    }

    private void changeRoom(String id, String game, String newGame, String newRoom, String url) {
        if (StringUtils.isEmpty(newGame)) {
            // если игру не указали, пробуем достать из комнаты
            newGame = roomService.game(newRoom);
        }
        // если такой комнаты нет или игра не отличается - мы меняем только комнату
        boolean changeRoom = newGame == null || game.equals(newGame);
        if (changeRoom) {
            // меняем комнату
            gameService.getGameType(game, newRoom);     // тут создастся новая комната
            dealsService.changeRoom(id, game, newRoom);
        } else {
            // меняем игру
            remove(id);
            register(id, newGame, newRoom, url);
        }
    }

    @Override // TODO test me
    public void loadSaveForAll(String room, String newSave) {
        lock().writeLock().lock();
        try {
            List<Player> players = dealsService.getPlayersByRoom(room);
            players.forEach(player -> dealsService.setLevel(
                    player.getId(),
                    new JSONObject(newSave)));
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public boolean contains(String id) {
        lock().readLock().lock();
        try {
            return getPlayer(id) != NullPlayer.INSTANCE;
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public Player get(String id) {
        lock().readLock().lock();
        try {
            return getPlayer(id);
        } finally {
            lock().readLock().unlock();
        }
    }

    private Player getPlayer(String id) {
        return dealsService.get(id).getPlayer();
    }

    @Override
    public void removeAll() {
        lock().writeLock().lock();
        try {
            dealsService.clear();
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public void removeAll(String room) {
        lock().writeLock().lock();
        try {
            dealsService.getAll(withRoom(room))
                    .stream()
                    .map(deal -> deal.getPlayer())
            // TODO тут раньше сносились все комнаты напрямую, но spreader не трогали, и тесты не тестируют это
                    .forEach(player -> dealsService.remove(player.getId(), Sweeper.off()));
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public Joystick getJoystick(String id) {
        lock().writeLock().lock();
        try {
            return dealsService.get(id).getGame().getJoystick();
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public void closeRegistration() {
        config.setRegistrationOpened(false);
    }

    @Override
    public boolean isRegistrationOpened() {
        return config.isRegistrationOpened()
                && roomService.isOpened();
    }

    @Override
    public boolean isRegistrationOpened(String room) {
        return isRegistrationOpened()
                && roomService.isOpened(room);
    }

    @Override
    public void openRegistration() {
        config.setRegistrationOpened(true);
    }

    @Override
    public void cleanAllScores() {
        lock().writeLock().lock();
        try {
            semifinal.clean();
            scoresCleaner.cleanAllScores();
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public void cleanAllScores(String room) {
        lock().writeLock().lock();
        try {
            semifinal.clean(room);
            scoresCleaner.cleanAllScores(room);
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public void cleanScores(String id) {
        lock().writeLock().lock();
        try {
            scoresCleaner.cleanScores(id);
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override // TODO test me
    public void reloadAllRooms() {
        lock().writeLock().lock();
        try {
            dealsService.reloadAll(true);
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override // TODO test me
    public void reloadAllRooms(String room) {
        lock().writeLock().lock();
        try {
            dealsService.reloadAll(true, withRoom(room));
        } finally {
            lock().writeLock().unlock();
        }
    }

    @Override
    public Player getRandomInRoom(String room) {
        lock().readLock().lock();
        try {
            if (dealsService.size() == 0) return NullPlayer.INSTANCE;

            if (room == null) {
                return dealsService.getAll(deal -> true).get(0).getPlayer();
            }

            Iterator<Player> iterator = dealsService.getPlayersByRoom(room).iterator();
            if (!iterator.hasNext()) return NullPlayer.INSTANCE;
            return iterator.next();
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public String getAnyRoomWithPlayers() {
        lock().readLock().lock();
        try {
            if (dealsService.size() == 0) return null;

            return dealsService.getAll(deal -> true).get(0).getRoom();
        } finally {
            lock().readLock().unlock();
        }
    }

    @Override
    public Map<String, Integer> getRoomCounts() {
        lock().readLock().lock();
        try {
            List<Player> players = dealsService.players();
            return roomService.rooms().stream()
                    .map(room -> new HashMap.SimpleEntry<>(room, count(players, room)))
                    .collect(toMap(entry -> entry.getKey(),
                            entry -> entry.getValue(),
                            (value1, value2) -> value2,
                            LinkedHashMap::new));
        } finally {
            lock().readLock().unlock();
        }
    }

    private int count(List<Player> players, String room) {
        return (int) players.stream()
                .filter(player -> room.equals(player.getRoom()))
                .count();
    }
}