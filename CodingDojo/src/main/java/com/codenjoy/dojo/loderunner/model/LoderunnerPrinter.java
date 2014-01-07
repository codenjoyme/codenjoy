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

    private List<Hero> heroes;
    private List<Point> ladder;
    private List<Point> pipe;
    private List<Point> gold;
    private List<Brick> bricks;
    private List<Enemy> enemies;

    public LoderunnerPrinter(Loderunner game, Player player) {
        this.game = game;
    }

    @Override
    public void init() {
        heroes = game.getHeroes();
        ladder = game.getLadder();
        pipe = game.getPipe();
        gold = game.getGold();
        bricks = game.getBricks();
        enemies = game.getEnemies();
    }

    @Override
    public Enum get(Point pt) {
        if (ladder.contains(pt)) {
            if (heroes.contains(pt)) {
                return Elements.HERO_LADDER;
            } else {
                return Elements.LADDER;
            }
        }

        if (pipe.contains(pt)) {
            if (heroes.contains(pt)) {
                Hero hero = heroes.get(heroes.indexOf(pt));
                if (hero.getDirection().equals(Direction.LEFT)) {
                    return Elements.HERO_PIPE_LEFT;
                } else {
                    return  Elements.HERO_PIPE_RIGHT;
                }
            } else {
                return Elements.PIPE;
            }
        }

        if (heroes.contains(pt)) {
            Hero hero = heroes.get(heroes.indexOf(pt));
            return hero.state();
        }

        if (enemies.contains(pt)) {
            Enemy enemy = enemies.get(enemies.indexOf(pt));
            return enemy.state();
        }

        if (gold.contains(pt)) {
            return Elements.GOLD;
        }

        Point bottom = Direction.DOWN.change(pt);
        if (bricks.contains(bottom)) {
            Brick brick = bricks.get(bricks.indexOf(bottom));
            if (brick.state() == Elements.DRILL_PIT) {
                return Elements.DRILL_SPACE;
            }
        }

        if (bricks.contains(pt)) {
            int index = bricks.indexOf(pt);
            Brick brick = bricks.get(index);
            return brick.state();
        }

        if (game.getBorders().contains(pt)) {
            return Elements.UNDESTROYABLE_WALL;
        }

        return Elements.NONE;
    }
}
