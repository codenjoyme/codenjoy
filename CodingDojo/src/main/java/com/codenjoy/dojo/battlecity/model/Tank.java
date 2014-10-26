package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

public class Tank extends MovingObject implements Joystick, Tickable, State<Elements, Player> {

    private Dice dice;
    private List<Bullet> bullets;
    protected Field field;
    private boolean alive;
    private Gun gun;

    public Tank(int x, int y, Direction direction, Dice dice, int ticksPerBullets) {
        super(x, y, direction);
        gun = new Gun(ticksPerBullets);
        bullets = new LinkedList<Bullet>();
        speed = 1;
        moving = false;
        alive = true;
        this.dice = dice;
    }

    void turn(Direction direction) {
        this.direction = direction;
    }

    @Override
    public void up() {
        direction = Direction.UP;
        moving = true;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
        moving = true;
    }

    @Override
    public void right() {
        direction = Direction.RIGHT;
        moving = true;
    }

    @Override
    public void left() {
        direction = Direction.LEFT;
        moving = true;
    }

    @Override
    public void moving(int newX, int newY) {
        if (field.isBarrier(newX, newY)) {
            // do nothing
        } else {
            x = newX;
            y = newY;
        }
        moving = false;
    }

    @Override
    public void act(int... p) {
        if (gun.tryToFire()) {
            Bullet bullet = new Bullet(field, direction, copy(), this, new OnDestroy() {
                @Override
                public void destroy(Object bullet) {
                    Tank.this.bullets.remove(bullet);
                }
            });
            if (!bullets.contains(bullet)) {
                bullets.add(bullet);
            }
        }
    }

    public Iterable<Bullet> getBullets() {
        return new LinkedList<Bullet>(bullets);
    }

    public void setField(Field field) {
        this.field = field;
        int xx = x;
        int yy = y;
        while (field.isBarrier(xx, yy)) {
            xx = dice.next(field.size());
            yy = dice.next(field.size());
        }
        x = xx;
        y = yy;
        alive = true;
    }

    public void kill(Bullet bullet) {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public void removeBullets() {
        bullets.clear();
    }

    @Override
    public void tick() {
        gun.tick();
    }

    @Override
    public Elements state(Player player) {
        if (isAlive()) {
            if (player.getTank() == this) {
                switch (direction) {
                    case LEFT:  return Elements.TANK_LEFT;
                    case RIGHT: return Elements.TANK_RIGHT;
                    case UP:    return Elements.TANK_UP;
                    case DOWN:  return Elements.TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            } else {
                switch (direction) {
                    case LEFT:  return Elements.OTHER_TANK_LEFT;
                    case RIGHT: return Elements.OTHER_TANK_RIGHT;
                    case UP:    return Elements.OTHER_TANK_UP;
                    case DOWN:  return Elements.OTHER_TANK_DOWN;
                    default:    throw new RuntimeException("Неправильное состояние танка!");
                }
            }
        } else {
            return Elements.BANG;
        }
    }
}
