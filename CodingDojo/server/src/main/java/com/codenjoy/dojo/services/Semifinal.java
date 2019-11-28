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
        // если режим включен не очищаем
        if (!settings.isEnabled()) return;

        // ждем заданное количество тиков
        if (++time % settings.getTimeout() != 0) return;
        time = 0;

        // получаем мапу по играм, где значениями являются сортированные по очкам списки PlayerGame
        Map<String, List<PlayerGame>> map =
                playerGames.getGameTypes().stream()
                    .map(GameType::name)
                    .distinct()
                    .collect(toMap(name -> name,
                            name -> playerGames.getAll(name)
                                        .stream()
                                        .sorted(Comparator.comparingInt(game -> (Integer)game.getPlayer().getScore()))
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
            playerGames.reloadAll(settings.isShuffleBoard());
        }
    }

    public SemifinalSettings settings() {
        return settings;
    }

    public int getTime() {
        return time;
    }
}
