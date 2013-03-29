package com.javatrainee.tanks;

public class Tanks {

    public final static int BATTLE_FIELD_SIZE = 13;
    private Field field;

    public Field getField() {
        return field;
    }

    public Tanks() {
        field = new Field(BATTLE_FIELD_SIZE);
    }

    public void tact() {
        Bullet bullet = field.getTank().getBullet();
        bullet.move();
    }
}
