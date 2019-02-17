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
import com.codenjoy.dojo.spacerace.services.Events;

import java.util.*;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class Spacerace implements Field {

    private static final int NEW_APPEAR_PERIOD = 3;
    private static final int MAX_COUNT_BULLET_PACKS = 1;
    private final int size;
    private List<Wall> walls;
    private List<BulletPack> bulletPacks;
    private List<Gold> gold;
    private List<Bomb> bombs;
    private List<Bullet> bullets;
    private List<Explosion> explosions;
    private List<Stone> stones;
    private List<Player> players;
    private Dice dice;
    private boolean isNewStone = true;
    private int countStone = 0;
    private boolean isNewBomb = true;
    private int countBomb = 0;
    private int ticksToRecharge;
    private int bulletsCount;
    private int currentBulletPacks = 0;

    public Spacerace(Level level, Dice dice, int ticksToRecharge, int bulletsCount) {
        this.dice = dice;
        this.ticksToRecharge = ticksToRecharge;
        this.bulletsCount = bulletsCount;
        walls = level.getWalls();
        gold = level.getGold();
        size = level.getSize();
        players = new LinkedList<Player>();
        bulletPacks = new LinkedList<BulletPack>();
        bombs = new LinkedList<Bomb>();
        bullets = new LinkedList<Bullet>();
        stones = new LinkedList<Stone>();
        explosions = new LinkedList<>();
    }

    @Override
    public void tick() {
        explosions.clear();
        createStone();
        createBomb();
        createBulletPack();
        tickHeroes();
        removeHeroDestroyedByBullet();
        tickBullets();
        tickStones();
        tickBombs();
        removeHeroDestroyedByBullet();
        removeStoneOutOfBoard();
        removeBulletOutOfBoard();
        removeBombOutOfBoard();
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

    private void tickBombs() {
       removeBombDestroyedByBullet();
        for (Bomb bomb : bombs) {
            bomb.tick();
        }
        removeBombDestroyedByBullet();
        heroExploytedByBomb();
    }

    private void heroExploytedByBomb() {
        ifBombIsNear(players);
    }

    private void ifBombIsNear(List<Player> players) {
        Collection<BombWave> badPlaces = getBombWaves();

        for (Player player :  players) {
            Hero hero = player.getHero();
            for (BombWave wave : badPlaces) {
                if (hero.itsMe(wave)) {
                    heroDie(wave.getBomb(), player);
                }
            }
        }
    }

    private Collection<BombWave> getBombWaves() {
        Collection<BombWave> badPlaces = new LinkedList<>();
        for (Bomb bomb : new ArrayList<>(bombs)) {  // TODO to use iterator.remove
            for (int x = bomb.getX() - 1; x < bomb.getX() + 2; x++) {
                for (int y = bomb.getY() - 1; y < bomb.getY() + 2; y++) {
                    badPlaces.add(new BombWave(x, y, bomb));
                }
            }
        }
        return badPlaces;
    }

    private void heroDie(Point point, Player player) {
        if(point instanceof Bomb){
            bombExplosion(point);
            bombs.remove(point);
        } else if(point instanceof Stone){
            stones.remove(point);
        } else if(point instanceof Bullet) {
            bullets.remove(point);
            getPlayerFor(((Bullet)point).getOwner()).event(Events.DESTROY_ENEMY);
        }
        player.getHero().die();
    }

    private Player getPlayerFor(Hero hero) {
        for (Player player : players) {
            if (player.getHero() == hero) {
                return player;
            }
        }
        return Player.NULL;
    }

    private void bombExplosion(Point pt) {
        for(int x = pt.getX() - 1; x < pt.getX() + 2; x++){
            for(int y = pt.getY() - 1; y < pt.getY() + 2; y++){
                if (y != size) {
                    explosions.add(new Explosion(x, y));
                }
            }
        }
    }

    private void createBomb() {
        countBomb++;
        if (countBomb == NEW_APPEAR_PERIOD) {
            int count = 0;
            while (count++ < 10000) {
                int x = dice.next(size - 2);
                if (x == -1) break;
                if (stones.contains(pt(x + 1, size))) continue;

                addBomb(x + 1);
                countBomb = 0;
                break;
            }
            if (count == 10000) {
                throw new RuntimeException("Извините не нашли пустого места");
            }
        }
    }

    private void tickHeroes() {
        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
            if (bulletPacks.contains(hero)) {
                bulletPacks.remove(hero);
                currentBulletPacks--;
                createBulletPack();
                player.recharge();
            }
        }
    }

    private void removeBulletOutOfBoard() {
        for (Iterator <Bullet> bullet = bullets.iterator(); bullet.hasNext();){
            if (bullet.next().isOutOf(size)){
                bullet.remove();
            }
        }
    }

    private void tickBullets() {
        for (Bullet bullet : bullets) {
            bullet.tick();
        }
    }

    private void tickStones() {
        removeStoneDestroyedByBullet();
        for (Stone stone : stones) {
            stone.tick();
        }
        removeStoneDestroyedByBullet();
        heroKilledByStone();
    }

    private void heroKilledByStone() {
        for (Stone stone : new ArrayList<>(stones)) { // TODO to use iterator.remove
            for (Player player : players) {
                if (stone.equals(player.getHero())) {// TODO Warning:(211, 27) 'equals()' between objects of
                    heroDie(stone, player);          // inconvertible types 'Hero' and 'Stone'
                }
            }
        }
    }

    private void removeStoneOutOfBoard() {
        for (Iterator <Stone> stone = stones.iterator(); stone.hasNext();){
            if (stone.next().isOutOf(size)){
                stone.remove();
            }
        }
    }

    private void removeBombOutOfBoard() {
        for (Iterator<Bomb> bomba = bombs.iterator(); bomba.hasNext();){
            if (bomba.next().isOutOf(size)){
                bomba.remove();
            }
        }
    }

    private void createStone() {
        countStone++;
        if (countStone == NEW_APPEAR_PERIOD) {
            int x = dice.next(size - 2);
            if (x != -1) {
                addStone(x + 1);
            }
            countStone = 0;
        }
    }

    private void createBulletPack() {//TODO Паки создаюься по одному за тик, можно в цикле создать все сразу
        if(currentBulletPacks < maxCountBulletPacks()) {
            int x = dice.next(size - 2);
            int y = dice.next(size/3) + size*2/3;
            if (x != -1 && y != -1) {
                addBulletPack(x + 1, y);
                    currentBulletPacks++;
                }
            }
    }

    private int maxCountBulletPacks() {
        return (players.size() - 1)/ 3 + 1;
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

    private void removeStoneDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            if (stones.contains(bullet)) {
                explosions.add(new Explosion(bullet));
                bullets.remove(bullet);
                stones.remove(bullet);
                fireWinScoresFor(bullet, Events.DESTROY_STONE);
            }
        }
    }

    private void fireWinScoresFor(Bullet bullet, Events event) {
        Hero hero = bullet.getOwner();
        getPlayerFor(hero).event(event);
    }

    private void removeBombDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            if (bombs.contains(bullet)) {
                bombs.remove(bullet);
                bullets.remove(bullet);
                bombExplosion(bullet);
                fireWinScoresFor(bullet, Events.DESTROY_BOMB);
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
        return  !walls.contains(pt) &&
                !bullets.contains(pt) &&
                !stones.contains(pt) &&
                !explosions.contains(pt) &&
                !getBombWaves().contains(pt) &&
                !gold.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public void addBullet(int x, int y, Hero hero) {
        bullets.add(new Bullet(x, y, hero));
    }

    @Override
    public BulletCharger getCharger() {
        return new BulletCharger(ticksToRecharge, bulletsCount);
    }

    public List<Gold> getGold() {
        return gold;
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

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    @Override
    public BoardReader reader() {
        return new BoardReader() {
            private int size = Spacerace.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return new LinkedList<Point>(){{
                    addAll(explosions);
                    addAll(walls);
                    addAll(getHeroes());
                    addAll(getGold());
                    addAll(bombs);
                    addAll(stones);
                    addAll(bullets);
                    addAll(bulletPacks);
                }};
            }
        };
    }

    public void addStone(int x) {
        stones.add(new Stone(x, size));
        isNewStone = false;
    }

    public List<Stone> getStones() {
        return stones;
    }

    public void addBomb(int x) {
        bombs.add(new Bomb(x, size));
        isNewBomb = false;
    }
}
