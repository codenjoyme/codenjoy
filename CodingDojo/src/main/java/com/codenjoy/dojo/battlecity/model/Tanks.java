package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.Tickable;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Tanks implements Tickable, ITanks {

    private final LinkedList<Tank> aiTanks;
    private ReadWriteLock lock = new ReentrantReadWriteLock(true);

    private int size;
    private List<Construction> constructions;
    private List<Player> players = new LinkedList<Player>();
    private int timer;

    public Tanks(int size, List<Construction> constructions, Tank... tanks) {
        timer = 0;
        this.size = size;
        this.aiTanks = new LinkedList<Tank>();
        this.constructions = new LinkedList<Construction>();
        add(constructions);

        for (Tank tank : tanks) {
            addAI(tank);
        }
    }

    @Override
    public void tick() {
        lock.writeLock().lock();
        try {
            if (collectTicks()) return;

            for (Tank tank : getTanks()) {
                if (!tank.isAlive()) {
                    aiTanks.remove(tank);
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
        } finally {
            lock.writeLock().unlock();
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
       return getTanks().iterator().next();
    }

    void add(List<Construction> constructions) {
        this.constructions.addAll(constructions);
        for (Construction construction : constructions) {
            construction.setOnDestroy(new OnDestroy() {
                @Override
                public void destroy(Object construction) {
                    Tanks.this.constructions.remove(construction);
                }
            });
        }
    }

    void addAI(Tank tank) {
        if (tank != null) {
            tank.setField(this);
        }
        aiTanks.add(tank);
    }

    void affect(Bullet bullet) {
        if (constructions.contains(bullet)) {
            int index = constructions.indexOf(bullet);
            constructions.get(index).destroyFrom(bullet.getDirection());
            bullet.onDestroy();
        } else if (getTanks().contains(bullet)) {  // TODO тут было aiTanks проверить тестами как убиваются другие танки
            int index = getTanks().indexOf(bullet);
            getTanks().get(index).kill(bullet);
            bullet.onDestroy();
        } else {
            for (Bullet bullet2 : getBullets().toArray(new Bullet[0])) {
                if (bullet != bullet2 && bullet.equals(bullet2)) {
                    bullet.onDestroy();
                    bullet2.onDestroy();
                }
            }
        }
    }

    boolean isBarrier(int x, int y) {
        for (Construction construction : constructions) {
            if (construction.equals(new PointImpl(x, y))) {
                return true;
            }
        }
        for (Tank tank : getTanks()) {   //  TODO проверить как один танк не может проходить мимо другого танка игрока (не AI)
            if (tank.equals(new PointImpl(x, y))) {
                return true;
            }
        }
        return isBorder(x, y);
    }

    boolean isBorder(int x, int y) {
        return x <= 0 || y <= 0 || y >= size - 1 || x >= size - 1;
    }

    private Collection<Bullet> getBullets() {
        List<Bullet> result = new LinkedList<Bullet>();
        for (Tank tank : getTanks()) { // TODO тут было aiTanks проверить тестами как муваются снаряды других танков
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
            result.add(player.getTank());
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
    public void add(Player player) {  // TODO test me
        lock.writeLock().lock();
        try {
            if (!players.contains(player)) {
                players.add(player);
            }
            player.getTank().setField(this);
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public int getSize() {
        return size;
    }

    List<Construction> getConstructions() {
        return constructions;
    }
}
