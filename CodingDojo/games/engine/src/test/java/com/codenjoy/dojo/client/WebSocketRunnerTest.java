package com.codenjoy.dojo.client;

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


import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WebSocketRunnerTest {

    @Test
    public void testUrlParser() {
        assertURL("UrlParser{server='codenjoy.com:80', context='codenjoy-contest', code='12345678901234567890', userName='3edq63tw0bq4w4iem7nb'}",
                "http://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890");

        assertURL("UrlParser{server='codenjoy.com:80', context='codenjoy-contest', code='12345678901234567890', userName='3edq63tw0bq4w4iem7nb'}",
                "https://codenjoy.com:80/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890");

        assertURL("UrlParser{server='127.0.0.1:8080', context='codenjoy-contest', code='12345678901234567890', userName='3edq63tw0bq4w4iem7nb'}",
                "http://127.0.0.1:8080/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890");

        assertURL("UrlParser{server='192.168.0.1', context='codenjoy-contest', code='12345678901234567890', userName='3edq63tw0bq4w4iem7nb'}",
                "http://192.168.0.1/codenjoy-contest/board/player/3edq63tw0bq4w4iem7nb?code=12345678901234567890");

        assertURL("UrlParser{server='otherDomain', context='other-context', code='otherCode', userName='otherEmail'}",
                "http://otherDomain/other-context/board/player/otherEmail?code=otherCode");

        String message = "Bad web socket server url, expected: http://server:port/codenjoy-contest/board/player/playerid?code=12345678901234567890";
        assertURL(message,
                "http://otherDomain/other-context/BUG/player/otherEmail?code=otherCode");

        assertURL(message,
                "http://otherDomain/other-context/board/BUG/otherEmail?code=otherCode");

        assertURL(message,
                "http://otherDomain/other-context/board/player/otherEmail?BUG=otherCode");

        assertURL(message,
                "http://otherDomain/other-context/board/otherEmail?code=otherCode");

        assertURL(message,
                "http://otherDomain/other-context/board/player/BUG/otherEmail?code=otherCode");
    }

    private void assertURL(String expected, String uri) {
        try {
            assertEquals(expected, new UrlParser(uri).toString());
        } catch (RuntimeException e) {
            assertEquals(expected, e.getMessage());
        }
    }
}
