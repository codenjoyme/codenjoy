package com.codenjoy.dojo.transport.ws;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import java.io.IOException;

public interface PlayerTransport<TEndpointSettings, TResponseContext> {
    /**
     * Send game state to the player by given player id.
     * @param id registered player id
     * @param state GameState instance
     * @throws IOException
     */
    void sendState(String id, GameState state) throws IOException;

    /**
     * Случается, когда игрок зарегистрировался в игре на страничке регистрации
     * Only one endpoint per player is allowed
     * @param id идентификатор пользователя - его email
     * @param responseHandler обработчик
     * @param endpointSettings дополнительные данные
     */
    void registerPlayerEndpoint(String id, PlayerResponseHandler<TResponseContext> responseHandler, TEndpointSettings endpointSettings);

    void unregisterPlayerEndpoint(String id);

    /**
     * Случается, когда игрок подключился по вебсокетам к серверу
     * @param id идентификатор пользователя - его email
     * @param playerSocket вебсокет
     */
    void registerPlayerSocket(String id, PlayerSocket playerSocket);

    void unregisterPlayerSocket(String id);
}
