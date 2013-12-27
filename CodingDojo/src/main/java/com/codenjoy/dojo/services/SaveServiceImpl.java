package com.codenjoy.dojo.services;

import com.codenjoy.dojo.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 19:49
 */
@Component("saveService")
public class SaveServiceImpl implements SaveService {

    @Autowired
    private GameSaver saver;

    @Autowired
    private ChatService chatService;

    @Autowired
    private PlayerService playerService;

    @Override
    public void saveAll() {
        for (Player player : playerService.getAll()) {
            saver.saveGame(player);
        }
        saver.saveChat(chatService);
    }

    @Override
    public void loadAll() {
        for (String playerName : saver.getSavedList()) {
            load(playerName);
        }
        saver.loadChat(chatService);
    }

    @Override
    public void save(String name) {
        Player player = playerService.get(name);
        if (player != Player.NULL) {
            saver.saveGame(player);
        }
    }

    @Override
    public void load(String name) {
        Player.PlayerBuilder builder = saver.loadGame(name);
        if (builder != null) {
            playerService.register(builder);
        }
    }

    @Override
    public List<PlayerInfo> getSaves() {
        List<PlayerInfo> result = new LinkedList<PlayerInfo>();
        for (Player player : playerService.getAll()) {
            result.add(new PlayerInfo(player));
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
