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


import com.codenjoy.dojo.games.icancode.Element;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class BaseItem implements Item {

    public static final boolean PASSABLE = true;
    public static final boolean IMPASSABLE = !PASSABLE;

    private Cell cell;
    private boolean passable;
    private Element element;

    public BaseItem(Element element) {
        this.element = element;
        this.passable = PASSABLE;
    }

    public BaseItem(Element element, boolean passable) {
        this.element = element;
        this.passable = passable;
    }

    @Override
    public void action(Item item) {
        // do nothing
    }

    @Override
    public Cell getCell() {
        return cell;
    }

    @Override
    public List<Item> getItemsInSameCell(int layer) {
        if (cell == null) {
            return Arrays.asList();
        }
        List<Item> items = cell.items(layer);
        items.remove(this);
        return items;
    }

    protected <T> Optional<T> check(Object item, Class<T> clazz) {
        if (item == null) {
            return Optional.empty();
        }
        if (item.getClass().equals(clazz)) {
            return Optional.of((T) item);
        }
        return Optional.empty();
    }

    public Element getState() {
        return element;
    }

    @Override
    public void setCell(Cell value) {
        cell = value;
    }

    @Override
    public void removeFromCell() {
        if (getCell() != null) {
            getCell().remove(this);
            setCell(null);
        }
    }

    // не переопределять этот метод, тут все чик-чик!
    @Override
    public boolean equals(Object o) {
        return this == o;
    }

    @Override
    public Element state(Player player, Object... alsoAtPoint) {
        return element;
    }

    @Override
    public String toString() {
        return String.format("'%s'", element);
    }

    @Override
    public boolean passable() {
        return passable;
    }

    @Override
    public int hashCode() {
        return element.hashCode();
    }

    @Override
    public int layer() {
        return element.getLayer();
    }
}
