package com.codenjoy.dojo.pong.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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

import com.codenjoy.dojo.services.*;

import java.util.LinkedList;
import java.util.List;

public class Hero extends PointImpl implements Joystick, Tickable, State<Elements, Player> {

    private Field field;
    private Direction direction;
    List<Panel> panel = new LinkedList<>();

    public Hero(Point xy) {
        super(xy);
        // hero - panel of three parts
        panel.add(new Panel(x, y-1, this));
        panel.add(new Panel(x, y, this));
        panel.add(new Panel(x, y+1, this));

        direction = null;
    }

    public void init(Field field) {
        this.field = field;
    }

    @Override
    public void down() {
        direction = Direction.DOWN;
    }

    @Override
    public void up() {
        direction = Direction.UP;
    }

    @Override
    public void left() {
        // do nothing, this should never happen
    }

    @Override
    public void right() {
        // do nothing, this should never happen
    }

    @Override
    public void act(int... p) {
        // do nothing, this should never happen
    }

    @Override
    public void message(String command) {
        // do nothing, this should never happen
    }

    public Direction getDirection() {
        return direction;
    }

    @Override
    public void tick() {

        if (direction != null) {
            changePanelPosition(direction);
        }

        direction = null;

    }

    private void changePanelPosition(Direction direction) {
        Panel bottomPiece = panel.get(0);
        Panel topPiece = panel.get(panel.size()-1);
        Panel headPiece = direction == Direction.DOWN? bottomPiece : topPiece;
        if (isValidDirection(headPiece, direction)) {
            for (Panel panelPiece : panel) {
                int x = panelPiece.getX();
                int y = panelPiece.getY();
                int newY = direction.changeY(y);
                panelPiece.move(x, newY);
            }
        }
    }

    private boolean isValidDirection(Panel headPiece, Direction direction) {

        int headPieceX = headPiece.getX();
        int headPieceY = headPiece.getY();
        int newHeadPieceY = direction.changeY(headPieceY);

        return (!(field.isBarrier(headPieceX, newHeadPieceY)));
    }

    public List<Panel> getPanel() {
        return panel;
    }

    @Override
    public Elements state(Player player, Object... alsoAtPoint) {
        return null;
    }
}
