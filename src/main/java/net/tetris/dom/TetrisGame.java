package net.tetris.dom;

/**
 * @author serhiy.zelenin
 */
public class TetrisGame {

    public static final int GLASS_HEIGHT = 20;
    public static final int GLASS_WIDTH = 10;
    private final Console console;
    private FigureQueue queue;
    private ScoreBoard scoreBoard;
    private Glass glass;
    private int x;
    private int y;
    private Figure currentFigure;
    private boolean dropRequested;

    public TetrisGame(Console console, FigureQueue queue, ScoreBoard scoreBoard, Glass glass) {
        this.console = console;
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

    public void moveLeft(int delta) {
        moveHorizontallyIfAccepted(x - delta < currentFigure.getLeft() ? currentFigure.getLeft() : x - delta);
    }

    private void moveHorizontallyIfAccepted(int tmpX) {
        if (glass.accept(currentFigure, tmpX, y)) {
            x = tmpX;
        }
    }

    public void moveRight(int delta) {
        moveHorizontallyIfAccepted(x + delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x + delta);
    }

    public void nextStep() {
        if (currentFigure == null) {
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

    private void showCurrentFigure() {
        console.drawGlass(glass);
        console.figureAt(currentFigure, x, y);
    }


    public void drop() {
        dropRequested = true;
    }
}
