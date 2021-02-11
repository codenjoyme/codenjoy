package com.codenjoy.dojo.expansion.model.levels;

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


import com.codenjoy.dojo.expansion.model.Elements;
import com.codenjoy.dojo.expansion.model.IField;
import com.codenjoy.dojo.expansion.model.Player;
import com.codenjoy.dojo.expansion.model.levels.items.*;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.PlayerHero;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static org.fest.reflect.core.Reflection.constructor;

public class LevelImpl implements Level {
    public static final int ONE_CHAR = 1;
    private Cell[] cells;
    private int size;
    private int viewSize;
    private LengthToXY xy;
    private String name;

    public LevelImpl(String name, String map, int viewSize) {
        this.name = name;
        cells = new Cell[map.length()];
        size = (int) Math.sqrt(map.length());
        this.viewSize = viewSize;
        xy = new LengthToXY(size);
        if (size*size != map.length()) {
            throw new IllegalArgumentException("map must be square! " + size + "^2 != " + map.length());
        }

        fillMap(map);
    }

    private void fillMap(String map) {
        fill(map, 1, (cell, ch) -> {
            Elements element = Elements.valueOf(ch.charAt(0));
            BaseItem item = getBaseItem(element);

            if (element.getLayer() != Elements.Layers.LAYER1) {
                Elements atBottom = Elements.valueOf(Elements.FLOOR.ch());
                cell.addItem(getBaseItem(atBottom));
            }
            cell.addItem(item);
        });
    }



    public class ChHeroForces extends HeroForces {
        public ChHeroForces(Hero hero) {
            super(hero, 0);
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

    public void fillForces(String forcesMap, List<Hero> heroes) {
        fill(forcesMap, ONE_CHAR, (cell, ch) -> {
            int index = Elements.valueOf(ch.charAt(0)).getIndex();
            if (index == -1) {
                return;
            }
            Hero hero = heroes.get(index);
            HeroForces oldItem = cell.getItem(HeroForces.class);
            if (oldItem != null) {
                oldItem.removeFromCell();
            }
            cell.captureBy(new ChHeroForces(hero));
        });
    }

    public void fillForcesCount(String forcesCountMap) {
        fill(forcesCountMap, ForcesState.COUNT_NUMBERS, (cell, countString) -> {
            int count = ForcesState.parseCount(countString);
            if (count == 0) {
                return;
            }
            cell.getItem(ChHeroForces.class).setCount(count);
        });
    }

    private void fill(String map, int len, BiConsumer<Cell, String> function) {
        int indexChar = 0;
        for (int y = size - 1; y > -1; --y) {
            for (int x = 0; x < size; ++x) {
                int length = xy.getLength(x, y);
                Cell cell = cells[length];
                if (cell == null) {
                    cell = new CellImpl(x, y);
                }
                String ch = map.substring(indexChar*len, (indexChar + 1)*len);
                function.accept(cell, String.valueOf(ch));
                cells[length] = cell;
                ++indexChar;
            }
        }
    }

    private BaseItem getBaseItem(Elements element) {
        return constructor()
                            .withParameterTypes(Elements.class)
                            .in(ElementsMapper.getItsClass(element))
                            .newInstance(element);
    }

    @Override
    public int getSize() {
        return size;
    }

    @Override
    public int getViewSize() {
        return viewSize;
    }

    @Override
    public Cell getCell(int x, int y) {
        return cells[xy.getLength(x, y)];
    }

    @Override
    public Cell getCell(Point point) {
        return getCell(point.getX(), point.getY());
    }

    @Override
    public Cell[] getCells() {
        return cells.clone();
    }

    @Override
    public boolean isBarrier(int x, int y) {
        boolean isAbroad = x > size - 1 || x < 0 || y < 0 || y > size - 1;

        return isAbroad || !getCell(x, y).isPassable();
    }

    @Override
    public <T> List<T> getItems(Class<T> clazz) {
        List<T> result = new LinkedList<T>();
        for (Cell cell : cells) {
            for (Item item : cell.getItems()) {
                if (clazz.isInstance(item)) {
                    result.add((T)item);
                }
            }
        }
        return result;
    }

    @Override
    public List<Cell> getCellsWith(Class with) {
        List<Cell> result = new LinkedList<Cell>();
        for (Cell cell : cells) {
            for (Item item : cell.getItems()) {
                if (with.isInstance(item)) {
                    result.add(cell);
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<Cell> getCellsWith(Predicate<Cell> is) {
        List<Cell> result = new LinkedList<Cell>();
        for (Cell cell : cells) {
            if (is.test(cell)) {
                result.add(cell);
            }
        }
        return result;
    }

    @Override
    public void setField(IField field) {
        List<FieldItem> items = getItems(FieldItem.class);

        for (int i = 0; i < items.size(); ++i) {
            items.get(i).setField(field);
        }
    }

    @Override
    public String getName() {
        return name;
    }
}
