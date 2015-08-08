package com.codenjoy.dojo.pong.model;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class PongTest {

    private Pong game;
//    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
//        Hero hero = level.getHero().get(0);

        game = new Pong(level, dice);
//        listener = mock(EventListener.class);
//        player = new Player(listener);
//        game.newGame(player);
//        player.hero = hero;
//        hero.init(game);
//        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }


    @Test
    public void shouldFieldAtStart() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallDownOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(Direction.DOWN));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallUpLeftTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(Direction.LEFT));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallRightOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(Direction.RIGHT));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|     o  |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallUpOnTick() {
        givenFl("----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
        game.setBallDirection(new BallDirection(Direction.UP));
        game.tick();

        assertE("----------" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------");
    }

    @Test
    public void shouldBallRevertDirectionUpToDownBeforeWall() {
        givenFl("          " +
                "----------" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");

        game.setBallDirection(new BallDirection(Direction.UP));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }


    @Test
    public void shouldBallRevertDirectionDownToUpBeforeWall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "----------" +
                "          " +
                "          ");

        game.setBallDirection(new BallDirection(Direction.DOWN));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "----------" +
                "          " +
                "          ");
    }

    @Test
    public void shouldBallRevertDirectionFromLeftToRightBeforeWall() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|o       |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.LEFT));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "| o      |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallRevertDirectionToRightBeforeWall() {
        givenFl("          " +
                "----------" +
                "|       o|" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.RIGHT));
        game.tick();

        assertE("          " +
                "----------" +
                "|      o |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionRightUp() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.RIGHT, Direction.UP));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionLeftUp() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.LEFT, Direction.UP));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|  o     |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionLeftDown() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.LEFT, Direction.DOWN));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|  o     |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

    @Test
    public void shouldBallChangeDiagonalDirectionRightDown() {
        givenFl("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|   o    |" +
                "|        |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
        game.setBallDirection(new BallDirection(Direction.RIGHT, Direction.DOWN));

        game.tick();

        assertE("          " +
                "----------" +
                "|        |" +
                "|        |" +
                "|        |" +
                "|    o   |" +
                "|        |" +
                "|        |" +
                "----------" +
                "          ");
    }

}
















