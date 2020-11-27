package com.codenjoy.dojo.web.rest;

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


import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerId;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Используется внешним сервисом для входа, выхода из
 * комнаты и проверки статуса для залогиненного/незалогиненного пользователя.
 */
@Controller
@RequestMapping("/rest")
@AllArgsConstructor
public class RestRoomController {

    private PlayerService playerService;
    private GameService gameService;
    private Validator validator;

    @GetMapping("/room/{roomName}/leave")
    @ResponseBody
    public synchronized boolean leavePlayerFromRoom(@PathVariable("roomName") String roomName,
                                                    @AuthenticationPrincipal Registration.User user)
    {
        validator.checkRoomName(roomName, Validator.CANT_BE_NULL);

        if (user == null) {
            return false;
        }

        String id = user.getId();

        if (!validator.isPlayerInRoom(id, roomName)) {
            return false;
        }

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return false;
        }

        playerService.remove(id);

        return true;
    }

    @GetMapping("/room/{roomName}/joined")
    @ResponseBody
    public boolean isPlayerInRoom(@PathVariable("roomName") String roomName,
                                  @AuthenticationPrincipal Registration.User user)
    {
        validator.checkRoomName(roomName, Validator.CANT_BE_NULL);

        if (user == null) {
            return false;
        }

        String id = user.getId();
        return validator.isPlayerInRoom(id, roomName);
    }

    @GetMapping("/room/{roomName}/player/{playerId}/joined")
    @ResponseBody
    public boolean isPlayerInRoom(@PathVariable("roomName") String roomName,
                                  @PathVariable("playerId") String playerId)
    {
        validator.checkRoomName(roomName, Validator.CANT_BE_NULL);
        validator.checkPlayerId(playerId, Validator.CANT_BE_NULL);

        if (playerId == null) {
            return false;
        }

        return validator.isPlayerInRoom(playerId, roomName);
    }

    @GetMapping("/room/{roomName}/game/{gameName}/join")
    @ResponseBody
    public synchronized PlayerId joinPlayerInRoom(@PathVariable("gameName") String gameName,
                                                  @PathVariable("roomName") String roomName,
                                                  HttpServletRequest request,
                                                  @AuthenticationPrincipal Registration.User user)
    {
        validator.checkRoomName(roomName, Validator.CANT_BE_NULL);

        if (user == null) {
            return null;
        }

        if (gameService.getGame(gameName) instanceof NullGameType) {
            return null;
        }

        playerService.register(user.getId(), request.getRemoteAddr(), roomName, gameName);

        return new PlayerId(user);
    }
}
