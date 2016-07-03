package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Component("saveService")
public class SaveServiceImpl implements SaveService {

    @Autowired private GameSaver saver;
    @Autowired private ChatService chatService;
    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;

    @Override
    public void saveAll() {
        for (Player player : playerService.getAll()) {
            saver.saveGame(player);
        }
        saver.saveChat(chatService.getMessages());
    }

    @Override
    public void loadAll() {
        for (String playerName : saver.getSavedList()) {
            load(playerName);
        }
        chatService.setMessages(saver.loadChat());
    }

    @Override
    public void save(String name) {
        Player player = playerService.get(name);
        if (player != NullPlayer.INSTANCE) {
            saver.saveGame(player);
        }
    }

    @Override
    public void load(String name) {
        PlayerSave save = saver.loadGame(name);
        playerService.register(save); // TODO тут получается, что игра не загрузится, если ее подгрузить в момент когда игрок уже играет. Может это и ок. Его сперва надо отрубить
    }

    @Override
    public List<PlayerInfo> getSaves() {
        List<PlayerInfo> result = new LinkedList<PlayerInfo>();
        for (Player player : playerService.getAll()) {
            PlayerInfo info = new PlayerInfo(player);
            info.setCode(registration.getCode(player.getName()));
            result.add(info);
        }

        List<String> savedList = saver.getSavedList();
        for (String name : savedList) {
            boolean notFound = true;
            for (PlayerInfo player : result) {
                if (name.equals(player.getName())) {  // TODO тут как-то был NPE
                    player.setSaved(true);
                    notFound = false;
                }
            }

            if (notFound) {
                result.add(new PlayerInfo(name, "", true));
            }
        }

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
