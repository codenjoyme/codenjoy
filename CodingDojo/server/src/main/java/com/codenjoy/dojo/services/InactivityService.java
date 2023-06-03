package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.incativity.InactivitySettings;
import com.codenjoy.dojo.services.multiplayer.Sweeper;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.settings.Settings;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InactivityService implements Tickable {

    private final Deals deals;
    private final TimeService timeService;
    private final RoomService roomService;

    @Override
    public void tick() {
        for (Deal game : deals.getAll(roomService::isRoomActive)) {
            GameType gameType = game.getGameType();
            Settings settings = gameType.getSettings();
            Player player = game.getPlayer();

            if (!InactivitySettings.is(settings)) return;

            InactivitySettings inactivity = InactivitySettings.get(settings);
            if (!inactivity.isKickEnabled()) return;

            int timeout = inactivity.getInactivityTimeout();

            long now = timeService.now();
            long delta = now - player.getLastResponse();
            if (delta >= 1000L * timeout) {
                removePlayer(player);
            }
        }
    }

    private void removePlayer(Player player) {
        try {
            deals.remove(player.getId(), Sweeper.off());
        } catch (Exception e) {
            String message = String.format("Unable to remove player %s", player);
            log.warn(message, e);
        }
    }
}
