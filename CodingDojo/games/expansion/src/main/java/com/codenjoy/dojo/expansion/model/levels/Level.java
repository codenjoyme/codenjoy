package com.codenjoy.dojo.expansion.model.levels;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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


import com.codenjoy.dojo.client.ElementsMap;
import com.codenjoy.dojo.expansion.model.IField;
import com.codenjoy.dojo.expansion.model.levels.items.*;
import com.codenjoy.dojo.games.expansion.Element;
import com.codenjoy.dojo.games.expansion.ElementUtils;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.field.AbstractLevel;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static com.codenjoy.dojo.client.AbstractLayeredBoard.Layers.LAYER1;
import static org.fest.reflect.core.Reflection.constructor;

public class Level extends AbstractLevel {
    
    private static final ElementsMap<Element> elements = new ElementsMap<>(Element.values());
    public static final int ONE_CHAR = 1;

    private Cell[] cells;
    private int viewSize;
    private String name;

    public Level(String name, String map, int viewSize) {
        super(map);

        this.name = name;
        cells = new Cell[map.length()];
        this.viewSize = viewSize;
        if (size() * size() != map.length()) {
            throw new IllegalArgumentException("map must be square! " + size() + "^2 != " + map.length());
        }

        fillMap(map);
    }

    private void fillMap(String map) {
        fill(map, 1, (cell, ch) -> {
            Element element = elements.get(ch.charAt(0));
            BaseItem item = baseItem(element);

            if (ElementUtils.layer(element) != LAYER1) {
                Element atBottom = elements.get(Element.FLOOR.ch());
                cell.add(baseItem(atBottom));
            }
            cell.add(item);
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
            int index = ElementUtils.index(elements.get(ch.charAt(0)));
            if (index == -1) {
                return;
            }
            Hero hero = heroes.get(index);
            HeroForces oldItem = cell.item(HeroForces.class);
            if (oldItem != null) {
                oldItem.leaveCell();
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
            cell.item(ChHeroForces.class).setCount(count);
        });
    }

    private void fill(String map, int len, BiConsumer<Cell, String> function) {
        int indexChar = 0;
        for (int y = size() - 1; y > -1; --y) {
            for (int x = 0; x < size(); ++x) {
                int length = this.map.xy().length(x, y);
                Cell cell = cells[length];
                if (cell == null) {
                    cell = new CellImpl(x, y);
                }
                String ch = map.substring(indexChar*len, (indexChar + 1)*len);
                function.accept(cell, ch);
                cells[length] = cell;
                ++indexChar;
            }
        }
    }

    private BaseItem baseItem(Element element) {
        return constructor().withParameterTypes(Element.class)
                            .in(ElementMapper.getItsClass(element))
                            .newInstance(element);
    }

    public int viewSize() {
        return viewSize;
    }

    public Cell cell(int x, int y) {
        return cells[map.xy().length(x, y)];
    }

    public Cell cell(Point point) {
        return cell(point.getX(), point.getY());
    }

    public Cell[] cells() {
        return cells.clone();
    }

    public boolean isBarrier(int x, int y) {
        boolean isAbroad = x > size() - 1 || x < 0 || y < 0 || y > size() - 1;

        return isAbroad || !cell(x, y).passable();
    }

    public <T> List<T> items(Class<T> clazz) {
        List<T> result = new LinkedList<>();
        for (Cell cell : cells) {
            for (Item item : cell.items()) {
                if (clazz.isInstance(item)) {
                    result.add((T) item);
                }
            }
        }
        return result;
    }

    public List<Cell> cellsWith(Class with) {
        List<Cell> result = new LinkedList<>();
        for (Cell cell : cells) {
            for (Item item : cell.items()) {
                if (with.isInstance(item)) {
                    result.add(cell);
                    break;
                }
            }
        }
        return result;
    }

    public List<Cell> cellsWith(Predicate<Cell> is) {
        List<Cell> result = new LinkedList<>();
        for (Cell cell : cells) {
            if (is.test(cell)) {
                result.add(cell);
            }
        }
        return result;
    }

    public void field(IField field) {
        List<FieldItem> items = items(FieldItem.class);

        for (int i = 0; i < items.size(); ++i) {
            items.get(i).setField(field);
        }
    }

    public String name() {
        return name;
    }
}
