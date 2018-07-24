package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.bomberman.services.Level1;
import com.codenjoy.dojo.services.GameInfo;
import com.codenjoy.dojo.services.LevelInfo;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Bomberman implements Tickable,LevelInfo {

    private GameSettings settings; //общие настройки игры

    private Level level;

    public Bomberman(GameSettings settings) {
        this.settings = settings;
        level = new LevelImpl(Level1.get(), settings);
    }

    public GameSettings getSettings() {
        return settings;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public void tick() {
        level.tick();
    }

    public void newGame(Player player) {
        if (!level.getPlayers().contains(player)) {
            level.getPlayers().add(player);
        }

        List<Player> relevantPlayersRegistry = getRelevantPlayersRegistry(player);
        if (!relevantPlayersRegistry.contains(player)) {
            relevantPlayersRegistry.add(player);
        }
        player.newHero(this);
    }

    private List<Player> getRelevantPlayersRegistry(Player player) {
        return player.isBot() ? level.getBotPlayers() : level.getNonBotPlayers();
    }

    public BoardReader reader() {
        return new BoardReader() {
//            private int size = Bomberman.this.level.size();

            @Override
            public int size() {
                return Bomberman.this.level.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(Bomberman.this.level.getBombermans());
                for (Wall wall : Bomberman.this.level.getWalls()) {
                    result.add(wall);
                }
                result.addAll(Bomberman.this.level.getBombs());
                result.addAll(Bomberman.this.level.getBlasts());
                return result;
            }
        };
    }

  @Override
  public GameInfo getGameInfo() {
    return level;
  }
}
