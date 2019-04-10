package com.codenjoy.dojo.loderunner.model;

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


import com.codenjoy.dojo.loderunner.services.Events;
import com.codenjoy.dojo.services.Dice;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class SingleTest {

    private Dice dice;
    private EventListener listener1;
    private Game game1;
    private EventListener listener2;
    private Game game2;
    private Loderunner field;
    private EventListener listener3;
    private Game game3;
    private PrinterFactory printerFactory = new PrinterFactoryImpl();

    // появляется другие игроки, игра становится мультипользовательской
    @Test
    public void shouldMultipleGame() { // TODO разделить тест на части
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼   $☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(1, 4);
        setupPlayer2(2, 2);
        setupPlayer3(3, 4);

        atGame1(
                "☼☼☼☼☼☼\n" +
                "☼► ( ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2(
                "☼☼☼☼☼☼\n" +
                "☼( ( ☼\n" +
                "☼####☼\n" +
                "☼ ► $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame3(
                "☼☼☼☼☼☼\n" +
                "☼( ► ☼\n" +
                "☼####☼\n" +
                "☼ ( $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().left();
        game3.getJoystick().right();

        field.tick(); 

        atGame1(
                "☼☼☼☼☼☼\n" +
                "☼ ► (☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2(
                "☼☼☼☼☼☼\n" +
                "☼ ( (☼\n" +
                "☼####☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame3(
                "☼☼☼☼☼☼\n" +
                "☼ ( ►☼\n" +
                "☼####☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().act();
        game3.close();

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼ R  ☼\n" +
                "☼##*#☼\n" +
                "☼)  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼ (  ☼\n" +
                "☼##*#☼\n" +
                "☼◄  $☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        try {
            atGame3("☼☼☼☼☼☼\n" +
                    "☼ (  ☼\n" +
                    "☼##*#☼\n" +
                    "☼)  $☼\n" +
                    "☼####☼\n" +
                    "☼☼☼☼☼☼\n");
        } catch (IllegalStateException e) {
            assertEquals("No board for this player", e.getMessage());
        }

        game1.getJoystick().right();

        field.tick();
        field.tick();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼) ►$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼◄ ($☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().left();
        game1.getJoystick().act();
        game2.getJoystick().right();

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ (Я$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼## #☼\n" +
                "☼ [)$☼\n" +
                "☼#*##☼\n" +
                "☼☼☼☼☼☼\n");

        for (int c = 2; c < Brick.DRILL_TIMER; c++) {
            field.tick();
        }

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼#Z##☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼####☼\n" +
                "☼  )$☼\n" +
                "☼#Ѡ##☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener2).event(Events.KILL_HERO);
        verify(listener1).event(Events.KILL_ENEMY);
        assertTrue(game2.isGameOver());

        when(dice.next(anyInt())).thenReturn(1, 4);

        game2.newGame();

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼  Я$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼  )$☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();

        when(dice.next(anyInt())).thenReturn(1, 2);

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼(   ☼\n" +
                "☼####☼\n" +
                "☼$  ►☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼►   ☼\n" +
                "☼####☼\n" +
                "☼$  (☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        verify(listener1).event(Events.GET_GOLD);

    }

    // можно ли проходить героям друг через дурга? Нет
    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtWay() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(1, 2);
        setupPlayer2(2, 2);

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtLadder() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ H  ☼" +
                "☼ H  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(1, 2);
        setupPlayer2(2, 4);

        atGame1("☼☼☼☼☼☼\n" +
                "☼ (  ☼\n" +
                "☼ H  ☼\n" +
                "☼►H  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().down();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().up();
        game2.getJoystick().down();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ U  ☼\n" +
                "☼ Y  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICantGoViaAnotherPlayer_whenAtPipe() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ ~~ ☼" +
                "☼#  #☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(1, 3);
        setupPlayer2(4, 3);

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼►~~(☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        atGame2("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ Є{ ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ }Э ☼\n" +
                "☼#  #☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // могу ли я сверлить под другим героем? Нет
    @Test
    public void shouldICantDrillUnderOtherHero() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼►►  ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(1, 2);
        setupPlayer2(2, 2);

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().act();
        game2.getJoystick().left();
        game2.getJoystick().act();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼►)  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // если я прыгаю сверху на героя, то я должен стоять у него на голове
    @Test
    public void shouldICantStayAtOtherHeroHead() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼####☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(2, 4);
        setupPlayer2(2, 2);

        atGame1("☼☼☼☼☼☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().down();  //и даже если я сильно захочу я не смогу впрыгнуть в него

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // если я прыгаю сверху на героя который на трубе, то я должен стоять у него на голове
    @Test
    public void shouldICantStayAtOtherHeroHeadWhenOnPipe() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼ ~  ☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(2, 4);
        setupPlayer2(2, 2);
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼    ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().down();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ Є  ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldCanMoveWhenAtOtherHero() {
        shouldICantStayAtOtherHeroHeadWhenOnPipe();

        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼)~  ☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼)}  ☼\n" +
                "☼☼☼☼☼☼\n");

        game2.getJoystick().right();  // нельзя входить в друг в друга :) даже на трубе
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼(}  ☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().left();  // нельзя входить в друг в друга :) даже на трубе
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼({  ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    // когда два героя на трубе, они не могут друг в друга войти
    @Test
    public void shouldStopOnPipe() {
        setupGm("☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼~~~~☼" +
                "☼    ☼" +
                "☼    ☼" +
                "☼☼☼☼☼☼");

        setupPlayer1(2, 4);
        setupPlayer2(3, 4);
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Є~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().right();
        game2.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼~}Э~☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n");
    }

    private void atGame1(String expected) {
        assertEquals(expected, game1.getBoardAsString());
    }

    private void atGame2(String expected) {
        assertEquals(expected, game2.getBoardAsString());
    }

    private void atGame3(String expected) {
        assertEquals(expected, game3.getBoardAsString());
    }

    private void setupPlayer2(int x, int y) {
        listener2 = mock(EventListener.class);
        game2 = new Single(new Player(listener2), printerFactory);
        game2.on(field);
        when(dice.next(anyInt())).thenReturn(x, y);
        game2.newGame();
    }

    private void setupPlayer3(int x, int y) {
        listener3 = mock(EventListener.class);
        game3 = new Single(new Player(listener3), printerFactory);
        game3.on(field);
        when(dice.next(anyInt())).thenReturn(x, y);
        game3.newGame();
    }

    private void setupPlayer1(int x, int y) {
        listener1 = mock(EventListener.class);
        game1 = new Single(new Player(listener1), printerFactory);
        game1.on(field);
        when(dice.next(anyInt())).thenReturn(x, y);
        game1.newGame();
    }

    private void setupGm(String map) {
        Level level = new LevelImpl(map);
        dice = mock(Dice.class);
        field = new Loderunner(level, dice);
    }

    @Test
    public void shouldICanGoWhenIAmAtOtherHero() {
        shouldICantStayAtOtherHeroHead();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game1.getJoystick().left();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼]   ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼◄(  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }

    @Test
    public void shouldICanGoWhenAtMeIsOtherHero() {
        shouldICantStayAtOtherHeroHead();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ►  ☼\n" +
                "☼ (  ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        game2.getJoystick().right();
        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ [  ☼\n" +
                "☼  ( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");

        field.tick();

        atGame1("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ►( ☼\n" +
                "☼####☼\n" +
                "☼☼☼☼☼☼\n");
    }
}
