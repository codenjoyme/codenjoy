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

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;

    private List<ILevel> levels;
    private boolean isMultiple;
    private ILevel level;
    private Dice dice;

    private int ticks;
    private List<Player> players;
    private boolean finished;

    public ICanCode(List<ILevel> levels, Dice dice, boolean isMultiple) {
        this.dice = dice;
        this.levels = levels;
        this.isMultiple = isMultiple;
        this.ticks = 0;
        this.finished = false;
        getNextLevel();

        players = new LinkedList<Player>();
    }

    private void getNextLevel() {
        level = levels.remove(0);
        level.init(this);
    }

    /**
     * @see Tickable#tick()
     */
    @Override
    public void tick() {
        if (isMultiple) {
            ticks++;
            if (ticks % players.size() != 0) {
                return;
            }
            ticks = 0;
        }

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
                player.setNextLevel();
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
        if (player.isNextLevel()) {
            if (!levels.isEmpty()) {
                getNextLevel();
            } else {
                if (!isMultiple) {
                    finished = true;
                }
            }
            player.newHero(this);
        }
    }

    public void remove(Player player) {
        players.remove(player);
    }

    public ILevel getCurrentLevel()
    {
        return level;
    }

    public boolean finished() {
        return finished;
    }

    public List<Player> getPlayers() {
        return players;
    }
}
