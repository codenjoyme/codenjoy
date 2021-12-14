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
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static com.codenjoy.dojo.services.multiplayer.MultiplayerType.MULTIPLE;
import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static java.util.stream.Collectors.toMap;

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
    private final RoomService roomService;

    @GetMapping("/player/{player}")
    public String boardPlayer(ModelMap model, HttpServletRequest request,
                              @PathVariable("player") String id,
                              @RequestParam(name = "only", required = false) Boolean justBoard)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);

        return boardPlayer(model, request, id, null, justBoard);
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
    public String boardPlayer(ModelMap model, HttpServletRequest request,
                              @PathVariable("player") String id,
                              @RequestParam("code") String code,
                              @RequestParam(name = "only", required = false) Boolean justBoard)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        Player player = playerService.get(id);
        String onBehalfPlayerId = registration.getIdByCode(code);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        populateBoardAttributes(model, request, onBehalfPlayerId, code, player, false);

        justBoard = justBoard != null && justBoard;
        model.addAttribute("justBoard", justBoard);
        return justBoard ? "board-only" : "board";
    }

    private void populateQueries(ModelMap model, HttpServletRequest request) {
        String query = request.getQueryString();
        Map<String, String> parameters = queryToMap(query);

        validator.checkCustomQueryParameters(parameters);

        model.addAttribute("query", parameters);
    }

    public static Map<String, String> queryToMap(String query) {
        return Arrays.stream(query.split("&"))
                .map(param -> {
                    String[] split = param.split("=");
                    return new AbstractMap.SimpleEntry<>(
                            (split.length == 0) ? "" : split[0],
                            (split.length != 2) ? "" : split[1]);
                })
                .filter(entry -> !StringUtils.isEmpty(entry.getKey()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
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
            return registrationService.connectRegisteredPlayer(user.getCode(),
                    request, user.getId(), room, game);
        }

        populateBoardAttributes(model, request, player.getId(),
                player.getCode(), player, false);

        return "board";
    }

    private void populateBoardAttributes(ModelMap model, HttpServletRequest request,
                                         String onBehalfPlayerId, String code,
                                         Player player, boolean allPlayersScreen)
    {
        populateBoardAttributes(model, request, onBehalfPlayerId, code,
                player.getGame(), player.getRoom(), player.getGameOnly(),
                player.getId(), player.getReadableName(), allPlayersScreen);
    }

    private void populateBoardAttributes(ModelMap model, HttpServletRequest request,
                                         String onBehalfPlayerId, String code,
                                         String game, String room, String gameOnly,
                                         String playerId, String readableName,
                                         boolean allPlayersScreen)
    {
        populateQueries(model, request);

        model.addAttribute("code", code);
        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("gameOnly", gameOnly);
        model.addAttribute("authorizedPlayerId", (isAuthenticated()) ? onBehalfPlayerId : null);
        model.addAttribute("playerId", playerId);
        model.addAttribute("readableName", readableName);
        model.addAttribute("allPlayersScreen", allPlayersScreen); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport
        model.addAttribute("playerScoreCleanupEnabled", properties.isPlayerScoreCleanupEnabled());
    }

    private boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @GetMapping(value = "/log/player/{player}", params = {"game", "room"})
    public String boardPlayerLog(ModelMap model, HttpServletRequest request,
                                 @PathVariable("player") String id,
                                 @RequestParam("game") String game,
                                 @RequestParam("room") String room)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);

        Optional<Registration.User> user = registration.getUserById(id);
        if (user.isEmpty()) {
            return "redirect:/register?id=" + id;
        }

        populateBoardAttributes(model, request, id, null, game,
                room, GameServiceImpl.removeNumbers(game),
                id, user.get().getReadableName(), false);

        return "board-log";
    }

    @GetMapping("/")
    public String boardAll() {
        // TODO #4FS тут проверить
        String room = playerService.getAnyRoomWithPlayers();
        if (room == null) {
            return "redirect:/register";
        }
        return "redirect:/board/room/" + room;
    }

    @GetMapping("/room/{room}")
    public String boardAllRoomGames(ModelMap model, HttpServletRequest request,
                                @PathVariable("room") String room,
                                @RequestParam(value = "code", required = false) String code,
                                @AuthenticationPrincipal Registration.User user)
    {
        // TODO возможно тут CAN_BE_NULL, иначе проверка (room == null) никогда не true
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        // TODO #4FS тут проверить
        String game = roomService.game(room);
        if (room == null || game == null) {
            return "redirect:/board" + code(code);
        }

        Player player = playerService.getRandomInRoom(room);
        if (player == NullPlayer.INSTANCE) {
            // TODO а это тут вообще надо?
            return "redirect:/register?" + "room" + "=" + room;
        }
        if (isMultiplayerType(player)) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        String id = null;
        if (user != null) {
            id = user.getId();
            code = (code == null) ? user.getCode() : code;
        }

        populateBoardAttributes(model, request, id, code,
                game, room, player.getGameOnly(), null, null, true);

        return "board";
    }

    @GetMapping(value = "/", params = "code")
    public String boardAll(ModelMap model, HttpServletRequest request,
                           @RequestParam("code") String code)
    {
        validator.checkCode(code, CAN_BE_NULL);

        String id = registration.getIdByCode(code);
        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            player = playerService.getRandomInRoom(null);
        }
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register";
        }
        if (isMultiplayerType(player)) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        populateBoardAttributes(model, request, id, code, player, true);
        return "board";
    }

    private boolean isMultiplayerType(Player player) {
        GameType gameType = player.getGameType();
        // TODO а тут точно сеттинги румы а не игры?
        return gameType.getMultiplayerType(gameType.getSettings()).is(MULTIPLE);
    }

    private String code(@RequestParam("code") String code) {
        return (code == null) ? "" : ("?code=" + code);
    }

}
