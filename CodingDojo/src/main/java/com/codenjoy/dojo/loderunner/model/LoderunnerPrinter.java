package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:58
 */
public class LoderunnerPrinter implements GamePrinter {
    private Loderunner game;
    private Player player;

    private List<Hero> heroes;
    private List<Point> gold;
    private List<Enemy> enemies;

    public LoderunnerPrinter(Loderunner game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void init() {
        heroes = game.getHeroes();
        gold = game.getGold();
        enemies = game.getEnemies();
    }

    @Override
    public Enum get(Point pt) {
        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            Elements heroState = hero.state();
            if (player.getHero() == hero) {
                return heroState;
            } else {
                return Elements.forOtherHero(heroState);
            }
        }

        if (enemies.contains(pt)) {
            Enemy enemy = enemies.get(enemies.indexOf(pt));
            return enemy.state();
        }

        if (game.is(pt, Ladder.class)) {
            return Elements.LADDER;
        }

        if (game.is(pt, Pipe.class)) {
            return Elements.PIPE;
        }

        if (gold.contains(pt)) {
            return Elements.GOLD;
        }

        Point bottom = Direction.DOWN.change(pt);
        if (game.is(bottom, Brick.class)) {
            Brick brick = (Brick)game.getAt(bottom);
            if (brick.state() == Elements.DRILL_PIT) {
                return Elements.DRILL_SPACE;
            }
        }

        if (game.is(pt, Brick.class)) {
            Brick brick = (Brick)game.getAt(pt);
            return brick.state();
        }

        if (game.is(pt, Border.class)) {
            return Elements.UNDESTROYABLE_WALL;
        }

        return Elements.NONE;
    }
}
