package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.GuiPlotColorDecoder;
import com.codenjoy.dojo.web.rest.pojo.PGameTypeInfo;
import com.codenjoy.dojo.web.rest.pojo.PSprites;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/game")
@AllArgsConstructor
public class RestGameController {

    private GameService gameService;

    @GetMapping("/{name}/exists")
    public Boolean exists(@PathVariable("name") String name) {
        return gameService.getGameNames().contains(name);
    }

    @GetMapping("/{name}/info")
    public PGameTypeInfo type(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        GameType game = gameService.getGame(name);

        PSprites sprites = new PSprites(spritesAlphabet(), spritesUrl(name),
                spritesNames(name), spritesValues(name));
        
        return new PGameTypeInfo(game, help(name), client(name), ws(), sprites);
    }

    @GetMapping("/{name}/help/url")
    public String help(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        // TODO тут было бы неплохо получить так же http[s]://domain:port/ 
        return String.format("/%s/resources/help/%s.html",
                CodenjoyContext.getContext(), name);
    }

    @GetMapping("/{name}/client/url")
    public String client(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        // TODO тут было бы неплохо получить так же http[s]://domain:port/
        return String.format("/%s/resources/user/%s-servers.zip",
                CodenjoyContext.getContext(), name);
    }

    @GetMapping("/ws/url")
    public String ws() {
        // TODO тут было бы неплохо получить так же SERVER:PORT 
        // TODO а еще надо подумать если юзер авторизирован, то можно выдать его PLAYER_ID & CODE 
        return String.format("ws[s]://SERVER:PORT/%s/ws?user=PLAYER_ID&code=CODE",
                CodenjoyContext.getContext());
    }

    @GetMapping("/sprites")
    public Map<String, List<String>> allSprites() {
        return gameService.getSprites();
    }

    @GetMapping("/{name}/sprites/exists")
    public Boolean isGraphic(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        return !spritesNames(name).isEmpty();
    }

    @GetMapping("/{name}/sprites/names")
    public List<String> spritesNames(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        return gameService.getSpritesNames().get(name);
    }

    @GetMapping("/{name}/sprites/values")
    public List<String> spritesValues(@PathVariable("name") String name) {
        if (!exists(name)) {
            return null;
        }

        return gameService.getSpritesValues().get(name);
    }

    @GetMapping("/{name}/sprites/url")
    public String spritesUrl(@PathVariable("name") String name) {
        if (!exists(name) || !isGraphic(name)) {
            return null;
        }

        // TODO тут было бы неплохо получить так же http[s]://domain:port/
        return String.format("/%s/resources/sprite/%s/%s.png",
                CodenjoyContext.getContext(), name, "*");
    }

    @GetMapping("/sprites/alphabet")
    public String spritesAlphabet() {
        return String.valueOf(GuiPlotColorDecoder.GUI.toCharArray());
    }

}

