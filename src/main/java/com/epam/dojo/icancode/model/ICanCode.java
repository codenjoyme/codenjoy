package com.epam.dojo.icancode.model;

import com.epam.dojo.icancode.model.items.*;
import com.epam.dojo.icancode.services.Events;
import com.codenjoy.dojo.services.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {ICanCode#tick()}
 */
public class ICanCode implements Tickable, Field {

    private List<Level> levels;
    private Level level;
    private Dice dice;

    private List<Player> players;
    private boolean nextLevel;

    public ICanCode(List<Level> levels, Dice dice) {
        this.dice = dice;
        this.levels = levels;
        getNextLevel();

        players = new LinkedList<Player>();
    }

    private void getNextLevel() {
        level = levels.remove(0);
        level.init(this);
        nextLevel = false;
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        for (Player player : players) {
            checkLevel(player);
        }

        for (Player player : players) {
            Hero hero = player.getHero();
            hero.tick();
        }

        // TODO как-то очень сложно вытаскивать тут все элементы поля, которые хочется про'tick'ать
        for (ICell cell : level.getCells(ItemLogicType.LASER)) {
            Laser laser = (Laser) cell.getItem(ItemLogicType.LASER);
            laser.tick();
        }

        for (ICell cell : level.getCells(ItemLogicType.LASER_MACHINE)) {
            LaserMachine laserMachine = (LaserMachine) cell.getItem(ItemLogicType.LASER_MACHINE);
            laserMachine.tick();
        }

        for (Player player : players) {
            Hero hero = player.getHero();

            if (hero.isWin()) {
                player.event(Events.WIN(hero.getGoldCount()));
                nextLevel = true;
            }

            if (!hero.isAlive()) {
                player.event(Events.LOOSE());
            }
        }
    }

    public int size() {
        return level.getSize();
    }

    @Override
    public boolean isBarrier(int x, int y) {
        return level.isBarrier(x, y);
    }

    @Override
    public ICell getStartPosition() {
        //TODO added check of existed barrier

        return level.getCells(ItemLogicType.START).get(0);
    }

    @Override
    public void move(BaseItem item, int x, int y) {
        ICell cell = level.getCell(x, y);
        cell.addItem(item);
        cell.comeIn(item);
    }

    @Override
    public ICell getCell(int x, int y) {
        return level.getCell(x, y);
    }

    @Override
    public void reset() {
        // TODO think about it
        List<ICell> cells = level.getCells(ItemLogicType.GOLD);
        for (ICell cell : cells) {
            ((Gold) cell.getItem(0)).reset();
        }
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

    private void checkLevel(Player player) {
        if (!player.getHero().isAlive()) {
            player.newHero(this);
        }
        if (nextLevel) {
            if (!levels.isEmpty()) { // TODO what of last level?
                getNextLevel();
            }
            player.newHero(this);
            nextLevel = false;
        }
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public BoardReader readLevel() {
        return new BoardReader() {
            private int size = level.getSize();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return level.getElements(0);
            }
        };
    }

    public BoardReader readElements() {
        return new BoardReader() {
            private int size = level.getSize();

            @Override
            public int size() {
                return size;
            }

            @Override
            public Iterable<? extends Point> elements() {
                return level.getElements(1);
            }
        };
    }
}
