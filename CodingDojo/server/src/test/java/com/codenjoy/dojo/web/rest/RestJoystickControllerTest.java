package com.codenjoy.dojo.web.rest;

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

import com.codenjoy.dojo.client.local.DiceGenerator;
import com.codenjoy.dojo.config.RealGameConfiguration;
import com.codenjoy.dojo.services.GameService;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;

import static com.codenjoy.dojo.sample.services.GameSettings.Keys.LOSE_PENALTY;
import static com.codenjoy.dojo.sample.services.GameSettings.Keys.WIN_SCORE;
import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

@Import(RealGameConfiguration.class)
public class RestJoystickControllerTest extends AbstractRestControllerTest {

    public static final String FOR_PLAYER = "/rest/joystick/player/%s/code/%s/do/%s";
    public static final String FOR_ADMIN = "/rest/joystick/player/%s/do/%s";

    private final String game = "sample";
    private final String room = "room";

    private final String player1 = "player1";
    private String code1;

    private final String player2 = "player2";
    private String code2;

    @SpyBean
    private GameService games;

    @Override
    public void setup() {
        super.setup();

        // given
        // mock dice for game in room
        with.rooms.mockDice(game, new DiceGenerator().getDice());

        // game type will be a multiple
        with.rooms.levelsSettings(room, game)
                .setLevelMaps(1,
                        "☼☼☼☼☼☼\n" +
                        "☼    ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼ $$ ☼\n" +
                        "☼    ☼\n" +
                        "☼☼☼☼☼☼\n")
                .integer(WIN_SCORE, 30)
                .integer(LOSE_PENALTY, 100);


        with.login.register(player1, "ip", room, game);
        code1 = with.login.code(player1);
        with.login.asUser(player1, player1);
       
        with.login.register(player2, "ip", room, game);
        code2 = with.login.code(player2);
        with.login.asUser(player2, player2);

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ $$ ☼\n" +
                "☼ $$ ☼\n" +
                "☼☻ ☺ ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCanUseJoystick_withCode_asPlayer_successful() {
        // given
        with.login.asUser(player1, player1);

        // when
        joystick("",
                player2, code2, "act,up");

        joystick("",
                player1, code1, "up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼$$$ ☼\n" +
                "☼☻$☺ ☼\n" +
                "☼x   ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCantUseJoystick_withCode_asPlayer_errorWhenBadCode() {
        // given
        with.login.asUser(player1, player1);

        // when
        joystickError("java.lang.IllegalArgumentException: Player code is invalid: 'bad code'",
                player2, "bad code", "act,up");

        joystickError("java.lang.IllegalArgumentException: Player code is invalid: 'bad code'",
                player1, "bad code", "up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ $$ ☼\n" +
                "☼ $$ ☼\n" +
                "☼☻ ☺ ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCanUseJoystick_withCode_asAdmin_successful() {
        // given
        with.login.asAdmin();

        // when
        joystick("",
                player2, code2, "act,up");

        joystick("",
                player1, code1, "up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼$$$ ☼\n" +
                "☼☻$☺ ☼\n" +
                "☼x   ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCantUseJoystick_withoutCode_asPlayer_errorNotAccessible() {
        // given
        with.login.asUser(player1, player1);

        // when
        adminJoystickError("org.springframework.security.access.AccessDeniedException: Access is denied",
                player2, "act,up");

        adminJoystickError("org.springframework.security.access.AccessDeniedException: Access is denied",
                player1, "up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ $$ ☼\n" +
                "☼ $$ ☼\n" +
                "☼☻ ☺ ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCantUseJoystick_withoutCode_errorBadPlayerId() {
        // given
        with.login.asAdmin();

        // when
        adminJoystickError("java.lang.IllegalArgumentException: Player id is invalid: 'bad player'",
                "bad player", "act,up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼ $$ ☼\n" +
                "☼ $$ ☼\n" +
                "☼☻ ☺ ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    @Test
    public void shouldCanUseJoystick_withoutCode_asAdmin_successful() {
        // given
        with.login.asAdmin();

        // when
        adminJoystick("",
                player2, "act,up");

        adminJoystick("",
                player1, "up");

        players.tick();

        // then
        assertEquals(
                "☼☼☼☼☼☼\n" +
                "☼    ☼\n" +
                "☼$$$ ☼\n" +
                "☼☻$☺ ☼\n" +
                "☼x   ☼\n" +
                "☼☼☼☼☼☼\n",
                with.rooms.board(player1));
    }

    private void joystick(String expected, String player, String code, String command) {
        assertEquals(expected,
                get(String.format(FOR_PLAYER,
                        player, code, command)));
    }

    private void joystickError(String expected, String player, String code, String command) {
        assertGetError(expected,
                String.format(FOR_PLAYER,
                        player, code, command));
    }

    private void adminJoystick(String expected, String player, String command) {
        assertEquals(expected,
                get(String.format(FOR_ADMIN,
                        player, command)));
    }

    private void adminJoystickError(String expected, String player, String command) {
        assertGetError(expected,
                String.format(FOR_ADMIN,
                        player, command));
    }
}