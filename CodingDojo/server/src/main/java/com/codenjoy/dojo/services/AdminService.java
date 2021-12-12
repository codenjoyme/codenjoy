package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.incativity.InactivitySettings;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.level.LevelsSettings;
import com.codenjoy.dojo.services.level.LevelsSettingsImpl;
import com.codenjoy.dojo.services.log.DebugService;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.security.GameAuthorities;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.services.semifinal.SemifinalSettingsImpl;
import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.controller.admin.AdminPostActions;
import com.codenjoy.dojo.web.controller.admin.AdminSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.TriConsumer;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.incativity.InactivitySettings.INACTIVITY;
import static com.codenjoy.dojo.services.level.LevelsSettings.LEVELS;
import static com.codenjoy.dojo.services.round.RoundSettings.ROUNDS;
import static com.codenjoy.dojo.services.semifinal.SemifinalSettings.SEMIFINAL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminService {

    private final TimerService timerService;
    private final TimeService timeService;
    private final PlayerService playerService;
    private final SaveService saveService;
    private final GameService gameService;
    private final ActionLogger actionLogger;
    private final AutoSaver autoSaver;
    private final DebugService debugService;
    private final Registration registration;
    private final SemifinalService semifinal;
    private final RoomService roomService;
    private final StatisticService statistics;
    private final AdminPostActions actions;
    private final Validator validator;

    private final Map<String, TriConsumer<AdminSettings, String, String>> map = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        map.put(actions.deleteRoom, this::deleteRoom);
        map.put(actions.createRoom, this::createRoom);
        map.put(actions.saveActiveGames, this::saveActiveGames);
        map.put(actions.setTimerPeriod, this::setTimerPeriod);
        map.put(actions.pauseGame, this::pauseGame);
        map.put(actions.resumeGame, this::resumeGame);
        map.put(actions.stopRecording, this::stopRecording); 
        map.put(actions.startRecording, this::startRecording);
        map.put(actions.stopDebug, this::stopDebug);
        map.put(actions.startDebug, this::startDebug);          
        map.put(actions.updateLoggers, this::updateLoggers);          
        map.put(actions.stopAutoSave, this::stopAutoSave);          
        map.put(actions.startAutoSave, this::startAutoSave);          
        map.put(actions.closeRegistration, this::closeRegistration);          
        map.put(actions.openRegistration, this::openRegistration);          
        map.put(actions.closeRoomRegistration, this::closeRoomRegistration);
        map.put(actions.openRoomRegistration, this::openRoomRegistration);          
        map.put(actions.cleanAllScores, this::cleanAllScores);          
        map.put(actions.reloadAllRooms, this::reloadAllRooms);          
        map.put(actions.reloadAllPlayers, this::reloadAllPlayers);          
        map.put(actions.loadSaveForAll, this::loadSaveForAll);
        // TODO without backend
        // map.put(actions.saveRegistrationFormSettings, this::saveRegistrationFormSettings);
        map.put(actions.createDummyUsers, this::createDummyUsers);
        map.put(actions.updateRoundsSettings, this::updateRoundsSettings);
        map.put(actions.updateSemifinalSettings, this::updateSemifinalSettings);
        map.put(actions.updateInactivitySettings, this::updateInactivitySettings);
        map.put(actions.updateOtherSettings, this::updateOtherSettings);
        // TODO without backend
        // map.put(actions.addNewLevelMap, this::addNewLevelMap);
        map.put(actions.saveLevelsMaps, this::saveLevelsMaps);
        map.put(actions.updateAllPlayers, this::updateAllPlayers);
        map.put(actions.saveAllPlayers, this::saveAllPlayers);
        map.put(actions.loadAllPlayers, this::loadAllPlayers);
        map.put(actions.removeAllPlayersSaves, this::removeAllPlayersSaves);
        map.put(actions.removeAllPlayersRegistrations, this::removeAllPlayersRegistrations);
        map.put(actions.gameOverAllPlayers, this::gameOverAllPlayers);
        map.put(actions.loadAIsForAllPlayers, this::loadAIsForAllPlayers);
        map.put(actions.savePlayer, this::savePlayer);
        map.put(actions.loadPlayer, this::loadPlayer);
        map.put(actions.removePlayersSave, this::removePlayersSave);
        map.put(actions.removePlayerRegistration, this::removePlayerRegistration);
        map.put(actions.gameOverPlayer, this::gameOverPlayer);
        map.put(actions.loadAIForPlayer, this::loadAIForPlayer);
    }

    private void deleteRoom(AdminSettings settings, String game, String room) {
        // нельзя удалять единственную комнату соответствующую игре,
        // потому что потом зайти некуда будет
        if (roomService.game(room).equals(room)) {
            return;
        }

        playerService.removeAll(room);
        saveService.removeAllSaves(room);
        roomService.remove(room);
        // после зачистки перейдем в default game room
        settings.setRoom(game);
    }

    private void createRoom(AdminSettings settings, String game, String room) {
        room = settings.getNewRoom();

        validator.checkRoom(room, Validator.CANT_BE_NULL);

        // если нет такой room
        if (!roomService.exists(room)){
            GameType gameType = gameService.getGameType(game);
            // проверяем есть ли game
            if (gameType instanceof NullGameType) {
                return;
            }
            // создаем новую комнату
            roomService.create(room, gameType);
        }
        // и тут же будем администрировать новую комнату
        settings.setRoom(room);
    }

    private void saveActiveGames(AdminSettings settings, String game, String room) {
        List<Object> games = settings.getActiveGames();
        List<String> opened = new LinkedList<>();
        List<String> allGames = gameService.getGames();
        if (games.size() != allGames.size()) {
            throw new IllegalStateException("The list of games is not complete");
        }
        for (int index = 0; index < allGames.size(); index++) {
            if (games.get(index) != null) {
                opened.add(allGames.get(index));
            }
        }

        // TODO #4FS тут проверить
        roomService.setOpenedGames(opened);
    }

    private void setTimerPeriod(AdminSettings settings, String game, String room) {
        timerService.changePeriod(settings.getTimerPeriod());
    }

    private void pauseGame(AdminSettings settings, String game, String room) {
        roomService.setActive(room, false);
    }

    private void resumeGame(AdminSettings settings, String game, String room) {
        roomService.setActive(room, true);
    }

    private void stopRecording(AdminSettings settings, String game, String room) {
        actionLogger.pause();
    }

    private void startRecording(AdminSettings settings, String game, String room) {
        actionLogger.resume();
    }

    private void stopDebug(AdminSettings settings, String game, String room) {
        debugService.pause();
    }

    private void startDebug(AdminSettings settings, String game, String room) {
        debugService.resume();
    }

    private void updateLoggers(AdminSettings settings, String game, String room) {
        debugService.setLoggersLevels(settings.getLoggersLevels());
    }

    private void stopAutoSave(AdminSettings settings, String game, String room) {
        autoSaver.pause();
    }

    private void startAutoSave(AdminSettings settings, String game, String room) {
        autoSaver.resume();
    }

    private void closeRegistration(AdminSettings settings, String game, String room) {
        playerService.closeRegistration();
    }

    private void openRegistration(AdminSettings settings, String game, String room) {
        playerService.openRegistration();
    }

    private void closeRoomRegistration(AdminSettings settings, String game, String room) {
        roomService.setOpened(room, false);
    }

    private void openRoomRegistration(AdminSettings settings, String game, String room) {
        roomService.setOpened(room, true);
    }

    private void cleanAllScores(AdminSettings settings, String game, String room) {
        playerService.cleanAllScores(room);
    }

    private void reloadAllRooms(AdminSettings settings, String game, String room) {
        playerService.reloadAllRooms(room);
    }

    private void reloadAllPlayers(AdminSettings settings, String game, String room) {
        saveService.saveAll(room);
        playerService.removeAll(room);
        saveService.loadAll(room);
    }

    private void loadSaveForAll(AdminSettings settings, String game, String room) {
        playerService.loadSaveForAll(room, settings.getProgress());
    }

    private void createDummyUsers(AdminSettings settings, String game, String room) {
        String mask = settings.getGenerateNameMask();
        int count = settings.getGenerateCount();

        int numLength = String.valueOf(count).length();

        int created = 0;
        int index = 0;
        while (created != count) {
            String number = StringUtils.leftPad(String.valueOf(++index), numLength, "0");
            String id = mask.replaceAll("%", number);

            if (playerService.contains(id) && index < playerService.getAll().size()) {
                continue;
            }

            created++;
            String code = register(id);
            playerService.register(id, game, room, "127.0.0.1");
        }
    }

    private void updateRoundsSettings(AdminSettings settings, String game, String room) {
        roundSettings(room).update(settings.getRounds());
    }

    private void updateSemifinalSettings(AdminSettings settings, String game, String room) {
        semifinalSettings(room).update(settings.getSemifinal());
        semifinal.clean(room);
    }

    void updateInactivitySettings(AdminSettings settings, String game, String room) {
        InactivitySettingsImpl updated = settings.getInactivity();

        InactivitySettingsImpl actual = inactivitySettings(room);
        boolean changed = actual
                .update(updated)
                .getInactivityParams().stream()
                .anyMatch(parameter -> ((Parameter) parameter).changed());
        actual.changesReacted();
        if (changed) {
            playerService.getAllInRoom(room).stream()
                    .filter(not(Player::hasAi))
                    .forEach(player -> player.setLastResponse(timeService.now()));
        }
    }

    private void updateOtherSettings(AdminSettings settings, String game, String room) {
        List<Object> updated = settings.getOtherValues();
        if (updated == null) {
            return;
        }

        List<Exception> errors = new LinkedList<>();

        Settings gameSettings = gameService.getGameType(game, room).getSettings();
        List<Parameter> actual = gameSettings.getParameters().stream()
                .filter(onlyUngrouped())
                .collect(toList());
        for (int index = 0; index < actual.size(); index++) {
            try {
                Parameter parameter = actual.get(index);
                Object value = updated.get(index);
                value = fixForCheckbox(parameter, value);
                parameter.update(value);
            } catch (Exception e) {
                errors.add(e);
            }
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors);
        }
    }

    private void saveLevelsMaps(AdminSettings settings, String game, String room) {
        gameService.getGameType(game, room)
                .getSettings()
                .updateAll(
                        onlyLevels(),
                        settings.getLevelsKeys(),
                        settings.getLevelsNewKeys(),
                        settings.getLevelsValues());
    }

    private void updateAllPlayers(AdminSettings settings, String game, String room) {
        playerService.updateAll(settings.getPlayers());
    }

    private void saveAllPlayers(AdminSettings settings, String game, String room) {
        saveService.saveAll(room);
    }

    private void loadAllPlayers(AdminSettings settings, String game, String room) {
        saveService.loadAll(room);
    }

    private void removeAllPlayersSaves(AdminSettings settings, String game, String room) {
        saveService.removeAllSaves(room);
    }

    private void removeAllPlayersRegistrations(AdminSettings settings, String game, String room) {
        saveService.getSaves(room)
                .forEach(player -> registration.remove(player.getId()));
    }

    private void gameOverAllPlayers(AdminSettings settings, String game, String room) {
        playerService.removeAll(room);
    }

    private void loadAIsForAllPlayers(AdminSettings settings, String game, String room) {
        playerService.getAllInRoom(room).stream()
                .filter(not(Player::hasAi))
                .map(Player::getId)
                .forEach(playerService::reloadAI);
    }

    private void savePlayer(AdminSettings settings, String game, String room) {
        saveService.save(settings.getPlayer());
    }

    private void loadPlayer(AdminSettings settings, String game, String room) {
        saveService.load(settings.getPlayer());
    }

    private void removePlayersSave(AdminSettings settings, String game, String room) {
        saveService.removeSave(settings.getPlayer());
    }

    private void removePlayerRegistration(AdminSettings settings, String game, String room) {
        registration.remove(settings.getPlayer());
    }

    private void gameOverPlayer(AdminSettings settings, String game, String room) {
        playerService.remove(settings.getPlayer());
    }

    private void loadAIForPlayer(AdminSettings settings, String game, String room) {
        playerService.reloadAI(settings.getPlayer());
    }

    private InactivitySettingsImpl inactivitySettings(String room) {
        return InactivitySettings.get(roomService.settings(room));
    }

    public void saveSettings(AdminSettings settings) {
        String game = settings.getGame();
        String room = settings.getRoom();

        validator.checkGame(game, Validator.CANT_BE_NULL);
        validator.checkRoom(room, Validator.CANT_BE_NULL);

        if (!map.containsKey(settings.getAction())) {
            throw new IllegalArgumentException(
                    "Admin action not found: " + settings.getAction());
        }

        map.get(settings.getAction()).accept(settings, game, room);
    }

    private Object fixForCheckbox(Parameter parameter, Object value) {
        if (value == null && parameter.getType().equals(CheckBox.TYPE)) {
            return false; // потому что так работает <form:checkbox
        }
        return value;
    }

    public static Predicate<Parameter> onlyUngrouped() {
        return Predicate.not(
                p -> p.getName().startsWith(SEMIFINAL)
                        || p.getName().startsWith(ROUNDS)
                        || p.getName().startsWith(LEVELS)
                        || p.getName().startsWith(INACTIVITY));
    }

    private Predicate<Parameter> onlyLevels() {
        return parameter -> parameter.getName().startsWith(LEVELS);
    }

    private String register(String id) {
        if (registration.registered(id)) {
            return registration.login(id, id);
        } else {
            return registration.register(id, id, id, id, "", GameAuthorities.USER.roles()).getCode();
        }
    }

    private SemifinalSettingsImpl semifinalSettings(String room) {
        return semifinal.semifinalSettings(room);
    }

    private RoundSettingsImpl roundSettings(String room) {
        return RoundSettings.get(roomService.settings(room));
    }

    private LevelsSettingsImpl levelsSettings(String room) {
        return LevelsSettings.get(roomService.settings(room));
    }

    public AdminSettings loadAdminPage(String game, String room) {
        validator.checkRoom(room, CAN_BE_NULL);
        validator.checkGame(game, CAN_BE_NULL);

        // если не установили оба - default админкa
        if (room == null && game == null) {
            return null;
        }

        // ну может хоть имя игры указали?
        if (room == null) {
            room = game;
        }

        // если нет такой room - default админка
        if (!roomService.exists(room)) {
            return null;
        }

        // получаем имя игры по комнате
        game = roomService.game(room);

        // получаем тип игры
        GameType gameType = gameService.getGameType(game, room);
        if (gameType instanceof NullGameType) {
            return null;
        }

        // готовим данные для странички
        return getAdminSettings(gameType, room);
    }

    private AdminSettings getAdminSettings(GameType gameType, String room) {
        AdminSettings result = new AdminSettings();

        setupSettings(gameType, room, result);

        // TODO #4FS тут проверить
        List<String> enabled = roomService.openedGames();
        result.setActiveGames(gameService.getGames().stream()
                .map(enabled::contains)
                .collect(toList()));

        List<PlayerInfo> saves = saveService.getSaves(room);
        result.setPlayers(preparePlayers(room, saves));

        result.setSemifinalTick(semifinal.getTime(room));
        result.setGame(gameType.name());
        result.setLoggersLevels(debugService.getLoggersLevels());
        result.setRoom(room);
        result.setGameVersion(gameType.getVersion());
        result.setGenerateNameMask("demo%");
        result.setGenerateCount(30);
        result.setTimerPeriod((int) timerService.getPeriod());
        result.setProgress(gameService.getDefaultProgress(gameType));
        result.setActive(roomService.isActive(room));
        result.setRoomOpened(roomService.isOpened(room));
        result.setRecording(actionLogger.isWorking());
        result.setAutoSave(autoSaver.isWorking());
        result.setDebugLog(debugService.isWorking());
        result.setOpened(playerService.isRegistrationOpened());
        result.setGamesRooms(roomService.gamesRooms());
        result.setPlayersCount(playerService.getRoomCounts());
        result.setStatistic(statistics);
        result.setActions(actions);

        return result;
    }

    private void setupSettings(GameType gameType, String room, AdminSettings result) {
        // сохраняем для отображения semifinal settings pojo
        // TODO тут снова берем getSettings().getParameters()
        result.setSemifinal(semifinalSettings(room));
        // сохраняем для отображения round settings pojo
        // TODO тут снова берем getSettings().getParameters()
        result.setRounds(roundSettings(room));
        // сохраняем для отображения round settings pojo
        // TODO тут снова берем getSettings().getParameters()
        LevelsSettingsImpl levels = levelsSettings(room);
        // это надо для передачи данных на jsp
        result.setLevels(levels);
        // это надо для получения данных от jsp
        result.setLevelsValues(levels
                .getParameters().stream()
                .map(Parameter::getValue)
                .collect(toList()));
        result.setLevelsKeys(levels
                .getParameters().stream()
                .map(Parameter::getName)
                .collect(toList()));
        result.setLevelsNewKeys(levels
                .getParameters().stream()
                .map(Parameter::getName)
                .collect(toList()));
        // сохраняем для отображения inactivity settings pojo
        result.setInactivity(inactivitySettings(room));
        // отдельно оставшиеся параметры
        List<Parameter> parameters = gameType.getSettings().getParameters();
        parameters.removeIf(Predicate.not(onlyUngrouped()));
        result.setOther(parameters);
        result.setOtherValues(parameters.stream()
                .map(Parameter::getValue)
                .collect(toList()));
    }

    private List<PlayerInfo> preparePlayers(String room, List<PlayerInfo> players) {
        for (PlayerInfo player : players) {
            player.setHidden(!room.equals(player.getRoom()));
        }

        return players;
    }
}
