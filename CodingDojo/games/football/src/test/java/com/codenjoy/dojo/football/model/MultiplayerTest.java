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

import com.codenjoy.dojo.football.services.Event;
import com.codenjoy.dojo.football.services.GameSettings;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.utils.events.EventsListenersAssert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.repeat;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplayerTest {

    private EventListener listener1;
    private EventListener listener2;
    private EventListener listener3;
    private Game game1;
    private Game game2;
    private Game game3;
    private Dice dice;
    private Football field;
    private EventsListenersAssert events;
    private Level level;

    // появляется другие игроки, игра становится мультипользовательской
    @Before
    public void setup() {
        level = new Level(
                "☼☼┴┴☼☼\n" +
                "☼    ☼\n" +
                "☼  ∙ ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼┬┬☼☼\n");

        dice = mock(Dice.class);
        GameSettings settings = new GameSettings();
        field = new Football(level, dice, settings);
        PrinterFactory factory = new PrinterFactoryImpl();

        listener1 = mock(EventListener.class);
        game1 = new Single(new Player(listener1, settings), factory);
        game1.on(field);

        listener2 = mock(EventListener.class);
        game2 = new Single(new Player(listener2, settings), factory);
        game2.on(field);

        listener3 = mock(EventListener.class);
        game3 = new Single(new Player(listener3, settings), factory);
        game3.on(field);

        events = new EventsListenersAssert(() -> Arrays.asList(listener1, listener2, listener3), Event.class);

        dice(1, 1);
        game1.newGame();

        dice(2, 4);
        game2.newGame();

        dice(3, 1);
        game3.newGame();
        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }

    @After
    public void tearDown() {
        verifyAllEvents("");
    }

    private void dice(int x, int y) {
        when(dice.next(anyInt())).thenReturn(x, y);
    }

    public void verifyAllEvents(String expected) {
        assertEquals(expected, events.getEvents());
    }

    // TODO попробовать этот подход в других Multiplayer играх
    private void asrtFls(String expected) {
        List<String> board1 = boardLines(game1);
        List<String> board2 = boardLines(game2);
        List<String> board3 = boardLines(game3);

        String empty = repeat(" ", level.size());

        List<String> result = new ArrayList<>();
        for (int i = 0; i < board1.size(); i++) {
            result.add(String.format("%s %s %s\n",
                    (board1 != null) ? board1.get(i) : empty,
                    (board2 != null) ? board2.get(i) : empty,
                    (board3 != null) ? board3.get(i) : empty
            ));
        }
        assertEquals(expected, result.stream().collect(joining("")));
    }

    private List<String> boardLines(Game game) {
        try {
            return Arrays.asList(game.getBoardAsString().toString().split("\n"));
        } catch (IllegalStateException e) {
            return null;
        }
    }

    // рисуем несколько игроков
    @Test
    public void shouldPrint() {
        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game3.getJoystick().up();
        field.tick();

        game3.getJoystick().up();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ♥ ☼ ☼  ♠ ☼ ☼  ☻ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game3.getJoystick().act(Actions.HIT_DOWN.getValue());
        game3.getJoystick().down();
        field.tick();
        
        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♥ ☼ ☼  ♠ ☼ ☼  ☻ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }

    // Каждый игрок может упраыляться за тик игры независимо
    @Test
    public void shouldJoystick() {
        game1.getJoystick().up();
        game2.getJoystick().down();
        game3.getJoystick().right();

        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♣∙ ☼ ☼ ☺∙ ☼ ☼ ♣∙ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼   ♦☼ ☼   ♣☼ ☼   ☺☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }

    // игроков можно удалять из игры
    @Test
    public void shouldRemove() {
        game2.close();

        field.tick();

        asrtFls("☼☼⌂⌂☼☼        ☼☼⌂⌂☼☼\n" +
                "☼    ☼        ☼    ☼\n" +
                "☼  ∙ ☼        ☼  ∙ ☼\n" +
                "☼    ☼        ☼    ☼\n" +
                "☼☺ ♦ ☼        ☼♦ ☺ ☼\n" +
                "☼☼==☼☼        ☼☼==☼☼\n");
    }

    // игрок может пробросить мяч через другого
    @Test
    public void shouldPassBallThroughPlayer() {
        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
        
        game2.getJoystick().down();
        game3.getJoystick().up();
        field.tick();
        game2.getJoystick().down();
        game3.getJoystick().up();
        field.tick();
        game2.getJoystick().right();
        field.tick();
        
        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♥ ☼ ☼  ♠ ☼ ☼  ☻ ☼\n" +
                "☼  ♣ ☼ ☼  ☺ ☼ ☼  ♣ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
        
        game3.getJoystick().act(Actions.HIT_DOWN.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼  ♠ ☼ ☼  ☻ ☼ ☼  ♠ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼  ♣ ☼ ☼  ☺ ☼ ☼  ♣ ☼\n" +
                "☼☺ * ☼ ☼♣ * ☼ ☼♦ * ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }

    // другой игрок может остановить мяч
    @Test
    public void shouldStopBall() {
        game2.getJoystick().down();
        game3.getJoystick().up();
        field.tick();
        game2.getJoystick().down();
        game3.getJoystick().up();
        field.tick();
        game2.getJoystick().right();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♥ ☼ ☼  ♠ ☼ ☼  ☻ ☼\n" +
                "☼  ♣ ☼ ☼  ☺ ☼ ☼  ♣ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game3.getJoystick().act(Actions.HIT_DOWN.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼  ♠ ☼ ☼  ☻ ☼ ☼  ♠ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game2.getJoystick().act(Actions.STOP_BALL.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼  ♠ ☼ ☼  ☻ ☼ ☼  ♠ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }
    
    // игрок не может пойи на другого игрока
    @Test
    public void shouldCantGoOnHero() {
        game1.getJoystick().right();
        game3.getJoystick().left();

        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ☺♦ ☼ ☼ ♣♣ ☼ ☼ ♦☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
    }
    
    // счет начисляется
    @Test
    public void scoreMultiplayerTest() {
        game3.getJoystick().up();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ∙ ☼ ☼  ∙ ☼ ☼  ∙ ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game3.getJoystick().up();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ♥ ☼ ☼  ♠ ☼ ☼  ☻ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game3.getJoystick().act(Actions.HIT_UP.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣* ☼ ☼ ☺* ☼ ☼ ♣* ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        field.tick();

        asrtFls("☼☼⌂x☼☼ ☼☼=#☼☼ ☼☼⌂x☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        verifyAllEvents("");

        field.tick();

        asrtFls("☼☼⌂x☼☼ ☼☼=#☼☼ ☼☼⌂x☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼  ♦ ☼ ☼  ♣ ☼ ☼  ☺ ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺   ☼ ☼♣   ☼ ☼♦   ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");
        
        assertEquals(true, game1.isGameOver());
        assertEquals(true, game2.isGameOver());
        assertEquals(true, game3.isGameOver());
        
        verifyAllEvents(
                "listener(0) => [TOP_GOAL, WIN]\n" +
                "listener(1) => [TOP_GOAL, LOSE]\n" +
                "listener(2) => [TOP_GOAL, WIN]\n");
    }

    @Test
    public void scoreMultiplayerTest_viseVersa() {
        field.getBalls().get(0).move(2, 3);  // cheat

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼ ∙  ☼ ☼ ∙  ☼ ☼ ∙  ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game2.getJoystick().down();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♠  ☼ ☼ ☻  ☼ ☼ ♠  ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game2.getJoystick().act(Actions.HIT_DOWN.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼ *  ☼ ☼ *  ☼ ☼ *  ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game2.getJoystick().down();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼☺*♦ ☼ ☼♣*♣ ☼ ☼♦*☺ ☼\n" +
                "☼☼==☼☼ ☼☼⌂⌂☼☼ ☼☼==☼☼\n");

        game2.getJoystick().act(Actions.HIT_DOWN.getValue());
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼#=☼☼ ☼☼x⌂☼☼ ☼☼#=☼☼\n");

        verifyAllEvents("");

        game2.getJoystick().down();
        field.tick();

        asrtFls("☼☼⌂⌂☼☼ ☼☼==☼☼ ☼☼⌂⌂☼☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼    ☼ ☼    ☼ ☼    ☼\n" +
                "☼ ♣  ☼ ☼ ☺  ☼ ☼ ♣  ☼\n" +
                "☼☺ ♦ ☼ ☼♣ ♣ ☼ ☼♦ ☺ ☼\n" +
                "☼☼#=☼☼ ☼☼x⌂☼☼ ☼☼#=☼☼\n");

        assertEquals(true, game1.isGameOver());
        assertEquals(true, game2.isGameOver());
        assertEquals(true, game3.isGameOver());

        verifyAllEvents(
                "listener(0) => [BOTTOM_GOAL, LOSE]\n" +
                "listener(1) => [BOTTOM_GOAL, WIN]\n" +
                "listener(2) => [BOTTOM_GOAL, LOSE]\n");
    }
}
