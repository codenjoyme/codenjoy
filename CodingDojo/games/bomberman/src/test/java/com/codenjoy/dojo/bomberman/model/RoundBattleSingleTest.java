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

import com.codenjoy.dojo.services.round.RoundSettingsWrapper;
import com.codenjoy.dojo.services.settings.Parameter;
import com.codenjoy.dojo.services.settings.SimpleParameter;
import org.junit.Test;

import static com.codenjoy.dojo.services.Direction.DOWN;
import static com.codenjoy.dojo.services.settings.SimpleParameter.v;
import static org.junit.Assert.assertEquals;

public class RoundBattleSingleTest extends AbstractSingleTest {

    public static final int DEFAULT_COUNT = 3;
    private int minTicksForWin = 1;
    private int timePerRound = 10;
    private int timeForWinner = 2;
    private int timeBeforeStart = 5;
    private int roundsPerMatch = 3;

    @Override
    protected RoundSettingsWrapper getRoundSettings() {
        return new RoundSettingsWrapper() {

            @Override
            public Parameter<Integer> timeBeforeStart() {
                return v(timeBeforeStart);
            }

            @Override
            public Parameter<Integer> roundsPerMatch() {
                return v(roundsPerMatch);
            }

            @Override
            public Parameter<Integer> minTicksForWin() {
                return v(minTicksForWin);
            }

            @Override
            public Parameter<Integer> timePerRound() {
                return v(timePerRound);
            }

            @Override
            public Parameter<Integer> timeForWinner() {
                return v(timeForWinner);
            }

            @Override
            public Parameter<Boolean> roundsEnabled() {
                return new SimpleParameter<>(true);
            }
        };
    }

