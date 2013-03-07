package com.codenjoy.bomberman;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:07 AM
 */
public class BoardTest {

    public static final int SIZE = 20;
    private Board board;
    private Bomberman bomberman;
    private Level level;

    @Before
    public void setUp() throws Exception {
        level = mock(Level.class);
        canDropBombs(2);
        board = new Board(level, SIZE);
        bomberman = board.getBomberman();
    }

    @Test
    public void shouldBoard_whenStartGame() {
        Board board = new Board(level, 10);
        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(20, board.size());
    }

    @Test
    public void shouldBombermanOnBoardAtInitPos_whenGameStart() {
        assertBombermanAt(0, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        bomberman.right();
        board.tact();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();

        assertBombermanAt(2, 0);
    }

    @Test
    public void shouldBombermanOnBoardOneDownStep_whenCallDownCommand() {
        bomberman.down();
        board.tact();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanWalkUp() {
        bomberman.down();
        board.tact();
        bomberman.down();
        board.tact();

        bomberman.up();
        board.tact();

        assertBombermanAt(0, 1);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallUp() {
        bomberman.up();
        board.tact();

        assertBombermanAt(0, 0);
    }

    private void assertBombermanAt(int x, int y) {
        assertEquals(x, bomberman.getX());
        assertEquals(y, bomberman.getY());
    }

    @Test
    public void shouldBombermanWalkLeft() {
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();

        bomberman.left();
        board.tact();

        assertBombermanAt(1, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallLeft() {
        bomberman.left();
        board.tact();

        assertBombermanAt(0, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallRight() {
        for (int x = 0; x <= SIZE + 1; x++) {
            bomberman.right();
            board.tact();
        }

        assertBombermanAt(SIZE - 1, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallDown() {
        for (int y = 0; y <= SIZE + 1; y++) {
            bomberman.down();
            board.tact();
        }

        assertBombermanAt(0, SIZE - 1);
    }

    @Test
    public void shouldBombermanMovedOncePerTact() {
        bomberman.down();
        bomberman.right();
        bomberman.up();
        bomberman.left();
        board.tact();

        assertBombermanAt(0, 1);

        bomberman.right();
        bomberman.up();
        bomberman.left();
        bomberman.down();
        board.tact();

        assertBombermanAt(1, 1);
    }

    @Test
    public void shouldBombDropped_whenBombermanDropBomb() {
        bomberman.bomb();
        board.tact();

        assertBombAt(0, 0);
    }

    private void assertBombAt(int x, int y) {
        assertBombsCount(1);

        Bomb bomb = board.getBombs().get(0);
        assertEquals(x, bomb.getX());
        assertEquals(y, bomb.getY());
    }

    @Test
    public void shouldBombDropped_whenBombermanDropBombAtAnotherPlace() {
        bomberman.down();
        board.tact();

        bomberman.right();
        board.tact();

        bomberman.bomb();
        board.tact();

        assertBombAt(1, 1);
    }

    @Test
    public void shouldBombsDropped_whenBombermanDropThreeBomb() {
        canDropBombs(3);

        bomberman.down();
        board.tact();
        bomberman.bomb();
        board.tact();

        bomberman.right();
        board.tact();
        bomberman.bomb();
        board.tact();

        assertBombsAt(0, 1,
                1, 1);
    }

    private void canDropBombs(int countBombs) {
        reset(level);
        when(level.bombsCount()).thenReturn(countBombs);
    }

    private void assertBombsAt(int... expected) {
        List<Bomb> bombs = board.getBombs();
        assertEquals(expected.length / 2, bombs.size());

        int[] actual = new int[expected.length];
        int index = 0;
        for (Bomb bomb : bombs) {
            actual[index++] = bomb.getX();
            actual[index++] = bomb.getY();
        }

        assertEquals(Arrays.toString(expected), Arrays.toString(actual));
    }

    // проверить, что бомбермен не может бомб дропать больше, чем у него в level прописано
    @Test
    public void shouldOnlyTwoBombs_whenLevelApproveIt() {
        canDropBombs(2);

        for (int y = 0; y < 5; y++) {
            bomberman.down();
            board.tact();
            bomberman.bomb();
            board.tact();
        }

        assertBombsCount(2);
    }

    private void assertBombsCount(int count) {
        List<Bomb> bombs = board.getBombs();
        assertEquals(count, bombs.size());
    }

    // бомберен не может дропать два бомбы на одно место
    @Test
    public void shouldOnlyOneBombPerPlace() {
        canDropBombs(2);

        bomberman.bomb();
        board.tact();

        bomberman.bomb();
        board.tact();

        assertBombsCount(1);
    }

    // проверить, что бомба взрывается за 5 тактов
    @Test
    public void shouldBoom_whenDroppedBombHas5Tacts() {
        bomberman.bomb();
        board.tact();
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();
        board.tact();
        assertBombsCount(1);

        board.tact();

        assertBombsCount(0);
    }

    // проверить, что я могу поставить еще одну бомбу, когда другая рванула
    @Test
    public void shouldCanDropNewBomb_whenOtherBoom() {
        shouldBoom_whenDroppedBombHas5Tacts();

        bomberman.left();
        board.tact();
        bomberman.left();
        board.tact();

        bomberman.bomb();
        board.tact();

        assertBombsCount(1);
    }

    // если бомбермен стоит на бомбе то он умирает после ее взрыва
    @Test
    public void shouldKillBoomberman_whenBombExploded() {
        bomberman.bomb();
        board.tact();
        board.tact();
        board.tact();
        board.tact();
        assertTrue(bomberman.isAlive());
        board.tact();

        assertFalse(bomberman.isAlive());
        assertGameOver();
    }

    private void assertGameOver() {
        assertTrue("Expected game over", board.isGameOver());
    }

    // после смерти ходить больше нельзя
    @Test(expected = IllegalStateException.class)
    public void shouldException_whenTryToMoveIfDead_goLeft() {
        shouldKillBoomberman_whenBombExploded();

        bomberman.left();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldException_whenTryToMoveIfDead_goUp() {
        shouldKillBoomberman_whenBombExploded();

        bomberman.up();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldException_whenTryToMoveIfDead_goDown() {
        shouldKillBoomberman_whenBombExploded();

        bomberman.down();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldException_whenTryToMoveIfDead_goRight() {
        shouldKillBoomberman_whenBombExploded();

        bomberman.right();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldException_whenTryToMoveIfDead_dropBomb() {
        shouldKillBoomberman_whenBombExploded();

        bomberman.bomb();
    }

    // если бомбермен стоит под действием ударной волны, он умирает
    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromLeft() {
        bomberman.bomb();
        board.tact();
        bomberman.right();
        board.tact();
        board.tact();
        board.tact();
        assertTrue(bomberman.isAlive());
        board.tact();

        assertFalse(bomberman.isAlive());
        assertGameOver();
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromRight() {
        bomberman.right();
        board.tact();
        bomberman.bomb();
        board.tact();
        bomberman.left();
        board.tact();
        board.tact();
        board.tact();
        assertTrue(bomberman.isAlive());
        board.tact();

        assertFalse(bomberman.isAlive());
        assertGameOver();
    }

    // я немогу модифицировать список бомб на доске, меняя getBombs
    // проверить, что бомбермен может одноверменно перемещаться по полю и дропать бомбы за один такт, только как именно?
    // бомбермен не может вернуться на место бомбы, она его не пускает как стена
    // появляются стенки, которые конфигурятся извне
    // бомбермен не может пойти вперед на стенку
    // появляются чертики, их несоклько за игру
    // каждый такт чертики куда-то рендомно муваются
    // если бомбермен и чертик попали в одну клетку - бомбермен умирает
    // чертик не может ходить по стенам
    // чертик не может ходить по бомбам
    // чертик умирает, если попадает под взывающуюся бомбу
    // в настройках уровня так же есть и разрущающиеся стены
    // на них не может ходить бомбермен
    // на них не может ходить чертик
    // они взрываются от ударной волны
    // под разрущающейся стенкой может быть приз - это специальная стенка
    // появляется приз - увеличение длительности ударной волны - его может бомбермен взять и тогда ударная волна будет больше
    // появляется приз - хождение сквозь разрушающиеся стенки - взяв его, бомбермен может ходить через тенки
    //
}
