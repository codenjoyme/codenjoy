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
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import com.codenjoy.dojo.web.rest.pojo.PlayerDetailInfo;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.SemifinalSettings.SEMIFINAL;
import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static java.util.stream.Collectors.toList;

@RestController
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@RequestMapping(RestAdminController.URI)
@AllArgsConstructor
public class RestAdminController {

    public static final String URI = "/rest/admin";
    public static final String ROOM = "/room/{roomName}";

    private Validator validator;
    private PlayerService playerService;
    private ErrorTicketService ticket;
    private SaveService saveService;
    private PlayerGamesView playerGamesView;
    private TimerService timerService;
    private RoomService roomService;
    private Registration registration;
    private PlayerGames playerGames;
    private SemifinalSettings semifinalSettings;
    private GameService games;

    @GetMapping("version")
    @ResponseBody
    public String version() {
        List<String> list = games.getGameNames();
        list.add(0, "engine");
        list.add(1, "server");
        return VersionReader.version(list).toString();
    }

    @GetMapping("/info")
    @ResponseBody
    public Map<String, String> getInfoLogs() {
        return ticket.getInfo();
    }

    @GetMapping("/errors")
    @ResponseBody
    public Map<String, Map<String, Object>> getTickets(
            @RequestParam(value = "ticket", required = false) String ticketId)
    {
        final Map<String, Map<String, Object>> tickets = ticket.getErrors();

        if (StringUtils.isEmpty(ticketId)) {
            return tickets;
        } else {
            return new HashMap<String, Map<String, Object>>(){{
                put(ticketId, tickets.get(ticketId));
            }};
        }
    }

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

        return !timerService.isPaused();
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

    /**
     * @param roomName имя комнаты, для которой мы хотим получить настройки
     * @param gameName имя игры указывается тут, потому что этот метод будет
     *                 дергаться еще до первого зарегистрированного пользователя
     *                 в комнату, а потому откуда codenjoy знает про связку комната + игра?
     * @return кастомные настройки для этой комнаты
     */
    @GetMapping(ROOM + "/settings/{gameName}")
    public PParameters getSettings(@PathVariable("roomName") String roomName,
                                   @PathVariable("gameName") String gameName)
    {
        validator.checkRoomName(roomName, CANT_BE_NULL);
        GameType type = validator.checkGameType(gameName);

        // TODO настройки должны распостраняться только на эту комнату
        Settings settings = type.getSettings();
        List<Parameter> result = settings.getParameters();
        result.addAll(semifinalSettings.parameters());

        return new PParameters(result);
    }

    @PostMapping(ROOM + "/settings/{gameName}")
    public void setSettings(@PathVariable("roomName") String roomName,
                            @PathVariable("gameName") String gameName,
                            @RequestBody PParameters input)
    {
        validator.checkRoomName(roomName, CANT_BE_NULL);
        validator.checkNotNull("parameters", input);
        validator.checkNotNull("parameters", input.getParameters());

        GameType type = validator.checkGameType(gameName);

        // TODO настройки должны распостраняться только на эту комнату
        Settings settings = type.getSettings();

        List<Parameter> parameters = input.build();
        semifinalSettings.update(parameters);

        List<Parameter> other = parameters.stream()
                .filter(parameter -> !parameter.getName().startsWith(SEMIFINAL))
                .collect(toList());

        settings.updateAll(other);
    }

    // TODO заменить во всех Rest и других controller's: roomName -> room, gameName -> game
    @GetMapping(ROOM + "/gameOver/{player}")
    public void gameOver(@PathVariable("roomName") String roomName,
                         @PathVariable("player") String id)
    {
        validator.checkPlayerInRoom(id, roomName);

        playerService.remove(id);
    }

    @GetMapping(ROOM + "/gameOverAll")
    public void gameOverAll(@PathVariable("roomName") String roomName) {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        playerService.removeAll(roomName);
    }

    @GetMapping(ROOM + "/load/{player}")
    public void load(@PathVariable("roomName") String roomName,
                     @PathVariable("player") String id)
    {
        validator.checkRoomName(roomName, CANT_BE_NULL);
        validator.checkPlayerId(id, CANT_BE_NULL);

        saveService.load(id);
    }

    @GetMapping(ROOM + "/saveAll")
    public void saveAll(@PathVariable("roomName") String roomName) {
        validator.checkRoomName(roomName, CANT_BE_NULL);

        saveService.saveAll(roomName);
    }

    // TODO test me
    @GetMapping("/player/{player}/remove")
    @ResponseBody
    public synchronized boolean removeUser(@PathVariable("player") String id) {
        validator.checkPlayerId(id);

        // не удаляем сейвы, а то повторное подключение не загрузит очки
        // saveService.removeSave(id);

        // и удаляем игрока с игрового сервера c его регистрацией
        playerService.remove(id);
        registration.remove(id);

        return true;
    }

}
