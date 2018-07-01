package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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
 * Определяет тип многопользовательский игры.
 * Этот функционал реализует сервис многопользовательской игры {@see MultiplayerService}
 */
public enum MultiplayerType {
    /**
     * Каждый игрок на своей борде. В случае конца игры он получит новую борду.
     */
    SINGLE,

    /**
     * Все игроки на одном поле без ограничений.
     * Если один из игроков закончил (проиграл или выиграл) он тут же регенерируется
     * на этом же поле в рендомном месте.
     */
    MULTIPLE,

    /**
     * По двое игроков за раз на одной борде.
     * Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появится
     * игрок, с которым он еще не играл.
     * Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    TOURNAMENT,

    /**
     * Трое игроков за раз на одной борде.
     * Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появятся
     * двое других игроков, с которыми он еще не играл.
     * Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    TRIPLE,

    /**
     * Четверо игроков за раз на одной борде.
     * Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появятся
     * трое других игроков, с которыми он еще не играл.
     * Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    QUADRO,
}
