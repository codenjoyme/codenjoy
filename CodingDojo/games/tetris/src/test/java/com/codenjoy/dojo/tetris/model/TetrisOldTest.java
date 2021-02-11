//package com.codenjoy.dojo.tetris.model;
//
///*-
// * #%L
// * Codenjoy - it's a dojo-like platform from developers to developers.
// * %%
// * Copyright (C) 2016 Codenjoy
// * %%
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as
// * published by the Free Software Foundation, either version 3 of the
// * License, or (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public
// * License along with this program.  If not, see
// * <http://www.gnu.org/licenses/gpl-3.0.html>.
// * #L%
// */
//
//
//import com.codenjoy.dojo.services.EventListener;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.ArgumentMatchers;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.mockito.stubbing.OngoingStubbing;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertSame;
//import static org.fest.assertions.Assertions.assertThat;
//import static org.mockito.Mockito.*;
//
///**
// * @author serhiy.zelenin
// */
// TODO доделать этот тест
//@RunWith(MockitoJUnitRunner.class)
//public class TetrisOldTest {
//    public static final int WIDTH = 10;
//    public static final int CENTER_X = WIDTH /2 - 1;
//    public static final int TOP_Y = 20;
//    public static final int HEIGHT = 20;
//    @Mock
//    Glass glass;
//    @Captor ArgumentCaptor<Integer> xCaptor;
//    @Captor ArgumentCaptor<Integer> yCaptor;
//    @Captor ArgumentCaptor<Figure> figureCaptor;
//
//    //field value will be initialized in GameSetupRule
//    private Tetris game;
//
//    @Rule public GameSetupRule gameSetup = new GameSetupRule(Tetris.class);
//    private Figure letterIFigure;
//
//
//    @Before
//    public void setUp() throws Exception {
//        letterIFigure = new FigureImpl(0, 1, Type.I, "#", "#", "#", "#");
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldBeInInitialPositionWhenGameStarts() {
//        assertCoordinates(CENTER_X, TOP_Y);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldBeMovedLeftWhenAsked() {
//        hero().left();
//        game.tick();
//
//        assertCoordinates(CENTER_X - 1, TOP_Y - 1);
//    }
//
//    private Hero hero() {
//        return game.getPlayer().getHero();
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldChangePositionWhenOnlyNextStep(){
//        hero().left();
//
//        assertCoordinates(CENTER_X, TOP_Y);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldBeMovedRightWhenAsked() {
//        hero().right();
//        game.tick();
//
//        assertCoordinates(CENTER_X + 1, TOP_Y - 1);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldNotChangeCurrentFigureWhenNextStep(){
//        game.tick();
//
//        captureFigureAtValues();
//        List<Figure> allFigures = figureCaptor.getAllValues();
//        assertSame(allFigures.get(0), allFigures.get(1));
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldNotMoveOutWhenLeftSide(){
//        left(CENTER_X + 1);
//        game.tick();
//
//        assertCoordinates(0, TOP_Y - 1);
//    }
//
//
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldNotMoveOutWhenRightSide(){
//        right(CENTER_X + 2);
//        game.tick();
//
//        assertCoordinates(9, TOP_Y - 1);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties(left = 1)})
//    public void shouldIncludeFigureSizeWhenMoveLeft() {
//        left(CENTER_X + 1);
//        game.tick();
//
//        assertCoordinates(1, HEIGHT - 1);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties(right = 1)})
//    public void shouldIncludeFigureSizeWhenMoveRight() {
//        right(CENTER_X + 1);
//        game.tick();
//
//        assertCoordinates(9 - 1, HEIGHT - 1);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties(top = 1)})
//    public void shouldAdjustFigurePositionWhenFigureHasTopCells(){
//        assertCoordinates(CENTER_X, HEIGHT - 1);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties(bottom = 1), @FigureProperties(bottom = 2)})
//    public void shouldTakeNextFigureWhenCurrentIsDropped() {
//        hero().down();
//        game.tick();
//
//        assertCoordinates(CENTER_X, HEIGHT);
//        assertEquals(2, figureCaptor.getValue().bottom());
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties(bottom = HEIGHT), @FigureProperties(bottom = 1)})
//    public void shouldGameOverWhenGlassOverflown() {
//        glassToRejectFigure();
//        game.tick();
//
//        assertCoordinates(CENTER_X, HEIGHT);
//        assertGameOver();
//    }
//
//    private void verifyDroppedAt(int x, int y) {
//        verify(glass).drop(Matchers.<Figure>anyObject(), eq(x), eq(y));
//    }
//
//    private void assertGameOver() {
//        verify(glass).empty();
//    }
//
//    private void glassToRejectFigure() {
//        glassToAccept(false);
//    }
//
//    private void glassToAccept(boolean accept) {
//        when(glass.accept(Matchers.<Figure>anyObject(), anyInt(), anyInt())).thenReturn(accept);
//    }
//
//    private void glassToAcceptFigure() {
//        glassToAccept(true);
//    }
//
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldMoveLeftWhenAcceptOnly(){
//        rejectWhenCoordinates(CENTER_X - 1, HEIGHT);
//        acceptWhenCoordinates(CENTER_X, HEIGHT - 1);
//
//        left(1);
//        game.tick();
//
//        assertCoordinates(CENTER_X, HEIGHT - 1);
//    }
//
//    private OngoingStubbing<Boolean> acceptWhenCoordinates(int x, int y) {
//        return when(glass.accept(Matchers.<Figure>anyObject(), eq(x), eq(y))).thenReturn(true);
//    }
//
//    private OngoingStubbing<Boolean> rejectWhenCoordinates(int x, int y) {
//        return when(glass.accept(Matchers.<Figure>anyObject(), eq(x), eq(y))).thenReturn(false);
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties})
//    public void shouldMoveRightWhenAcceptOnly() {
//        rejectWhenCoordinates(CENTER_X + 1, HEIGHT);
//        acceptWhenCoordinates(CENTER_X, HEIGHT - 1);
//
//        right(1);
//        game.tick();
//
//        assertCoordinates(CENTER_X, HEIGHT - 1);
//    }
//
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties, @FigureProperties(left = 1)})
//    public void shouldTakeNextFigureWhenRejectedAfterNextStep(){
//        acceptWhenCoordinates(CENTER_X, HEIGHT);
//        rejectWhenCoordinates(CENTER_X, HEIGHT - 1);
//
//        game.tick();
//
//        captureFigureAtValues();
//        assertThat(yCaptor.getAllValues()).isEqualTo(Arrays.asList(HEIGHT, HEIGHT));
//        assertEquals(1, figureCaptor.getValue().left());
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties, @FigureProperties(left = 1)})
//    public void shouldOverflowWhenFigureRejectedAtFirstStep(){
//        rejectWhenCoordinates(CENTER_X, HEIGHT);
//
//        game.tick();
//
//        assertCoordinates(CENTER_X, HEIGHT);
//        assertGameOver();
//    }
//
//    @Test
//    @GivenFiguresInQueue({@FigureProperties, @FigureProperties(left = 1)})
//    public void shouldPerformDropWhenRejected(){
//        acceptWhenCoordinates(CENTER_X, HEIGHT);
//        rejectWhenCoordinates(CENTER_X, HEIGHT - 1);
//
//        game.tick();
//
//        verifyDroppedAt(CENTER_X, HEIGHT);
//
//    }
//
//    @Test
//    public void shouldRotateOnce(){
//        Tetris game = createGameWithOneFigureInQueue(letterIFigure);
//        glassToAcceptFigure();
//        game.tick();
//
//        hero().act(1);
//
//        captureFigureAtValues();
//        Figure capturedFigure = figureCaptor.getValue();
//        assertThat(capturedFigure.rowCodes(false)).isEqualTo(new int[]{0b001001001001});
//    }
//
//    @Test
//    public void shouldIgnoreRotationWhenNotAccepted(){
//        Tetris game = createGameWithOneFigureInQueue(letterIFigure);
//        glassToAcceptFigure();
//        game.tick();
//
//        glassToRejectFigure();
//        hero().act(1);
//
//        captureFigureAtValues();
//        Figure capturedFigure = figureCaptor.getValue();
//        assertThat(capturedFigure.rowCodes(false)).isEqualTo(new int[]{0b1, 0b1, 0b1, 0b1});
//    }
//
//    private Tetris createGameWithOneFigureInQueue(final Figure figure) {
//        FigureQueue figureQueue = mock(FigureQueue.class);
//        when(figureQueue.next()).thenReturn(figure);
//
//        Levels levels = mock(Levels.class);
//        Tetris game = new Tetris(levels, figureQueue, 10);
//
//        EventListener listener = mock(EventListener.class);
//        Player player = new Player(listener);
//        game.newGame(player);
//
//        return game;
//    }
//
//    private void assertCoordinates(int x, int y) {
//        captureFigureAtValues();
//        assertEquals(x, xCaptor.getValue().intValue());
//        assertEquals(y, yCaptor.getValue().intValue());
//    }
//
//    private void captureFigureAtValues() {
//        verify(glass, atLeastOnce()).figureAt(figureCaptor.capture(), xCaptor.capture(), yCaptor.capture());
//    }
//
//    private void left(int times) {
//        for (int i = 0; i< times;i++) {
//            hero().left();
//        }
//    }
//
//    private void right(int times) {
//        for (int i = 0; i< times;i++) {
//            hero().right();
//        }
//    }
//}
