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
    private Player player;

    private List<Hero> heroes;
    private List<Point> walls;

    public HexPrinter(Hex game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void init() {
        heroes = game.getHeroes();
        walls = game.getWalls();
    }

    @Override
    public Elements get(Point pt) {
        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            if (player.getHeroes().contains(hero)) {
                return Elements.HERO1;
            } else {
                return Elements.HERO2;
            }
        }

        if (walls.contains(pt)) return Elements.WALL;

        return Elements.NONE;
    }
}
