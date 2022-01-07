package com.codenjoy.dojo.football.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.football.TestGameSettings;
import com.codenjoy.dojo.football.model.items.Hero;
import com.codenjoy.dojo.football.services.Event;
import com.codenjoy.dojo.football.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameTest {

    private Football game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer;
    private GameSettings settings;
    private EventsListenersAssert events;

    @Before
    public void setup() {
        dice = mock(Dice.class);
        printer = new PrinterFactoryImpl();
        settings = new TestGameSettings();
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events.getEvents());
    }

    private void givenFl(String board) {
        Level level = new Level(board);
        hero = level.hero().get(0);

        game = new Football(level, dice, settings);
        listener = mock(EventListener.class);
        events = new EventsListenersAssert(() -> Arrays.asList(listener), Event.class);
        player = new Player(listener, settings);
        player.setHero(hero);
        game.newGame(player);
        hero.init(game);
    }

    private void assertE(String expected) {
        assertEquals(expected,
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
    
    @Test
    public void heroCanMove() {
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
        
        hero.up();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
        
        hero.right();
        game.tick();
        
        assertE("☼☼☼☼☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
        
        hero.down();
        game.tick();
        
        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼ ☺ ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
    
    @Test
    public void heroCannotPassThroughWalls() {
        givenFl("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
        
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼\n" +
                "☼   ☼\n" +
                "☼☺  ☼\n" +
                "☼   ☼\n" +
                "☼☼☼☼☼\n");
    }
    
    @Test
    public void ballIsPresent() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼  ∙   ☼\n" +
                "☼☺     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        
        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼  ∙   ☼\n" +
                "☼☺     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void playerCanBeWithBall() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☺∙    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ☻    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

    }

    @Test
    public void ballCanMove() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ∙    ☼\n" +
                "☼     ☺☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        game.getBall(2, 2).right(1);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  *   ☼\n" +
                "☼     ☺☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void playerCanMoveWithBall() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☺*    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        assertEquals(game.getBalls().size(), 1);
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ☻    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        assertEquals(true, game.getBall(2, 4) != null);
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertEquals(true, game.getBall(3, 4) != null);
        
        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☻   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void playerCanNotMoveWithBallThrowWall() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼*☺    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☻     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        
        hero.left();
        game.tick();
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☻     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        
        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ☻    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void gameStartsFromPlayerWithBallState() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☻     ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼ ☻    ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void playerHitsBallRight() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☻   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☺*  ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☺ * ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }
    
    @Test
    public void playerHitsTheGate() {
        givenFl("☼☼☼┴┴☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☻   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼┬┬☼☼☼\n");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼x⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");
    }
    
    @Test
    public void ballStopsWhetGetWall() {
        givenFl("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼    ☻ ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼    ☺*☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼    ☺∙☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼☼☼☼☼☼\n");
    }

    @Test
    public void resetFieldAfterGoalAndNewGameCall() {
        givenFl("☼☼☼┴┴☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☻   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼┬┬☼☼☼\n");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();

        assertE("☼☼☼⌂⌂☼☼☼\n" +
                "☼  *   ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.tick();

        assertEquals(true, player.isAlive());
        verifyAllEvents("");

        assertE("☼☼☼x⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.tick();

        assertEquals(false, player.isAlive());
        verifyAllEvents("[TOP_GOAL, WIN]");

        assertE("☼☼☼x⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.newGame(player);

        assertEquals(true, player.isAlive());
        verifyAllEvents("");
        
        assertE("☼☼☼⌂⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼   ∙  ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");
    }

    @Test
    public void resetFieldAfterHittenGoal() {
        givenFl("☼☼☼┴┴☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☻   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼┬┬☼☼☼\n");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();

        assertE("☼☼☼⌂⌂☼☼☼\n" +
                "☼  *   ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.tick();

        assertEquals(true, player.isAlive());
        verifyAllEvents("");

        assertE("☼☼☼x⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.tick();

        assertEquals(false, player.isAlive());
        verifyAllEvents("[TOP_GOAL, WIN]");

        assertE("☼☼☼x⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");

        game.newGame(player);

        assertEquals(true, player.isAlive());
        verifyAllEvents("");

        assertE("☼☼☼⌂⌂☼☼☼\n" +
                "☼      ☼\n" +
                "☼  ☺   ☼\n" +
                "☼   ∙  ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼      ☼\n" +
                "☼☼☼==☼☼☼\n");
    }

    @Test
    public void resetFieldByNewGameCall() {
        givenFl("☼☼☼x┴┴☼☼☼\n" +
                "☼       ☼\n" +
                "☼  ☺    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼┬┬┬☼☼☼\n");
        
        game.tick();

        assertEquals(true, player.isAlive());
        verifyAllEvents("");

        assertE("☼☼☼∙⌂⌂☼☼☼\n" +   // TODO тут как-то не очень
                "☼       ☼\n" +
                "☼  ☺    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼===☼☼☼\n");

        game.newGame(player);

        assertEquals(true, player.isAlive());
        verifyAllEvents("");

        assertE("☼☼☼∙⌂⌂☼☼☼\n" +
                "☼       ☼\n" +
                "☼  ☺    ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼       ☼\n" +
                "☼☼☼===☼☼☼\n");
    }
    
}
