package com.codenjoy.dojo.crossyroad.model;

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
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

//@Ignore
public class CrossyroadTest {

    private Crossyroad game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
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
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Crossyroad(dice, level);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");
    }

    // есть карта с камнями
    @Test
    public void shouldFieldWithStoneAtStart() {
        givenFl("#     #" +
                "#  0  #" +
                "#     #" +
                "#    0#" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

        assertE("#     #" +
                "#  0  #" +
                "#     #" +
                "#    0#" +
                "#     #" +
                "#     #" +
                "#  ▼  #");
    }

    // есть карта с машинами
    @Test
    public void shouldFieldWithCarsAtStart() {
        givenFl("#     #" +
                "#> >  #" +
                "#     #" +
                "#    <#" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

        assertE("#     #" +
                "#> >  #" +
                "#     #" +
                "#    <#" +
                "#     #" +
                "#     #" +
                "#  ▼  #");
    }

    // машины должны двигаться
    @Test
    public void shouldMoveLeftCarRightToLeft() {
        givenFl("#     #" +
                "#  <  #" +
                "#     #" +
                "#    <#" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
        game.tick();
        assertE("#     #" +
                "# <   #" +
                "#     #" +
                "#   < #" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
    }

    @Test
    public void shouldMoveRightCarLeftToRight() {
        givenFl("#     #" +
                "#  >  #" +
                "#     #" +
                "#    >#" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
        game.tick();
        assertE("#     #" +
                "#   > #" +
                "#     #" +
                "#>    #" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
    }

    // машины должны появляться на другом краю карты, если вышли за границу
    @Test
    public void shouldMoveThroughBoundCarRightToLeft() {
        givenFl("#     #" +
                "#     #" +
                "#<    #" +
                "#     #" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
        game.tick();
        assertE("#     #" +
                "#     #" +
                "#    <#" +
                "#     #" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
    }

    @Test
    public void shouldMoveThroughBoundCarLeftToRight() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#    >#" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
        game.tick();
        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#>    #" +
                "#  ▼  #" +
                "#     #" +
                "#     #");
    }

    // машины должны убираться с доски
    @Test
    public void shouldDestroyAllCarsOutOfBorders() {
        givenFl("#     #" +
                "#>    #" +
                "#     #" +
                "#     #" +
                "#  ▼  #" +
                "#     #" +
                "#<<<<<#");
        hero.up();
        game.tick();
        assertE("#     #" +
                "#     #" +
                "# >   #" +
                "#     #" +
                "#  ▲  #" +
                "#     #" +
                "#     #");

        Assert.assertEquals(1, game.getCars().size());
    }

    // герой должен телепортироваться, если врезается в препятствие
    @Test
    public void shouldHeroTeleportWhenCollision() {
        givenFl("#     #" +
                "#  ▼< #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #");
        game.tick();
        assertE("#     #" +
                "#  <  #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

    }

    // герой должен телепортироваться, если врезается в стену
    @Test
    public void shouldHeroTeleportWhenWallCollision() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#▼    #");
        hero.left();
        game.tick();
        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

    }

    // герой должен ходить
    @Test
    public void shouldWalk() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

        hero.left();
        game.tick();

        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "# ◄   #");
        ;

        hero.right();
        game.tick();

        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ►  #");
        ;

        hero.up();
        game.tick();

        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▲  #");
        ;
    }

    // объекты должны смещаться вниз, когда герой идет вверх
    @Test
    public void shouldObjectsMoveWhenUp() {
        givenFl("#     #" +
                "#0    #" +
                "# >   #" +
                "#     #" +
                "#  <  #" +
                "#     #" +
                "#  ▼  #");
        ;

        hero.up();
        game.tick();
        assertE("#     #" +
                "#     #" +
                "#0    #" +
                "#  >  #" +
                "#     #" +
                "# <   #" +
                "#  ▲  #");
        ;
    }

    // герой должен телепортироваться, если не идет вперед пять ходов
    @Test
    public void shouldHeroTeleportWhenDontGoUpFiveTurns() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#    ▼#");
        hero.left();
        game.tick();
        hero.left();
        game.tick();
        hero.left();
        game.tick();
        hero.left();
        game.tick();
        hero.right();
        game.tick();
        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");


    }

    // проверка счетчика тиков
    @Test
    public void shouldMakeTenTicks() {
        givenFl("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");
        dice(2 - 1, 10);
        for (int i = 0; i < 10; i++) {
            game.tick();
        }

        assertE("#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#     #" +
                "#  ▼  #");

        Assert.assertEquals(10, game.getTickCounter());
    }

//    @Test
//    public void shouldMakePlatformNoHigherThanY4WhenDice5() {
//        givenFl("#     #" +
//                "#     #" +
//                "#     #" +
//                "#     #" +
//                "#<<   #" +
//                "#     #" +
//                "#  ▼  #");
//
//        dice(5 - 1, 1);
//        game.tick();
//
//        assertE("#     #" +
//                "#     #" +
//                "#    <#" +
//                "#     #" +
//                "#<   <#" +
//                "#     #" +
//                "#  ▼  #");
//    }

//    @Test
//    public void shouldNotMakeSecondPlatformHigherThanY4WhenDice5() {
//        givenFl("#######" +
//                "       " +
//                "       " +
//                "☺      " +
//                "=======" +
//                "       " +
//                "#######");
//
//        dice(5 - 1, 2);
//        game.tick();
//        game.tick();
//        dice(6 - 1, 1);
//        game.tick();
//
//        assertE("#######" +
//                "       " +
//                "    == " +
//                "☺      " +
//                "====   " +
//                "       " +
//                "#######");
//    }
//
//    @Test
//    public void shouldMake3Platforms() {
//        givenFl("#######" +
//                "       " +
//                "       " +
//                "☺      " +
//                "=======" +
//                "       " +
//                "#######");
//
//        dice(4 - 1, 1);
//        game.tick();
//        game.tick();
//        dice(1 - 1, 1);
//        game.tick();
//        game.tick();
//        dice(4 - 1, 1);
//        game.tick();
//
//        assertE("#######" +
//                "       " +
//                "  =    " +
//                "☺     =" +
//                "==     " +
//                "    =  " +
//                "#######");
//    }
//
//    @Test
//    public void shouldFullGameWithPlatformsAndJumps() {
//        givenFl("####################" +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                " =                  " +
//                "☺             =     " +
//                " =                  " +
//                " =  ====   ==       " +
//                "####################");
//
//        dice(1 - 1, 10);
//        game.tick();
//        game.tick();
//        game.tick();
//        hero.up();
//        game.tick();
//
//        assertE("####################" +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "☺         =         " +
//                "                    " +
//                "====   ==       ====" +
//                "####################");
//
//        game.tick();
//        game.tick();
//        game.tick();
//        hero.up();
//        game.tick();
//        hero.up();
//        game.tick();
//
//        assertE("####################" +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "☺                   " +
//                "     =              " +
//                "                    " +
//                "  ==       =========" +
//                "####################");
//
//        game.tick();
//        game.tick();
//        game.tick();
//        hero.up();
//        game.tick();
//        hero.up();
//        game.tick();
//
//        assertE("####################" +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "                    " +
//                "☺                   " +
//                "=                ===" +
//                "                    " +
//                "      ==========    " +
//                "####################");
//    }

}
















