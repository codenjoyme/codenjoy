package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.PointImpl;

import java.util.List;

/**
 * User: sanja
 * Date: 24.12.13
 * Time: 3:41
 */
public class SamplePrinter implements GamePrinter {

    private final Sample game;
    private Player player;

    public SamplePrinter(Sample game, Player player) {
        this.player = player;
        this.game = game;
    }

    @Override
    public Elements get(int x, int y) {
        List<Hero> heroes = game.getHeroes();  // TODO подумать на досуге, как снизить количество вычислений тут
        List<Point> gold = game.getGold();
        List<Point> bombs = game.getBombs();
        List<Point> walls = game.getWalls();

        Point pt = PointImpl.pt(x, y);

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
