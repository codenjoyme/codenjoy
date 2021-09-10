package com.codenjoy.dojo.services.controller.screen;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.transport.ws.PlayerTransport;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class ScreenController implements Controller<Map<ScreenRecipient, ScreenData>, Void> {

    private final PlayerTransport transport;

    // autowiring by name
    public ScreenController(PlayerTransport screenPlayerTransport) {
        this.transport = screenPlayerTransport;
    }

    @PostConstruct
    public void init() {
        registerPlayerTransport(Deal.ANONYMOUS);
    }

    @Override
    public void requestControlToAll(Map<ScreenRecipient, ScreenData> data) {
        transport.sendStateToAll(data);
    }

    @Override
    public void registerPlayerTransport(Deal deal) {
        transport.registerPlayerEndpoint(deal.getPlayerId(),
                new ScreenResponseHandler(transport, deal.getPlayer()));
    }

    @Override
    public void unregisterPlayerTransport(Deal deal) {
        transport.unregisterPlayerEndpoint(deal.getPlayerId());
    }
}
