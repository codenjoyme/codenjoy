package com.codenjoy.dojo.services.controller.control;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.springframework.stereotype.Component;

@Component
public class PlayerController implements Controller<String, Joystick> {

    private final PlayerTransport transport;
    private final TimeService timeService;

    // autowiring by name
    public PlayerController(PlayerTransport controlPlayerTransport, TimeService timeService) {
        transport = controlPlayerTransport;
        transport.setDefaultFilter(Object::toString);
        this.timeService = timeService;
    }

    @Override
    public int requestControl(Player player, String board) {
        return transport.sendState(player.getId(), new BoardGameState(board));
    }

    @Override
    public void register(Deal deal) {
        transport.registerPlayerEndpoint(deal.getPlayerId(),
                new PlayerResponseHandler(deal.getPlayer(),
                        deal.getJoystick(), timeService.future()));
    }

    @Override
    public void unregister(Deal deal) {
        transport.unregisterPlayerEndpoint(deal.getPlayerId());
    }
}
