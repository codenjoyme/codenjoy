package com.codenjoy.dojo.transport.ws;

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


import org.eclipse.jetty.websocket.api.Session;

public class NullResponseHandler implements ResponseHandler {

    public static ResponseHandler NULL = new NullResponseHandler();

    @Override
    public void onResponse(PlayerSocket socket, String message) {
        // do nothing
    }

    @Override
    public void onClose(PlayerSocket socket, int statusCode, String reason) {
        // do nothing
    }

    @Override
    public void onError(PlayerSocket socket, Throwable error) {
        // do nothing
    }

    @Override
    public void onConnect(PlayerSocket socket, Session session) {
        // do nothing
    }
}
