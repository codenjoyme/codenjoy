package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.DispatcherSettings;
import com.codenjoy.dojo.services.entity.server.PlayerDetailInfo;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.entity.server.User;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.web.controller.GlobalExceptionHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class GameServer {

    private static Logger logger = DLoggerFactory.getLogger(GameServer.class);

    @Autowired ConfigProperties config;
    @Autowired DispatcherSettings settings;

    private String playerExistsUrl(String server, String email) {
        return String.format(settings.getUrlExistsPlayer(),
                server,
                config.getId(email));
    }

    private String gameEnabledUrl(String server, boolean enabled) {
        return String.format(settings.getUrlGameEnabled(),
                server,
                enabled,
                config.getAdminToken());
    }

    private String getPlayersUrl(String server) {
        return String.format(settings.getUrlGetPlayers(),
                server,
                settings.getGameType());
    }

    private String createPlayerUrl(String server) {
        return String.format(settings.getUrlCreatePlayer(),
                server,
                config.getAdminToken());
    }

    private String removePlayerUrl(String server, String email, String code) {
        return String.format(settings.getUrlRemovePlayer(),
                server,
                config.getId(email),
                code);
    }

    private String clearPlayersScoreUrl(String server) {
        return String.format(settings.getUrlClearScores(),
                server,
                config.getAdminToken());
    }

    public List<PlayerInfo> getPlayersInfos(String server) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<List<PlayerInfo>> entity = rest.exchange(
                getPlayersUrl(server),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlayerInfo>>(){});

        return entity.getBody();
    }

    public String createNewPlayer(String server, String email, String name,
                                  String password, String callbackUrl,
                                  String score, String save)
    {
        String id = config.getId(email);
        String code = Hash.getCode(email, password);

        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> entity = rest.postForEntity(
                createPlayerUrl(server),
                new PlayerDetailInfo(
                        id,
                        name,
                        callbackUrl,
                        settings.getGameType(),
                        score,
                        save,
                        new User(
                                id,
                                name,
                                1,
                                password,
                                code,
                                null)

                ),
                String.class);

        return entity.getBody();
    }

    public boolean existsOnServer(String server, String email) {
        try {
            RestTemplate rest = new RestTemplate();
            ResponseEntity<Boolean> entity = rest.exchange(
                    playerExistsUrl(server, email),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Boolean>(){});

            return entity.getBody();
        } catch (RestClientException e) {
            logger.error("Error check player exists on server: " + server, e);
            return false;
        }
    }

    public String clearScores(String server) {
        try {
            RestTemplate rest = new RestTemplate();
            ResponseEntity<Void> entity = rest.exchange(
                    clearPlayersScoreUrl(server),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Void>(){});

            return "Successful with code: " + entity.getStatusCode();

        } catch (RestClientException e) {
            logger.error("Error clearing scores on server: " + server, e);

            return GlobalExceptionHandler.getPrintableMessage(e);
        }
    }

    public String gameEnable(String server, boolean enable) {
        String status = enable ? "start" : "stop";
        try {
            RestTemplate rest = new RestTemplate();
            ResponseEntity<Boolean> entity = rest.exchange(
                    gameEnabledUrl(server, enable),
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<Boolean>(){});

            return "Successful " + status + " game: " + entity.getBody();

        } catch (RestClientException e) {
            logger.error("Error " + status + " game on server: " + server, e);

            return GlobalExceptionHandler.getPrintableMessage(e);
        }
    }

    public Boolean remove(String server, String email, String code) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Boolean> entity = rest.exchange(
                removePlayerUrl(server, email, code),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Boolean>(){});
        return entity.getBody();
    }


}
