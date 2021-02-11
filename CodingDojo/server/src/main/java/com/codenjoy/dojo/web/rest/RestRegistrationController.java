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

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/rest")
@AllArgsConstructor
public class RestRegistrationController {

    private Registration registration;
    private PlayerService playerService;
    private PlayerGames playerGames;
    private GameService gameService;
    private SaveService saveService;
    private Validator validator;

    @GetMapping("/player/{player}/check/{code}")
    @ResponseBody
    public boolean checkUserLogin(@PathVariable("player") String id,
                                  @PathVariable("code") String code)
    {
        validator.checkPlayerId(id, Validator.CANT_BE_NULL);
        validator.checkCode(code, Validator.CANT_BE_NULL);

        return registration.checkUser(id, code) != null;
    }

    // TODO test me
    @GetMapping("/game/{gameName}/players")
    @ResponseBody
    public List<PlayerInfo> getGamePlayers(@PathVariable("gameName") String gameName) {
        validator.checkGameName(gameName, Validator.CANT_BE_NULL);

        return playerService.getAll(gameName).stream()
                .map(PlayerInfo::new)
                .collect(toList());
    }

    // TODO test me
    @GetMapping("/room/{roomName}/game/{gameName}/join")
    @ResponseBody
    public synchronized PlayerId joinPlayerInRoom(@PathVariable("gameName") String gameName,
                                                  @PathVariable("roomName") String roomName,
                                                  HttpServletRequest request,
                                                  @AuthenticationPrincipal Registration.User user) 
    {
        if (user == null) {
            return null;
        }
        
        if (gameService.getGame(gameName) instanceof NullGameType) {
            return null;
        }
        
        playerService.register(user.getId(), request.getRemoteAddr(), roomName, gameName);
        
        return new PlayerId(user);
    }
    
    // TODO test me
    @PostMapping("/player/create")
    @ResponseBody
    public synchronized String createPlayer(@RequestBody PlayerDetailInfo player) {
        Registration.User user = player.getRegistration().build();
        registration.replace(user);

        boolean fromSave = player.getScore() == null;
        if (fromSave) {
            // делаем попытку грузить по сейву
            if (!saveService.load(player.getId())) {
                // неудача - обнуляем все
                player.setSave("{}");
                player.setScore("0");
                fromSave = false;
            }
        }

        if (!fromSave) {
            // грузим как положено
            PlayerSave save = player.buildPlayerSave();
            playerService.register(save);

            playerGames.setLevel(player.getId(),
                    new JSONObject(player.getSave()));
        }

        return user.getCode();
    }

    // TODO test me
    @GetMapping("/player/{player}/exists")
    @ResponseBody
    public boolean isPlayerExists(@PathVariable("player") String id) {
        validator.checkPlayerId(id, Validator.CANT_BE_NULL);

        return registration.checkUser(id) != null
                && playerService.contains(id);
    }
}
