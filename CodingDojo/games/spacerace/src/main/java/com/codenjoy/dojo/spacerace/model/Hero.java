package com.codenjoy.dojo.spacerace.model;

import com.codenjoy.dojo.services.*;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private Field field;
    private boolean alive;
    private Direction direction;
    private boolean fire;
    private int deadCount = 0;
    private BulletCharger charger;

    public Hero(Point xy, BulletCharger charger) {
        super(xy);
        direction = null;
        fire = false;
        alive = true;
        this.charger = charger;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        if (!alive) return;

        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {
        if (!alive) return;

        direction = Direction.LEFT;
    }

    @Override
    public void right() {
        if (!alive) return;

        direction = Direction.RIGHT;
    }

    @Override
    public void act(int... p) {
        if (p.length == 1 && p[0] == 0) {
            die();
        }

        if (!alive) return;

        fire = true;
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        charger.tick();

        if (!alive) {
            if(deadCount > 1){
                alive = true;
                deadCount = 0;
                return;
            }
            deadCount++;
            return;
        }

        if (fire){
            if (charger.canShoot()) {
                field.addBullet(this.getX(), this.getY(), this);
            }
            fire = false;
        }

        if (direction != null) {
            int newX = direction.changeX(x);
            int newY = direction.changeY(y);

            if (!field.isBarrier(newX, newY)) {
                move(newX, newY);
            }
        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die(){
        deadCount++;
        alive = false;
    }
    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (!isAlive()) {
            return Elements.DEAD_HERO;
        }

        if (this == player.getHero()) {
            return Elements.HERO;
        } else {
            return Elements.OTHER_HERO;
        }
    }

    // TODO потестить кейз, когда игрок уже погиб, а ему за его снаряды начисляются очки - не должны начисляться
    public void renew(Point pt, BulletCharger charger) {
        this.charger = charger;
        alive = true;
        move(pt.getX(), pt.getY());
    }

    public void recharge() {
        charger.setToRecharge(true);
    }
}
