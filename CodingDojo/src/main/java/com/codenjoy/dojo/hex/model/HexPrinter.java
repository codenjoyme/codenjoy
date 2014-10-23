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
    private List<Player> players;
    private List<Point> walls;

    public HexPrinter(Hex game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public boolean init() {
        heroes = game.getHeroes();
        walls = game.getWalls();
        players = game.getPlayers();
        return true;
    }

    @Override
    public Elements get(Point pt) {
        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            for (Player otherPayer : players) {
                if (otherPayer.getHeroes().contains(hero)) {
                    return otherPayer.getElement();
                }
            }
        }

        if (walls.contains(pt)) return Elements.WALL;

        return Elements.NONE;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать этот метод
    }
}
