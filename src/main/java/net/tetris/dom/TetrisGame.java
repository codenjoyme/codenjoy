package net.tetris.dom;

/**
 * @author serhiy.zelenin
 */
public class TetrisGame {

    private final GameConsole console;
    private FigureQueue queue;
    private ScoreBoard scoreBoard;
    private Glass glass;
    private int x;
    private int y;
    private Figure currentFigure;
    private boolean dropRequested;

    public TetrisGame(GameConsole console, FigureQueue queue, ScoreBoard scoreBoard, Glass glass) {
        this.console = console;
        this.queue = queue;
        this.scoreBoard = scoreBoard;
        this.glass = glass;
        takeFigure();
    }

    private void takeFigure() {
        x = 4;
        currentFigure = queue.next();
        y = 20 - currentFigure.getTop();
        showCurrentFigure();
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
        if (dropRequested) {
            dropRequested = false;
            glass.drop(currentFigure, x, y);
            currentFigure = null;
            if (!glass.accept(currentFigure, x, y)) {
                scoreBoard.glassOverflown();
                glass.empty();
            }
            return;
        }
        if (!glass.accept(currentFigure, x, y - 1)) {
            currentFigure = null;
            return;
        }
        y--;
        showCurrentFigure();
    }

    private void showCurrentFigure() {
        console.figureAt(currentFigure, x, y);
    }


    public void drop() {
        dropRequested = true;
    }
}
