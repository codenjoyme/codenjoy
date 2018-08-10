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
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.settings.Parameter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.util.*;

@Controller
@RequestMapping(value = "/rest")
public class RestBoardController {

    @Autowired private GameService gameService;
    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private ServletContext servletContext;

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

    static class GameTypeInfo {
        private final String version;
        private final String info;
        private final int boardSize;
        private final List<Parameter<?>> parameters;
        private final MultiplayerType multiplayerType;

        GameTypeInfo(GameType gameType) {
            version = gameType.getVersion();
            info = gameType.toString();
            boardSize = gameType.getBoardSize().getValue();
            parameters = gameType.getSettings().getParameters();
            multiplayerType = gameType.getMultiplayerType();
        }

        public String getVersion() {
            return version;
        }

        public String getInfo() {
            return info;
        }

        public int getBoardSize() {
            return boardSize;
        }

        public List<Parameter<?>> getParameters() {
            return parameters;
        }

        public MultiplayerType getMultiplayerType() {
            return multiplayerType;
        }
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

}
