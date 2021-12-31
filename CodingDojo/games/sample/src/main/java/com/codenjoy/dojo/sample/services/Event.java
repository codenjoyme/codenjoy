package com.codenjoy.dojo.sample.services;

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
 * Ивенты, которые могут возникать в игре.
 *
 * Что есть ивенты? Допустим убили твоего героя
 * и есть острая необходимость начислить ему штрафных очков.
 * Быть может, наоборот - герой поднял что-то
 * ценное и стоит вручить йгерою бонус. Вот тут нужны ивенты.
 *
 * Ивенты уйдут за предеты игры в сторону фреймворка,
 * а затем вернутся в {@link Scores#event(Object)}
 * чтобы произвести подсчет очков.
 */
public enum Event {

    START_ROUND,
    WIN_ROUND,

    KILL_OTHER_HERO,
    KILL_ENEMY_HERO,
    HERO_DIED,

    GET_GOLD,
}
