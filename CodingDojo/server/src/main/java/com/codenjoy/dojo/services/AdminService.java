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

    private final Map<String, TriConsumer<AdminSettings, String, String>> map = new ConcurrentHashMap<>();
    
    @PostConstruct
    public void init() {
        map.put(actions.deleteRoom, (settings, game, room) -> {
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
        });

        map.put(actions.createRoom, (settings, game, room) -> {
            room = settings.getNewRoom();
            // если нет такой room
            if (!roomService.exists(room)){
                GameType gameType = gameService.getGameType(game);
                // проверяем есть 0ли game
                if (gameType instanceof NullGameType) {
                    return;
                }
                // создаем новую комнату
                roomService.create(room, gameType);
            }
            // и тут же будем администрировать новую комнату
            settings.setRoom(room);
        });

        map.put(actions.saveActiveGames, (settings, game, room) -> {
            setActiveGames(settings.getActiveGames());
        });

        map.put(actions.setTimerPeriod, (settings, game, room) -> {
            try {
                timerService.changePeriod(settings.getTimerPeriod());
            } catch (NumberFormatException e) {
                // do nothing
            }
        });

        map.put(actions.pauseGame, (settings, game, room) -> {
            roomService.setActive(room, false);
        });

        map.put(actions.resumeGame, (settings, game, room) -> {
            roomService.setActive(room, true);
        });

        map.put(actions.stopRecording, (settings, game, room) -> {
            actionLogger.pause();
        });

        map.put(actions.startRecording, (settings, game, room) -> {
            actionLogger.resume();
        });

        map.put(actions.stopDebug, (settings, game, room) -> {
            debugService.pause();
        });

        map.put(actions.startDebug, (settings, game, room) -> {
            debugService.resume();
        });

        map.put(actions.updateLoggers, (settings, game, room) -> {
            debugService.setLoggersLevels(settings.getLoggersLevels());
        });

        map.put(actions.stopAutoSave, (settings, game, room) -> {
            autoSaver.pause();
        });

        map.put(actions.startAutoSave, (settings, game, room) -> {
            autoSaver.resume();
        });

        map.put(actions.closeRegistration, (settings, game, room) -> {
            playerService.closeRegistration();
        });

        map.put(actions.openRegistration, (settings, game, room) -> {
            playerService.openRegistration();
        });

        map.put(actions.closeRoomRegistration, (settings, game, room) -> {
            roomService.setOpened(room, false);
        });

        map.put(actions.openRoomRegistration, (settings, game, room) -> {
            roomService.setOpened(room, true);
        });

        map.put(actions.cleanAllScores, (settings, game, room) -> {
            playerService.cleanAllScores(room);
        });

        map.put(actions.reloadAllRooms, (settings, game, room) -> {
            playerService.reloadAllRooms(room);
        });

        map.put(actions.reloadAllPlayers, (settings, game, room) -> {
            saveService.saveAll(room);
            playerService.removeAll(room);
            saveService.loadAll(room);
        });

        map.put(actions.reloadAllPlayers, (settings, game, room) -> {
            playerService.loadSaveForAll(room, settings.getProgress());
        });

    }

    void updateInactivity(String room, InactivitySettings updated) {
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

    private InactivitySettingsImpl inactivitySettings(String room) {
        return InactivitySettings.get(roomService.settings(room));
    }

    public void saveSettings(AdminSettings settings) {
        String game = settings.getGame();
        String room = settings.getRoom();

        if (settings.getPlayers() != null) {
            playerService.updateAll(settings.getPlayers());
        }

        if (settings.getSemifinal() != null) {
            try {
                semifinalSettings(room)
                        .update(settings.getSemifinal());
                semifinal.clean(room);
            } catch (Exception e) {
                // do nothing
            }
        }

        if (settings.getLevels() != null) {
            try {
                levelsSettings(room)
                        .updateFrom(settings.getLevels().getParameters());
            } catch (Exception e) {
                // do nothing
            }
        }

        if (settings.getRounds() != null) {
            try {
                roundSettings(room)
                        .update(settings.getRounds());
            } catch (Exception e) {
                // do nothing
            }
        }

        if (settings.getInactivity() != null) {
            try {
                updateInactivity(room, settings.getInactivity());
            } catch (Exception e) {
                // do nothing
            }
        }
        
        if (!StringUtils.isEmpty(settings.getAction())) {
            map.get(settings.getAction()).accept(settings, game, room);
            room = settings.getRoom();
            game = settings.getGame();
        }

        Settings gameSettings = gameService.getGameType(game, room).getSettings();
        List<Exception> errors = new LinkedList<>();
        if (settings.getOtherValues() != null) {
            List<Object> updated = settings.getOtherValues();
            updateParameters(gameSettings, onlyUngrouped(), updated, errors);
        }
        if (settings.getLevelsValues() != null) {
            gameSettings.updateAll(
                    onlyLevels(),
                    settings.getLevelsKeys(),
                    settings.getLevelsNewKeys(),
                    settings.getLevelsValues());
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors);
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = settings.getGenerateCount();
            String generateRoom = settings.getGenerateRoom();
            generateNewPlayers(game, generateRoom, mask, count);
        }
    }

    private void setActiveGames(List<Object> games) {
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

    private void updateParameters(Settings gameSettings, Predicate<Parameter> filter,
                                 List<Object> updated, List<Exception> errors)
    {
        List<Parameter> actual = gameSettings.getParameters().stream()
                .filter(filter)
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

    private void generateNewPlayers(String game, String room, String mask, int count) {
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
        result.setGenerateRoom(room);
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
        result.setSemifinal(semifinalSettings(room)); // TODO тут снова берем getSettings().getParameters()
        // сохраняем для отображения round settings pojo
        result.setRounds(roundSettings(room)); // TODO тут снова берем getSettings().getParameters()
        // сохраняем для отображения round settings pojo
        LevelsSettingsImpl levels = levelsSettings(room);  // TODO тут снова берем getSettings().getParameters()
        result.setLevels(levels); // TODO а точно тут нада эту строчку?
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
