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
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerDetailInfo;
import lombok.AllArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

@RestController
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@RequestMapping(RestAdminController.URI)
@AllArgsConstructor
public class RestAdminController {

    public static final String URI = "/rest/admin";
    public static final String ROOM = "/room/{roomName}";

    private Validator validator;
    private PlayerService playerService;
    private PlayerGamesView playerGamesView;
    private TimerService timerService;
    private RoomService roomService;
    private Registration registration;
    private PlayerGames playerGames;

    // TODO test me и вообще где это надо?
    @GetMapping("/player/all/groups")
    public Map<String, List<List<String>>> getPlayersGroups() {
        Map<String, List<List<String>>> result = new HashMap<>();
        List<Player> players = playerService.getAll();
        List<List<String>> groups = playerGamesView.getGroupsByField();
        for (List<String> group : groups) {
            String playerId = group.get(0);
            Player player = players.stream()
                    .filter(p -> p.getId().equals(playerId))
                    .findFirst()
                    .orElse(NullPlayer.INSTANCE);

            String gameName = player.getGameName();
            if (!result.containsKey(gameName)) {
                result.put(gameName, new LinkedList<>());
            }
            result.get(gameName).add(group);
        }
        return result;
    }

    // TODO test me
    @GetMapping("/player/all/scores")
    public Map<String, Object> getPlayersScores() {
        return playerGamesView.getScores();
    }

    // TODO test me
    @GetMapping("/scores/clear")
    public boolean clearAllScores() {
        playerService.cleanAllScores();
        return true;
    }

    // TODO test me
    // TODO он используется в balancer объединить с новым
    @GetMapping("/game/enabled/{enabled}")
    public boolean startStopGame(@PathVariable("enabled") boolean enabled) {
        if (enabled) {
            timerService.resume();
        } else {
            timerService.pause();
        }

        return timerService.isPaused(); // TODO вот это странно, на вход идет enabled, а на выход paused = !enabled
    }

    // TODO test me
    @GetMapping("/player/all/info")
    public List<PlayerDetailInfo> getPlayersForMigrate() {
        List<Player> players = playerService.getAll();
        List<Registration.User> users = registration.getUsers();
        Map<String, List<String>> groups = playerGamesView.getGroupsMap();

        List<PlayerDetailInfo> result = new LinkedList<>();
        for (Player player : players) {
            Registration.User user = users.stream()
                    .filter(it -> it.getId().equals(player.getId()))
                    .findFirst()
                    .orElse(null);

            PlayerGame playerGame = playerGames.get(player.getId());
            Game game = playerGame.getGame();
            String roomName = playerGame.getRoomName();
            List<String> group = groups.get(player.getId());
            result.add(new PlayerDetailInfo(player, user, roomName, game, group));
        }

        return result;
    }

    @GetMapping(ROOM + "/pause/{enabled}")
    public void setEnabled(@PathVariable("roomName") String roomName,
                           @PathVariable("enabled") boolean enabled)
    {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        roomService.setActive(roomName, enabled);
    }

    @GetMapping(ROOM + "/pause")
    public boolean getEnabled(@PathVariable("roomName") String roomName) {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        return roomService.isActive(roomName);
    }

    @GetMapping(ROOM + "/scores/clear")
    public void cleanScores(@PathVariable("roomName") String roomName) {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        playerService.cleanAllScores(roomName);
    }

    @GetMapping(ROOM + "/scores/{player}/set/{score}")
    public void setScores(@PathVariable("roomName") String roomName,
                          @PathVariable("player") String id,
                          @PathVariable("score") String score)
    {
        validator.checkNotEmpty("score", score);
        validator.checkPlayerInRoom(id, roomName);

        Player player = playerService.get(id);
        player.setRoomName(null);
        player.setData(null);
        player.setScore(score);
        playerService.update(player);
    }

    @GetMapping(ROOM + "/reload")
    public void reload(@PathVariable("roomName") String roomName) {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        playerService.reloadAllRooms(roomName);
    }
}
