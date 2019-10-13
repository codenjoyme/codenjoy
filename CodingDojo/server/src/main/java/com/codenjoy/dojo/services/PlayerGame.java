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

import java.util.function.Consumer;

public class PlayerGame implements Tickable {

    private Player player;
    private Game game;
    private LazyJoystick joystick;

    public PlayerGame(Player player, Game game) {
        this.player = player;
        this.game = game;
        this.joystick = new LazyJoystick(game);
    }

    // only for searching
    public static PlayerGame by(Game game) {
        return new PlayerGame(null, game);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullPlayerGame.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

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

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public GameField getField() {
        return game.getField();
    }

    @Override
    public String toString() {
        return String.format("PlayerGame[player=%s, game=%s]",
                player,
                game.getClass().getSimpleName());
    }

    @Override
    public void tick() {
        joystick.tick();
    }

    public Joystick getJoystick() {
        return joystick;
    }

    public GameType getGameType() {
        return player.getGameType();
    }

    public String popLastCommand() {
        return joystick.popLastCommands();
    }

    public void clearScore() {
        player.clearScore();
        game.clearScore();
    }
}