    // во время старта игры, когда не прошло timeBeforeStart тиков,
    // все игроки неактивны (видно их трупики)
    @Test
    public void shouldAllPlayersOnBoardIsInactive_whenStart() {
        playersPerRoom.update(DEFAULT_COUNT);
        setup();

        dice(heroDice,
                0, 0,
                1, 0,
                1, 1);
        givenBoard(DEFAULT_COUNT);

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));
    }

    // после старта идет отсчет обратного времени
    @Test
    public void shouldCountdownBeforeRound_whenTicksOnStart() {
        shouldAllPlayersOnBoardIsInactive_whenStart();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [[....4....]]\n" +
                "listener(1) => [[....4....]]\n" +
                "listener(2) => [[....4....]]\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [[...3...]]\n" +
                "listener(1) => [[...3...]]\n" +
                "listener(2) => [[...3...]]\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [[..2..]]\n" +
                "listener(1) => [[..2..]]\n" +
                "listener(2) => [[..2..]]\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => [[.1.]]\n" +
                "listener(1) => [[.1.]]\n" +
                "listener(2) => [[.1.]]\n");
    }

    // пока идет обратный отсчет я не могу ничего предпринимать, а герои отображаются на карте как трупики
    // но после объявления раунда я могу начать играть
    @Test
    public void shouldActiveAndCanMove_afterCountdown() {
        shouldCountdownBeforeRound_whenTicksOnStart();

        // пока еще не активны
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "♣Ѡ   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " Ѡ   \n" +
                "♣♣   \n", game(2));

        // и я не могу ничего поделать с ними
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        // после сообщения что раунд начался
        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        // можно играть - игроки видны как активные
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "♥☺   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "♥♥   \n", game(2));

        // ... и когда я муваю героев, они откликаются
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "☺    \n" +
                "  ♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ♥   \n" +
                "♥    \n" +
                "  ☺  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ☺   \n" +
                "♥    \n" +
                "  ♥  \n", game(2));
    }

    // если один игрок вынесет другого но на поле есть едще игроки,
    // то тот, которого вынесли появится в новом месте в виде трупика
    @Test
    public void shouldMoveToInactive_whenKillSomeone() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1; // TODO а что будет если тут 0 игра хоть начнется?
        setup();

        dice(heroDice,
                0, 0,
                1, 0,
                1, 1);
        givenBoard(DEFAULT_COUNT);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "☺♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♥   \n" +
                "♥☺   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "♥♥   \n", game(2));

        // когда я выношу одного игрока
        hero(2).act();
        tick();

        hero(2).right();
        tick();

        hero(2).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "  ☺  \n" +
                " 1   \n" +
                "♥♥   \n", game(2));

        // игрок активный и живой
        assertEquals(true, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());

        tick();

        // игрок активный но неживой (cервер ему сделает newGame)
        assertEquals(true, hero(1).isActive());
        assertEquals(false, hero(1).isAlive());
        // тут без изменений
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());


        asrtBrd("     \n" +
                "     \n" +
                " ҉☺  \n" +
                "҉҉҉  \n" +
                "♥♣   \n", game(2));

        tick();

        dice(heroDice, 3, 4); // новые координаты для героя
        field.newGame(player(1)); // это сделоает сервер в ответ на isAlive = false
        resetHeroes();

        // игрок уже живой но неактивный до начала следующего раунда
        assertEquals(false, hero(1).isActive());
        assertEquals(true, hero(1).isAlive());
        // тут без изменений
        assertEquals(true, player(1).wantToStay());
        assertEquals(false, player(1).shouldLeave());

        asrtBrd("   ♣ \n" +
                "     \n" +
                "  ☺  \n" +
                "     \n" +
                "♥    \n", game(2));
    }

    // если один игрок вынесет обоих, то должен получить за это очки
    @Test
    public void shouldGetWinRoundScores_whenKillAllEnemies() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        setup();

        dice(heroDice,
                1, 1, // первый игрок
                0, 1, // второй
                1, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "♥☺   \n" +
                " ♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                " ♥   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "♥♥   \n" +
                " ☺   \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "  ☺  \n" +
                "♥1   \n" +
                " ♥   \n", game(0));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                " ҉☺  \n" +
                "♣҉҉  \n" +
                " ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "Ѡ҉҉  \n" +
                " ♣   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "♣҉҉  \n" +
                " Ѡ   \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO, WIN_ROUND]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => [DIED]\n");
    }

    // если на карте один вынес другого, а последний противник покинул игру
    // - очки победителю положено вручить
    @Test
    public void shouldGetWinRoundScores_whenKillOneAndAnotherLeaveTheGame() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        setup();

        dice(heroDice,
                1, 1, // первый игрок, кто побежит
                0, 1, // второй, жертва
                4, 4); // третий, тот кто покинет комнату

        givenBoard(DEFAULT_COUNT);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                    "listener(1) => [START_ROUND, [Round 1]]\n" +
                    "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("    ♥\n" +
                "     \n" +
                "     \n" +
                "♥☺   \n" +
                "     \n", game(0));

        asrtBrd("    ♥\n" +
                "     \n" +
                "     \n" +
                "☺♥   \n" +
                "     \n", game(1));

        asrtBrd("    ☺\n" +
                "     \n" +
                "     \n" +
                "♥♥   \n" +
                "     \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("    ♥\n" +
                "     \n" +
                "  ☺  \n" +
                "♥1   \n" +
                "     \n", game(0));

        tick();

        asrtBrd("    ♥\n" +
                "     \n" +
                " ҉☺  \n" +
                "♣҉҉  \n" +
                " ҉   \n", game(0));

        asrtBrd("    ♥\n" +
                "     \n" +
                " ҉♥  \n" +
                "Ѡ҉҉  \n" +
                " ҉   \n", game(1));

        asrtBrd("    ☺\n" +
                "     \n" +
                " ҉♥  \n" +
                "♣҉҉  \n" +
                " ҉   \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => []\n");

        // а теперь самое интересное - выходим из комнаты оставшимся игроком
        field.remove(player(2));

        asrtBrd("     \n" +
                "     \n" +
                " ҉☺  \n" +
                "♣҉҉  \n" +
                " ҉   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "Ѡ҉҉  \n" +
                " ҉   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                " ҉♥  \n" +
                "♣҉҉  \n" +
                " ҉   \n", game(2));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "  ☺  \n" +
                "♣    \n" +
                "     \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "  ♥  \n" +
                "Ѡ    \n" +
                "     \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "  ♥  \n" +
                "♣    \n" +
                "     \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" + // заслуженная победа
                "listener(1) => []\n" +
                "listener(2) => [DIED]\n"); // за то что он трус )

    }

    // если на карте один вынес другого, а последний противник покинул игру
    // - очки победителю положено вручить
    // но полсе этого если покинет комнату и второй, то мы не должны получить еще раз победные очки
    @Test
    public void shouldNotGetWinRoundScoresTwice_whenDieThenLeaveRoom() {
        // тут один игрок вынес другого, а третий после покинул комнату,
        // за что победитель получил свои очки, а все проигравшие - штрафы
        shouldGetWinRoundScores_whenKillOneAndAnotherLeaveTheGame();

        // а теперь самое интересное - выходим из комнаты оставшимся игроком
        field.remove(player(1));

        // никто больше не должен ничего получить
        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

    }

    // если на поле трое, и один игрок имеет преимущество по очкам за вынос другого игрока
    // то по истечении таймаута раунда он получит очки за победу в раунде
    @Test
    public void shouldGetWinRoundScores_whenKillOneEnemyAdvantage_whenRoundTimeout() {
        int count = 3;

        playersPerRoom.update(count);
        timeBeforeStart = 1;
        setup();

        dice(heroDice,
                1, 1, // первый игрок
                0, 2, // второй - его не накроет волной
                1, 0); // третий - его накроет волной

        givenBoard(count);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                " ☺   \n" +
                " ♥   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                " ♥   \n" +
                " ♥   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                " ♥   \n" +
                " ☺   \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥ ☺  \n" +
                " 1   \n" +
                " ♥   \n", game(0));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥҉☺  \n" +
                "҉҉҉  \n" +
                " ♣   \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "☺҉♥  \n" +
                "҉҉҉  \n" +
                " ♣   \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "♥҉♥  \n" +
                "҉҉҉  \n" +
                " Ѡ   \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => []\n" +
                "listener(2) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "♥ ☺  \n" +
                "     \n" +
                " ♣   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        // вот он последний тик раунда, тут все и случится
        dice(heroDice,
                0, 0,
                1, 0,
                1, 1);
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ♣   \n" +
                "Ѡ♣   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n");
    }

    // если на поле группа игроков, два из них активны и расставляют бомбы
    // так вот после окончания таймаута раунда тот из них победит,
    // кто большее количество игроков вынес
    @Test
    public void shouldGetWinRoundScores_whenKillsAdvantage_whenRoundTimeout() {
        int count = 5;

        playersPerRoom.update(count);
        timeBeforeStart = 1;
        setup();

        dice(heroDice,
                1, 1, // первый активный игрок - будет победителем
                3, 3, // второй активный игрок - будет проигравшим
                1, 0, // жертва первого
                0, 1, // жертва первого
                3, 4); // единственная жертва второго, потому он проиграет по очкам

        givenBoard(count);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n");

        assertBoards(
                "game(0)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥☺   \n" +
                " ♥   \n" +
                "\n" +
                "game(1)\n" +
                "   ♥ \n" +
                "   ☺ \n" +
                "     \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(2)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥♥   \n" +
                " ☺   \n" +
                "\n" +
                "game(3)\n" +
                "   ♥ \n" +
                "   ♥ \n" +
                "     \n" +
                "☺♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(4)\n" +
                "   ☺ \n" +
                "   ♥ \n" +
                "     \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n");

        // пошла движуха
        hero(0).act();
        hero(1).act();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        tick();

        asrtBrd("   ♥ \n" +
                " ♥ 1 \n" +
                "     \n" +
                "♥1 ☺ \n" +
                " ♥   \n", game(0));

        tick();

        assertBoards(
                "game(0)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉☺ \n" +
                " ♣   \n" +
                "\n" +
                "game(1)\n" +
                "   ♣ \n" +
                " ☺҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(2)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " Ѡ   \n" +
                "\n" +
                "game(3)\n" +
                "   ♣ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "Ѡ҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(4)\n" +
                "   Ѡ \n" +
                " ♥҉҉҉\n" +
                " ҉ ҉ \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO]\n" +
                "listener(1) => [KILL_OTHER_HERO]\n" +
                "listener(2) => [DIED]\n" +
                "listener(3) => [DIED]\n" +
                "listener(4) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("   ♣ \n" +
                " ♥   \n" +
                "     \n" +
                "♣  ☺ \n" +
                " ♣   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n");

        // вот он последний тик раунда, тут все и случится
        dice(heroDice,
                0, 2,  // размещаем всех в свободные места
                1, 2,
                2, 2,
                3, 2,
                4, 2);
        tick();
        newGameForAllDied(); // это сделает сервер (вообще он это сделал намного раньше, но для наглядности тут)

        asrtBrd("     \n" +
                "     \n" +
                "Ѡ♣♣♣♣\n" +
                "     \n" +
                "     \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [WIN_ROUND]\n" +
                "listener(1) => [[Time is over]]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n");
    }

    // если на поле группа игроков, два из них активны и расставляют бомбы
    // и даже уничтожили одинаковое количество игроков
    // так вот после окончания таймаута раунда тот из них победит,
    // кто большее очков заработал во время своего экшна (в данном случае коробку)
    // еще проверяем, что спаунится на месте трупиков нельзя (пусть даже они тоже ждут спауна)
    @Test
    public void shouldGetWinRoundScores_whenKillsAdvantagePlusOneBox_whenRoundTimeout() {
        int count = 6;

        playersPerRoom.update(count);
        timeBeforeStart = 1;
        setup();

        dice(heroDice,
                1, 1, // первый активный игрок - будет проигравшим
                3, 3, // второй активный игрок - будет победителем, потому как снесет еще корбку
                1, 0, // жертва первого
                0, 1, // жертва первого
                3, 4, // жертва второго
                4, 3); // жертва второго

        destroyWallAt(3, 2);
        givenBoard(count);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n" +
                "listener(3) => [START_ROUND, [Round 1]]\n" +
                "listener(4) => [START_ROUND, [Round 1]]\n" +
                "listener(5) => [START_ROUND, [Round 1]]\n");

        assertBoards(
                "game(0)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥☺   \n" +
                " ♥   \n" +
                "\n" +
                "game(1)\n" +
                "   ♥ \n" +
                "   ☺♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(2)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ☺   \n" +
                "\n" +
                "game(3)\n" +
                "   ♥ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "☺♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(4)\n" +
                "   ☺ \n" +
                "   ♥♥\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n" +
                "game(5)\n" +
                "   ♥ \n" +
                "   ♥☺\n" +
                "   # \n" +
                "♥♥   \n" +
                " ♥   \n" +
                "\n");

        // пошла движуха
        hero(0).act();
        hero(1).act();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        hero(0).right();
        hero(1).left();
        tick();

        tick();

        asrtBrd("   ♥ \n" +
                " ♥ 1♥\n" +
                "   # \n" +
                "♥1 ☺ \n" +
                " ♥   \n", game(0));

        tick();

        assertBoards(
                "game(0)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉☺ \n" +
                " ♣   \n" +
                "\n" +
                "game(1)\n" +
                "   ♣ \n" +
                " ☺҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(2)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " Ѡ   \n" +
                "\n" +
                "game(3)\n" +
                "   ♣ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "Ѡ҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(4)\n" +
                "   Ѡ \n" +
                " ♥҉҉♣\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n" +
                "game(5)\n" +
                "   ♣ \n" +
                " ♥҉҉Ѡ\n" +
                " ҉ H \n" +
                "♣҉҉♥ \n" +
                " ♣   \n" +
                "\n");

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, KILL_OTHER_HERO]\n" +
                "listener(1) => [KILL_OTHER_HERO, KILL_OTHER_HERO, KILL_DESTROY_WALL]\n" +
                "listener(2) => [DIED]\n" +
                "listener(3) => [DIED]\n" +
                "listener(4) => [DIED]\n" +
                "listener(5) => [DIED]\n");

        // затем пройдет еще некоторое количество тиков, до общего числа = timePerRound
        tick();
        tick();
        tick();
        tick();

        asrtBrd("   ♣ \n" +
                " ♥  ♣\n" +
                "     \n" +
                "♣  ☺ \n" +
                " ♣   \n", game(0));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n");

        // вот он последний тик раунда, тут все и случится
        dice(heroDice,
                0, 1,  // на трупики нельзя!
                1, 0,  // на трупики нельзя!
                0, 2,  // теперь размещаем всех в свободные места
                1, 2,
                2, 2,
                3, 2,
                4, 2,
                4, 1);
        tick();
        newGameForAllDied(); // это сделает сервер (вообще он это сделал намного раньше, но для наглядности тут)

        asrtBrd("     \n" +
                "     \n" +
                "Ѡ♣♣♣♣\n" +
                "    ♣\n" +
                "     \n", game(0));

        events.verifyAllEvents(
                "listener(0) => [[Time is over]]\n" +
                "listener(1) => [WIN_ROUND]\n" +
                "listener(2) => []\n" +
                "listener(3) => []\n" +
                "listener(4) => []\n" +
                "listener(5) => []\n");
    }

    // останки другого бомбера не являются препятствием для прохождения любым бомбером
    // так же отрисовка живого и мертвого героя в одной клетке от имени трех типов героев
    // 1) тот которого вынесли видит свой трупик
    // 2) тот кто стоит в той же клетке видит себя
    // 3) сторонний наблюдатель видит живого соперника
    @Test
    public void shouldPlaceOfDeath_isNotABarrierForOtherHero() {
        givenCaseWhenPlaceOfDeathOnMyWay();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣♥  \n", game(0));

        // а вот и попытка пойти на место трупика
        hero(0).right();
        tick();

        // от имени того кто стоит на месте смерти другого героя он видет себя
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺♥  \n", game(0));

        // от имени того кого вынесли он видит свой трупик
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ♥  \n", game(1));

        // от имени стороннего наблюдателя - он видит живую угрозу
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ♥☺  \n", game(2));
    }

    private void givenCaseWhenPlaceOfDeathOnMyWay() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        timePerRound = 20;
        setup();

        dice(heroDice,
                0, 0, // первый игрок
                1, 0, // второй
                2, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♥♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥☺♥  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥♥☺  \n", game(2));

        // когда я выношу одного игрока
        hero(0).act();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "     \n" +
                "1♥♥  \n", game(0));

        tick();

        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "҉    \n" +
                "҉♣♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                "҉    \n" +
                "҉Ѡ♥  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                "҉    \n" +
                "҉♣☺  \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => []\n");

        hero(0).down();
        tick();

        hero(0).down();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣♥  \n", game(0));
    }

    // я не могу подрывать уже убитого бомбера
    // а в отрисовке, на месте трупика я не вижу взрывной волны, там всегда будет трупик
    @Test
    public void shouldCantDestroyHeroPlaceOfDeath() {
        givenCaseWhenPlaceOfDeathOnMyWay();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣♥  \n", game(0));

        hero(0).act();
        tick();

        hero(0).up();
        tick();

        hero(0).up();
        tick();

        tick();
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        // на месте героя которого вынесли я как сторонний наблюдатель
        // вижу его останки, а не взрывную волну
        asrtBrd("     \n" +
                "     \n" +
                "☺    \n" +
                "҉    \n" +
                "҉♣♥  \n", game(0));

        // я как тот которого вынесли, на месте взрыва вижу себя
        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                "҉    \n" +
                "҉Ѡ♥  \n", game(1));

        // на месте героя которого вынесли я как сторонний наблюдатель
        // вижу его останки, а не взрывную волну
        asrtBrd("     \n" +
                "     \n" +
                "♥    \n" +
                "҉    \n" +
                "҉♣☺  \n", game(2));
    }

    // люой герой может зайти на место трупика и там его можно прибить, так что
    // будет у нас двап трупика в одной клетке
    @Test
    public void shouldDestroySecondHero_whenItOnDeathPlace() {
        shouldPlaceOfDeath_isNotABarrierForOtherHero();

        // вижу себя в клетке где еще трупик
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ☺♥  \n", game(0));

        // вижу свой трупик, раз меня вынесли
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " Ѡ♥  \n", game(1));

        // вижу своего соперника в клетке, где трупик
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                " ♥☺  \n", game(2));

        // ставим бомбу и убегаем
        hero(2).act();
        tick();

        hero(2).right();
        tick();

        hero(2).up();
        tick();

        tick();
        tick();

        // что в результате

        // я вижу свой трупик в клетке, где есть еще один такой же
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "  ҉♥ \n" +
                " Ѡ҉҉ \n", game(0));

        // я вижу свой трупик в клетке, где есть еще один такой же
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "  ҉♥ \n" +
                " Ѡ҉҉ \n", game(1));

        // я вижу трупик одного из убитых там героев (их там двое)
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "  ҉☺ \n" +
                " ♣҉҉ \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [DIED]\n" +
                "listener(1) => []\n" +
                "listener(2) => [KILL_OTHER_HERO, WIN_ROUND]\n");
    }

    // проверяем, что при clearScore обнуляется:
    // - таймеры раунда
    // - очки заработанные в этом раунде
    // - и все игроки пересоздаются снова
    @Test
    public void shouldCleanEverything_whenCleanScores() {
        int count = 3;

        playersPerRoom.update(count);
        timeBeforeStart = 1;
        timePerRound = 60; // до конца раунда целая минута
        setup();

        dice(heroDice,
                4, 4, // первый игрок
                4, 3, // второй
                3, 4, // третий
                0, 0);  // для последующих начнем с левого нижнего угла

        givenBoard(count);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        asrtBrd("   ♥☺\n" +
                "    ♥\n" +
                "     \n" +
                "     \n" +
                "     \n", game(0));

        asrtBrd("   ♥♥\n" +
                "    ☺\n" +
                "     \n" +
                "     \n" +
                "     \n", game(1));

        asrtBrd("   ☺♥\n" +
                "    ♥\n" +
                "     \n" +
                "     \n" +
                "     \n", game(2));

        // бахнем бомбу
        hero(2).act();
        tick();

        hero(2).left();
        tick();

        hero(2).left();
        tick();

        tick();
        tick();

        events.verifyAllEvents(
                "listener(0) => [DIED]\n" +
                "listener(1) => []\n" +
                "listener(2) => [KILL_OTHER_HERO]\n");

        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(200, hero(2).scores()); // за победу

        assertEquals(true, hero(0).isActive());
        assertEquals(false, hero(0).isAlive()); // убит
        assertEquals(true, hero(1).isActiveAndAlive());
        assertEquals(true, hero(2).isActiveAndAlive());

        // делаем очистку очков
        field.clearScore();
        resetHeroes();

        // после этого тика будет сразу же новый раунд
        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        // и очки обнулятся
        assertEquals(0, hero(0).scores());
        assertEquals(0, hero(1).scores());
        assertEquals(0, hero(2).scores());

        // и все игроки активны
        assertEquals(true, hero(0).isActiveAndAlive());
        assertEquals(true, hero(1).isActiveAndAlive());
        assertEquals(true, hero(2).isActiveAndAlive());
    }

    // проверил как отрисуется митчопер если под ним будет трупик героя:
    // - от имени наблюдателя я там вижу опасность - митчопера, мне не интересны останки игроков
    // - от имени жертвы я вижу свой трупик, мне пофиг уже что на карте происходит, главное где поставить памятник герою
    @Test
    public void shouldDrawMeatChopper_onPlaceOfDeath() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        timePerRound = 20;
        setup();

        MeatChopper chopper = meatChopperAt(1, 1);

        dice(heroDice,
                0, 0, // первый игрок
                1, 0, // второй
                2, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        // ставлю бомбу
        hero(0).act();
        tick();

        // и тикать
        hero(0).up();
        tick();

        hero(0).up();
        tick();
        tick();

        // взрыв
        tick();

        // идем назад
        hero(0).down();
        tick();

        hero(0).down();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "☺♣♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "♥Ѡ♥  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "♥♣☺  \n", game(2));

        // попробуем митчопером сходить на место падшего героя
        chopper.move(DOWN.change(chopper));

        // от имени наблюдателя в клеточке с останками я вижу живого митчопера
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺&♥  \n", game(0));

        // от имени пострадавшего в клеточке я вижу свои останки, митчопер хоть и есть там, я его не вижу
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥Ѡ♥  \n", game(1));

        // от имени наблюдателя в клеточке с останками я вижу живого митчопера
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥&☺  \n", game(2));
    }

    // проверил как отрисуется митчопер если под ним будет не только трупик героя но и бобма:
    // - от имени наблюдателя я там вижу опасность - митчопера, мне не интересны останки игроков
    // - от имени жертвы я вижу свой трупик, мне пофиг уже что на карте происходит, главное где поставить памятник герою
    // но если митчопера нет, и бомба с останками, то подобно описанному выше:
    // - я от имени наблюдателя вижу тикающую бомбу
    // - а от имени пострадавшего - свои останки
    // приоритет прорисовки такой: 1) митчопер 2) бомба 3) останки
    @Test
    public void shouldDrawMeatChopper_onPlaceOfDeath_withBomb() {
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        timePerRound = 20;
        setup();

        MeatChopper chopper = meatChopperAt(1, 1);

        dice(heroDice,
                0, 0, // первый игрок
                1, 0, // второй
                2, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        // ставлю бомбу
        hero(0).act();
        tick();

        // и тикать
        hero(0).up();
        tick();

        hero(0).up();
        tick();
        tick();

        // взрыв
        tick();

        // идем назад
        hero(0).down();
        tick();

        hero(0).down();
        tick();

        hero(0).right();
        hero(0).act();
        tick();

        hero(0).left();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "☺3♥  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "♥Ѡ♥  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " &   \n" +
                "♥3☺  \n", game(2));

        // попробуем митчопером сходить на место падшего героя
        chopper.move(DOWN.change(chopper));

        // от имени наблюдателя в клеточке с останками я вижу живого митчопера, он по моему опаснее чем бомба
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺&♥  \n", game(0));

        // от имени пострадавшего в клеточке я вижу свои останки, митчопер хоть и есть там, я его не вижу
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥Ѡ♥  \n", game(1));

        // от имени наблюдателя в клеточке с останками я вижу живого митчопера, он по моему опаснее чем бомба
        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "♥&☺  \n", game(2));
    }

    // просто любопытно как рванут два бомбера, вместе с митчопером и трупом под бомбой
    @Test
    public void shouldDestroyMeatChopper_withOtherHeroes_onDeathPlace() {
        shouldDrawMeatChopper_onPlaceOfDeath_withBomb();

        resetListeners();

        tick();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "Ѡx♣  \n", game(0));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "♣Ѡ♣  \n", game(1));

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ҉   \n" +
                "♣xѠ  \n", game(2));

        // победителей нет
        events.verifyAllEvents(
                "listener(0) => [DIED, KILL_OTHER_HERO, KILL_MEAT_CHOPPER]\n" +
                "listener(1) => []\n" +
                "listener(2) => [DIED]\n");

        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");
    }

    // в этом тесте проверяется что взрывная волна не проходит через живого героя,
    // но его останки не являются препятствием
    @Test
    public void shouldPlaceOfDeath_isNotABarrierForBlast() {
        bombsPower = 3; // бомба с большим радиусом, чем обычно
        playersPerRoom.update(DEFAULT_COUNT);
        timeBeforeStart = 1;
        timePerRound = 60;
        timeForWinner = 15; // после победы я хочу еще чуть повисеть на уровне
        setup();

        dice(heroDice,
                0, 0, // первый игрок
                1, 0, // второй
                2, 0); // третий

        givenBoard(DEFAULT_COUNT);

        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");


        // выношу одного игрока мощным снарядом
        hero(0).act();
        tick();

        hero(0).up();
        tick();

        hero(0).right();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "1♥♥  \n", game(0));

        tick();

        // второй не погибает - его экранирует обычный герой
        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉♣♥  \n", game(0));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉Ѡ♥  \n", game(1));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉♣☺  \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => [DIED]\n" +
                "listener(2) => []\n");

        hero(0).left();
        tick();

        hero(0).down();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣♥  \n", game(0));

        // а теперь пробую то же, но через останки только что
        // поверженного соперника - они не должны мешать взрывной волне
        hero(0).act();
        tick();

        hero(0).up();
        tick();

        hero(0).right();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "1♣♥  \n", game(0));

        tick();

        // второй так же падет
        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉♣♣  \n", game(0));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉Ѡ♣  \n", game(1));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉♣Ѡ  \n", game(2));

        events.verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, WIN_ROUND]\n" +
                "listener(1) => []\n" +
                "listener(2) => [DIED]\n");

        // ну и напоследок вернемся на место
        hero(0).left();
        tick();

        hero(0).down();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                "     \n" +
                "☺♣♣  \n", game(0));

        // а теперь посмотрим как взорвется бомба на двух трупиках
        // они должны быть полностью прозрачна для взрывной волны
        hero(0).act();
        tick();

        hero(0).up();
        tick();

        hero(0).right();
        tick();
        tick();

        asrtBrd("     \n" +
                "     \n" +
                "     \n" +
                " ☺   \n" +
                "1♣♣  \n", game(0));

        tick();

        // второй так же падет
        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉☺   \n" +
                "҉♣♣҉ \n", game(0));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉Ѡ♣҉ \n", game(1));

        asrtBrd("     \n" +
                "҉    \n" +
                "҉    \n" +
                "҉♥   \n" +
                "҉♣Ѡ҉ \n", game(2));

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");
    }

    // в этом тесте я проверяю, что после победы героя на уровне
    // в случае, если timeForWinner > 1 то герой повисит некоторое время на поле сам
    // и в конечном счете начнется новый раунд
    @Test
    public void shouldWinScore_whenTimeoutBy_timeForWinner() {
        timePerRound = 60;
        timeForWinner = 15; // после победы я хочу еще чуть повисеть на уровне

        shouldPlaceOfDeath_isNotABarrierForBlast();

        // пройдет еще некоторое число тиков до общего числа timeForWinner
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                "listener(1) => []\n" +
                "listener(2) => []\n");

        // и начнется новый раунд
        tick();

        events.verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 2]]\n" +
                "listener(1) => [START_ROUND, [Round 2]]\n" +
                "listener(2) => [START_ROUND, [Round 2]]\n");

        // а дальше все как обычно
        tick();

        events.verifyAllEvents(
                "listener(0) => []\n" +
                        "listener(1) => []\n" +
                        "listener(2) => []\n");
    }
}
