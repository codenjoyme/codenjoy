package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.Direction;

import java.util.LinkedList;
import java.util.List;

public class AITankWithPresent extends AITank {
    private boolean alive;
    private List<Bullet> bulletsHitTarget;
    private int bulletsForKill;

    public AITankWithPresent(int x, int y, Dice dice, Direction direction, int bulletsForKill) {
        super(x, y, dice, direction);
        alive = true;
        this.bulletsForKill = bulletsForKill;
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

        if (bulletsHitTarget.size() == bulletsForKill) {
            alive = false;
            bulletsHitTarget = null;
        } else {
            //do nothing
        }
    }

    public boolean isAlive() {
        return alive;
    }
}
