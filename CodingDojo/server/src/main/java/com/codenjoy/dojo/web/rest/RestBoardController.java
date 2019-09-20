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
import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.GameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PPlayerWantsToPlay;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.*;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

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

//    @RequestMapping(value = "/sprites", method = RequestMethod.GET)
    public Map<String, List<String>> getAllSprites() {
        return gameService.getSprites();
    }

//    @RequestMapping(value = "/sprites/{gameName}/exists", method = RequestMethod.GET)
    public boolean isGraphicOrTextGame(@PathVariable("gameName") String gameName) {
        return !getSpritesForGame(gameName).isEmpty();
    }

//    @RequestMapping(value = "/sprites/{gameName}", method = RequestMethod.GET)
    public List<String> getSpritesForGame(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new ArrayList<>();
        }
        return gameService.getSprites().get(gameName);
    }

//    @RequestMapping(value = "/sprites/alphabet", method = RequestMethod.GET)
    public String getSpritesAlphabet() {
        return String.valueOf(GuiPlotColorDecoder.GUI.toCharArray());
    }

//    @RequestMapping(value = "/context", method = RequestMethod.GET)
    public String getContext() {
        String contextPath = servletContext.getContextPath();
        if (contextPath.charAt(contextPath.length() - 1) == '/') {
            contextPath += contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

//    @RequestMapping(value = "/game/{gameName}/type", method = RequestMethod.GET)
    public GameTypeInfo getGameType(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new GameTypeInfo(NullGameType.INSTANCE);
        }
        GameType game = gameService.getGame(gameName);

        return new GameTypeInfo(game);
    }

    @RequestMapping(value = "/player/{player}/{code}/level/{level}", method = RequestMethod.GET)
    public synchronized boolean changeLevel(@PathVariable("player") String emailOrId,
                                @PathVariable("code") String code,
                                @PathVariable("level") int level)
    {
        String id = validator.checkPlayerCode(emailOrId, code);

        playerGames.changeLevel(id, level);

        return true;
    }

    // TODO test me и вообще где это надо?
//    @RequestMapping(value = "/player/all/groups", method = RequestMethod.GET)
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

//    @RequestMapping(value = "/player/all/scores", method = RequestMethod.GET)
    public Map<String, Object> getPlayersScores() {
        return playerGamesView.getScores();
    }

    @RequestMapping(value = "/game/{gameName}/scores", method = RequestMethod.GET)
    public List<PScoresOf> getPlayersScoresForGame(@PathVariable("gameName") String gameName) {
        return playerGamesView.getScoresFor(gameName);
    }

//    @RequestMapping(value = "/scores/clear/{adminPassword}", method = RequestMethod.GET)
    public boolean clearAllScores(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        playerService.cleanAllScores();

        return true;
    }

//    @RequestMapping(value = "/game/enabled/{enabled}/{adminPassword}", method = RequestMethod.GET)
    public boolean startStopGame(@PathVariable("adminPassword") String adminPassword,
                                  @PathVariable("enabled") boolean enabled)
    {
        validator.checkIsAdmin(adminPassword);

        if (enabled) {
            timerService.resume();
        } else {
            timerService.pause();
        }

        return timerService.isPaused();
    }

    // TODO test me
//    @RequestMapping(value = "/player/{player}/{code}/reset", method = RequestMethod.GET)
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
    @RequestMapping(value = "/player/{player}/{code}/wantsToPlay/{gameName}", method = RequestMethod.GET)
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

    // TOOD test me
    @RequestMapping(value = "/player/{player}/log/{time}", method = RequestMethod.GET)
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
