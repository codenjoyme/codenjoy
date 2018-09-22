package com.epam.dojo.icancode.model;

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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;
import com.epam.dojo.icancode.model.interfaces.IField;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.model.items.*;
import com.epam.dojo.icancode.services.Events;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

public class ICanCode implements Tickable, IField {

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;

    private Dice dice;
    private ILevel level;

    private List<Player> players;
    private boolean isMultiplayer;

    public ICanCode(ILevel level, Dice dice, boolean isMultiplayer) {
        this.level = level;
        level.setField(this);
        this.dice = dice;
        this.isMultiplayer = isMultiplayer;
        players = new LinkedList();
    }

    @Override
    public void fire(Direction direction, Point from) {
        Point to = direction.change(from);
        move(newLaser(direction), to.getX(), to.getY());
    }

    private Laser newLaser(Direction direction) {
        Laser laser = new Laser(direction);
        laser.setField(this);
        return laser;
    }

    int priority(Object o) {
        if (o instanceof HeroItem) return 12;
        if (o instanceof ZombiePot) return 10;
        if (o instanceof Zombie) return 8;
        if (o instanceof LaserMachine) return 6;
        if (o instanceof Box) return 5;
        if (o instanceof Laser) return 4;
        return 2;
    }

    @Override
    public void tick() {
        level.getItems(HeroItem.class).stream()
                .map(it -> (HeroItem)it)
                .forEach(HeroItem::tick);

        level.getItems(Tickable.class).stream()
                .filter(it -> !(it instanceof HeroItem))
                .sorted((o1, o2) -> Integer.compare(priority(o2), priority(o1)))
                .map(it -> (Tickable)it)
                .forEach(Tickable::tick);

        for (Player player : players) {
            Hero hero = player.getHero();

            if (!hero.isAlive()) {
                player.event(Events.LOOSE());
            } else if (hero.isWin()) {
                player.event(Events.WIN(hero.getGoldCount(), isMultiplayer));
                hero.die();
            }
        }
    }

    @Override
    public int size() {
        return level.getSize();
    }

    @Override
    public List<Zombie> zombies() {
        return level.getItems(Zombie.class);
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
    public void move(IItem item, int x, int y) {
        ICell cell = level.getCell(x, y);
        cell.addItem(item);
        cell.comeIn(item);
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

        if (isMultiplayer) {
            setRandomGold(golds); // TODO test me
        }

        for (BaseItem gold : golds) {
            ((Gold) gold).reset();
        }
    }

    @Override
    public boolean isMultiplayer() {
        return isMultiplayer;
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
            hero.removeFromCell();
        }
    }


    @Override
    public BoardReader reader() {
        return new BoardReader() {
            @Override
            public int size() {
                return ICanCode.this.size();
            }

            @Override
            public Iterable<? extends Point> elements() {
                return null; // because layeredReader() implemented here
            }
        };
    }

    @Override
    public LayeredBoardReader layeredReader() {
        return new LayeredBoardReader() {
            @Override
            public int size() {
                return ICanCode.this.size();
            }

            @Override
            public BiFunction<Integer, Integer, State> elements() {
                ICell[] cells = ICanCode.this.level.getCells();
                return (index, layer) -> cells[index].getItem(layer);
            }

            @Override
            public Point viewCenter(Object player) {
                return ((Player)player).getHero().getPosition();
            }

            @Override
            public Object[] itemsInSameCell(State item) {
                return ((IItem) item).getItemsInSameCell().toArray();
            }
        };
    }

    @Override
    public Dice dice() {
        return dice;
    }

    @Override
    public ILevel getLevel() {
        return level;
    }
}
