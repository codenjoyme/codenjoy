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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.lock.LockedGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Created by indigo on 2018-07-01.
 */
@Component("multiplayerService") // TODO test me
public class MultiplayerServiceImpl implements MultiplayerService{

    static class Room {
        private final int maxCount;
        private final List<GameField> archive;
        private GameField current;
        private int currentCount;

        public Room(int maxCount) {
            this.maxCount = maxCount;
            this.currentCount = 0;
            this.archive = new LinkedList<>();
        }

        public GameField getOrCreateGame(Supplier<GameField> creator) {
            if (currentCount >= maxCount) {
                archive.add(current);
                current = creator.get();
                currentCount = 0;
            }
            currentCount++;
            return current;
        }
    }

    private final Map<String, Room> rooms = new HashMap<>();

    @Autowired
    private PlayerGames playerGames;

    @Override
    public PlayerGame playerWantsToPlay(GameType gameType, Player player, String save) {
        GameField field = getField(gameType);
        GamePlayer gamePlayer = gameType.createPlayer(player.getEventListener(), save, player.getName());
        Single single = new Single(field, gamePlayer,
                gameType.getPrinterFactory(),
                gameType.getMultiplayerType());
        Game lockedGame = new LockedGame(new ReentrantReadWriteLock()).wrap(single);
        return playerGames.add(player, lockedGame);
    }

    private GameField getField(GameType gameType) {
        MultiplayerType type = gameType.getMultiplayerType();

        if (type.isSingle()) {
            return getSingle(gameType);
        }

        if (type.isTournament()) {
            return getTeam(gameType, 2);
        }

        if (type.isTriple()) {
            return getTeam(gameType, 3);
        }

        if (type.isQuadro()) {
            return getTeam(gameType, 4);
        }

        if (type.isTeam()) {
            return getTeam(gameType, type.getCount());
        }

        if (type.isMultiplayer()) {
            return getMultiple(gameType);
        }

        throw new UnsupportedOperationException("Not implemented game type: " + type);
    }

    private GameField getTeam(GameType gameType, int count) {
        return roomFor(gameType, count).getOrCreateGame(gameType::createGame);
    }

    private Room roomFor(GameType gameType, int maxCount) {
        String name = gameType.name();
        if (!rooms.containsKey(name)) {
            rooms.put(name, new Room(maxCount));
        }
        return rooms.get(name);
    }

    private GameField getSingle(GameType gameType) {
        return gameType.createGame();
    }

    private GameField getMultiple(GameType gameType) {
        List<PlayerGame> games = playerGames.getAll(gameType.name());
        if (games.isEmpty()) {
            return gameType.createGame();
        } else {
            PlayerGame playerGame = games.iterator().next();
            Game game = playerGame.getGame();
            return ((Single) game).getField();
        }
    }

}
