package com.codenjoy.dojo.services.controller;

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

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.controller.control.PlayerResponseHandler;
import com.codenjoy.dojo.transport.ws.PlayerSocket;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class PlayerResponseHandlerTest {

    @Test
    public void shouldSetLastResponseTimeToPlayer_andCallJoystick_onResponse() {
        // given
        Joystick joystick = mock(Joystick.class);
        PlayerSocket playerSocket = mock(PlayerSocket.class);
        Player player = spy(new Player());
        PlayerResponseHandler playerResponseHandler = new PlayerResponseHandler(player, joystick, () -> 123L);

        // when
        playerResponseHandler.onResponse(playerSocket, "act");

        // then
        verify(player, times(1)).setLastResponse(123L);
        verify(joystick, times(1)).act();
    }
}
