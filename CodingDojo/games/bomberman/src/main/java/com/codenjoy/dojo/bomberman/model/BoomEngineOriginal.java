package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 11:59 PM
 */
public class BoomEngineOriginal implements BoomEngine {

    private Hero bomberman;

    public BoomEngineOriginal(Hero bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends PointImpl> barriers, int boardSize, PointImpl source, int radius) {
        List<Blast> blasts = new LinkedList<Blast>();

        add(barriers, boardSize, blasts, source.getX(), source.getY());

        for (int dx = 1; dx <= radius; dx++) {
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

    private boolean add(List<? extends PointImpl> barriers, int boardSize, List<Blast> blasts, int x, int y) {
        PointImpl pt = new PointImpl(x, y);

        if (!isOnBoard(pt, boardSize)) {
            return false;
        }

        if (barriers.contains(pt)) {
            if (!barriers.get(barriers.indexOf(pt)).getClass().equals(Wall.class)) {    // TODO немного жвачка
                blasts.add(new Blast(x, y, bomberman));
            }
            return false;
        }

        blasts.add(new Blast(x, y, bomberman));
        return true;
    }

    private boolean isOnBoard(PointImpl pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
