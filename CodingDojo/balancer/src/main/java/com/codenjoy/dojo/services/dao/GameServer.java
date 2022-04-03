package com.codenjoy.dojo.services.dao;

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

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.entity.server.PParameters;
import com.codenjoy.dojo.services.entity.server.PlayerDetailInfo;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.entity.server.User;
import com.codenjoy.dojo.services.httpclient.GameClientResolver;
import com.codenjoy.dojo.services.httpclient.GameServerClientException;
import com.codenjoy.dojo.web.controller.BalancerErrorTicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static com.codenjoy.dojo.conf.Authority.ROLE_USER;

@Slf4j
@Component
public class GameServer {

    @Autowired ConfigProperties config;
    @Autowired GameClientResolver gameClientResolver;

    public List<PlayerInfo> getPlayersInfos(String server) {
        return gameClientResolver.resolveClient(server).getPlayerInfos(config.getGame().getType());
    }

    public PParameters getGameSettings(String server) {
        return gameClientResolver.resolveClient(server).getGameSettings(config.getGame().getType());
    }

    public void setGameSettings(String server, PParameters parameters) {
        gameClientResolver.resolveClient(server).setGameSettings(config.getGame().getType(), parameters);
    }

    public String createNewPlayer(String server, String id, String code,
                                  String email, String phone, String name,
                                  String hashedPassword, String callbackUrl,
                                  String score, String save)
    {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Create new player {} ({}) for '{}' on server {} with save {} and score {}",
                        id, code, name, server, save, score);
            }

            PlayerDetailInfo player = new PlayerDetailInfo(
                id,
                name,
                callbackUrl,
                config.getGame().getType(),
                config.getGame().getType(),
                score,
                save,
                new User(
                    id,
                    email,
                    phone,
                    name,
                    User.APPROVED,
                    hashedPassword,
                    code,
                    "{}",
                    Arrays.asList(ROLE_USER.name()))
            );

            return gameClientResolver.resolveClient(server).registerPlayer(player);
        } catch (GameServerClientException e) {
            String message = "Cant create new player. Status is: " + e.getMessage();
            log.error(message);
            throw e;
        }
    }

    public boolean existsOnServer(String server, String id) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Check is player {} exists on server {}",
                        id, server);
            }

            return gameClientResolver.resolveClient(server).checkPlayerExists(id);
        } catch (GameServerClientException e) {
            log.error("Error check player exists on server: " + server, e);
            return false;
        }
    }

    public String clearScores(String server) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Clear all scores on server {}",
                        server);
            }

            gameClientResolver.resolveClient(server).clearScores();

            return "Successful";

        } catch (GameServerClientException e) {
            log.error("Error clearing scores on server: " + server, e);

            return BalancerErrorTicketService.getPrintableMessage(e);
        }
    }

    public String gameEnable(String server, boolean enable) {
        String status = status(enable);
        try {
            if (log.isDebugEnabled()) {
                log.debug("Set status {} of game on server {}",
                        status, server);
            }

            Boolean enabled = gameClientResolver.resolveClient(server).checkGameEnabled(enable);
            return status(enabled);
        } catch (GameServerClientException e) {
            log.error("Error " + status + " game on server: " + server, e);

            return BalancerErrorTicketService.getPrintableMessage(e);
        }
    }

    private String status(boolean enable) {
        return enable ? "start" : "stop";
    }

    public Boolean remove(String server, String id) {
        try {
            if (log.isDebugEnabled()) {
                log.debug("Remove player {} on server {}",
                        id, server);
            }
            Boolean removed = gameClientResolver.resolveClient(server).removePlayer(id);
            return removed;
        } catch (GameServerClientException e) {
            String message = "Cant remove player. Status is: " + e.getMessage();
            log.error(message);
            return false;
        }
    }
}
