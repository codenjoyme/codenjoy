package com.codenjoy.dojo.services;

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


/**
 * Когда бот игрока управляет ботом, он соединяется по вебсокетом с фреймворком, а тот в свою
 * очередь дергает соответсвующие методы этого интерфейса. Фактически это джойтик игрока - так
 * ты даешь пользователю возможность управлять его героем в твоей игре.
 */
public interface Joystick {

    void down();

    void up();

    void left();

    void right();

    /**
     * Зависит от твоей игры - обычно это действие: поставить бомбу, выстрелить, просверлить ямку и т.д.
     */
    void act(int... p);

    /**
     * Еще один параметр введенный для текстовых (не графических) игор
     * @param command любая строка в формате "message('текстовое сообщение')"
     */
    void message(String command);
}
