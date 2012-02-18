package net.tetris.dom;

/**
 * @author serhiy.zelenin
 */
public class TetrisGame {
    public TetrisGame(GameConsole console) {
        console.figureAt(new Figure() {
        }, 5, 20);
    }
}
