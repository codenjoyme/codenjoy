package com.codenjoy.dojo.sample.model;

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

import com.codenjoy.dojo.sample.services.GameSettings;
import org.junit.Test;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.*;
import static com.codenjoy.dojo.services.round.RoundSettings.Keys.*;

public class RoundBattleTest extends AbstractGameTest {

    @Override
    protected GameSettings setupSettings() {
        return super.setupSettings()
                .bool(ROUNDS_ENABLED, true)
                .integer(ROUNDS_TIME_BEFORE_START, 5)
                .integer(ROUNDS_PER_MATCH, 3)
                .integer(ROUNDS_MIN_TICKS_FOR_WIN, 1)
                .integer(ROUNDS_TIME, 10)
                .integer(ROUNDS_TIME_FOR_WINNER, 2);
    }

    // во время старта игры, когда не прошло timeBeforeStart тиков,
    // все игроки неактивны (видно их трупики)
    @Test
    public void shouldAllPlayersOnBoardIsInactive_whenStart() {
        // given
        settings().integer(ROUNDS_PLAYERS_PER_ROOM, 3);

        givenFl("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☺☺  ☼\n" +
                "☼☼☼☼☼☼\n");

        // when then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ X  ☼\n" +
                "☼YY  ☼\n" +
                "☼☼☼☼☼☼\n");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼XY  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼YX  ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertScores("");
    }

    // после старта идет отсчет обратного времени
    @Test
    public void shouldCountdownBeforeRound_whenTicksOnStart() {
        // given
        shouldAllPlayersOnBoardIsInactive_whenStart();

        verifyAllEvents("");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [[....4....]]\n" +
                "listener(1) => [[....4....]]\n" +
                "listener(2) => [[....4....]]\n");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [[...3...]]\n" +
                "listener(1) => [[...3...]]\n" +
                "listener(2) => [[...3...]]\n");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [[..2..]]\n" +
                "listener(1) => [[..2..]]\n" +
                "listener(2) => [[..2..]]\n");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [[.1.]]\n" +
                "listener(1) => [[.1.]]\n" +
                "listener(2) => [[.1.]]\n");

        assertScores("");
    }

    // пока идет обратный отсчет я не могу ничего предпринимать,
    // а герои отображаются на карте как трупики
    // но после объявления раунда я могу начать играть
    @Test
    public void shouldActiveAndCanMove_afterCountdown() {
        // given
        shouldCountdownBeforeRound_whenTicksOnStart();

        // пока еще не активны
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ X  ☼\n" +
                "☼YY  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼XY  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ Y  ☼\n" +
                "☼YX  ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        // when
        // и я не могу ничего поделать с ними
        hero(0).up();
        hero(1).right();
        hero(2).up();

        tick();

        // then
        // после сообщения что раунд начался
        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        assertScores("");

        // можно играть - игроки видны как активные
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☻☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☺☻  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☻☺  ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        // when
        // ... и когда я муваю героев, они откликаются
        hero(0).up();
        hero(1).up();
        hero(2).right();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☻   ☼\n" +
                "☼  ☻ ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☺   ☼\n" +
                "☼  ☻ ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☻   ☼\n" +
                "☼  ☺ ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertScores("");
    }

    // если один игрок вынесет другого, но на поле есть еще игроки,
    // то тот, которого вынесли появится в новом месте в виде трупика
    @Test
    public void shouldMoveToInactive_whenKillSomeone() {
        // given
        settings().integer(ROUNDS_PLAYERS_PER_ROOM, 3)
                .integer(ROUNDS_TIME_BEFORE_START, 1); // TODO а что будет если тут 0 игра хоть начнется?

        givenFl("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☺☺  ☼\n" +
                "☼☼☼☼☼☼\n");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☻☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☺☻  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☻☺  ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        assertScores("");

        // when
        // когда я выношу одного игрока
        hero(0).bomb();
        tick();

        hero(0).right();
        tick();

        hero(0).up();
        tick();

        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼  ☺ ☼\n" +
                "☼ x  ☼\n" +
                "☼☻☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        // игрок активный и живой
        assertEquals(true, hero(2).isActive());
        assertEquals(true, hero(2).isAlive());
        assertEquals(true, player(2).wantToStay());
        assertEquals(false, player(2).shouldLeave());

        assertScores("");

        // when
        hero(2).up();
        tick();

        // then
        // игрок активный, но неживой (сервер ему сделает newGame)
        assertEquals(true, hero(2).isActive());
        assertEquals(false, hero(2).isAlive());
        // тут без изменений
        assertEquals(true, player(2).wantToStay());
        assertEquals(false, player(2).shouldLeave());

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼  ☺ ☼\n" +
                "☼ Y  ☼\n" +
                "☼☻   ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(2) => [HERO_DIED]\n");

        assertScores("hero(0)=5");

        // when
        tick();

        // новые координаты для героя
        dice(4, 4);
        // это сделает сервер в ответ на isAlive = false
        game(2).newGame();

        // then
        // игрок уже живой, но неактивный до начала следующего раунда
        assertEquals(false, hero(2).isActive());
        assertEquals(true, hero(2).isAlive());
        // тут без изменений
        assertEquals(true, player(2).wantToStay());
        assertEquals(false, player(2).shouldLeave());

        assertF("☼☼☼☼☼☼\n" +
                "☼   Y☼\n" +
                "☼  ☺ ☼\n" +
                "☼    ☼\n" +
                "☼☻   ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertScores("hero(0)=5");
    }

    // если один игрок вынесет обоих, то должен получить за это очки
    @Test
    public void shouldGetWinRoundScores_whenKillAllOtherHeroes() {
        // given
        settings().integer(ROUNDS_PLAYERS_PER_ROOM, 3)
                .integer(ROUNDS_TIME_BEFORE_START, 1)
                .integer(GET_GOLD_SCORE, 10)
                .integer(HERO_DIED_PENALTY, -50)
                .integer(WIN_ROUND_SCORE, 100);

        givenFl("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☺$$ ☼\n" +
                "☼☺☺  ☼\n" +
                "☼☼☼☼☼☼\n");

        assertScores("");

        hero(0).addScore(100);
        hero(1).addScore(100);
        hero(2).addScore(100);

        assertScores(
                "hero(0)=100\n" +
                "hero(1)=100\n" +
                "hero(2)=100");

        // when
        tick();

        // then
        verifyAllEvents(
                "listener(0) => [START_ROUND, [Round 1]]\n" +
                "listener(1) => [START_ROUND, [Round 1]]\n" +
                "listener(2) => [START_ROUND, [Round 1]]\n");

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☺$$ ☼\n" +
                "☼☻☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☻$$ ☼\n" +
                "☼☺☻  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼☻$$ ☼\n" +
                "☼☻☺  ☼\n" +
                "☼☼☼☼☼☼\n", 2);


        // cleaned after new round
        assertScores("");

        // but we want to change heroes scores
        hero(0).addScore(100);
        hero(1).addScore(100);
        hero(2).addScore(100);

        assertScores(
                "hero(0)=100\n" +
                "hero(1)=100\n" +
                "hero(2)=100");

        // when
        // расставляю бомбы и подбираю золото
        hero(0).bomb();
        hero(0).right();
        tick();

        verifyAllEvents(
                "listener(0) => [GET_GOLD]\n");

        assertScores(
                "hero(0)=110\n" +
                "hero(1)=100\n" +
                "hero(2)=100");

        // when
        // расставляю бомбы и подбираю золото
        hero(0).bomb();
        hero(0).right();
        tick();

        verifyAllEvents(
                "listener(0) => [GET_GOLD]\n");

        assertScores(
                "hero(0)=120\n" +
                "hero(1)=100\n" +
                "hero(2)=100");

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼xx☺ ☼\n" +
                "☼☻☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        // when
        hero(1).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼Yx☺ ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼Xx☻ ☼\n" +
                "☼ ☻  ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼Yx☻ ☼\n" +
                "☼ ☺  ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO]\n" +
                "listener(1) => [HERO_DIED]\n");

        assertScores(
                "hero(0)=125\n" +
                "hero(1)=50\n" +
                "hero(2)=100");

        // when
        hero(2).up();
        tick();

        // then
        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼YY☺ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 0);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼XY☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 1);

        assertF("☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼    ☼\n" +
                "☼YX☻ ☼\n" +
                "☼    ☼\n" +
                "☼☼☼☼☼☼\n", 2);

        verifyAllEvents(
                "listener(0) => [KILL_OTHER_HERO, WIN_ROUND]\n" +
                "listener(2) => [HERO_DIED]\n");

        assertScores(
                "hero(0)=230\n" +
                "hero(1)=50\n" +
                "hero(2)=50");
    }
}