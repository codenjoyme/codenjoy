package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.icancode.model.Elements;

public class ZombiePot extends FieldItem implements Tickable {

    public static int TICKS = 5;
    private int time = 0;

    public ZombiePot(Elements el) {
        super(el);
    }

    @Override
    public void tick() {
        if (++time % TICKS == 0) {
            field.move(newZombie(), this.getCell().getX(), this.getCell().getY());
        }
    }

    private Zombie newZombie() {
        boolean gender = field.dice().next(1) == 0;
        Zombie zombie = new Zombie(gender);
        zombie.setField(field);
        return zombie;
    }
}
