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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequestMapping(BoardController.URI)
@RequiredArgsConstructor
public class BoardController {

    public static final String URI = "/board";

    private final PlayerService playerService;
    private final Registration registration;
    private final Validator validator;
    private final ConfigProperties properties;
    private final RegistrationService registrationService;

    @GetMapping("/player/{player}")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam(name = "only", required = false) Boolean justBoard)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);

        return boardPlayer(model, id, null, justBoard, (String) model.get("game"));
    }

    @GetMapping(value = "/player/{player}", params = {"code", "remove"})
    public String removePlayer(@PathVariable("player") String id, @RequestParam("code") String code) {
        validator.checkPlayerCode(id, code);

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        playerService.remove(player.getId());
        return "redirect:/";
    }

    @GetMapping(value = "/player/{player}", params = "code")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam("code") String code,
                              @RequestParam(name = "only", required = false) Boolean justBoard,
                              @RequestParam(name = "game", required = false, defaultValue = "") String game)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGame(game, CAN_BE_NULL); // TODO а зачем тут вообще game?

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        populateBoardAttributes(model, code, player, false);

        justBoard = justBoard != null && justBoard;
        model.addAttribute("justBoard", justBoard);
        return justBoard ? "board-only" : "board";
    }

    @GetMapping("/rejoining/{game}")
    public String rejoinGame(ModelMap model, @PathVariable("game") String game,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user)
    {
        validator.checkGame(game, CANT_BE_NULL);

        if (user == null) {
            return "redirect:/login?" + "game" + "=" + game;
        }

        // TODO ROOM а надо ли тут этот метод вообще, ниже есть более универсальный? 
        // TODO ROOM так как есть rest методы то может вообще убрать отсюда этих двоих?
        String room = game;
        return rejoinGame(model, game, room, request, user);
    }

    @GetMapping("/rejoining/{game}/room/{room}")
    public String rejoinGame(ModelMap model, @PathVariable("game") String game,
                             @PathVariable("room") String room,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user)
    {
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);

        Player player = playerService.get(user.getCode());
        if (player == NullPlayer.INSTANCE) {
            return registrationService.connectRegisteredPlayer(user.getCode(), request, user.getId(), room, game);
        }

        populateBoardAttributes(model, player.getCode(), player, false);
        return "board";
    }

    private void populateBoardAttributes(ModelMap model, String code, Player player, boolean allPlayersScreen) {
        populateBoardAttributes(model, code, player.getGame(), player.getRoom(), player.getGameOnly(), player.getId(),
                player.getReadableName(), allPlayersScreen);
    }

    private void populateBoardAttributes(ModelMap model, String code, String game, String room, String gameOnly,
                                         String playerId, String readableName, boolean allPlayersScreen) {
        model.addAttribute("code", code);
        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("allPlayersScreen", false);
        model.addAttribute("game", game);
        model.addAttribute("gameOnly", gameOnly);
        model.addAttribute("playerId", playerId);
        model.addAttribute("readableName", readableName);
        model.addAttribute("allPlayersScreen", allPlayersScreen); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport
        model.addAttribute("playerScoreCleanupEnabled", properties.isPlayerScoreCleanupEnabled());
    }

    @GetMapping(value = "/log/player/{player}", params = {"game", "room"})
    public String boardPlayerLog(ModelMap model, @PathVariable("player") String id,
                                 @RequestParam("game") String game,
                                 @RequestParam("room") String room)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);

        Optional<Registration.User> user = registration.getUserById(id);
        if (!user.isPresent()) {
            return "redirect:/register?id=" + id;
        }

        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("gameOnly", GameServiceImpl.removeNumbers(game));
        model.addAttribute("playerId", user.get().getId());
        model.addAttribute("readableName", user.get().getReadableName());

        return "board-log";
    }

    @GetMapping("/")
    public String boardAll() {
        GameType gameType = playerService.getAnyGameWithPlayers();
        if (gameType == NullGameType.INSTANCE) {
            return "redirect:/register";
        }
        return "redirect:/board/game/" + gameType.name();
    }

    @GetMapping("/game/{game}")
    public String boardAllGames(ModelMap model,
                                @PathVariable("game") String game,
                                @RequestParam(value = "code", required = false) String code,
                                @AuthenticationPrincipal Registration.User user)
    {
        // TODO возможно тут CAN_BE_NULL, иначе проверка (game == null) никогда не true
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        if (game == null) {
            return "redirect:/board" + code(code);
        }

        String room = game; // TODO закончить с room

        Player player = playerService.getRandom(game);
        if (player == NullPlayer.INSTANCE) {
            // TODO а это тут вообще надо?
            return "redirect:/register?" + "game" + "=" + game;
        }
        GameType gameType = player.getGameType(); // TODO а тут точно сеттинги румы а не игры?
        if (gameType.getMultiplayerType(gameType.getSettings()) == MultiplayerType.MULTIPLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        if (user != null && code == null) {
            code = user.getCode();
        }

        populateBoardAttributes(model, code, game, room, player.getGameOnly(), null, null, true);
        return "board";
    }

    @GetMapping(value = "/", params = "code")
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
        GameType gameType = player.getGameType(); // TODO а тут точно сеттинги румы а не игры?
        if (gameType.getMultiplayerType(gameType.getSettings()) != MultiplayerType.SINGLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        model.addAttribute("code", code);
        model.addAttribute("game", player.getGame());
        model.addAttribute("room", player.getRoom());
        model.addAttribute("gameOnly", player.getGameOnly());
        model.addAttribute("playerId", player.getId());
        model.addAttribute("readableName", player.getReadableName());
        model.addAttribute("allPlayersScreen", true);
        return "board";
    }

    private String code(@RequestParam("code") String code) {
        return (code != null)?"?code=" + code:"";
    }

}
