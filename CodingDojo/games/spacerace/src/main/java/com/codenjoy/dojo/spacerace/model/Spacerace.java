package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.spacerace.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Spacerace#tick()}
 */
public class Spacerace implements Tickable, Field {

    private static final int NEW_APPEAR_PERIOD = 3;
    private final int size;
    private List<Wall> walls;
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

    public Spacerace(Level level, Dice dice) {
        this.dice = dice;
        walls = level.getWalls();
        gold = level.getGold();
        size = level.getSize();
        players = new LinkedList<Player>();
        bombs = new LinkedList<Bomb>();
        bullets = new LinkedList<Bullet>();
        stones = new LinkedList<Stone>();
        explosions = new LinkedList<>();
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        explosions.clear();
        createStone();
        createBomb();
        tickHeroes();
        tickBullets();
        tickStones();
        tickBombs();
        removeStoneOutOfBoard();
        removeBulletOutOfBoard();
        removeBombOutOfBoard();
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
        for (Bomb bomb : new ArrayList<>(bombs)) {  // TODO to use iterator.remove
            for (Player player :  players) {
                for (int x = bomb.getX() - 1; x < bomb.getX() + 2; x++) {
                    for (int y = bomb.getY() - 1; y < bomb.getY() + 2; y++) {
                        if (new PointImpl(x, y).equals(player.getHero())) {
                            heroDie(bomb, player);
                        }
                    }
                }
            }
        }
    }

    private void heroDie(Point point, Player player) {
        if(point instanceof Bomb){
            bombExplosion(point);
            bombs.remove(point);
        }else if(point instanceof Stone){
            stones.remove(point);
        }
        player.event(Events.LOOSE);
    }

    private void removeBombDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            if (bombs.contains(bullet)) {
                bombs.remove(bullet);
                bullets.remove(bullet);
                bombExplosion(bullet);
                for (Player player : players) {
                    player.event(Events.WIN);
                }
            }
        }
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
            if (gold.contains(hero)) {
                gold.remove(hero);
                player.event(Events.WIN);

                Point pos = getFreeRandom();
                gold.add(new Gold(pos.getX(), pos.getY()));
            }
        }
        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOOSE);
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
                if (stone.equals(player.getHero())) {
                    heroDie(stone, player);
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
                countStone = 0;
            }
        }
    }


    private void removeStoneDestroyedByBullet() {
        for (Bullet bullet : new ArrayList<>(bullets)) { // TODO to use iterator.remove
            if (stones.contains(bullet)) {
                explosions.add(new Explosion(bullet));
                bullets.remove(bullet);
                stones.remove(bullet);
                for (Player player : players) {
                    player.event(Events.WIN);
                }
            }
        }
    }


    public int size() {
        return size;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        return x > size - 1 || x < 0 || y < 0 || y > size - 1 || walls.contains(pt) || getHeroes().contains(pt);
    }

    @Override
    public Point getFreeRandom() {
        int rndX = 0;
        int rndY = 0;
        int c = 0;
        do {
            rndX = dice.next(size);
            rndY = dice.next(size);
        } while (!isFree(rndX, rndY) && c++ < 100);

        if (c >= 100) {
            return PointImpl.pt(0, 0);
        }

        return PointImpl.pt(rndX, rndY);
    }

    @Override
    public boolean isFree(int x, int y) {
        Point pt = PointImpl.pt(x, y);

        return !gold.contains(pt) &&
                !bombs.contains(pt) &&
                !walls.contains(pt) &&
                !getHeroes().contains(pt);
    }

    @Override
    public boolean isBomb(int x, int y) {
        return bombs.contains(PointImpl.pt(x, y));
    }

    @Override
    public void setBomb(int x, int y) {
        Point pt = PointImpl.pt(x, y);
        if (!bombs.contains(pt)) {
            bombs.add(new Bomb(x, y));
        }
    }

    @Override
    public void removeBomb(int x, int y) {
        bombs.remove(PointImpl.pt(x, y));
    }

    @Override
    public void addBullet(int x, int y) {
        bullets.add(new Bullet(x, y));
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

    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        }
        player.newHero(this);
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public List<Wall> getWalls() {
        return walls;
    }

    public List<Bomb> getBombs() {
        return bombs;
    }

    public BoardReader reader() {
        return new BoardReader() {
            private int size = Spacerace.this.size;

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                List<Point> result = new LinkedList<Point>();
                result.addAll(explosions);
                result.addAll(walls);
                result.addAll(getHeroes());
                result.addAll(getGold());
                result.addAll(bombs);
                result.addAll(stones);
                result.addAll(bullets);
                return result;
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
