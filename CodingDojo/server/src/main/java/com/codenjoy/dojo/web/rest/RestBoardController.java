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
import com.codenjoy.dojo.web.rest.pojo.PGameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PPlayerWantsToPlay;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@RestController
@RequestMapping("/rest")
@AllArgsConstructor
public class RestBoardController {

    private GameService gameService;
    private RestRegistrationController registrationController;
    private RestGameController gameController;
    private PlayerService playerService;
    private Registration registration;
    private Validator validator;
    private PlayerGames playerGames;
    private PlayerGamesView playerGamesView;
    private SaveService saveService;
    private ActionLogger actionLogger;

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

        playerGames.changeLevel(id, level);

        return true;
    }

    @GetMapping("/game/{gameName}/scores")
    public List<PScoresOf> getPlayersScoresForGame(@PathVariable("gameName") String gameName) {
        return playerGamesView.getScoresForGame(gameName);
    }

    // TODO test me
    @GetMapping("/room/{roomName}/scores")
    public List<PScoresOf> getPlayersScoresForRoom(@PathVariable("roomName") String roomName) {
        return playerGamesView.getScoresForRoom(roomName);
    }

    // TODO test me
    @GetMapping("/player/{player}/{code}/reset")
    public synchronized boolean reset(@PathVariable("player") String id, @PathVariable("code") String code){
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

    // TODO test me
    @GetMapping("/player/{player}/{code}/wantsToPlay/{gameName}")
    public synchronized PPlayerWantsToPlay playerWantsToPlay(
            @PathVariable("player") String id,
            @PathVariable("code") String code,
            @PathVariable("gameName") String gameName)
    {
        validator.checkPlayerId(id, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        String context = getContext();
        PGameTypeInfo gameType = gameController.type(gameName);
        boolean registered = registration.checkUser(id, code) != null;
        List<String> sprites = gameController.spritesNames(gameName);
        String alphabet = gameController.spritesAlphabet();
        List<PlayerInfo> players = registrationController.getGamePlayers(gameName);

        return new PPlayerWantsToPlay(context, gameType,
                registered, sprites, alphabet, players);
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
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameService.getGame(result.get(0).getGameType()).getPlots());

        // TODO доделать для icancode
        result.forEach(log -> {
            Object board = tryJson(log.getBoard());
            log.setBoard(decoder.encodeForBrowser(board).toString());
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

    @GetMapping("/{gameName}/status")
    public Map<String, Object> checkGameIsActive(@PathVariable("gameName") String gameName) {
        boolean active = playerGames.getPlayers(gameName).size() > 0;
        return Collections.singletonMap("active", active);
    }
}
