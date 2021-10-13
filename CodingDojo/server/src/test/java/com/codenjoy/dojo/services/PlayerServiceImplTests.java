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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class PlayerServiceImplTests {

    public static final String GITHUB_USERNAME = "username";
    public static final String GAME_NAME = "random-game";

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private Registration registration;

    @Mock
    private PlayerGames playerGames;

    @Mock
    private PlayerGame playerGame;

    @Mock
    private Player player;

    @Mock
    private GameSaver saver;


    @Test
    public void updateScoreTest() {
        //Arrange
        when(registration.getIdByGitHubUsername(GITHUB_USERNAME)).thenReturn("1");
        when(playerGames.getAll()).thenReturn(Collections.singletonList(playerGame));
        when(player.getId()).thenReturn("1");
        when(playerGame.getPlayer()).thenReturn(player);

        //Act
        playerService.updateScore(GITHUB_USERNAME, GAME_NAME, 5);

        //Assert
        verify(registration, times(1)).getIdByGitHubUsername(GITHUB_USERNAME);
        verify(playerGames, times(1)).getAll();
        verify(player, times(1)).getId();
        verify(playerGame, times(1)).getPlayer();
    }
}
