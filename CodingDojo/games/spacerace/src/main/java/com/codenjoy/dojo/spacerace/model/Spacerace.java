package com.codenjoy.dojo.spacerace.model;


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

import com.codenjoy.dojo.services.BoardUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.spacerace.model.flyingitems.BombController;
import com.codenjoy.dojo.spacerace.model.flyingitems.FlyingItemController;
import com.codenjoy.dojo.spacerace.model.flyingitems.GoldController;
import com.codenjoy.dojo.spacerace.model.flyingitems.StoneController;
import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.spacerace.services.GameSettings;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Spacerace implements Field {

    private final int size;
    private final GameSettings settings;
    private final List<Wall> walls;
    private final List<BulletPack> bulletPacks;
    private final List<Gold> gold;
    private final List<Bomb> bombs;
    private final List<Bullet> bullets;
    private final List<Explosion> explosions;
    private final List<Stone> stones;
    private final List<Player> players;

    private List<FlyingItemController<?>> flyingItemFactories;

    private Dice dice;
    private int currentBulletPacks = 0;

    public Spacerace(Level level, Dice dice, GameSettings settings) {
        this.dice = dice;
        walls = level.getWalls();
        gold = level.getGold();
        size = level.getSize();
        this.settings = settings;
        players = new LinkedList<>();
        bulletPacks = new LinkedList<>();
        bombs = new LinkedList<>();
        bullets = new LinkedList<>();
        stones = new LinkedList<>();
        explosions = new LinkedList<>();

        flyingItemFactories = new LinkedList<>();
        flyingItemFactories.add(new BombController(this,  bombs, explosions));
        flyingItemFactories.add(new StoneController(this, stones, explosions ));
        flyingItemFactories.add(new GoldController(this, gold, explosions));             
    }

    @Override
    public void tick() {
        explosions.clear();
        List<Integer> emptyFrontPoints = new ArrayList<>(size - 2);
        for (int i = 0; i < size - 2; i++) {
            emptyFrontPoints.add(i + 1);
        }
        for (FlyingItemController<?> factory : flyingItemFactories) {
            factory.create(emptyFrontPoints);
        }

        createBulletPack();
        tickHeroes();
        removeHeroDestroyedByBullet();
        tickBullets();

        for (FlyingItemController<?> factory : flyingItemFactories) {
            factory.tick(bullets);
        }

        removeHeroDestroyedByBullet();
        removeBulletOutOfBoard();

        for (FlyingItemController<?> factory : flyingItemFactories) {
            factory.removeOutOfBoard();
        }

        checkHeroesAlive();
    }

    private void checkHeroesAlive() {
        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOOSE);
            }
        }
    }

    private void heroDie(Bullet point, Player player) {
        bullets.remove(point);
        getPlayerFor(point.getOwner())
                .ifPresent(p -> p.event(Events.DESTROY_ENEMY));
        player.getHero().die();
    }

    public Optional<Player> getPlayerFor(Hero hero) {
        for (Player player : players) {
            if (player.getHero() == hero) {
                return Optional.of(player);
            }
        }
        return Optional.empty();
    }



    @SuppressWarnings("unlikely-arg-type")
    private void tickHeroes() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
            if (bulletPacks.remove(hero)) {
                currentBulletPacks--;
                createBulletPack();
                player.recharge();
            }
        }
    }

    private void removeBulletOutOfBoard() {
        for (Iterator<Bullet> bullet = bullets.iterator(); bullet.hasNext();) {
            if (bullet.next().isOutOf(size)) {
                bullet.remove();
            }
        }
    }

    private void tickBullets() {
        for (Bullet bullet : bullets) {
            bullet.tick();
        }
    }

    private void createBulletPack() {// TODO Паки создаюься по одному за тик, можно в цикле создать все сразу
        if (currentBulletPacks < maxCountBulletPacks()) {
            int x = dice.next(size - 2);
            int y = dice.next(size / 3) + size * 2 / 3;
            if (x != -1 && y != -1) {
                addBulletPack(x + 1, y);
                currentBulletPacks++;
            }
        }
    }

    private int maxCountBulletPacks() {
        return (players.size() - 1) / 3 + 1;
    }

    private void addBulletPack(int x, int y) {
        bulletPacks.add(new BulletPack(x, y));
    }

    private void removeHeroDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            for (Player player : players) {
                Hero hero = player.getHero();

                if (hero.equals(bullet) && bullet.getOwner() != hero) {
                    heroDie(bullet, player);
                }
            }
        }
    }

    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        return BoardUtils.getFreeRandom(
                () -> dice.next(size),
                () -> dice.next(4),
                pt -> isFree(pt));
    }

    @Override
    public boolean isFree(Point pt) {
        // TODO test me
        return !walls.contains(pt) &&
                !bullets.contains(pt) &&
                !stones.contains(pt) &&
                !explosions.contains(pt) &&
                !BombController.getBombWaves(bombs).contains(pt) &&
                !gold.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public void addBullet(int x, int y, Hero hero) {
        bullets.add(new Bullet(x, y, hero));
    }

    @Override
    public BulletCharger getCharger() {
        return new BulletCharger(settings);
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new ArrayList<Hero>(players.size());
        for (Player player : players) {
            result.add(player.getHero());
        }
        return result;
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

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Spacerace.this.size;

            @Override
            public int size() {
                return size;
            }

            @SuppressWarnings("serial")
            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<>() {
                    {
                        addAll(explosions);
                        addAll(walls);
                        addAll(getHeroes());
                        addAll(getGold());
                        addAll(bombs);
                        addAll(stones);
                        addAll(bullets);
                        addAll(bulletPacks);
                    }
                };
            }
        };
    }

    public int dice(int n) {
        return dice.next(n);
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public List<Gold> getGold() {
        return gold;
    }

    public List<Player> getPlayers() {
        return players;
    }


}
