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


import com.codenjoy.dojo.client.Encoding;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.dao.GameData;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;

@RestController
@RequestMapping(RestSettingsController.URI)
@AllArgsConstructor
public class RestSettingsController {

    public static final String URI = "/rest/settings";
    public static final String SETTINGS = "_settings_";
    public static final String GENERAL = "general";

    private GameService gameService;
    private GameData gameData;
    private Validator validator;

    @GetMapping("/{gameName}/{roomName}/{key}")
    public String get(@PathVariable("gameName") String gameName,
                      @PathVariable("roomName") String roomName,
                      @PathVariable("key") String key)
    {
        validator.checkNotEmpty("key", key);
        validator.checkGameName(gameName, Validator.CANT_BE_NULL);
        validator.checkRoomName(roomName, Validator.CAN_BE_NULL);

        if (GENERAL.equals(gameName)) {
            return gameData.get(gameName, key);
        }

        validator.checkGameType(gameName);

        // если не указывают им комнаты, используем комнату по умолчанию для всех игр
        if (isEmpty(roomName)) {
            roomName = gameName;
        }

        GameType type = gameService.getGame(gameName, roomName);

        Settings settings = type.getSettings();
        if (key.equals(SETTINGS)) {
            PParameters parameters = new PParameters(settings.getParameters());
            return new JSONObject(parameters).toString();
        }

        if (settings.hasParameter(key)) {
            return settings.getParameter(key).getValue().toString();
        }

        return gameData.get(gameName, key);
    }

    @PostMapping("/{gameName}/{roomName}/{key}")
    public String set(@PathVariable("gameName") String gameName,
                      @PathVariable("roomName") String roomName,
                      @PathVariable("key") String key,
                      @RequestBody String value)
    {
        validator.checkNotEmpty("key", key);
        validator.checkGameName(gameName, Validator.CANT_BE_NULL);
        validator.checkRoomName(roomName, Validator.CAN_BE_NULL);

        value = encode(value);

        if (GENERAL.equals(gameName)) {
            gameData.set(gameName, key, value);
            return "{}";
        }

        validator.checkGameType(gameName);

        // если не указывают им комнаты, используем комнату по умолчанию для всех игр
        if (isEmpty(roomName)) {
            roomName = gameName;
        }

        GameType type = gameService.getGame(gameName, roomName);

        Settings settings = type.getSettings();
        if (settings.hasParameter(key)) {
            settings.getParameter(key).update(value);
            return "{}";
        }

        gameData.set(gameName, key, value);
        return "{}";
    }

    private boolean isEmpty(String roomName) {
        return StringUtils.isEmpty(roomName) || "null".equalsIgnoreCase(roomName);
    }

    @SneakyThrows
    private String encode(String value) {
        // TODO это странно, если данные приходят с кавычками не как строка а как часть данных, мы их удаляем все равно
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }

        return URLDecoder.decode(Encoding.replaceN(value), Encoding.UTF8);
    }
}
