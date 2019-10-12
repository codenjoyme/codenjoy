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

import com.codenjoy.dojo.web.rest.pojo.GameTypeInfo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by Oleksandr_Baglai on 2019-10-12.
 */
@Component
@Getter
@Setter
public class Semifinal implements Tickable {

    @Value("${game.semifinal.enabled}")
    private boolean enabled;

    @Value("${game.semifinal.percentage}")
    private boolean percentage;

    @Value("${game.semifinal.limit}")
    private int limit;

    @Value("${game.semifinal.timeout}")
    private int timeout;

    @Value("${game.semifinal.reset-board}")
    private boolean resetBoard;

    @Autowired
    protected PlayerGames playerGames;

    private int time;

    public void disable() {
        enabled = false;
    }

    public void enable() {
        enabled = true;
    }

    public void clean() {
        time = 0;
    }

    @Override
    public void tick() {
        if (!enabled) return;

        if (++time % timeout != 0) return;

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
            if (games.size() <= 1) return;
            int from = percentage
                    ? (int)((1D - 1D*limit/100)*games.size())
                    : (games.size() - Math.min(limit, games.size()));
            toRemove.addAll(games.subList(0, from));
        });

        toRemove.forEach(game -> playerGames.remove(game.getPlayer()));

        if (resetBoard) {
            playerGames.forEach(PlayerGame::clearScore);
        }
    }
}
