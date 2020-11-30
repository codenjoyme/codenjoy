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
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.security.GameAuthorities;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;

import static java.util.stream.Collectors.toList;

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

    @GetMapping(params = "save")
    public String savePlayerGame(@RequestParam("save") String id, Model model, HttpServletRequest request) {
        saveService.save(id);
        return getAdmin(request);
    }

    private String getAdmin(HttpServletRequest request) {
        return getAdmin(getGameName(request));
    }

    @GetMapping(params = "gameVersion")
    public @ResponseBody String getGameVersion(@RequestParam("gameVersion") String gameName) {
        return gameService.getGame(gameName).getVersion();
    }

    @GetMapping(params = "saveAll")
    public String saveAllGames(Model model, HttpServletRequest request) {
        saveService.saveAll();
        return getAdmin(request);
    }

    @GetMapping(params = "load")
    public String loadPlayerGame(@RequestParam("load") String id, Model model, HttpServletRequest request) {
        saveService.load(id);
        return getAdmin(request);
    }

    // TODO ROOM а этот метод вообще зачем?
    @GetMapping(params = {"player", "data"})
    public String loadPlayerGameFromSave(@RequestParam("player") String id,
                                         @RequestParam("data") String save,
                                         Model model, HttpServletRequest request)
    {
        saveService.load(id, getGameRoom(request), getGameName(request), save);
        return "redirect:/board/player/" + id;
    }

    @GetMapping(params = "reloadAI")
    public String reloadAI(@RequestParam("reloadAI") String id, Model model, HttpServletRequest request) {
        playerService.reloadAI(id);
        return getAdmin(request);
    }

    @GetMapping(params = "reloadAllAI")
    public String reloadAllAI(Model model, HttpServletRequest request) {
        playerService.getAll()
                .stream().filter(not(Player::hasAi))
                .map(Player::getId)
                .forEach(playerService::reloadAI);

        return getAdmin(request);
    }

    private <T> Predicate<T> not(Predicate<T> predicate) {
        return t -> !predicate.test(t);
    }

    @GetMapping(params = "loadAll")
    public String loadAllGames(Model model, HttpServletRequest request) {
        saveService.loadAll();
        return getAdmin(request);
    }

    @GetMapping(params = "gameOver")
    public String removePlayer(@RequestParam("gameOver") String name, HttpServletRequest request) {
        playerService.remove(name);
        return getAdmin(request);
    }

    @GetMapping(params = "removeSave")
    public String removePlayerSave(@RequestParam("removeSave") String id, HttpServletRequest request) {
        saveService.removeSave(id);
        return getAdmin(request);
    }

    @GetMapping(params = "removeRegistration")
    public String removePlayerRegistration(@RequestParam("removeRegistration") String name, Model model, HttpServletRequest request) {
        registration.remove(name);
        return getAdmin(request);
    }

    @GetMapping(params = "removeRegistrationAll")
    public String removePlayerRegistration(HttpServletRequest request) {
        registration.removeAll();
        return getAdmin(request);
    }

    @GetMapping(params = "removeSaveAll")
    public String removePlayerSave(HttpServletRequest request) {
        saveService.removeAllSaves();
        return getAdmin(request);
    }

    @GetMapping(params = "gameOverAll")
    public String gameOverAllPlayers(Model model, HttpServletRequest request) {
        playerService.removeAll();
        return getAdmin(request);
    }

    @GetMapping(params = "resetAll")
    public String resetAllPlayers(Model model, HttpServletRequest request) {
        saveService.saveAll();
        playerService.removeAll();
        saveService.loadAll();
        return "redirect:/";
    }

    // ----------------

    @GetMapping(params = "pause")
    public String pauseGame(Model model, HttpServletRequest request) {
        timerService.pause();
        return getAdmin(request);
    }


    @GetMapping(params = "resume")
    public String resumeGame(Model model, HttpServletRequest request) {
        timerService.resume();
        return getAdmin(request);
    }

    private void checkGameStatus(Model model) {
        model.addAttribute("paused", timerService.isPaused());
    }

    // ----------------

    @GetMapping(params = "stopDebug")
    public String stopDebug(Model model, HttpServletRequest request) {
        debugService.pause();
        return getAdmin(request);
    }

    @GetMapping(params = "startDebug")
    public String startDebug(Model model, HttpServletRequest request) {
        debugService.resume();
        return getAdmin(request);
    }

    private void checkDebugStatus(Model model) {
        model.addAttribute("debugLog", debugService.isWorking());
    }

    // ----------------

    @GetMapping(params = "stopAutoSave")
    public String stopAutoSave(Model model, HttpServletRequest request) {
        autoSaver.pause();
        return getAdmin(request);
    }

    @GetMapping(params = "startAutoSave")
    public String startAutoSave(Model model, HttpServletRequest request) {
        autoSaver.resume();
        return getAdmin(request);
    }

    private void checkAutoSaveStatus(Model model) {
        model.addAttribute("autoSave", autoSaver.isWorking());
    }

    // ----------------

    @GetMapping(params = "recording")
    public String recordingGame(Model model, HttpServletRequest request) {
        actionLogger.resume();
        return getAdmin(request);
    }

    @GetMapping(params = "stopRecording")
    public String stopRecordingGame(Model model, HttpServletRequest request) {
        actionLogger.pause();
        return getAdmin(request);
    }

    private void checkRecordingStatus(Model model) {
        model.addAttribute("recording", actionLogger.isWorking());
    }

    // ----------------

    @PostMapping()
    public String saveSettings(AdminSettings settings, BindingResult result, Model model, HttpServletRequest request) {
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

        if (settings.getProgress() != null) {
            playerService.loadSaveForAll(settings.getGameName(),
                    settings.getProgress());
        }

        if (settings.getGames() != null) {
            List<Parameter> games = (List)settings.getGames();
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

        List<Exception> errors = new LinkedList<>();
        if (settings.getParameters() != null) {
            Settings gameSettings = gameService.getGame(settings.getGameName()).getSettings();
            List<Parameter> parameters = gameSettings.getParameters();
            for (int index = 0; index < parameters.size(); index++) {
                try {
                    Parameter parameter = parameters.get(index);
                    Object value = settings.getParameters().get(index);
                    value = fixForCheckbox(parameter, value);
                    parameter.update(value);
                } catch (Exception e) {
                    errors.add(e);
                }
            }
        }
        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("There are errors during save settings: " + errors.toString());
        }

        if (settings.getGenerateNameMask() != null) {
            String mask = settings.getGenerateNameMask();
            int count = Integer.parseInt(settings.getGenerateCount());
            String roomName = settings.getGenerateRoomName();
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
                String code = getCode(id);
                playerService.register(id, "127.0.0.1", roomName, settings.getGameName());
            }
        }

        request.setAttribute(GAME_NAME_KEY, settings.getGameName());
        return getAdmin(settings.getGameName());
    }

    private String getCode(String id) {
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

    private String getAdmin(String gameName) {
        if (gameName == null) {
            return getAdmin();
        }
        return "redirect:/admin?" + GAME_NAME_KEY + "=" + gameName;
    }

    private String getAdmin() {
        return getAdmin(getDefaultGame());
    }

    private String getDefaultGame() {
        return gameService.getDefaultGame();
    }

    @GetMapping()
    public String getAdminPage(Model model,
                               @RequestParam(value = GAME_NAME_KEY, required = false) String gameName,
                               @RequestParam(value = CUSTOM_ADMIN_PAGE_KEY, required = false, defaultValue = "false")
                                           Boolean gameSpecificAdminPage) {

        gameName = (gameName == null || gameName.equals("null")) ? null : gameName;

        if (gameName == null) {
            return getAdmin(gameName);
        }

        if (gameSpecificAdminPage) {
            return viewDelegationService.adminView(gameName);
        }

        GameType game = gameService.getGame(gameName);

        if (game instanceof NullGameType) {
            return getAdmin(gameName);
        }

        Settings gameSettings = game.getSettings();
        List<Parameter> parameters = gameSettings.getParameters();

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

        model.addAttribute("adminSettings", settings);
        model.addAttribute("settings", parameters);
        model.addAttribute("semifinalTick", semifinal.getTime());
        model.addAttribute(GAME_NAME_KEY, gameName);
        model.addAttribute("gameVersion", game.getVersion());
        model.addAttribute("generateNameMask", "demo%");
        model.addAttribute("generateCount", "30");
        model.addAttribute("generateRoomName", gameName);
        model.addAttribute("timerPeriod", timerService.getPeriod());

        MultiplayerType type = gameService.getGame(gameName).getMultiplayerType();
        JSONObject save = type.progress().saveTo(new JSONObject());
        model.addAttribute("defaultProgress", save.toString().replace('"', '\''));

        checkGameStatus(model);
        checkRecordingStatus(model);
        checkAutoSaveStatus(model);
        checkDebugStatus(model);
        checkRegistrationClosed(model);
        prepareList(model, settings, gameName);

        return "admin";
    }

    private String getGameRoom(HttpServletRequest request) {
         String roomName = request.getParameter(ROOM_NAME_KEY);
        if (roomName == null || roomName.equals("null")) {
            return null;
        }
        return roomName;
    }

    private String getGameName(HttpServletRequest request) {
        String gameName = request.getParameter(GAME_NAME_KEY);
        if (gameName == null || gameName.equals("null")) {
            return null;
        }
        return gameName;
    }

    private void prepareList(Model model, AdminSettings settings, String gameName) {
        List<PlayerInfo> players = saveService.getSaves();

        Set<String> gameNames = new TreeSet<>(gameService.getGameNames());
        List<String> counts = new LinkedList<>();
        for (String name : gameNames) {
            int count = 0;
            for (PlayerInfo player : players) {
                if (name.equals(player.getGameName())) {
                    count++;
                }
            }
            String countPlayers = (count != 0) ? String.format("(%s)", count) : "";
            counts.add(countPlayers);
        }
        model.addAttribute("games", gameNames);
        model.addAttribute("gamesCount", counts);


        for (PlayerInfo player : players) {
            player.setHidden(!gameName.equals(player.getGameName()));
        }

        if (!players.isEmpty()) {
            model.addAttribute("players", players);
        }
        settings.setPlayers(players);
    }

    @GetMapping(params = "cleanAll")
    public String cleanAllPlayersScores(Model model, HttpServletRequest request) {
        playerService.cleanAllScores();
        return getAdmin(request);
    }

    @GetMapping(params = "reloadRooms")
    public String reloadAllPlayersRooms(Model model, HttpServletRequest request) {
        playerService.reloadAllRooms();
        return getAdmin(request);
    }

    @GetMapping(params = "select")
    public String selectGame(HttpServletRequest request, Model model, @RequestParam(GAME_NAME_KEY) String gameName) {
        if (gameName == null) {
            gameName = getDefaultGame();
        }
        request.setAttribute(GAME_NAME_KEY, gameName);
        return getAdmin(request);
    }

    @GetMapping(params = "close")
    public String close(Model model, HttpServletRequest request) {
        playerService.closeRegistration();
        return getAdmin(request);
    }

    private void checkRegistrationClosed(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
    }

    @GetMapping(params = "open")
    public String open(Model model, HttpServletRequest request) {
        playerService.openRegistration();
        return getAdmin(request);
    }

    @PostMapping("/user/info")
    public @ResponseBody String update(@RequestBody Player player) {
        try {
            playerService.update(player);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "{}";
    }

}
