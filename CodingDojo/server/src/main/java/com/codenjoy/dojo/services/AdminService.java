package com.codenjoy.dojo.services;

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
