package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.codenjoy.dojo.services.PointImpl.pt;

public class BattlecityPrinter implements GamePrinter {

    private Field field;
    private Player player;

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

    private Map<Direction, Elements> aiTankDirections =
            new HashMap<Direction, Elements>() {{
                put(Direction.UP, Elements.AI_TANK_UP);
                put(Direction.RIGHT, Elements.AI_TANK_RIGHT);
                put(Direction.DOWN, Elements.AI_TANK_DOWN);
                put(Direction.LEFT, Elements.AI_TANK_LEFT);
            }};


    public BattlecityPrinter(Field field, Player player) {
        this.field = field;
        this.player = player;
    }

    private Elements getTankElement(Tank tank) {
        return getDirectionElementsMap(tank).get(tank.getDirection());
    }

    private Map<Direction, Elements> getDirectionElementsMap(Tank tank) {
        if (tank instanceof AITank) {
            return aiTankDirections;
        } else if (tank.equals(player.getTank())) {
            return tankDirections;
        } else {
            return otherTankDirections;
        }
    }

    @Override
    public Enum get(int x, int y) {
        Point pt = pt(x, y);

        List<Construction> constructions = field.getConstructions();
        if (constructions.contains(pt)) {
            Construction construction = constructions.get(constructions.indexOf(pt));
            if (!construction.destroyed()) {
                return construction.state();
            }
        }

        List<Point> borders = field.getBorders();
        if (borders.contains(pt)) {
            return Elements.BATTLE_WALL;
        }

        List<Tank> tanks = field.getTanks();
        if (tanks.contains(pt)) {
            Tank tank = tanks.get(tanks.indexOf(pt));

            if (tank.isAlive()) {
                return getTankElement(tank);
            }

            return Elements.BANG;
        }

        List<Bullet> bullets = field.getBullets();
        if (bullets.contains(pt)) {
            Bullet bullet = bullets.get(bullets.indexOf(pt));
            if (bullet.destroyed()) {
                return Elements.BANG;
            }

            return Elements.BULLET;
        }

        return Elements.BATTLE_GROUND;
    }
}
