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


import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
public class RestGameControllerTests {

    public static final String GITHUB_USERNAME = "username";
    public static final long SCORE = 5L;

    @InjectMocks
    private RestGameController restGameController;

    @Mock
    private PlayerService playerService;
    @Mock
    private UpdateHandler updateHandler;

    @Test
    public void updateUserScore() {
        //Arrange
        doNothing().when(playerService).updateScore(GITHUB_USERNAME, SCORE);

        //Act
        restGameController.updateUserScore(GITHUB_USERNAME, SCORE);

        //Assert
        verify(playerService, times(1)).updateScore(GITHUB_USERNAME, SCORE);
        verify(updateHandler, times(1)).sendUpdate(GITHUB_USERNAME, SCORE);
    }
}
