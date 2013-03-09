package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.console.BombermanPrinter;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotSame;
import static junit.framework.Assert.assertSame;
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

    public static final int SIZE = 5;
    private Board board;
    private Bomberman bomberman;
    private Level level;
    private WallsImpl walls;

    @Before
    public void setUp() throws Exception {
        level = mock(Level.class);
        canDropBombs(1);
        bombsPower(1);
        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());
        givenBoard();
    }

    private void givenBoard() {
        board = new UnmodifiableBoard(walls, level, SIZE);
        bomberman = board.getBomberman();
    }

    @Test
    public void shouldBoard_whenStartGame() {
        Board board = new UnmodifiableBoard(walls, level, 10);
        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(SIZE, board.size());
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
        gotoMaxRight();

        assertBombermanAt(SIZE - 1, 0);
    }

    @Test
    public void shouldBombermanStop_whenGoToWallDown() {
        gotoMaxDown();

        assertBombermanAt(0, SIZE - 1);
    }

    private void gotoMaxDown() {
        for (int y = 0; y <= SIZE + 1; y++) {
            bomberman.down();
            board.tact();
        }
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

        Bomb bomb = getBomb(0);
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

        for (int y = 0; y < 2 + 2; y++) {
            bomberman.down();
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

    private Bomb getBomb(int index) {
        return board.getBombs().get(index);
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
        assertBombermanAlive();
        board.tact();

        assertBombermanDie();
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
        assertBombermanAlive();
        board.tact();

        assertBombermanDie();
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
        assertBombermanAlive();
        board.tact();

        assertBombermanDie();
        assertGameOver();
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromUp() {
        bomberman.bomb();
        board.tact();
        bomberman.down();
        board.tact();
        board.tact();
        board.tact();
        assertBombermanAlive();
        board.tact();

        assertBombermanDie();
        assertGameOver();
    }

    private void assertBombermanAlive() {
        assertTrue(bomberman.isAlive());
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromDown() {
        bomberman.down();
        board.tact();
        bomberman.bomb();
        board.tact();
        bomberman.up();
        board.tact();
        board.tact();
        board.tact();
        assertBombermanAlive();
        board.tact();

        assertBombermanDie();
        assertGameOver();
    }

    @Test
    public void shouldNoKillBoomberman_whenBombExploded_blastWaveAffect_fromDownRight() {
        bomberman.down();
        board.tact();
        bomberman.right();
        board.tact();
        bomberman.bomb();
        board.tact();
        bomberman.up();
        board.tact();
        bomberman.left();
        board.tact();
        board.tact();
        assertBombermanAlive();
        board.tact();

        assertBombermanAlive();
    }

    private void assertBombermanDie() {
        assertFalse(bomberman.isAlive());
    }

    @Test
    public void shouldSameBoomberman_whenNetFromBoard() {
        assertSame(bomberman, board.getBomberman());
    }

    @Test
    public void shouldBlastAfter_whenBombExposed() {
        bomberman.bomb();
        board.tact();
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();
        board.tact();
        board.tact();

        assertBoard("҉҉☺  \n" +
                    "҉    \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_inOtherCorner() {
        gotoMaxDown();
        gotoMaxRight();

        bomberman.bomb();
        board.tact();
        bomberman.left();
        board.tact();
        bomberman.left();
        board.tact();
        board.tact();
        board.tact();

        assertBoard("     \n" +
                    "     \n" +
                    "     \n" +
                    "    ҉\n" +
                    "  ☺҉҉\n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_bombermanDie() {
        gotoBoardCenter();
        bomberman.bomb();
        board.tact();
        bomberman.down();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        assertBoard("     \n" +
                    "  ҉  \n" +
                    " ҉҉҉ \n" +
                    "  Ѡ  \n" +
                    "     \n");

        assertBombermanDie();
        assertGameOver();
    }

    private void gotoBoardCenter() {
        for (int y = 0; y < SIZE/2; y++) {
            bomberman.down();
            board.tact();
            bomberman.right();
            board.tact();
        }
    }

    private void assertBoard(String expected) {
        assertEquals(expected, new BombermanPrinter(board.size()).print(board));
    }

    // появляются стенки, которые конфигурятся извне
    @Test
    public void shouldBombermanNotAtWall() {
        assertBoard("☺    \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");

        givenBoardWithWalls();

        assertBoard("☼☼☼☼☼\n" +
                    "☼☺  ☼\n" +
                    "☼   ☼\n" +
                    "☼   ☼\n" +
                    "☼☼☼☼☼\n");

    }

    // бомбермен не может пойти вперед на стенку
    @Test
    public void shouldBombermanStop_whenUpWall() {
        givenBoardWithWalls();

        bomberman.up();
        board.tact();

        assertBoard("☼☼☼☼☼\n" +
                    "☼☺  ☼\n" +
                    "☼   ☼\n" +
                    "☼   ☼\n" +
                    "☼☼☼☼☼\n");

        assertBombermanAt(1, 1);
    }

    @Test
    public void shouldBombermanStop_whenLeftWall() {
        givenBoardWithWalls();

        bomberman.left();
        board.tact();

        assertBoard("☼☼☼☼☼\n" +
                    "☼☺  ☼\n" +
                    "☼   ☼\n" +
                    "☼   ☼\n" +
                    "☼☼☼☼☼\n");

        assertBombermanAt(1, 1);
    }

    @Test
    public void shouldBombermanStop_whenRightWall() {
        givenBoardWithWalls();

        gotoMaxRight();

        assertBoard("☼☼☼☼☼\n" +
                    "☼  ☺☼\n" +
                    "☼   ☼\n" +
                    "☼   ☼\n" +
                    "☼☼☼☼☼\n");

        assertBombermanAt(SIZE - 2, 1);
    }

    @Test
    public void shouldBombermanStop_whenDownWall() {
        givenBoardWithWalls();

        gotoMaxDown();

        assertBoard("☼☼☼☼☼\n" +
                    "☼   ☼\n" +
                    "☼   ☼\n" +
                    "☼☺  ☼\n" +
                    "☼☼☼☼☼\n");

        assertBombermanAt(1, SIZE - 2);
    }

    private void gotoMaxRight() {
        for (int x = 0; x <= SIZE + 1; x++) {
            bomberman.right();
            board.tact();
        }
    }

    private void givenBoardWithWalls() {
        givenBoardWithWalls(SIZE);
    }

    private void givenBoardWithWalls(int size) {
        board = new UnmodifiableBoard(new BasicWalls(size), level, size);
        bomberman = board.getBomberman();
    }

    private void givenBoardWithDestroyWalls() {
        givenBoardWithDestroyWalls(SIZE);
    }

    private void givenBoardWithDestroyWalls(int size) {
        board = new UnmodifiableBoard(new DestroyWalls(new OriginalWalls(size), new RandomDice()), level, size);
        bomberman = board.getBomberman();
    }

    private void givenBoardWithOriginalWalls() {
        givenBoardWithOriginalWalls(SIZE);
    }

    private void givenBoardWithOriginalWalls(int size) {
        board = new UnmodifiableBoard(new OriginalWalls(size), level, size);
        bomberman = board.getBomberman();
    }

    // бомбермен не может вернуться на место бомбы, она его не пускает как стена
    @Test
    public void shouldBombermanStop_whenGotoBomb() {
        bomberman.bomb();
        board.tact();
        bomberman.right();
        board.tact();

        bomberman.left();
        board.tact();

        assertBoard("2☺   \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    // проверить, что бомбермен может одноверменно перемещаться по полю и дропать бомбы за один такт, только как именно?
    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_bombFirstly() {
        bomberman.bomb();
        bomberman.right();
        board.tact();

        assertBoard("4☺   \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_moveFirstly() {
        bomberman.right();
        bomberman.bomb();
        board.tact();

        assertBoard(" ☻   \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");

        bomberman.right();
        board.tact();

        assertBoard(" 3☺  \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_bombThanMove() {
        bomberman.bomb();
        board.tact();
        bomberman.right();
        board.tact();

        assertBoard("3☺   \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_moveThanBomb() {
        bomberman.right();
        board.tact();
        bomberman.bomb();
        board.tact();

        assertBoard(" ☻   \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");

        bomberman.right();
        board.tact();

        assertBoard(" 3☺  \n" +
                    "     \n" +
                    "     \n" +
                    "     \n" +
                    "     \n");
    }

    @Test
    public void shouldWallProtectsBomberman() {
        givenBoardWithOriginalWalls();

        bomberman.bomb();
        goOut();

        assertBoard("☼☼☼☼☼\n" +
                    "☼1  ☼\n" +
                    "☼ ☼ ☼\n" +
                    "☼  ☺☼\n" +
                    "☼☼☼☼☼\n");

        board.tact();

        assertBoard("☼☼☼☼☼\n" +
                    "☼҉҉ ☼\n" +
                    "☼҉☼ ☼\n" +
                    "☼  ☺☼\n" +
                    "☼☼☼☼☼\n");

        assertBombermanAlive();
    }

    @Test
    public void shouldWallProtectsBomberman2() {
        assertBombPower(5,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉҉҉҉҉҉ ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");

        assertBombermanAlive();
    }

    private void bombsPower(int power) {
        when(level.bombsPower()).thenReturn(power);
    }

    // проверить, что разрыв бомбы длинной указанной в level
    @Test
    public void shouldChangeBombPower_to2() {
        assertBombPower(2,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉҉҉    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to3() {
        assertBombPower(3,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉҉҉҉   ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to6() {
        assertBombPower(6,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉҉҉҉҉҉҉☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    private void assertBombPower(int power, String expected) {
        givenBoardWithOriginalWalls(9);
        bombsPower(power);

        bomberman.bomb();
        goOut();
        board.tact();

        assertBoard(expected);
    }

    private void goOut() {
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();
        bomberman.down();
        board.tact();
        bomberman.down();
        board.tact();
    }

    // я немогу модифицировать список бомб на доске, меняя getBombs
    // но список бомб, что у меня на руках обязательно синхронизирован с теми, что на поле
    @Test
    public void shouldNoChangeOriginalBombsWhenUseBoardApiButTimersSynchronized() {
        canDropBombs(2);
        bomberman.bomb();
        bomberman.right();
        board.tact();
        bomberman.bomb();
        bomberman.right();
        board.tact();

        List<Bomb> bombs1 = board.getBombs();
        List<Bomb> bombs2 = board.getBombs();
        List<Bomb> bombs3 = board.getBombs();
        assertNotSame(bombs1, bombs2);
        assertNotSame(bombs2, bombs3);
        assertNotSame(bombs3, bombs1);

        Bomb bomb11 = bombs1.get(0);
        Bomb bomb12 = bombs2.get(0);
        Bomb bomb13 = bombs3.get(0);
        assertNotSame(bomb11, bomb12);
        assertNotSame(bomb12, bomb13);
        assertNotSame(bomb13, bomb11);

        Bomb bomb21 = bombs1.get(1);
        Bomb bomb22 = bombs2.get(1);
        Bomb bomb23 = bombs3.get(1);
        assertNotSame(bomb21, bomb22);
        assertNotSame(bomb22, bomb23);
        assertNotSame(bomb23, bomb21);

        board.tact();
        board.tact();

        assertFalse(bomb11.isExploded());
        assertFalse(bomb12.isExploded());
        assertFalse(bomb13.isExploded());

        board.tact();

        assertTrue(bomb11.isExploded());
        assertTrue(bomb12.isExploded());
        assertTrue(bomb13.isExploded());

        assertFalse(bomb21.isExploded());
        assertFalse(bomb22.isExploded());
        assertFalse(bomb23.isExploded());

        board.tact();

        assertTrue(bomb21.isExploded());
        assertTrue(bomb22.isExploded());
        assertTrue(bomb23.isExploded());
    }

    @Test
    public void shouldReturnShouldSynchronizedBombsList_whenUseBoardApi() {
        bomberman.bomb();
        bomberman.right();
        board.tact();

        List<Bomb> bombs1 = board.getBombs();
        assertEquals(1, bombs1.size());

        board.tact();
        board.tact();
        board.tact();
        board.tact();

        List<Bomb> bombs2 = board.getBombs();
        assertEquals(0, bombs2.size());

        assertEquals(0, bombs1.size());
    }

    @Test
    public void shouldNoChangeBlast_whenUseBoardApi() {
        bomberman.bomb();
        bomberman.right();
        board.tact();
        bomberman.right();
        board.tact();
        board.tact();
        board.tact();
        board.tact();

        List<Point> blasts1 = board.getBlasts();
        List<Point> blasts2 = board.getBlasts();
        List<Point> blasts3 = board.getBlasts();
        assertNotSame(blasts1, blasts2);
        assertNotSame(blasts2, blasts3);
        assertNotSame(blasts3, blasts1);

        Point blast11 = blasts1.get(0);
        Point blast12 = blasts2.get(0);
        Point blast13 = blasts3.get(0);
        assertNotSame(blast11, blast12);
        assertNotSame(blast12, blast13);
        assertNotSame(blast13, blast11);

        Point blast21 = blasts1.get(1);
        Point blast22 = blasts2.get(1);
        Point blast23 = blasts3.get(1);
        assertNotSame(blast21, blast22);
        assertNotSame(blast22, blast23);
        assertNotSame(blast23, blast21);
    }

    @Test
    public void shouldNoChangeWall_whenUseBoardApi() {
        givenBoardWithWalls();

        Walls walls1 = board.getWalls();
        Walls walls2 = board.getWalls();
        Walls walls3 = board.getWalls();
        assertNotSame(walls1, walls2);
        assertNotSame(walls2, walls3);
        assertNotSame(walls3, walls1);

        Iterator<Wall> iterator1 = walls1.iterator();
        Iterator<Wall> iterator2 = walls2.iterator();
        Iterator<Wall> iterator3 = walls3.iterator();

        Point wall11 = iterator1.next();
        Point wall12 = iterator2.next();
        Point wall13 = iterator3.next();
        assertNotSame(wall11, wall12);
        assertNotSame(wall12, wall13);
        assertNotSame(wall13, wall11);

        Point wall21 = iterator1.next();
        Point wall22 = iterator2.next();
        Point wall23 = iterator3.next();
        assertNotSame(wall21, wall22);
        assertNotSame(wall22, wall23);
        assertNotSame(wall23, wall21);
    }

    // в настройках уровня так же есть и разрущающиеся стены
    @Test
    public void shouldRandomSetDestroyWalls_whenStart() {
        givenBoardWithDestroyWalls();

        assertBoard("#####\n" +
                    "#☺  #\n" +
                    "# # #\n" +
                    "#   #\n" +
                    "#####\n");
    }

    // они взрываются от ударной волны
    @Test
    public void shouldDestroyWallsDestroyed_whenBombExploded() {
        givenBoardWithDestroyWalls();

        bomberman.bomb();
        goOut();

        assertBoard("#####\n" +
                    "#1  #\n" +
                    "# # #\n" +
                    "#  ☺#\n" +
                    "#####\n");

        board.tact();

        assertBoard("#҉###\n" +
                    "҉҉҉ #\n" +
                    "#҉# #\n" +
                    "#  ☺#\n" +
                    "#####\n");
    }

    // появляются чертики, их несоклько за игру
    // каждый такт чертики куда-то рендомно муваются
    // чертик не может ходить по стенкам и бомбам
    // если бомбермен и чертик попали в одну клетку - бомбермен умирает
    // чертик умирает, если попадает под взывающуюся бомбу
    // под разрущающейся стенкой может быть приз - это специальная стенка
    // появляется приз - увеличение длительности ударной волны - его может бомбермен взять и тогда ударная волна будет больше
    // появляется приз - хождение сквозь разрушающиеся стенки - взяв его, бомбермен может ходить через тенки
    //
}
