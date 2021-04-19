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
import static java.util.stream.Collectors.toMap;

@Component("saveService")
public class SaveServiceImpl implements SaveService {

    public static final String DEFAULT_CALLBACK_URL = "127.0.0.1";

    @Autowired protected GameSaver saver;
    @Autowired protected PlayerService players;
    @Autowired protected Registration registration;
    @Autowired protected PlayerGames playerGames;
    @Autowired protected TimeService time;
    @Autowired protected ConfigProperties config;

    @Override
    public long saveAll() {
        return saveAll(playerGames.all());
    }

    private long saveAll(List<PlayerGame> playerGames) {
        long now = time.now();
        saver.saveGames(playerGames, now);
        return now;
    }

    @Override
    public long saveAll(String room) {
        return saveAll(playerGames.getAll(withRoom(room)));
    }

    @Override
    public void loadAll() {
        loadAll(saver.loadAll(saver.getSavedList()));
    }

    private void loadAll(List<PlayerSave> saves) {
        saves.forEach(this::load);
    }

    @Override
    public void loadAll(String room) {
        loadAll(saver.loadAll(saver.getSavedList(room)));
    }

    @Override
    public long save(String id) {
        PlayerGame playerGame = playerGames.get(id);
        if (playerGame == NullPlayerGame.INSTANCE) {
            return -1;
        }

        long now = time.now();
        saveGame(playerGame, now);
        return now;
    }

    private void saveGame(PlayerGame playerGame, long time) {
        saver.saveGame(playerGame.getPlayer(),
                playerGame.getGame().getSave().toString(),
                time);
    }

    @Override
    public boolean load(String id) {
        return load(saver.loadGame(id));
    }

    public boolean load(PlayerSave save) {
        if (save == PlayerSave.NULL) {
            return false;
        }

        resetPlayer(save);
        return true;
    }

    private void resetPlayer(PlayerSave save) {
        String id = save.getId();
        if (players.contains(id)) {
            players.remove(id);
        }
        players.register(save);
    }

    /**
     * Метод для ручной загрузки player из save для заданной gameType / room.
     * Из save если он существует, грузится только callbackUrl пользователя,
     * все остальное передается с параметрами.
     * TODO я не уверен, что оно тут надо, т.к. есть вероятно другие версии этого метода
     */
    @Override
    public void load(String id, String game, String room, String save) {
        String ip = tryGetIpFromSave(id);
        PlayerSave playerSave = new PlayerSave(id, ip, game, room, 0, save);
        resetPlayer(playerSave);
    }

    private String tryGetIpFromSave(String id) {
        PlayerSave save = saver.loadGame(id);
        return (save == PlayerSave.NULL) ? DEFAULT_CALLBACK_URL : save.getCallbackUrl();
    }

    @Override
    public List<PlayerInfo> getSaves() {
        List<Player> active = players.getAll();
        List<String> savedList = saver.getSavedList();
        return getSaves(active, savedList);
    }

    @Override
    public List<PlayerInfo> getSaves(String room) {
        List<Player> active = players.getAllInRoom(room);
        List<String> savedList = saver.getSavedList(room);
        return getSaves(active, savedList);
    }

    private List<PlayerInfo> getSaves(List<Player> active, List<String> saved) {
        Set<String> ids = new LinkedHashSet<>(saved);
        active.forEach(player -> ids.add(player.getId()));
        Map<String, Registration.User> users = registration.getUsers(ids).stream()
                .collect(toMap(user -> user.getId(), user -> user));

        Map<String, PlayerInfo> map = new HashMap<>();
        for (Player player : active) {
            PlayerInfo info = new PlayerInfo(player);
            setDataFromRegistration(info, users, player.getId());
            setSaveFromField(info, playerGames.get(player.getId()));

            map.put(player.getId(), info);
        }

        for (String id : saved) {
            if (id == null) continue;

            boolean found = map.containsKey(id);
            if (found) {
                PlayerInfo info = map.get(id);
                info.setSaved(true);
            } else {
                PlayerSave save = saver.loadGame(id);
                PlayerInfo info = new PlayerInfo(save, null, null);
                setDataFromRegistration(info, users, id);

                map.put(id, info);
            }
        }

        List<PlayerInfo> result = new LinkedList<>(map.values());
        Collections.sort(result, Comparator.comparing(playerInfo -> playerInfo.getId()));

        return result;
    }

    private void setDataFromRegistration(PlayerInfo info,
                                         Map<String, Registration.User> users,
                                         String id)
    {
        Registration.User user = users.get(id);
        if (user != null) {
            info.setCode(user.getCode());
            info.setReadableName(user.getReadableName());
            info.setEmail(user.getEmail());
        }
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

    @Override
    public void removeAllSaves(String room) {
        for (String id : saver.getSavedList(room)) {
            saver.delete(id);
        }
    }

}
