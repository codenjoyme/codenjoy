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


import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import lombok.Getter;

import java.util.function.Consumer;

/**
 * Класс представляет собой игру пользователя, объединяя данные о пользователе в Player,
 * Игру на которой он играет - Game. Комнату в которой эта игра происходит - playerRoom. 
 * А так же джойстик которым он играет - Joystick. 
 */
@Getter
public class PlayerGame implements Tickable {
    
    private Player player;
    private Game game;
    private String roomName;
    private LazyJoystick joystick;

    public PlayerGame(Player player, Game game, String roomName) {
        this.player = player;
        this.game = game;
        setRoomName(roomName);
        joystick = new LazyJoystick(game);
    }

    // only for searching
    public static PlayerGame by(Game game) {
        return new PlayerGame(null, game, null);
    }

    /**
     * Есть необходимость искать по разным компонентам этого объекта, 
     * а потому o - может принимать разные типы
     * @param o если String - это roomName, 
     *          может быть так же Player, PlayerGame, GameField
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullPlayerGame.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

        if (o instanceof String) {
            return o.equals(roomName);
        }
        
        if (o instanceof Player) {
            Player p = (Player)o;

            return p.equals(player);
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            if (player == null || pg.getPlayer() == null) {
                return LockedGame.equals(pg.game, game);
            }
            return player.equals(pg.player);
        }

        if (o instanceof GameField) {
            GameField gf = (GameField)o;

            return game.getField() == gf;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    public void remove(Consumer<PlayerGame> onRemove) {
        if (onRemove != null) {
            onRemove.accept(this);
        }
        game.close();
        player.close();
    }

    public GameField getField() {
        return game.getField();
    }

    @Override
    public String toString() {
        return String.format("PlayerGame[player=%s, roomName=%s, game=%s]",
                player,
                roomName,
                game.getClass().getSimpleName());
    }

    @Override
    public void tick() {
        joystick.tick();
    }

    public GameType getGameType() {
        return player.getGameType();
    }
    
    public void setRoomName(String roomName) {
        this.roomName = roomName;
        getPlayer();
    }

    /*
     * Так случилось, что roomName содержится в двух местах, 
     * а потому надо держать в консистентности данные  
     */
    public Player getPlayer() {
        if (player != null) {
            player.setRoomName(roomName);
        }
        return player;
    }

    public String popLastCommand() {
        return joystick.popLastCommands();
    }

    public void clearScore() {
        player.clearScore();
        game.clearScore();
    }

    public void fireOnLevelChanged() {
        Game game = getGame();
        Player player = getPlayer();
        player.getEventListener().levelChanged(game.getProgress());
    }
}
