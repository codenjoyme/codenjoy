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
import com.codenjoy.dojo.icancode.model.interfaces.Item;
import com.codenjoy.dojo.icancode.model.items.Air;
import com.codenjoy.dojo.services.PointImpl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static com.codenjoy.dojo.icancode.model.Elements.Layers.*;
import static java.util.stream.Collectors.toList;

/**
 * Created by Mikhail_Udalyi on 08.06.2016.
 */
public class Cell extends PointImpl implements ICell {

    private List<Item> items = new ArrayList<>();

    public Cell(int x, int y) {
        super(x, y);
    }

    @Override
    public void add(Item item) {
        item.removeFromCell();

        items.add(item);
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
        return items.stream()
                .allMatch(item -> item.passable());
    }

    @Override
    public <T extends Item> T item(T type) {
        return (T) streamOf(type.getClass())
                .findFirst()
                .orElse(null);
    }

    private Stream<Item> streamOf(Class clazz) {
        return items.stream()
                .filter(item -> item.getClass() == clazz);
    }

    @Override
    public <T extends Item> T item(int layer) {
        if (items.size() <= layer) {
            return (T) new Air();
        }

        return (T) items.get(layer);
    }

    @Override
    public <T extends Item> List<T> items(Class type) {
        return (List<T>)streamOf(type)
                .collect(toList());
    }

    @Override
    public <T extends Item> List<T> items() {
        return (List<T>)new LinkedList<>(items);
    }

    @Override
    public void remove(Item item) {
        for (int i = 0; i < items.size(); ++i) {
            if (items.get(i) == item) {
                items.remove(i);
                return;
            }
        }
        items.remove(item);
    }

    @Override
    public void jump(Item item) {
        boolean heroOn2Layer = items.indexOf(item) == LAYER2;
        if (!heroOn2Layer) {
            // нас не интересуют случаи, когда герой не на втором слое
            return;
        }

        // если герой на втором слое, то в прыжке его надо перенести на 3й
        boolean twoLayers = (items.size() - 1 == LAYER2);
        boolean threeLayers = (items.size() - 1 == LAYER3);

        if (twoLayers) {
            // если два слоя, то добавляем воздух
            items.add(LAYER2, new Air());
        } else if (threeLayers) {
            // если три слоя, то ставим игрока выше
            items.add(items.remove(LAYER2));
        }
    }

    @Override
    public void landOn(Item item) {
        boolean heroOn3Layer = (items.indexOf(item) == LAYER3);
        if (!heroOn3Layer) {
            // нас не интересуют случаи, когда герой не на третьем слое (не в полете)
            return;
        }

        boolean isAirOnSecondLayer = (items.get(LAYER2) instanceof Air);
        if (isAirOnSecondLayer) {
            // если в процессе полета на втором слое был воздух мы его удаляем
            // TODO подумать о сценарии, когда воздух остался в клетке, а мы ее покинули
            items.remove(LAYER2);
        }
    }

    //================================ Overrides ================================

    @Override
    public String toString() {
        return String.format("Cell[%s,%s]=%s", x, y, items);
    }

}
