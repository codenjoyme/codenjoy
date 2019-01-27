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
import com.codenjoy.dojo.web.rest.pojo.PlayerDetailInfo;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping(value = "/rest")
public class RestRegistrationController {

    @Autowired private Registration registration;
    @Autowired private PlayerService playerService;
    @Autowired private PlayerGames playerGames;
    @Autowired private PlayerGamesView playerGamesView;
    @Autowired private SaveService saveService;

    @RequestMapping(value = "/player/{playerName}/check/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUserLogin(@PathVariable("playerName") String playerName,
                                  @PathVariable("code") String code)
    {
        return registration.checkUser(playerName, code);
    }

    // TODO test me
    @RequestMapping(value = "/player/{playerName}/remove/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean removeUser(@PathVariable("playerName") String playerName,
                              @PathVariable("code") String code)
    {
        if (!registration.checkUser(playerName, code)) {
            return false;
        }

        playerService.remove(playerName);
        saveService.removeSave(playerName);
        registration.remove(playerName);

        return true;
    }

    // TODO test me
    @RequestMapping(value = "/game/{gameName}/players", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInfo> getGamePlayers(@PathVariable("gameName") String gameName) {
        return playerService.getAll(gameName).stream()
                .map(PlayerInfo::new)
                .collect(toList());
    }

    // TODO test me
    @RequestMapping(value = "/player/all/info", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerDetailInfo> getPlayersForMigrate() {
        List<Player> players = playerService.getAll();
        List<Registration.User> users = registration.getUsers();
        Map<String, List<String>> groups = playerGamesView.getGroupsMap();

        List<PlayerDetailInfo> result = new LinkedList<>();
        for (Player player : players) {
            Registration.User user = users.stream()
                    .filter(it -> it.getEmail().equals(player.getName()))
                    .findFirst()
                    .orElse(null);
            Game game = playerGames.get(player.getName()).getGame();

            List<String> group = groups.get(player.getName());
            result.add(new PlayerDetailInfo(player, user, game, group));
        }

        return result;
    }

    // TODO test me
    @RequestMapping(value = "/player/create", method = RequestMethod.POST)
    @ResponseBody
    public String createPlayer(@RequestBody PlayerDetailInfo playerInfo) {

        Registration.User user = playerInfo.getRegistration();

        String code = Registration.makeCode(user.getEmail(), user.getPassword());
        user.setCode(code);

        registration.replace(user);

        PlayerSave playerSave = playerInfo.buildPlayerSave();
        playerService.register(playerSave);

        playerGames.setLevel(playerInfo.getName(),
                new JSONObject(playerInfo.getSave()));

        return code;
    }

    // TODO test me
    @RequestMapping(value = "/player/{playerName}/exists", method = RequestMethod.GET)
    @ResponseBody
    public boolean isPlayerExists(@PathVariable("playerName") String playerName) {
        boolean registered = registration.registered(playerName);
        boolean active = playerService.contains(playerName);

        return registered && active;
    }
}
