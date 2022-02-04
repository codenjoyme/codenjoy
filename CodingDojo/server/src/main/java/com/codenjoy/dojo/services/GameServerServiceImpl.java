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

import com.codenjoy.dojo.config.oauth2.OAuth2CodeExecutionClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GameServerServiceImpl implements GameServerService {
    public static final String REPOSITORY_NOT_FOUND = "Repository not found!";
    private final ConfigProperties config;
    private final OAuth2CodeExecutionClient OAuth2CodeExecutionClient;

    @Autowired
    public GameServerServiceImpl(ConfigProperties config, OAuth2CodeExecutionClient OAuth2CodeExecutionClient) {
        this.config = config;
        this.OAuth2CodeExecutionClient = OAuth2CodeExecutionClient;
    }

    @Override
    public String createOrGetRepository(String gitHubUsername) {
        return OAuth2CodeExecutionClient.getRequest(createHostUrl(gitHubUsername, ""));
    }

    @Override
    public String createOrGetRepositoryWithGame(String gitHubUsername, String game) {
        return OAuth2CodeExecutionClient.getRequest(createHostUrl(gitHubUsername, game));
    }

    @Override
    public String recover(String gitHubUsername) {
        return REPOSITORY_NOT_FOUND;
    }

    private String createHostUrl(String username, String game) {
        return "http://" + config.getGitHubHostName() + ":" +
                config.getGitHubPort() + "/repository?username="
                + username + "&game=" + game;
    }
}
