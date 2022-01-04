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

import com.codenjoy.dojo.services.room.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.codenjoy.dojo.services.Deals.exclude;
import static com.codenjoy.dojo.services.Deals.withRoom;
import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
public class ScoresCleaner {

    private final Deals deals;
    private final GameSaver saver;

    private final RoomService roomService;
    private final GameService gameService;
    private final TimeService time;

    /**
     * Clean scores for all available deals.
     */
    public void cleanAllScores() {
        List<Deal> active = deals.all();
        List<String> saved = saver.getSavedList();
        cleanAllSavedScores(active, saved);
    }

    /**
     * Clean scores for deals in a particular room.
     */
    public void cleanAllScores(String room) {
        List<Deal> active = deals.getAll(withRoom(room));
        List<String> saved = saver.getSavedList(room);
        cleanAllSavedScores(active, saved);
    }

    /**
     * Clean scores for a particular deal.
     */
    public void cleanScores(String id) {
        deals.get(id).clearScore();
        cleanSavedScore(id);
    }

    private void cleanAllSavedScores(List<Deal> active, List<String> saved) {
        active.forEach(Deal::clearScore);
        saved.forEach(this::cleanSavedScore);

        List<Deal> notSaved = active.stream()
                .filter(exclude(saved))
                .collect(toList());
        saver.saveGames(notSaved, time.now());
    }

    private void cleanSavedScore(String id) {
        PlayerSave playerSave = saver.loadGame(id);
        GameType type = roomService.gameType(playerSave.getRoom());
        String save = gameService.getDefaultProgress(type);
        Player player = new Player(playerSave);
        player.setScore(0);
        saver.saveGame(player, playerSave.getTeamId(), save, time.now());
    }
}
