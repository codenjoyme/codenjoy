package net.tetris.dom;

import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.tetris.model.Figure;
import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.Glass;

import java.util.List;

/**
 * Created by Sergii_Zelenin on 7/10/2016.
 */
public class TetrisGame implements Joystick {
    public static final int GLASS_HEIGHT = 20;
    public static final int GLASS_WIDTH = 10;
    protected Glass glass;
    protected int x;
    protected Figure currentFigure;
    protected FigureQueue queue;
    private int y;
    private boolean dropRequested;

    public TetrisGame(FigureQueue queue, Glass glass) {
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

    public void nextStep() {
        if (theFirstStep()) {
            takeFigure();
            return;
        }

        if (!glass.accept(currentFigure, x, y)) {
            glass.empty();
            currentFigure = null;
            nextStep();
            return;
        }

        if (dropRequested) {
            dropRequested = false;
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            nextStep();
            return;
        }
        if (!glass.accept(currentFigure, x, y - 1)) {
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            nextStep();
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
}
