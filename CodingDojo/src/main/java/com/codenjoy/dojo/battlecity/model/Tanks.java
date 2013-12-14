package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.battlecity.model.levels.DefaultBorders;
import com.codenjoy.dojo.battlecity.services.BattlecityEvents;
import com.codenjoy.dojo.services.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Tanks implements Tickable, ITanks, Field {

    private final Dice dice;
    private LinkedList<Tank> aiTanks;
    private int aiCount;

    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    private int size;
    private List<Construction> constructions;
    private List<Point> borders;

    private List<Player> players = new LinkedList<Player>();
    private int timer;

    public Tanks(int size, List<Construction> constructions, Tank... aiTanks) {
        this(size, constructions, new DefaultBorders(size).get(), aiTanks);
    }

    public Tanks(int size, List<Construction> constructions,
                 List<Point> borders, Tank... aiTanks) {
        timer = 0;
        dice = new RandomDice();
        aiCount = aiTanks.length;
        this.size = size;
        this.aiTanks = new LinkedList<Tank>();
        this.constructions = new LinkedList<Construction>(constructions);
        this.borders = new LinkedList<Point>(borders);

        for (Tank tank : aiTanks) {
            addAI(tank);
        }
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            if (collectTicks()) return;

            removeDeadTanks();

            newAI();

            for (Tank tank : getTanks()) {
                tank.tick();
            }

            for (Bullet bullet : getBullets()) {
                if (bullet.destroyed()) {
                    bullet.onDestroy();
                }
            }

            for (Bullet bullet : getBullets()) {
                bullet.move();
            }
            for (Tank tank : getTanks()) {
                if (tank.isAlive()) {
                    tank.move();
                }
            }
            for (Construction construction : constructions) {
                if (!getTanks().contains(construction) && !getBullets().contains(construction)) {
                    construction.tick();
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void newAI() {
        for (int count = aiTanks.size(); count < aiCount; count++) {
            int y = size - 2;
            int x = 0;
            int c = 0;
            do {
                x = dice.next(size);
                c++;
            } while (isBarrier(x, y) & c < size);

            if (!isBarrier(x, y)) {
                addAI(new AITank(x, y, Direction.DOWN));
            }
        }
    }

    private void removeDeadTanks() {
        for (Tank tank : getTanks()) {
            if (!tank.isAlive()) {
                aiTanks.remove(tank);
            }
        }
        for (Player player : players.toArray(new Player[0])) {
            if (!player.getTank().isAlive()) {
                players.remove(player);
            }
        }
    }

    private boolean collectTicks() {
        timer++;
        if (timer >= players.size()) {
            timer = 0;
        } else {
            return true;
        }
        return false;
    }

    @Override
    public Joystick getJoystick() {
       return players.get(0).getTank();
    }

    void addAI(Tank tank) {
        if (tank != null) {
            tank.setField(this);
        }
        aiTanks.add(tank);
    }

    void affect(Bullet bullet) {
        if (borders.contains(bullet)) {
            bullet.onDestroy();
        } else if (constructions.contains(bullet)) {
            int index = constructions.indexOf(bullet);
            Construction construction = constructions.get(index);

            if (!construction.destroyed()) {
                construction.destroyFrom(bullet.getDirection());
                bullet.onDestroy();  // TODO заимплементить взрыв
            }
        } else if (getTanks().contains(bullet)) {
            int index = getTanks().indexOf(bullet);
            Tank tank = getTanks().get(index);

            scoresForKill(bullet, tank);

            tank.kill(bullet);
            bullet.onDestroy();  // TODO заимплементить взрыв
        } else {
            for (Bullet bullet2 : getBullets().toArray(new Bullet[0])) {
                if (bullet != bullet2 && bullet.equals(bullet2)) {
                    bullet.boom();
                    bullet2.boom();
                }
            }
        }
    }

    private void scoresForKill(Bullet killedBullet, Tank diedTank) {
        Player died = null;
        if (!aiTanks.contains(diedTank)) {
             died = getPlayer(diedTank);
        }

        Tank killerTank = killedBullet.getOwner();
        Player killer = null;
        if (!aiTanks.contains(killerTank)) {
            killer = getPlayer(killerTank);
        }

        if (killer != null) {
            killer.event(BattlecityEvents.KILL_OTHER_TANK);
        }
        if (died != null) {
            died.event(BattlecityEvents.KILL_YOUR_TANK);
        }
    }

    private Player getPlayer(Tank tank) {
        for (Player player : players) {
            if (player.getTank().equals(tank)) {
                return player;
            }
        }

        throw new RuntimeException("Танк игрока не найден!");
    }

    boolean isBarrier(int x, int y) {
        for (Construction construction : constructions) {
            if (construction.itsMe(x, y) && !construction.destroyed()) {
                return true;
            }
        }
        for (Point border : borders) {
            if (border.itsMe(x, y)) {
                return true;
            }
        }
        for (Tank tank : getTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.itsMe(x, y)) {
                return true;
            }
        }
        return outOfField(x, y);
    }

    boolean outOfField(int x, int y) {
        return x < 0 || y < 0 || y > size - 1 || x > size - 1;
    }

    private Collection<Bullet> getBullets() {
        List<Bullet> result = new LinkedList<Bullet>();
        for (Tank tank : getTanks()) {
            for (Bullet bullet : tank.getBullets()) {
                result.add(bullet);
            }
        }
        return result;
    }

    @Override
    public List<Tank> getTanks() {
        LinkedList<Tank> result = new LinkedList<Tank>(aiTanks);
        for (Player player : players) {
//            if (player.getTank().isAlive()) { // TODO разремарить с тестом
                result.add(player.getTank());
//            }
        }
        return result;
    }

    @Override
    public void remove(Player player) {   // TODO test me
        lock.writeLock().lock();
        try {
            players.remove(player);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void newGame(Player player) {  // TODO test me
        lock.writeLock().lock();
        try {
            player.getTank().removeBullets();
            player.getTank().setField(this);
            if (!players.contains(player)) {
                players.add(player);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public List<Construction> getConstructions() {
        return constructions;
    }

    @Override
    public List<Point> getBorders() {
        return borders;
    }
}
