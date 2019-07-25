package com.codenjoy.dojo.excitebike.services.generation.generator;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import com.codenjoy.dojo.excitebike.model.items.Accelerator;
import com.codenjoy.dojo.excitebike.model.elements.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.Inhibitor;
import com.codenjoy.dojo.excitebike.model.items.LineChanger;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;
import com.google.common.collect.Lists;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.ACCELERATOR;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.INHIBITOR;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_DOWN;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.LINE_CHANGER_UP;
import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.OBSTACLE;

/**
 * Created by Pavel Bobylev 7/18/2019
 */
public class SingleElementGenerator implements Generator {

    private final Dice dice;
    private final int xSize;
    private final int ySize;

    public SingleElementGenerator(Dice dice, int xSize, int ySize) {
        this.dice = dice;
        this.xSize = xSize;
        this.ySize = ySize;
    }

    @Override
    public Map<? extends CharElements, List<Shiftable>> generate() {
        int rndNonFenceElementOrdinal = dice.next(GameElementType.values().length - 2) + 2;
        int rndNonFenceLaneNumber = dice.next(ySize - 2) + 1;
        CharElements randomType = GameElementType.values()[rndNonFenceElementOrdinal];
        int firstPossibleX = xSize - 1;
        return getNewElement(randomType, firstPossibleX, rndNonFenceLaneNumber);
    }

    private Map<GameElementType, List<Shiftable>> getNewElement(CharElements randomType, int x, int y) {
        Map<GameElementType, List<Shiftable>> map = new EnumMap<>(GameElementType.class);
        if (ACCELERATOR.equals(randomType)) {
            map.put(ACCELERATOR, Lists.newArrayList(new Accelerator(x, y)));
        } else if (INHIBITOR.equals(randomType)) {
            map.put(INHIBITOR, Lists.newArrayList(new Inhibitor(x, y)));
        } else if (OBSTACLE.equals(randomType)) {
            map.put(OBSTACLE, Lists.newArrayList(new Obstacle(x, y)));
        } else if (LINE_CHANGER_UP.equals(randomType)) {
            map.put(LINE_CHANGER_UP, Lists.newArrayList(new LineChanger(x, y, true)));
        } else if (LINE_CHANGER_DOWN.equals(randomType)) {
            map.put(LINE_CHANGER_DOWN, Lists.newArrayList(new LineChanger(x, y, false)));
        }
        return map;
    }

    @Override
    public int generationLockSize() {
        return 0;
    }
}
