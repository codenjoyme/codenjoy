package com.codenjoy.dojo.web.controller;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.GameAuthorities;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.services.security.ViewDelegationService;
import com.codenjoy.dojo.services.settings.CheckBox;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Controller
@RequestMapping(AdminController.URI)
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@Slf4j
@RequiredArgsConstructor
public class AdminController {

    public static final String URI = "/admin";

    public static final String GAME_NAME_KEY = "gameName";
    public static final String ROOM_NAME_KEY = "roomName";
    public static final String CUSTOM_ADMIN_PAGE_KEY = "custom";

    private final TimerService timerService;
    private final PlayerService playerService;
    private final SaveService saveService;
    private final GameService gameService;
    private final ActionLogger actionLogger;
    private final AutoSaver autoSaver;
    private final DebugService debugService;
    private final Registration registration;
    private final RoomsAliaser rooms;
    private final ViewDelegationService viewDelegationService;
    private final Semifinal semifinal;
    private final RoomService roomService;

    @GetMapping("/gameVersion")
    public @ResponseBody String getGameVersion(@RequestParam(GAME_NAME_KEY) String gameName) {
        return gameService.getGame(gameName).getVersion();
    }

    // TODO ROOM а этот метод вообще зачем?
    @GetMapping(params = {"player", "data"})
    public String loadPlayerGameFromSave(@RequestParam("player") String id,
                                         @RequestParam("data") String save,
                                         HttpServletRequest request)
    {
        saveService.load(id, getGameRoom(request), getGameName(request), save);
        return "redirect:/board/player/" + id;
    }
    // используется как rest для апдейта полей конкретного player на admin page
    @PostMapping("/user/info")
    public @ResponseBody String update(@RequestBody Player player) {
        try {
            playerService.update(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

    // ----------------

    @GetMapping("/player/{player}/save")
    public String savePlayerGame(@PathVariable("player") String id,
                                 HttpServletRequest request)
    {
        saveService.save(id);
        return getAdmin(request);
    }

    @GetMapping("/player/saveAll")
    public String saveAllGames(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        saveService.saveAll(roomName);
        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/player/{player}/load")
    public String loadPlayerGame(@PathVariable("player") String id,
                                 HttpServletRequest request)
    {
        saveService.load(id);
        return getAdmin(request);
    }

    @GetMapping("/player/loadAll")
    public String loadAllGames(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        saveService.loadAll(roomName);
        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/player/{player}/ai/reload")
    public String reloadAI(@RequestParam("player") String id,
                           HttpServletRequest request) {
        playerService.reloadAI(id);
        return getAdmin(request);
    }

    @GetMapping("/player/ai/reloadAll")
    public String reloadAllAI(HttpServletRequest request) {
        String roomName = getGameRoom(request);

        playerService.getAllInRoom(roomName)
                .stream().filter(not(Player::hasAi))
                .map(Player::getId)
                .forEach(playerService::reloadAI);

        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/player/{player}/gameOver")
    public String removePlayer(@PathVariable("player") String id,
                               HttpServletRequest request)
    {
        playerService.remove(id);
        return getAdmin(request);
    }

    @GetMapping("/player/gameOverAll")
    public String gameOverAllPlayers(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        playerService.removeAll(roomName);
        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/player/{player}/save/remove")
    public String removePlayerSave(@PathVariable("player") String id,
                                   HttpServletRequest request) {
        saveService.removeSave(id);
        return getAdmin(request);
    }

    @GetMapping("/player/save/removeAll")
    public String removePlayerSave(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        saveService.removeAllSaves(roomName);
        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/player/{player}/registration/remove")
    public String removePlayerRegistration(@PathVariable("player") String id,
                                           HttpServletRequest request)
    {
        registration.remove(id);
        return getAdmin(request);
    }

    @GetMapping("/player/registration/removeAll")
    public String removePlayerRegistration(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        playerService.getAllInRoom(roomName)
                .forEach(player -> registration.remove(player.getId()));
        return getAdmin(roomName);
    }

    // ----------------

    @GetMapping("/game/board/reloadAll")
    public String resetAllPlayers(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        saveService.saveAll(roomName);
        playerService.removeAll(roomName);
        saveService.loadAll(roomName);
        return getAdmin(roomName);
    }

    @GetMapping("/game/scores/cleanAll")
    public String cleanAllPlayersScores(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        playerService.cleanAllScores(roomName);
        return getAdmin(roomName);
    }

    @GetMapping("/player/reloadAll")
    public String reloadAllPlayersRooms(HttpServletRequest request) {
        String roomName = getGameRoom(request);
        playerService.reloadAllRooms(roomName);
        return getAdmin(request);
    }

    // ----------------

    @GetMapping("/registration/start")
    public String close(HttpServletRequest request) {
        playerService.openRegistration();
        return getAdmin(request);
    }

    @GetMapping("/registration/stop")
    public String open(HttpServletRequest request) {
        playerService.closeRegistration();
        return getAdmin(request);
    }

    // ----------------

    @GetMapping("/game/pause")
    public String pauseGame(HttpServletRequest request) {
        timerService.pause();
        return getAdmin(request);
    }

    @GetMapping("/game/resume")
    public String resumeGame(HttpServletRequest request) {
        timerService.resume();
        return getAdmin(request);
    }

    // ----------------

    @GetMapping("/debug/stop")
    public String stopDebug(HttpServletRequest request) {
        debugService.pause();
        return getAdmin(request);
    }

    @GetMapping("/debug/start")
    public String startDebug(HttpServletRequest request) {
        debugService.resume();
        return getAdmin(request);
    }

    // ----------------

    @GetMapping("/autoSave/stop")
    public String stopAutoSave(HttpServletRequest request) {
        autoSaver.pause();
        return getAdmin(request);
    }

    @GetMapping("/autoSave/start")
    public String startAutoSave(HttpServletRequest request) {
        autoSaver.resume();
        return getAdmin(request);
    }

    // ----------------

    @GetMapping("/recording/start")
    public String startRecording(HttpServletRequest request) {
        actionLogger.resume();
        return getAdmin(request);
    }

    @GetMapping("/recording/stop")
    public String stopRecording(HttpServletRequest request) {
        actionLogger.pause();
        return getAdmin(request);
    }

    // ----------------

    @PostMapping()
    public String saveSettings(AdminSettings settings,
                               BindingResult result,
                               HttpServletRequest request)
    {
        if (!result.hasErrors()) {
            // do nothing
        }
        if (settings.getPlayers() != null) {
            playerService.updateAll(settings.getPlayers());
        }

        if (settings.getSemifinal() != null) {
            try {
                semifinal.settings().apply(settings.getSemifinal());
                semifinal.clean();
            } catch (Exception e) {
                // do nothing
            }
        }

        if (settings.getTimerPeriod() != null) {
            try {
                timerService.changePeriod(Integer.parseInt(settings.getTimerPeriod()));
            } catch (NumberFormatException e) {
                // do nothing
            }
        }

        String gameName = settings.getGameName();
        String roomName = settings.getRoomName();

        if (settings.getProgress() != null) {
            playerService.loadSaveForAll(roomName, settings.getProgress());
        }

        if (settings.getGames() != null) {
            List<Parameter> games = (List)settings.getGames();
            setEnable(games);
        }

        List<Exception> errors = new LinkedList<>();
        if (settings.getParameters() != null) {
            List<Object> updated = settings.getParameters();
            updateParameters(gameName, roomName, updated, errors);
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors.toString());
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = Integer.parseInt(settings.getGenerateCount());
            String room = settings.getGenerateRoomName();
            generateNewPlayers(gameName, room, mask, count);
        }

        request.setAttribute(ROOM_NAME_KEY, roomName);
        return getAdmin(roomName);
    }

    private void setEnable(List<Parameter> games) {
        List<String> toRemove = new LinkedList<>();
        List<String> allGames = gameService.getGameNames();
        if (games.size() != allGames.size()) {
            throw new IllegalStateException("Список игр к активации не полный");
        }
        for (int i = 0; i < allGames.size(); i++) {
            if (games.get(i) == null) {
                toRemove.add(allGames.get(i));
            }
        }

        rooms.enableGames(toRemove);
    }

    public void updateParameters(String gameName, String roomName, List<Object> updated, List<Exception> errors) {
        Settings gameSettings = gameService.getGame(gameName, roomName).getSettings();
        List<Parameter> actual = gameSettings.getParameters();
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

    public void generateNewPlayers(String gameName, String roomName, String mask, int count) {
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
            playerService.register(id, gameName, roomName, "127.0.0.1");
        }
    }

    private String register(String id) {
        if (registration.registered(id)) {
            return registration.login(id, id);
        } else {
            return registration.register(id, id, id, id, "", GameAuthorities.USER.roles()).getCode();
        }
    }

    private Object fixForCheckbox(Parameter parameter, Object value) {
        if (value == null && parameter.getType().equals(CheckBox.TYPE)) {
            return false; // потому что так работает <form:checkbox
        }
        return value;
    }

    // ----------------

    private String getAdmin(HttpServletRequest request) {
        return getAdmin(getGameRoom(request));
    }

    private String getAdmin(String roomName) {
        if (roomName == null) {
            return getAdmin();
        }
        return "redirect:/admin?" + ROOM_NAME_KEY + "=" + roomName;
    }

    private String getAdmin() {
        return getAdmin(gameService.getDefaultRoom());
    }

    // ----------------

    @GetMapping()
    public String getAdmin(Model model,
                           @RequestParam(value = ROOM_NAME_KEY, required = false)
                               String roomName,
                           @RequestParam(value = GAME_NAME_KEY, required = false)
                               String gameName,
                           @RequestParam(value = CUSTOM_ADMIN_PAGE_KEY, required = false, defaultValue = "false")
                               Boolean gameSpecificAdminPage)
    {
        // каждый из этих параметров может быть null, "", "null"
        roomName = Validator.isEmpty(roomName) ? null : roomName;
        gameName = Validator.isEmpty(gameName) ? null : gameName;

        // если не установили оба, идем на дифолтовую админку
        if (roomName == null && gameName == null) {
            return getAdmin();
        }

        // ну может хоть имя игры указали?
        if (roomName == null) {
            roomName = gameName;
        }

        // если нет такой roomName, првоеряем есть ли gameName
        if (!roomService.exists(roomName)) {
            GameType game = gameService.getGame(gameName);
            if (game instanceof NullGameType) {
                // если нет - дифлотовая админка
                return getAdmin();
            }
            // иначе создаем новую комнату, которую тут же будем админить
            roomService.create(roomName, game);
        }

        // получаем уже законным образом имя игры по комнате
        gameName = roomService.gameName(roomName);

        // проверяем не надо ли нам перейти на кастомную страничку
        if (gameSpecificAdminPage && gameName != null) {
            return viewDelegationService.adminView(gameName);
        }

        // получаем тип игры
        GameType game = gameService.getGame(gameName, roomName);
        if (game instanceof NullGameType) {
            return getAdmin();
        }

        // готовим данные для странички
        Settings gameSettings = game.getSettings();
        List<Parameter> parameters = gameSettings.getParameters();
        model.addAttribute("settings", parameters);
        model.addAttribute("semifinalTick", semifinal.getTime());
        model.addAttribute(GAME_NAME_KEY, gameName);
        model.addAttribute(ROOM_NAME_KEY, roomName);
        model.addAttribute("gameVersion", game.getVersion());
        model.addAttribute("generateNameMask", "demo%");
        model.addAttribute("generateCount", "30");
        model.addAttribute("generateRoomName", roomName);
        model.addAttribute("timerPeriod", timerService.getPeriod());
        model.addAttribute("defaultProgress", getDefaultProgress(game));
        model.addAttribute("paused", timerService.isPaused());
        model.addAttribute("recording", actionLogger.isWorking());
        model.addAttribute("autoSave", autoSaver.isWorking());
        model.addAttribute("debugLog", debugService.isWorking());
        model.addAttribute("opened", playerService.isRegistrationOpened());
        AdminSettings settings = getAdminSettings(parameters);
        model.addAttribute("adminSettings", settings);
        List<PlayerInfo> saves = saveService.getSaves();
        model.addAttribute("gameRooms", roomService.gameRooms());
        model.addAttribute("playersCount", getRoomCounts(saves));
        settings.setPlayers(preparePlayers(model, roomName, saves));

        return "admin";
    }

    public String getDefaultProgress(GameType game) {
        MultiplayerType type = game.getMultiplayerType(game.getSettings());
        JSONObject save = type.progress().saveTo(new JSONObject());
        return save.toString().replace('"', '\'');
    }

    public AdminSettings getAdminSettings(List<Parameter> parameters) {
        AdminSettings settings = new AdminSettings();

        settings.setSemifinal(semifinal.settings().clone());

        settings.setParameters(new LinkedList<>());
        for (Parameter p : parameters) {
            settings.getParameters().add(p.getValue());
        }

        Set<String> enabled = rooms.gameNames();
        List<Object> games = gameService.getGameNames()
                                .stream()
                                .map(name -> enabled.contains(name))
                                .collect(toList());
        settings.setGames(games);
        return settings;
    }

    private String getGameRoom(HttpServletRequest request) {
        String result = request.getParameter(ROOM_NAME_KEY);
        if (Validator.isEmpty(result)) {
            return null;
        }
        return result;
    }

    private String getGameName(HttpServletRequest request) {
        String result = request.getParameter(GAME_NAME_KEY);
        if (Validator.isEmpty(result)) {
            return null;
        }
        return result;
    }

    private List<PlayerInfo> preparePlayers(Model model, String roomName, List<PlayerInfo> players) {
        for (PlayerInfo player : players) {
            player.setHidden(!roomName.equals(player.getRoomName()));
        }

        if (!players.isEmpty()) {
            model.addAttribute("players", players);
        }
        return players;
    }

    private Map<String, Integer> getRoomCounts(List<PlayerInfo> players) {
        return roomService.names().stream()
                .map(room -> new HashMap.SimpleEntry<>(room, count(players, room)))
                .collect(toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    private int count(List<PlayerInfo> players, String room) {
        return (int) players.stream()
                .filter(player -> room.equals(player.getRoomName()))
                .count();
    }

    // ----------------
}