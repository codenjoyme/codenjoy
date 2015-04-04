package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.BombermanEvents;
import com.codenjoy.dojo.services.*;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static junit.framework.Assert.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: oleksandr.baglai
 * Date: 3/7/13
 * Time: 9:07 AM
 */
public class BombermanTest {

    public int SIZE = 5;
    private SingleBomberman game;
    private Joystick hero;
    private Level level;
    private WallsImpl walls;
    private GameSettings settings;
    private EventListener listener;
    private Dice meatChppperDice;
    private Dice bombermanDice;
    private Player player;
    private List bombermans;
    private Bomberman bomberman;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setUp() throws Exception {
        meatChppperDice = mock(Dice.class);
        bombermanDice = mock(Dice.class);

        level = mock(Level.class);
        canDropBombs(1);
        bombsPower(1);
        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(new LinkedList<Wall>().iterator());
        settings = mock(GameSettings.class);
        listener = mock(EventListener.class);

        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
        when(settings.getLevel()).thenReturn(level);
        initBomberman();
        givenBoard(SIZE);
    }

    private void initBomberman() {
        dice(bombermanDice, 0, 0);
        HeroImpl bomberman = new HeroImpl(level, bombermanDice);
        when(settings.getBomberman(level)).thenReturn(bomberman);
        this.hero = bomberman;
    }

    private void givenBoard(int size) {
        when(settings.getBoardSize()).thenReturn(v(size));
        bomberman = new Bomberman(settings);
        game = new SingleBomberman(bomberman, listener, printer);
        dice(bombermanDice, 0, 0);
        game.newGame();
        hero = game.getJoystick();
        player = game.getPlayer();
    }

    @Test
    public void shouldBoard_whenStartGame() {
        when(settings.getBoardSize()).thenReturn(v(10));

        Bomberman board = new Bomberman(settings);

        assertEquals(10, board.size());
    }

    @Test
    public void shouldBoard_whenStartGame2() {
        assertEquals(SIZE, bomberman.size());
    }

    @Test
    public void shouldBombermanOnBoardAtInitPos_whenGameStart() {
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    @Test
    public void shouldBombermanOnBoardOneRightStep_whenCallRightCommand() {
        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");
    }

    @Test
    public void shouldBombermanOnBoardTwoRightSteps_whenCallRightCommandTwice() {
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☺  \n");
    }

    @Test
    public void shouldBombermanOnBoardOneUpStep_whenCallDownCommand() {
        hero.up();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "     \n");
    }

