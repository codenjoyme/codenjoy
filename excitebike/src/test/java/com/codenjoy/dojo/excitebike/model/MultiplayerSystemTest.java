package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.excitebike.services.Events;
import com.codenjoy.dojo.excitebike.services.SettingsHandler;
import com.codenjoy.dojo.excitebike.services.parse.MapParser;
import com.codenjoy.dojo.excitebike.services.parse.MapParserImpl;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import static com.codenjoy.dojo.excitebike.TestUtils.ticks;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MultiplayerSystemTest {

    private Game game1;
    private EventListener eventListenerSpy1 = spy(EventListener.class);
    private Game game2;
    private EventListener eventListenerSpy2 = spy(EventListener.class);
    private Game game3;
    private EventListener eventListenerSpy3 = spy(EventListener.class);
    private Dice dice;
    private GameFieldImpl field;

    private void init() {
        MapParser mapParser = new MapParserImpl("■■■■■■■" +
                "       " +
                "       " +
                "       " +
                "       " +
                "       " +
                "■■■■■■■");

        dice = mock(Dice.class);
        field = new GameFieldImpl(mapParser, dice, new SettingsHandler());
        PrinterFactory factory = new PrinterFactoryImpl();

        game1 = new Single(new Player(eventListenerSpy1), factory);
        game1.on(field);

        game2 = new Single(new Player(eventListenerSpy2), factory);
        game2.on(field);

        game3 = new Single(new Player(eventListenerSpy3), factory);
        game3.on(field);

        game1.newGame();
        game2.newGame();
        game3.newGame();
    }

    @Test
    public void games__shouldInitializeCorrectly() {
        //given

        //when
        init();

        //then
        assertThat(game1.getBoardAsString(), is("■■■■■■■\n" +
                "Ḃ      \n" +
                "       \n" +
                "Ḃ      \n" +
                "       \n" +
                "B      \n" +
                "■■■■■■■\n"));
        assertThat(game2.getBoardAsString(), is("■■■■■■■\n" +
                "Ḃ      \n" +
                "       \n" +
                "B      \n" +
                "       \n" +
                "Ḃ      \n" +
                "■■■■■■■\n"));
        assertThat(game3.getBoardAsString(), is("■■■■■■■\n" +
                "B      \n" +
                "       \n" +
                "Ḃ      \n" +
                "       \n" +
                "Ḃ      \n" +
                "■■■■■■■\n"));
    }

    @Test
    public void shouldJoystick() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);

        game3.getJoystick().up();
        game2.getJoystick().left();
        game1.getJoystick().down();

        //when
        field.tick();

        //then
        String expected = "ḟ■■■■■■\n" +
                "       \n" +
                "       \n" +
                "Ḃ      \n" +
                "       \n" +
                "       \n" +
                "f■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldRemove() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game3.close();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "Ḃ      \n" +
                "       \n" +
                "B      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushEnemyBikeAfterClashAndGetEvents() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "Ḃ      \n" +
                "       \n" +
                "       \n" +
                "K      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
        verify(eventListenerSpy1).event(Events.WIN);
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldCrushEnemyBikeAfterClash2AndGetScores() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setY(bike1.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḱ      \n" +
                "B      \n" +
                "       \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game3.isGameOver(), is(true));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy3).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldCrushBikeAfterClashWithFenceAndGetScores() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);

        //when
        game1.getJoystick().down();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "Ḃ      \n" +
                "       \n" +
                "       \n" +
                "B      \n" +
                "       \n" +
                "ḟ■■■■■■\n";
        assertThat(game2.getBoardAsString(), is(expected));
        assertThat(game1.isGameOver(), is(true));
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy1).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike1() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setY(bike1.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḃ      \n" +
                "Ḃ      \n" +
                "B      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike1__tick2() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setY(bike1.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 1);
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḃ      \n" +
                "Ḃ      \n" +
                "B      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike2() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setY(bike1.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḃ      \n" +
                "B      \n" +
                "Ḃ      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game2.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldDoNothingAfterBikesClashEachOther__bike3() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setY(bike1.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 1);

        //when
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "B      \n" +
                "Ḃ      \n" +
                "Ḃ      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game3.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldMoveBikesInAnyOrderOfCall() {
        //given
        init();
        when(dice.next(anyInt())).thenReturn(5);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);

        //when
        game1.getJoystick().up();
        game2.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "Ḃ      \n" +
                "Ḃ      \n" +
                "B      \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIncrementYCoordinateForAllBikes() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/Ḃ\\    \n" +
                "/B\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldNotIgnoreCommands_whenBikeBeforeTwoStepsFromSpringboardRise() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 5);

        //when
        game1.getJoystick().down();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■■╔═╗■■\n" +
                "  /═\\  \n" +
                " Ḃ/═\\  \n" +
                "  /═\\  \n" +
                " Ḃ/═\\  \n" +
                "  ╚/╝  \n" +
                "■f■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldNotSpawnBikeInFlight() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;
        init();
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        ticks(field, 6);
        ((Bike) game1.getPlayer().getHero()).crush();
        ticks(field, 2);

        //when
        game1.newGame();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "Ḃ\\     \n" +
                "═\\     \n" +
                "BŘ     \n" +
                "/╝     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldStartBikeFlightFromSpringboard_whenBikeTakeDownCommandOnSpringboardRise() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().down();
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/Ḃ\\    \n" +
                "/═\\    \n" +
                "╚F╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldCrushBike_whenBikeHitOtherBikeAtTopOfSpringboardAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "╔═╗■■■■\n" +
                "/═\\    \n" +
                "/Ḃ\\    \n" +
                "/K\\    \n" +
                "/═\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldShiftCrushedBikeAtSpringboardTopAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().up();
        field.tick();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "ḃŘ     \n" +
                "═R     \n" +
                "/╝     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game2.isGameOver(), is(true));
        verify(eventListenerSpy1).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldCrushOnFenceBikeAfterFlightFromSpringboardAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().down();
        field.tick();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═Ř     \n" +
                "═Ř     \n" +
                "/╝     \n" +
                "■f■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldDecrementYCoordinateForAllBikes() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 4);
        ticks(field, 7);
        field.tick();

        //when
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 4);
        ticks(field, 8);

        //when
        game2.getJoystick().up();
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent2() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 4);
        ticks(field, 8);

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═\\     \n" +
                "═\\     \n" +
                "═R     \n" +
                "═Ř     \n" +
                "/Ŝ     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent3() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        ticks(field, 8);

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "═╗■■■■■\n" +
                "═Ř     \n" +
                "═Ř     \n" +
                "═R     \n" +
                "═\\     \n" +
                "/╝     \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldCrushBike_whenBikeAtHighestLineAndTakeUpCommandAfterSpringboardRiseAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        ticks(field, 7);

        //when
        game3.getJoystick().up();
        game2.getJoystick().up();
        game1.getJoystick().down();
        field.tick();

        //then
        String expected = "╔Ḃ╗■■■■\n" +
                "/═\\    \n" +
                "/═\\    \n" +
                "/B\\    \n" +
                "/═\\    \n" +
                "╚/╝    \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        assertThat(game3.isGameOver(), is(true));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldCrushBike_whenBikeAtHighestLineAndTakeUpCommandAfterSpringboardDecentAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 1;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        ticks(field, 9);

        //when
        game3.getJoystick().up();
        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "╗ḟ■■■■■\n" +
                "\\      \n" +
                "\\Ḃ     \n" +
                "\\B     \n" +
                "\\      \n" +
                "╝      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent4() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/\\     \n" +
                "/\\     \n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "╚S     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent5() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        bike3.setY(bike3.getY() - 2);
        ticks(field, 7);

        //when
        game1.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/\\     \n" +
                "/\\     \n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "╚S     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeBeforeSpringboardDecent6() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        ticks(field, 7);

        //when
        game1.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "╔╗■■■■■\n" +
                "/Ř     \n" +
                "/Ř     \n" +
                "/R     \n" +
                "/\\     \n" +
                "╚╝     \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldNotIgnoreAllCommands_whenBikeBeforeTwoStepsFromSpringboardRiseAndGetScores() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 1);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 1);
        bike2.setY(bike2.getY() + 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setX(bike3.getX() + 1);
        ticks(field, 5);

        //when
        game1.getJoystick().up();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "■ḟ╔╗■■■\n" +
                "  /\\   \n" +
                " K/\\   \n" +
                "  /\\   \n" +
                "  /\\   \n" +
                "  ╚╝   \n" +
                "■■■■■■■\n";

        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void twoBikesBehindObstacleTick1__shouldBeSpawnedCorrectly() {
        //given
        init();
        game3.close();
        when(dice.next(20)).thenReturn(12);
        when(dice.next(5)).thenReturn(2, 2);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 4);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 5);

        //when
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "    BḂ|\n" +
                "       \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void twoBikesBehindObstacleTick2__shouldCrushEnemyBikeAtObstacle() {
        //given
        init();
        game3.close();
        when(dice.next(20)).thenReturn(12, 1);
        when(dice.next(5)).thenReturn(2, 2);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 4);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 5);
        field.tick();

        //when
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "    Bō \n" +
                "       \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void twoBikesBehindObstacleTick3__shouldCrushPlayerBikeAtEnemyAtObstacleAndGetScores() {
        //given
        init();
        game3.close();
        when(dice.next(20)).thenReturn(12, 1);
        when(dice.next(5)).thenReturn(2, 2);
        Bike bike1 = (Bike) game1.getPlayer().getHero();
        bike1.setX(bike1.getX() + 4);
        bike1.setY(bike1.getY() + 2);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setX(bike2.getX() + 5);
        ticks(field, 2);

        //when
        field.tick();

        //then
        String expected = "■■■■■■■\n" +
                "       \n" +
                "       \n" +
                "    o  \n" +
                "       \n" +
                "       \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeJustSpawnedBeforeSpringboardDecent() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 2;
        init();
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        ticks(field, 8);
        ((Bike) game1.getPlayer().getHero()).crush();
        ((Bike) game2.getPlayer().getHero()).crush();
        ((Bike) game3.getPlayer().getHero()).crush();
        ticks(field, 2);

        //when
        game1.newGame();
        game2.newGame();
        game3.newGame();
        game1.getJoystick().down();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "╗■■■■■■\n" +
                "\\      \n" +
                "\\      \n" +
                "ŘḂ     \n" +
                "\\      \n" +
                "S      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeJustSpawnedBeforeSpringboardDecent2() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 2;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);
        ticks(field, 8);
        ((Bike) game1.getPlayer().getHero()).crush();
        ((Bike) game2.getPlayer().getHero()).crush();
        ((Bike) game3.getPlayer().getHero()).crush();
        ticks(field, 2);

        //when
        game1.newGame();
        game2.newGame();
        game3.newGame();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "╗■■■■■■\n" +
                "\\      \n" +
                "\\      \n" +
                "Ř      \n" +
                "\\Ḃ     \n" +
                "S      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldIgnoreAllCommands_whenBikeJustSpawnedBeforeSpringboardDecent3() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 0;

        init();
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);
        ticks(field, 6);
        ((Bike) game1.getPlayer().getHero()).crush();
        ((Bike) game2.getPlayer().getHero()).crush();
        ((Bike) game3.getPlayer().getHero()).crush();
        ticks(field, 2);

        //when
        game1.newGame();
        game2.newGame();
        game3.newGame();
        game2.getJoystick().down();
        field.tick();

        //then
        String expected = "╗■■■■■■\n" +
                "Ř      \n" +
                "\\      \n" +
                "Ř      \n" +
                "\\      \n" +
                "S      \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
    }

    @Test
    public void shouldNotCrushBike_whenBikeMovesToTopLineOfSpringboardAfterRespawn() {
        //given
        int springboardWeight = 17;
        int springboardTopSize = 7;
        init();
        Bike bike2 = (Bike) game2.getPlayer().getHero();
        bike2.setY(bike2.getY() - 1);
        Bike bike3 = (Bike) game3.getPlayer().getHero();
        bike3.setY(bike3.getY() - 2);
        when(dice.next(anyInt())).thenReturn(springboardWeight, springboardTopSize);
        ticks(field, 8);
        ((Bike) game1.getPlayer().getHero()).crush();
        ((Bike) game2.getPlayer().getHero()).crush();
        ((Bike) game3.getPlayer().getHero()).crush();
        ticks(field, 2);

        //when
        game1.newGame();
        game2.newGame();
        game3.newGame();
        game3.getJoystick().up();
        field.tick();
        game3.getJoystick().up();
        field.tick();

        //then
        String expected = "════╗■■\n" +
                "═Ḃ══\\  \n" +
                "Ḃ═══\\  \n" +
                "════\\  \n" +
                "B═══\\  \n" +
                "////╝  \n" +
                "■■■■■■■\n";
        assertThat(game1.getBoardAsString(), is(expected));
        verify(eventListenerSpy1, never()).event(Events.LOSE);
        verify(eventListenerSpy1, never()).event(Events.WIN);
        verify(eventListenerSpy2, never()).event(Events.LOSE);
        verify(eventListenerSpy2, never()).event(Events.WIN);
        verify(eventListenerSpy3, never()).event(Events.LOSE);
        verify(eventListenerSpy3, never()).event(Events.WIN);
    }

}
