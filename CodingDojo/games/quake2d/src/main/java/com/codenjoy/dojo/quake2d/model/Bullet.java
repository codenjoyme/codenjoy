package com.codenjoy.dojo.quake2d.model;


import com.codenjoy.dojo.client.Direction;
import com.codenjoy.dojo.services.PointImpl;
import com.codenjoy.dojo.services.State;
import com.codenjoy.dojo.services.Tickable;

/**
 * Артефакт Бомба на поле
 */
public class Bullet extends PointImpl implements Tickable, State<Elements, Player> {
    public static final int START_DAMAGE = 10;
    public static final int WEAPON_MULTIPLICATOR = 2;
    private Direction direction;

    private Field field;
    private int damage;

    public Field getField() {
        return field;
    }

    public Bullet(int x, int y, Direction direction, Field field, Hero hero) {
        super(x, y);
        this.direction = direction;
        this.damage = ((hero.getAbility() != null && hero.getAbility().getAbilityType() == Ability.Type.WEAPON) ? START_DAMAGE*WEAPON_MULTIPLICATOR : START_DAMAGE);

        this.field = field;
    }

    public Direction getDirection() {
        return direction;
    }
//    public Bullet(Point point) {
//        super(point);
//    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.BULLET;
    }

    @Override
    public void tick() {
        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.inverted().changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
                x = newX;
                y = newY;
            }
        }
//        direction = null;
    }

    public int getDamage() {
        return damage;
    }
}
