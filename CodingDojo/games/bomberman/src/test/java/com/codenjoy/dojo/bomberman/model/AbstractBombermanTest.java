package com.codenjoy.dojo.bomberman.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.bomberman.services.DefaultGameSettings;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.multiplayer.Single;
import com.codenjoy.dojo.services.printer.PrinterFactory;
import com.codenjoy.dojo.services.printer.PrinterFactoryImpl;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import org.junit.Before;
import org.mockito.stubbing.OngoingStubbing;

import java.util.Collections;

import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AbstractBombermanTest {


    public int SIZE = 5;
    protected Game game;
    protected Hero hero;
    protected Level level;
    private WallsImpl walls;
    protected GameSettings settings;
    protected EventListener listener;
    protected Dice chopperDice;
    protected Dice wallDice;
    protected Dice heroDice;
    protected Player player;
    protected Bomberman field;
    private final PrinterFactory printer = new PrinterFactoryImpl();

    @Before
    public void setUp() {
        chopperDice = mock(Dice.class);
        wallDice = mock(Dice.class);
        heroDice = mock(Dice.class);

        level = mock(Level.class);
        canDropBombs(1);
        bombsPower(1);
        walls = mock(WallsImpl.class);
        when(walls.iterator()).thenReturn(Collections.emptyIterator());
        settings = mock(GameSettings.class);
        listener = mock(EventListener.class);

        withWalls(walls);
        when(settings.getLevel()).thenReturn(level);
        when(settings.getRoundSettings()).thenReturn(getRoundSettings());
        when(settings.killOtherHeroScore()).thenReturn(v(200));
        when(settings.killMeatChopperScore()).thenReturn(v(100));
        when(settings.killWallScore()).thenReturn(v(10));
        when(settings.catchPerkScore()).thenReturn(v(5));

        initHero();
        givenBoard(SIZE);
        PerksSettingsWrapper.clear();
    }

    protected void initHero() {
        dice(heroDice, 0, 0);
        Hero hero = new Hero(level, heroDice);
        when(settings.getHero(level)).thenReturn(hero);
        when(settings.getDice()).thenReturn(heroDice);
        this.hero = hero;
    }

    public static RoundSettingsWrapper getRoundSettings() {
        return new DefaultGameSettings(mock(Dice.class)).getRoundSettings();
    }

    protected void givenBoard(int size) {
        when(settings.getBoardSize()).thenReturn(v(size));
        field = new Bomberman(settings);
        player = new Player(listener, getRoundSettings().roundsEnabled());
        game = new Single(player, printer);
        game.on(field);
        game.newGame();
        hero = (Hero)game.getJoystick();
    }

    protected void gotoMaxUp() {
        for (int y = 0; y <= SIZE + 1; y++) {
            hero.up();
            field.tick();
        }
    }

    protected void newGameForDied() {
        if (!player.isAlive()) {
            field.newGame(player);
        }
        hero = player.getHero();
        hero.setAlive(true);
    }

    protected void canDropBombs(int countBombs) {
        reset(level);
        when(level.bombsCount()).thenReturn(countBombs);
    }

    protected void assertHeroDie() {
        assertEquals("Expected game over", true, game.isGameOver());
    }

    protected void assertHeroAlive() {
        assertFalse(game.isGameOver());
    }

    protected void gotoBoardCenter() {
        for (int y = 0; y < SIZE / 2; y++) {
            hero.up();
            field.tick();
            hero.right();
            field.tick();
        }
    }

    protected void asrtBrd(String expected) {
        assertEquals(expected, printer.getPrinter(
                field.reader(), player).print());
    }


    protected void givenBoardWithWalls() {
        givenBoardWithWalls(SIZE);
    }

    protected void givenBoardWithWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        dice(heroDice, 1, 1);  // hero в левом нижнем углу
        givenBoard(size);
    }

    protected void givenBoardWithDestroyWalls() {
        givenBoardWithDestroyWalls(SIZE);
    }

    protected void givenBoardWithDestroyWalls(int size) {
        withWalls(new DestroyWalls(new OriginalWalls(v(size))));
        dice(heroDice, 1, 1);  // hero в левом нижнем углу
        givenBoard(size);
    }

    protected void withWalls(Walls walls) {
        when(settings.getWalls()).thenReturn(walls);
    }

    protected void givenBoardWithOriginalWalls() {
        givenBoardWithOriginalWalls(SIZE);
    }

    protected void givenBoardWithOriginalWalls(int size) {
        withWalls(new OriginalWalls(v(size)));
        dice(heroDice, 1, 1);  // hero в левом нижнем углу
        givenBoard(size);
    }

    protected void bombsPower(int power) {
        when(level.bombsPower()).thenReturn(power);
    }

    protected void assertBombPower(int power, String expected) {
        givenBoardWithOriginalWalls(9);
        bombsPower(power);

        hero.act();
        goOut();
        field.tick();

        asrtBrd(expected);
    }

    protected void goOut() {
        hero.right();
        field.tick();
        hero.right();
        field.tick();
        hero.up();
        field.tick();
        hero.up();
        field.tick();
    }

    protected void dice(Dice dice, int... values) {
        OngoingStubbing<Integer> when = when(dice.next(anyInt()));
        for (int value : values) {
            when = when.thenReturn(value);
        }
    }

    protected void givenBoardWithMeatChopper(int size) {
        dice(chopperDice, size - 2, size - 2);

        SIZE = size;
        MeatChoppers walls = new MeatChoppers(new OriginalWalls(v(size)), v(1), chopperDice);
        withWalls(walls);

        dice(heroDice, 1, 1);  // hero в левом нижнем углу
        givenBoard(size);

        walls.init(field);
        walls.regenerate();

        dice(chopperDice, 1, Direction.UP.value());  // Чертик будет упираться в стенку и стоять на месте
    }

    protected void givenBoardWithDestroyWallsAt(Point wall, Point hero) {
        withWalls(new AbstractBombermanTest.DestroyWallAt(wall.getX(), wall.getY(), new WallsImpl()));
        dice(heroDice, hero.getX(), hero.getY());
        givenBoard(SIZE);
    }

    protected void givenBoardWithMeatChopperAt(Point chopper, Point hero) {
        withWalls(new AbstractBombermanTest.MeatChopperAt(chopper.getX(), chopper.getY(), new WallsImpl()));
        dice(heroDice, hero.getX(), hero.getY());
        givenBoard(SIZE);
    }

    static class DestroyWallAt extends WallsDecorator {

        public DestroyWallAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new DestroyWall(x, y));
        }

        @Override
        public Wall destroy(Point pt) {   // неразрушаемая стенка
            return walls.get(pt);
        }

    }

    static class MeatChopperAt extends WallsDecorator {

        public MeatChopperAt(int x, int y, Walls walls) {
            super(walls);
            walls.add(new MeatChopper(x, y));
        }

        @Override
        public Wall destroy(Point pt) {   // неубиваемый монстрик
            return walls.get(pt);
        }

    }

}
