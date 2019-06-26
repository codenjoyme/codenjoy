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


import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.nullobj.NullPlayerGame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("saveService")
public class SaveServiceImpl implements SaveService {

    @Autowired protected GameSaver saver;
    @Autowired protected PlayerService playerService;
    @Autowired protected Registration registration;
    @Autowired protected PlayerGames playerGames;
    @Autowired protected ConfigProperties config;

    @Override
    public long saveAll() {
        long now = System.currentTimeMillis();
        for (PlayerGame playerGame : playerGames) {
            saveGame(playerGame, now);
        }
        return now;
    }

    @Override
    public void loadAll() {
        for (String playerName : saver.getSavedList()) {
            load(playerName);
        }
    }

    @Override
    public long save(String name) {
        PlayerGame playerGame = playerGames.get(name);
        if (playerGame != NullPlayerGame.INSTANCE) {
            long now = System.currentTimeMillis();
            saveGame(playerGame, now);
            return now;
        }
        return -1;
    }

    private void saveGame(PlayerGame playerGame, long time) {
        saver.saveGame(playerGame.getPlayer(),
                playerGame.getGame().getSave().toString(),
                time);
    }

    @Override
    public boolean load(String name) {
        PlayerSave save = saver.loadGame(name);
        if (save == PlayerSave.NULL) { // TODO test me
            save = saver.loadGame(Hash.getEmail(name, config.getEmailHash()));
            if (save == PlayerSave.NULL) {
                return false;
            }
        }

        if (playerService.contains(name)) { // TODO test me
            playerService.remove(name);
        }
        playerService.register(save);

        return true;
    }

    @Override
    public void load(String name, String gameName, String save) {
        PlayerSave playerSave = new PlayerSave(name, "127.0.0.1", gameName, 0, save);
        if (playerService.contains(name)) { // TODO test me
            playerService.remove(name);
        }
        playerService.register(playerSave);
    }

    @Override
    public List<PlayerInfo> getSaves() {
        Map<String, PlayerInfo> map = new HashMap<>();
        List<Player> active = playerService.getAll();
        for (Player player : active) {
            PlayerInfo info = new PlayerInfo(player);
            info.setCode(registration.getCodeById(player.getName()));
            info.setCallbackUrl(player.getCallbackUrl());
            info.setReadableName(registration.getNameById(player.getName()));
            info.setAIPlayer(player.hasAI());
            info.setScores(player.getScores()); // TODO test me

            copySave(player, info);
            map.put(player.getName(), info);
        }

        List<String> savedList = saver.getSavedList();
        for (String name : savedList) {
            if (name == null) continue;

            boolean found = map.containsKey(name);
            if (found) {
                PlayerInfo info = map.get(name);
                info.setSaved(true);
            } else {
                PlayerSave save = saver.loadGame(name);
                // TODO оптимизнуть два запроса в один
                String code = registration.getCodeById(name);
                String readableName = registration.getNameById(name);
                map.put(name, new PlayerInfo(name, readableName, code, save.getCallbackUrl(), save.getGameName(), true));
            }
        }

        List<PlayerInfo> result = new LinkedList<>(map.values());
        Collections.sort(result, Comparator.comparing(Player::getName));

        return result;
    }

    void copySave(Player player, PlayerInfo info) {
        PlayerGame playerGame = playerGames.get(player.getName());
        Game game = playerGame.getGame();
        if (game != null && game.getSave() != null) {
            info.setData(game.getSave().toString());
        }
    }

    @Override
    public void removeSave(String name) {
        saver.delete(name);
    }

    @Override
    public void removeAllSaves() {
        for (String playerName : saver.getSavedList()) {
            saver.delete(playerName);
        }
    }

}
