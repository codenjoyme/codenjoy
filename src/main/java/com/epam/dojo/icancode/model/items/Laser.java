package com.epam.dojo.icancode.model.items;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.icancode.model.Elements;
import com.epam.dojo.icancode.model.ItemLogicType;

/**
 * Created by oleksandr.baglai on 20.06.2016.
 */
public class Laser extends BaseItem implements Tickable {

    private final Direction direction;

    public Laser(Direction direction) {
        super(new ItemLogicType[] {ItemLogicType.PASSABLE, ItemLogicType.LASER},
                getElement(direction));
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
    public void action(BaseItem item) {
        if (item instanceof Hero) {
            Hero hero = (Hero) item;
            if (!hero.isFlying()) {
                cell.removeItem(this);
                hero.dieOnLaser();
            }
        }
    }

    @Override
    public void tick() {
        int newX = direction.changeX(cell.getX());
        int newY = direction.changeY(cell.getY());

        if (!field.isBarrier(newX, newY)) { // TODO из за этого пришлось протаскивать Field через levelImpl -> Cell -> BaseItem
            field.move(this, newX, newY);
        } else {
            cell.removeItem(this);
        }
    }
}
