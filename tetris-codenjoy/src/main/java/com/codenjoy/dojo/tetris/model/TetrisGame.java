package com.codenjoy.dojo.tetris.model;

import com.codenjoy.dojo.services.*;

import java.util.List;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class TetrisGame implements Joystick, Game {
    public static final int GLASS_HEIGHT = 20;
    public static final int GLASS_WIDTH = 10;
    protected Glass glass;
    protected int x;
    protected Figure currentFigure;
    protected FigureQueue queue;
    private int y;
    private boolean dropRequested;

    public TetrisGame(FigureQueue queue, Glass glass, PrinterFactory factory) {
        this.glass = glass;
        this.queue = queue;
        takeFigure();
    }

    protected void takeFigure() {
        x = GLASS_WIDTH /2 - 1;
        currentFigure = queue.next();
        y = initialYPosition();
        showCurrentFigure();
    }

    private int initialYPosition() {
        return GLASS_HEIGHT - currentFigure.getTop();
    }

    protected void moveHorizontallyIfAccepted(int tmpX) {
        if (glass.accept(currentFigure, tmpX, y)) {
            x = tmpX;
        }
    }

    public void tick() {
        if (theFirstStep()) {
            takeFigure();
            return;
        }

        if (!glass.accept(currentFigure, x, y)) {
            glass.empty();
            currentFigure = null;
            tick();
            return;
        }

        if (dropRequested) {
            dropRequested = false;
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            tick();
            return;
        }
        if (!glass.accept(currentFigure, x, y - 1)) {
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            tick();
            return;
        }
        y--;
        showCurrentFigure();
    }

    private boolean theFirstStep() {
        return currentFigure == null;
    }

    private void showCurrentFigure() {
        glass.figureAt(currentFigure, x, y);
    }

    public void down() {
        dropRequested = true;
    }

    @Override
    public void up() {

    }

    @Override
    public void left() {
        moveHorizontallyIfAccepted(x - 1 < currentFigure.getLeft() ? currentFigure.getLeft() : x - 1);
    }

    @Override
    public void right() {
        moveHorizontallyIfAccepted(x + 1 > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x + 1);
    }

    @Override
    public void act(int... p) {

    }

    public void act(int times) {
        Figure clonedFigure = currentFigure.getCopy();

        currentFigure.rotate(times);
        if (!glass.accept(currentFigure, x, y)) {
            currentFigure = clonedFigure;
        }
        glass.figureAt(currentFigure, x, y);
    }

    public Figure.Type getCurrentFigureType() {
        if (currentFigure == null) {
            return null;
        }
        return currentFigure.getType();
    }

    public int getCurrentFigureX() {
        return x;
    }

    public int getCurrentFigureY() {
        return y;
    }

    public List<Figure.Type> getFutureFigures() {
        return queue.getFutureFigures();
    }

    @Override
    public Joystick getJoystick() {
        return this;
    }

    @Override
    public int getMaxScore() {
        return 0;
    }

    @Override
    public int getCurrentScore() {
        return 0;
    }

    @Override
    public boolean isGameOver() {
        return false;
    }

    @Override
    public void newGame() {

    }

    @Override
    public String getBoardAsString() {
        return null;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void clearScore() {

    }

    @Override
    public Point getHero() {
        return null;
    }
}
