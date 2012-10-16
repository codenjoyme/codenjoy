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
    private static final int NUMBER_ZERO = 0;
    private static final int NUMBER_ONE = 1;
    private static final int BOARD_SIZE = 4;
    private Board board;
    private Sapper sapper;
    private List<Mine> mines;

    @Before
    public void gameStart() throws Exception {
        board = new Board(BOARD_SIZE, MINES_COUNT);
        sapper = board.getSapper();
        mines = board.getMines();
    }

    // Есть поле
    @Test
    public void shouldBoardOnStartGame() {
        assertNotNull(board);
    }

    //   поле состоит из клеток.
    @Test
    public void shouldBoardConsistFromCells() {
        assertNotNull(board.getBoardCells());
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


    //На поле появляются мины.
    @Test
    public void shouldMinesOnBoard() {
        assertNotNull(mines);
    }


    //количество мин задается в начале игры
    @Test
    public void shouldMinesCountSpecifyAtGameStart() {
        assertNotNull(board.getMinesCount());
    }

    //Мины появляются случайно. вероятность ~(1/16)^2
    @Test
    public void shouldMinesRandomPlacedOnBoard() {
        assertTrue(assertMinesRandomPlacedOnBoard());
    }

    private boolean assertMinesRandomPlacedOnBoard() {
        try {
            for (int index = 0; index < 100; index++) {
                Board firstBoard = new Board(BOARD_SIZE, NUMBER_ONE);
                Board secondBoard = new Board(BOARD_SIZE, NUMBER_ONE);
                Mine mineFromFirstBoard = firstBoard.getMines().get(NUMBER_ONE - 1);
                Mine mineFromSecondBoard = secondBoard.getMines().get(NUMBER_ONE - 1);
                if (!mineFromFirstBoard.equals(mineFromSecondBoard)) {
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    // Когда появляются мины и сапер, то они занимают свободные клетки.
    @Test
    public void shouldFreeCellsDecreaseOnCreatingSapperAndMines() {
        assertEquals(board.getBoardCells().size(), board.getFreeCells().size() + mines.size() + 1);
    }

    //Сапер может двигаться по горизонтали, вертикали
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

    //Если сапер наступает на мину, то он умирает.
    @Test
    public void shouldSapperMoveToMine() {
        assertSapperMoveToMine();

        assertTrue(sapper.isDead());
    }

    private void assertSapperMoveToMine() {
        placeMineNearSapper();
        board.sapperMoveTo(Direction.DOWN);
    }

    private void placeMineNearSapper() {
        Cell possibleIsMine = new Cell(sapper.getXPosition(), sapper.getYPosition() + 1);
        if (!mines.contains(possibleIsMine)) {
            board.createMineOnPositionIfPossible(possibleIsMine);
        }
    }

    //Проверить что игра окончена
    @Test
    public void assertGameOver() {
        assertSapperMoveToMine();

        assertTrue("Конец игры", board.isGameOver());
    }

    //Смерть сапера значит конец игры.
    @Test
    public void shouldGameIsOverIfSapperIsDead() {
        assertSapperMoveToMine();

        assertEquals("Сапер мертв игра не окончена", board.isGameOver(), sapper.isDead());
    }

//        Сапер знает о количестве мин на поле.

    //После движения сапера начинается новый ход

    //Смерть сапера значит конец игры.
    @Test
    public void shouldNextTurnAfterSapperMove() {
        int turnBeforeSapperMotion = board.getTurn();

        board.sapperMoveTo(Direction.DOWN);
        int turnAfterSapperMotion = board.getTurn();

        assertEquals("Новый ход не начался", turnBeforeSapperMotion, turnAfterSapperMotion - 1);
    }


    // У сапера есть чутье, и он знает, сколько вокруг мин.
    @Test
    public void shouldSapperKnowsHowMuchMinesNearHim() {
        assertNotNull(board.getMinesNearSapper());
    }

    //У сапера есть детектор мин.
    @Test
    public void shoulSapperHaveMineDetector() {
        assertNotNull(sapper.getMineDetector());
    }

    //У детектора мин есть заряд батареи равный  8
    @Test
    public void shouldMineDetectorHaveCharge() {
        assertNotNull(sapper.getMineDetectorCharge());
    }

    //Заряд батареи больше чем количество мин на поле.
    @Test
    public void shouldMineDetectorChargeMoreThanMinesOnBoard() {
        assertTrue(sapper.getMineDetectorCharge() > board.getMinesCount());
    }

    //Сапер может проверить любую соседнюю клетку детектором мин.
//    TODO
    @Test
    public void shouldSapperDestroyMineInGivenDirectionByMineDetectorIfMineExist() {
        try {
            for (Direction direction : Direction.values()) {
                board.useMineDetectorToGivenDirection(direction);
            }
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    //Если сапер использует детектор мин, то заряд батареи уменьшается.
    @Test
    public void shouldMineDetectorChargeDecreaseByOneAfterUse() {
        int chargeBeforeUse = sapper.getMineDetectorCharge();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int chargeAfterUse = sapper.getMineDetectorCharge();

        assertEquals(chargeBeforeUse, chargeAfterUse + 1);
    }
//Если на поле остались мины и заряд батареи исчерпан, то сапер умирает.


//Появляется сообщение о причине смерти.

    //Если минер разминирует мину, то значение количества мин вокруг него уменьшится на один.
    @Test
    public void shouldMineCountDecreaseByOneIfMineIsDestroyed() {
        placeMineNearSapper();
        int mineCountBeforeDestroying = board.getMinesCount();

        board.useMineDetectorToGivenDirection(Direction.DOWN);
        int mineCountAfterDestroying = board.getMinesCount();

        assertEquals(mineCountBeforeDestroying, mineCountAfterDestroying + 1);
    }
}