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


import com.codenjoy.dojo.icancode.model.items.Air;
import com.codenjoy.dojo.services.PointImpl;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER2;
import static com.codenjoy.dojo.icancode.model.Elements.Layers.LAYER3;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class CellImpl extends PointImpl implements Cell {

    private ListMultimap<Integer, Item> items = LinkedListMultimap.create();

    public CellImpl(int x, int y) {
        super(x, y);
    }

    @Override
    public void add(Item item) {
        item.removeFromCell();

        items.put(item.layer(), item);
        item.setCell(this);
    }

    @Override
    public void comeIn(Item input) {
        items().stream()
                .filter(item -> !item.equals(input))
                .forEach(item -> {
                    item.action(input);
                    input.action(item);
                });
    }

    @Override
    public boolean passable() {
        return items.values().stream()
                .allMatch(item -> item.passable());
    }

    @Override
    public <T extends Item> T item(int layer) {
        Collection<Item> list = items.get(layer);
        return (T) (list.isEmpty() ? new Air() : list.iterator().next());
    }

    @Override
    public <T extends Item> List<T> items() {
        return (List<T>)new LinkedList<>(items.values());
    }

    @Override
    public <T extends Item> List<T> items(int layer) {
        return (List<T>)new LinkedList<>(items.get(layer));
    }

    @Override
    public void remove(Item item) {
        items.values().remove(item);
    }

    @Override
    public void jump(Item item) {
        int index = items.get(LAYER2).indexOf(item);
        boolean heroOn2Layer = index != -1;
        if (!heroOn2Layer) {
            // нас не интересуют случаи, когда герой не на втором слое
            return;
        }

        Item removed = items.get(LAYER2).remove(index);
        items.get(LAYER3).add(removed);
    }

    @Override
    public void landOn(Item item) {
        int index = items.get(LAYER3).indexOf(item);
        boolean heroOn3Layer = index != -1;
        if (!heroOn3Layer) {
            // нас не интересуют случаи, когда герой не на третьем слое (не в полете)
            return;
        }

        Item removed = items.get(LAYER3).remove(index);
        items.get(LAYER2).add(removed);
    }

    //================================ Overrides ================================

    @Override
    public String toString() {
        return String.format("Cell[%s,%s]=%s", x, y, items());
    }

}
