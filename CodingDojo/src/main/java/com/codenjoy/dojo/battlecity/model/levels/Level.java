package com.codenjoy.dojo.battlecity.model.levels;

import com.codenjoy.dojo.battlecity.model.*;
import com.codenjoy.dojo.services.LengthToXY;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Point;

import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 13.12.13
 * Time: 18:15
 */
public class Level implements Field {

    private final LengthToXY xy;

    private String map =
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ " +
            "☼ ˅ ˅       ˅ ˅       ˅ ˅       ˅ ˅       ˅ ˅       ˅ ˅       ˅ ˅ ☼ " +
            "☼                                                                 ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ☼ ☼ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬                         ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼           ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬           ☼ " +
            "☼ ☼ ☼       ╬ ╬ ╬ ╬ ╬                         ╬ ╬ ╬ ╬ ╬       ☼ ☼ ☼ " +
            "☼                                                                 ☼ " +
            "☼                         ╬ ╬ ╬     ╬ ╬ ╬                         ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬                                             ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼     ╬ ╬ ╬               ╬ ╬ ╬ ╬ ╬ ╬ ╬ ╬               ╬ ╬ ╬     ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼                         ╬ ╬         ╬ ╬                         ☼ " +
            "☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ☼ ";

    public Level() {
        removeSpaces();
        xy = new LengthToXY(getSize());
    }

    private void removeSpaces() {
        String result = "";
        for (int i = 0; i < map.length(); i += 2) {
            result += map.charAt(i);
        }
        map = result;
    }

    @Override
    public int getSize() {
        return (int) Math.sqrt(map.length());
    }

    @Override
    public List<Construction> getConstructions() {
        List<Construction> result = new LinkedList<Construction>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.CONSTRUCTION.getChar()) {
                result.add(new Construction(xy.getXY(index)));
            }
        }
        return result;
    }

    @Override
    public boolean isBarrier(int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean outOfField(int x, int y) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void affect(Bullet bullet) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Tank> getTanks() {
        List<Tank> result = new LinkedList<Tank>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.OTHER_TANK_DOWN.getChar()) {
                Point pt = xy.getXY(index);
                result.add(new AITank(pt.getX(), pt.getY(), Direction.DOWN));
            }
        }
        return result;
    }

    @Override
    public List<Point> getBorders() {
        List<Point> result = new LinkedList<Point>();
        for (int index = 0; index < map.length(); index++) {
            if (map.charAt(index) == Elements.BATTLE_WALL.getChar()) {
                result.add(xy.getXY(index));
            }
        }
        return result;
    }
}
