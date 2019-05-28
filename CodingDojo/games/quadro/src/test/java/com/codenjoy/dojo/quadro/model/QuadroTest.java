package com.codenjoy.dojo.quadro.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.quadro.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class QuadroTest {

    private Quadro game;
    private Hero hero1;
    private Hero hero2;
    private Dice dice;
    private EventListener listener1;
    private EventListener listener2;
    private Player player1;
    private Player player2;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int... ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        Level level = new LevelImpl(board);
        game = new Quadro(level, dice);
        listener1 = mock(EventListener.class);
        listener2 = mock(EventListener.class);
        player1 = new Player(listener1);
        player2 = new Player(listener2);
        game.newGame(player1);
        game.newGame(player2);
        hero1 = game.getHeroes().get(0);
        hero2 = game.getHeroes().get(1);
    }

    private void assertE(String expected) {
        assertE(expected, player1);
        assertE(expected, player2);
    }

    private void assertE(String expected, Player player) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // Поле 9х9 изначально пустое
    @Test
    public void shouldFieldAtStart9x9() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    // Игрок может походить
    @Test
    public void shouldAddChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    ");
    }

    // Игрок не может походить если он один на борде
    @Test
    public void shouldNotTurnWhenALone() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
        game.remove(player2);

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    // Игрок может походить на столбец, где есть фишки
    @Test
    public void shouldAddChipOnChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    ");

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    " +
                "    o    ");
    }

    // Второй игрок может походить
    @Test
    public void shouldHero2AddChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(0);
        game.tick();

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "o   x    ");
    }

    // Желтый игрок победил когда в ряд 4 его фишки вертикально
    @Test
    public void shouldWinVertical_yellow() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    " +
                "    o    " +
                "    o    ");

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    " +
                "    o    " +
                "    o    " +
                "    o    ");

        verify(listener1).event(Events.WIN);
        verify(listener2).event(Events.LOOSE);
    }

    // Ничего, когда в ряд 4 фишки желтого игрока вертикально но есть между чужая фишка
    @Test
    public void shouldNothing_verticalYellowHasSeparated4() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    " +
                "    o    " +
                "    x    " +
                "    o    ");

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "    o    " +
                "    o    " +
                "    o    " +
                "    x    " +
                "    o    ");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    // Красный игрок победил когда в ряд 4 его фишки вертикально
    @Test
    public void shouldWinVertical_red() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                " x       " +
                " x       " +
                " x       ");

        hero1.act(0);
        game.tick();

        hero2.act(1);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                " x       " +
                " x       " +
                " x       " +
                "ox       ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // Первым ходит желтый
    @Test
    public void shouldYellowGoesFirst() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        game.tick();
        hero1.act(5);
        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "     o   ");

        hero1.act(5);
        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    xo   ");
    }

    // Первым ходит желтый даже если они оба походят в одну клетку
    @Test
    public void shouldYellowGoesFirst_eventIfRedGoesToTheSameCell() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(4);
        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    ");
    }

    // Игроки ходят по очереди, сначала желтый, потом красный
    @Test
    public void shouldHero1AddChip_thenShouldHero1AddChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    ");

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "    o    ");
    }

    // Если игрок пропустил ход, то на следующий ход ходит он же
    @Test
    public void shouldAddChip_afterMissedAct() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        game.tick();
        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    o    ");
    }

    // Если игрок пропустил 10 ходов, то он проиграл
    @Test
    public void shouldLoose_whenMissed10Act() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        for (int i = 0; i < 9; i++) {
            game.tick();
            verifyNoMoreInteractions(listener1);
            verifyNoMoreInteractions(listener2);
        }
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // Нельзя положить фишку, когда столбик полностью забит
    @Test
    public void shouldNotAddChip_whenNoMoreSpaceInColumn() {
        givenFl("      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  ");

        hero1.act(6);
        game.tick();

        assertE("      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  ");

        hero2.act(5); // ходит все равно hero1
        game.tick();

        assertE("      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  " +
                "      o  ");
    }

    // Валидация параметров для хода
    @Test
    public void shouldSkipNotValidActCommands_parameterShouldBePositive () {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(-2);
        game.tick();

        hero2.act(1); // ходит все равно первый
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    @Test
    public void shouldSkipNotValidActCommands_notExistParameter() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act();
        game.tick();

        hero2.act(1); // ходит все равно первый
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    @Test
    public void shouldSkipNotValidActCommands_moreThanSize() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(12);
        game.tick();

        hero2.act(1); // ходит все равно первый
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    @Test
    public void shouldSkipNotValidActCommands_moreThanOneParameter() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        hero1.act(1, 1);
        game.tick();

        hero2.act(1); // ходит все равно первый
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    // Игра не начинается, хотя идут тики, пока нет двух игроков
    @Test
    public void waitForTwoPlayers() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        game.remove(player2);
        hero1.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        game.newGame(player2);
        hero2 = player2.hero;
        hero1.act(4);
        game.tick();
        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "    o    ");

        game.remove(player1);
        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "    o    ");
    }

    // Ничья когда нет места для хода
    @Test
    public void shouldDraw_whenNoMoreSpace() {
        givenFl("oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo");

        game.tick();

        verify(listener1).event(Events.DRAW);
        verify(listener2).event(Events.DRAW);
    }

    // Начиная с третьего игрока идут спектаторы?, либо не добавлять?
    // Исключение
    @Test(expected = IllegalStateException.class)
    public void exception_whenAddThirdPlayer() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");

        game.newGame(new Player(mock(EventListener.class)));
    }

    // Если ничья, то игра начинается снова; Через TIMEOUT_TICKS тиков
    @Test
    public void gameOverDraw() {
        givenFl(" xoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo" +
                "xoxoxoxox" +
                "oxoxoxoxo");

        hero1.act(0);
        game.tick();

        verify(listener1).event(Events.DRAW);
        verify(listener2).event(Events.DRAW);

        for (int i = 0; i < Quadro.TIMEOUT_TICKS; i++) {
            game.tick();
        }

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    // Если кто-то победил, то игра начинается снова; Через TIMEOUT_TICKS тиков
    @Test
    public void gameOverWin() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "o        " +
                "o        " +
                "o        ");

        hero1.act(0);
        game.tick();

        verify(listener1).event(Events.WIN);
        verify(listener2).event(Events.LOOSE);

        for (int i = 0; i < Quadro.TIMEOUT_TICKS; i++) {
            game.tick();
        }

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         ");
    }

    // Игрок победил когда в ряд 4 его фишки горизонтально
    @Test
    public void shouldWinHorizontal() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                " o       " +
                " o       " +
                " xx xxo  ");

        hero1MakeSomeStep();

        hero2.act(3);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                " o       " +
                " o       " +
                "oxxxxxo  ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    private void hero1MakeSomeStep() {
        hero1.act(0); // любой ход, нам интересно что сделает hero2
        game.tick();
    }

    // Игрок победил когда в ряд 4 его фишки по диагонали вправо вверх
    @Test
    public void shouldWin_directionBottomLeftToTopRightActive() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xx    " +
                "  xxo    " +
                "  ooooo  ");

        hero1MakeSomeStep();

        hero2.act(1);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xx    " +
                "  xxo    " +
                "oxooooo  ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // Ничего, когда в ряд 4 фишки игрока по диагонали вправо вверх, но есть разрыв чужой фишкой
    @Test
    public void shouldNothing_directionBottomLeftToTopRightActiveAndAlienChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "     x   " +
                "    oo   " +
                "   xxo   " +
                "  xxoo   " +
                "  ooooo  ");

        hero1MakeSomeStep();

        hero2.act(1);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "     x   " +
                "    oo   " +
                "   xxo   " +
                "  xxoo   " +
                "oxooooo  ");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    // Ничего, когда в ряд 4 фишки игрока горизонтально, но есть разрыв двумя чужими фишками
    @Test
    public void shouldNothing_directionHorizontalAndAlienTwoChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                " xxo oxx ");

        hero1MakeSomeStep();

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "oxxoxoxx ");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    // Ничего, когда в ряд 4 фишки игрока горизонтально, но есть разрыв чужой фишкой
    @Test
    public void shouldNothing_directionHorizontalAndAlienChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "  xx oxx ");

        hero1MakeSomeStep();

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "o xxxoxx ");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    // Ничего, когда в ряд 4 фишки игрока по диагонали, но есть разрыв чужой фишкой
    @Test
    public void shouldNothing_directionDiagonalAndAlienChip() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "      x  " +
                "      x  " +
                "    ooo  " +
                "   xoxo  " +
                "  xxooxx ");

        hero1MakeSomeStep();

        hero2.act(5);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "      x  " +
                "     xx  " +
                "    ooo  " +
                "   xoxo  " +
                "o xxooxx ");

        verifyNoMoreInteractions(listener1);
        verifyNoMoreInteractions(listener2);
    }

    // Игрок победил когда в ряд 4 его фишки по диагонали влево вниз
    @Test
    public void shouldWin_directionTopRightToBottomLeftActive() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "   xx    " +
                "  xxo    " +
                " xooooo  ");

        hero1MakeSomeStep();

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xx    " +
                "  xxo    " +
                "oxooooo  ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // Игрок победил когда в ряд 4 его фишки по диагонали влево вверх
    @Test
    public void shouldWin_directionBottomRightToTopLeftActive() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xxx   " +
                "  oxoox  " +
                " xooooo  ");

        hero1MakeSomeStep();

        hero2.act(7);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xxx   " +
                "  oxoox  " +
                "oxooooox ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }

    // Игрок победил когда в ряд 4 его фишки по диагонали вправо вниз
    @Test
    public void shouldWin_directionTopLeftToBottomRightActive() {
        givenFl("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "   xxx   " +
                "  oxoox  " +
                " xooooox ");

        hero1MakeSomeStep();

        hero2.act(4);
        game.tick();

        assertE("         " +
                "         " +
                "         " +
                "         " +
                "         " +
                "    x    " +
                "   xxx   " +
                "  oxoox  " +
                "oxooooox ");

        verify(listener1).event(Events.LOOSE);
        verify(listener2).event(Events.WIN);
    }
}
