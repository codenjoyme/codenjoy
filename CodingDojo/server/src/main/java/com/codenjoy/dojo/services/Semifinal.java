package com.codenjoy.dojo.services;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.codenjoy.dojo.services.PlayerGames.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Oleksandr_Baglai on 2019-10-12.
 */
@Component
public class Semifinal implements Tickable {

    @Autowired
    protected SemifinalSettings settings;

    @Autowired
    protected PlayerGames playerGames;

    private int time;

    public void clean() {
        time = 0;
    }

    @Override
    public void tick() {
        // если режим включен - выходим
        // TODO эта настройка должна включаться и выключаться не для всех комнат, а для заданных в настройках
        if (!settings.isEnabled()) return;

        // если не с кем работать - выходим
        if (playerGames.isEmpty()) return;

        // ждем заданное количество тиков
        if (++time % settings.getTimeout() != 0) return;
        time = 0;

        // получаем мапу по комнатам, где значениями являются сортированные
        // по очкам списки PlayerGame
        Map<String, List<PlayerGame>> map =
                playerGames.getRooms(ACTIVE).stream()
                    .collect(toMap(room -> room,
                            room -> playerGames.getAll(withRoom(room))
                                        .stream()
                                        .sorted(byScore())
                                        .collect(toList())));

        List<PlayerGame> toRemove = new LinkedList<>();
        map.values().forEach(games -> {
            // единственного героя оставляем и не удаляем
            if (games.size() <= 1) return;

            // адская формула рассчета индекса разделения списка
            int index = settings.isPercentage()
                    ? (int)((1D - 1D*settings.getLimit()/100)*games.size())
                    : (games.size() - Math.min(settings.getLimit(), games.size()));

            // если на границе "отрезания" есть участники с тем же числом очков,
            // что и последний "проскочивший" участник, мы должны оставить их всех
            while (index > 0 &&
                    (games.get(index - 1).getPlayer().getScore() ==
                     games.get(index).getPlayer().getScore()))
            {
                index--;
            }

            // готовим список для удаления
            toRemove.addAll(games.subList(0, index));
        });

        // собственно удаление
        toRemove.forEach(game -> playerGames.removeCurrent(game.getPlayer()));

        // если после удаления надо перегруппировать участников по бордам
        if (settings.isResetBoard()) {
            playerGames.reloadAll(settings.isShuffleBoard(), playerGames.withActive());
        }
    }

    private Comparator<PlayerGame> byScore() {
        return Comparator.comparingInt(game -> (Integer)game.getPlayer().getScore());
    }

    public SemifinalSettings settings() {
        return settings;
    }

    public int getTime() {
        return time;
    }
}
