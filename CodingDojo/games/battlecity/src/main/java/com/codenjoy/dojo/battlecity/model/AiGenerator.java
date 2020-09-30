package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.battlecity.model.items.AITank;
import com.codenjoy.dojo.battlecity.model.items.AITankPrize;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AiGenerator {

    private Field field;
    private Dice dice;

    private int maxAi;
    private Parameter<Integer> whichSpawnWithPrize;
    private Parameter<Integer> damagesBeforeAiDeath;
    private int spawn;

    public AiGenerator(Field field, Dice dice,
                       Parameter<Integer> whichSpawnWithPrize,
                       Parameter<Integer> damagesBeforeAiDeath)
    {
        this.field = field;
        this.dice = dice;
        this.spawn = 0;
        this.whichSpawnWithPrize = whichSpawnWithPrize;
        this.damagesBeforeAiDeath = damagesBeforeAiDeath;
    }

    void newSpawn(){
        spawn++;
    }

    public void dropAll() {
        int size = field.size();
        int needed = maxAi - field.aiTanks().size();

        for (int i = 0; i < needed; i++) {
            int y = size - 2;
            Point pt = findFreePosition(y, size);
            if (pt == null) continue;

            drop(pt);
        }
    }

    private Point findFreePosition(int y, int size) {
        Point pt = pt(0, y);

        int c = 0;
        do {
            pt.setX(dice.next(size));
        } while (field.isBarrier(pt) && c++ < size);

        if (field.isBarrier(pt)) {
            return null;
        }
        return pt;
    }

    private Tank tank(Point pt) {
        if (isPrizeTankTurn()) {
            return new AITankPrize(pt, Direction.DOWN, damagesBeforeAiDeath.getValue(), dice);
        } else {
            return new AITank(pt, Direction.DOWN, dice);
        }
    }

    private boolean isPrizeTankTurn() {
        if (whichSpawnWithPrize.getValue() == 0) {
            return false;
        }

        return spawn % whichSpawnWithPrize.getValue() == 0;
    }

    public Tank drop(Point pt) {
        Tank tank = tank(pt);
        tank.init(field);
        field.addAi(tank);
        newSpawn();
        return tank;
    }

    public void dropAll(List<? extends Point> pts) {
        maxAi = pts.size();
        for (Point pt : pts) {
            drop(pt);
        }
    }
}
