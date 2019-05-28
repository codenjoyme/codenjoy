package com.codenjoy.dojo.football.model;

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

import com.codenjoy.dojo.football.model.elements.Hero;
import com.codenjoy.dojo.football.services.Events;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.utils.TestUtils;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.stubbing.OngoingStubbing;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 4:47
 */
public class FootballTest {

    private Football game;
    private Hero hero;
    private Dice dice;
    private EventListener listener;
    private Player player;
    private PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setup() {
        dice = mock(Dice.class);
    }

    private void dice(int...ints) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int i : ints) {
            when = when.thenReturn(i);
        }
    }

    private void givenFl(String board) {
        LevelImpl level = new LevelImpl(board);
        Hero hero = level.getHero().get(0);

        game = new Football(level, dice);
        listener = mock(EventListener.class);
        player = new Player(listener);
        game.newGame(player);
        player.hero = hero;
        hero.init(game);
        this.hero = game.getHeroes().get(0);
    }

    private void assertE(String expected) {
        assertEquals(TestUtils.injectN(expected),
                printer.getPrinter(game.reader(), player).print());
    }

    // есть карта со мной
    @Test
    public void shouldFieldAtStart() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼ ☺ ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
    
    @Test
    public void heroCanMove() {
        givenFl("☼☼☼☼☼" +
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
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.right();
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
    
    @Test
    public void heroCannotPassThroughWalls() {
        givenFl("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
        
        hero.left();
        game.tick();

        assertE("☼☼☼☼☼" +
                "☼   ☼" +
                "☼☺  ☼" +
                "☼   ☼" +
                "☼☼☼☼☼");
    }
    
    @Test
    public void ballIsPresent() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼  ∙   ☼" +
                "☼☺     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        assertE("☼☼☼☼☼☼☼☼" +
                "☼  ∙   ☼" +
                "☼☺     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }

    @Test
    public void playerCanBeWithBall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☺∙    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

    }
    
    public void ballCanMove() {
        
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼∙     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        game.getBall(2, 2).right(1);
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼ *    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
    }
    
    @Test
    public void playerCanMoveWithBall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☺*    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        assertEquals(game.getBalls().size(), 1);
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        assertEquals(true, game.isBall(2, 4));
        
        hero.right();
        assertEquals(true, game.getBall(2, 4) != null);
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertEquals(true, game.getBall(3, 4) != null);
        
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerCanNotMoveWithBallThrowWall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼*☺    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.left();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        hero.left();
        game.tick();
        
        hero.left();
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void gameStartsFromPlayerWithBallState() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☻     ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.right();
        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼ ☻    ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerHitsBallRight() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺*  ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺ * ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    @Test
    public void playerHitsTheGate() {
        givenFl("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        game.tick();

        assertE("☼☼☼x⌂☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼  ☺   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼==☼☼☼");
    }
    
    @Test
    public void ballStopsWhetGetWall() {
        givenFl("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ☻ ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");

        hero.act(Actions.HIT_RIGHT.getValue());
        game.tick();
        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ☺*☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
        game.tick();
        game.tick();

        assertE("☼☼☼☼☼☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼    ☺∙☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼☼☼☼☼☼");
    }
    
    public void resetFieldAfterGoalAndNewGameCall() {
        
        givenFl("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        dice(2, 2);
        game.newGame(player);
        
        assertE("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   *  ☼" +
                "☼      ☼" +
                "☼ ☺    ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
    }
    
    @Ignore
    @Test
    public void resetFieldAfterHittenGoal() {
        
        givenFl("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼  ☻   ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
        
        hero.act(Actions.HIT_UP.getValue());
        game.tick();
        game.tick();
        dice(2, 2);
        game.tick();
        verify(listener, only()).event(Events.TOP_GOAL);
        
        assertE("☼☼☼┴┴☼☼☼" +
                "☼      ☼" +
                "☼      ☼" +
                "☼   *  ☼" +
                "☼      ☼" +
                "☼ ☺    ☼" +
                "☼      ☼" +
                "☼☼☼┬┬☼☼☼");
    }
    
    @Ignore
    @Test
    public void resetFieldByNewGameCall() {
        
        givenFl("☼☼☼x┴┴☼☼☼" +
                "☼       ☼" +
                "☼  ☺    ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼☼☼┬┬┬☼☼☼");
        
        dice(2, 2);
        game.tick();
        verify(listener, only()).event(Events.TOP_GOAL);
        
        assertE("☼☼☼┴┴┴☼☼☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼       ☼" +
                "☼   *   ☼" +
                "☼       ☼" +
                "☼ ☺     ☼" +
                "☼       ☼" +
                "☼☼☼┬┬┬☼☼☼");
    }
    
}
