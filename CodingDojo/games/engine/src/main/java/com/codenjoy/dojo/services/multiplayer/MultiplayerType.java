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


import java.util.function.Function;

/**
 * Определяет тип многопользовательский игры.
 * Этот функционал реализует сервис многопользовательской игры {@see MultiplayerService}
 */
public class MultiplayerType {

    /**
     * Каждый игрок на своей борде.
     * В случае окончания и стартановой игры он получит новую борду.
     */
    public static final MultiplayerType SINGLE = new SINGLE();
    static class SINGLE extends MultiplayerType {
        SINGLE() {
            super(1);
        }
    }

    /**
     * Все игроки на одном поле без ограничений.
     * Если один из игроков закончил (проиграл или выиграл) он тут же регенерируется
     * на этом же поле в рендомном месте.
     */
    public static final MultiplayerType MULTIPLE = new MULTIPLE();
    static class MULTIPLE extends MultiplayerType {
        MULTIPLE() {
            super(Integer.MAX_VALUE);
        }
    }

    /**
     * Все игроки в заданном количестве на одном поле без ограничений.
     * Если один из игроков закончил (проиграл или выиграл) он тут же регенерируется
     * TODO на новом поле в рендомном месте.
     * Новый игрок сверх количества команды не регистрируется в новой игре.
     */
    public static final Function<Integer, MultiplayerType> TEAM = TEAM_::new;
    static class TEAM_ extends MultiplayerType {
        TEAM_(Integer count) {
            super(count);
        }
    }

    /**
     * По двое игроков за раз на одной борде.
     * TODO Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появится
     * игрок, с которым он еще не играл.
     * TODO Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    public static final MultiplayerType TOURNAMENT = new TOURNAMENT();
    static class TOURNAMENT extends MultiplayerType {
        TOURNAMENT() {
            super(2);
        }
    }

    /**
     * Трое игроков за раз на одной борде.
     * TODO Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появятся
     * двое других игроков, с которыми он еще не играл.
     * TODO Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    public static final MultiplayerType TRIPLE = new TRIPLE();
    static class TRIPLE extends MultiplayerType {
        TRIPLE() {
            super(3);
        }
    }

    /**
     * Четверо игроков за раз на одной борде.
     * TODO Если один из игроков закончил, он попадает в лобби и ждет пока в нем не появятся
     * трое других игроков, с которыми он еще не играл.
     * TODO Игра находится на паузе, пока не соберется заданное количество игроков.
     */
    public static MultiplayerType QUADRO = new QUADRO();
    static class QUADRO extends MultiplayerType {
        QUADRO() {
            super(4);
        }
    }

    private int count;

    MultiplayerType(int count) {
        this.count = count;
    }

    public boolean isSingle() {
        return this instanceof SINGLE;
    }

    public boolean isMultiple() {
        return this instanceof MULTIPLE;
    }

    public boolean isTriple() {
        return this instanceof TRIPLE;
    }

    public boolean isTournament() {
        return this instanceof TOURNAMENT;
    }

    public boolean isQuadro() {
        return this instanceof QUADRO;
    }

    public boolean isTeam() {
        return this instanceof TEAM_;
    }

    public boolean isSingleplayer() {
        return isSingle();
    }

    public boolean isMultiplayer() {
        return !isSingle();
    }

    public int getCount() {
        return count;
    }
}
