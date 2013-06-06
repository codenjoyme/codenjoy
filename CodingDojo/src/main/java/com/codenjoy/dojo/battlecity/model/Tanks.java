package com.codenjoy.dojo.battlecity.model;


public class Tanks {

    public final static int BATTLE_FIELD_SIZE = 13;
    private Field field;
    private Joystick joystick;

    public Field getField() {
        return field;
    }

    public Tanks() {
        field = new Field(BATTLE_FIELD_SIZE);
        joystick = new Joystick();
    }

    public void tact() {
        Bullet bullet = field.getTank().getBullet();
        if(bullet!=null) {
        	bullet.move();
        }
    }

    public Joystick getJoystick() {
        return joystick;
    }
}
