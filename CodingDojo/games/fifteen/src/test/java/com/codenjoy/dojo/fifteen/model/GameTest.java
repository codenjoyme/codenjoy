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

import com.codenjoy.dojo.fifteen.services.GameSettings;
import com.codenjoy.dojo.games.fifteen.Element;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private Fifteen game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer;
    private GameSettings settings;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printer = new PrinterFactoryImpl();
        settings = new GameSettings();
    }

    private void dice() {
        for (int i = 16; i > 0; i--) {
            when(dice.next(i)).thenReturn(i - 1);
        }
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        game = new Fifteen(level, dice, settings);
        listener = mock(EventListener.class);
        player = new Player(listener, settings);
        game.newGame(player);
        hero = player.getHero();
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
        assertEquals(true, hero.isAlive());

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

        assertEquals(false, hero.isAlive());
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
        assertEquals(
                "******" +
                "*abcd*" +
                "*efgh*" +
                "*ijkl*" +
                "*mno+*" +
                "******",
                new Randomizer().getRandomMap(dice));
    }

    //проверка на правильный расклад
    @Test
    public void testIsSolvabilityTrue() {
        List<Element> test = new LinkedList<Element>();
        test.add(Element.A);
        test.add(Element.B);
        test.add(Element.C);
        test.add(Element.D);
        test.add(Element.E);
        test.add(Element.F);
        test.add(Element.G);
        test.add(Element.H);
        test.add(Element.I);
        test.add(Element.G);
        test.add(Element.K);
        test.add(Element.L);
        test.add(Element.M);
        test.add(Element.N);
        test.add(Element.O);
        test.add(Element.HERO);
        boolean result = new Randomizer().canBeSolved(test);
        assertEquals(true, result);
    }

    //проверка на неправильный расклад
    @Test
    public void testIsSolvabilityFalse() {
        List<Element> test = new LinkedList<>();
        test.add(Element.A);
        test.add(Element.B);
        test.add(Element.C);
        test.add(Element.D);
        test.add(Element.E);
        test.add(Element.F);
        test.add(Element.G);
        test.add(Element.H);
        test.add(Element.I);
        test.add(Element.G);
        test.add(Element.K);
        test.add(Element.L);
        test.add(Element.M);
        test.add(Element.O);
        test.add(Element.N);
        test.add(Element.HERO);
        boolean result = new Randomizer().canBeSolved(test);
        assertEquals(false, result);
    }
}
