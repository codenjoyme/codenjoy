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
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.util.stream.Collectors.toList;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Sample#tick()}
 */
public class Sample implements Field {

    private PointField field;
    private List<Player> players;
    private Dice dice;
    private GameSettings settings;

    public Sample(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        players = new LinkedList<>();
        this.settings = settings;
        init(level);
    }

    private void init(Level level) {
        field = new PointField(level.size());
        field.addAll(level.walls());
        field.addAll(level.gold());
        field.addAll(level.bombs());
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            Hero hero = player.getHero();

            hero.tick();

            if (gold().contains(hero)) {
                gold().remove(hero);
                player.event(Events.WIN);

                Optional<Point> pos = freeRandom(null);
                if (pos.isPresent()) {
                    field.add(new Gold(pos.get()));
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
        return field.size();
    }

    public static int count;

    @Override
    public boolean isBarrier(Point pt) {
        return pt.isOutOf(size())
                || walls().contains(pt)
                || heroes().contains(pt);
    }

    @Override
    public Optional<Point> freeRandom(Player player) {
        return BoardUtils.freeRandom(size(), dice, pt -> isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        return !(gold().contains(pt)
                || bombs().contains(pt)
                || walls().contains(pt)
                || heroes().contains(pt));
    }

    @Override
    public boolean isBomb(Point pt) {
        return bombs().contains(pt);
    }

    @Override
    public void setBomb(Point pt) {
        if (!bombs().contains(pt)) {
            field.add(new Bomb(pt));
        }
    }

    @Override
    public void removeBomb(Point pt) {
        bombs().remove(pt);
    }

    @Override
    public void add(Hero hero) {
        field.add(hero);
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
        removeAloneHero();

    }

    private void removeAloneHero() {
        heroes().removeNotIn(players.stream().
                map(GamePlayer::getHero)
                .collect(toList()));
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
        heroes().remove(player.getHero());
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    @Override
    public BoardReader reader() {
        return field.reader();
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

    private PointField.Accessor<Gold> gold() {
        return field.of(Gold.class);
    }

    private PointField.Accessor<Hero> heroes() {
        return field.of(Hero.class);
    }

    private PointField.Accessor<Wall> walls() {
        return field.of(Wall.class);
    }

    private PointField.Accessor<Bomb> bombs() {
        return field.of(Bomb.class);
    }

}
