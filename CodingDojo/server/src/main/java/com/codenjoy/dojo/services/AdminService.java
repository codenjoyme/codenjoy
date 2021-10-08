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
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.security.GameAuthorities;
import com.codenjoy.dojo.services.security.ViewDelegationService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.services.semifinal.SemifinalSettingsImpl;
import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.AdminSettings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
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
    private final ViewDelegationService viewDelegationService;
    private final SemifinalService semifinal;
    private final RoomService roomService;

    public void updateInactivity(String room, InactivitySettings updated) {
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

    public InactivitySettingsImpl inactivitySettings(String room) {
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

        if (settings.getTimerPeriod() != null) {
            try {
                timerService.changePeriod(settings.getTimerPeriod());
            } catch (NumberFormatException e) {
                // do nothing
            }
        }

        if (settings.getProgress() != null) {
            playerService.loadSaveForAll(room, settings.getProgress());
        }

        if (settings.getGames() != null) {
            List<Parameter> games = (List) settings.getGames();
            setEnable(games);
        }

        Settings gameSettings = gameService.getGameType(game, room).getSettings();
        List<Exception> errors = new LinkedList<>();
        if (settings.getOtherValues() != null) {
            List<Object> updated = settings.getOtherValues();
            updateParameters(gameSettings, onlyUngrouped(), updated, errors);
        }
        if (settings.getLevelsValues() != null) {
            List<Object> updated = settings.getLevelsValues();
            updateParameters(gameSettings, onlyLevels(), updated, errors);
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors.toString());
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = settings.getGenerateCount();
            String generateRoom = settings.getGenerateRoom();
            generateNewPlayers(game, generateRoom, mask, count);
        }
    }

    private void setEnable(List<Parameter> games) {
        List<String> opened = new LinkedList<>();
        List<String> allGames = gameService.getGames();
        if (games.size() != allGames.size()) {
            throw new IllegalStateException("Список игр к активации регистрации не полный");
        }
        for (int i = 0; i < allGames.size(); i++) {
            if (games.get(i) != null) {
                opened.add(allGames.get(i));
            }
        }

        // TODO #4FS тут проверить
        roomService.setOpenedGames(opened);
    }

    public void updateParameters(Settings gameSettings, Predicate<Parameter> filter,
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

    public Predicate<Parameter> onlyUngrouped() {
        return Predicate.not(
                p -> p.getName().startsWith(SEMIFINAL)
                        || p.getName().startsWith(ROUNDS)
                        || p.getName().startsWith(LEVELS)
                        || p.getName().startsWith(INACTIVITY));
    }

    public Predicate<Parameter> onlyLevels() {
        return p -> p.getName().startsWith(LEVELS);
    }

    public void generateNewPlayers(String game, String room, String mask, int count) {
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

    public SemifinalSettingsImpl semifinalSettings(String room) {
        return semifinal.semifinalSettings(room);
    }

    private RoundSettingsImpl roundSettings(String room) {
        return RoundSettings.get(roomService.settings(room));
    }

    private LevelsSettingsImpl levelsSettings(String room) {
        return LevelsSettings.get(roomService.settings(room));
    }

    public AdminSettings getAdminSettings(GameType gameType, String room) {
        AdminSettings result = new AdminSettings();

        setupSettings(gameType, room, result);

        // TODO #4FS тут проверить
        List<String> enabled = roomService.openedGames();
        result.setGames(gameService.getGames().stream()
                .map(name -> enabled.contains(name))
                .collect(toList()));

        List<PlayerInfo> saves = saveService.getSaves(room);
        result.setPlayers(preparePlayers(room, saves));

        result.setSemifinalTick(semifinal.getTime(room));
        result.setGame(gameType.name());
        result.setRoom(room);
        result.setGameVersion(gameType.getVersion());
        result.setGenerateNameMask("demo%");
        result.setGenerateCount(30);
        result.setGenerateRoom(room);
        result.setTimerPeriod((int) timerService.getPeriod());
        result.setProgress(gameService.getDefaultProgress(gameType));

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
