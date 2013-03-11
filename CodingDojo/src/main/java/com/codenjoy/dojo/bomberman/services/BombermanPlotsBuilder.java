package com.codenjoy.dojo.bomberman.services;

import com.codenjoy.dojo.bomberman.model.*;
import com.codenjoy.dojo.services.Plot;
import com.codenjoy.dojo.services.playerdata.PlotsBuilder;

import java.util.*;

/**
 * User: oleksandr.baglai
 * Date: 3/9/13
 * Time: 8:13 AM
 */
public class BombermanPlotsBuilder implements PlotsBuilder {      // TODO test me
    private Board board;
    private Map<Point, BombermanPlotColor> plots;

    public BombermanPlotsBuilder(Board board) {
        this.board = board;
        plots = new HashMap<Point, BombermanPlotColor>();
    }

    private void printEmpty() {
        for (int x = 0; x < board.size(); x++) {
            for (int y = 0; y < board.size(); y++) {
                plots.put(new Point(x, y), BombermanPlotColor.EMPTY);
            }
        }
    }

    @Override
    public List<Plot> get() {
        printEmpty();
        printBomberman(board.getBomberman());
        printBombs(board.getBombs());
        printWall(board.getWalls());
        printBlasts(board.getBlasts());

        return getPlots();
    }

    private void printBomberman(Bomberman bomberman) {
        plots.put(new Point(bomberman.getX(), bomberman.getY()), BombermanPlotColor.BOMBERMAN);
    }


    private void printWall(Walls walls) {
        for (Wall wall : walls) {
            if (wall instanceof DestroyWall) {
                plots.put(wall, BombermanPlotColor.DESTROY_WALL);
            } else if (wall instanceof MeatChopper) {
                BombermanPlotColor element = plots.get(wall);
                if (element == BombermanPlotColor.BOMBERMAN) {
                    plots.put(wall, BombermanPlotColor.DEAD_BOMBERMAN);
                } else {
                    plots.put(wall, BombermanPlotColor.MEAT_CHOPPER);
                }
            } else {
                plots.put(wall, BombermanPlotColor.WALL);
            }
        }
    }

    private void printBlasts(List<Point> blasts) {
        for (Point blast : blasts) {
            BombermanPlotColor element = plots.get(blast);
            if (element == BombermanPlotColor.BOMB_FIVE || element == BombermanPlotColor.BOMB_FOUR ||
                element == BombermanPlotColor.BOMB_THREE || element == BombermanPlotColor.BOMB_TWO ||
                element == BombermanPlotColor.BOMB_ONE) {
                continue;
            } else if (element == BombermanPlotColor.BOMBERMAN ||
                    element == BombermanPlotColor.BOMB_BOMBERMAN ||
                    element == BombermanPlotColor.DEAD_BOMBERMAN)
            {
                plots.put(blast, BombermanPlotColor.DEAD_BOMBERMAN);
            } else if (element == BombermanPlotColor.MEAT_CHOPPER) {
                plots.put(blast, BombermanPlotColor.DEAD_MEAT_CHOPPER);
            } else if (element == BombermanPlotColor.DESTROY_WALL) {
                plots.put(blast, BombermanPlotColor.DESTROYED_WALL);
            } else {
                plots.put(blast, BombermanPlotColor.BOOM);
            }
        }
    }

    private void printBombs(List<Bomb> bombs) {
        for (Bomb bomb : bombs) {
            BombermanPlotColor element = plots.get(bomb);
            if (element == BombermanPlotColor.BOMBERMAN) {
                plots.put(bomb, BombermanPlotColor.BOMB_BOMBERMAN);
            } else {
                switch (bomb.getTimer()) {
                    case 5 : plots.put(bomb, BombermanPlotColor.BOMB_FIVE); break;
                    case 4 : plots.put(bomb, BombermanPlotColor.BOMB_FOUR); break;
                    case 3 : plots.put(bomb, BombermanPlotColor.BOMB_THREE); break;
                    case 2 : plots.put(bomb, BombermanPlotColor.BOMB_TWO); break;
                    case 1 : plots.put(bomb, BombermanPlotColor.BOMB_ONE); break;
                }
            }
        }
    }

    private Plot getPlot(Point point, BombermanPlotColor color) {
        return new Plot(point.getX(), board.size() - point.getY() - 1, color);
    }

    private List<Plot> getPlots() {
        List<Plot> result = new LinkedList<Plot>();
        for (Map.Entry<Point,BombermanPlotColor> plot : plots.entrySet()) {
            result.add(getPlot(plot.getKey(), plot.getValue()));
        }
        return result;
    }
}
