package com.codenjoy.bomberman.model;

import java.util.LinkedList;
import java.util.List;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 10:56 PM
 */
public class BoomEngine {

    public static List<Point> boom(List<Point> barriers, int boardSize, Point source, int radius) {
        List<Point> container = new LinkedList<Point>();
        double dn = 0.01d / radius;
        double n = 0;
        while (n < 2d * Math.PI) {
            int x = (int) (source.getX() + radius * Math.cos(n));
            int y = (int) (source.getY() + radius * Math.sin(n));

            List<Point> line = new Line().draw(source.getX(), source.getY(), x, y);
            for (Point barrier : barriers) {
                if (line.contains(barrier)) {
                    line = new Line().draw(source.getX(), source.getY(), barrier.getX(), barrier.getY());
                }
            }

            for (Point pt : line) {
                if (isOnBoard(pt, boardSize) && !container.contains(pt) && !barriers.contains(pt)) {
                    container.add(pt);
                }
            }

            n = n + dn;
        }
        return container;
    }

    private static boolean isOnBoard(Point pt, int boardSize) {
        return pt.getX() >= 0 && pt.getY() >= 0 && pt.getX() < boardSize && pt.getY() < boardSize;
    }
}
