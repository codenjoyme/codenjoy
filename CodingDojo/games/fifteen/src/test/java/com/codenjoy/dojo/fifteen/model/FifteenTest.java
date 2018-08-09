package com.codenjoy.dojo.fifteen.model;

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

import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FifteenTest {

    private Fifteen game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice() {
        for (int i = 16; i > 0; i--) {
            when(dice.next(i)).thenReturn(i - 1);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        game = new Fifteen(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть поле карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");
    }

    @Test
    public void newGameTest() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijk+*" +
                "*mnol*" +
                "******");

        game.tick();
        assertTrue(hero.isAlive());

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijk+*" +
                "*mnol*" +
                "******");

        hero.down();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");

        assertFalse(hero.isAlive());
        game.newGame(player);

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijk+*" +
                "*mnol*" +
                "******");
    }

    //движение вверх
    @Test
    public void shouldMoveUp() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");

        hero.up();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijk+*" +
                "*mnol*" +
                "******");
    }

    //движение вниз
    @Test
    public void shouldMoveDown() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijk+*" +
                "*mnol*" +
                "******");

        hero.down();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");
    }

    //движение вправо
    @Test
    public void shouldMoveRight() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*m+no*" +
                "******");

        hero.right();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mn+o*" +
                "******");
    }

    //движение влево
    @Test
    public void shouldMoveLeft() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");

        hero.left();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mn+o*" +
                "******");
    }

    //в стену - нечего не делаем
    @Test
    public void shouldDoNothingWhenMoveToWall() {
        givenFl("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");

        hero.right();
        game.tick();

        assertE("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******");
    }

    // тест рандомной инициализации
    @Test
    public void testRandomInitialization() {
        dice();
        String ramdomMap = new Randomizer().getRamdomMap(dice);
        assertEquals("******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******", ramdomMap);
    }

    //проверка на правильный расклад
    @Test
    public void testIsSolvabilityTrue() {
        List<Elements> test = new LinkedList<Elements>();
        test.add(Elements.A);
        test.add(Elements.B);
        test.add(Elements.C);
        test.add(Elements.D);
        test.add(Elements.E);
        test.add(Elements.F);
        test.add(Elements.G);
        test.add(Elements.H);
        test.add(Elements.I);
        test.add(Elements.G);
        test.add(Elements.K);
        test.add(Elements.L);
        test.add(Elements.M);
        test.add(Elements.N);
        test.add(Elements.O);
        test.add(Elements.HERO);
        boolean result = new Randomizer().isSolvability(test);
        assertTrue(result);
    }

    //проверка на неправильный расклад
    @Test
    public void testIsSolvabilityFalse() {
        List<Elements> test = new LinkedList<Elements>();
        test.add(Elements.A);
        test.add(Elements.B);
        test.add(Elements.C);
        test.add(Elements.D);
        test.add(Elements.E);
        test.add(Elements.F);
        test.add(Elements.G);
        test.add(Elements.H);
        test.add(Elements.I);
        test.add(Elements.G);
        test.add(Elements.K);
        test.add(Elements.L);
        test.add(Elements.M);
        test.add(Elements.O);
        test.add(Elements.N);
        test.add(Elements.HERO);
        boolean result = new Randomizer().isSolvability(test);
        assertFalse(result);
    }
}
