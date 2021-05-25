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

import com.codenjoy.dojo.services.dao.ActionLogger;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.incativity.InactivitySettings;
import com.codenjoy.dojo.services.incativity.InactivitySettingsImpl;
import com.codenjoy.dojo.services.log.DebugService;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.ViewDelegationService;
import com.codenjoy.dojo.services.semifinal.SemifinalService;
import com.codenjoy.dojo.services.settings.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static java.util.function.Predicate.not;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminService {

    private final TimerService timerService;
    private final TimeService timeService;
    private final PlayerService playerService;
    private final SaveService saveService;
    private final GameService gameService;
    private final ActionLogger actionLogger;
    private final AutoSaver autoSaver;
    private final DebugService debugService;
    private final Registration registration;
    private final ViewDelegationService viewDelegationService;
    private final SemifinalService semifinal;
    private final RoomService roomService;

    public void updateInactivity(String room, InactivitySettings updated) {
        InactivitySettingsImpl actual = inactivitySettings(room);
        boolean changed = actual
                .update(updated)
                .getInactivityParams().stream()
                .anyMatch(parameter -> ((Parameter) parameter).changed());
        actual.changesReacted();
        if (changed) {
            playerService.getAllInRoom(room).stream()
                    .filter(not(Player::hasAi))
                    .forEach(player -> player.setLastResponse(timeService.now()));
        }
    }

    public InactivitySettingsImpl inactivitySettings(String room) {
        return InactivitySettings.get(roomService.settings(room));
    }
}
