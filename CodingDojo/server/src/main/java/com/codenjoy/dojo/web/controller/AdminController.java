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


    // TODO ROOM а этот метод вообще зачем?
    @GetMapping(params = {"player", "data"})
    public String loadPlayerGameFromSave(@RequestParam("player") String id,
                                         @RequestParam("data") String save,
                                         HttpServletRequest request)
    {
        saveService.load(id, getGameName(request), getGameRoom(request), save);
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
        String room = getGameRoom(request);
        saveService.saveAll(room);
        return getAdmin(room);
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
        String room = getGameRoom(request);
        saveService.loadAll(room);
        return getAdmin(room);
    }

    // ----------------

    @GetMapping("/player/{player}/ai/reload")
    public String reloadAI(@PathVariable("player") String id,
                           HttpServletRequest request) {
        playerService.reloadAI(id);
        return getAdmin(request);
    }

    @GetMapping("/player/ai/reloadAll")
    public String reloadAllAI(HttpServletRequest request) {
        String room = getGameRoom(request);

        playerService.getAllInRoom(room)
                .stream().filter(not(Player::hasAi))
                .map(Player::getId)
                .forEach(playerService::reloadAI);

        return getAdmin(room);
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
        String room = getGameRoom(request);
        playerService.removeAll(room);
        return getAdmin(room);
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
        String room = getGameRoom(request);
        saveService.removeAllSaves(room);
        return getAdmin(room);
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
        String room = getGameRoom(request);
        playerService.getAllInRoom(room)
                .forEach(player -> registration.remove(player.getId()));
        return getAdmin(room);
    }

    // ----------------

    @GetMapping("/game/board/reloadAll")
    public String resetAllPlayers(HttpServletRequest request) {
        String room = getGameRoom(request);
        saveService.saveAll(room);
        playerService.removeAll(room);
        saveService.loadAll(room);
        return getAdmin(room);
    }

    @GetMapping("/game/scores/cleanAll")
    public String cleanAllPlayersScores(HttpServletRequest request) {
        String room = getGameRoom(request);
        playerService.cleanAllScores(room);
        return getAdmin(room);
    }

    @GetMapping("/player/reloadAll")
    public String reloadAllPlayersRooms(HttpServletRequest request) {
        String room = getGameRoom(request);
        playerService.reloadAllRooms(room);
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
        String room = getGameRoom(request);
        roomService.setActive(room, false);
        return getAdmin(request);
    }

    @GetMapping("/game/resume")
    public String resumeGame(HttpServletRequest request) {
        String room = getGameRoom(request);
        roomService.setActive(room, true);
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

        String game = settings.getGame();
        String room = settings.getRoom();

        if (settings.getProgress() != null) {
            playerService.loadSaveForAll(room, settings.getProgress());
        }

        if (settings.getGames() != null) {
            List<Parameter> games = (List)settings.getGames();
            setEnable(games);
        }

        List<Exception> errors = new LinkedList<>();
        if (settings.getParameters() != null) {
            List<Object> updated = settings.getParameters();
            updateParameters(game, room, updated, errors);
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors.toString());
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = Integer.parseInt(settings.getGenerateCount());
            String generateRoom = settings.getGenerateRoom();
            generateNewPlayers(game, generateRoom, mask, count);
        }

        request.setAttribute("room", room);
        return getAdmin(room);
    }

    private void setEnable(List<Parameter> games) {
        List<String> toRemove = new LinkedList<>();
        List<String> allGames = gameService.getGames();
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

    public void updateParameters(String game, String room, List<Object> updated, List<Exception> errors) {
        Settings gameSettings = gameService.getGameType(game, room).getSettings();
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

    private String getAdmin(String room) {
        if (room == null) {
            return getAdmin();
        }
        return "redirect:/admin?" + "room" + "=" + room;
    }

    private String getAdmin() {
        return getAdmin(gameService.getDefaultRoom());
    }

    // ----------------

    @GetMapping()
    public String getAdmin(Model model,
                           @RequestParam(value = "room", required = false) String room,
                           @RequestParam(value = "game", required = false) String game,
                           @RequestParam(value = CUSTOM_ADMIN_PAGE_KEY, required = false, defaultValue = "false")
                               Boolean gameSpecificAdminPage)
    {
        // каждый из этих параметров может быть null, "", "null"
        room = Validator.isEmpty(room) ? null : room;
        game = Validator.isEmpty(game) ? null : game;

        // если не установили оба, идем на дифолтовую админку
        if (room == null && game == null) {
            return getAdmin();
        }

        // ну может хоть имя игры указали?
        if (room == null) {
            room = game;
        }

        // если нет такой room, првоеряем есть ли game
        if (!roomService.exists(room)) {
            GameType gameType = gameService.getGameType(game);
            if (gameType instanceof NullGameType) {
                // если нет - дифлотовая админка
                return getAdmin();
            }
            // иначе создаем новую комнату, которую тут же будем админить
            roomService.create(room, gameType);
        }

        // получаем уже законным образом имя игры по комнате
        game = roomService.game(room);

        // проверяем не надо ли нам перейти на кастомную страничку
        if (gameSpecificAdminPage && game != null) {
            return viewDelegationService.adminView(game);
        }

        // получаем тип игры
        GameType gameType = gameService.getGameType(game, room);
        if (gameType instanceof NullGameType) {
            return getAdmin();
        }

        // готовим данные для странички
        Settings gameSettings = gameType.getSettings();
        List<Parameter> parameters = gameSettings.getParameters();
        model.addAttribute("settings", parameters);
        model.addAttribute("semifinalTick", semifinal.getTime());
        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("gameVersion", gameType.getVersion());
        model.addAttribute("generateNameMask", "demo%");
        model.addAttribute("generateCount", "30");
        model.addAttribute("generateRoom", room);
        model.addAttribute("timerPeriod", timerService.getPeriod());
        model.addAttribute("defaultProgress", getDefaultProgress(gameType));
        model.addAttribute("active", roomService.isActive(room));
        model.addAttribute("recording", actionLogger.isWorking());
        model.addAttribute("autoSave", autoSaver.isWorking());
        model.addAttribute("debugLog", debugService.isWorking());
        model.addAttribute("opened", playerService.isRegistrationOpened());
        AdminSettings settings = getAdminSettings(parameters);
        model.addAttribute("adminSettings", settings);
        List<PlayerInfo> saves = saveService.getSaves();
        model.addAttribute("gameRooms", roomService.gameRooms());
        model.addAttribute("playersCount", getRoomCounts(saves));
        settings.setPlayers(preparePlayers(model, room, saves));

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

        Set<String> enabled = rooms.game();
        List<Object> games = gameService.getGames()
                                .stream()
                                .map(name -> enabled.contains(name))
                                .collect(toList());
        settings.setGames(games);
        return settings;
    }

    private String getGameRoom(HttpServletRequest request) {
        String result = request.getParameter("room");
        if (Validator.isEmpty(result)) {
            return null;
        }
        return result;
    }

    private String getGameName(HttpServletRequest request) {
        String result = request.getParameter("game");
        if (Validator.isEmpty(result)) {
            return null;
        }
        return result;
    }

    private List<PlayerInfo> preparePlayers(Model model, String room, List<PlayerInfo> players) {
        for (PlayerInfo player : players) {
            player.setHidden(!room.equals(player.getRoom()));
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
                .filter(player -> room.equals(player.getRoom()))
                .count();
    }

    // ----------------
}