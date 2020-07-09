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


import java.util.function.Function;

public interface PlayerTransport {

    /**
     * Send game state to the all players
     * @param state state
     */
    void sendStateToAll(Object state);

    /**
     * Send game state to the player by given player id.
     * @param id registered player id
     * @param state state
     */
    boolean sendState(String id, Object state);

    /**
     * Случается, когда игрок зарегистрировался в игре на страничке регистрации
     * Only one endpoint per player is allowed
     * @param id идентификатор пользователя
     * @param responseHandler обработчик
     */
    void registerPlayerEndpoint(String id, ResponseHandler responseHandler);

    /**
     * Случается, когда игрока удалили на админке
     * @param id идентификатор пользователя
     */
    void unregisterPlayerEndpoint(String id);

    /**
     * Случается, когда игрок подключился по вебсокетам к серверу
     * @param id идентификатор пользователя
     * @param playerSocket вебсокет
     */
    void registerPlayerSocket(String id, PlayerSocket playerSocket);

    /**
     * Случается, когда игрок отключился вебсокет-клиентом от сервера
     * @param socket
     */
    void unregisterPlayerSocket(PlayerSocket socket);

    void setFilterFor(PlayerSocket socket, Function<Object, Object> filter);

    void setDefaultFilter(Function<Object, Object> filter);
}
