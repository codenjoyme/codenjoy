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

import com.codenjoy.dojo.excitebike.model.elements.GameElementType;
import com.codenjoy.dojo.excitebike.model.items.Obstacle;
import com.codenjoy.dojo.excitebike.model.items.Shiftable;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.printer.CharElements;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.excitebike.model.elements.GameElementType.OBSTACLE;
import static com.codenjoy.dojo.excitebike.services.generation.generator.ObstacleChainGenerator.Shape.LADDER_DOWN;
import static com.codenjoy.dojo.excitebike.services.generation.generator.ObstacleChainGenerator.Shape.LADDER_UP;
import static com.codenjoy.dojo.excitebike.services.generation.generator.ObstacleChainGenerator.Shape.STRAIGHT;

/**
 * Created by Pavel Bobylev 7/18/2019
 */
public class ObstacleChainGenerator implements Generator {

    private Map<GameElementType, List<Shiftable>> elements = new EnumMap<>(GameElementType.class);
    private final Dice dice;
    private final int x0;
    private final int ySize;
    private int width;
    private Shape shape;
    private boolean ladderDirectionForward;

    public ObstacleChainGenerator(Dice dice, int xSize, int ySize) {
        this.dice = dice;
        x0 = xSize - 1;
        this.ySize = ySize;
    }

    @Override
    public Map<? extends CharElements, List<Shiftable>> generate() {
        ladderDirectionForward = true;
        elements.put(OBSTACLE, new LinkedList<>());
        shape = Shape.values()[dice.next(Shape.values().length)];
        width = shape == STRAIGHT ? 1 : dice.next(ySize - 2) + 1;
        int exitNumber = dice.next((ySize - 2) / 2) + 1;
        int exitCounter = 0;
        int x = x0 - 1;
        for (int y = 1; y < ySize - 1; y++) {
            boolean needMoreExits = exitCounter < exitNumber;
            boolean randomBoolean = dice.next(10) < 5;
            boolean iterationsLeftNumberEqualsToExitsRequired = exitNumber - exitCounter > ySize - 2 - y;
            if (needMoreExits && (randomBoolean || iterationsLeftNumberEqualsToExitsRequired)) {
                exitCounter++;
                x++;
            } else {
                x = getX(x0, x, y);
                elements.get(OBSTACLE).add(new Obstacle(x, y));
            }
        }
        return elements;
    }

    private int getX(int x0, int previousX, int y) {
        if (shape == STRAIGHT) {
            return x0;
        }
        if (shape == LADDER_UP) {
            if (previousX - x0 + 1 < width) {
                return ++previousX;
            } else {
                shape = LADDER_DOWN;
                ladderDirectionForward = false;
                return --previousX;
            }
        }
        if (shape == LADDER_DOWN) {
            if (ladderDirectionForward) {
                if (y <= width) {
                    return x0 + width - y;
                } else {
                    shape = LADDER_UP;
                    ladderDirectionForward = false;
                    return ++previousX;
                }
            } else {
                if (previousX > x0) {
                    return -- previousX;
                } else {
                    shape = LADDER_UP;
                    ladderDirectionForward = true;
                    return ++previousX;
                }
            }
        }
        return x0 + dice.next(width);
    }

    @Override
    public int generationLockSize() {
        return width + 1;
    }

    enum Shape {
        STRAIGHT,
        LADDER_UP,
        LADDER_DOWN
    }
}
