package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DojoPlayerService {

    private final Registration registration;
    private final GameServerServiceImpl gameServerService;
    private final PlayerService playerService;
    private final UpdateHandler updateHandler;

    @Autowired
    public DojoPlayerService(Registration registration, GameServerServiceImpl gameServerService, PlayerService playerService, UpdateHandler updateHandler) {
        this.registration = registration;
        this.gameServerService = gameServerService;
        this.playerService = playerService;
        this.updateHandler = updateHandler;
    }

    public int updateGitHubUsername(String username, String newUsername) {

        String id = registration.getIdByGitHubUsername(username);
        if (id == null) {
            return 0;
        }
        String repository = gameServerService.createOrGetRepository(newUsername);

        playerService.updateUserRepository(id, repository);

        return registration.updateGitHubUsername(username, newUsername);
    }

    public void updateUserScore(String username, String game, long score) {

        updateHandler.sendUpdate(username, score);
        playerService.updateScore(username, game, score);
    }
}
