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

    @Override
    public void saveAll() {
        for (PlayerGame playerGame : playerGames) {
            saveGame(playerGame);
        }
    }

    @Override
    public void loadAll() {
        for (String playerName : saver.getSavedList()) {
            load(playerName);
        }
    }

    @Override
    public void save(String name) {
        PlayerGame playerGame = playerGames.get(name);
        if (playerGame != NullPlayerGame.INSTANCE) {
            saveGame(playerGame);
        }
    }

    private void saveGame(PlayerGame playerGame) {
        saver.saveGame(playerGame.getPlayer(),
                playerGame.getGame().getSave().toString());
    }

    @Override
    public void load(String name) {
        PlayerSave save = saver.loadGame(name);
        if (playerService.contains(name)) { // TODO test me
            playerService.remove(name);
        }
        playerService.register(save);
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
            info.setCode(registration.getCode(player.getName()));
            info.setCallbackUrl(player.getCallbackUrl());
            info.setAIPlayer(player.getAI() != null);
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
                String code = registration.getCode(name);
                map.put(name, new PlayerInfo(name, code, save.getCallbackUrl(), save.getGameName(), true));
            }
        }


        List<PlayerInfo> result = new LinkedList<>(map.values());
        Collections.sort(result, new Comparator<PlayerInfo>() {
            @Override
            public int compare(PlayerInfo o1, PlayerInfo o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return result;
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
