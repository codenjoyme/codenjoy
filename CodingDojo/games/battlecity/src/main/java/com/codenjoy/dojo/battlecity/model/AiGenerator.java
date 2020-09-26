package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.settings.Parameter;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class AiGenerator {

    private Field field;
    private Dice dice;

    private int maxAi;
    private Parameter<Integer> whichSpawnWithPrize;
    private Parameter<Integer> damagesBeforeAiDeath;
    private int aiSpawn;

    public AiGenerator(Field field, Dice dice,
                       Parameter<Integer> whichSpawnWithPrize,
                       Parameter<Integer> damagesBeforeAiDeath)
    {
        this.field = field;
        this.dice = dice;
        this.aiSpawn = 0;
        this.whichSpawnWithPrize = whichSpawnWithPrize;
        this.damagesBeforeAiDeath = damagesBeforeAiDeath;
    }

    public void dropAll() {
        int size = field.size();

        for (int i = field.getAiTanks().size(); i < maxAi; i++) {
            Point pt = pt(0, size - 2);
            int c = 0;
            do {
                pt.setX(dice.next(size));
            } while (field.isBarrier(pt) && c++ < size);

            if (!field.isBarrier(pt)) {
                drop(new AITank(pt, dice, Direction.DOWN));
            }
        }
    }

    public void drop(Tank tank) {
        tank = replaceAiOnAiPrize(tank);
        tank.init(field);
        field.addAi(tank);
        aiSpawn++;
    }

    private Tank replaceAiOnAiPrize(Tank tank) {
        if (aiSpawn == whichSpawnWithPrize.getValue()) {
            aiSpawn = 0;
        }

        if (whichSpawnWithPrize.getValue() > 1) {
            int indexAiPrize = whichSpawnWithPrize.getValue() - 2;
            if (aiSpawn == indexAiPrize) {
                Point pt = pt(tank.getX(), tank.getY());
                return new AITankPrize(pt, dice, tank.getDirection(), damagesBeforeAiDeath.getValue());
            }
        }
        return tank;
    }

    public void init(Tank[] tanks) {
        maxAi = tanks.length;
        for (Tank tank : tanks) {
            drop(tank);
        }
    }
}
