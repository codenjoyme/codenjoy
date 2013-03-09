package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.Tickable;

import java.util.Iterator;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/8/13
 * Time: 8:17 PM
 */
public class MeatChoppers implements Walls, Tickable {

    private Walls walls;
    private int size;
    private int count;
    private Dice dice;

    public MeatChoppers(Walls walls, int size, int count, Dice dice) {
        this.dice = dice;
        if (walls.subList(Wall.class).size() + count >= size*size - 1) {
            throw new IllegalArgumentException("No more space at board for MeatChoppers");
        }
        this.walls = walls;
        this.size = size;
        this.count = count;
        randomFill();
    }

    private void randomFill() { // TODO remove duplicates
        int index = 0;
        int counter = 0;
        do {
            int x = dice.next(size);
            int y = dice.next(size);

            if (walls.itsMe(x, y)) {
                continue;
            }

            if (y == 1) {  // чтобы бомбермену было откуда начинать
                continue;
            }

            walls.add(new MeatChopper(x, y));

            index++;
            counter++;
        } while (index != count && counter < 10000);
        if (counter == 10000) {
            throw new  RuntimeException("Dead loop at MeatChoppers.randomFill!");
        }
    }

    @Override
    public Iterator<Wall> iterator() {
        return walls.iterator();
    }

    @Override
    public void add(int x, int y) {
        walls.add(x, y);
    }

    @Override
    public boolean itsMe(int x, int y) {
        return walls.itsMe(x, y);
    }

    @Override
    public <T extends Wall> List<T> subList(Class<T> filter) {
        return walls.subList(filter);
    }

    @Override
    public void add(Wall wall) {
        walls.add(wall);
    }

    @Override
    public Wall destroy(int x, int y) {
        return walls.destroy(x, y);
    }

    @Override
    public void tick() {
        List<MeatChopper> meatChoppers = walls.subList(MeatChopper.class);
        for (MeatChopper meatChopper : meatChoppers) {
            Direction direction = meatChopper.getDirection();
            if (direction != null && dice.next(5) > 0) {
                int x = direction.changeX(meatChopper.getX());
                int y = direction.changeY(meatChopper.getY());
                if (!walls.itsMe(x, y)) {
                    meatChopper.move(x, y);
                    continue;
                } else {
                    // do nothig
                }
            }
            meatChopper.setDirection(tryToMove(meatChopper));
        }
    }

    private Direction tryToMove(Point pt) {
        int count = 0;
        int x = pt.getX();
        int y = pt.getY();
        Direction direction = null;
        do {
            int n = 4;
            int move = dice.next(n);
            direction = Direction.valueOf(move);

            x = direction.changeX(pt.getX());
            y = direction.changeY(pt.getY());
        } while (walls.itsMe(x, y) && count++ < 10);

        if (count < 10) {
            pt.move(x, y);
            return direction;
        }
        return null;
    }
}
