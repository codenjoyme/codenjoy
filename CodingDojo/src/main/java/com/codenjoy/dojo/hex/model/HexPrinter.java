package com.codenjoy.dojo.hex.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class HexPrinter implements GamePrinter {

    private final Hex game;

    private List<Hero> heroes;
    private List<Point> walls;

    public HexPrinter(Hex game, Player player) {
        this.game = game;
    }

    @Override
    public void init() {
        heroes = game.getHeroes();
        walls = game.getWalls();
    }

    @Override
    public Elements get(Point pt) {
        if (heroes.contains(pt)) {
            return Elements.HERO;
        }

        if (walls.contains(pt)) return Elements.WALL;

        return Elements.NONE;
    }
}
