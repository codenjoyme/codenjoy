package com.epam.dojo.expansion.model;

/*-
 * #%L
 * iCanCode - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 EPAM
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
import com.codenjoy.dojo.services.DoubleDirection;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.services.Events;
import com.epam.dojo.expansion.services.Levels;
import com.epam.dojo.expansion.services.Printer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.PointImpl.pt;
import static junit.framework.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class ExpansionTest {

    public static final int FIRE_TICKS = 6;
    private Expansion game;
    private Printer printer;

    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private Player otherPlayer;

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

    private void givenFl(String... boards) {
        Levels.VIEW_SIZE = Levels.VIEW_SIZE_TESTING;
        List<ILevel> levels = createLevels(boards);

        game = new Expansion(levels, dice, Expansion.SINGLE);
        listener = mock(EventListener.class);
        player = new Player(listener, new ProgressBar(game, null));
        game.newGame(player);
        this.hero = game.getHeroes().get(0);

        printer = new Printer(game, Levels.size());
    }

    private void givenFlWithOnePlayer(String... boards) {
        Levels.VIEW_SIZE = Levels.VIEW_SIZE_TESTING;
        List<ILevel> levels = createLevels(boards);

        game = new Expansion(levels, dice, Expansion.MULTIPLE);
        listener = mock(EventListener.class);
        player = new Player(listener, new ProgressBar(game, null));
        otherPlayer = new Player(mock(EventListener.class), new ProgressBar(game, null));
        game.newGame(player);
        game.newGame(otherPlayer);
        this.hero = game.getHeroes().get(0);

        printer = new Printer(game, Levels.size());
    }

    private List<ILevel> createLevels(String[] boards) {
        List<ILevel> levels = new LinkedList<ILevel>();
        for (String board : boards) {
            ILevel level = new LevelImpl(board);
            levels.add(level);
        }
        return levels;
    }

    private void assertL(String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.getBoardAsString(1, player).getLayers().get(0)));
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                TestUtils.injectN(printer.getBoardAsString(2, player).getLayers().get(1)));
    }

    private void assertF(String expected) {
        assertEquals(expected, printer.getBoardAsString(2, player).getLayers().get(2).replace('"', '\''));
    }

    @Test
    public void shouldFieldAtStart() {
        // given
        givenFl("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║........E│" +
                "└─────────┘");

        // then
        assertL("╔═════════┐" +
                "║.........│" +
                "║.S.┌─╗...│" +
                "║...│ ║...│" +
                "║.┌─┘ └─╗.│" +
                "║.│     ║.│" +
                "║.╚═┐ ╔═╝.│" +
                "║...│ ║...│" +
                "║...╚═╝...│" +
                "║........E│" +
                "└─────────┘");

        assertE("-----------" +
                "-----------" +
                "--☺--------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------" +
                "-----------");

        assertF("[{'region':'[2,8]','count':10}]");
    }

    @Test
    public void shouldIncreaseExistingForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), 1));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':11}]");

        // when
        hero.increase(new Forces(pt(2, 2), 3));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':14}]");

        // when
        hero.increase(new Forces(pt(2, 2), 5));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':19}]");
    }

    @Test
    public void shouldIncreaseExistingForces_notMoreThan() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), 100));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':20}]");
    }

    @Test
    public void shouldDoNothingWhenNoCommands() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':10}]");

        assertL("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }

    @Test
    public void shouldMoveForces_down() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':'[2,2]','count':4}," +
                " {'region':'[2,1]','count':6}]");
    }

    @Test
    public void shouldMoveForces_up() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.UP));
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,3]','count':6}," +
                " {'region':'[2,2]','count':4}]");
    }

    @Test
    public void shouldMoveForces_left() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.LEFT));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[1,2]','count':6}," +
                " {'region':'[2,2]','count':4}]");
    }

    @Test
    public void shouldMoveForces_right() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':'[2,2]','count':4}," +
                " {'region':'[3,2]','count':6}]");
    }

    @Test
    public void shouldMoveForces_leftUp() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.LEFT_UP));
        game.tick();

        // then
        assertE("-----" +
                "-☺---" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[1,3]','count':6}," +
                " {'region':'[2,2]','count':4}]");
    }

    @Test
    public void shouldMoveForces_leftDown() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.LEFT_DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-☺---" +
                "-----");

        assertF("[{'region':'[2,2]','count':4}," +
                " {'region':'[1,1]','count':6}]");
    }

    @Test
    public void shouldMoveForces_rightUp() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT_UP));
        game.tick();

        // then
        assertE("-----" +
                "---☺-" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':'[3,3]','count':6}," +
                " {'region':'[2,2]','count':4}]");
    }

    @Test
    public void shouldMoveForces_rightDown() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT_DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "---☺-" +
                "-----");

        assertF("[{'region':'[2,2]','count':4}," +
                " {'region':'[3,1]','count':6}]");
    }

    @Test
    public void shouldMoveForces_allSidesSameTime() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':'[2,2]','count':10}]");

        // when
        hero.movements(
                new Forces(pt(2, 2), 1, DoubleDirection.LEFT),
                new Forces(pt(2, 2), 1, DoubleDirection.RIGHT),
                new Forces(pt(2, 2), 1, DoubleDirection.UP),
                new Forces(pt(2, 2), 1, DoubleDirection.DOWN),
                new Forces(pt(2, 2), 1, DoubleDirection.RIGHT_DOWN),
                new Forces(pt(2, 2), 1, DoubleDirection.RIGHT_UP),
                new Forces(pt(2, 2), 1, DoubleDirection.LEFT_DOWN),
                new Forces(pt(2, 2), 1, DoubleDirection.LEFT_UP)
        );
        game.tick();

        // then
        assertE("-----" +
                "-☺☺☺-" +
                "-☺☺☺-" +
                "-☺☺☺-" +
                "-----");

        assertF("[{'region':'[1,3]','count':1}," +
                " {'region':'[2,3]','count':1}," +
                " {'region':'[3,3]','count':1}," +
                " {'region':'[1,2]','count':1}," +
                " {'region':'[2,2]','count':2}," +
                " {'region':'[3,2]','count':1}," +
                " {'region':'[1,1]','count':1}," +
                " {'region':'[2,1]','count':1}," +
                " {'region':'[3,1]','count':1}]");
    }

    @Test
    public void shouldCantMoveForcesOnWall() {
        // given
        givenFl("╔═┐" +
                "║S│" +
                "└─┘");

        assertF("[{'region':'[1,1]','count':10}]");

        // when
        hero.movements(
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT),
                new Forces(pt(1, 1), 1, DoubleDirection.UP),
                new Forces(pt(1, 1), 1, DoubleDirection.DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT_DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT_UP),
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT_DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT_UP)
        );
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        assertF("[{'region':'[1,1]','count':10}]");

        assertL("╔═┐" +
                "║S│" +
                "└─┘");
    }

    @Test
    public void shouldCantMoveForcesOnWall_otherWalls() {
        // given
        givenFl("┌─╗" +
                "│S│" +
                "╚═╝");

        assertF("[{'region':'[1,1]','count':10}]");

        // when
        hero.movements(
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT),
                new Forces(pt(1, 1), 1, DoubleDirection.UP),
                new Forces(pt(1, 1), 1, DoubleDirection.DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT_DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.RIGHT_UP),
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT_DOWN),
                new Forces(pt(1, 1), 1, DoubleDirection.LEFT_UP)
        );
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        assertF("[{'region':'[1,1]','count':10}]");

        assertL("┌─╗" +
                "│S│" +
                "╚═╝");
    }

    @Test
    public void shouldStopWhenWalls2() {
        // given
        givenFl(".│." +
                "┘S└" +
                ".─.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldStopWhenWalls3() {
        // given
        givenFl(".┌." +
                "╝S╗" +
                ".╚.");

        // when
        hero.left();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.right();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.up();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");

        // when
        hero.down();
        game.tick();

        // then
        assertE("---" +
                "-☺-" +
                "---");
    }

    @Test
    public void shouldWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");
    }

    @Test
    public void shouldNewGameAfterWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        assertTrue(hero.isWin());

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertL("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertFalse(hero.isWin());
    }

    @Test
    public void shouldStartInNewPlaceWhenAct() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║S..│" +
                "║...│" +
                "└───┘");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        // when
        hero.reset();

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");

        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");
    }

    @Test
    public void demo1_generalWay() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldNextLevelWhenFinishCurrent() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        // when
        hero.right();
        game.tick();

        verify(listener).event(Events.WIN(0));

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                        "║SE│" +
                        "║..│" +
                        "└──┘",
                "╔══┐" +
                        "║.S│" +
                        "║.E│" +
                        "└──┘",
                "╔══┐" +
                        "║..│" +
                        "║ES│" +
                        "└──┘",
                "╔══┐" +
                        "║E.│" +
                        "║S.│" +
                        "└──┘"
        );

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when done 1 level - go to 2
        hero.right();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when done 2 level - go to 3
        hero.down();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when done 3 level - go to 4
        hero.left();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when done 4 level - start 4 again
        hero.up();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when done 4 level - start 4 again
        hero.up();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Test
    public void shouldChangeLevelWhenAllLevelsAreDone() {
        // given
        shouldAllLevelsAreDone();

        // when try to change level 1  - success
        hero.loadLevel(0);
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 2  - success
        hero.loadLevel(1);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 3  - success
        hero.loadLevel(2);
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 500 - fail
        hero.right();
        game.tick();
        hero.loadLevel(500);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 2 - success
        hero.loadLevel(1);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(1);
        game.tick();
        hero.down();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseWhenGoToMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(2);
        game.tick();
        hero.left();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(3);
        game.tick();
        hero.right();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 3 (previous) - success
        hero.loadLevel(2);
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");
    }

    @Test
    public void shouldResetLevelWhenAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘",
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘",
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘"
        );

        hero.down();
        game.tick();

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when done 1 level - go to 2
        hero.right();
        game.tick();
        game.tick();
        hero.left();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when done 2 level - go to 3
        hero.down();
        game.tick();
        game.tick();
        hero.up();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when done 3 level - go to 4
        hero.left();
        game.tick();
        game.tick();
        hero.right();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when done 4 level - start 4 again
        hero.up();
        game.tick();
        game.tick();
        hero.right();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 1  - success
        hero.loadLevel(0);
        game.tick();
        game.tick();
        hero.down();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level 2  - success
        hero.loadLevel(1);
        game.tick();
        game.tick();
        hero.left();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when try to change level 3  - success
        hero.loadLevel(2);
        game.tick();
        game.tick();
        hero.up();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();
        game.tick();
        hero.right();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 500 - fail
        hero.right();
        game.tick();
        game.tick();
        hero.loadLevel(500);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        // when try to change level 2 - success
        hero.loadLevel(1);
        game.tick();
        game.tick();
        hero.left();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

    }

    @Test
    public void shouldSelectLevelWhenNotAllLevelsAreDone() {
        // given
        givenFl("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘",
                "╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘",
                "╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘",
                "╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘"
        );

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when done level 1 - go to level 2
        hero.right();
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        // when try to change level to 1 - success
        hero.loadLevel(0);
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level to 2 - success
        hero.loadLevel(1);
        game.tick();
        hero.left();
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level to 3 - fail
        hero.loadLevel(2);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level 4 - fail
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when try to change level to 1 - success
        hero.loadLevel(0);
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");
    }

    @Test
    public void shouldAfterNextLevelHeroCanMove() {
        // given
        shouldNextLevelWhenFinishCurrent();

        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "----" +
                "----");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");
    }

    @Test
    public void shouldDoubleScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(1));

        assertL("     " +
                "╔═══┐" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "---☺-" +
                "-----" +
                "-----");
    }

    @Test
    public void shouldDoubleScoreWhenGetTwoGold() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        // then
        verify(listener).event(Events.WIN(2));

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "----☺-" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNoScoreWhenGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        hero.right();
        game.tick();

        // then
        verifyNoMoreInteractions(listener);
    }

    @Test
    public void shouldHideGoldWhenGet() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S.$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "--☺---" +
                "------" +
                "------" +
                "------");

        // when
        hero.right();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldReNewGoldWhenReset() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        hero.right();
        game.tick();

        hero.right();
        game.tick();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "---☺--" +
                "------" +
                "------" +
                "------");

        // when
        hero.reset();
        game.tick();

        // then
        assertL("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺----" +
                "------" +
                "------" +
                "------");
    }

    private void ticks(int count) {
        for (int i = 0; i < count; i++) {
            game.tick();
        }
    }

    @Test
    public void shouldScrollingView() {
        //given
        givenFl("╔══════════════════┐" +
                "║S.................│" +
                "║..................│" +
                "║....┌──╗..........│" +
                "║....│  ║..........│" +
                "║..┌─┘  └─╗........│" +
                "║..│      ║........│" +
                "║..│      ║........│" +
                "║..╚═┐  ╔═╝........│" +
                "║....│  ║..........│" +
                "║....╚══╝..........│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║..................│" +
                "║.................E│" +
                "└──────────────────┘");

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        for (int i = 0; i < 10; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("╔═══════════════" +
                "║S.............." +
                "║..............." +
                "║....┌──╗......." +
                "║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║...............");

        //when
        hero.right();
        game.tick();

        //then
        assertL("════════════════" +
                "S..............." +
                "................" +
                "....┌──╗........" +
                "....│  ║........" +
                "..┌─┘  └─╗......" +
                "..│      ║......" +
                "..│      ║......" +
                "..╚═┐  ╔═╝......" +
                "....│  ║........" +
                "....╚══╝........" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");


        //when
        for (int i = 0; i < 12; ++i) {
            hero.right();
            game.tick();
        }

        //then
        assertL("═══════════════┐" +
                "...............│" +
                "...............│" +
                ".┌──╗..........│" +
                ".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│");

        //when
        hero.left();
        game.tick();

        for (int i = 0; i < 20; ++i) {
            hero.down();
            game.tick();
        }

        assertL(".│  ║..........│" +
                "─┘  └─╗........│" +
                "      ║........│" +
                "      ║........│" +
                "═┐  ╔═╝........│" +
                ".│  ║..........│" +
                ".╚══╝..........│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "...............│" +
                "..............E│" +
                "───────────────┘");

        //when
        for (int i = 0; i < 20; ++i) {
            hero.left();
            game.tick();
        }

        assertL("║....│  ║......." +
                "║..┌─┘  └─╗....." +
                "║..│      ║....." +
                "║..│      ║....." +
                "║..╚═┐  ╔═╝....." +
                "║....│  ║......." +
                "║....╚══╝......." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "║..............." +
                "└───────────────");
    }


    @Test
    public void shouldStartOnCenter() {
        //given
        givenFl("╔════════════════════════════════════┐" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║.................S..................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║....................................│" +
                "║...................................E│" +
                "└────────────────────────────────────┘");

        //then
        assertL("................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "........S......." +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");
    }

    @Test
    public void shouldMovableBoxOnBoard() {
        // given
        givenFl("╔════┐" +
                "║SB..│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnWall() {
        // given
        givenFl("╔════┐" +
                "║..SB│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║..S.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "---☺B-" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnOtherBox() {
        // given
        givenFl("╔════┐" +
                "║SBB.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S...│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺BB--" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldNotMovedBoxWhenHeroPushItOnGold() {
        // given
        givenFl("╔════┐" +
                "║SB$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        // when
        hero.right();
        game.tick();

        // then
        assertL("╔════┐" +
                "║S.$.│" +
                "║....│" +
                "║....│" +
                "║....│" +
                "└────┘");

        assertE("------" +
                "-☺B---" +
                "------" +
                "------" +
                "------" +
                "------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case1() {
        // given
        when(dice.next(anyInt())).thenReturn(0);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-☺-----" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case2() {
        // given
        when(dice.next(anyInt())).thenReturn(1);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-----☺-" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case3() {
        // given
        when(dice.next(anyInt())).thenReturn(2);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-☺-----" +
                "-------");
    }

    @Test
    public void shouldStartWhenSeveralStarts_case4() {
        // given
        when(dice.next(anyInt())).thenReturn(3);

        givenFl("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        // when
        game.tick();

        // then
        assertL("╔═════┐" +
                "║S...S│" +
                "║.....│" +
                "║.....│" +
                "║.....│" +
                "║S...S│" +
                "└─────┘");

        assertE("-------" +
                "-------" +
                "-------" +
                "-------" +
                "-------" +
                "-----☺-" +
                "-------");
    }

    /*@Test
    public void shouldFlyOnOtherPlayer() {
        // given
        givenFlWithOnePlayer(
                "╔═══┐" +
                "║..˂│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        ticks((FIRE_TICKS - 1) * 2);
        hero.right();
        ticks(2);

        // then
        assertE("-----" +
                "-----" +
                "--X☺-" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║..◄│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        // when
        hero.jump();
        hero.left();
        ticks(2);

        // then
        assertE("-----" +
                "--←--" +
                "--X☺-" +
                "-----" +
                "-----");

        assertL("╔═══┐" +
                "║..◄│" +
                "║.S.│" +
                "║...│" +
                "└───┘");
    }*/
}
