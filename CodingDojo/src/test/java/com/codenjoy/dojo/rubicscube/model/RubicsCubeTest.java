package com.codenjoy.dojo.rubicscube.model;

import com.codenjoy.dojo.loderunner.model.LoderunnerTest;
import com.codenjoy.dojo.rubicscube.model.command.B;
import com.codenjoy.dojo.rubicscube.services.RubicsCubeEvents;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.Printer;
import com.codenjoy.dojo.snake.model.artifacts.Element;
import org.apache.commons.io.input.BOMInputStream;
import org.junit.Test;

import static junit.framework.Assert.*;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class RubicsCubeTest {

    public static final String INITIAL =
            "   WWW      " +
            "   WWW      " +
            "   WWW      " +
            "BBBOOOGGGRRR" +
            "BBBOOOGGGRRR" +
            "BBBOOOGGGRRR" +
            "   YYY      " +
            "   YYY      " +
            "   YYY      " +
            "            " +
            "            " +
            "            ";

    private static final int CLOCKWISE = 1;
    private static final int CONTR_CLOCKWISE = -1;
    private static final int TWICE = 2;

    private static final int FRONT = 2;
    private static final int RIGHT = 3;
    private static final int LEFT = 1;
    private static final int BACK = 4;
    private static final int UP = 5;
    private static final int DOWN = 6;

    private RubicsCube game;
    private EventListener listener;
    private Player player;
    private Joystick joystick;
    private RandomCommand command;

    private void givenFl(String command) {
        this.command = mock(RandomCommand.class);
        when(this.command.next()).thenReturn(command);

        game = new RubicsCube(this.command);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        joystick = player.getJoystick();
    }

    private void assertE(String expected) {
        LoderunnerTest.assertE(new Printer(game.getSize(),
                new Printer.GamePrinterSimpleImpl<Elements, Player>(game.reader(), player, Elements.NONE.ch())), expected);
    }

    // я вижу поле
    @Test
    public void shouldFieldAtStart() {
        givenFl("");

        assertE(INITIAL);
    }

    @Test
    public void shouldF() {
        givenFl("");

        joystick.act(FRONT, CLOCKWISE);
        game.tick();

        assertE("   WWW      " +
                "   WWW      " +
                "   BBB      " +
                "BBYOOOWGGRRR" +
                "BBYOOOWGGRRR" +
                "BBYOOOWGGRRR" +
                "   GGG      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");
    }

    @Test
    public void shouldGivenF_() {
        givenFl("F'");

        assertE("   WWW      " +
                "   WWW      " +
                "   GGG      " +
                "BBWOOOYGGRRR" +
                "BBWOOOYGGRRR" +
                "BBWOOOYGGRRR" +
                "   BBB      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(FRONT, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenF() {
        givenFl("F");

        assertE("   WWW      " +
                "   WWW      " +
                "   BBB      " +
                "BBYOOOWGGRRR" +
                "BBYOOOWGGRRR" +
                "BBYOOOWGGRRR" +
                "   GGG      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(FRONT, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenF2() {
        givenFl("F2");

        assertE("   WWW      " +
                "   WWW      " +
                "   YYY      " +
                "BBGOOOBGGRRR" +
                "BBGOOOBGGRRR" +
                "BBGOOOBGGRRR" +
                "   WWW      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(FRONT, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenR_() {
        givenFl("R'");

        assertE("   WWR      " +
                "   WWR      " +
                "   WWR      " +
                "BBBOOWGGGYRR" +
                "BBBOOWGGGYRR" +
                "BBBOOWGGGYRR" +
                "   YYO      " +
                "   YYO      " +
                "   YYO      " +
                "            " +
                "            " +
                "            ");

        joystick.act(RIGHT, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenR() {
        givenFl("R");

        assertE("   WWO      " +
                "   WWO      " +
                "   WWO      " +
                "BBBOOYGGGWRR" +
                "BBBOOYGGGWRR" +
                "BBBOOYGGGWRR" +
                "   YYR      " +
                "   YYR      " +
                "   YYR      " +
                "            " +
                "            " +
                "            ");

        joystick.act(RIGHT, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenR2() {
        givenFl("R2");

        assertE("   WWY      " +
                "   WWY      " +
                "   WWY      " +
                "BBBOORGGGORR" +
                "BBBOORGGGORR" +
                "BBBOORGGGORR" +
                "   YYW      " +
                "   YYW      " +
                "   YYW      " +
                "            " +
                "            " +
                "            ");

        joystick.act(RIGHT, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenD_() {
        givenFl("D'");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "OOOGGGRRRBBB" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(DOWN, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenD() {
        givenFl("D");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "RRRBBBOOOGGG" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(DOWN, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenD2() {
        givenFl("D2");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "GGGRRRBBBOOO" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(DOWN, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenU_() {
        givenFl("U'");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "RRRBBBOOOGGG" +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(UP, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenU() {
        givenFl("U");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "OOOGGGRRRBBB" +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(UP, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenU2() {
        givenFl("U2");

        assertE("   WWW      " +
                "   WWW      " +
                "   WWW      " +
                "GGGRRRBBBOOO" +
                "BBBOOOGGGRRR" +
                "BBBOOOGGGRRR" +
                "   YYY      " +
                "   YYY      " +
                "   YYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(UP, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenL_() {
        givenFl("L'");

        assertE("   OWW      " +
                "   OWW      " +
                "   OWW      " +
                "BBBYOOGGGRRW" +
                "BBBYOOGGGRRW" +
                "BBBYOOGGGRRW" +
                "   RYY      " +
                "   RYY      " +
                "   RYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(LEFT, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenL() {
        givenFl("L");

        assertE("   RWW      " +
                "   RWW      " +
                "   RWW      " +
                "BBBWOOGGGRRY" +
                "BBBWOOGGGRRY" +
                "BBBWOOGGGRRY" +
                "   OYY      " +
                "   OYY      " +
                "   OYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(LEFT, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenL2() {
        givenFl("L2");

        assertE("   YWW      " +
                "   YWW      " +
                "   YWW      " +
                "BBBROOGGGRRO" +
                "BBBROOGGGRRO" +
                "BBBROOGGGRRO" +
                "   WYY      " +
                "   WYY      " +
                "   WYY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(LEFT, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenB_() {
        givenFl("B'");

        assertE("   BBB      " +
                "   WWW      " +
                "   WWW      " +
                "YBBOOOGGWRRR" +
                "YBBOOOGGWRRR" +
                "YBBOOOGGWRRR" +
                "   YYY      " +
                "   YYY      " +
                "   GGG      " +
                "            " +
                "            " +
                "            ");

        joystick.act(BACK, CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenB() {
        givenFl("B");

        assertE("   GGG      " +
                "   WWW      " +
                "   WWW      " +
                "WBBOOOGGYRRR" +
                "WBBOOOGGYRRR" +
                "WBBOOOGGYRRR" +
                "   YYY      " +
                "   YYY      " +
                "   BBB      " +
                "            " +
                "            " +
                "            ");

        joystick.act(BACK, CONTR_CLOCKWISE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenB2() {
        givenFl("B2");

        assertE("   YYY      " +
                "   WWW      " +
                "   WWW      " +
                "GBBOOOGGBRRR" +
                "GBBOOOGGBRRR" +
                "GBBOOOGGBRRR" +
                "   YYY      " +
                "   YYY      " +
                "   WWW      " +
                "            " +
                "            " +
                "            ");

        joystick.act(BACK, TWICE);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldGivenHARD() {
        givenFl("D' B2 F2 U2 L2 R2 U' L' B' F D U' L' R F2 D2 U2 F'");

        assertE("   WOW      " +
                "   OWO      " +
                "   WOW      " +
                "BYBOBOGWGRGR" +
                "YBYBOBWGWGRG" +
                "BYBOBOGWGRGR" +
                "   YRY      " +
                "   RYR      " +
                "   YRY      " +
                "            " +
                "            " +
                "            ");

        joystick.act(FRONT, CLOCKWISE);
        game.tick();

        joystick.act(UP, TWICE);
        game.tick();

        joystick.act(DOWN, TWICE);
        game.tick();

        joystick.act(FRONT, TWICE);
        game.tick();

        joystick.act(RIGHT, CONTR_CLOCKWISE);
        game.tick();

        joystick.act(LEFT, CLOCKWISE);
        game.tick();

        joystick.act(UP, CLOCKWISE);
        game.tick();

        joystick.act(DOWN, CONTR_CLOCKWISE);
        game.tick();

        joystick.act(FRONT, CONTR_CLOCKWISE);
        game.tick();

        joystick.act(BACK, CLOCKWISE);
        game.tick();

        joystick.act(LEFT, CLOCKWISE);
        game.tick();

        joystick.act(UP, CLOCKWISE);
        game.tick();

        joystick.act(RIGHT, TWICE);
        game.tick();

        joystick.act(LEFT, TWICE);
        game.tick();

        joystick.act(UP, TWICE);
        game.tick();

        joystick.act(FRONT, TWICE);
        game.tick();

        joystick.act(BACK, TWICE);
        game.tick();

        joystick.act(DOWN, CLOCKWISE);
        game.tick();

        joystick.act(BACK, 0);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldCanOpenNumberTwice() {
        givenFl("");

        joystick.act(2, 1);
        game.tick();

        joystick.act(2, -1);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldSuccessEventWhenSuccess() {
        givenFl("R'");

        joystick.act(RIGHT, CLOCKWISE);
        game.tick();

        verify(listener).event(RubicsCubeEvents.SUCCESS);
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldIsGameOverWhenWin() {
        shouldSuccessEventWhenSuccess();

        assertTrue(game.isGameOver());

        game.newGame(player);

        assertFalse(game.isGameOver());

        game.tick();

        assertE("   WWR      " + // given R'
                "   WWR      " +
                "   WWR      " +
                "BBBOOWGGGYRR" +
                "BBBOOWGGGYRR" +
                "BBBOOWGGGYRR" +
                "   YYO      " +
                "   YYO      " +
                "   YYO      " +
                "            " +
                "            " +
                "            ");
    }

    @Test
    public void shouldCantGoTwicePerTact() {
        givenFl("");

        joystick.act(UP, CLOCKWISE); // игнорится
        joystick.act(RIGHT, TWICE);
        game.tick();

        assertE("   WWY      " +
                "   WWY      " +
                "   WWY      " +
                "BBBOORGGGORR" +
                "BBBOORGGGORR" +
                "BBBOORGGGORR" +
                "   YYW      " +
                "   YYW      " +
                "   YYW      " +
                "            " +
                "            " +
                "            ");
    }

    @Test
    public void shouldIgnoreBadCoordinates() {
        givenFl("");

        assertIgnoreCommand(0, 1);
        assertIgnoreCommand(-1, 1);
        assertIgnoreCommand(7, 1);
        assertIgnoreCommand(1, -2);
        assertIgnoreCommand(1, 3);
    }

    private void assertIgnoreCommand(int face, int count) {
        joystick.act(face, count);
        game.tick();

        assertE(INITIAL);
    }

    @Test
    public void shouldLooseWhenNewGame() {
        givenFl("");

        joystick.act(2, 1);
        game.tick();

        joystick.act(0); // просим новую игру
        game.tick();

        verify(listener).event(RubicsCubeEvents.FAIL);
        verifyNoMoreInteractions(listener);

        assertTrue(game.isGameOver());

        game.newGame(player);

        assertFalse(game.isGameOver());
        assertE(INITIAL);
    }

}