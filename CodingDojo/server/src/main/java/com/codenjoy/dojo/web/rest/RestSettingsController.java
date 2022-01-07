package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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
import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Deals;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.dao.GameData;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.Settings;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PParameters;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.AdminService.onlyUngrouped;
import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;
import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping(RestSettingsController.URI)
@AllArgsConstructor
public class RestSettingsController {

    public static final String URI = "/rest/settings";
    public static final String SETTINGS = "_settings_";
    public static final String GENERAL = "general";

    private Deals deals;
    private GameService gameService;
    private GameData gameData;
    private Validator validator;

    @GetMapping("/player")
    @Secured(GameAuthoritiesConstants.ROLE_USER)
    public Map<String, String> getForPlayer(@AuthenticationPrincipal Registration.User user) {
        if (user == null) {
            throw new IllegalArgumentException(
                    "Please join the game to check room settings");
        }
        String id = user.getId();

        Deal deal = deals.get(id);
        GameType gameType = deal.getGameType();

        List<Parameter> parameters = gameType.getSettings().getParameters();

        return parameters.stream()
                .filter(onlyUngrouped())
                .collect(toMap(Parameter::getName,
                        parameter -> String.valueOf(parameter.getValue())));
    }

    @GetMapping("/{game}/{room}/{key}")
    public String get(@PathVariable("game") String game,
                      @PathVariable("room") String room,
                      @PathVariable("key") String key)
    {
        validator.checkNotEmpty("key", key);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CAN_BE_NULL);

        if (GENERAL.equals(game)) {
            return gameData.get(game, key);
        }

        validator.checkGameType(game);

        // если не указывают им комнаты, используем комнату по умолчанию для всех игр
        if (Validator.isEmpty(room)) {
            room = game;
        }

        GameType type = gameService.getGameType(game, room);

        Settings settings = type.getSettings();
        if (key.equals(SETTINGS)) {
            PParameters parameters = new PParameters(settings.getParameters());
            return new JSONObject(parameters).toString();
        }

        if (settings.hasParameter(key)) {
            return settings.getParameter(key).getValue().toString();
        }

        return gameData.get(game, key);
    }

    @Secured(GameAuthoritiesConstants.ROLE_ADMIN)
    @PostMapping("/{game}/{room}/{key}")
    public String set(@PathVariable("game") String game,
                      @PathVariable("room") String room,
                      @PathVariable("key") String key,
                      @RequestBody String value)
    {
        validator.checkNotEmpty("key", key);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CAN_BE_NULL);

        value = encode(value);

        if (GENERAL.equals(game)) {
            gameData.set(game, key, value);
            return "{}";
        }

        validator.checkGameType(game);

        // если не указывают им комнаты, используем комнату по умолчанию для всех игр
        if (Validator.isEmpty(room)) {
            room = game;
        }

        GameType type = gameService.getGameType(game, room);

        Settings settings = type.getSettings();
        if (settings.hasParameter(key)) {
            settings.getParameter(key).update(value);
            return "{}";
        }

        gameData.set(game, key, value);
        return "{}";
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
