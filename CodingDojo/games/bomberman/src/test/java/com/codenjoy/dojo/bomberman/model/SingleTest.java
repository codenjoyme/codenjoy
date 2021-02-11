package com.codenjoy.dojo.bomberman.model;

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


import com.codenjoy.dojo.bomberman.model.perks.PerksSettingsWrapper;
import com.codenjoy.dojo.services.Direction;
import com.codenjoy.dojo.services.Joystick;
import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class SingleTest extends AbstractSingleTest {

    @Before
    public void setup() {
        super.setup();
    }

    @Test
    public void shouldGameReturnsRealJoystick() {
        givenBoard();

        hero(0).act();
        hero(1).up();
        tick();

        hero(1).up();
        tick();

        tick();
        tick();
        tick();

        assertFalse(hero(0).isAlive());
        assertTrue(hero(1).isAlive());

        Joystick joystick1 = game(0).getJoystick();
        Joystick joystick2 = game(0).getJoystick();

        // when
        game(0).newGame();
        game(1).newGame();

        // then
        assertNotSame(joystick1, game(0).getJoystick());
        assertNotSame(joystick2, game(0).getJoystick());
    }

    @Test
    public void shouldGetTwoHeroesOnBoard() {
        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        assertSame(hero(0), game(0).getJoystick());
        assertSame(hero(1), game(1).getJoystick());

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n", game(1));
    }

    @Test
    public void shouldOnlyOneListenerWorksWhenOneHeroKillAnother() {
        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        tick();

        hero(0).up();
        tick();

        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "҉    \n" +
                "҉♣   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => [DIED]\n");
    }

    @Test
    public void shouldPrintOtherBombHero() {
        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☻♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♠☺   \n", game(1));
    }

    @Test
    public void shouldHeroCantGoToAnotherHero() {
        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).right();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game(0));
    }
    
    // бомбермен может идти на митчопера, при этом он умирает
    @Test
    public void shouldKllOtherHeroWhenHeroGoToMeatChopper() {
        meatChopperAt(2, 0);

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game(0));

        hero(1).right();
        tick();
        // от имени наблюдателя вижу опасность - митчопера
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +

                "☺ &  \n", game(0));

        // от имени жертвы вижу свой трупик
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥ Ѡ  \n", game(1));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [DIED]\n");
    }

    // если митчопер убил другого бомбермена, как это на моей доске отобразится? Хочу видеть трупик
    @Test
    public void shouldKllOtherHeroWhenMeatChopperGoToIt() {
        MeatChopper chopper = meatChopperAt(2, 0);

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game(0));

        chopper.setDirection(Direction.LEFT);
        tick();

        // от имени наблюдателя я там вижу опасность - митчопера, мне не интересны останки игроков
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺&   \n", game(0));

        // от имени жертвы я вижу свой трупик, мне пофиг уже что на карте происходит, главное где поставить памятник герою
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥Ѡ   \n", game(1));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [DIED]\n");
    }

    // А что если бомбермен идет на митчопера а тот идет на встречу к нему - бомбермен проскочит или умрет? должен умереть!
    @Test
    public void shouldKllOtherHeroWhenMeatChopperAndHeroMoves() {
        MeatChopper chopper = meatChopperAt(2, 0);

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥&  \n", game(0));

        chopper.setDirection(Direction.LEFT);
        hero(1).right();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺&♣  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥&Ѡ  \n", game(1));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => [DIED]\n");
    }

    //  бомбермены не могут ходить по бомбам ни по своим ни по чужим
    @Test
    public void shouldHeroCantGoToBombFromAnotherHero() {
        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(1).act();
        hero(1).right();
        tick();

        hero(1).right();
        hero(0).right();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺3 ♥ \n", game(0));

        hero(1).left();
        tick();

        hero(1).left();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺1♥  \n", game(0));
    }

    private void givenBoard() {
        super.givenBoard(2);
    }

    @Test
    public void shouldBombKillAllHero() {
        shouldHeroCantGoToBombFromAnotherHero();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "Ѡ҉♣  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "♣҉Ѡ  \n", game(1));
    }

    @Test
    public void shouldNewGamesWhenKillAll() {
        shouldBombKillAllHero();

        when(settings.getHero(any(Level.class))).thenReturn(new Hero(level, heroDice), new Hero(level, heroDice));

        dice(heroDice,
                0, 0,
                1, 0);
        game(0).newGame();
        game(1).newGame();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n", game(1));
    }

    // на поле можно чтобы каждый поставил то количество бомб которое ему позволено и не более того
    @Test
    public void shouldTwoBombsOnBoard() {
        bombsCount = 1;

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();

        hero(1).act();
        hero(1).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n", game(0));

        hero(0).act();
        hero(0).up();

        hero(1).act();
        hero(1).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺♥   \n" +
                "     \n" +
                "33   \n", game(0));

    }

    @Test
    public void shouldFourBombsOnBoard() {
        bombsCount = 2;

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();

        hero(1).act();
        hero(1).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n", game(0));

        hero(0).act();
        hero(0).up();

        hero(1).act();
        hero(1).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺♥   \n" +
                "44   \n" +
                "33   \n", game(0));

        hero(0).act();
        hero(0).up();

        hero(1).act();
        hero(1).up();

        tick();

        asrtBrd("     \n" +
                "☺♥   \n" +
                "     \n" +
                "33   \n" +
                "22   \n", game(0));
    }

    @Test
    public void shouldFourBombsOnBoard_checkTwoBombsPerHero() {
        bombsCount = 2;

        dice(heroDice,
                0, 0,
                1, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺    \n" +
                "4♥   \n", game(0));

        hero(0).act();
        hero(0).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "4    \n" +
                "3♥   \n", game(0));

        hero(0).act();
        hero(0).up();

        tick();

        asrtBrd("     \n" +
                "☺    \n" +
                "     \n" +
                "3    \n" +
                "2♥   \n", game(0));
    }

    @Test
    public void shouldFireEventWhenKillWallOnlyForOneHero() {
        destroyWallAt(0, 0);

        dice(heroDice,
                1, 0,
                1, 1);
        givenBoard();

        hero(0).act();
        hero(0).right();
        hero(1).up();
        tick();
        hero(0).right();
        hero(1).up();
        tick();
        hero(0).right();
        hero(1).up();
        tick();
        tick();
        tick();

        asrtBrd(" ♥   \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "H҉҉ ☺\n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_DESTROY_WALL]\n" +
                "listener(1) => []\n");

        tick();

        asrtBrd(" ♥   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "    ☺\n", game(0));
    }

    @Test
    public void shouldFireEventWhenKillMeatChopper() {
        meatChopperAt(0, 0);

        dice(heroDice,
                1, 0,
                1, 1);
        givenBoard();

        hero(0).act();
        hero(0).right();
        hero(1).up();
        tick();
        hero(0).right();
        hero(1).up();
        tick();
        hero(0).right();
        hero(1).up();
        tick();
        tick();
        tick();

        asrtBrd(" ♥   \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "x҉҉ ☺\n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_MEAT_CHOPPER]\n" +
                "listener(1) => []\n");

        tick();

        asrtBrd(" ♥   \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "    ☺\n", game(0));
    }

    @Test
    public void bug() {
        destroyWallAt(0, 0);
        meatChopperAt(1, 0);
        meatChopperAt(2, 0);

        dice(heroDice,
                1, 1,
                2, 1);
        givenBoard();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺♥  \n" +
                "#&&  \n", game(0));

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();

        hero(0).left();
        hero(1).right();
        tick();

        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺҉҉♥ \n" +
                "҉҉҉҉ \n" +
                "#xx  \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [KILL_MEAT_CHOPPER]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺  ♥ \n" +
                "     \n" +
                "#    \n", game(0));
    }

    @Override
    protected RoundSettingsWrapper getRoundSettings() {
        return BombermanTest.getRoundSettings();
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenDestroyWall_caseDied() {
        destroyWallAt(1, 0);

        dice(heroDice,
                0, 0,
                2, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();
        tick();
        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ ♣  \n" +
                "҉H҉҉ \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_DESTROY_WALL]\n" +
                "listener(1) => [DIED, KILL_DESTROY_WALL]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ ♣  \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenDestroyWall_caseAlive() {
        destroyWallAt(1, 0);

        dice(heroDice,
                0, 0,
                2, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺ ♥  \n" +
                "҉ ҉  \n" +
                "҉H҉҉ \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_DESTROY_WALL]\n" +
                "listener(1) => [KILL_DESTROY_WALL]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺ ♥  \n" +
                "     \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenTwoDestroyWalls_caseDied() {
        destroyWallAt(2, 0);
        destroyWallAt(1, 0);

        bombsPower = 2;

        dice(heroDice,
                0, 0,
                3, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();

        tick();
        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ  ♣ \n" +
                "҉HH҉҉\n", game(0));

        // по 1 ачивке за стенку, потому что взрывная волна не проходит через стенку
        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_DESTROY_WALL]\n" +
                "listener(1) => [DIED, KILL_DESTROY_WALL]\n");

         tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ  ♣ \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenFourDestroyWalls_caseDied() {
        destroyWallAt(2, 2);

        dice(heroDice,
                1, 2,
                2, 1,
                3, 2,
                2, 3);
        givenBoard(4);

        hero(0).act();
        hero(1).act();
        hero(2).act();
        hero(3).act();
        tick();

        tick();
        tick();
        tick();
        tick();

        asrtBrd("  ҉  \n" +
                " ҉♣҉ \n" +
                "҉ѠH♣҉\n" +
                " ҉♣҉ \n" +
                "  ҉  \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_DESTROY_WALL]\n" +
                        "listener(1) => [DIED, KILL_DESTROY_WALL]\n" +
                        "listener(2) => [DIED, KILL_DESTROY_WALL]\n" +
                        "listener(3) => [DIED, KILL_DESTROY_WALL]\n");

        tick();

        asrtBrd("     \n" +
                "  ♣  \n" +
                " Ѡ ♣ \n" +
                "  ♣  \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenFourDestroyWalls_caseDied_caseNotEqualPosition() {
        destroyWallAt(1, 1);
        destroyWallAt(2, 2);
        destroyWallAt(0, 2);

        dice(heroDice,
                1, 2,
                2, 1,
                3, 2,
                2, 3);
        givenBoard(4);

        hero(0).act();
        hero(1).act();
        hero(2).act();
        hero(3).act();
        tick();

        tick();
        tick();
        tick();
        tick();

        asrtBrd("  ҉  \n" +
                " ҉♣҉ \n" +
                "HѠH♣҉\n" +  // первую стенку подбил монополист, центральную все
                " H♣҉ \n" +  // эту стенку подбили только лвое
                "  ҉  \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_DESTROY_WALL, KILL_DESTROY_WALL, KILL_DESTROY_WALL]\n" +
                "listener(1) => [DIED, KILL_DESTROY_WALL, KILL_DESTROY_WALL]\n" +
                "listener(2) => [DIED, KILL_DESTROY_WALL]\n" +
                "listener(3) => [DIED, KILL_DESTROY_WALL]\n");

        tick();

        asrtBrd("     \n" +
                "  ♣  \n" +
                " Ѡ ♣ \n" +
                "  ♣  \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenTwoDestroyWalls_caseAlive() {
        destroyWallAt(2, 0);
        destroyWallAt(1, 0);

        bombsPower = 2;

        dice(heroDice,
                0, 0,
                3, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        tick();
        tick();

        asrtBrd("     \n" +
                "☺  ♥ \n" +
                "҉  ҉ \n" +
                "҉  ҉ \n" +
                "҉HH҉҉\n", game(0));

        // по 1 ачивке за стенку, потому что взрывная волна не проходит через стенку
        events.verifyAllEvents(
                "listener(0) => [KILL_DESTROY_WALL]\n" +
                "listener(1) => [KILL_DESTROY_WALL]\n");

        tick();

        asrtBrd("     \n" +
                "☺  ♥ \n" +
                "     \n" +
                "     \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenMeatChopper_caseDied() {
        meatChopperAt(1, 0);

        dice(heroDice,
                0, 0,
                2, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();
        tick();
        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ ♣  \n" +
                "҉x҉҉ \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [DIED, KILL_MEAT_CHOPPER]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "Ѡ ♣  \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenMeatChopper_caseAlive() {
        meatChopperAt(1, 0);

        dice(heroDice,
                0, 0,
                2, 0);
        givenBoard();

        hero(0).act();
        hero(0).up();
        hero(1).act();
        hero(1).up();
        tick();

        hero(0).up();
        hero(1).up();
        tick();

        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺ ♥  \n" +
                "҉ ҉  \n" +
                "҉x҉҉ \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [KILL_MEAT_CHOPPER]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺ ♥  \n" +
                "     \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenFourMeatChoppers_caseDied() {
        meatChopperAt(2, 2);

        dice(heroDice,
                1, 2,
                2, 1,
                3, 2,
                2, 3);
        givenBoard(4);

        hero(0).act();
        hero(1).act();
        hero(2).act();
        hero(3).act();
        tick();

        tick();
        tick();
        tick();
        tick();

        asrtBrd("  ҉  \n" +
                " ҉♣҉ \n" +
                "҉Ѡx♣҉\n" +
                " ҉♣҉ \n" +
                "  ҉  \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [DIED, KILL_MEAT_CHOPPER]\n" +
                "listener(2) => [DIED, KILL_MEAT_CHOPPER]\n" +
                "listener(3) => [DIED, KILL_MEAT_CHOPPER]\n");

        tick();

        asrtBrd("     \n" +
                "  ♣  \n" +
                " Ѡ ♣ \n" +
                "  ♣  \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldPerkCantSpawnFromMeetChopper() {
        meatChopperAt(1, 0);

        dice(heroDice,
                0, 0);
        givenBoard(1);

        PerksSettingsWrapper.setPerkSettings(Elements.BOMB_BLAST_RADIUS_INCREASE, 4, 3);
        PerksSettingsWrapper.setDropRatio(20); // 20%
        PerksSettingsWrapper.setPickTimeout(50);

        hero(0).act();
        tick();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "҉    \n" +
                "҉x   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [KILL_MEAT_CHOPPER]\n");

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "     \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenBigBadaboom() {
        bombsCount = 2;
        when(settings.isBigBadaboom()).thenReturn(new SimpleParameter<>(true));
        PerksSettingsWrapper.setDropRatio(0);

        destroyWallAt(2, 2);
        meatChopperAt(0, 1);
        meatChopperAt(0, 3);
        meatChopperAt(4, 1);
        meatChopperAt(4, 3);

        dice(heroDice,
                0, 0,
                1, 0,
                2, 0,
                3, 0);
        givenBoard(4);

        // бомба, которой все пордорвем
        hero(0).move(1, 2);
        hero(0).act();
        tick();

        hero(0).move(1, 3);
        hero(0).act();
        hero(0).move(0, 0);

        hero(1).move(2, 1);
        hero(1).act();
        hero(1).move(1, 1);
        hero(1).act();
        hero(1).move(1, 0);
        tick();

        hero(2).move(3, 2);
        hero(2).act();
        hero(2).move(3, 1);
        hero(2).act();
        hero(2).move(2, 0);
        tick();

        hero(3).move(2, 3);
        hero(3).act();
        hero(3).move(3, 3);
        hero(3).act();
        hero(3).move(3, 0);

        tick();

        asrtBrd("     \n" +
                "&244&\n" +
                " 1#3 \n" +
                "&223&\n" +
                "☺♥♥♥ \n",
                game(0));

        hero(0).move(0, 0);
        hero(1).move(1, 1);
        hero(2).move(3, 1);
        hero(3).move(3, 3);

        asrtBrd("     \n" +
                "&24♠&\n" +
                " 1#3 \n" +
                "&♠2♠&\n" +
                "☺    \n",
                game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_DESTROY_WALL, KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [DIED, KILL_OTHER_HERO, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]\n" +
                "listener(2) => [DIED, KILL_OTHER_HERO, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]\n" +
                "listener(3) => [DIED, KILL_DESTROY_WALL, KILL_MEAT_CHOPPER]\n");

        asrtBrd(" ҉҉҉ \n" +
                "x҉҉♣x\n" +
                "҉҉H҉҉\n" +
                "x♣҉♣x\n" +
                "☺҉҉҉ \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        asrtBrd("     \n" +
                "   ♣ \n" +
                "     \n" +
                " ♣ ♣ \n" +
                "☺    \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenBigBadaboom_caseKillAll() {
        bombsCount = 2;
        when(settings.isBigBadaboom()).thenReturn(new SimpleParameter<>(true));
        PerksSettingsWrapper.setDropRatio(0);

        destroyWallAt(2, 2);
        meatChopperAt(0, 1);
        meatChopperAt(0, 3);
        meatChopperAt(4, 1);
        meatChopperAt(4, 3);

        dice(heroDice,
                0, 0,
                1, 0,
                2, 0,
                3, 0);
        givenBoard(4);

        // бомба, которой все пордорвем
        hero(0).move(1, 2);
        hero(0).act();
        tick();

        hero(0).move(1, 3);
        hero(0).act();
        hero(0).move(0, 0);

        hero(1).move(2, 1);
        hero(1).act();
        hero(1).move(1, 1);
        hero(1).act();
        hero(1).move(1, 0);
        tick();

        hero(2).move(3, 2);
        hero(2).act();
        hero(2).move(3, 1);
        hero(2).act();
        hero(2).move(2, 0);
        tick();

        hero(3).move(2, 3);
        hero(3).act();
        hero(3).move(3, 3);
        hero(3).act();
        hero(3).move(3, 0);

        tick();

        asrtBrd("     \n" +
                "&244&\n" +
                " 1#3 \n" +
                "&223&\n" +
                "☺♥♥♥ \n",
                game(0));

        hero(0).move(1, 3);
        hero(1).move(1, 1);
        hero(2).move(3, 1);
        hero(3).move(3, 3);

        asrtBrd("     \n" +
                "&☻4♠&\n" +
                " 1#3 \n" +
                "&♠2♠&\n" +
                "     \n",
                game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_OTHER_HERO, KILL_DESTROY_WALL, KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [DIED, KILL_OTHER_HERO, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]\n" +
                "listener(2) => [DIED, KILL_OTHER_HERO, KILL_MEAT_CHOPPER, KILL_DESTROY_WALL]\n" +
                "listener(3) => [DIED, KILL_OTHER_HERO, KILL_DESTROY_WALL, KILL_MEAT_CHOPPER]\n");

        asrtBrd(" ҉҉҉ \n" +
                "xѠ҉♣x\n" +
                "҉҉H҉҉\n" +
                "x♣҉♣x\n" +
                " ҉҉҉ \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        asrtBrd("     \n" +
                " Ѡ ♣ \n" +
                "     \n" +
                " ♣ ♣ \n" +
                "     \n", game(0));
    }

    @Test
    public void shouldCrossBlasts_checkingScores_whenNotBigBadaboom_caseKillAll() {
        bombsCount = 2;
        when(settings.isBigBadaboom()).thenReturn(new SimpleParameter<>(false));
        PerksSettingsWrapper.setDropRatio(0);

        destroyWallAt(2, 2);
        meatChopperAt(0, 1);
        meatChopperAt(0, 3);
        meatChopperAt(4, 1);
        meatChopperAt(4, 3);

        dice(heroDice,
                0, 0,
                1, 0,
                2, 0,
                3, 0);
        givenBoard(4);

        // бомба, которой все пордорвем
        hero(0).move(1, 2);
        hero(0).act();
        tick();

        hero(0).move(1, 3);
        hero(0).act();
        hero(0).move(0, 0);

        hero(1).move(2, 1);
        hero(1).act();
        hero(1).move(1, 1);
        hero(1).act();
        hero(1).move(1, 0);
        tick();

        hero(2).move(3, 2);
        hero(2).act();
        hero(2).move(3, 1);
        hero(2).act();
        hero(2).move(2, 0);
        tick();

        hero(3).move(2, 3);
        hero(3).act();
        hero(3).move(3, 3);
        hero(3).act();
        hero(3).move(3, 0);

        tick();

        asrtBrd("     \n" +
                "&244&\n" +
                " 1#3 \n" +
                "&223&\n" +
                "☺♥♥♥ \n",
                game(0));

        hero(0).move(1, 3);
        hero(1).move(1, 1);
        hero(2).move(3, 1);
        hero(3).move(3, 3);

        asrtBrd("     \n" +
                "&☻4♠&\n" +
                " 1#3 \n" +
                "&♠2♠&\n" +
                "     \n",
                game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_OTHER_HERO, KILL_DESTROY_WALL]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        asrtBrd("     \n" +
                "&Ѡ3♠&\n" +
                "҉҉H2 \n" +
                "&11♠&\n" +
                "     \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => [KILL_MEAT_CHOPPER]\n" +
                "listener(1) => [KILL_OTHER_HERO, KILL_MEAT_CHOPPER]\n" +
                "listener(2) => [DIED]\n" +
                "listener(3) => []\n");

        asrtBrd(" ҉   \n" +
                "xѠ2♠&\n" +
                " ҉҉1 \n" +
                "x♣҉1&\n" +
                " ҉҉  \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => [KILL_OTHER_HERO, KILL_MEAT_CHOPPER]\n" +
                "listener(3) => [DIED]\n");

        asrtBrd("     \n" +
                " Ѡ11&\n" +
                "  ҉҉҉\n" +
                " ♣҉♣x\n" +
                "   ҉ \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => [KILL_MEAT_CHOPPER]\n");

        asrtBrd("  ҉҉ \n" +
                " Ѡ҉♣x\n" +
                "  ҉҉ \n" +
                " ♣ ♣ \n" +
                "     \n", game(0));

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n");

        asrtBrd("     \n" +
                " Ѡ ♣ \n" +
                "     \n" +
                " ♣ ♣ \n" +
                "     \n", game(0));
    }
}
