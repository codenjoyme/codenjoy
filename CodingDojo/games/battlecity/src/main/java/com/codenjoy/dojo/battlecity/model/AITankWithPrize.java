package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.util.LinkedList;
import java.util.List;

public class AITankWithPrize extends AITank {
    private boolean alive;
    private boolean isTankWithPrize;
    private List<Bullet> bulletsHitTarget;
    private int bulletsForKillAIWithPrize;
    private int ticksCount = 0;

    public AITankWithPrize(int x, int y, Dice dice, Direction direction, int bulletsForKillAIWithPrize) {
        super(x, y, dice, direction);
        alive = true;
        this.bulletsForKillAIWithPrize = bulletsForKillAIWithPrize;
    }

    public Iterable<Bullet> getBulletsHitTarget() {
        if (bulletsHitTarget == null) {
            bulletsHitTarget = new LinkedList<Bullet>();
            return bulletsHitTarget;
        } else {
            return bulletsHitTarget;
        }
    }

    public void kill(Bullet bullet) {
        getBulletsHitTarget();
        bulletsHitTarget.add(bullet);

        if (bulletsHitTarget.size() == bulletsForKillAIWithPrize) {
            alive = false;
            bulletsHitTarget = null;
        } else {
            //do nothing
        }
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        int assignElementAfterTicksCount = 4;

        if (isAlive()) {
            if (ticksCount <= assignElementAfterTicksCount) {
                this.ticksCount++;
                switch (direction) {
                    case LEFT:  return Elements.AI_TANK_LEFT;
                    case RIGHT: return Elements.AI_TANK_RIGHT;
                    case UP:    return Elements.AI_TANK_UP;
                    case DOWN:  return Elements.AI_TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            } else {
                switch (direction) {
                    case LEFT:  return Elements.AI_TANK_PRIZE_LEFT;
                    case RIGHT: return Elements.AI_TANK_PRIZE_RIGHT;
                    case UP:    return Elements.AI_TANK_PRIZE_UP;
                    case DOWN:  return Elements.AI_TANK_PRIZE_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            }
        } else {
            return Elements.BANG;
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isTankWithPrize() {
        isTankWithPrize = true;
        return isTankWithPrize;
    }
}
