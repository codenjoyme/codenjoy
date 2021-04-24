
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


import com.codenjoy.dojo.client.ClientBoard;
import com.codenjoy.dojo.client.Closeable;
import com.codenjoy.dojo.client.Solver;
import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.semifinal.SemifinalStatus;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
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
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

import static com.codenjoy.dojo.services.PlayerGames.exclude;
import static com.codenjoy.dojo.services.PlayerGames.withRoom;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component("playerService")
@Slf4j
public class PlayerServiceImpl implements PlayerService {
    
    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Map<Player, String> cacheBoards = new HashMap<>();

    @Autowired protected PlayerGames playerGames;
    @Autowired private PlayerGamesView playerGamesView;

    @Autowired
    @Qualifier("playerController")
    protected Controller playerController;

    @Autowired
    @Qualifier("screenController")
    protected Controller screenController;

    @Autowired protected GameService gameService;
    @Autowired protected AutoSaver autoSaver;
    @Autowired protected GameSaver saver;
    @Autowired protected Chat chat;
    @Autowired protected ActionLogger actionLogger;
    @Autowired protected Registration registration;
    @Autowired protected ConfigProperties config;
    @Autowired protected RoomService roomService;
    @Autowired protected SemifinalService semifinal;
    @Autowired protected SimpleProfiler profiler;
    @Autowired protected TimeService time;

    @Value("${game.ai}")
    protected boolean isAiNeeded;

    @PostConstruct
    public void init() {
        playerGames.init(lock);
        playerGames.onAdd(playerGame -> {
            Player player = playerGame.getPlayer();
            Joystick joystick = playerGame.getJoystick();
            playerController.registerPlayerTransport(player, joystick);
            screenController.registerPlayerTransport(player, null);
        });
        playerGames.onRemove(playerGame -> {
            Player player = playerGame.getPlayer();
            playerController.unregisterPlayerTransport(player);
            screenController.unregisterPlayerTransport(player);
        });
    }

    @Override
    public Player register(String id, String game, String room, String ip) {
        lock.writeLock().lock();
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
                save = new PlayerSave(id, ip, game, room, 0, null);
            }
            Player player = register(new PlayerSave(id, ip, game, room, save.getScore(), save.getSave()));

