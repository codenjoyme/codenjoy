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

import com.codenjoy.dojo.services.hero.HeroData;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.codenjoy.dojo.services.PlayerGame.by;

@Component
public class PlayerGamesView {

    @Autowired
    protected PlayerGames service;

    public Map<String, GameData> getGamesDataMap() {
        Map<String, GameData> result = new LinkedHashMap<>();
        for (GameType gameType : service.getGameTypes()) {
            int boardSize = gameType.getBoardSize().getValue();
            GuiPlotColorDecoder decoder = new GuiPlotColorDecoder(gameType.getPlots());
            JSONObject scores = getScoresJSON(gameType.name());
            JSONObject heroesData = getCoordinatesJSON(gameType.name());

            result.put(gameType.name(), new GameData(boardSize, decoder, scores, heroesData));
        }
        return result;
    }

    private JSONObject getCoordinatesJSON(String gameType) {
        List<PlayerGame> playerGames = service.getAll(gameType);

        Map<Player, List<Player>> playersMap = new HashMap<>();
        for (PlayerGame playerGame : playerGames) {
            Player player = playerGame.getPlayer();
            Game game = playerGame.getGame();
            HeroData heroData = game.getHero();
            List<Game> gamesGroup = heroData.playersGroup();
            List<Player> playersGroup = new LinkedList<>();
            if (gamesGroup == null) {
                playersGroup.add(player);
            } else {
                for (Game game2 : gamesGroup) {
                    int index = playerGames.indexOf(by(game2));
                    if (index != -1) {
                        playersGroup.add(playerGames.get(index).getPlayer());
                    } else {
                        // TODO этого не должн случиться, но лучше порефакторить
                        throw new IllegalStateException("Игрок не в группе");
                    }
                }
            }
            playersMap.put(player, playersGroup);
        }

        Map<String, JSONObject> heroesData = new HashMap<>();
        for (PlayerGame playerGame : playerGames) {
            heroesData.put(playerGame.getPlayer().getName(),
                    new JSONObject(playerGame.getGame().getHero()));
        }

        JSONObject result = new JSONObject();
        for (Map.Entry<Player, List<Player>> entry : playersMap.entrySet()) {
            Player player1 = entry.getKey();

            JSONObject map = new JSONObject();
            result.put(player1.getName(), map);

            for (Player player2 : entry.getValue()) {
                String name = player2.getName();
                map.put(name, heroesData.get(name));
            }
        }
        return result;
    }

    private JSONObject getScoresJSON(String gameType) {
        JSONObject result = new JSONObject();
        for (PlayerGame playerGame : service.getAll(gameType)) {
            Player player = playerGame.getPlayer();
            result.put(player.getName(), player.getScore());
        }
        return result;
    }
}
