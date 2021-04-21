package com.codenjoy.dojo.services.semifinal;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.room.RoomState;
import com.codenjoy.dojo.services.round.RoundSettings;
import com.codenjoy.dojo.services.round.RoundSettingsImpl;
import com.codenjoy.dojo.services.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

import static com.codenjoy.dojo.services.PlayerGames.withRoom;
import static java.util.stream.Collectors.toList;

@Component
public class SemifinalService implements Tickable {

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected PlayerGames playerGames;

    public void clean() {
        for (String room : roomService.names()) {
            clean(room);
        }
    }

    public void clean(String room) {
        roomService.resetTick(room);
    }

    @Override
    public void tick() {
        for (RoomState state : roomService.all()) {
            Settings settings = state.getType().getSettings();

            // TODO не все игры могут это реализовать, а надо чтобы все
            if (!isSemifinalAllowed(settings)) continue;
            SemifinalSettings reader = (SemifinalSettings)settings;

            // если режим не включен - выходим
            if (!reader.isEnabled()) continue; // TODO потестить

            // если режим не включен - выходим
            if (!state.isActive()) continue; // TODO потестить

            state.tick();

            // ждем заданное количество тиков
            if (state.getTick() % reader.getTimeout() != 0) continue;
            state.resetTick();

            // если не с кем работать - выходим
            if (playerGames.isEmpty()) continue; // TODO потестить

            // получаем мапу по комнатам, где значениями являются сортированные
            // по очкам списки PlayerGame
            Predicate<PlayerGame> withRoom = withRoom(state.getName());
            List<PlayerGame> games =
                    playerGames.getAll(withRoom).stream()
                            .sorted(byScore())
                            .collect(toList());

            // единственного героя оставляем и не удаляем
            if (games.size() <= 1) continue;

            // адская формула рассчета индекса разделения списка
            int index = reader.isPercentage()
                    ? (int)Math.ceil((1D - 1D*reader.getLimit()/100)*(games.size() - 1))
                    : (games.size() - Math.min(reader.getLimit(), games.size()));

            // если на границе "отрезания" есть участники с тем же числом очков,
            // что и последний "проскочивший" участник, мы должны оставить их всех
            while (index > 0 &&
                    (games.get(index - 1).getPlayer().getScore() ==
                            games.get(index).getPlayer().getScore()))
            {
                index--;
            }

            // готовим список для удаления
            List<PlayerGame> toRemove = new LinkedList<>();
            toRemove.addAll(games.subList(0, index));

            // собственно удаление
            toRemove.forEach(game -> playerGames.removeCurrent(game.getPlayer()));

            // если после удаления надо перегруппировать участников по бордам
            if (reader.isResetBoard()) {
                playerGames.reloadAll(reader.isShuffleBoard(), withRoom);
            }
        }
    }

    private Comparator<PlayerGame> byScore() {
        return Comparator.comparingInt(game -> (Integer)game.getPlayer().getScore());
    }

    public boolean isSemifinalAllowed(Settings settings) {
        return settings instanceof SemifinalSettings;
    }

    public boolean isRoundAllowed(Settings settings) {
        return settings instanceof RoundSettings;
    }

    public SemifinalSettingsImpl semifinalSettings(String room) {
        Settings settings = roomService.settings(room);
        if (isSemifinalAllowed(settings)) {
            return new SemifinalSettingsImpl((SemifinalSettings) settings);
        } else {
            // на админке будет пусто в этой области
            return new SemifinalSettingsImpl(null);
        }
    }

    public SemifinalStatus getSemifinalStatus(String room) {
        int current = roomService.getTick(room);
        SemifinalSettings settings = semifinalSettings(room);
        int countPlayers = playerGames.getPlayersByRoom(room).size();
        return new SemifinalStatus(current, countPlayers, settings);
    }

    public RoundSettingsImpl roundSettings(String room) { // TODO перенести бы их куда-то
        Settings settings = roomService.settings(room);
        if (isRoundAllowed(settings)) {
            return new RoundSettingsImpl((RoundSettings) settings);
        } else {
            // на админке будет пусто в этой области
            return new RoundSettingsImpl(null);
        }
    }

    public int getTime(String room) {
        return roomService.getTick(room);
    }
}
