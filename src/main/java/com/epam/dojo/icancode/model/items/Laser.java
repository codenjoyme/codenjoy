package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.interfaces.IItem;

/**
 * Created by oleksandr.baglai on 20.06.2016.
 */
public class Laser extends FieldItem implements Tickable {

    private final Direction direction;

    public Laser(Direction direction) {
        super(getElement(direction));
        this.direction = direction;
    }

    private static Elements getElement(Direction direction) {
        switch (direction) {
            case LEFT: return Elements.LASER_LEFT;
            case RIGHT: return Elements.LASER_RIGHT;
            case DOWN: return Elements.LASER_DOWN;
            case UP: return Elements.LASER_UP;
        }
        throw new IllegalStateException("Unexpected direction: " + direction);
    }

    @Override
    public void action(IItem item) {
        if (item instanceof Hero) {
            Hero hero = (Hero) item;
            if (!hero.isFlying()) {
                getCell().removeItem(this);
                hero.dieOnLaser();
            }
        }
    }

    @Override
    public void tick() {
        int newX = direction.changeX(getCell().getX());
        int newY = direction.changeY(getCell().getY());

        if (!field.isBarrier(newX, newY)) {
            field.move(this, newX, newY);
        } else {
            getCell().removeItem(this);
        }
    }
}
