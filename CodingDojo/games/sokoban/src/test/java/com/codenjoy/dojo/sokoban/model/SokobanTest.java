package com.codenjoy.dojo.sokoban.model;

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


import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.sokoban.helper.TextIOHelper;
import com.codenjoy.dojo.sokoban.services.Player;
import com.codenjoy.dojo.sokoban.model.game.Sokoban;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Box;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Hero;
import com.codenjoy.dojo.sokoban.model.itemsImpl.LevelImpl;
import com.codenjoy.dojo.sokoban.model.itemsImpl.Mark;
import com.codenjoy.dojo.utils.TestUtils;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SokobanTest {

    private Sokoban game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();
    private Box box;
    private Mark mark;

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

    private void givenF1(String board, int expectedMarksToWin) {
        LevelImpl level;
        if (expectedMarksToWin != 0)
            level = new LevelImpl(board, expectedMarksToWin);
        else
            level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Sokoban(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener,"PlayerFirst");
        game.newGame(player);
        player.hero = hero;

        hero.init(game);

        this.hero = game.getHeroes().get(0);
    }

    private void givenF(String board) {
        givenF1(board, 0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

//    DONE: initial map is created (map == walls && boxes && markes)
//    DONE: hero is moving in 4 directions (UP, DOWN, LEFT, RIGHT)
//    DONE: hero is not passing through the walls
//    DONE: hero can push boxes in 4 directions (UP, DOWN, LEFT, RIGHT)
//    DONE: hero cannot push boxes if wall is next element after a box in the direction of hero pushing.
//    TODO: if expected (definied by scenario) boxes in the marks == win event (in perspective == next level/scenario).
//    TODO: If all boxes not in the marks and in the corners - lose.

    // initial map is created (map == walls && boxes)
    @Test
    public void shouldFieldWithBoxesAtStart() {

        givenF("☼☼☼☼☼" +
                "☼■XX☼" +
                "☼ ■X☼" +
                "☼☺ ■☼" +
                "☼☼☼☼☼");
        assertE("☼☼☼☼☼" +
                "☼■XX☼" +
                "☼ ■X☼" +
                "☼☺ ■☼" +
                "☼☼☼☼☼");
    }

    // hero is moving in 4 directions (UP, DOWN, LEFT, RIGHT)
    @Test
    public void shouldMoveHeroin4Directioons() {
        givenF("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        hero.left();
        game.tick();
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        hero.up();
        game.tick();
        assertE("☼☼☼☼☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        hero.down();
        game.tick();
        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }

    //    hero is not passing through the walls
    @Test
    public void shouldNotMoveThroughTheWalls() {

        String testBoardInit = "☼☼☼" +
                "☼☺☼" +
                "☼☼☼";
        givenF(testBoardInit);

        hero.left();
        game.tick();
        assertE(testBoardInit);

        hero.right();
        game.tick();
        assertE(testBoardInit);

        hero.up();
        game.tick();
        assertE(testBoardInit);

        hero.down();
        game.tick();
        assertE(testBoardInit);
    }


    //hero can push boxes in 4 directions (UP, DOWN, LEFT, RIGHT)
    @Test
    public void shouldPushBoxesIn4Direcitons() {
        givenF("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ■■  ☼" +
                "☼ ■☺  ☼" +
                "☼  ■  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        hero.left();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼     ☼" +
                "☼ ■■  ☼" +
                "☼■☺   ☼" +
                "☼  ■  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        hero.up();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼ ■   ☼" +
                "☼ ☺■  ☼" +
                "☼■    ☼" +
                "☼  ■  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        hero.right();
        game.tick();
        assertE("☼☼☼☼☼☼☼" +
                "☼ ■   ☼" +
                "☼  ☺■ ☼" +
                "☼■    ☼" +
                "☼  ■  ☼" +
                "☼     ☼" +
                "☼☼☼☼☼☼☼");
        for (int i = 0; i < 2; i++) {
            hero.down();
            game.tick();
        }
        assertE("☼☼☼☼☼☼☼" +
                "☼ ■   ☼" +
                "☼   ■ ☼" +
                "☼■    ☼" +
                "☼  ☺  ☼" +
                "☼  ■  ☼" +
                "☼☼☼☼☼☼☼");
    }

    //hero cannot push boxes if wall is next element after a box in the direction of hero pushing.
    @Test
    public void shouldNotPushBoxesIn4DirecitonsIfNextIsWall() {
        String testBoardInit = "☼☼☼☼☼" +
                "☼ ■ ☼" +
                "☼■☺■☼" +
                "☼ ■ ☼" +
                "☼☼☼☼☼";
        givenF(testBoardInit);

        hero.left();
        game.tick();
        assertE(testBoardInit);

        hero.up();
        game.tick();
        assertE(testBoardInit);

        hero.right();
        game.tick();
        assertE(testBoardInit);

        for (int i = 0; i < 2; i++) {
            hero.down();
            game.tick();
        }
        assertE(testBoardInit);
    }


    //    DONE: if expected (definied by scenario) boxes in the marks == win event (in perspective == next level/scenario).
    @Test
    public void shouldWinIfExpectedValueOfBoxInMarksIsReached() {
        String testBoardInit = "☼☼☼☼☼" +
                "☼☺■X☼" +
                "☼  ■☼" +
                "☼   ☼" +
                "☼☼☼☼☼";
        givenF(testBoardInit);
        hero.right();
        game.tick();
        assertTrue("\nWe need to have same expected and real marks, but" +
                "\n expected marks to fill is: " + game.getExpectedMarksToWin() + " real value is:" + game.getRealMarksToWin(), game.isWon());
    }

    @Test
    public void shouldFillBoxOnTheMark() {
        String testBoardInit = "☼☼☼☼☼" +
                "☼☺■X☼" +
                "☼  ■☼" +
                "☼   ☼" +
                "☼☼☼☼☼";
        String testBoardAfter = "☼☼☼☼☼" +
                "☼ ☺*☼" +
                "☼  ■☼" +
                "☼   ☼" +
                "☼☼☼☼☼";
        givenF(testBoardInit);
        hero.right();
        game.tick();
        assertE(testBoardAfter);
    }

    @Test
    public void shouldRestoreInitMarkWhenBoxMoveFromTheBoxOnTheMark() {
        String testBoardInit = "☼☼☼☼☼" +
                "☼☺* ☼" +
                "☼  ■☼" +
                "☼   ☼" +
                "☼☼☼☼☼";
        String testBoardAfter = "☼☼☼☼☼" +
                "☼ ☺■☼" +
                "☼  ■☼" +
                "☼   ☼" +
                "☼☼☼☼☼";
        givenF(testBoardInit);
        hero.right();
        game.tick();
        assertE(testBoardAfter);
    }

    @Test
    public void shouldrestoreMarkAndMove2moves() {
        String testBoardInit = "☼☼☼☼☼☼" +
                "☼☺*  ☼" +
                "☼  ■ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼";
        String testBoardAfter = "☼☼☼☼☼☼" +
                "☼ X☺■☼" +
                "☼  ■ ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼";
        givenF(testBoardInit);
        for (int i = 0; i<2; i++){
            hero.right();
            game.tick();
        }
        assertE(testBoardAfter);
    }

    @Test
    public void shouldReadlevelFromRtfResource() {
        String testBoardInit = TextIOHelper.getStringFromResourcesRtf(0);
        givenF(testBoardInit);
        assertE(testBoardInit);
    }

    @Test
    public void shouldReadLevelFromRtfResourceWithSpacesAtTheEnd5FirstLevels() {
        for (int i = 1; i < 6; i++) {
            String testBoardInit = TextIOHelper.getStringFromResourcesRtf(i);
            givenF(testBoardInit);
            assertE(testBoardInit);
        }
    }



//    @Test
//    public void justToChecksSomeBehaviours() {
//        String testBoardInit =
//                "☼☼☼☼☼☼☼☼☼"+
//                "☼ ■     ☼"   +
//                "☼■☺■X   ☼"  +
//                "☼ ■     ☼"   +
//                "☼       ☼"    +
//                "☼       ☼"    +
//                "☼       ☼"    +
//                "☼       ☼"    +
//                        "☼☼☼☼☼☼☼☼☼";
//        givenF(testBoardInit);
//
//        for (int i = 0; i < 3; i++) {
//            hero.right();
//            game.tick();
//        }
//        assertE(testBoardInit);
//    }
}