            return player;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void reloadAI(String id) {
        lock.writeLock().lock();
        try {
            PlayerGame playerGame = playerGames.get(id);
            registerAI(id, playerGame.getGameType().name(), playerGame.getRoom()); // TODO ROOM test me
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public SemifinalStatus getSemifinalStatus(String room) {
        lock.writeLock().lock();
        try {
            return semifinal.getSemifinalStatus(room);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void registerAIIfNeeded(String forPlayer, String game, String room) {
        if (isAI(forPlayer)) return;
        if (!isAiNeeded) return;

        GameType gameType = gameService.getGameType(game, room);

        // если в эту игру ai еще не играет
        String aiId = game + WebSocketRunner.BOT_ID_SUFFIX;
        PlayerGame playerGame = playerGames.get(aiId);

        if (playerGame instanceof NullPlayerGame) {
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
        lock.writeLock().lock();
        try {
            return justRegister(save);
        } finally {
            lock.writeLock().unlock();
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
            return null;
        }
    }

    protected WebSocketRunner runAI(String id, String code, Solver solver, ClientBoard board) {
        return WebSocketRunner.runAI(id, code, solver, board);
    }

    private Player getPlayer(PlayerSave save, String game, String room) {
        String name = save.getId();
        String callbackUrl = save.getCallbackUrl();

        GameType gameType = gameService.getGameType(game, room);
        Player player = getPlayer(name);
        PlayerGame oldPlayerGame = playerGames.get(name);

        boolean newPlayer = (player instanceof NullPlayer) 
                || !game.equals(player.getGame())
                || !room.equals(oldPlayerGame.getRoom()); // TODO ROOM test me
        if (newPlayer) {
            playerGames.remove(player);

            PlayerScores playerScores = gameType.getPlayerScores(save.getScore(), gameType.getSettings());
            InformationCollector listener = new InformationCollector(playerScores);

            player = new Player(name, callbackUrl,
                    gameType, playerScores, listener);
            player.setEventListener(listener);

            player.setGameType(gameType);
            PlayerGame playerGame = playerGames.add(player, room, save);

            player = playerGame.getPlayer();

            // TODO N+1 проблема во время загрузки приложения
            player.setReadableName(registration.getNameById(player.getId()));

            log.debug("Player {} starting new game {}", name, playerGame.getGame());
        } else {
          // do nothing
        }
        return player;
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            profiler.start("PSI.tick()");

            actionLogger.log(playerGames);
            autoSaver.tick();

            playerGames.tick();
            sendScreenUpdates();
            requestControls();

            semifinal.tick();

            profiler.end();
        } catch (Error e) {
            e.printStackTrace();
            log.error("PlayerService.tick() throws", e);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void requestControls() {
        int requested = 0;

        for (PlayerGame playerGame : playerGames.active()) {
            Player player = playerGame.getPlayer();
            try {
                String board = cacheBoards.get(player);
                // TODO в конце концов если if (pair == null || pair.noSockets()) то ничего не отправляется, и зря гоняли но вроде как из кеша берем, так что проблем быть не должно
                if (playerController.requestControl(player, board)) {
                    requested++; // TODO test me
                }
            } catch (Exception e) {
                log.error("Unable to send control request to player " + player.getId() +
                        " URL: " + player.getCallbackUrl(), e);
            }
        }
        log.debug("tick().requestControls() {} players", requested);
    }

    private void sendScreenUpdates() {
        Map<ScreenRecipient, ScreenData> map = buildScreenData();
        sendScreenForWebSockets(map);
    }

    private Map<ScreenRecipient, ScreenData> buildScreenData() {
        Map<ScreenRecipient, ScreenData> map = new HashMap<>();
        cacheBoards.clear();

        Map<String, GameData> gameDataMap = playerGamesView.getGamesDataMap();
        Map<String, Integer> lastChatIds = chat.getLastMessageIds();
        for (PlayerGame playerGame : playerGames) {
            Game game = playerGame.getGame();
            Player player = playerGame.getPlayer();
            try {
                Integer lastChatMessage = lastChatIds.get(player.getRoom());

                String gameType = playerGame.getGameType().name();
                GameData gameData = gameDataMap.get(player.getId());

                // TODO вот например для бомбера всем отдаются одни и те же борды, отличие только в паре спрайтов
                Object encoded = null;
                try {
                    Object board = game.getBoardAsString(); // TODO дольше всего строчка выполняется, прооптимизировать!

                    GuiPlotColorDecoder decoder = gameData.getDecoder();
                    cacheBoards.put(player, decoder.encodeForClient(board));
                    encoded = decoder.encodeForBrowser(board);
                } catch (Exception e) {
                    log.error("Error during draw board for player: " + player.getId(), e);
                }

                int boardSize = gameData.getBoardSize();
                Object score = player.getScore();
                String message = player.getMessage();
                Map<String, Object> scores = gameData.getScores();
                List<String> group = gameData.getGroup();
                Map<String, HeroData> coordinates = gameData.getCoordinates();
                Map<String, String> readableNames = gameData.getReadableNames();
                map.put(player, new PlayerData(boardSize,
                        encoded,
                        gameType,
                        score,
                        message,
                        scores,
                        coordinates,
                        readableNames,
                        group,
                        lastChatMessage));

            } catch (Exception e) {
                log.error("Unable to send screen updates to player " + player.getId() +
                        " URL: " + player.getCallbackUrl(), e);
                e.printStackTrace();
            }
        }

        return map;
    }

    private void sendScreenForWebSockets(Map<ScreenRecipient, ScreenData> map) {
        try {
            screenController.requestControlToAll(map);
        } catch (Exception e) {
            log.error("Unable to send screen updates to all players", e);
            e.printStackTrace();
        }
    }

    @Override
    public List<Player> getAll() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(playerGames.players());
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Player> getAll(String game) {
        lock.readLock().lock();
        try {
            return playerGames.getPlayersByGame(game);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Player> getAllInRoom(String room) {
        lock.readLock().lock();
        try {
            return playerGames.getPlayersByRoom(room);
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void remove(String id) {
        lock.writeLock().lock();
        try {
            Player player = getPlayer(id);

            log.debug("Unregistered user {} from game {}",
                    player.getId(), player.getGame());

            playerGames.remove(player);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void updateAll(List<? extends Player> players) {
        lock.writeLock().lock();
        try {
            if (players == null) {
                return;
            }
            players.stream()
                    // TODO почему тут могут быть id null посмотреть git history?
                    .filter(player -> player.getId() != null)
                    .forEach(player -> updatePlayer(found(player), player));
        } finally {
            lock.writeLock().unlock();
        }
    }

    private PlayerGame found(Player player) {
        PlayerGame playerGame = playerGames.get(player.getId());
        if (playerGame == NullPlayerGame.INSTANCE) {
            throw new IllegalArgumentException("Player not found by id: " + player.getId());
        }
        return playerGame;
    }

    @Override
    public void update(Player player) {
        lock.writeLock().lock();
        try {
            updatePlayer(found(player), player);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void updatePlayer(PlayerGame playerGame, Player input) {
        Player updated = playerGame.getPlayer();

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

        boolean updateId = !playerGame.getPlayer().getId().equals(input.getId());
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
                && !playerGame.getRoom().equals(input.getRoom());
        if (updateRoomName) {
            String id = input.getId();
            String callbackUrl = updated.getCallbackUrl();
            String newRoom = input.getRoom();
            String newGame = input.getGame();
            String game = updated.getGame();
            changeRoom(id, game, newGame, newRoom, callbackUrl);
        }
        
        Game game = playerGame.getGame();
        boolean saveExists = game != null && (game.getSave() != null || updateRoomName);
        if (saveExists) {
            String oldSave = game.getSave().toString();
            String newSave = input.getData();
            boolean updateSave = !PlayerSave.isSaveNull(newSave) && !newSave.equals(oldSave);
            if (updateSave) {
                playerGames.setLevel(
                        input.getId(),
                        new JSONObject(newSave));
            }
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
            playerGames.changeRoom(id, game, newRoom);
        } else {
            // меняем игру
            remove(id);
            register(id, newGame, newRoom, url);
        }
    }

    @Override // TODO test me
    public void loadSaveForAll(String room, String newSave) {
        lock.writeLock().lock();
        try {
            List<Player> players = playerGames.getPlayersByRoom(room);
            players.forEach(player -> playerGames.setLevel(
                    player.getId(),
                    new JSONObject(newSave)));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public boolean contains(String id) {
        lock.readLock().lock();
        try {
            return getPlayer(id) != NullPlayer.INSTANCE;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Player get(String id) {
        lock.readLock().lock();
        try {
            return getPlayer(id);
        } finally {
            lock.readLock().unlock();
        }
    }

    private Player getPlayer(String id) {
        return playerGames.get(id).getPlayer();
    }

    @Override
    public void removeAll() {
        lock.writeLock().lock();
        try {
            playerGames.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void removeAll(String room) {
        lock.writeLock().lock();
        try {
            playerGames.getAll(withRoom(room))
                    .stream()
                    .map(pg -> pg.getPlayer())
            // TODO тут раньше сносились все комнаты напрямую, но spreader не трогали, и тесты не тестируют это
                    .forEach(playerGames::remove);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Joystick getJoystick(String id) {
        lock.writeLock().lock();
        try {
            return playerGames.get(id).getGame().getJoystick();
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void closeRegistration() {
        config.setRegistrationOpened(false);
    }

    @Override
    public boolean isRegistrationOpened() {
        return config.isRegistrationOpened();
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
        lock.writeLock().lock();
        try {
            semifinal.clean();

            List<PlayerGame> active = playerGames.all();
            active.forEach(PlayerGame::clearScore);

            List<String> saved = saver.getSavedList();
            clearAllSavedScores(active, saved);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void cleanAllScores(String room) {
        lock.writeLock().lock();
        try {
            semifinal.clean(room);

            List<PlayerGame> active = playerGames.getAll(withRoom(room));
            active.forEach(PlayerGame::clearScore);

            List<String> saved = saver.getSavedList(room);
            clearAllSavedScores(active, saved);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void clearAllSavedScores(List<PlayerGame> active, List<String> saved) {
        long now = time.now();
        saved.forEach(id -> cleanSavedScore(now, id));

        List<PlayerGame> notSaved = active.stream()
                .filter(exclude(saved))
                .collect(toList());
        saver.saveGames(notSaved, now);
    }

    public void cleanSavedScore(long now, String id) {
        PlayerSave playerSave = saver.loadGame(id);
        GameType type = roomService.gameType(playerSave.getRoom());
        String save = gameService.getDefaultProgress(type);
        Player player = new Player(playerSave);
        player.setScore(0);
        saver.saveGame(player, save, now);
    }

    @Override
    public void cleanScores(String id) {
        lock.writeLock().lock();
        try {
            playerGames.get(id).clearScore();
            cleanSavedScore(time.now(), id);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override // TODO test me
    public void reloadAllRooms() {
        lock.writeLock().lock();
        try {
            playerGames.reloadAll(true);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override // TODO test me
    public void reloadAllRooms(String room) {
        lock.writeLock().lock();
        try {
            playerGames.reloadAll(true, withRoom(room));
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public Player getRandom(String game) {
        lock.readLock().lock();
        try {
            if (playerGames.isEmpty()) return NullPlayer.INSTANCE;

            if (game == null) {
                return playerGames.iterator().next().getPlayer();
            }

            Iterator<Player> iterator = playerGames.getPlayersByGame(game).iterator();
            if (!iterator.hasNext()) return NullPlayer.INSTANCE;
            return iterator.next();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public GameType getAnyGameWithPlayers() {
        lock.readLock().lock();
        try {
            if (playerGames.isEmpty()) return NullGameType.INSTANCE;

            return playerGames.iterator().next().getGameType();
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public Map<String, Integer> getRoomCounts() {
        lock.readLock().lock();
        try {
            List<Player> players = playerGames.players();
            return roomService.names().stream()
                    .map(room -> new HashMap.SimpleEntry<>(room, count(players, room)))
                    .collect(toMap(entry -> entry.getKey(),
                            entry -> entry.getValue(),
                            (value1, value2) -> value2,
                            LinkedHashMap::new));
        } finally {
            lock.readLock().unlock();
        }
    }

    private int count(List<Player> players, String room) {
        return (int) players.stream()
                .filter(player -> room.equals(player.getRoom()))
                .count();
    }
}