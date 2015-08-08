package com.codenjoy.dojo.startandjump.model;

import com.codenjoy.dojo.startandjump.services.HeroStatus;
import com.codenjoy.dojo.services.*;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private boolean alive;
    private Direction direction;
    private HeroStatus status;
    private int alreadyJumped;

    public Hero(Point xy) {
        super(xy);
        status = HeroStatus.IDLE;
        alreadyJumped = 0;
        direction = null;
        alive = true;
    }

//    public void init(Field field) {
//        this.field = field;
//    }

    @Override
    public void down() {

    }

    @Override
    public void up() {
        if (!alive) return;

        direction = Direction.UP;
    }

    @Override
    public void left() {

    }

    @Override
    public void right() {

    }

    @Override
    public void act(int... p) {
        if (!alive) return;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (status == HeroStatus.IDLE) alreadyJumped = 0;

        if (direction == Direction.UP) {
            if (alreadyJumped <= 1) {
                setStatus(HeroStatus.JUMPING);
            }
        }

        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        if (alive) {
            return Elements.HERO;
        } else {
            return Elements.BLACK_HERO;
        }
    }

    public void dies() {
        alive = false;
    }

    public void setStatus(HeroStatus status) {
        this.status = status;
    }

    public void falls() {
        move(x, y - 1);
    }

    public void jumps() {
        alreadyJumped++;
        move(x, y + 1);
    }

    public HeroStatus getStatus() {
        return status;
    }

    public void setAlreadyJumped(int alreadyJumped) {
        this.alreadyJumped = alreadyJumped;
    }
}
