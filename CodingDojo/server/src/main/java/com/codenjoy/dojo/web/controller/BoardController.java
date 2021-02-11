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
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.security.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static com.codenjoy.dojo.web.controller.AdminController.GAME_NAME_KEY;
import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequiredArgsConstructor
public class BoardController {

    public static final String URI = "/board";

    private final PlayerService playerService;
    private final Registration registration;
    private final Validator validator;
    private final ConfigProperties properties;
    private final RegistrationService registrationService;

    @GetMapping(URI + "/player/{player}")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam(name = "only", required = false) Boolean justBoard)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);

        return boardPlayer(model, id, null, justBoard, (String) model.get("gameName"));
    }

    @GetMapping(value = URI + "/player/{player}", params = {"code", "remove"})
    public String removePlayer(@PathVariable("player") String id, @RequestParam("code") String code) {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkPlayerCode(id, code);

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        playerService.remove(player.getId());
        return "redirect:/";
    }

    @GetMapping(value = URI + "/player/{player}", params = "code")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam("code") String code,
                              @RequestParam(name = "only", required = false) Boolean justBoard,
                              @RequestParam(name = "gameName", required = false, defaultValue = "") String gameName)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGameName(gameName, CAN_BE_NULL); // TODO а зачем тут вообще gameName?

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        populateJoiningGameModel(model, code, player);

        justBoard = justBoard != null && justBoard;
        model.addAttribute("justBoard", justBoard);
        return justBoard ? "board-only" : "board";
    }

    @GetMapping(URI + "/rejoining/{gameName}")
    public String rejoinGame(ModelMap model, @PathVariable("gameName") String gameName,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user)
    {
        validator.checkGameName(gameName, CANT_BE_NULL);

        // TODO ROOM а надо ли тут этот метод вообще, ниже есть более универсальный? 
        // TODO ROOM так как есть rest методы то может вообще убрать отсюда этих двоих?
        String roomName = gameName; 
        return rejoinGame(model, gameName, roomName, request, user);
    }

    @GetMapping(URI + "/rejoining/{gameName}/room/{roomName}")
    public String rejoinGame(ModelMap model, @PathVariable("gameName") String gameName,
                             @PathVariable("roomName") String roomName,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user)
    {
        validator.checkGameName(gameName, CANT_BE_NULL);
        validator.checkRoomName(roomName, CANT_BE_NULL);

        Player player = playerService.get(user.getCode());
        if (player == NullPlayer.INSTANCE) {
            return registrationService.connectRegisteredPlayer(user.getCode(), request, user.getId(), roomName, gameName);
        }

        populateJoiningGameModel(model, player.getCode(), player);
        return "board";
    }

    private void populateJoiningGameModel(ModelMap model, String code, Player player) {
        model.addAttribute("code", code);
        model.addAttribute(GAME_NAME_KEY, player.getGameName());
        model.addAttribute("gameNameOnly", player.getGameNameOnly());
        model.addAttribute("playerId", player.getId());
        model.addAttribute("readableName", player.getReadableName());
        model.addAttribute("allPlayersScreen", false);
    }

    @GetMapping(value = URI + "/log/player/{player}", params = "gameName")
    public String boardPlayerLog(ModelMap model, @PathVariable("player") String id,
                                 @RequestParam("gameName") String gameName)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        Optional<Registration.User> user = registration.getUserById(id);
        if (!user.isPresent()) {
            return "redirect:/register?id=" + id;
        }

        model.addAttribute(GAME_NAME_KEY, gameName);
        model.addAttribute("gameNameOnly", GameServiceImpl.removeNumbers(gameName));
        model.addAttribute("playerId", user.get().getId());
        model.addAttribute("readableName", user.get().getReadableName());

        return "board-log";
    }

    @GetMapping(URI)
    public String boardAll() {
        GameType gameType = playerService.getAnyGameWithPlayers();
        if (gameType == NullGameType.INSTANCE) {
            return "redirect:/register";
        }
        return "redirect:/board/game/" + gameType.name();
    }

    @GetMapping(URI + "/game/{gameName}")
    public String boardAllGames(ModelMap model,
                                @PathVariable("gameName") String gameName,
                                @RequestParam(value = "code", required = false) String code)
    {
        // TODO возможно тут CAN_BE_NULL, иначе проверка (gameName == null) никогда не true
        validator.checkGameName(gameName, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        if (gameName == null) {
            return "redirect:/board" + code(code);
        }

        Player player = playerService.getRandom(gameName);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?" + GAME_NAME_KEY + "=" + gameName;
        }
        GameType gameType = player.getGameType();
        if (gameType.getMultiplayerType() == MultiplayerType.MULTIPLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        model.addAttribute("code", code);
        model.addAttribute(GAME_NAME_KEY, gameName);
        model.addAttribute("gameNameOnly", player.getGameNameOnly());
        model.addAttribute("playerId", null);
        model.addAttribute("readableName", null);
        model.addAttribute("allPlayersScreen", true); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport
        return "board";
    }

    @GetMapping(value = URI, params = "code")
    public String boardAll(ModelMap model, @RequestParam("code") String code) {
        validator.checkCode(code, CAN_BE_NULL);

        String id = registration.getIdByCode(code);
        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            player = playerService.getRandom(null);
        }
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register";
        }
        if (player.getGameType().getMultiplayerType() != MultiplayerType.SINGLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        String gameName = player.getGameName();
        model.addAttribute("code", code);
        model.addAttribute(GAME_NAME_KEY, gameName);
        model.addAttribute("gameNameOnly", player.getGameNameOnly());
        model.addAttribute("playerId", player.getId());
        model.addAttribute("readableName", player.getReadableName());
        model.addAttribute("allPlayersScreen", true);
        return "board";
    }

    private String code(@RequestParam("code") String code) {
        return (code != null)?"?code=" + code:"";
    }

    @GetMapping("/donate")
    public String donate(ModelMap model) {
        model.addAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("donateCode", properties.getDonateCode());
        return "donate-form";
    }

    @RequestMapping("/help")
    public String help() {
        return "help";
    }
}
