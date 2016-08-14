package net.tetris.dom;

import com.codenjoy.dojo.services.PrinterFactory;
import com.codenjoy.dojo.services.PrinterFactoryImpl;
import com.codenjoy.dojo.tetris.model.FigureQueue;
import com.codenjoy.dojo.tetris.model.Glass;
import com.codenjoy.dojo.tetris.model.TetrisGame;

/**
 * @author serhiy.zelenin
 */
public class TetrisAdvancedGame extends TetrisGame implements TetrisJoystik, Cloneable {

    public TetrisAdvancedGame(FigureQueue queue, Glass glass, PrinterFactory printerFactory) {
        super(queue, glass, printerFactory);
    }

    @Override
    public void left(int delta) {
        moveHorizontallyIfAccepted(x - delta < currentFigure.getLeft() ? currentFigure.getLeft() : x - delta);
    }

    @Override
    public void right(int delta) {
        moveHorizontallyIfAccepted(x + delta > 9 - currentFigure.getRight() ? 9 - currentFigure.getRight() : x + delta);
    }


}
