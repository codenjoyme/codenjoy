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


import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import com.codenjoy.dojo.web.rest.pojo.*;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@RestController
@RequestMapping(RestBoardController.URI)
@AllArgsConstructor
public class RestBoardController {

    public static final String URI = "/rest";
    public static final String HEALTH = "/health";

    private GameService gameService;
    private RestRegistrationController registrationController;
    private RestGameController gameController;
    private PlayerService playerService;
    private Registration registration;
    private Validator validator;
    private Deals deals;
    private DealsView dealsView;
    private SaveService saveService;
    private ActionLogger actionLogger;

    @GetMapping(HEALTH)
    public String checkHealth() {
        return "ok";
    }

    @GetMapping("/context")
    public String getContext() {
        return "/" + CodenjoyContext.getContext();
    }

    @GetMapping("/player/{player}/{code}/level/{level}")
    public synchronized boolean changeLevel(@PathVariable("player") String id,
                                @PathVariable("code") String code,
                                @PathVariable("level") int level)
    {
        validator.checkPlayerCode(id, code);

        deals.changeLevel(id, level);

        return true;
    }

    // TODO ROOM тут наверное room надо, хотя вот ниже есть метод...
    @GetMapping("/game/{game}/scores")
    public List<PScoresOf> getPlayersScoresForGame(@PathVariable("game") String game) {
        return dealsView.getScoresForGame(game);
    }

    // TODO test me
    @GetMapping("/room/{room}/scores")
    public List<PScoresOf> getPlayersScoresForRoom(@PathVariable("room") String room) {
        return dealsView.getScoresForRoom(room);
    }

    // TODO test me
    @GetMapping("/player/{player}/{code}/reset")
    public synchronized boolean reset(@PathVariable("player") String id,
                                      @PathVariable("code") String code)
    {
        validator.checkPlayerCode(id, code);

        if (!playerService.contains(id)) {
            return false;
        }

        saveService.save(id);
        Player player = playerService.get(id);

        boolean loaded = saveService.load(id);
        if (!loaded) {
            if (playerService.contains(id)) {
                playerService.remove(id);
            }
            playerService.register(new PlayerSave(player));
        }

        return true;
    }

    @GetMapping("/player/{player}/{code}/wantsToPlay/{game}/{room}")
    public synchronized PPlayerWantsToPlay playerWantsToPlay(
            @PathVariable("player") String id,
            @PathVariable("code") String code,
            @PathVariable("game") String game,
            @PathVariable("room") String room)
    {
        // TODO test validation
        validator.checkPlayerId(id, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);

        String context = getContext();
        PGameTypeInfo gameType = gameController.type(game, room);
        boolean registered = registration.checkUser(id, code) != null;
        List<String> sprites = gameController.spritesNames(game);
        String alphabet = gameController.spritesAlphabet();
        String spritesAlphabet = gameController.spritesValuesAlphabet(game);
        List<PlayerInfo> players = registrationController.getRoomPlayers(room);

        return new PPlayerWantsToPlay(context, gameType,
                registered, sprites, alphabet, spritesAlphabet, players);
    }

    // TODO test me
    @GetMapping("/player/{player}/log/{time}")
    public List<BoardLog> changeLevel(@PathVariable("player") String id,
                                            @PathVariable("time") Long time)
    {
        validator.checkPlayerId(id, CANT_BE_NULL);

        if (registration.checkUser(id) == null) {
            return Arrays.asList();
        }

        if (time == null || time == 0) {
            time = actionLogger.getLastTime(id);
        }

        List<BoardLog> result = actionLogger.getBoardLogsFor(id, time, 100);

        if (result.isEmpty()) {
            return Arrays.asList();
        }

        // TODO Как-то тут сложно и долго грузится
        // TODO а еще если участник играл в одну игру, а потом переключился в бомбер и там продолжал, то тут будет ошибка так как одной игрой парсим другую
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameService.getGameType(result.get(0).getGame()).getPlots());

        // TODO доделать для icancode
        result.forEach(log -> {
            Object board = tryJson(log.getBoard());
            log.setBoard(decoder.encodeForBrowser(board));
        });

        return result;
    }

    private Object tryJson(String board) {
        try {
            return new JSONObject(board);
        } catch (JSONException e) {
            return board;
        }
    }

    @GetMapping("/{game}/status")
    public Map<String, Object> checkGameIsActive(@PathVariable("game") String game) {
        boolean active = deals.getPlayersByGame(game).size() > 0;
        return Collections.singletonMap("active", active);
    }

    @GetMapping("/room/{room}/whats-next")
    public String whatsNext(@PathVariable("room") String room,
                            @NotNull @RequestBody PWhatsNext input)
    {
        validator.checkRoom(room, CANT_BE_NULL);
        validator.checkNotNull("board", input.getBoard());
        validator.checkNotNull("actions", input.getActions());

        return playerService.whatsNext(room, input.getBoard(), input.getActions());
    }
}
