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


import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

import com.codenjoy.dojo.services.BoardLog;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.GuiPlotColorDecoder;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.PlayerGamesView;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.SaveService;
import com.codenjoy.dojo.services.TimerService;
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.GameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PPlayerWantsToPlay;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/rest")
@RequiredArgsConstructor
public class RestBoardController {

    private final GameService gameService;
    private final RestRegistrationController registrationController;
    private final PlayerService playerService;
    private final Registration registration;
    private final ServletContext servletContext;
    private final Validator validator;
    private final PlayerGames playerGames;
    private final PlayerGamesView playerGamesView;
    private final TimerService timerService;
    private final SaveService saveService;
    private final ActionLogger actionLogger;

//    @GetMapping("/sprites")
    public Map<String, List<String>> getAllSprites() {
        return gameService.getSprites();
    }

//    @GetMapping("/sprites/{gameName}/exists")
    public boolean isGraphicOrTextGame(@PathVariable("gameName") String gameName) {
        return !getSpritesForGame(gameName).isEmpty();
    }

//    @GetMapping("/sprites/{gameName}")
    public List<String> getSpritesForGame(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new ArrayList<>();
        }
        return gameService.getSprites().get(gameName);
    }

//    @GetMapping("/sprites/alphabet")
    public String getSpritesAlphabet() {
        return String.valueOf(GuiPlotColorDecoder.GUI.toCharArray());
    }

//    @GetMapping("/context")
    public String getContext() {
        String contextPath = servletContext.getContextPath();
        if (contextPath.charAt(contextPath.length() - 1) == '/') {
            contextPath += contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

//    @GetMapping("/game/{gameName}/type")
    public GameTypeInfo getGameType(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new GameTypeInfo(NullGameType.INSTANCE);
        }
        GameType game = gameService.getGame(gameName);

        return new GameTypeInfo(game);
    }

    @GetMapping("/player/{player}/{code}/level/{level}")
    public synchronized boolean changeLevel(@PathVariable("player") String emailOrId,
                                @PathVariable("code") String code,
                                @PathVariable("level") int level)
    {
        String id = validator.checkPlayerCode(emailOrId, code);

        playerGames.changeLevel(id, level);

        return true;
    }

    // TODO test me и вообще где это надо?
//    @GetMapping("/player/all/groups")
    public Map<String, List<List<String>>> getPlayersGroups() {
        Map<String, List<List<String>>> result = new HashMap<>();
        List<Player> players = playerService.getAll();
        List<List<String>> groups = playerGamesView.getGroups();
        for (List<String> group : groups) {
            String playerId = group.get(0);
            Player player = players.stream()
                    .filter(p -> p.getName().equals(playerId))
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

//    @GetMapping("/player/all/scores")
    public Map<String, Object> getPlayersScores() {
        return playerGamesView.getScores();
    }

    @GetMapping("/game/{gameName}/scores")
    public List<PScoresOf> getPlayersScoresForGame(@PathVariable("gameName") String gameName) {
        return playerGamesView.getScoresFor(gameName);
    }

    @GetMapping("/scores/clear")
    public boolean clearAllScores() {
        playerService.cleanAllScores();
        return true;
    }

    @GetMapping("/game/enabled/{enabled}")
    public boolean startStopGame(@PathVariable("enabled") boolean enabled)
    {

        if (enabled) {
            timerService.resume();
        } else {
            timerService.pause();
        }

        return timerService.isPaused();
    }

    // TODO test me
//    @GetMapping("/player/{player}/{code}/reset")
    public synchronized boolean reset(@PathVariable("player") String emailOrId, @PathVariable("code") String code){
        String id = validator.checkPlayerCode(emailOrId, code);

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
            @PathVariable("player") String emailOrId,
            @PathVariable("code") String code,
            @PathVariable("gameName") String gameName)
    {
        validator.checkPlayerName(emailOrId, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        String context = getContext();
        GameTypeInfo gameType = getGameType(gameName);
        boolean registered = registration.checkUser(emailOrId, code) != null;
        List<String> sprites = getSpritesForGame(gameName);
        String alphabet = getSpritesAlphabet();
        List<PlayerInfo> players = registrationController.getGamePlayers(gameName);

        return new PPlayerWantsToPlay(context, gameType,
                registered, sprites, alphabet, players);
    }

    // TODO test me
    @GetMapping("/player/{player}/log/{time}")
    public List<BoardLog> changeLevel(@PathVariable("player") String emailOrId,
                                            @PathVariable("time") Long time)
    {
        validator.checkPlayerName(emailOrId, CANT_BE_NULL);

        String id = registration.checkUser(emailOrId);

        if (time == null || time == 0) {
            time = actionLogger.getLastTime(id);
        }

        List<BoardLog> result = actionLogger.getBoardLogsFor(id, time, 100);

        if (result.isEmpty()) {
            return Arrays.asList();
        }

        // TODO Как-то тут сложно
        GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameService.getGame(result.get(0).getGameType()).getPlots());

        result.forEach(log -> {
            String board = log.getBoard().replaceAll("\n", "");
            log.setBoard((String) decoder.encodeForBrowser(board));
        });

        return result;
    }

    @GetMapping("/{gameName}/status")
    public Map<String, Object> checkGameIsActive(@PathVariable("gameName") String gameName) {
        return Collections.singletonMap("active", playerGames.getPlayers(gameName).size() > 0);
    }
}
