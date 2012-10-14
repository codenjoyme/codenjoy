import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Cell;
import com.globallogic.sapperthehero.game.Mine;
import com.globallogic.sapperthehero.game.Sapper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */

public class SapperTheHeroTest {


    private static final int MINES_COUNT = 4;
    public static final String GET_X_POSITION = "getXPosition";
    public static final String GET_Y_POSITION = "getYPosition";
    public static final int NUMBER_ZERO = 0;
    public static final int NUMBER_ONE = 1;
    private Board board;
    private List<Cell> freeCells;
    private List<Cell> boardCells;
    private static final int BOARD_SIZE = 4;
    private int boardSize;
    private Sapper sapper;
    private List<Mine> mines;

    @Before
    public void gameStart() {
        board = new Board(BOARD_SIZE, MINES_COUNT);
        freeCells = board.getFreeCells();
        boardCells = board.getBoardCells();
        boardSize = board.getBoardSize();
        sapper = board.getSapper();
        mines = board.getMines();
    }

    // Есть поле
    @Test
    public void shouldBoardOnStartGame() {
        assertNotNull(board);
    }

    //   поле состоит клеток.
    @Test
    public void shouldBoardConsistFromCells() {
        assertNotNull(boardCells);
    }

    //    клетка имеет координаты X и Y
//    направление оси X вправо, направление оси Y вниз, как в языке программирования
    @Test
    public void shouldCellContainXAndYCoordinates() {
        assertClassCellContainsCoordinates();
    }

    private void assertClassCellContainsCoordinates() {
        try {
            Cell.class.getMethod(GET_X_POSITION);
        } catch (NoSuchMethodException e) {
            assertFalse("Поле не имеет X коодринаты", true);
        }
        try {
            Cell.class.getMethod(GET_Y_POSITION);
        } catch (NoSuchMethodException e) {
            assertFalse("Поле не имеет Y коодринаты", true);
        }
    }

    //    Количество свободных клеток не нуль
    @Test
    public void shouldFreeCellsNumberBeMoreThanZero() {
        assertTrue(freeCells.size() > NUMBER_ZERO);
    }

    //  Размеры поля задаются перед игрой.
    @Test
    public void shouldBoardSizeSpecifyAtGameStart() {
        assertNotNull(boardSize);
    }

    // Поле квадратное.
    @Test
    public void shouldBoardBeSquare() {
        assertEquals("Поле не квадратное", boardCells.size() % boardSize, NUMBER_ZERO);
    }

    //  У поля больше одной клетки.
    @Test
    public void shouldBoardCellsNumberBeMoreThanOne() {
        assertTrue(boardCells.size() > NUMBER_ONE);
    }

    //        На поле появляется сапер.
    @Test
    public void shouldSapperOnBoard() {
        assertNotNull(sapper);
    }

    //сапер появляется на позиции 1, 1
    @Test
    public void shouldSapperBeAtBoardPositionOneOne() {
        assertEquals(sapper, new Cell(1, 1));
    }


    //        На поле появляются мины.
    @Test
    public void shouldMinesOnBoard() {
        assertNotNull(mines);
    }


    //  количество мин задается в начале игры
    @Test
    public void shouldMinesCountSpecifyAtGameStart() {
        assertNotNull(board.getMinesCount());
    }

    //  Мины появляются случайно. вероятность ~1/16
    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        assertTrue(assertMinesRandomPlacedOnBoard());
    }

    private boolean assertMinesRandomPlacedOnBoard() {
        for (int index = 0; index < 100; index++) {
            Board firstBoard = new Board(BOARD_SIZE, NUMBER_ONE);
            Board secondBoard = new Board(BOARD_SIZE, NUMBER_ONE);
            Mine mineFromFirstBoard = firstBoard.getMines().get(NUMBER_ONE - 1);
            Mine mineFromSecondBoard = secondBoard.getMines().get(NUMBER_ONE - 1);
            if (!mineFromFirstBoard.equals(mineFromSecondBoard)) {
                return true;
            }
        }
        return false;
    }

    // Когда появляются мины и сапер, то они занимают свободные клетки.
    @Test
    public void shouldFreeCellsDecreaseOnCreatingSapperAndMines() {
        assertEquals(boardCells.size(), freeCells.size() + mines.size() + 1);
    }


//        Сапер может двигаться по горизонтали, вертикали и диагонали.

//        Мины появляются случайно.
//                Смерть сапера значит конец игры.
//        Если сапер наступает на мину, то он умирает.
//        Сапер знает о количестве мин на поле.
//                У сапера есть чутье, и он знает, сколько вокруг мин.
//        У сапера есть детектор мин.
//                У детектора мин есть заряд батареи.
//                Заряд батареи больше чем количество мин на поле.
//                Сапер может проверить любую соседнюю клетку детектором мин.
//                Если сапер использует детектор мин, то заряд батареи уменьшается.
//                Если на поле остались мины и заряд батареи исчерпан, то сапер умирает.
//        Появляется сообщение о причине смерти.
//                Мина не может появиться на месте сапера.
//        Если минер разминирует мину, то значение количества мин вокруг него уменьшится на один.
}

