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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerDetailInfo;
import com.codenjoy.dojo.web.rest.pojo.PlayerId;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static java.util.stream.Collectors.toList;

/**
 * Используется внешним сервисом для входа и выхода из комнаты залогиненного пользователя.
 * Валидации нет, т.к. ее клиент не будет обрабатывать.
 */
@Controller
@RequestMapping("/rest")
@AllArgsConstructor
public class RestRoomController {

    private PlayerService playerService;
    private GameService gameService;
    private Validator validator;

    // TODO test me
    @GetMapping("/room/{roomName}/leave")
    @ResponseBody
    public synchronized boolean leavePlayerFromRoom(@PathVariable("roomName") String roomName,
                                                    HttpServletRequest request,
                                                    @AuthenticationPrincipal Registration.User user)
    {
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

    // TODO test me
    @GetMapping("/room/{roomName}/joined")
    @ResponseBody
    public boolean isPlayerInRoom(@PathVariable("roomName") String roomName,
                                  HttpServletRequest request,
                                  @AuthenticationPrincipal Registration.User user)
    {
        if (user == null) {
            return false;
        }

        String id = user.getId();
        return validator.isPlayerInRoom(id, roomName);
    }

    // TODO test me
    @GetMapping("/room/{roomName}/game/{gameName}/join")
    @ResponseBody
    public synchronized PlayerId joinPlayerInRoom(@PathVariable("gameName") String gameName,
                                                  @PathVariable("roomName") String roomName,
                                                  HttpServletRequest request,
                                                  @AuthenticationPrincipal Registration.User user)
    {
        // TODO where is validation
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
