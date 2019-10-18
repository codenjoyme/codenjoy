package com.codenjoy.dojo.icancode.model;

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


import com.codenjoy.dojo.icancode.model.interfaces.ICell;
import com.codenjoy.dojo.icancode.model.interfaces.IField;
import com.codenjoy.dojo.icancode.model.interfaces.IItem;
import com.codenjoy.dojo.icancode.model.interfaces.ILevel;
import com.codenjoy.dojo.icancode.model.items.*;
import com.codenjoy.dojo.icancode.services.Events;
import com.codenjoy.dojo.icancode.services.Levels;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.printer.layeredview.LayeredBoardReader;
import com.codenjoy.dojo.icancode.model.items.*;

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
    public void fire(State owner, Direction direction, Point from) {
        Point to = direction.change(from);
        move(newLaser(owner, direction), to.getX(), to.getY());
    }

    private Laser newLaser(State owner, Direction direction) {
        Laser laser = new Laser(owner, direction);
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
                .filter(it -> !(it instanceof Laser && ((Laser)it).skipFirstTick()) ) // TODO это хак, надо разобраться!
                .sorted((o1, o2) -> Integer.compare(priority(o2), priority(o1)))
                .map(it -> (Tickable)it)
                .forEach(Tickable::tick);

        // после всех перемещений, если герой в полете его надо на 3й леер, иначе приземлить
        level.getItems(HeroItem.class).stream()
                .map(it -> (HeroItem)it)
                .forEach(HeroItem::fixLayer);

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
    public List<Laser> lasers() {
        return level.getItems(Laser.class);
    }

    @Override
    public List<Gold> golds() {
        return level.getItems(Gold.class);
    }

    @Override
    public List<LaserMachine> laserMachines() {
        return level.getItems(LaserMachine.class);
    }

    @Override
    public List<ZombiePot> zombiePots() {
        return level.getItems(ZombiePot.class);
    }

    @Override
    public List<Floor> floors() {
        return level.getItems(Floor.class);
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
        List<Gold> golds = golds();

        if (isMultiplayer) {
            setRandomGold(golds); // TODO test me
        }

        golds.forEach(it -> it.reset());

        if (!isMultiplayer) {
            // TODO test me
            zombiePots().forEach(it -> it.reset());

            // TODO test me
            laserMachines().forEach(it -> it.reset());
        }
    }

    @Override
    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    private void setRandomGold(List<Gold> golds) {
        List<Floor> floors = floors();

        for (int i = floors.size() - 1; i > -1; --i) {
            if (floors.get(i).getCell().getItems().size() > 1) {
                floors.remove(i);
            }
        }

        for (Gold gold : golds) {
            if (gold.getHidden() && !floors.isEmpty()) {
                int random = dice.next(floors.size());

                Floor floor = floors.get(random);
                floors.remove(random);

                ICell cell = gold.getCell();
                floor.getCell().addItem(gold);
                cell.addItem(floor);
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
            public int viewSize() {
                return Levels.size();
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
