package net.tetris.dom;

/**
 * @author serhiy.zelenin
 */
public class TetrisGame {

    private final GameConsole console;
    private FigureQueue queue;
    private int x;
    private int y;
    private Figure currentFigure;

    public TetrisGame(GameConsole console, FigureQueue queue) {
        this.console = console;
        this.queue = queue;
        x = 4;
        y = 20;
        currentFigure = queue.next();
        this.console.figureAt(currentFigure, x, y);
    }

    public void moveLeft(int delta) {
        x = x-delta < currentFigure.getLeft()? currentFigure.getLeft(): x - delta;
    }

    public void nextStep() {
        y--;
        console.figureAt(currentFigure, x, y);
    }

    public void moveRight(int delta) {
        x = x+delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x+delta;
    }
}
