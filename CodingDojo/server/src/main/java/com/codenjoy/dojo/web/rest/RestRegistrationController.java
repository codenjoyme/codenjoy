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
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping(value = "/rest")
public class RestRegistrationController {

    @Autowired private Registration registration;
    @Autowired private PlayerService playerService;
    @Autowired private PlayerGames playerGamesService;
    @Autowired private PlayerGamesView playerGamesViewService;

    @RequestMapping(value = "/player/{playerName}/check/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean checkUserLogin(@PathVariable("playerName") String playerName, @PathVariable("code") String code) {
        return registration.checkUser(playerName, code);
    }

    static class PlayerInfo {
        private final String gameType;
        private final String callbackUrl;
        private final String name;
        private final String score;
        private final String code;

        PlayerInfo(Player player) {
            gameType = player.getGameType().name();
            callbackUrl = player.getCallbackUrl();
            name = player.getName();
            score = String.valueOf(player.getScore());
            code = player.getCode();
        }

        public String getGameType() {
            return gameType;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public String getName() {
            return name;
        }

        public String getScore() {
            return score;
        }

        public String getCode() {
            return code;
        }
    }

    @RequestMapping(value = "/game/{gameName}/players", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerInfo> getPlayerForGame(@PathVariable("gameName") String gameName) {
        return playerService.getAll(gameName).stream()
                .map(PlayerInfo::new)
                .collect(toList());
    }

    static class MType {
        private int levelsCount;
        private int roomSize;
        private String type;

        public MType(com.codenjoy.dojo.services.multiplayer.MultiplayerType multiplayer) {
            this.type = multiplayer.getType();
            roomSize = multiplayer.getRoomSize();
            levelsCount = multiplayer.getLevelsCount();
        }

        public int getLevelsCount() {
            return levelsCount;
        }

        public int getRoomSize() {
            return roomSize;
        }

        public String getType() {
            return type;
        }
    }

    static class PlayerDetailInfo {
        private MType multiplayer;
        private HeroData hero;
        private List<String> group;
        private String save;
        private LevelProgress progress;
        private String name;
        private String gameType;
        private String callbackUrl;
        private String score;
        private Registration.User registration;

        PlayerDetailInfo(Player player, Registration.User registration,
                         Game game, List<String> group)
        {
            gameType = player.getGameType().name();
            multiplayer = new MType(player.getGameType().getMultiplayerType());

            callbackUrl = player.getCallbackUrl();
            score = String.valueOf(player.getScore());
            name = player.getName();

            this.registration = registration;

            progress = game.getProgress();
            save = game.getSave().toString();
            hero = game.getHero();
            this.group = group;
        }

        public String getGameType() {
            return gameType;
        }

        public String getCallbackUrl() {
            return callbackUrl;
        }

        public String getScore() {
            return score;
        }

        public Registration.User getRegistration() {
            return registration;
        }

        public String getName() {
            return name;
        }

        public LevelProgress getProgress() {
            return progress;
        }

        public String getSave() {
            return save;
        }

        public HeroData getHero() {
            return hero;
        }

        public List<String> getGroup() {
            return group;
        }

        public MType getMultiplayer() {
            return multiplayer;
        }
    }

    // TODO test me
    @RequestMapping(value = "/player/all/info", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerDetailInfo> getPlayersForMigrate() {
        List<Player> players = playerService.getAll();
        List<Registration.User> users = registration.getUsers();
        Map<String, List<String>> groups = playerGamesViewService.getGroupsMap();

        List<PlayerDetailInfo> result = new LinkedList<>();
        for (Player player : players) {
            Registration.User user = users.stream()
                    .filter(it -> it.getEmail().equals(player.getName()))
                    .findFirst()
                    .orElse(null);
            Game game = playerGamesService.get(player.getName()).getGame();

            List<String> group = groups.get(player.getName());
            result.add(new PlayerDetailInfo(player, user, game, group));
        }

        return result;
    }
}
