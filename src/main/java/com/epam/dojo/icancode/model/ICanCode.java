package com.epam.dojo.icancode.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.icancode.model.interfaces.ICell;
import com.epam.dojo.icancode.model.interfaces.IField;
import com.epam.dojo.icancode.model.interfaces.IItem;
import com.epam.dojo.icancode.model.interfaces.ILevel;
import com.epam.dojo.icancode.model.items.BaseItem;
import com.epam.dojo.icancode.model.items.Exit;
import com.epam.dojo.icancode.model.items.Floor;
import com.epam.dojo.icancode.model.items.Gold;
import com.epam.dojo.icancode.model.items.Hero;
import com.epam.dojo.icancode.model.items.Start;
import com.epam.dojo.icancode.services.Events;

import java.util.LinkedList;
import java.util.List;

/**
 * О! Это самое сердце игры - борда, на которой все происходит.
 * Если какой-то из жителей борды вдруг захочет узнать что-то у нее, то лучше ему дать интефейс {@see Field}
 * Борда реализует интерфейс {@see Tickable} чтобы быть уведомленной о каждом тике игры. Обрати внимание на {ICanCode#tick()}
 */
public class ICanCode implements Tickable, IField {

    public static final boolean SINGLE = false;
    public static final boolean MULTIPLE = true;

    private Dice dice;
    private List<ILevel> levels;
    private ILevel level;

    private boolean isMultiple;

    private int ticks;
    private List<Player> players;

    public ICanCode(List<ILevel> levels, Dice dice, boolean multiple) {
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
        return level.getItems(Start.class).get(0).getCell();
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
    public void reset() {
        // TODO think about it
        List<BaseItem> golds = level.getItems(Gold.class);

        if (isMultiple) {
            setRandomGold(golds); // TODO test me
        }

        for (BaseItem gold : golds) {
            ((Gold) gold).reset();
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
