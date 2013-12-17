package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:58
 */
public class Printer {
    private Loderunner game;
    private Elements[][] field;
    private final int size;

    public Printer(Loderunner game) {
        this.game = game;
        size = game.getSize();
    }

    @Override
    public String toString() {
        fillField();

        String string = "";
        for (Elements[] currentRow : field) {
            for (Elements element : currentRow) {
                string += element.ch;
            }
            string += "\n";
        }
        return string;
    }

    private void fillField() {
        field = new Elements[size][size];

        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            for (int colNumber = 0; colNumber < size; colNumber++) {
                set(new PointImpl(rowNumber, colNumber), Elements.NONE);
            }
        }

        List<Point> borders = game.getBorders();
        for (Point border : borders) {
            set(border, Elements.WALL);
        }

        List<Brick> bricks = game.getBricks();
        for (Brick brick : bricks) {
            if (brick.drillCount() == 1) {
                set(new PointImpl(brick.getX(), brick.getY() + 1), Elements.DRILL_SPACE);
                set(brick, Elements.DRILL_PIT);
            } else if (brick.drillCount() > 1) {
                set(brick, Elements.NONE);
            } else {
                set(brick, Elements.BRICK);
            }
        }

        Hero hero = game.getHero();
        if (hero.isDrilled()) {
            if (hero.getDirection().equals(Direction.LEFT)) {
                set(hero, Elements.HERO_DRILL_LEFT);
            } else {
                set(hero, Elements.HERO_DRILL_RIGHT);
            }
        } else {
            if (hero.getDirection().equals(Direction.LEFT)) {
                set(hero, Elements.HERO_LEFT);
            } else {
                set(hero, Elements.HERO_RIGHT);
            }
        }
    }

    private void set(Point pt, Elements element) {
        if (pt.getY() == -1 || pt.getX() == -1) {
            return;
        }

        field[size - 1 - pt.getY()][pt.getX()] = element;
    }
}
