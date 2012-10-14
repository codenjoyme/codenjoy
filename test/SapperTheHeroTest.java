import com.globallogic.sapperthehero.game.Board;
import com.globallogic.sapperthehero.game.Cell;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

/**
 * Created with IntelliJ IDEA.
 * User: oleksii.morozov
 * Date: 10/14/12
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class SapperTheHeroTest {


    private static final int MINES_COUNT = 4;
    private Board board;
    private static final int BOARD_SIZE = 4;

    @Before
    public void gameStart() {
        board = new Board(BOARD_SIZE, MINES_COUNT);
    }

    // Есть поле
    @Test
    public void shouldBoardOnStartGame() {
        assertNotNull(board);
    }

    //   поле состоит из пустых клеток.
    @Test
    public void shouldBoardConsistFromCells() {
        assertNotNull(board.getFreeCells());
    }
//    клетка имеет координаты X и Y
//    направление оси X вправо, направление оси Y вниз, как в языке программирования

    @Test
    public void shouldCellContainXAndYCoordinates() {
        try {
            Cell.class.getMethod("getXPosition");
            Cell.class.getMethod("getYPosition");
        } catch (NoSuchMethodException e) {
            assertFalse(true);
            e.printStackTrace();
        }
    }


//  Клетки пустые, то есть на них ничего нет
//        Поле квадратное.
//        Поле у поля больше одной клетки.
//        На поле появляется сапер.
//        Сапер может двигаться по горизонтали, вертикали и диагонали.
//        На поле появляются мины.
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
