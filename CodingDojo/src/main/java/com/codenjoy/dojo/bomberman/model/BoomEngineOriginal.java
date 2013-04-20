package com.codenjoy.dojo.bomberman.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 11:59 PM
 */
public class BoomEngineOriginal implements BoomEngine {

    private Bomberman bomberman;

    public BoomEngineOriginal(Bomberman bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends Point> barriers, int boardSize, Point source, int radius) {
        List<Blast> blasts = new LinkedList<Blast>();
        for (int dx = 0; dx <= radius; dx++) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dx = -1; dx >= -radius; dx--) {
            int x = source.getX() + dx;
            int y = source.getY() + 0;
            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dy = 1; dy <= radius; dy++) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        for (int dy = -1; dy >= -radius; dy--) {
            int x = source.getX() + 0;
            int y = source.getY() + dy;

            if (!add(barriers, boardSize, blasts, x, y)) {
                break;
            }
        }

        return blasts;
    }

    private boolean add(List<? extends Point> barriers, int boardSize, List<Blast> blasts, int x, int y) {
        Point pt = new Point(x, y);

        if (!isOnBoard(pt, boardSize)) {
            return false;
        }

        if (barriers.contains(pt)) {
            return false;
        }

        blasts.add(new Blast(x, y, bomberman));
        return true;
    }

    private boolean isOnBoard(Point pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
