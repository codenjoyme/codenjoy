package com.codenjoy.dojo.services.httpclient;

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

import com.codenjoy.dojo.services.entity.server.PParameters;
import com.codenjoy.dojo.services.entity.server.PlayerDetailInfo;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.util.List;

/**
 * Open-feign game-server client declaration.
 *
 * @see GameClientResolver GameClientResolver for more details on client configuration
 */
public interface GameServerClient {

    @RequestLine("GET /codenjoy-contest/rest/game/{game}/players")
    List<PlayerInfo> getPlayerInfos(@Param("game") String game);

    @RequestLine("GET /codenjoy-contest/rest/admin/room/{game}/settings/{game}")
    PParameters getGameSettings(@Param("game") String game);

    @RequestLine("POST /codenjoy-contest/rest/admin/room/{game}/settings/{game}")
    @Headers({"Content-Type: application/json"})
    void setGameSettings(@Param("game") String game, PParameters parameters);

    @RequestLine("POST /codenjoy-contest/rest/player/create")
    @Headers({"Content-Type: application/json"})
    String registerPlayer(PlayerDetailInfo player);

    @RequestLine("GET /codenjoy-contest/rest/player/{id}/exists")
    Boolean checkPlayerExists(@Param("id") String id);

    @RequestLine("GET /codenjoy-contest/rest/admin/scores/clear")
    void clearScores();

    @RequestLine("GET /codenjoy-contest/rest/admin/game/enabled/{enabled}")
    Boolean checkGameEnabled(@Param("enabled") Boolean enabled);

    @RequestLine("GET /codenjoy-contest/rest/admin/player/{id}/remove")
    Boolean removePlayer(@Param("id") String id);
}
