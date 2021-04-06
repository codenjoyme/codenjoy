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
import com.codenjoy.dojo.services.room.RoomService;
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

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;

@RestController
@Secured(GameAuthoritiesConstants.ROLE_ADMIN)
@RequestMapping(RestAdminController.URI)
@AllArgsConstructor
public class RestAdminController {

    public static final String URI = "/rest/admin";
    public static final String ROOM = "/room/{room}";

    private GameService gameService;
    private Validator validator;
    private PlayerService playerService;
    private ErrorTicketService ticket;
    private SaveService saveService;
    private PlayerGamesView playerGamesView;
    private TimerService timerService;
    private RoomService roomService;
    private Registration registration;
    private PlayerGames playerGames;
    private GameService games;

    @GetMapping("version")
    public String version() {
        List<String> list = games.getGames();
        list.add(0, "engine");
        list.add(1, "server");
        return VersionReader.version(list).toString();
    }

    @GetMapping("/info")
    public Map<String, String> getInfoLogs() {
        return ticket.getInfo();
    }

    @GetMapping("/errors")
    public Map<String, Map<String, Object>> getTickets(
            @RequestParam(value = "ticket", required = false) String ticketId)
    {
        final Map<String, Map<String, Object>> tickets = ticket.getErrors();

        if (StringUtils.isEmpty(ticketId)) {
            return tickets;
        } else {
            return new HashMap<>(){{
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

            String game = player.getGame();
            if (!result.containsKey(game)) {
                result.put(game, new LinkedList<>());
            }
            result.get(game).add(group);
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
            String room = playerGame.getRoom();
            List<String> group = groups.get(player.getId());
            result.add(new PlayerDetailInfo(player, user, room, game, group));
        }

        return result;
    }

    @GetMapping(ROOM + "/pause/{enabled}")
    public void setRoomActive(@PathVariable("room") String room,
                           @PathVariable("enabled") boolean enabled)
    {
        validator.checkRoom(room, CANT_BE_NULL);

        roomService.setActive(room, enabled);
    }

    @GetMapping(ROOM + "/pause")
    public boolean isRoomActive(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        return roomService.isActive(room);
    }

    @GetMapping(ROOM + "/registration/open/{enabled}")
    public void setRoomRegistrationOpened(@PathVariable("room") String room,
                        @PathVariable("enabled") boolean enabled) {
        validator.checkRoom(room, CANT_BE_NULL);

        roomService.setOpened(room, enabled);
    }

    @GetMapping(ROOM + "/registration/open")
    public boolean isRoomRegistrationOpened(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        return roomService.isOpened(room);
    }

    @GetMapping(ROOM + "/scores/clear")
    public void cleanScores(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        playerService.cleanAllScores(room);
    }

    @GetMapping(ROOM + "/scores/{player}/set/{score}")
    public void setScores(@PathVariable("room") String room,
                          @PathVariable("player") String id,
                          @PathVariable("score") String score)
    {
        validator.checkNotEmpty("score", score);
        validator.checkPlayerInRoom(id, room);

        Player player = playerService.get(id);
        player.setRoom(null);
        player.setData(null);
        player.setScore(score);
        playerService.update(player);
    }

    @GetMapping(ROOM + "/player/reload")
    public void reloadPlayers(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        playerService.reloadAllRooms(room);
    }

    @GetMapping(ROOM + "/board/reload") // TODO test me
    public void reloadBoards(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        saveService.saveAll(room);
        playerService.removeAll(room);
        saveService.loadAll(room);
    }

    /**
     * @param room имя комнаты, для которой мы хотим получить настройки
     * @param game имя игры указывается тут, потому что этот метод будет
     *                 дергаться еще до первого зарегистрированного пользователя
     *                 в комнату, а потому откуда codenjoy знает про связку комната + игра?
     * @return кастомные настройки для этой комнаты
     */
    @GetMapping(ROOM + "/settings/{game}")
    public PParameters getSettings(@PathVariable("room") String room,
                                   @PathVariable("game") String game)
    {
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkGameType(game);

        GameType type = gameService.getGameType(game, room);
        Settings settings = type.getSettings();
        List<Parameter> result = settings.getParameters();
        return new PParameters(result);
    }

    @PostMapping(ROOM + "/settings/{game}")
    public void setSettings(@PathVariable("room") String room,
                            @PathVariable("game") String game,
                            @RequestBody PParameters input)
    {
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkNotNull("parameters", input);
        validator.checkNotNull("parameters", input.getParameters());

        validator.checkGameType(game);

        GameType type = gameService.getGameType(game, room);
        Settings settings = type.getSettings();
        List<Parameter> parameters = input.build();
        settings.updateAll(parameters);
    }

    @GetMapping(ROOM + "/gameOver/{player}")
    public void gameOver(@PathVariable("room") String room,
                         @PathVariable("player") String id)
    {
        validator.checkPlayerInRoom(id, room);

        playerService.remove(id);
    }

    @GetMapping(ROOM + "/gameOverAll")
    public void gameOverAll(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        playerService.removeAll(room);
    }

    @GetMapping(ROOM + "/load/{player}")
    public void load(@PathVariable("room") String room,
                     @PathVariable("player") String id)
    {
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkPlayerId(id, CANT_BE_NULL);

        saveService.load(id);
    }

    @GetMapping(ROOM + "/saveAll")
    public void saveAll(@PathVariable("room") String room) {
        validator.checkRoom(room, CANT_BE_NULL);

        saveService.saveAll(room);
    }

    // TODO test me
    @GetMapping("/player/{player}/remove")
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
