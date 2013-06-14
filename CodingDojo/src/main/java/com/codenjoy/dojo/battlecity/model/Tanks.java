package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;

import java.util.Arrays;
import java.util.List;

public class Tanks implements Game {

    private Field field;
    private Printer printer;

    public Field getField() {
        return field;
    }

    public Tanks(int size, List<Construction> constructions, Tank... tanks) {
        field = new Field(size);

        for (Tank tank : tanks) {
            field.addTank(tank);
        }

        field.addConstructions(constructions);
        printer = new Printer(field);
    }

    @Override
    public void tick() {
        for (Tank tank : field.getTanks()) {
            if (!tank.isAlive()) {
                field.remove(tank);
            }
        }
        for (Bullet bullet : field.getBullets()) {
            bullet.move();
        }
        for (Tank tank : field.getTanks()) {
            if (tank.isAlive()) {
                tank.move();
            }
        }
    }

    @Override
    public Joystick getJoystick() {
        return field.getTanks().iterator().next();
    }

    @Override
    public int getMaxScore() {  // TODO implement me
        return 0;
    }

    @Override
    public int getCurrentScore() {  // TODO implement me
        return 0;
    }

    @Override
    public boolean isGameOver() {
        for (Tank tank : field.getTanks()) {
            if (!(tank instanceof AITank)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void newGame() {   // TODO implement me

    }

    @Override
    public String getBoardAsString() {
        return printer.toString();
    }

    @Override
    public void destroy() {    // TODO implement me

    }

}
