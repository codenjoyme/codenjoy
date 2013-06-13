package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.Joystick;

import java.util.List;

public class Tanks implements Game {

    private Field field;
    private Tank tank;
    private Printer printer;

    public Field getField() {
        return field;
    }

    public Tanks(int size, List<Construction> constructions, Tank tank) {
        field = new Field(size);
        this.tank = tank;
        field.setTank(tank);
        tank.setField(field);
        field.addConstructions(constructions);
        printer = new Printer(field);
    }

    @Override
    public void tick() {
        for (Bullet bullet : field.getTank().getBullets()) {
            bullet.move();
        }
        field.getTank().move();
    }

    @Override
    public Joystick getJoystick() {
        return tank;
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
    public boolean isGameOver() {   // TODO implement me
        return false;
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
