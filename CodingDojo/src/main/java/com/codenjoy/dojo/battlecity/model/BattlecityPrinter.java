package com.codenjoy.dojo.battlecity.model;

import com.codenjoy.dojo.bomberman.model.Wall;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattlecityPrinter implements GamePrinter {

    private int size;
    private Field board;
    private Player player;
    private Point[][] field;

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


    public BattlecityPrinter(Field board, Player player) {
        this.board = board;
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
    public boolean init() {
        size = board.size();
        field = new Point[size][size];

        addAll(board.getBorders());
        addAll(board.getBullets());
        addAll(board.getConstructions());
        addAll(board.getTanks());

        return false;
    }

    private void addAll(List<? extends Point> elements) {
        for (Point el : elements) {
            field[el.getX()][el.getY()] = el;
        }
    }

    @Override
    public Enum get(Point pt) {
        return Elements.BATTLE_GROUND;
    }

    @Override
    public void printAll(Filler filler) {
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Point el = field[x][y];

                if (el instanceof Bullet) {
                    Bullet bullet = (Bullet)el;
                    if (bullet.destroyed()) {
                        filler.set(x, y, Elements.BANG);
                    } else {
                        filler.set(x, y, Elements.BULLET);
                    }
                    continue;
                }

                if (el instanceof Tank) {
                    Tank tank = (Tank)el;

                    if (tank.isAlive()) {
                        filler.set(x, y, getTankElement(tank));
                    } else {
                        filler.set(x, y, Elements.BANG);
                    }
                    continue;
                }


                if (el instanceof Border) {
                    filler.set(x, y, Elements.BATTLE_WALL);
                    continue;
                }

                if (el instanceof Construction) {
                    Construction construction = (Construction)el;
                    if (!construction.destroyed()) {
                        filler.set(x, y, construction.state());
                        continue;
                    }
                }

                filler.set(x, y, Elements.BATTLE_GROUND);
            }
        }
    }
}
