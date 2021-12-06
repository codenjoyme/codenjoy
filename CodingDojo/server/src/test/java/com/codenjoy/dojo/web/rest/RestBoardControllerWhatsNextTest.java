package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2021 Codenjoy
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
import org.junit.Test;
import org.springframework.context.annotation.Import;

import static com.codenjoy.dojo.utils.smart.SmartAssert.assertEquals;

@Import(RealGameConfiguration.class)
public class RestBoardControllerWhatsNextTest extends AbstractRestControllerTest {

    private final String game = "sample";
    private final String room = "room";
    private final String player = "player";

    @Override
    public void setup() {
        super.setup();

        // given
        // mock dice for game in room
        with.rooms.mockDice(game, new DiceGenerator().getDice());

        // just create room, whatsNext feature will use it
        with.login.register(player, "ip", room, game);
        with.login.asUser(player, player);
    }

    @Test
    public void shouldWhatsNext_success() {
        // when then
        assertEquals("+-----------------\n" +
                        "|      setup       \n" +
                        "+-----------------\n" +
                        "|                 \n" +
                        "| (1) Board:      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) ☼    ☼      \n" +
                        "| (1) ☼ $$ ☼      \n" +
                        "| (1) ☼ $$ ☼      \n" +
                        "| (1) ☼ ☺☻ ☼      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) Events:[]   \n" +
                        "|                 \n" +
                        "| (2) Board:      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) ☼    ☼      \n" +
                        "| (2) ☼ $$ ☼      \n" +
                        "| (2) ☼ $$ ☼      \n" +
                        "| (2) ☼ ☻☺ ☼      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) Events:[]   \n" +
                        "|                 \n" +
                        "+-----------------\n" +
                        "|      tick 1      \n" +
                        "+-----------------\n" +
                        "|                 \n" +
                        "| (1) Board:      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) ☼    ☼      \n" +
                        "| (1) ☼ $$ ☼      \n" +
                        "| (1) ☼ ☺$ ☼      \n" +
                        "| (1) ☼$x ☻☼      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) Events:[WIN => +30]\n" +
                        "|                 \n" +
                        "| (2) Board:      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) ☼    ☼      \n" +
                        "| (2) ☼ $$ ☼      \n" +
                        "| (2) ☼ ☻$ ☼      \n" +
                        "| (2) ☼$x ☺☼      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) Events:[]   \n" +
                        "|                 \n" +
                        "+-----------------\n" +
                        "|      tick 2      \n" +
                        "+-----------------\n" +
                        "|                 \n" +
                        "| (1) Board:      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) ☼    ☼      \n" +
                        "| (1) ☼$☺$ ☼      \n" +
                        "| (1) ☼  $☻☼      \n" +
                        "| (1) ☼$x  ☼      \n" +
                        "| (1) ☼☼☼☼☼☼      \n" +
                        "| (1) Events:[WIN => +30]\n" +
                        "|                 \n" +
                        "| (2) Board:      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) ☼    ☼      \n" +
                        "| (2) ☼$☻$ ☼      \n" +
                        "| (2) ☼  $☺☼      \n" +
                        "| (2) ☼$x  ☼      \n" +
                        "| (2) ☼☼☼☼☼☼      \n" +
                        "| (2) Events:[]   \n" +
                        "|                 \n" +
                        "+-----------------\n",

                get(200, "/rest/room/" + room + "/whats-next",
                        unquote(validRequest())));
    }

    @Test
    public void shouldWhatsNext_failure_notExistsRoom() {
        String notExists = "notExists";

        assertGetError("java.lang.IllegalArgumentException: " +
                        "Room not exists: notExists",
                "/rest/room/" + notExists + "/whats-next",
                        unquote(validRequest()));
    }

    @Test
    public void shouldWhatsNext_failure_noBoard() {
        String notExists = "notExists";

        assertGetError("java.lang.IllegalArgumentException: " +
                        "Object 'board' is null",
                "/rest/room/" + notExists + "/whats-next",
                unquote("{actions:'" +
                        "(2)->[RIGHT]&(1)->[ACT, UP];" +
                        "(1)->[UP]&(2)->[UP]" +
                        "'}"));
    }

    @Test
    public void shouldWhatsNext_failure_noActions() {
        String notExists = "notExists";

        assertGetError("java.lang.IllegalArgumentException: " +
                        "Object 'actions' is null",
                "/rest/room/" + notExists + "/whats-next",
                unquote("{board:'" +
                        "☼☼☼☼☼☼" +
                        "☼    ☼" +
                        "☼ $$ ☼" +
                        "☼ $$ ☼" +
                        "☼ ☺☺ ☼" +
                        "☼☼☼☼☼☼" +
                        "'}"));
    }

    private String validRequest() {
        return "{board:'" +
                "☼☼☼☼☼☼" +
                "☼    ☼" +
                "☼ $$ ☼" +
                "☼ $$ ☼" +
                "☼ ☺☺ ☼" +
                "☼☼☼☼☼☼" +
                "', actions:'" +
                "(2)->[RIGHT]&(1)->[ACT, UP];" +
                "(1)->[UP]&(2)->[UP]" +
                "'}";
    }
}