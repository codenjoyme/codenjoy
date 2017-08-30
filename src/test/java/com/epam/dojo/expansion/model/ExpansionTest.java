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
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.utils.TestUtils;
import com.epam.dojo.expansion.model.interfaces.ILevel;
import com.epam.dojo.expansion.model.items.Hero;
import com.epam.dojo.expansion.services.Events;
import com.epam.dojo.expansion.services.Levels;
import com.epam.dojo.expansion.services.Printer;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

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

        assertF("[{'region':{'x':2,'y':8},'count':10}]");
    }

    @Test
    public void shouldIncreaseExistingForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), 1));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':11}]");

        // when
        hero.increase(new Forces(pt(2, 2), 3));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':14}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':19}]");
    }

    @Test
    public void shouldIncreaseExistingForces_notMoreThan() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), 100));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':20}]");
    }

    @Test
    public void shouldDoNothingWhenNoCommands() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':2,'y':1},'count':6}]");
    }

    @Test
    public void shouldMoveForces_up() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.UP));
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':3},'count':6}," +
                " {'region':{'x':2,'y':2},'count':4}]");
    }

    @Test
    public void shouldMoveForces_left() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.LEFT));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':6}," +
                " {'region':{'x':2,'y':2},'count':4}]");
    }

    @Test
    public void shouldMoveForces_right() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':3,'y':2},'count':6}]");
    }

    @Test
    public void shouldMoveForces_leftUp() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.LEFT_UP));
        game.tick();

        // then
        assertE("-----" +
                "-☺---" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':3},'count':6}," +
                " {'region':{'x':2,'y':2},'count':4}]");
    }

    @Test
    public void shouldMoveForces_leftDown() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.LEFT_DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-☺---" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':1,'y':1},'count':6}]");
    }

    @Test
    public void shouldMoveForces_rightUp() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT_UP));
        game.tick();

        // then
        assertE("-----" +
                "---☺-" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':3,'y':3},'count':6}," +
                " {'region':{'x':2,'y':2},'count':4}]");
    }

    @Test
    public void shouldMoveForces_rightDown() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 6, DoubleDirection.RIGHT_DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "---☺-" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':3,'y':1},'count':6}]");
    }

    @Test
    public void shouldMoveForces_allSidesSameTime() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(
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

        assertF("[{'region':{'x':1,'y':3},'count':1}," +
                " {'region':{'x':2,'y':3},'count':1}," +
                " {'region':{'x':3,'y':3},'count':1}," +
                " {'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':2}," +
                " {'region':{'x':3,'y':2},'count':1}," +
                " {'region':{'x':1,'y':1},'count':1}," +
                " {'region':{'x':2,'y':1},'count':1}," +
                " {'region':{'x':3,'y':1},'count':1}]");
    }

    @Test
    public void shouldCantMoveForcesOnWall() {
        // given
        givenFl("╔═┐" +
                "║S│" +
                "└─┘");

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when
        hero.move(
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when
        hero.move(
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        assertL("┌─╗" +
                "│S│" +
                "╚═╝");
    }

    @Test
    public void shouldWin() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║.SE│" +
                "└───┘" +
                "     ");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertE("-----" +
                "-----" +
                "--☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':9}," +
                " {'region':{'x':3,'y':2},'count':1}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.RIGHT));
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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        hero.move(new Forces(pt(1, 2), 2, DoubleDirection.RIGHT));
        game.tick();

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.RIGHT));
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':8}," +
                " {'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':3,'y':2},'count':1}]");

        // when
        hero.reset();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':8}," +
                " {'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':3,'y':2},'count':1}]");

        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':10}]");
    }

    @Test
    public void shouldCantMoveForcesFromEmptySpace() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(
                new Forces(pt(1, 1), 6, DoubleDirection.DOWN),
                new Forces(pt(3, 3), 6, DoubleDirection.LEFT),
                new Forces(pt(1, 3), 6, DoubleDirection.UP),
                new Forces(pt(3, 1), 6, DoubleDirection.RIGHT),
                new Forces(pt(2, 1), 6, DoubleDirection.LEFT_UP),
                new Forces(pt(2, 3), 6, DoubleDirection.RIGHT_DOWN),
                new Forces(pt(1, 2), 6, DoubleDirection.RIGHT_UP),
                new Forces(pt(3, 2), 6, DoubleDirection.LEFT_DOWN)
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
    }

    @Test
    public void shouldCantMoveMoreThanExistingSoOneMustBeLeaved() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), 10, DoubleDirection.DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':9}]");
    }

    @Test
    public void shouldCantMoveMoreThanExistingSoOneMustBeLeaved_caseMultipleMovements() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(
                new Forces(pt(2, 2), 3, DoubleDirection.LEFT),
                new Forces(pt(2, 2), 3, DoubleDirection.RIGHT)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':3}," +
                " {'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':3,'y':2},'count':3}]");

        // when
        hero.move(
                new Forces(pt(1, 2), 5, DoubleDirection.UP),
                new Forces(pt(2, 2), 5, DoubleDirection.UP),
                new Forces(pt(3, 2), 5, DoubleDirection.UP)
        );
        game.tick();

        // then
        assertE("-----" +
                "-☺☺☺-" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':3},'count':2}," +
                " {'region':{'x':2,'y':3},'count':3}," +
                " {'region':{'x':3,'y':3},'count':2}," +
                " {'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':3,'y':2},'count':1}]");
    }

    // я могу переместить на то место где уже что-то есть, тогда армии сольются
    @Test
    public void shouldCantMergeForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(
                new Forces(pt(2, 2), 3, DoubleDirection.LEFT),
                new Forces(pt(2, 2), 3, DoubleDirection.RIGHT)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':3}," +
                " {'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':3,'y':2},'count':3}]");

        // when
        hero.move(
                new Forces(pt(1, 2), 2, DoubleDirection.RIGHT),
                new Forces(pt(3, 2), 2, DoubleDirection.LEFT)
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':8}," +
                " {'region':{'x':3,'y':2},'count':1}]");
    }

    // я не могу увеличить количество войск на пустом месте
    @Test
    public void shouldCantIncreaseNonExistingForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increase(
                new Forces(pt(1, 1), 1),
                new Forces(pt(1, 3), 5),
                new Forces(pt(3, 1), 10),
                new Forces(pt(3, 3), 100)
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
    }

    // если на месте осталось 1 войско и я увеличил в следующем тике, то сейчас я снова могу перемещать
    @Test
    public void shouldCanMoveForceAfterIncrease() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 9, DoubleDirection.DOWN));
        game.tick();

        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':9}]");

        // when
        hero.move(new Forces(pt(2, 2), 2, DoubleDirection.UP));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':9}]");

        // when
        hero.increaseAndMove(
                new Forces(pt(2, 2), 5),
                new Forces(pt(2, 2), 2, DoubleDirection.UP)
        );
        game.tick();

        // then
        assertE("-----" +
                "--☺--" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':3},'count':2}," +
                " {'region':{'x':2,'y':2},'count':4}," +
                " {'region':{'x':2,'y':1},'count':9}]");

    }

    // я не могу оперировать в перемещении отрицательным числом войск
    @Test
    public void shouldCantMoveNegativeForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(2, 2), -1, DoubleDirection.DOWN));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
    }

    // я не могу оперировать в добавлении отрицательного числа войск
    @Test
    public void shouldCantIncreaseNegativeForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), -1));
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
    }

    // если я делаю какие-то перемещения, то я не могу переместить с только что перемещенного до тика
    @Test
    public void shouldCantMoveJustMovedForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(
                new Forces(pt(2, 2), 4, DoubleDirection.DOWN), // can do this
                new Forces(pt(2, 1), 2, DoubleDirection.LEFT)  // cant do this
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':6}," +
                " {'region':{'x':2,'y':1},'count':4}]");
    }

    // не брать во внимание перемещения войск без указания direction
    @Test
    public void shouldCantMoveWithoutDirections() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.move(
                new Forces(pt(2, 2), 2, DoubleDirection.DOWN), // can do this
                new Forces(pt(2, 2), 2)  // cant do this
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':8}," +
                " {'region':{'x':2,'y':1},'count':2}]");
    }

    // не брать во внимания direction во время увеличения числа войск
    @Test
    public void shouldIgnoreDirectionWhenIncreaseForces() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increase(new Forces(pt(2, 2), 10, DoubleDirection.DOWN)); // ignore direction
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':20}]");
    }

    // я могу переместить в другое место только что выставленные войска
    @Test
    public void shouldCanMoveJustIncreased() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when
        hero.increaseAndMove(
                new Forces(pt(2, 2), 20),
                new Forces(pt(2, 2), 19, DoubleDirection.DOWN)
        );
        game.tick();

        // then
        assertE("-----" +
                "-----" +
                "--☺--" +
                "--☺--" +
                "-----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':19}]");
    }

    // я могу увеличивать армии всего на заданное число, а не каждую отдельную
    @Test
    public void shouldIncreaseExistingForces_notMoreThan_totalForAllArmies() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(
                new Forces(pt(2, 2), 1, DoubleDirection.LEFT),
                new Forces(pt(2, 2), 1, DoubleDirection.RIGHT)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':8}," +
                " {'region':{'x':3,'y':2},'count':1}]");

        // when
        hero.increase(
                new Forces(pt(1, 2), 4),
                new Forces(pt(2, 2), 4),
                new Forces(pt(3, 2), 4)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':5}," +
                " {'region':{'x':2,'y':2},'count':12}," +
                " {'region':{'x':3,'y':2},'count':3}]");
    }

    @Test
    public void shouldIncreaseExistingForces_notMoreThan_totalForAllArmies_case2() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║.S.│" +
                "║...│" +
                "└───┘");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(
                new Forces(pt(2, 2), 1, DoubleDirection.LEFT),
                new Forces(pt(2, 2), 1, DoubleDirection.RIGHT)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':8}," +
                " {'region':{'x':3,'y':2},'count':1}]");

        // when
        hero.increase(
                new Forces(pt(1, 2), 10),
                new Forces(pt(2, 2), 10),
                new Forces(pt(3, 2), 10)
        );
        game.tick();

        assertE("-----" +
                "-----" +
                "-☺☺☺-" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':11}," +
                " {'region':{'x':2,'y':2},'count':8}," +
                " {'region':{'x':3,'y':2},'count':1}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        verify(listener).event(Events.WIN(0));

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':2,'y':2},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when done 1 level - go to 2
        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT));
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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when done 2 level - go to 3
        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.DOWN));
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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        // when done 3 level - go to 4
        hero.move(new Forces(pt(2, 1), 1, DoubleDirection.LEFT));
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when done 4 level - start 4 again
        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.UP));
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when done 4 level - start 4 again
        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.UP));
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when try to change level 500 - fail
        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
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
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(1);
        game.tick();

        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.DOWN));

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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseWhenGoToMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(2);
        game.tick();

        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "----" +
                "--☺-" +
                "----");

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        hero.move(new Forces(pt(2, 1), 1, DoubleDirection.LEFT));
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");
    }

    @Test
    public void shouldWinOnPassedLevelThanCanSelectAnother_caseGoFromMultiple() {
        // given
        shouldAllLevelsAreDone();

        // when win on level then try to change to last - success
        hero.loadLevel(3);
        game.tick();

        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺--" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
        game.tick();
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.DOWN));
        game.tick();

        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "-☺--" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':1,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when done 1 level - go to 2
        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT));
        game.tick();
        game.tick();

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.LEFT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when done 2 level - go to 3
        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.DOWN));
        game.tick();
        game.tick();

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        hero.move(new Forces(pt(2, 1), 1, DoubleDirection.UP));
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "--☺-" +
                "----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':9}]");

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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        // when done 3 level - go to 4
        hero.move(new Forces(pt(2, 1), 1, DoubleDirection.LEFT));
        game.tick();
        game.tick();

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when done 4 level - start 4 again
        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.UP));
        game.tick();
        game.tick();

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when try to change level 1  - success
        hero.loadLevel(0);
        game.tick();
        game.tick();

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.DOWN));
        game.tick();

        // then
        assertL("╔══┐" +
                "║SE│" +
                "║..│" +
                "└──┘");

        assertE("----" +
                "-☺--" +
                "-☺--" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':1,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when try to change level 2  - success
        hero.loadLevel(1);
        game.tick();
        game.tick();

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.LEFT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        // when try to change level 3  - success
        hero.loadLevel(2);
        game.tick();
        game.tick();

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        hero.move(new Forces(pt(2, 1), 1, DoubleDirection.UP));
        game.tick();

        // then
        assertL("╔══┐" +
                "║..│" +
                "║ES│" +
                "└──┘");

        assertE("----" +
                "--☺-" +
                "--☺-" +
                "----");

        assertF("[{'region':{'x':2,'y':2},'count':1}," +
                " {'region':{'x':2,'y':1},'count':9}]");

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

        assertF("[{'region':{'x':2,'y':1},'count':10}]");

        // when try to change level 4 - success
        hero.loadLevel(3);
        game.tick();
        game.tick();

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when try to change level 500 - fail
        hero.move(new Forces(pt(1, 1), 1, DoubleDirection.RIGHT));
        game.tick();
        game.tick();

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

        hero.loadLevel(500);
        game.tick();

        // then
        assertL("╔══┐" +
                "║E.│" +
                "║S.│" +
                "└──┘");

        assertE("----" +
                "----" +
                "-☺☺-" +
                "----");

        assertF("[{'region':{'x':1,'y':1},'count':9}," +
                " {'region':{'x':2,'y':1},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");

        // when try to change level 2 - success
        hero.loadLevel(1);
        game.tick();
        game.tick();

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.LEFT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when done level 1 - go to level 2
        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT));
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

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when try to change level to 2 - success
        hero.loadLevel(1);
        game.tick();

        assertF("[{'region':{'x':2,'y':2},'count':10}]");

        hero.move(new Forces(pt(2, 2), 1, DoubleDirection.LEFT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

        // when try to change level to 3 - fail
        hero.loadLevel(2);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

        // when try to change level 4 - fail
        hero.loadLevel(3);
        game.tick();

        // then
        assertL("╔══┐" +
                "║.S│" +
                "║.E│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':1}," +
                " {'region':{'x':2,'y':2},'count':9}]");

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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        // when
        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertL("╔══┐" +
                "║S.│" +
                "║E.│" +
                "└──┘");

        assertE("----" +
                "-☺☺-" +
                "----" +
                "----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':2,'y':2},'count':1}]");
    }

    @Test
    public void shouldOneMoreArmyToTheMaxCountWhenGetGold() {
        // given
        givenFl("╔═══┐" +
                "║...│" +
                "║S$E│" +
                "└───┘" +
                "     ");

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        assertEquals(10, hero.getForcesPerTick());

        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.RIGHT)); // pick up gold
        game.tick();

        assertL("╔═══┐" +
                "║...│" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':2,'y':2},'count':1}]");

        assertEquals(11, hero.getForcesPerTick());

        // when
        hero.increase(new Forces(pt(2, 2), 20)); // only 11 can increase
        game.tick();

        // then
        assertL("╔═══┐" +
                "║...│" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':2,'y':2},'count':12}]"); // 1+11

        assertEquals(11, hero.getForcesPerTick());

        // when
        hero.increase(new Forces(pt(2, 2), 20)); // only 11 can increase
        game.tick();

        // then
        assertL("╔═══┐" +
                "║...│" +
                "║S.E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-----" +
                "-☺☺--" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':2},'count':9}," +
                " {'region':{'x':2,'y':2},'count':23}]"); // 12+11

        assertEquals(11, hero.getForcesPerTick());
    }

    @Test
    public void shouldResetGoldCountAlso() {
        // given
        shouldOneMoreArmyToTheMaxCountWhenGetGold();

        // when
        hero.reset(); // reset all gold scores also
        game.tick();

        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        assertEquals(10, hero.getForcesPerTick());

        hero.move(new Forces(pt(1, 2), 1, DoubleDirection.UP));
        game.tick();

        assertL("╔═══┐" +
                "║...│" +
                "║S$E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-☺---" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':3},'count':1}," +
                " {'region':{'x':1,'y':2},'count':9}]");

        assertEquals(10, hero.getForcesPerTick());

        // when
        hero.increase(new Forces(pt(1, 3), 20)); // only 10 can increase
        game.tick();

        // then
        assertL("╔═══┐" +
                "║...│" +
                "║S$E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-☺---" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':3},'count':11}," + // 1+10
                " {'region':{'x':1,'y':2},'count':9}]");

        assertEquals(10, hero.getForcesPerTick());

        // when
        hero.increase(new Forces(pt(1, 3), 20)); // only 10 can increase
        game.tick();

        // then
        assertL("╔═══┐" +
                "║...│" +
                "║S$E│" +
                "└───┘" +
                "     ");

        assertE("-----" +
                "-☺---" +
                "-☺---" +
                "-----" +
                "-----");

        assertF("[{'region':{'x':1,'y':3},'count':21}," + // 11+10
                " {'region':{'x':1,'y':2},'count':9}]");

        assertEquals(10, hero.getForcesPerTick());
    }

    @Test
    public void shouldDoubleScoreArmyIncreaseWhenGetTwoGold() {
        // given
        givenFl("      " +
                "╔════┐" +
                "║S$$E│" +
                "└────┘" +
                "      " +
                "      ");

        assertF("[{'region':{'x':1,'y':3},'count':10}]");

        assertEquals(10, hero.getForcesPerTick());

        hero.move(new Forces(pt(1, 3), 2, DoubleDirection.RIGHT));
        game.tick();

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':2}]");

        assertEquals(11, hero.getForcesPerTick());

        hero.move(new Forces(pt(2, 3), 1, DoubleDirection.RIGHT));
        game.tick();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':1}," +
                " {'region':{'x':3,'y':3},'count':1}]");

        assertEquals(12, hero.getForcesPerTick());

        // when
        hero.increase(
                // 7 + 7 = 14, but only 12 possible
                new Forces(pt(2, 3), 7),  // 7 here
                new Forces(pt(3, 3), 7)   // 12 - 7 = 5 here
        );
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
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':8}," + // 1+7
                " {'region':{'x':3,'y':3},'count':6}]"); // 1+5

        assertEquals(12, hero.getForcesPerTick());

        // when
        hero.increase(new Forces(pt(3, 3), 20)); // only 12 possible to increase
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
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':8}," +
                " {'region':{'x':3,'y':3},'count':18}]"); //6+12

        assertEquals(12, hero.getForcesPerTick());
    }

    @Test
    public void shouldNoScoreAfterGetGold() {
        // given
        givenFl("     " +
                "╔═══┐" +
                "║S$E│" +
                "└───┘" +
                "     ");

        // when
        assertF("[{'region':{'x':1,'y':2},'count':10}]");

        hero.move(new Forces(pt(1, 2), 2, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertF("[{'region':{'x':1,'y':2},'count':8}," +
                " {'region':{'x':2,'y':2},'count':2}]");

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

        assertF("[{'region':{'x':1,'y':3},'count':10}]");

        // when
        hero.move(new Forces(pt(1, 3), 2, DoubleDirection.RIGHT));
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
                "-☺☺---" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':2}]");

        // when
        hero.move(new Forces(pt(2, 3), 1, DoubleDirection.RIGHT));
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
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':1}," +
                " {'region':{'x':3,'y':3},'count':1}]");
    }

    @Test
    public void shouldHiddenGoldCantGetAgain() {
        // given
        shouldHideGoldWhenGet();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':1}," +
                " {'region':{'x':3,'y':3},'count':1}]");

        hero.remove(new Forces(pt(2, 3), 1));
        game.tick();

        assertE("------" +
                "------" +
                "-☺-☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':3,'y':3},'count':1}]");

        // when
        // cant get hidden gold
        hero.move(new Forces(pt(1, 3), 1, DoubleDirection.RIGHT));
        game.tick();

        // then
        assertEquals(12, hero.getForcesPerTick());
    }

    @Test
    public void shouldReNewGoldWhenReset() {
        // given
        shouldHideGoldWhenGet();

        assertL("      " +
                "╔════┐" +
                "║S..E│" +
                "└────┘" +
                "      " +
                "      ");

        assertE("------" +
                "------" +
                "-☺☺☺--" +
                "------" +
                "------" +
                "------");

        assertF("[{'region':{'x':1,'y':3},'count':8}," +
                " {'region':{'x':2,'y':3},'count':1}," +
                " {'region':{'x':3,'y':3},'count':1}]");

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

        assertF("[{'region':{'x':1,'y':3},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':18},'count':10}]");

        // when
        for (int i = 1; i <= 10; ++i) {
            Point from = pt(i, 18);
            hero.increaseAndMove(
                    new Forces(from, 1),
                    new Forces(from, 1, DoubleDirection.RIGHT)
            );
            game.tick();
        }

        // then
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

        assertE("----------------" +
                "-☺☺☺☺☺☺☺☺☺☺☺----" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------");

        assertF("[{'region':{'x':1,'y':18},'count':10}," +
                " {'region':{'x':2,'y':18},'count':1}," +
                " {'region':{'x':3,'y':18},'count':1}," +
                " {'region':{'x':4,'y':18},'count':1}," +
                " {'region':{'x':5,'y':18},'count':1}," +
                " {'region':{'x':6,'y':18},'count':1}," +
                " {'region':{'x':7,'y':18},'count':1}," +
                " {'region':{'x':8,'y':18},'count':1}," +
                " {'region':{'x':9,'y':18},'count':1}," +
                " {'region':{'x':10,'y':18},'count':1}," +
                " {'region':{'x':11,'y':18},'count':1}]");

        // when
        Point from = pt(11, 18);
        hero.increaseAndMove(
                new Forces(from, 1),
                new Forces(from, 1, DoubleDirection.DOWN)
        );
        game.tick();

        from = pt(11, 17);
        hero.increaseAndMove(
                new Forces(from, 1),
                new Forces(from, 1, DoubleDirection.RIGHT)
        );
        game.tick();

        // then
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

        assertE("----------------" +
                "☺☺☺☺☺☺☺☺☺☺☺-----" +
                "----------☺☺----" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------");

        assertF("[{'region':{'x':1,'y':18},'count':10}," +
                " {'region':{'x':2,'y':18},'count':1}," +
                " {'region':{'x':3,'y':18},'count':1}," +
                " {'region':{'x':4,'y':18},'count':1}," +
                " {'region':{'x':5,'y':18},'count':1}," +
                " {'region':{'x':6,'y':18},'count':1}," +
                " {'region':{'x':7,'y':18},'count':1}," +
                " {'region':{'x':8,'y':18},'count':1}," +
                " {'region':{'x':9,'y':18},'count':1}," +
                " {'region':{'x':10,'y':18},'count':1}," +
                " {'region':{'x':11,'y':18},'count':1}," +
                " {'region':{'x':11,'y':17},'count':1}," +
                " {'region':{'x':12,'y':17},'count':1}]");


        // when
        for (int i = 12; i <= 12+11; ++i) {
            from = pt(i, 17);
            hero.increaseAndMove(
                    new Forces(from, 1),
                    new Forces(from, 1, DoubleDirection.RIGHT)
            );
            game.tick();
        }

        // then
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

        assertE("----------------" +
                "☺☺☺☺☺☺☺☺--------" +
                "-------☺☺☺☺☺☺☺☺-" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------");

        assertF("[{'region':{'x':4,'y':18},'count':1}," +
                " {'region':{'x':5,'y':18},'count':1}," +
                " {'region':{'x':6,'y':18},'count':1}," +
                " {'region':{'x':7,'y':18},'count':1}," +
                " {'region':{'x':8,'y':18},'count':1}," +
                " {'region':{'x':9,'y':18},'count':1}," +
                " {'region':{'x':10,'y':18},'count':1}," +
                " {'region':{'x':11,'y':18},'count':1}," +
                " {'region':{'x':11,'y':17},'count':1}," +
                " {'region':{'x':12,'y':17},'count':1}," +
                " {'region':{'x':13,'y':17},'count':1}," +
                " {'region':{'x':14,'y':17},'count':1}," +
                " {'region':{'x':15,'y':17},'count':1}," +
                " {'region':{'x':16,'y':17},'count':1}," +
                " {'region':{'x':17,'y':17},'count':1}," +
                " {'region':{'x':18,'y':17},'count':2}]");

        // when
        from = pt(7, 18);
        hero.increaseAndMove(
                new Forces(from, 1),
                new Forces(from, 1, DoubleDirection.DOWN)
        );
        game.tick();

        // then
        assertL("════════════════" +
                "................" +
                "................" +
                "..┌──╗.........." +
                "..│  ║.........." +
                "┌─┘  └─╗........" +
                "│      ║........" +
                "│      ║........" +
                "╚═┐  ╔═╝........" +
                "..│  ║.........." +
                "..╚══╝.........." +
                "................" +
                "................" +
                "................" +
                "................" +
                "................");

        assertE("----------------" +
                "☺☺☺☺☺☺☺☺☺-------" +
                "----☺---☺☺☺☺☺☺☺☺" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------" +
                "----------------");

        assertF("[{'region':{'x':3,'y':18},'count':1}," +
                " {'region':{'x':4,'y':18},'count':1}," +
                " {'region':{'x':5,'y':18},'count':1}," +
                " {'region':{'x':6,'y':18},'count':1}," +
                " {'region':{'x':7,'y':18},'count':1}," +
                " {'region':{'x':8,'y':18},'count':1}," +
                " {'region':{'x':9,'y':18},'count':1}," +
                " {'region':{'x':10,'y':18},'count':1}," +
                " {'region':{'x':11,'y':18},'count':1}," +
                " {'region':{'x':7,'y':17},'count':1}," +
                " {'region':{'x':11,'y':17},'count':1}," +
                " {'region':{'x':12,'y':17},'count':1}," +
                " {'region':{'x':13,'y':17},'count':1}," +
                " {'region':{'x':14,'y':17},'count':1}," +
                " {'region':{'x':15,'y':17},'count':1}," +
                " {'region':{'x':16,'y':17},'count':1}," +
                " {'region':{'x':17,'y':17},'count':1}," +
                " {'region':{'x':18,'y':17},'count':2}]");

        // when
        for (int i = 18; i >= 2; i--) {
            from = pt(11, i);
            hero.increaseAndMove(
                    new Forces(from, 1),
                    new Forces(from, 1, DoubleDirection.DOWN)
            );
            game.tick();
        }

        assertL("..│  ║.........." +
                "┌─┘  └─╗........" +
                "│      ║........" +
                "│      ║........" +
                "╚═┐  ╔═╝........" +
                "..│  ║.........." +
                "..╚══╝.........." +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "................" +
                "...............E" +
                "────────────────");

        assertE("--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "--------☺-------" +
                "----------------");

        assertF("[{'region':{'x':11,'y':15},'count':1}," +
                " {'region':{'x':11,'y':14},'count':1}," +
                " {'region':{'x':11,'y':13},'count':1}," +
                " {'region':{'x':11,'y':12},'count':1}," +
                " {'region':{'x':11,'y':11},'count':1}," +
                " {'region':{'x':11,'y':10},'count':1}," +
                " {'region':{'x':11,'y':9},'count':1}," +
                " {'region':{'x':11,'y':8},'count':1}," +
                " {'region':{'x':11,'y':7},'count':1}," +
                " {'region':{'x':11,'y':6},'count':1}," +
                " {'region':{'x':11,'y':5},'count':1}," +
                " {'region':{'x':11,'y':4},'count':1}," +
                " {'region':{'x':11,'y':3},'count':1}," +
                " {'region':{'x':11,'y':2},'count':1}," +
                " {'region':{'x':11,'y':1},'count':1}]");

        // when
        for (int i = 11; i >= 1; i--) {
            from = pt(i, 1);
            hero.increaseAndMove(
                    new Forces(from, 1),
                    new Forces(from, 1, DoubleDirection.LEFT)
            );
            game.tick();
        }

        // then
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

        assertE("-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-----------☺----" +
                "-☺☺☺☺☺☺☺☺☺☺☺----" +
                "----------------");

        assertF("[{'region':{'x':11,'y':15},'count':1}, " +
                "{'region':{'x':11,'y':14},'count':1}, " +
                "{'region':{'x':11,'y':13},'count':1}, " +
                "{'region':{'x':11,'y':12},'count':1}, " +
                "{'region':{'x':11,'y':11},'count':1}, " +
                "{'region':{'x':11,'y':10},'count':1}, " +
                "{'region':{'x':11,'y':9},'count':1}, " +
                "{'region':{'x':11,'y':8},'count':1}, " +
                "{'region':{'x':11,'y':7},'count':1}, " +
                "{'region':{'x':11,'y':6},'count':1}, " +
                "{'region':{'x':11,'y':5},'count':1}, " +
                "{'region':{'x':11,'y':4},'count':1}, " +
                "{'region':{'x':11,'y':3},'count':1}, " +
                "{'region':{'x':11,'y':2},'count':1}, " +
                "{'region':{'x':1,'y':1},'count':2}, " +
                "{'region':{'x':2,'y':1},'count':1}, " +
                "{'region':{'x':3,'y':1},'count':1}, " +
                "{'region':{'x':4,'y':1},'count':1}, " +
                "{'region':{'x':5,'y':1},'count':1}, " +
                "{'region':{'x':6,'y':1},'count':1}, " +
                "{'region':{'x':7,'y':1},'count':1}, " +
                "{'region':{'x':8,'y':1},'count':1}, " +
                "{'region':{'x':9,'y':1},'count':1}, " +
                "{'region':{'x':10,'y':1},'count':1}, " +
                "{'region':{'x':11,'y':1},'count':1}]");
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

        assertF("[{'region':{'x':18,'y':28},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':5},'count':10}]");
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

        assertF("[{'region':{'x':5,'y':5},'count':10}]");
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

        assertF("[{'region':{'x':1,'y':1},'count':10}]");
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

        assertF("[{'region':{'x':5,'y':1},'count':10}]");
    }
}
