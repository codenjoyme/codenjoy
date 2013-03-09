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
    private Map<Point, BombermanPlotColor> plots = new HashMap<Point, BombermanPlotColor>();

    public BombermanPlotsBuilder(Board board) {
        this.board = board;
    }

    @Override
    public List<Plot> get() {
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
            if (element == BombermanPlotColor.BOMB5 || element == BombermanPlotColor.BOMB4 ||
                element == BombermanPlotColor.BOMB3 || element == BombermanPlotColor.BOMB2 ||
                element == BombermanPlotColor.BOMB1) {
                continue;
            } else if (element == BombermanPlotColor.BOMBERMAN ||
                    element == BombermanPlotColor.BOMB_BOMBERMAN ||
                    element == BombermanPlotColor.DEAD_BOMBERMAN)
            {
                plots.put(blast, BombermanPlotColor.DEAD_BOMBERMAN);
            } else if (element == BombermanPlotColor.MEAT_CHOPPER) {
                plots.put(blast, BombermanPlotColor.DEAD_MEAT_CHOPPER);  // TODO implement me
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
                    case 5 : plots.put(bomb, BombermanPlotColor.BOMB5); break;
                    case 4 : plots.put(bomb, BombermanPlotColor.BOMB4); break;
                    case 3 : plots.put(bomb, BombermanPlotColor.BOMB3); break;
                    case 2 : plots.put(bomb, BombermanPlotColor.BOMB2); break;
                    case 1 : plots.put(bomb, BombermanPlotColor.BOMB1); break;
                }
            }
        }
    }

    private Plot getPlot(Point point, BombermanPlotColor color) {
        return new Plot(point.getX(), point.getY(), color);
    }

    private List<Plot> getPlots() {
        List<Plot> result = new LinkedList<Plot>();
        for (Map.Entry<Point,BombermanPlotColor> plot : plots.entrySet()) {
            result.add(getPlot(plot.getKey(), plot.getValue()));
        }
        return result;
    }
}
