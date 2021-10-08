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
import com.codenjoy.dojo.services.log.DebugService;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.services.security.ViewDelegationService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static java.util.function.Predicate.not;

@Controller
@RequestMapping(AdminController.URI)
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@Slf4j
@RequiredArgsConstructor
// TODO move all business logic to the AdminService
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
    private final ViewDelegationService viewDelegationService;
    private final SemifinalService semifinal;
    private final RoomService roomService;

    private final AdminService adminService;

    // TODO ROOM а этот метод вообще зачем?
    @GetMapping(params = {"player", "data"})
    public String loadDealFromSave(@RequestParam("player") String id,
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
    public String saveDeal(@PathVariable("player") String id,
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
    public String loadDeal(@PathVariable("player") String id,
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

        playerService.getAllInRoom(room).stream()
                .filter(not(Player::hasAi))
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
        saveService.getSaves(room)
                .forEach(player -> registration.remove(player.getId()));
        return getAdmin(room);
    }

    // ----------------

    @GetMapping("/player/reloadAll")
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

    @GetMapping("/game/board/reloadAll")
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

    @GetMapping("/room/registration/stop")
    public String openRoom(HttpServletRequest request) {
        String room = getGameRoom(request);
        roomService.setOpened(room, false);
        return getAdmin(request);
    }

    @GetMapping("/room/registration/start")
    public String closeRoom(HttpServletRequest request) {
        String room = getGameRoom(request);
        roomService.setOpened(room, true);
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

        adminService.saveSettings(settings);

        String room = settings.getRoom();
        request.setAttribute("room", room);
        return getAdmin(room);
    }

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
        AdminSettings settings = adminService.getAdminSettings(gameType, room);
        model.addAttribute("adminSettings", settings);
        model.addAttribute("active", roomService.isActive(room));
        model.addAttribute("roomOpened", roomService.isOpened(room));
        model.addAttribute("recording", actionLogger.isWorking());
        model.addAttribute("autoSave", autoSaver.isWorking());
        model.addAttribute("debugLog", debugService.isWorking());
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gamesRooms", roomService.gamesRooms());
        model.addAttribute("playersCount", playerService.getRoomCounts());

        return "admin";
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

}