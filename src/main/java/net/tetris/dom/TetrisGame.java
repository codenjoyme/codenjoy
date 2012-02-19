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
        x = x-delta < currentFigure.getLeft()? currentFigure.getLeft(): x - delta;
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
        y--;
        showCurrentFigure();
    }

    private void showCurrentFigure() {
        console.figureAt(currentFigure, x, y);
    }

    public void moveRight(int delta) {
        x = x+delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x+delta;
    }

    public void drop() {
        dropRequested = true;
    }
}
