package com.codenjoy.dojo.fifteen.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.Point;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Это реализация героя. Обрати внимание, что он имплементит {@see Joystick}, а значит может быть управляем фреймворком
 * Так же он имплементит {@see Tickable}, что значит - есть возможность его оповещать о каждом тике игры.
 */
public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {
    private List<Digit> digits;
    private int moveCount;
    private Player player;
    private Field field;
    private boolean alive;
    private Direction direction;

    public Hero(int x, int y) {
        super(x, y);
        direction = null;
        alive = true;
        moveCount = 1;
        digits = new LinkedList<Digit>();
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Hero(Point xy) {
        super(xy);
        direction = null;
        alive = true;
        moveCount = 1;
        digits = new LinkedList<Digit>();
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
        // Do nothing
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {
        if (!alive) return;

        if (direction != null) {
            int newX = direction.changeX(getX());
            int newY = direction.changeY(getY());

            if (!field.isBarrier(newX, newY)) {
                moveCount++;
                Digit digit = field.getDigit(newX, newY);
                digit.move(getX(), getY());
                move(newX, newY);

                if (new DigitHandler().isRightPosition(digit)) {

                   if(!digits.contains(digit)){
                       int number = 1 + Arrays.asList(DigitHandler.DIGITS).indexOf(digit);
                       player.event(new Bonus(moveCount, number));
                       moveCount = 1;
                       digits.add(digit);
                   }

                }
            }

        }
        direction = null;
    }

    public boolean isAlive() {
        return alive;
    }

    public void die() {
        alive = false;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return Elements.HERO;
    }
}
