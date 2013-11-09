package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Printer {

    private Tanks field;
    private Player player;
    private Elements[][] battleField;
    private final int size;

    private Map<Direction, Elements> tankDirections =
            new HashMap<Direction, Elements>() {{
                put(Direction.UP, Elements.TANK_UP);
                put(Direction.RIGHT, Elements.TANK_RIGHT);
                put(Direction.DOWN, Elements.TANK_DOWN);
                put(Direction.LEFT, Elements.TANK_LEFT);
            }};

    private Map<Direction, Elements> otherTankDirections =
            new HashMap<Direction, Elements>() {{
                put(Direction.UP, Elements.OTHER_TANK_UP);
                put(Direction.RIGHT, Elements.OTHER_TANK_RIGHT);
                put(Direction.DOWN, Elements.OTHER_TANK_DOWN);
                put(Direction.LEFT, Elements.OTHER_TANK_LEFT);
            }};

    public Printer(Tanks field, Player player) {
        this.field = field;
        this.player = player;
        size = field.getSize();
    }

    private void addHorizontalBorders() {
        for (int colNumber = 0; colNumber < size; colNumber++) {
            battleField[0][colNumber] = Elements.WALL;
            battleField[size - 1][colNumber] = Elements.WALL;
        }
    }

    private void addVerticalBorders() {
        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            battleField[rowNumber][0] = Elements.WALL;
            battleField[rowNumber][size - 1] = Elements.WALL;
        }
    }

    @Override
    public String toString() {
        fillField();
        
        String string = "";
        for (Elements[] currentRow : battleField) {
            for (Elements element : currentRow) {
                string += element.ch;
            }
            string += "\n";
        }
        return string;
    }

    private void fillField() {
        battleField = new Elements[size][size];

        for (int rowNumber = 0; rowNumber < size; rowNumber++) {
            for (int colNumber = 0; colNumber < size; colNumber++) {
                set(new PointImpl(rowNumber, colNumber), Elements.GROUND);
            }
        }

        List<Construction> constructions = field.getConstructions();
        for (Construction construction : constructions) {
            set(construction, construction.getChar());
        }

        for (Tank tank : field.getTanks()) {
            if (tank.isAlive()) {
                set(tank, getTankElement(tank));

                for (Bullet bullet : tank.getBullets()) {
                    if (!bullet.equals(tank)) {
                        set(bullet, Elements.BULLET);
                    }
                }
            } else {
                set(tank, Elements.DEAD);
            }
        }

        addHorizontalBorders();
        addVerticalBorders();
    }

    private Elements getTankElement(Tank tank) {
        if (tank.equals(player.getTank())) {
            return tankDirections.get(tank.getDirection());
        } else {
            return otherTankDirections.get(tank.getDirection());
        }
    }

    private void set(Point pt, Elements element) {
        if (pt.getY() == -1 || pt.getX() == -1) {
            return;
        }

        battleField[size - 1 - pt.getY()][pt.getX()] = element;
    }

}
