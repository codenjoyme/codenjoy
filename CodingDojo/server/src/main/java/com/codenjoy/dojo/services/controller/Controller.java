package com.codenjoy.dojo.services.controller;

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


import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Player;

import java.io.IOException;

public interface Controller<TData, TControl> {

    /**
     * С помощью этого метода PlayerServiceImpl отправляет через ws
     * всем клиентам информацию об игре
     * @param data Данные к отправке
     * @result count requested
     */
    default int requestControlToAll(TData data) {
        throw new UnsupportedOperationException();
    }

    /**
     * С помощью этого метода PlayerServiceImpl отправляет через ws
     * клиентам информацию об игре
     * @param player Плеер, которому отправляется ответ
     * @param data Данные к отправке
     * @result count requested
     */
    default int requestControl(Player player, TData data) {
        throw new UnsupportedOperationException();
    }

    /**
     * В момент регистрации пользователя для него создается канал связи
     * к которому потом можно будет подключиться по ws и управлять ввереным
     * ему контролом.
     * @param deal Игра которая начинается.
     */
    void register(Deal deal);

    /**
     * В случае, если пользователь не хочет больше играть, то и канал связи
     * закрывается.
     * @param deal Игра которая завершилась.
     */
    void unregister(Deal deal);
}
