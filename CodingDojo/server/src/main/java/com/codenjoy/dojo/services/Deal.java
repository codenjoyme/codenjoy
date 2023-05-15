package com.codenjoy.dojo.services;

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


import com.codenjoy.dojo.services.chat.ChatPost;
import com.codenjoy.dojo.services.info.MessagesListener;
import com.codenjoy.dojo.services.lock.LockedGame;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullDeal;
import com.codenjoy.dojo.services.nullobj.NullGame;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Consumer;

import static com.codenjoy.dojo.services.multiplayer.GamePlayer.DEFAULT_TEAM_ID;

/**
 * Класс представляет собой игру пользователя, объединяя данные о пользователе в Player,
 * Игру на которой он играет - Game. Комнату в которой эта игра происходит - playerRoom. 
 * А так же джойстик которым он играет - Joystick. 
 */
@Slf4j
@Getter
public class Deal implements Tickable {

    public static final Deal ANONYMOUS = new Deal(Player.ANONYMOUS, NullGame.INSTANCE, null);

    private Player player;
    private Game game;
    private LazyJoystick joystick;
    private ChatPost chat;
    private MessagesListener messages;

    public Deal(Player player, Game game, String room) {
        this.player = player;
        this.game = game;
        setRoom(room);
        joystick = new LazyJoystick(game);
    }

    // only for searching
    public static Deal by(Game game) {
        return new Deal(null, game, null);
    }

    /**
     * Есть необходимость искать по разным компонентам этого объекта, 
     * а потому o - может принимать разные типы
     * @param o если String - это room,
     *          может быть так же Player, Deal, GameField
     */
    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullDeal.INSTANCE && (o != NullPlayer.INSTANCE && o != NullDeal.INSTANCE)) return false;

        if (o instanceof String) {
            return o.equals(player.getRoom());
        }
        
        if (o instanceof Player) {
            Player p = (Player)o;

            return p.equals(player);
        }

        if (o instanceof Deal) {
            Deal deal = (Deal)o;

            if (player == null || deal.getPlayer() == null) {
                return LockedGame.equals(deal.game, game);
            }
            return player.equals(deal.player);
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

    public void remove(Consumer<Deal> onRemove) {
        if (onRemove != null) {
            onRemove.accept(this);
        }
        player.close();
    }

    public GameField getField() {
        return game.getField();
    }

    @Override
    public String toString() {
        return String.format("Deal[player=%s, room=%s, game=%s]",
                player,
                player.getRoom(),
                game.getClass().getSimpleName());
    }

    @Override
    public void tick() {
        joystick.tick();
    }

    public GameType getGameType() {
        return player.getGameType();
    }

    public String getPlayerId() {
        return getPlayer().getId();
    }

    public String popLastCommand() {
        return joystick.popLastCommands();
    }

    public void clearScore() {
        player.clearScore();
        game.clearScore();
        game.getProgress().reset();
    }

    public void fireOnLevelChanged() {
        Game game = getGame();
        Player player = getPlayer();
        player.getInfo().levelChanged(game.getProgress());
    }

    public String getRoom() {
        return Optional.ofNullable(player)
                .map(Player::getRoom)
                .orElse(null);
    }

    public void setRoom(String room) {
        if (player != null) {
            player.setRoom(room);
        }
    }

    public int getTeamId() {
        return Optional.ofNullable(getPlayer())
                .map(Player::getTeamId)
                .orElse(DEFAULT_TEAM_ID);
    }

    public void setTeamId(int teamId) {
        if (player != null) {
            player.setTeamId(teamId);
        }
        if (game != null && game.getPlayer() != null) {
            game.getPlayer().inTeam(teamId);
        }
    }

    public MultiplayerType getType() {
        GameType gameType = getGameType();
        return gameType.getMultiplayerType(gameType.getSettings());
    }

    public void setChat(ChatPost chat) {
        this.chat = chat;
        if (chat != null) {
            messages = (playerId, message) -> {
                try {
                    chat.postFieldFor(playerId, message, getRoom());
                } catch (Exception exception) {
                    // TODO случается такое, когда перегружаешь всех участников на админке
                    //      а в это время игра постит сообщения
                    log.error("Post event message to the player: " + getPlayerId(), exception);
                }
            };
            player.getInfo().add(messages);
        } else {
            player.getInfo().remove(messages);
        }
    }

    public ChatPost chat() {
        return chat;
    }

    public LevelProgress levelFromSave() {
        return new LevelProgress(getGame().getSave());
    }
}
