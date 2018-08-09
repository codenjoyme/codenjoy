package com.codenjoy.dojo.minesweeper.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


import com.codenjoy.dojo.minesweeper.services.Events;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MinesweeperTest {

    private MockBoard game;
    private int size = 5;
    private List<Mine> mines;
    private int detectorCharge = 3;
    private EventListener listener;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    private void shouldSize(int size) {
        this.size = size;
    }

    private void shouldDetectorCharge(int charge) {
        this.detectorCharge = charge;
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼**☺☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1 ☼\n" +
                "☼*☺ ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveLeft() {
        game.sapper().left();
        game.tick();
    }

    private void moveDown() {
        game.sapper().down();
        game.tick();
    }

    private void moveRight() {
        game.sapper().right();
        game.tick();
    }

    @Test
    public void shouldLeaveEmptySpaceshouldWalkOnBoardDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1*☼\n" +
                "☼*1☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**☺☼\n" +
                "☼*1 ☼\n" +
                "☼*1 ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*☺*☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼☺ *☼\n" +
                "☼*1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  *☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldLeaveEmptySpace_shouldWalkOnBoardLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlag_whenSetLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldDie_whenSapperAtBombs() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldSaveCommandAndActAfterTick() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        game.sapper().right();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        game.tick();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldPrintAllBombs_whenSapperAtBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3));

        unbombUp();
        unbombDown();
        unbombLeft();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x☻☼\n" +
                "☼‼3Ѡ☼\n" +
                "☼ x☻☼\n" +
                "☼☼☼☼☼\n");

        assertTrue(game.isGameOver());
    }

    @Test
    public void shouldPrintBoard_whenNearSapperNoBombs() {
        shouldBoardWith(new Sapper(3, 3), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**☺☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperOneBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺1*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperTwoBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺2*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperThreeBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺3*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFourBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺4*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperFiveBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺5*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

    }

    @Test
    public void shouldPrintBoard_whenNearSapperSixBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺6*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperSevenBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼☺7*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldPrintBoard_whenNearSapperEightBombs() {
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 2), new Mine(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼☻☻☻☼\n" +
                "☼☻8☻☼\n" +
                "☼☻Ѡ☻☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombRightAndLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2), new Mine(1, 2));

        unbombRight();
        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺x☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombRight() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 1));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼ x ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombDown() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 1));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼*☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 3));

        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ x ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombUp() {
        shouldBoardWith(new Sapper(2, 2), new Mine(2, 3));

        unbombDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺*☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldSetFlagOnBomb_whenBombLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    private void assertWin() {
        assertTrue(game.isWin());
    }

    @Test
    public void shouldSetFlagOnEmptySpace_whenBombLeft() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼‼☺*☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();
    }

    @Test
    public void shouldWin_whenDestroyAllBombs() {
        detectorCharge = 8;
        shouldBoardWith(new Sapper(2, 2),
                new Mine(3, 3), new Mine(3, 2), new Mine(3, 1),
                new Mine(2, 1), new Mine(2, 3),
                new Mine(1, 3), new Mine(1, 2), new Mine(1, 1));

        unbombLeft();
        unbombDown();
        unbombRight();
        unbombUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼*‼*☼\n" +
                "☼‼☺‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveUp();
        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼‼☺‼☼\n" +
                "☼‼2‼☼\n" +
                "☼*‼*☼\n" +
                "☼☼☼☼☼\n");

        assertStillNotWin();

        moveDown();
        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼‼‼‼☼\n" +
                "☼‼2‼☼\n" +
                "☼*☺*☼\n" +
                "☼☼☼☼☼\n");

        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼xxx☼\n" +
                "☼x x☼\n" +
                "☼x☺x☼\n" +
                "☼☼☼☼☼\n");

        assertWin();
    }

    @Test
    public void shouldLeaveBombMap_whenWalkBetweenBombs() {
        shouldBoardWith(new Sapper(1, 1),
                new Mine(2, 3), new Mine(2, 2));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼☺**☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼1☺*☼\n" +
                "☼☼☼☼☼\n");

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼***☼\n" +
                "☼11☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼**☺☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");

        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼**☺☼\n" +
                "☼**2☼\n" +
                "☼111☼\n" +
                "☼☼☼☼☼\n");
    }

    private void moveUp() {
        game.sapper().up();
        game.tick();
    }

    private void assertStillNotWin() {
        assertFalse(game.isWin());
    }

    private void unbombUp() {
        game.sapper().act();
        moveUp();
    }

    private void unbombRight() {
        game.sapper().act();
        moveRight();
    }

    private void unbombDown() {
        game.sapper().act();
        moveDown();
    }

    private void unbombLeft() {
        game.sapper().act();
        moveLeft();
    }

    private void assertBoard(String expected) {
        assertEquals(expected, printerFactory.getPrinter(
                game.reader(), null).print());
    }

    private void shouldBoardWith(Sapper sapper, Mine... mines) {
        listener = mock(EventListener.class);
        game = new MockBoard(sapper, mines);
    }

    private class MockBoard extends Minesweeper {
        private Player player;

        public MockBoard(Sapper sapper, Mine...mines) {
            super(v(size), v(0), v(detectorCharge),
                    (count, board) -> new ArrayList<>());

            player = new Player(listener) {
                @Override
                public void newHero(Field field) {
                    hero = sapper;
                    hero.init(field);
                }
            };

            MinesweeperTest.this.mines = new LinkedList<>();
            MinesweeperTest.this.mines.addAll(Arrays.asList(mines));
            for (Mine mine : mines) {
                mine.init(this);
            }

            newGame(player);
        }

        @Override
        public List<Mine> getMines() {
            return MinesweeperTest.this.mines;
        }

        @Override
        public int getMinesCount() {
            return getMines().size();
        }
    }

    @Test
    public void shouldFireEvent_whenDie() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼ 11☼\n" +
                "☼ 1Ѡ☼\n" +
                "☼ 11☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.KILL_ON_MINE);
    }

    private void verifyEvents(Events... events) {
        for (Events event : events) {
            verify(listener).event(event);
        }
    }

    @Test
    public void shouldFireEvent_whenOpenSpace() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.CLEAN_BOARD);
    }

    @Test
    public void shouldNotFireEvent_whenReturnsHome() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*1☺☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.CLEAN_BOARD);

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺ ☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldFireEvent_whenNoMoreCharge() {
        detectorCharge = 3;
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1));

        unbombDown();
        unbombLeft();
        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼‼☺‼☼\n" +
                "☼☻‼ ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(3, Events.FORGET_CHARGE);
        verifyEvents(Events.NO_MORE_CHARGE);

        unbombUp();
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldPrintAllBoardBombs_whenNoMoreCharge() {
        detectorCharge = 2;
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 1), new Mine(1, 2), new Mine(1, 3), new Mine(3, 3), new Mine(2, 1));

        unbombDown();
        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼☻2☻☼\n" +
                "☼x☺1☼\n" +
                "☼☻x ☼\n" +
                "☼☼☼☼☼\n");
    }

    private void verifyEvents(int count, Events event) {
        verify(listener, times(count)).event(event);
    }

    @Test
    public void shouldFireEvent_whenCleanMine() {
        shouldBoardWith(new Sapper(2, 2), new Mine(3, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.DESTROY_MINE);
    }

    @Test
    public void shouldFireEvent_whenCleanAllMines() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼x☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(
                Events.DESTROY_MINE,
                Events.WIN);
    }

    @Test
    public void shouldOnlyOneFlagPerSpace() {
        shouldBoardWith(new Sapper(2, 2), new Mine(1, 2));

        unbombRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼***☼\n" +
                "☼*☺‼☼\n" +
                "☼***☼\n" +
                "☼☼☼☼☼\n");

        verifyEvents(Events.FORGET_CHARGE);

        unbombRight();

        verifyNoMoreInteractions(listener);
    }


    @Test
    public void shouldCantGoOnBoard() {
        shouldBoardWith(new Sapper(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveLeft();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveDown();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        moveRight();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        moveUp();

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }

    @Test
    public void shouldCantUnbombOnBoard() {
        shouldBoardWith(new Sapper(1, 1));

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        unbombLeft();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        unbombDown();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼☼☼☼☼\n");

        moveRight();
        moveRight();
        reset(listener);

        unbombRight();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼  ☺☼\n" +
                "☼☼☼☼☼\n");

        moveUp();
        moveUp();
        reset(listener);

        unbombUp();
        verifyNoMoreInteractions(listener);

        assertBoard(
                "☼☼☼☼☼\n" +
                "☼  ☺☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
}
