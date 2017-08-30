package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.interfaces.ICell;
import com.epam.dojo.expansion.model.interfaces.IField;
import com.epam.dojo.expansion.model.interfaces.IItem;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.*;
import com.epam.dojo.expansion.services.Events;

import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {Expansion#tick()}
 */
public class Expansion implements Tickable, IField {

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;

    private Dice dice;
    private List<ILevel> levels;
    private ILevel level;

    private boolean isMultiple;

    private int ticks;
    private List<Player> players;

    public Expansion(List<ILevel> levels, Dice dice, boolean multiple) {
        this.dice = dice;
        this.levels = new LinkedList(levels);

        isMultiple = multiple;
        ticks = 0;

        players = new LinkedList();
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
            player.tick();
        }

        for (IItem item : level.getItems(Tickable.class)) {
            if (item instanceof  Hero) {
                continue;
            }

            ((Tickable) item).tick();
        }

        for (Player player : players) {
            Hero hero = player.getHero();

            if (hero.isWin()) {
                player.event(Events.WIN(hero.getGoldCount(), isMultiple));
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
        List<IItem> items = level.getItems(Start.class);
        int index = dice.next(items.size());
        return items.get(index).getCell();
    }

    @Override
    public ICell getEndPosition() {
        return level.getItems(Exit.class).get(0).getCell();
    }

    @Override
    public HeroForces tryIncreaseForces(Hero hero, int x, int y, int count) {
        if (count == 0) return HeroForces.EMPTY;

        ICell cell = level.getCell(x, y);

        List<HeroForces> forces = cell.getItems(HeroForces.class);
        if (forces.isEmpty()) {
            HeroForces income = new HeroForces(hero);
            cell.addItem(income);
            cell.comeIn(income);
            income.tryIncrease(count);
            return income;
        } else if (forces.size() == 1) {
            HeroForces heroForces = forces.get(0);
            if (heroForces.itsMe(hero)) {
                heroForces.tryIncrease(count);
                return heroForces;
            } else {
                HeroForces income = new HeroForces(hero, count);
                return attack(heroForces, income);
            }
        } else {
            throw new IllegalStateException("There are more than 1 heroes on cell!");
        }
    }

    private HeroForces attack(HeroForces defensive, IItem attack) {
        // TODO implement me
        return null;
    }

    @Override
    public int decreaseForces(Hero hero, int x, int y, int count) {
        ICell cell = level.getCell(x, y);
        if (cell.getItems(HeroForces.class).size() > 1) {
            throw new IllegalStateException("There are more than 1 heroes on cell!");
        }
        HeroForces forces = cell.getItem(HeroForces.class);
        if (forces != null && forces.itsMe(hero)) {
            return forces.decrease(count);
        } else {
            return 0;
        }
    }

    @Override
    public int countForces(Hero hero, int x, int y) {
        ICell cell = level.getCell(x, y);

        List<HeroForces> forces = cell.getItems(HeroForces.class);
        if (forces.isEmpty()) {
            return 0;
        } else if (forces.size() == 1) {
            HeroForces heroForces = forces.get(0);
            if (heroForces.itsMe(hero)) {
                return heroForces.getCount();
            } else {
                return 0;
            }
        } else {
            throw new IllegalStateException("There are more than 1 heroes on cell!");
        }
    }

    @Override
    public ICell getCell(int x, int y) {
        return level.getCell(x, y);
    }

    @Override
    public IItem getIfPresent(Class<? extends BaseItem> clazz, int x, int y) {
        for (IItem item : getCell(x, y).getItems()) {
            if (item.getClass().equals(clazz)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public boolean isAt(int x, int y, Class<? extends BaseItem>... classes) {
        for (Class clazz : classes) {
            if (getIfPresent(clazz, x, y) != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void reset() {
        // TODO think about it
        List<BaseItem> golds = level.getItems(Gold.class);

        if (isMultiple) {
            setRandomGold(golds); // TODO test me
        }

        for (BaseItem gold : golds) {
            ((Gold) gold).reset();
        }

        List<HeroForces> forces = level.getItems(HeroForces.class);
        for (HeroForces force : forces) {
            force.removeFromCell();
        }
    }

    private void setRandomGold(List<BaseItem> golds) {
        List<BaseItem> floors = level.getItems(Floor.class);

        for (int i = floors.size() - 1; i > -1; --i) {
            if (floors.get(i).getCell().getItems().size() > 1) {
                floors.remove(i);
            }
        }

        Gold gold;
        for (BaseItem item : golds) {
            gold = (Gold) item;

            if (gold.getHidden() && !floors.isEmpty()) {
                int random = dice.next(floors.size());

                Floor floor = (Floor) floors.get(random);
                floors.remove(random);

                ICell fromCell = gold.getCell();
                floor.getCell().addItem(gold);
                fromCell.addItem(floor);
            }
        }
    }

    public List<Hero> getHeroes() {
        List<Hero> result = new LinkedList();
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
        Hero hero = player.getHero();
        if (hero != null) {
            removeFromCell(hero);
        }
    }

    protected void removeFromCell(Hero hero) {
        for (ICell cell : level.getCells()) {
            for (HeroForces forces : cell.getItems(HeroForces.class)) {
                if (forces.itsMe(hero)) {
                    cell.removeItem(forces);
                }
            }
        }
    }

    public ILevel getCurrentLevel() {
        return level;
    }

    public List<Player> getPlayers() {
        return new LinkedList(players);
    }

    public List<ILevel> getLevels() {
        return new LinkedList<>(levels);
    }

    public boolean isMultiple() {
        return isMultiple;
    }

    public void setLevel(ILevel level) {
        this.level = level;
    }
}
