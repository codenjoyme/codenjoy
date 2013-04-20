package com.codenjoy.dojo.bomberman.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 11:59 PM
 */
public class BoomEngineBad implements BoomEngine {

    private Bomberman bomberman;

    public BoomEngineBad(Bomberman bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends Point> barriers, int boardSize, Point source, int radius) {
        List<Blast> blasts = new LinkedList<Blast>();
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int x = source.getX() + dx;
                int y = source.getY() + dy;
                Point pt = new Point(x, y);

                if (!isOnBoard(pt, boardSize)) {
                    continue;
                }

                if (barriers.contains(pt)) {
                    continue;
                }

                blasts.add(new Blast(x, y, bomberman));
            }
        }
        return blasts;
    }

    private boolean isOnBoard(Point pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
