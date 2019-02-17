package com.codenjoy.dojo.services.multiplayer;

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


import com.codenjoy.dojo.services.printer.BoardReader;
import com.codenjoy.dojo.services.Tickable;
import org.json.JSONObject;

/**
 * Любая игра должна реализовать этот интерфейс чтобы с ней мог работать фреймворк
 * @param <P> Объъект-игрок, который предоставляет героя в игре
 */
public interface GameField<P extends GamePlayer> extends Tickable {

    BoardReader reader();

    void newGame(P player);

    void remove(P player);

    default void clearScore(){
        // do nothing
    }

    default JSONObject getSave(){
        return null;
    }

    default void loadSave(JSONObject save) {
        // do nothing
    }

    /**
     * Никогда не переопределяй этот метод
     */
    boolean equals(Object o);

    /**
     * Никогда не переопределяй этот метод
     */
    int hashCode();
}
