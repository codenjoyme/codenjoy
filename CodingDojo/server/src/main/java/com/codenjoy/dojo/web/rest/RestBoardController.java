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
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.GameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PPlayerWantsToPlay;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletContext;
import java.util.*;

@Controller
@RequestMapping(value = "/rest")
public class RestBoardController {

    @Autowired private GameService gameService;
    @Autowired private RestRegistrationController registrationController;
    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private ServletContext servletContext;
    @Autowired private Validator validator;
    @Autowired private PlayerGames playerGames;
    @Autowired private PlayerGamesView playerGamesView;
    @Autowired private ConfigProperties properties;
    @Autowired private TimerService timerService;

    @RequestMapping(value = "/sprites", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<String>> getAllSprites() {
        return gameService.getSprites();
    }

    @RequestMapping(value = "/sprites/{gameName}/exists", method = RequestMethod.GET)
    @ResponseBody
    public boolean isGraphicOrTextGame(@PathVariable("gameName") String gameName) {
        return !getSpritesForGame(gameName).isEmpty();
    }

    @RequestMapping(value = "/sprites/{gameName}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getSpritesForGame(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new ArrayList<>();
        }
        return gameService.getSprites().get(gameName);
    }

    @RequestMapping(value = "/sprites/alphabet", method = RequestMethod.GET)
    @ResponseBody
    public String getSpritesAlphabet() {
        return String.valueOf(GuiPlotColorDecoder.GUI.toCharArray());
    }

    @RequestMapping(value = "/context", method = RequestMethod.GET)
    @ResponseBody
    public String getContext() {
        String contextPath = servletContext.getContextPath();
        if (contextPath.charAt(contextPath.length() - 1) == '/') {
            contextPath += contextPath.substring(0, contextPath.length() - 1);
        }
        return contextPath;
    }

    @RequestMapping(value = "/game/{gameName}/type", method = RequestMethod.GET)
    @ResponseBody
    public GameTypeInfo getGameType(@PathVariable("gameName") String gameName) {
        if (StringUtils.isEmpty(gameName)) {
            return new GameTypeInfo(NullGameType.INSTANCE);
        }
        GameType game = gameService.getGame(gameName);

        return new GameTypeInfo(game);
    }

    @RequestMapping(value = "/player/{playerName}/{code}/level/{level}", method = RequestMethod.GET)
    @ResponseBody
    public boolean changeLevel(@PathVariable("playerName") String playerName,
                                @PathVariable("code") String code,
                                @PathVariable("level") int level)
    {
        validator.checkPlayerCode(playerName, code);
        playerGames.changeLevel(playerName, level);
        return true;
    }

    // TODO test me
    @RequestMapping(value = "/player/all/groups", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, List<List<String>>> getPlayersGroups() {
        Map<String, List<List<String>>> result = new HashMap<>();
        List<Player> players = playerService.getAll();
        List<List<String>> groups = playerGamesView.getGroups();
        for (List<String> group : groups) {
            String playerName = group.get(0);
            Player player = players.stream()
                    .filter(p -> p.getName().equals(playerName))
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

    @RequestMapping(value = "/player/all/scores", method = RequestMethod.GET)
    @ResponseBody
    public Map<String, Object> getPlayersScores() {
        return playerGamesView.getScores();
    }

    private void verifyIsAdmin(String adminPassword) {
        validator.validateAdmin(properties.getAdminPassword(), adminPassword);
    }

    @RequestMapping(value = "/scores/clear/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean clearAllScores(@PathVariable("adminPassword") String adminPassword) {
        verifyIsAdmin(adminPassword);

        playerService.cleanAllScores();

        return true;
    }

    @RequestMapping(value = "/game/enabled/{enabled}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean startStopGame(@PathVariable("adminPassword") String adminPassword,
                                  @PathVariable("enabled") boolean enabled)
    {
        verifyIsAdmin(adminPassword);

        if (enabled) {
            timerService.resume();
        } else {
            timerService.pause();
        }

        return timerService.isPaused();
    }

    // TODO test me
    @RequestMapping(value = "/player/{playerName}/{code}/wantsToPlay/{gameName}", method = RequestMethod.GET)
    @ResponseBody
    public PPlayerWantsToPlay playerWantsToPlay(
            @PathVariable("playerName") String playerName,
            @PathVariable("code") String code,
            @PathVariable("gameName") String gameName)
    {
        String context = getContext();
        GameTypeInfo gameType = getGameType(gameName);
        boolean registered = registrationController.checkUserLogin(playerName, code);
        List<String> sprites = getSpritesForGame(gameName);
        String alphabet = getSpritesAlphabet();
        List<PlayerInfo> players = registrationController.getGamePlayers(gameName);

        return new PPlayerWantsToPlay(context, gameType,
                registered, sprites, alphabet, players);
    }
}
