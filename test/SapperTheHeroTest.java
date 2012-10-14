import com.globallogic.sapperthehero.game.*;
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
    private static final int BOARD_SIZE = 4;
    private Board board;
    private Sapper sapper;
    private List<Mine> mines;

    @Before
    public void gameStart() {
        board = new Board(BOARD_SIZE, MINES_COUNT);
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
        assertNotNull(board.getBoardCells());
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
        assertTrue(board.getFreeCells().size() > NUMBER_ZERO);
    }

    //  Размеры поля задаются перед игрой.
    @Test
    public void shouldBoardSizeSpecifyAtGameStart() {
        assertNotNull(board.getBoardSize());
    }

    // Поле квадратное.
    @Test
    public void shouldBoardBeSquare() {
        assertEquals("Поле не квадратное", board.getBoardCells().size() % board.getBoardSize(), NUMBER_ZERO);
    }

    //  У поля больше одной клетки.
    @Test
    public void shouldBoardCellsNumberBeMoreThanOne() {
        assertTrue(board.getBoardCells().size() > NUMBER_ONE);
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


    //  На поле появляются мины.
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
        assertEquals(board.getBoardCells().size(), board.getFreeCells().size() + mines.size() + 1);
    }

    //  Сапер может двигаться по горизонтали, вертикали
    @Test
    public void shouldSapperMoveToUp() {
        int oldXPosition = sapper.getXPosition();
        int oldYPosition = sapper.getYPosition();
        board.sapperMoveTo(Direction.UP);
        int newXPosition = sapper.getXPosition();
        int newYPosition = sapper.getYPosition();
        assertTrue(oldXPosition == newXPosition && oldYPosition == newYPosition + 1);
    }

    @Test
    public void shouldSapperMoveToDown() {
        int oldXPosition = sapper.getXPosition();
        int oldYPosition = sapper.getYPosition();
        board.sapperMoveTo(Direction.DOWN);
        int newXPosition = sapper.getXPosition();
        int newYPosition = sapper.getYPosition();
        assertTrue(oldXPosition == newXPosition && oldYPosition == newYPosition - 1);
    }

    @Test
    public void shouldSapperMoveToLeft() {
        int oldXPosition = sapper.getXPosition();
        int oldYPosition = sapper.getYPosition();
        board.sapperMoveTo(Direction.LEFT);
        int newXPosition = sapper.getXPosition();
        int newYPosition = sapper.getYPosition();
        assertTrue(oldXPosition == newXPosition + 1 && oldYPosition == newYPosition);
    }

    @Test
    public void shouldSapperMoveToRight() {
        int oldXPosition = sapper.getXPosition();
        int oldYPosition = sapper.getYPosition();
        board.sapperMoveTo(Direction.RIGHT);
        int newXPosition = sapper.getXPosition();
        int newYPosition = sapper.getYPosition();
        assertTrue(oldXPosition == newXPosition - 1 && oldYPosition == newYPosition);
    }

    //        Если сапер наступает на мину, то он умирает.
    @Test
    public void shouldSapperMoveToMine() {
        assertSapperMoveToMine();
        assertTrue(sapper.isDead());
    }

    private void assertSapperMoveToMine() {
        Cell possibleIsMine = new Cell(sapper.getXPosition(), sapper.getYPosition() + 1);
        if (!mines.contains(possibleIsMine)) {
            board.createMineOnPositionIfPossible(possibleIsMine);
        }
        board.sapperMoveTo(Direction.DOWN);
    }

    //  Проверить что игра окончена
    @Test
    public void assertGameOver() {
        assertSapperMoveToMine();
        assertTrue("Конец игры", board.isGameOver());
    }

    //   Смерть сапера значит конец игры.
    @Test
    public void shouldGameIsOverIfSapperIsDead() {
        assertSapperMoveToMine();
        assertEquals("Сапер мертв игра не окончена", board.isGameOver(), sapper.isDead());
    }

}
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

