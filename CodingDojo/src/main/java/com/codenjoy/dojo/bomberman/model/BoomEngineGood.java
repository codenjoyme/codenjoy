package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.services.PointImpl;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 10:56 PM
 */
public class BoomEngineGood implements BoomEngine {

    private Bomberman bomberman;

    public BoomEngineGood(Bomberman bomberman) {
        this.bomberman = bomberman;
    }

    @Override
    public List<Blast> boom(List<? extends PointImpl> barriers, int boardSize, PointImpl source, int radius) {
        List<Blast> blasts = new LinkedList<Blast>();
        double dn = 0.01d / radius;
        double n = 0;
        while (n < 2d * Math.PI) {
            int x = (int)Math.round (source.getX() + radius * Math.cos(n));
            int y = (int)Math.round (source.getY() + radius * Math.sin(n));

            List<PointImpl> line = new Line().draw(source.getX(), source.getY(), x, y);
            for (PointImpl barrier : barriers) {
                if (line.contains(barrier)) {
                    line = new Line().draw(source.getX(), source.getY(), barrier.getX(), barrier.getY());
                }
                line.remove(barrier);
            }
            //removeFar(boardSize, source, line); // TODO #1 подумать над этим - оно обрезает возле стенки
            //removeFar(boardSize, source, line);

            for (PointImpl pt : line) {
                if (isOnBoard(pt, boardSize) && !blasts.contains(pt) && !barriers.contains(pt)) {
                    blasts.add(new Blast(pt.getX(), pt.getY(), bomberman));
                }
            }

            n = n + dn;
        }
        return blasts;
    }

    private void removeFar(int boardSize, PointImpl source, List<PointImpl> elements) {
        if (elements.size() == 0) {
            return;
        }
        Collections.sort(elements, new Comparator<PointImpl>() {
            @Override
            public int compare(PointImpl o1, PointImpl o2) {
                double a = o1.getX() - o2.getX();
                double a2 = a*a;

                double b = o1.getY() - o2.getY();
                double b2 = b*b;

                double c2 = a2 + b2;
                return (int)c2;
            }
        });
        PointImpl point = elements.get(elements.size() - 1);
        // TODO #1 очень некрасивый хак
//        if (point.getX() <= 1 || point.getY() <= 1 || point.getX() >= boardSize - 2 || point.getY() >= boardSize - 2) {
//
//        } else {
            elements.remove(elements.size() - 1);
//        }
    }

    private boolean isOnBoard(PointImpl pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
