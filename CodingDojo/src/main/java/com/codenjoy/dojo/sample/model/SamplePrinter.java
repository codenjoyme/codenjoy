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
    public boolean init() {
        heroes = game.getHeroes();
        gold = game.getGold();
        bombs = game.getBombs();
        walls = game.getWalls();
        return true;
    }

    @Override
    public char get(Point pt) {
        if (gold.contains(pt)) return Elements.GOLD.ch;

        if (bombs.contains(pt)) return Elements.BOMB.ch;

        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            if (!hero.isAlive()) {
                return Elements.DEAD_HERO.ch;
            }

            if (hero.equals(player.getHero())) {
                return Elements.HERO.ch;
            } else {
                return Elements.OTHER_HERO.ch;
            }
        }

        if (walls.contains(pt)) return Elements.WALL.ch;

        return Elements.NONE.ch;
    }

    @Override
    public void printAll(Filler filler) {
        // TODO использовать этот метод
    }
}
