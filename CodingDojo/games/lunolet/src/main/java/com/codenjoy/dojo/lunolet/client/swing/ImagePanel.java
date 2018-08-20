package com.codenjoy.dojo.lunolet.client.swing;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.lunolet.client.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImagePanel extends JPanel {

    private static final Font fontBoard = new Font("Monospaced", Font.PLAIN, 12);

    private Image image;

    public ImagePanel() {
        image = new BufferedImage(600, 400, BufferedImage.TYPE_INT_RGB);
    }

    public void drawBoard(Board board) {
        Graphics g = image.getGraphics();
        g.setFont(fontBoard);
        Graphics2D g2 = (Graphics2D) g;

        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 600, 400);  // clear the canvas

        g.setColor(Color.BLACK);
        g.drawString("TIME " + board.getTime(), 50, 30);
        g.drawString("FUEL " + board.getFuelMass(), 50, 45);
        g.drawString("STATE " + board.getState().toString(), 50, 60);
        g.drawString("XPOS " + board.getPoint().getX(), 200, 30);
        g.drawString("YPOS " + board.getPoint().getY(), 200, 45);
        g.drawString("HSPEED " + board.getHSpeed(), 350, 30);
        g.drawString("VSPEED " + board.getVSpeed(), 350, 45);
        if (board.getHSpeed() >= 0.001) {
            g.drawString("→", 500, 30);
        } else if (board.getHSpeed() <= -0.001) {
            g.drawString("←", 500, 30);
        }
        if (board.getVSpeed() >= 0.001) {
            g.drawString("↑", 500, 45);
        } else if (board.getVSpeed() <= -0.001) {
            g.drawString("↓", 500, 45);
        }

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // scale, move center to (300, 200), and flip vertically
        double scale = 6;
        double xshift = 300 - board.getPoint().getX() * scale;
        double yshift = 200 + board.getPoint().getY() * scale;
        g2.translate(xshift, yshift);
        g2.scale(scale / 100.0, -scale / 100.0);
        g2.setStroke(new BasicStroke((float) (100.0 / scale)));

        // draw relief
        g.setColor(Color.BLACK);
        ArrayList<Point2D.Double> relief = board.getRelief();
        if (relief != null && relief.size() > 0) {
            for (int i = 1; i < relief.size(); i++) {
                Point2D.Double pt1 = relief.get(i - 1);
                Point2D.Double pt2 = relief.get(i);
                g.drawLine((int) (pt1.x * 100.0), (int) (pt1.y * 100.0), (int) (pt2.x * 100.0), (int) (pt2.y * 100.0));
            }
        }

        g.setColor(Color.GREEN);
        ArrayList<Point2D.Double> history = board.getHistory();
        if (history != null && history.size() > 0) {
            Point2D.Double pt1 = history.get(0);
            for (int i = 1; i < history.size(); i++) {
                Point2D.Double pt2 = history.get(i);
                g.drawLine((int) (pt1.x * 100.0), (int) (pt1.y * 100.0), (int) (pt2.x * 100.0), (int) (pt2.y * 100.0));
            }
        }

        // draw target (same transform)
        Point2D.Double target = board.getTarget();
        if (target != null) {
            g.setColor(Color.RED);
            g2.setStroke(new BasicStroke((float) (125.0 / scale)));
            g.drawLine((int) (target.x * 100.0), (int) (target.y * 100.0 - 100), (int) (target.x * 100.0), (int) (target.y * 100.0 + 100));
            g.drawLine((int) (target.x * 100.0 - 100), (int) (target.y * 100.0), (int) (target.x * 100.0 + 100), (int) (target.y * 100.0));
        }

        // draw the ship
        g.setColor(Color.BLUE);
        g2.setStroke(new BasicStroke((float) (12.0 / scale)));
        g2.setTransform(new AffineTransform());
        g2.translate(300, 200);
        g2.scale(scale / 10, -scale / 10);
        g2.rotate(-board.getAngle() / 180.0 * Math.PI);
        g.drawLine(0, 0, -10, -2);
        g.drawLine(-10, -2, -7, 11);
        g.drawLine(-7, 11, 0, 16);
        g.drawLine(0, 16, 7, 11);
        g.drawLine(7, 11, 10, -2);
        g.drawLine(10, -2, 0, 0);

        // draw arrow pointing to target
        if (target != null) {
            g.setColor(Color.RED);
            double deltaX = target.x - board.getPoint().x;
            double deltaY = target.y - board.getPoint().y;
            double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
            if (distance > 1) {
                double radian = Math.atan2(deltaY, deltaX); // In radians
                g2.setTransform(new AffineTransform());
                g2.translate(300, 100);
                g2.rotate(-radian);
                g2.scale(1.0, -1.0);
                g2.setStroke(new BasicStroke((float) 1.0));
                g.drawLine(-30, 0, 0, 0);
                g.drawLine(30, 0, 0, 5);
                g.drawLine(0, 5, 0, -5);
                g.drawLine(0, -5, 30, 0);
            }
        }

        g2.dispose();

        this.repaint();
    }

    public void paintComponent(Graphics g) {
        // Draws the image to the canvas
        g.drawImage(image, 0, 0, image.getWidth(null), image.getHeight(null), null);
    }
}
