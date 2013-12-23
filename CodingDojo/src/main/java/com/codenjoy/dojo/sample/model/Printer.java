package com.codenjoy.dojo.sample.model;

import com.codenjoy.dojo.services.Point;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:58
 */
public class Printer {
    private Sample game;
    private Player player;
    private Elements[][] field;
    private final int size;

    public Printer(Sample game, Player player) {
        this.game = game;
        this.player = player;
        size = game.getSize();
    }

    @Override
    public String toString() {
        fillField();

        String string = "";
        for (Elements[] currentRow : field) {
            for (Elements element : currentRow) {
                string += element.ch;
            }
            string += "\n";
        }
        return string;
    }

    private void fillField() {
        field = new Elements[size][size];

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                set(pt(x, y), Elements.NONE);
            }
        }

        for (Point wall : game.getWalls()) {
            set(wall, Elements.WALL);
        }

        for (Point gold : game.getGold()) {
            set(gold, Elements.GOLD);
        }

        for (Point gold : game.getBombs()) {
            set(gold, Elements.BOMB);
        }

        for (Hero hero : game.getHeroes()) {
            if (hero.isAlive()) {
                if (hero.equals(player.getHero())) {
                    set(hero, Elements.HERO);
                } else {
                    set(hero, Elements.OTHER_HERO);
                }
            } else {
                set(hero, Elements.DEAD_HERO);
            }
        }
    }

    private void set(Point pt, Elements element) {
        if (pt.getY() == -1 || pt.getX() == -1) {
            return;
        }

        field[size - 1 - pt.getY()][pt.getX()] = element;
    }
}
