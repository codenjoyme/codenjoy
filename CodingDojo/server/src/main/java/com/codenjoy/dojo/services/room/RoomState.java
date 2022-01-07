package com.codenjoy.dojo.services.room;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.GameType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class RoomState {

    /**
     * Имя/id комнаты, ее alias.
     */
    private String room;

    /**
     * Тип игры в комнате. Важно понимать, что у каждой комнаты свой клон GameType
     * Settings которого изменяются независимо от остальных.
     */
    private GameType type;

    /**
     * Говорит, находится ли комната на паузе. Будучи на паузе комната не тикается,
     * а все участники не получают обновления борды.
     */
    private boolean active;

    /**
     * Открыта ли комната для регистрации. Если регистрация в комнате закрыта -
     * ни один новый участник не может заджойниться в нее.
     */
    private boolean opened;

    /**
     * Номер тика, используется в SemifinalSettings для подсчета времени оставшегося для
     * очередного полуфинала.
     */
    private int tick;

    public void resetTick() {
        tick = 0;
    }

    public void tick() {
        tick++;
    }

    public String getGame() {
        return type.name();
    }
}
