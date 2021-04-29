package com.codenjoy.dojo.web.rest;

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

import com.codenjoy.dojo.services.GameServerService;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class RestRegistrationControllerTest {

    public static final String OLD_GITHUB_USERNAME = "dummy-username";
    public static final String NEW_GITHUB_USERNAME = "new-username";

    @InjectMocks
    private RestRegistrationController restRegistrationController;

    @Mock
    private Registration registration;
    @Mock
    private PlayerService playerService;
    @Mock
    private GameServerService gameServerService;

    @Test
    public void updateGitHubUsernameWhenIsExistingUsername() {
        //Arrange
        String repository = "dummy-repository";
        when(registration.getIdByGitHubUsername(OLD_GITHUB_USERNAME)).thenReturn("id");
        when(gameServerService.createOrGetRepository(NEW_GITHUB_USERNAME)).thenReturn(repository);
        when(registration.updateGitHubUsername(OLD_GITHUB_USERNAME, NEW_GITHUB_USERNAME)).thenReturn(1);

        //Act
        int actual = restRegistrationController.updateGitHubUsername(OLD_GITHUB_USERNAME, NEW_GITHUB_USERNAME);

        //Assert
        assertEquals(1, actual);
        verify(registration, times(1)).getIdByGitHubUsername(OLD_GITHUB_USERNAME);
        verify(gameServerService, times(1)).createOrGetRepository(NEW_GITHUB_USERNAME);
        verify(registration, times(1)).updateGitHubUsername(OLD_GITHUB_USERNAME, NEW_GITHUB_USERNAME);
    }

    @Test
    public void updateGitHubUsernameWhenIsNotExistingUsername() {
        //Arrange

        //Act
        int actual = restRegistrationController.updateGitHubUsername(OLD_GITHUB_USERNAME, NEW_GITHUB_USERNAME);

        //Assert
        assertEquals(0, actual);
        verify(registration, times(1)).getIdByGitHubUsername(OLD_GITHUB_USERNAME);
    }
}
