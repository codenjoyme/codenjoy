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

 import static com.codenjoy.dojo.services.PlayerGames.withRoom;

@Component("saveService")
public class SaveServiceImpl implements SaveService {

    public static final String DEFAULT_CALLBACK_URL = "127.0.0.1";

    @Autowired protected GameSaver saver;
    @Autowired protected PlayerService players;
    @Autowired protected Registration registration;
    @Autowired protected PlayerGames playerGames;
    @Autowired protected ConfigProperties config;

    @Override
    public long saveAll() {
        return saveAll(playerGames.all());
    }

    private long saveAll(List<PlayerGame> playerGames) {
        long now = System.currentTimeMillis();
        for (PlayerGame playerGame : playerGames) {
            saveGame(playerGame, now);
        }
        return now;
    }

    @Override
    public long saveAll(String roomName) {
        return saveAll(playerGames.getAll(withRoom(roomName)));
    }

    @Override
    public void loadAll() {
        for (String id : saver.getSavedList()) {
            load(id);
        }
    }

    @Override
    public long save(String id) {
        PlayerGame playerGame = playerGames.get(id);
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
    public boolean load(String id) {
        PlayerSave save = saver.loadGame(id);
        if (save == PlayerSave.NULL) {
            return false;
        }

        resetPlayer(id, save);
        return true;
    }

    private void resetPlayer(String id, PlayerSave save) {
        if (players.contains(id)) {
            players.remove(id);
        }
        players.register(save);
    }

    /**
     * Метод для ручной загрузки player из save для заданной gameType / roomName.
     * Из save если он существует, грузится только callbackUrl пользователя,
     * все остальное передается с параметрами.
     * TODO я не уверен, что оно тут надо, т.к. есть вероятно другие версии этого метода
     */
    @Override
    public void load(String id, String roomName, String gameName, String save) {
        String ip = tryGetIpFromSave(id);
        resetPlayer(id, new PlayerSave(id, ip, roomName, gameName, 0, save));
    }

    private String tryGetIpFromSave(String id) {
        PlayerSave save = saver.loadGame(id);
        return (save == PlayerSave.NULL) ? DEFAULT_CALLBACK_URL : save.getCallbackUrl();
    }

    @Override
    public List<PlayerInfo> getSaves() {
        Map<String, PlayerInfo> map = new HashMap<>();
        for (Player player : players.getAll()) {
            PlayerInfo info = new PlayerInfo(player);
            setDataFromRegistration(info, player.getId());
            setSaveFromField(info, playerGames.get(player.getId()));

            map.put(player.getId(), info);
        }

        List<String> savedList = saver.getSavedList();
        for (String id : savedList) {
            if (id == null) continue;

            boolean found = map.containsKey(id);
            if (found) {
                PlayerInfo info = map.get(id);
                info.setSaved(true);
            } else {
                PlayerSave save = saver.loadGame(id);
                PlayerInfo info = new PlayerInfo(save, null, null);
                setDataFromRegistration(info, id);

                map.put(id, info);
            }
        }

        List<PlayerInfo> result = new LinkedList<>(map.values());
        Collections.sort(result, Comparator.comparing(playerInfo -> playerInfo.getId()));

        return result;
    }

    private void setDataFromRegistration(PlayerInfo info, String name) {
        registration.getUserById(name)
                .ifPresent((user) -> {
                    info.setCode(user.getCode());
                    info.setReadableName(user.getReadableName());
                });
    }

    void setSaveFromField(PlayerInfo info, PlayerGame playerGame) {
        Game game = playerGame.getGame();
        if (game != null && game.getSave() != null) {
            info.setData(game.getSave().toString());
        }
    }

    @Override
    public void removeSave(String id) {
        saver.delete(id);
    }

    @Override
    public void removeAllSaves() {
        for (String id : saver.getSavedList()) {
            saver.delete(id);
        }
    }

}
