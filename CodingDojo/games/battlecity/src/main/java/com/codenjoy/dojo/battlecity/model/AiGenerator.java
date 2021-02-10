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

    private final Field field;
    private final Dice dice;

    private Parameter<Integer> whichSpawnWithPrize;
    private Parameter<Integer> damagesBeforeAiDeath;
    private Parameter<Integer> aiTicksPerShoot;
    private Parameter<Integer> aiPrizeLimit;

    private int capacity;
    private int spawn;
    private int haveWithPrize;
    private int aiPrize;

    public AiGenerator(Field field, Dice dice,
                       Parameter<Integer> whichSpawnWithPrize,
                       Parameter<Integer> damagesBeforeAiDeath,
                       Parameter<Integer> aiTicksPerShoot,
                       Parameter<Integer> aiPrizeLimit)
    {
        this.field = field;
        this.dice = dice;
        this.spawn = 0;
        this.aiPrize = 0;
        this.whichSpawnWithPrize = whichSpawnWithPrize;
        this.damagesBeforeAiDeath = damagesBeforeAiDeath;
        this.aiTicksPerShoot = aiTicksPerShoot;
        this.aiPrizeLimit = aiPrizeLimit;
    }

    void newSpawn(){
        spawn++;
    }

    public void dropAll() {
        int needed = capacity - field.aiTanks().size();

        for (int i = 0; i < needed; i++) {
            Point pt = freePosition();
            if (pt == null) continue;

            drop(pt);
        }
    }

    private Point findFreePosition(int y, int size) {
        Point pt = pt(0, y);

        int c = 0;
        do {
            pt.setX(dice.next(size));

        } while ((field.isBarrier(pt) || field.isRiver(pt)) && c++ < size);

        if (field.isBarrier(pt)) {
            return null;
        }
        return pt;
    }

    private Point freePosition() {
        return findFreePosition(field.size() - 2, field.size());
    }

    private Tank tank(Point pt) {
        if (isPrizeTankTurn() && canDrop()) {
            return new AITankPrize(pt,
                    Direction.DOWN,
                    damagesBeforeAiDeath.getValue(),
                    aiTicksPerShoot.getValue(),
                    dice);
        } else {
            return new AITank(pt,
                    Direction.DOWN,
                    aiTicksPerShoot.getValue(),
                    dice);
        }
    }

    private boolean isPrizeTankTurn() {
        if (whichSpawnWithPrize.getValue() == 0) {
            return false;
        }
        return spawn % whichSpawnWithPrize.getValue() == 0;
    }

    public Tank drop(Point pt) {
        Tank tank = checkDropPt(pt);
        tank.init(field);
        field.addAi(tank);
        newSpawn();
        return tank;
    }

    private Tank checkDropPt(Point pt) {
        Tank tank;
        if (field.isRiver(pt)) {
            tank = tank(freePosition());
        } else {
            tank = tank(pt);
        }
        return tank;
    }

    public void dropAll(List<? extends Point> points) {
        capacity = points.size();
        for (Point pt : points) {
            drop(pt);
        }
    }

    public void allHave(int haveWithPrize) {
        this.haveWithPrize = haveWithPrize;
    }

    private int neededWithPrize() {
        if (aiPrizeLimit.getValue() == 0) {
            aiPrize = 0;
        }
        int neededWithPrize = aiPrizeLimit.getValue() - haveWithPrize;

        if (aiPrize < neededWithPrize) {
            aiPrize++;
        } else {
            aiPrize = 0;
        }
        return aiPrize;
    }

    private boolean canDrop() {
        int moreWithPrize = neededWithPrize();

        if(moreWithPrize == 0) {
            return false;
        }

        if (moreWithPrize > 0) {
            return true;
        }
       return false;
    }
}
