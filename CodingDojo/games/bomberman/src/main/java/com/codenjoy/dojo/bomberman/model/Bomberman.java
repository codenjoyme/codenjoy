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


import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.bomberman.services.Level1;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:11 AM
 */
public class Bomberman implements Tickable {

    private GameSettings settings; //общие настройки игры

    private Level level;

    public Bomberman(GameSettings settings) {
        this.settings = settings;
        level = new LevelImpl(Level1.get());
    }

    public GameSettings getSettings() {
        return settings;
    }
    public Level getLevel() { return level; }

    @Override
    public void tick() {
        removeBlasts();
        tactAllPlayers();
        meatChopperEatBombermans();
        level.tick();
        meatChopperEatBombermans();
        tactAllBombs();
    }

    private void tactAllPlayers() {
        for (Player player : level.getPlayers()) {
            player.getBomberman().apply();
        }
    }

    private void removeBlasts() {
        level.getBlasts().clear();
        for (PointImpl pt : level.getDestroyedWalls()) {
            level.getWalls().destroy(pt.getX(), pt.getY());
        }
        level.getDestroyedWalls().clear();
    }

    private void wallDestroyed(Wall wall, Blast blast) {
        for (Player player : level.getPlayers()) {
            if (blast.itsMine(player.getBomberman())) {
                if (wall instanceof MeatChopper) {
                    player.event(Events.KILL_MEAT_CHOPPER);
                } else if (wall instanceof DestroyWall) {
                    player.event(Events.KILL_DESTROY_WALL);
                }
            }
        }
    }

    private void meatChopperEatBombermans() {
        for (MeatChopper chopper : level.getWalls().subList(MeatChopper.class)) {
            for (Player player : level.getPlayers()) {
                Hero bomberman = player.getBomberman();
                if (bomberman.isAlive() && chopper.itsMe(bomberman)) {
                    player.event(Events.KILL_BOMBERMAN);
                }
            }
        }
    }

    private void tactAllBombs() {
        for (Bomb bomb : level.getBombs()) {
            bomb.tick();
        }

        for (Bomb bomb : level.getDestroyedBombs()) {
            level.getBombs().remove(bomb);

            List<Blast> blast = makeBlast(bomb);
            killAllNear(blast, bomb);
            level.getBlasts().addAll(blast);
        }
        level.getDestroyedBombs().clear();
    }


    private List<Blast> makeBlast(Bomb bomb) {
        List barriers = (List) level.getWalls().subList(Wall.class);
        barriers.addAll(level.getBombermans());

        return new BoomEngineOriginal(bomb.getOwner()).boom(barriers, level.size(), bomb, bomb.getPower());   // TODO move bomb inside BoomEngine
    }

    private void killAllNear(List<Blast> blasts, Bomb bomb) {
        for (Blast blast: blasts) {
            if (level.getWalls().itsMe(blast.getX(), blast.getY())) {
                level.getDestroyedWalls().add(blast);

                Wall wall = level.getWalls().get(blast.getX(), blast.getY());
                wallDestroyed(wall, blast);
            }
        }
        for (Blast blast: blasts) {
            for (Player dead : level.getPlayers()) {
                if (dead.getBomberman().itsMe(blast)) {
                    dead.event(Events.KILL_BOMBERMAN);

                    for (Player bombOwner : level.getPlayers()) {
                        if (dead != bombOwner && blast.itsMine(bombOwner.getBomberman())) {
                            bombOwner.event(Events.KILL_OTHER_BOMBERMAN);
                        }
                    }
                }
            }
        }
    }


    public void newGame(Player player) {
        if (!level.getPlayers().contains(player)) {
            level.getPlayers().add(player);
        }
        player.newHero(this);
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Bomberman.this.level.size();

            @Override
            public int size() {
                return size;
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
}
