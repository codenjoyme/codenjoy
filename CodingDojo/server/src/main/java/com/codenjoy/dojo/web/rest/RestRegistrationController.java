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
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerDetailInfo;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping(value = "/rest")
@RequiredArgsConstructor
public class RestRegistrationController {

    private final Registration registration;
    private final PlayerService playerService;
    private final PlayerGames playerGames;
    private final PlayerGamesView playerGamesView;
    private final SaveService saveService;
    private final Validator validator;

//    @RequestMapping(value = "/player/{player}/check/{code}", method = RequestMethod.GET)
//    @ResponseBody
    public boolean checkUserLogin(@PathVariable("player") String emailOrId,
                                  @PathVariable("code") String code)
    {
        validator.checkPlayerName(emailOrId, Validator.CANT_BE_NULL);
        validator.checkCode(code, Validator.CANT_BE_NULL);

        return registration.checkUser(emailOrId, code) != null;
    }

    // TODO test me
//    @RequestMapping(value = "/player/{player}/remove/{code}", method = RequestMethod.GET)
//    @ResponseBody
    public synchronized boolean removeUser(@PathVariable("player") String emailOrId,
                              @PathVariable("code") String code)
    {
        String id = validator.checkPlayerCode(emailOrId, code);

        // оставляем только актуальные на сейчас очки, мало ли захочет залогиниться назад
        // TODO как-то тут не очень оставлять последние очки, иначе пользователь потеряет их, что тоже не ок
        saveService.removeSave(id);
        saveService.save(id);

        // и удаляем игрока с игрового сервера
        playerService.remove(id);
        registration.remove(id);

        return true;
    }

    // TODO test me
//    @RequestMapping(value = "/game/{gameName}/players", method = RequestMethod.GET)
//    @ResponseBody
    public List<PlayerInfo> getGamePlayers(@PathVariable("gameName") String gameName) {
        validator.checkGameName(gameName, Validator.CANT_BE_NULL);

        return playerService.getAll(gameName).stream()
                .map(PlayerInfo::new)
                .collect(toList());
    }

    // TODO test me
//    @RequestMapping(value = "/player/all/info/{adminPassword}", method = RequestMethod.GET)
//    @ResponseBody
    public List<PlayerDetailInfo> getPlayersForMigrate(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

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
//    @RequestMapping(value = "/player/create/{adminPassword}", method = RequestMethod.POST)
//    @ResponseBody
    public synchronized String createPlayer(@RequestBody PlayerDetailInfo player,
                               @PathVariable("adminPassword") String adminPassword)
    {
        validator.checkIsAdmin(adminPassword);

        Registration.User user = player.getRegistration();
        registration.replace(user);

        boolean fromSave = player.getScore() == null;
        if (fromSave) {
            // делаем попытку грузить по сейву
            if (!saveService.load(player.getName())) {
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

            playerGames.setLevel(player.getName(),
                    new JSONObject(player.getSave()));
        }

        return user.getCode();
    }

    // TODO test me
//    @RequestMapping(value = "/player/{player}/exists", method = RequestMethod.GET)
//    @ResponseBody
    public boolean isPlayerExists(@PathVariable("player") String emailOrId) {
        validator.checkPlayerName(emailOrId, Validator.CANT_BE_NULL);

        String id = registration.checkUser(emailOrId);
        return (id != null) && playerService.contains(id);
    }
}
