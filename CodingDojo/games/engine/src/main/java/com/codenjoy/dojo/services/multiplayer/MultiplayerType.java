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


import com.codenjoy.dojo.services.Game;
import org.json.JSONObject;

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

    public boolean isLastLevel(int levelNumber) {
        return levelNumber == levelsCount;
    }

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

    /**
     * Игроки играют каждый на своем уровне заданное количество уровней,
     * затем они все переходят на одну и ту же карту (multiple) и она зацикливается.
     */
    public static final Function<Integer, MultiplayerType> TRAINING = TRAINING_::new;
    static class TRAINING_ extends MultiplayerType {
        TRAINING_(Integer levels) {
            super(1, levels);
        }

        @Override
        public int getRoomSize(Object data) {
            if (data == null) {
                return super.roomSize;
            }
            LevelProgress progress = (LevelProgress)data;
            if (progress.getCurrent() < progress.getTotal()) {
                return SINGLE.getRoomSize();
            } else {
                return MULTIPLE.getRoomSize();
            }
        }
    }

    private int roomSize;
    private int levelsCount;

    MultiplayerType(int roomSize) {
        this(roomSize, 1);
    }

    public MultiplayerType(int roomSize, int levelsCount) {
        this.roomSize = roomSize;
        this.levelsCount = levelsCount;
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

    public boolean isTraining() {
        return this instanceof TRAINING_;
    }

    public int loadProgress(Game game, JSONObject save) {
        int roomSize;
        if (isTraining() && save.has("levelProgress")) {
            LevelProgress progress = new LevelProgress(save);
            roomSize = this.getRoomSize(progress);
            game.setProgress(progress);
        } else {
            roomSize = this.getRoomSize();
            game.setProgress(new LevelProgress(this));
        }
        return roomSize;
    }

    public int getRoomSize(Object data) {
        return roomSize;
    }

    public int getRoomSize() {
        return getRoomSize(null);
    }

    public int getLevelsCount() {
        return levelsCount;
    }
}
