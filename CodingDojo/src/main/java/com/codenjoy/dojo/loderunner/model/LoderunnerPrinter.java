package com.codenjoy.dojo.loderunner.model;

import com.codenjoy.dojo.services.GamePrinter;
import com.codenjoy.dojo.services.Point;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:58
 */
public class LoderunnerPrinter implements GamePrinter {
    private Loderunner board;
    private Player player;

    public LoderunnerPrinter(Loderunner board, Player player) {
        this.board = board;
        this.player = player;
    }

    @Override
    public boolean init() {
        return false;
    }

    @Override
    public Enum get(Point pt) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void printAll(Filler filler) {

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                filler.set(x, y, Elements.NONE);
            }
        }

        for (Point gold : board.getGold()) {
            filler.set(gold.getX(), gold.getY(), Elements.GOLD);
        }

        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                Point el = board.getAt(x, y);
                if (el == null) continue;

                Elements result;
                if (el instanceof Ladder) {
                    result = Elements.LADDER;
                } else if (el instanceof Pipe) {
                    result = Elements.PIPE;
                } else if (el instanceof Brick) {
                    Brick brick = (Brick) el;
                    result = brick.state();
                    if (result == Elements.DRILL_PIT) {
                        filler.set(x, y + 1, Elements.DRILL_SPACE);
                    }
                } else if (el instanceof Border) {
                    result = Elements.UNDESTROYABLE_WALL;
                } else {
                    continue;
                }

                filler.set(x, y, result);
            }
        }

        for (Enemy enemy : board.getEnemies()) {
            filler.set(enemy.getX(), enemy.getY(), enemy.state());
        }

        for (Hero hero : board.getHeroes()) {
            Elements heroState = hero.state();
            if (player.getHero() == hero) {
                filler.set(hero.getX(), hero.getY(), heroState);
            } else {
                filler.set(hero.getX(), hero.getY(), Elements.forOtherHero(heroState));
            }
        }


    }
}
