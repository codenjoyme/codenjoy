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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
public class GameServerServiceTest {

    @InjectMocks
    private GameServerServiceImpl gameServerService;

    @Mock
    private ConfigProperties config;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private OAuth2CodeExecutionClient OAuth2CodeExecutionClient;

    private String username = "dummy-user";


    @Test
    public void createOrGetRepositoryRepositoryTest() {
        gameServerService.createOrGetRepository(username);
        verify(config, times(1)).getGitHubHostName();
        verify(config, times(1)).getGitHubPort();
    }

    @Test
    public void recoverTest() {
        String expected = "Repository not found!";
        String actual = gameServerService.recover(username);
        assertEquals(expected, actual);
    }
}
