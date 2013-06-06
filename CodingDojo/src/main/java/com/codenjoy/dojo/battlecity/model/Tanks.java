package com.codenjoy.dojo.battlecity.model;


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.Joystick;

public class Tanks implements Game {

    public final static int BATTLE_FIELD_SIZE = 13;
    private Field field;
    private Tank tank;
    private Printer printer;

    public Field getField() {
        return field;
    }

    public Tanks() {
        field = new Field(BATTLE_FIELD_SIZE);   // TODO move to config
        tank = new Tank(6, 6);
        field.setTank(tank);
        Construction construction = new Construction(2,3);
        field.setConstruction(construction);
        printer = new Printer (field);
    }

    @Override
    public void tick() {
        Bullet bullet = field.getTank().getBullet();
        if(bullet!=null) {
        	bullet.move();
        }
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
        return printer.drawField();
    }

    @Override
    public void destroy() {    // TODO implement me

    }

}
