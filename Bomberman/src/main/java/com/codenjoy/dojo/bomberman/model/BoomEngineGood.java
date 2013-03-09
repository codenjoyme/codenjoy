package com.codenjoy.dojo.bomberman.model;

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

    @Override
    public List<Point> boom(List<Point> barriers, int boardSize, Point source, int radius) {
        radius = radius + 1; // TODO #1 подумать над этим - взрыв лезет между стенками
        List<Point> blasts = new LinkedList<Point>();
        double dn = 0.01d / radius;
        double n = 0;
        while (n < 2d * Math.PI) {
            int x = (int)Math.round (source.getX() + radius * Math.cos(n));
            int y = (int)Math.round (source.getY() + radius * Math.sin(n));

            List<Point> line = new Line().draw(source.getX(), source.getY(), x, y);
            for (Point barrier : barriers) {
                if (line.contains(barrier)) {
                    line = new Line().draw(source.getX(), source.getY(), barrier.getX(), barrier.getY());
                }
                line.remove(barrier);
            }
            removeFar(boardSize, source, line); // TODO #1 подумать над этим - оно обрезает возле стенки

            for (Point pt : line) {
                if (isOnBoard(pt, boardSize) && !blasts.contains(pt) && !barriers.contains(pt)) {
                    blasts.add(pt);
                }
            }

            n = n + dn;
        }
        return blasts;
    }

    private void removeFar(int boardSize, Point source, List<Point> elements) {
        if (elements.size() == 0) {
            return;
        }
        Collections.sort(elements, new Comparator<Point>() {
            @Override
            public int compare(Point o1, Point o2) {
                double a = o1.getX() - o2.getX();
                double a2 = a*a;

                double b = o1.getY() - o2.getY();
                double b2 = b*b;

                double c2 = a2 + b2;
                return (int)c2;
            }
        });
        Point point = elements.get(elements.size() - 1);
        // TODO #1 очень некрасивый хак
        if (point.getX() <= 1 || point.getY() <= 1 || point.getX() >= boardSize - 2 || point.getY() >= boardSize - 2) {

        } else {
            elements.remove(elements.size() - 1);
        }
    }

    private boolean isOnBoard(Point pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
