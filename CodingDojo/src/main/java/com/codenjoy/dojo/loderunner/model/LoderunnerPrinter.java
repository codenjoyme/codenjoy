package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:58
 */
public class LoderunnerPrinter implements GamePrinter {
    private Loderunner game;

    public LoderunnerPrinter(Loderunner game, Player player) {
        this.game = game;
    }

    @Override
    public Enum get(int x, int y) {
        Point pt = pt(x, y);

        List<Hero> heroes = game.getHeroes();
        List<Point> ladder = game.getLadder();
        if (ladder.contains(pt)) {
            if (heroes.contains(pt)) {
                return Elements.HERO_LADDER;
            } else {
                return Elements.LADDER;
            }
        }

        List<Point> pipe = game.getPipe();
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

        List<Point> gold = game.getGold();
        if (gold.contains(pt)) {
            return Elements.GOLD;
        }

        List<Brick> bricks = game.getBricks();
        Point bottom = pt(x, y - 1);
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
