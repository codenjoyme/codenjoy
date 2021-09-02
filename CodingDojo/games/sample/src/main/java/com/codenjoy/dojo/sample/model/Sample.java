package com.codenjoy.dojo.sample.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.sample.model.items.Bomb;
import com.codenjoy.dojo.sample.model.items.Gold;
import com.codenjoy.dojo.sample.model.items.Wall;
import com.codenjoy.dojo.sample.model.level.Level;
import com.codenjoy.dojo.sample.model.level.LevelImpl;
import com.codenjoy.dojo.sample.services.Events;
import com.codenjoy.dojo.sample.services.GameSettings;
import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.function.Predicate.*;
import static java.util.stream.Collectors.toList;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Sample implements Field {

    private List<Wall> walls;
    private List<Gold> gold;
    private List<Bomb> bombs;

    private List<Player> players;

    private int size;
    private Dice dice;

    private GameSettings settings;

    public Sample(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        players = new LinkedList<>();
        this.settings = settings;
        init(level);
    }

    private void init(Level level) {
        walls = level.walls();
        gold = level.gold();
        bombs = level.bombs();
        size = level.size();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();

            if (gold.contains(hero)) {
                gold.remove(hero);
                player.event(Events.WIN);

                Optional<Point> pos = freeRandom(null);
                if (pos.isPresent()) {
                    gold.add(new Gold(pos.get()));
                }
            }
        }

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOSE);
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(Point pt) {
        int x = pt.getX();
        int y = pt.getY();

        return x > size - 1
                || x < 0
                || y < 0
                || y > size - 1
                || walls.contains(pt)
                || getHeroes().contains(pt);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size, dice, pt -> isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        return !(gold.contains(pt)
                || bombs.contains(pt)
                || walls.contains(pt)
                || getHeroes().contains(pt));
    }

    @Override
    public boolean isBomb(Point pt) {
        return bombs.contains(pt);
    }

    @Override
    public void setBomb(Point pt) {
        if (!bombs.contains(pt)) {
            bombs.add(new Bomb(pt));
        }
    }

    @Override
    public void removeBomb(Point pt) {
        bombs.remove(pt);
    }

    public List<Gold> getGold() {
        return gold;
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .filter(not(Objects::isNull))
                .collect(toList());
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader<Player>() {
            private int size = Sample.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements(Player player) {
                return new LinkedList<Point>(){{
                    addAll(Sample.this.getWalls());
                    addAll(Sample.this.getHeroes());
                    addAll(Sample.this.getGold());
                    addAll(Sample.this.getBombs());
                }};
            }
        };
    }

    @Override
    public List<Player> load(String board, Supplier<Player> createPlayer) {
        Level level = new LevelImpl(board);
        List<Player> players = new LinkedList<>();
        level.heroes().forEach(hero -> {
            Player player = createPlayer.get();
            player.setHero(hero);
            players.add(player);

        });
        init(level);
        return players;
    }
}