    @Test
    public void shouldBombermanWalkUp() {
        hero.up();
        game.tick();

        hero.up();
        game.tick();

        hero.down();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "     \n");
    }

    @Test
    public void shouldBombermanStop_whenGoToWallDown() {
        hero.down();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    private void assertBombermanAt(int x, int y) {
        assertEquals(x, player.getBomberman().getX());
        assertEquals(y, player.getBomberman().getY());
    }

    @Test
    public void shouldBombermanWalkLeft() {
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.left();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");
    }

    @Test
    public void shouldBombermanStop_whenGoToWallLeft() {
        hero.left();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");
    }

    @Test
    public void shouldBombermanStop_whenGoToWallRight() {
        gotoMaxRight();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "    ☺\n");
    }

    @Test
    public void shouldBombermanStop_whenGoToWallUp() {
        gotoMaxUp();

        asrtBrd("☺    \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "     \n");
    }

    private void gotoMaxUp() {
        for (int y = 0; y <= SIZE + 1; y++) {
            hero.up();
            game.tick();
        }
    }

    @Test
    public void shouldBombermanMovedOncePerTact() {
        hero.down();
        hero.up();
        hero.left();
        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n");

        hero.right();
        hero.left();
        hero.down();
        hero.up();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "     \n");
    }

    @Test
    public void shouldBombDropped_whenBombermanDropBomb() {
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻    \n");
    }

    @Test
    public void shouldBombDropped_whenBombermanDropBombAtAnotherPlace() {
        hero.up();
        game.tick();

        hero.right();
        game.tick();

        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n" +
                "     \n");
    }

    @Test
    public void shouldBombsDropped_whenBombermanDropThreeBomb() {
        canDropBombs(3);

        hero.up();
        game.tick();
        hero.act();
        game.tick();

        hero.right();
        game.tick();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "2☻   \n" +
                "     \n");
    }

    private void canDropBombs(int countBombs) {
        reset(level);
        when(level.bombsCount()).thenReturn(countBombs);
    }

    // проверить, что бомбермен не может бомб дропать больше, чем у него в level прописано
    @Test
    public void shouldOnlyTwoBombs_whenLevelApproveIt() {
        canDropBombs(2);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");

        hero.up();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☻    \n" +
                "     \n");

        hero.up();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "☻    \n" +
                "3    \n" +
                "     \n");

        hero.up();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "☺    \n" +
                "3    \n" +
                "2    \n" +
                "     \n");
    }

    // бомберен не может дропать два бомбы на одно место
    @Test
    public void shouldOnlyOneBombPerPlace() {
        canDropBombs(2);

        hero.act();
        game.tick();

        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻    \n");

        assertEquals(1, bomberman.getBombs().size());

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "2☺   \n");

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1 ☺  \n");

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "҉ ☺  \n");

        game.tick();   // бомб больше нет, иначе тут был бы взрыв второй

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☺  \n");
    }

    @Test
    public void shouldBoom_whenDroppedBombHas5Ticks() {
        hero.act();
        game.tick();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1 ☺  \n");

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");
    }

    // проверить, что я могу поставить еще одну бомбу, когда другая рванула
    @Test
    public void shouldCanDropNewBomb_whenOtherBoom() {
        shouldBoom_whenDroppedBombHas5Ticks();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");

        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "  ☻  \n");
    }

    // если бомбермен стоит на бомбе то он умирает после ее взрыва
    @Test
    public void shouldKillBoomberman_whenBombExploded() {
        hero.act();
        hero.right();
        game.tick();
        game.tick();
        game.tick();

        game.tick();

        assertBombermanAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1☺   \n");

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉Ѡ   \n");
        assertBombermanDie();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n");
        assertBombermanDie();
    }

    private void assertBombermanDie() {
        assertTrue("Expected model over", game.isGameOver());
    }

    // после смерти ходить больше нельзя
    @Test
    public void shouldException_whenTryToMoveIfDead_goLeft() {
        killBomber();

        hero.left();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    private void killBomber() {
        hero.up();
        game.tick();
        hero.right();
        hero.act();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                " ҉   \n" +
                "҉Ѡ҉  \n" +
                " ҉   \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goUp() {
        killBomber();

        hero.up();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goDown() {
        killBomber();

        hero.down();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_goRight() {
        killBomber();

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    @Test
    public void shouldException_whenTryToMoveIfDead_dropBomb() {
        killBomber();

        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "     \n");
    }

    // если бомбермен стоит под действием ударной волны, он умирает
    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromLeft() {
        hero.act();
        game.tick();
        hero.right();
        game.tick();
        game.tick();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "1☺   \n");
        assertBombermanAlive();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉Ѡ   \n");
        assertBombermanDie();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n");
        assertBombermanDie();
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromRight() {
        hero.right();
        game.tick();
        hero.act();
        game.tick();
        hero.left();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺1   \n");
        assertBombermanAlive();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "Ѡ҉҉  \n");
        assertBombermanDie();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n");
        assertBombermanDie();
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromUp() {
        hero.up();
        hero.act();
        game.tick();
        hero.down();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "1    \n" +
                "☺    \n");
        assertBombermanAlive();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉   \n" +
                "Ѡ    \n");
        assertBombermanDie();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n");
        assertBombermanDie();
    }

    private void assertBombermanAlive() {
        assertFalse(game.isGameOver());
    }

    @Test
    public void shouldKillBoomberman_whenBombExploded_blastWaveAffect_fromDown() {
        hero.down();
        game.tick();
        hero.act();
        game.tick();
        hero.up();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "1    \n");
        assertBombermanAlive();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n" +
                "҉҉   \n");
        assertBombermanDie();

        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ    \n" +
                "     \n");
        assertBombermanDie();
    }

    @Test
    public void shouldNoKillBoomberman_whenBombExploded_blastWaveAffect_fromDownRight() {
        hero.down();
        game.tick();
        hero.right();
        game.tick();
        hero.act();
        game.tick();
        hero.up();
        game.tick();
        hero.left();
        game.tick();

        game.tick();

        assertBombermanAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                " 1   \n");

        game.tick();

        assertBombermanAlive();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺҉   \n" +
                "҉҉҉  \n");
    }

    @Test
    public void shouldSameBoomberman_whenNetFromBoard() {
        assertSame(hero, game.getJoystick());
    }

    @Test
    public void shouldBlastAfter_whenBombExposed() {
        hero.act();
        game.tick();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "҉    \n" +
                "҉҉☺  \n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_inOtherCorner() {
        gotoMaxUp();
        gotoMaxRight();

        hero.act();
        game.tick();
        hero.left();
        game.tick();
        hero.left();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("  ☺҉҉\n" +
                "    ҉\n" +
                "     \n" +
                "     \n" +
                "     \n");
    }

    @Test
    public void shouldBlastAfter_whenBombExposed_bombermanDie() {
        gotoBoardCenter();
        hero.act();
        game.tick();
        hero.down();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "  ҉  \n" +
                " ҉҉҉ \n" +
                "  Ѡ  \n" +
                "     \n");

        assertBombermanDie();

        game.tick();
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "  Ѡ  \n" +
                "     \n");

        assertBombermanDie();
    }

    private void gotoBoardCenter() {
        for (int y = 0; y < SIZE / 2; y++) {
            hero.up();
            game.tick();
            hero.right();
            game.tick();
        }
    }

    private void asrtBrd(String expected) {
        assertEquals(expected, printer.getPrinter(
                bomberman.reader(), player).print());
    }

    // появляются стенки, которые конфигурятся извне
    @Test
    public void shouldBombermanNotAtWall() {
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺    \n");

        givenBoardWithWalls();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

    }

    // бомбермен не может пойти вперед на стенку
    @Test
    public void shouldBombermanStop_whenUpWall() {
        givenBoardWithWalls();

        hero.down();
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldBombermanStop_whenLeftWall() {
        givenBoardWithWalls();

        hero.left();
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldBombermanStop_whenRightWall() {
        givenBoardWithWalls();

        gotoMaxRight();

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldBombermanStop_whenDownWall() {
        givenBoardWithWalls();

        gotoMaxUp();

        asrtBrd("☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼ ☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void gotoMaxRight() {
        for (int x = 0; x <= SIZE + 1; x++) {
            hero.right();
            game.tick();
        }
    }

    private void givenBoardWithWalls() {
        givenBoardWithWalls(SIZE);
    }

    private void givenBoardWithWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        givenBoard(size);
    }

    private void givenBoardWithDestroyWalls() {
        givenBoardWithDestroyWalls(SIZE);
    }

    private void givenBoardWithDestroyWalls(int size) {
        withWalls(new DestroyWalls(new OriginalWalls(v(size))));
        givenBoard(size);
    }

    private void withWalls(Walls walls) {
        when(settings.getWalls(any(Bomberman.class))).thenReturn(walls);
    }

    private void givenBoardWithOriginalWalls() {
        givenBoardWithOriginalWalls(SIZE);
    }

    private void givenBoardWithOriginalWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        givenBoard(size);
    }

    // бомбермен не может вернуться на место бомбы, она его не пускает как стена
    @Test
    public void shouldBombermanStop_whenGotoBomb() {
        hero.act();
        game.tick();
        hero.right();
        game.tick();

        hero.left();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "2☺   \n");
    }

    // проверить, что бомбермен может одноверменно перемещаться по полю и дропать бомбы за один такт, только как именно?
    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_bombFirstly() {
        hero.act();
        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "4☺   \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_moveFirstly() {
        hero.right();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n");

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " 3☺  \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_bombThanMove() {
        hero.act();
        game.tick();
        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "3☺   \n");
    }

    @Test
    public void shouldBombermanWalkAndDropBombsTogetherInOneTact_moveThanBomb() {
        hero.right();
        game.tick();
        hero.act();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☻   \n");

        hero.right();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " 3☺  \n");
    }

    @Test
    public void shouldWallProtectsBomberman() {
        givenBoardWithOriginalWalls();

        hero.act();
        goOut();

        asrtBrd("☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼ ☼ ☼\n" +
                "☼1  ☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼҉☼ ☼\n" +
                "☼҉҉ ☼\n" +
                "☼☼☼☼☼\n");

        assertBombermanAlive();
    }

    @Test
    public void shouldWallProtectsBomberman2() {
        assertBombPower(5,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉ ☼\n" +
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
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉    ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to3() {
        assertBombPower(3,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼       ☼\n" +
                "☼ ☼ ☼ ☼ ☼\n" +
                "☼       ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉   ☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldChangeBombPower_to6() {
        assertBombPower(6,
                "☼☼☼☼☼☼☼☼☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉      ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉ ☺    ☼\n" +
                "☼҉☼ ☼ ☼ ☼\n" +
                "☼҉҉҉҉҉҉҉☼\n" +
                "☼☼☼☼☼☼☼☼☼\n");
    }

    private void assertBombPower(int power, String expected) {
        givenBoardWithOriginalWalls(9);
        bombsPower(power);

        hero.act();
        goOut();
        game.tick();

        asrtBrd(expected);
    }

    private void goOut() {
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        hero.up();
        game.tick();
        hero.up();
        game.tick();
    }

    // я немогу модифицировать список бомб на доске, меняя getBombs
    // но список бомб, что у меня на руках обязательно синхронизирован с теми, что на поле
    @Test
    public void shouldNoChangeOriginalBombsWhenUseBoardApiButTimersSynchronized() {
        canDropBombs(2);
        hero.act();
        hero.right();
        game.tick();
        hero.act();
        hero.right();
        game.tick();

        List<Bomb> bombs1 = bomberman.getBombs();
        List<Bomb> bombs2 = bomberman.getBombs();
        List<Bomb> bombs3 = bomberman.getBombs();
        assertSame(bombs1, bombs2);
        assertSame(bombs2, bombs3);
        assertSame(bombs3, bombs1);

        Bomb bomb11 = bombs1.get(0);
        Bomb bomb12 = bombs2.get(0);
        Bomb bomb13 = bombs3.get(0);
        assertSame(bomb11, bomb12);
        assertSame(bomb12, bomb13);
        assertSame(bomb13, bomb11);

        Bomb bomb21 = bombs1.get(1);
        Bomb bomb22 = bombs2.get(1);
        Bomb bomb23 = bombs3.get(1);
        assertSame(bomb21, bomb22);
        assertSame(bomb22, bomb23);
        assertSame(bomb23, bomb21);

        game.tick();
        game.tick();

        assertFalse(bomb11.isExploded());
        assertFalse(bomb12.isExploded());
        assertFalse(bomb13.isExploded());

        game.tick();

        assertTrue(bomb11.isExploded());
        assertTrue(bomb12.isExploded());
        assertTrue(bomb13.isExploded());

        assertFalse(bomb21.isExploded());
        assertFalse(bomb22.isExploded());
        assertFalse(bomb23.isExploded());

        game.tick();

        assertTrue(bomb21.isExploded());
        assertTrue(bomb22.isExploded());
        assertTrue(bomb23.isExploded());
    }

    @Test
    public void shouldReturnShouldNotSynchronizedBombsList_whenUseBoardApi() {
        hero.act();
        hero.right();
        game.tick();

        List<Bomb> bombs1 = bomberman.getBombs();
        assertEquals(1, bombs1.size());

        game.tick();
        game.tick();
        game.tick();
        game.tick();

        List<Bomb> bombs2 = bomberman.getBombs();
        assertEquals(0, bombs2.size());
        assertEquals(0, bombs1.size());
        assertSame(bombs1, bombs2);
    }

    @Test
    public void shouldChangeBlast_whenUseBoardApi() {  // TODO а нода вообще такое? стреляет по перформансу перекладывать объекты и усложняет код
        hero.act();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        List<Blast> blasts1 = bomberman.getBlasts();
        List<Blast> blasts2 = bomberman.getBlasts();
        List<Blast> blasts3 = bomberman.getBlasts();
        assertSame(blasts1, blasts2);
        assertSame(blasts2, blasts3);
        assertSame(blasts3, blasts1);

        Point blast11 = blasts1.get(0);
        Point blast12 = blasts2.get(0);
        Point blast13 = blasts3.get(0);
        assertSame(blast11, blast12);
        assertSame(blast12, blast13);
        assertSame(blast13, blast11);

        Point blast21 = blasts1.get(1);
        Point blast22 = blasts2.get(1);
        Point blast23 = blasts3.get(1);
        assertSame(blast21, blast22);
        assertSame(blast22, blast23);
        assertSame(blast23, blast21);
    }

    @Test
    public void shouldNoChangeWall_whenUseBoardApi() {
        givenBoardWithWalls();

        Walls walls1 = bomberman.getWalls();
        Walls walls2 = bomberman.getWalls();
        Walls walls3 = bomberman.getWalls();
        assertNotSame(walls1, walls2);
        assertNotSame(walls2, walls3);
        assertNotSame(walls3, walls1);

        Iterator<Wall> iterator1 = walls1.iterator();
        Iterator<Wall> iterator2 = walls2.iterator();
        Iterator<Wall> iterator3 = walls3.iterator();

        PointImpl wall11 = iterator1.next();
        PointImpl wall12 = iterator2.next();
        PointImpl wall13 = iterator3.next();
        assertNotSame(wall11, wall12);
        assertNotSame(wall12, wall13);
        assertNotSame(wall13, wall11);

        PointImpl wall21 = iterator1.next();
        PointImpl wall22 = iterator2.next();
        PointImpl wall23 = iterator3.next();
        assertNotSame(wall21, wall22);
        assertNotSame(wall22, wall23);
        assertNotSame(wall23, wall21);
    }

    // в настройках уровня так же есть и разрущающиеся стены
    @Test
    public void shouldRandomSetDestroyWalls_whenStart() {
        givenBoardWithDestroyWalls();

        asrtBrd("#####\n" +
                "#   #\n" +
                "# # #\n" +
                "#☺  #\n" +
                "#####\n");
    }

    // они взрываются от ударной волны
    @Test
    public void shouldDestroyWallsDestroyed_whenBombExploded() {
        givenBoardWithDestroyWalls();

        hero.act();
        goOut();

        asrtBrd("#####\n" +
                "#  ☺#\n" +
                "# # #\n" +
                "#1  #\n" +
                "#####\n");

        game.tick();

        asrtBrd("#####\n" +
                "#  ☺#\n" +
                "#҉# #\n" +
                "H҉҉ #\n" +
                "#H###\n");
    }

    private void dice(Dice dice, int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    // появляются чертики, их несоклько за игру
    // каждый такт чертики куда-то рендомно муваются
    // если бомбермен и чертик попали в одну клетку - бомбермен умирает
    @Test
    public void shouldRandomMoveMonster() {
        givenBoardWithMeatChopper(11);
        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 1, Direction.DOWN.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼&☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 1);
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 0, Direction.LEFT.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼       & ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼&        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 1, Direction.RIGHT.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼ &       ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 0, Direction.LEFT.value());
        game.tick();
        game.tick();

        dice(meatChppperDice, Direction.LEFT.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼&        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, Direction.DOWN.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼&☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼Ѡ        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        Assert.assertTrue(game.isGameOver());
        verify(listener).event(BombermanEvents.KILL_BOMBERMAN);
    }

    private void givenBoardWithMeatChopper(int size) {
        dice(meatChppperDice, size - 2, size - 2);

        Field temp = mock(Field.class);
        when(temp.size()).thenReturn(size);
        MeatChoppers walls = new MeatChoppers(new OriginalWalls(v(size)), temp, v(1), meatChppperDice);
        bombermans = mock(List.class);
        when(bombermans.contains(anyObject())).thenReturn(false);
        when(temp.getBombermans()).thenReturn(bombermans);
        withWalls(walls);
        walls.regenerate();
        givenBoard(size);

        dice(meatChppperDice, 1, Direction.UP.value());  // Чертик будет упираться в стенку и стоять на месте
    }

    // чертик умирает, если попадает под взывающуюся бомбу
    @Test
    public void shouldDieMonster_whenBombExploded() {
        SIZE = 11;
        givenBoardWithMeatChopper(SIZE);

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼        &☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, 1, Direction.DOWN.value());
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        dice(meatChppperDice, 1, Direction.LEFT.value());
        game.tick();
        game.tick();
        hero.act();
        hero.up();
        game.tick();
        hero.up();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼1 &      ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼҉☼ ☼ ☼ ☼ ☼\n" +
                "☼҉x       ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");

        dice(meatChppperDice, SIZE - 2, SIZE - 2, Direction.DOWN.value());
        game.tick();

        asrtBrd("☼☼☼☼☼☼☼☼☼☼☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼&☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼☺        ☼\n" +
                "☼ ☼ ☼ ☼ ☼ ☼\n" +
                "☼         ☼\n" +
                "☼☼☼☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldFireEventWhenKillBomberman() {
        shouldKillBoomberman_whenBombExploded();

        verify(listener).event(BombermanEvents.KILL_BOMBERMAN);
    }

    @Test
    public void shouldNoEventsWhenBombermanNotMove() {
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldFireEventWhenKillWall() {
        givenBoardWithDestroyWallsAt(0, 0);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "#☺   \n");

        hero.act();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "H҉҉ ☺\n");

        verify(listener).event(BombermanEvents.KILL_DESTROY_WALL);
    }

    static class DestroyWallAt extends WallsDecorator {

        public DestroyWallAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new DestroyWall(x, y));
        }

        @Override
        public Wall destroy(int x, int y) {   // неразрушаемая стенка
            return walls.get(x, y);
        }

    }

    private void givenBoardWithDestroyWallsAt(int x, int y) {
        withWalls(new DestroyWallAt(x, y, new WallsImpl()));
        givenBoard(SIZE);
    }

    @Test
    public void shouldFireEventWhenKillMeatChopper() {
        givenBoardWithMeatChopperAt(0, 0);

        hero.act();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "x҉҉ ☺\n");

        verify(listener).event(BombermanEvents.KILL_MEAT_CHOPPER);
    }


    static class MeatChopperAt extends WallsDecorator {

        public MeatChopperAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new MeatChopper(x, y));
        }

        @Override
        public Wall destroy(int x, int y) {   // неубиваемый монстрик
            return walls.get(x, y);
        }

    }

    private void givenBoardWithMeatChopperAt(int x, int y) {
        withWalls(new MeatChopperAt(x, y, new WallsImpl()));
        givenBoard(SIZE);
    }

    @Test
    public void shouldCalculateMeatChoppersAndWallKills() {
        withWalls(new MeatChopperAt(0, 0, new DestroyWallAt(0, 1, new MeatChopperAt(0, 2, new DestroyWallAt(0, 3, new WallsImpl())))));
        givenBoard(SIZE);
        canDropBombs(4);
        bombsPower(1);

        asrtBrd("     \n" +
                "#    \n" +
                "&    \n" +
                "#    \n" +
                "&☺   \n");

        hero.act();
        hero.up();
        game.tick();

        hero.act();
        hero.up();
        game.tick();

        hero.act();
        hero.up();
        game.tick();

        hero.act();
        hero.up();
        game.tick();
        asrtBrd(" ☺   \n" +
                "#4   \n" +
                "&3   \n" +
                "#2   \n" +
                "&1   \n");
        assertScores(0, 0);

        hero.right();
        game.tick();
        asrtBrd("  ☺  \n" +
                "#3   \n" +
                "&2   \n" +
                "#1   \n" +
                "x҉҉  \n");
        assertScores(1, 1);

        game.tick();
        asrtBrd("  ☺  \n" +
                "#2   \n" +
                "&1   \n" +
                "H҉҉  \n" +
                "&҉   \n");
        assertScores(2, 2);

        game.tick();
        asrtBrd("  ☺  \n" +
                "#1   \n" +
                "x҉҉  \n" +
                "#҉   \n" +
                "&    \n");
        assertScores(3, 3);

        game.tick();
        asrtBrd(" ҉☺  \n" +
                "H҉҉  \n" +
                "&҉   \n" +
                "#    \n" +
                "&    \n");
        assertScores(4, 4);

        hero.left();
        game.tick();
        hero.down();
        hero.act();
        game.tick();
        asrtBrd("     \n" +
                "#☻   \n" +
                "&    \n" +
                "#    \n" +
                "&    \n");
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        asrtBrd(" ҉   \n" +
                "HѠ҉  \n" +
                "&҉   \n" +
                "#    \n" +
                "&    \n");
        assertScores(0, 5);

        assertBombermanDie();

        initBomberman();
        game.tick();
        game.newGame();
        asrtBrd("     \n" +
                "#    \n" +
                "&    \n" +
                "#    \n" +
                "&☺   \n");

        hero.act();
        hero.right();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();
        game.tick();
        asrtBrd("     \n" +
                "#    \n" +
                "&    \n" +
                "#҉   \n" +
                "x҉҉☺ \n");
        assertScores(1, 5);
    }

    private void assertScores(int expectedCurrent, int expectedMax) {
        assertEquals(expectedCurrent, game.getCurrentScore());
        assertEquals(expectedMax, game.getMaxScore());
    }

    // если я двинулся за пределы стены и тут же поставил бомбу, то бомба упадет на моем текущем месте
    @Test
    public void shouldMoveOnBoardAndDropBombTogether() {
        givenBoardWithOriginalWalls();
        hero.up();
        game.tick();
        hero.up();
        game.tick();

        hero.left();
        hero.act();
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼☻  ☼\n" +
                "☼ ☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    // чертик  может ходить по бомбам
    @Test
    public void shouldMonsterCanMoveOnBomb() {
        givenBoardWithMeatChopper(SIZE);

        asrtBrd("☼☼☼☼☼\n" +
                "☼  &☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        hero.up();
        game.tick();
        hero.up();
        game.tick();
        hero.right();
        hero.act();
        game.tick();
        hero.left();
        game.tick();
        hero.down();
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼ 2&☼\n" +
                "☼☺☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        dice(meatChppperDice, 1, Direction.LEFT.value());
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼ & ☼\n" +
                "☼☺☼ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldStopBlastWhenBombermanOrDestroyWalls() {
        bombsPower(5);
        withWalls(new DestroyWallAt(3, 0, new WallsImpl()));
        givenBoard(7);

        hero.act();
        hero.up();
        game.tick();
        hero.up();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("       \n" +
                "       \n" +
                "       \n" +
                "       \n" +
                "Ѡ      \n" +
                "҉      \n" +
                "҉҉҉H   \n");
    }

    @Test
    public void shouldStopBlastWhenMeatChopper() {
        bombsPower(5);
        withWalls(new MeatChopperAt(4, 0, new WallsImpl()));
        givenBoard(7);

        hero.act();
        hero.up();
        game.tick();
        hero.up();
        game.tick();
        hero.up();
        game.tick();
        hero.right();
        game.tick();
        game.tick();

        asrtBrd("       \n" +
                "҉      \n" +
                "҉      \n" +
                "҉☺     \n" +
                "҉      \n" +
                "҉      \n" +
                "҉҉҉҉x  \n");
    }

    @Test
    public void shouldMeatChopperAppearAfterKill() {
        bombsPower(3);
        dice(meatChppperDice, 3, 0, Direction.DOWN.value());
        withWalls(new MeatChoppers(new WallsImpl(), bomberman, v(1), meatChppperDice));
        givenBoard(SIZE);

        hero.act();
        hero.up();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉҉҉x \n");

        dice(meatChppperDice, 2, 2, Direction.DOWN.value());
        game.tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺&  \n" +
                "     \n");
    }

    @Test
    public void shouldMeatChopperNotAppearWhenDestroyWall() {
        bombsPower(3);
        dice(meatChppperDice, 4, 4, Direction.RIGHT.value());
        withWalls(new MeatChoppers(new DestroyWallAt(3, 0, new WallsImpl()), bomberman, v(1), meatChppperDice));
        givenBoard(SIZE);

        hero.act();
        hero.up();
        game.tick();
        hero.right();
        game.tick();
        game.tick();
        game.tick();
        game.tick();

        asrtBrd("    &\n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉҉҉H \n");

        dice(meatChppperDice, Direction.DOWN.value());
        game.tick();

        asrtBrd("     \n" +
                "    &\n" +
                "     \n" +
                " ☺   \n" +
                "   # \n");
    }

    // Чертик не может появится на бомбере!
    @Test
    public void shouldMeatChopperNotApperOnBommber() {
        shouldMonsterCanMoveOnBomb();
        hero.down();
        game.tick();

        asrtBrd("☼☼☼☼☼\n" +
                "☼x҉҉☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        dice(meatChppperDice, 1, 2, Direction.DOWN.value());
        when(bombermans.contains(anyObject())).thenReturn(true);

        try {
            game.tick();
//            fail(); // TODO надо с этим разобраться
        } catch (IllegalStateException e) {
            assertEquals("java.lang.IllegalStateException: Dead loop at MeatChoppers.regenerate!", e.toString());
        }

        asrtBrd("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☼ ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

    }


    // под разрущающейся стенкой может быть приз - это специальная стенка
    // появляется приз - увеличение длительности ударной волны - его может бомбермен взять и тогда ударная волна будет больше
    // появляется приз - хождение сквозь разрушающиеся стенки - взяв его, бомбермен может ходить через тенки
    // чертики тоже могут ставить бомбы

}
