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

import java.util.function.BiConsumer;

public class PlayerGame implements Tickable {

    private Player player;
    private Game game;
    private LazyJoystick lazyJoystick;

    public PlayerGame(Player player, Game game, LazyJoystick lazyJoystick) {
        this.player = player;
        this.game = game;
        this.lazyJoystick = lazyJoystick;
    }

    // only for searching
    public static PlayerGame by(Game game) {
        return new PlayerGame(null, game, null);
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

            if (player == null) {
                return LockedGame.equals(pg.game, game);
            }
            return pg.player.equals(player);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return player.hashCode();
    }

    public void remove(BiConsumer<Player, Joystick> onRemove) {
        if (onRemove != null) {
            onRemove.accept(player, lazyJoystick);
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

    @Override
    public String toString() {
        return String.format("PlayerGame[player=%s, game=%s]",
                player,
                game.getClass().getSimpleName());
    }

    @Override
    public void tick() {
        lazyJoystick.tick();
    }
}
