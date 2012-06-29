package net.tetris.dom;

/**
 * @author serhiy.zelenin
 */
public class TetrisGame implements Joystick, Cloneable {

    public static final int GLASS_HEIGHT = 20;
    public static final int GLASS_WIDTH = 10;
    private FigureQueue queue;
    private ScoreBoard scoreBoard;
    private Glass glass;
    private int x;
    private int y;
    private Figure currentFigure;
    private boolean dropRequested;

    public TetrisGame(FigureQueue queue, ScoreBoard scoreBoard, Glass glass) {
        this.queue = queue;
        this.scoreBoard = scoreBoard;
        this.glass = glass;
        takeFigure();
    }

    private void takeFigure() {
        x = GLASS_WIDTH /2 - 1;
        currentFigure = queue.next();
        y = initialYPosition();
        showCurrentFigure();
    }

    private int initialYPosition() {
        return GLASS_HEIGHT - currentFigure.getTop();
    }

    @Override
    public void moveLeft(int delta) {
        moveHorizontallyIfAccepted(x - delta < currentFigure.getLeft() ? currentFigure.getLeft() : x - delta);
    }

    private void moveHorizontallyIfAccepted(int tmpX) {
        if (glass.accept(currentFigure, tmpX, y)) {
            x = tmpX;
        }
    }

    @Override
    public void moveRight(int delta) {
        moveHorizontallyIfAccepted(x + delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x + delta);
    }

    public void nextStep() {
        if (theFirstStep()) {
            takeFigure();
            return;
        }

        if (!glass.accept(currentFigure, x, y)) {
            scoreBoard.glassOverflown();
            glass.empty();
            currentFigure = null;
            return;
        }

        if (dropRequested) {
            dropRequested = false;
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            return;
        }
        if (!glass.accept(currentFigure, x, y - 1)) {
            glass.drop(currentFigure, x, y);
            currentFigure = null;
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


    @Override
    public void drop() {
        dropRequested = true;
    }

    @Override
    public void rotate(int times) {
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
}
