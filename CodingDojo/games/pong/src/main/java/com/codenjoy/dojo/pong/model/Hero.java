package com.codenjoy.dojo.pong.model;

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

    }

    @Override
    public void right() {

    }

    @Override
    public void act(int... p) {

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
