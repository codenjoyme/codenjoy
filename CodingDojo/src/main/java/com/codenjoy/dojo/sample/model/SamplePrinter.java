package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class SamplePrinter implements GamePrinter {

    private final Sample game;
    private Player player;

    private List<Hero> heroes;
    private List<Point> bombs;
    private List<Point> gold;
    private List<Point> walls;

    public SamplePrinter(Sample game, Player player) {
        this.player = player;
        this.game = game;
    }

    @Override
    public void init() {
        heroes = game.getHeroes();
        gold = game.getGold();
        bombs = game.getBombs();
        walls = game.getWalls();
    }

    @Override
    public Elements get(Point pt) {
        if (gold.contains(pt)) return Elements.GOLD;

        if (bombs.contains(pt)) return Elements.BOMB;

        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            if (!hero.isAlive()) {
                return Elements.DEAD_HERO;
            }

            if (hero.equals(player.getHero())) {
                return Elements.HERO;
            } else {
                return Elements.OTHER_HERO;
            }
        }

        if (walls.contains(pt)) return Elements.WALL;

        return Elements.NONE;
    }
}
