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
 * Принтер доски может прежставить любой ее элемент как {@see Elements}
 */
public interface GamePrinter {

    /**
     * Вызывается перед прогоном по всему полю. Сделано для оптимизации и уменьшеня рассчетов в методе
     * @see GamePrinter#printAll(Filler)
     */
    void init();

    /**
     * Этим методом мы даем клиенту возможность отрисоваться на доске
     * @param filler любой, кто хочет дать возможность писать в себя должен реализовать этот интерфейс
     */
    void printAll(Filler filler);

    interface Filler {
        void set(int x, int y, char ch);
    }
}
