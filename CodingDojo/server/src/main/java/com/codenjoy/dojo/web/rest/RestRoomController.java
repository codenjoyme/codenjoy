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


import com.codenjoy.dojo.services.GameServerService;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerId;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

/**
 * Используется внешним сервисом для входа, выхода из
 * комнаты и проверки статуса для залогиненного/незалогиненного пользователя.
 */
@RestController
@RequestMapping("/rest")
@AllArgsConstructor
public class RestRoomController {

    private PlayerService playerService;
    private GameService gameService;
    private Validator validator;
    private SaveService saveService;
    private GameServerService gameServerService;

    @GetMapping("/room/{room}/leave")
    public synchronized boolean leavePlayerFromRoom(@PathVariable("room") String room,
                                                    @AuthenticationPrincipal Registration.User user) {
        validator.checkRoom(room, CANT_BE_NULL);

        if (user == null) {
            return false;
        }

        String id = user.getId();

        if (!validator.isPlayerInRoom(id, room)) {
            return false;
        }

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return false;
        }

        playerService.remove(id);

        return true;
    }

    @GetMapping("/room/{room}/joined")
    public boolean isPlayerInRoom(@PathVariable("room") String room,
                                  @AuthenticationPrincipal Registration.User user) {
        validator.checkRoom(room, CANT_BE_NULL);

        if (user == null) {
            return false;
        }

        String id = user.getId();
        return validator.isPlayerInRoom(id, room);
    }

    @GetMapping("/room/{room}/player/{playerId}/joined")
    public boolean isPlayerInRoom(@PathVariable("room") String room,
                                  @PathVariable("playerId") String playerId) {
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkPlayerId(playerId, CANT_BE_NULL);

        if (playerId == null) {
            return false;
        }

        return validator.isPlayerInRoom(playerId, room);
    }

    @PostMapping("/room/{room}/game/{game}/join")
    public synchronized PlayerId joinPlayerInRoom(@PathVariable("game") String game,
                                                  @PathVariable("room") String room,
                                                  HttpServletRequest request,
                                                  //changing @AuthenticationPrincipal to @RequestBody
                                                  // to bypass authentication process
                                                  @RequestBody Registration.User user) {
        validator.checkRoom(room, CANT_BE_NULL);

        if (user == null || user.getId() == null) {
            return null;
        }

        if (!gameService.exists(game)) {
            return null;
        }
        String repositoryUrl = gameServerService.createOrGetRepositoryWithGame(user.getGitHubUsername(), game);

        Player player = playerService.register(user.getId(), game, room, request.getRemoteAddr(), repositoryUrl, user.getSlackEmail());

        saveService.save(player);

        return new PlayerId(user);
    }
}
