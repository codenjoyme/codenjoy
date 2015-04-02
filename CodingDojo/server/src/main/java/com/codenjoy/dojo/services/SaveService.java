package com.codenjoy.dojo.services;

import java.util.List;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 19:49
 */
public interface SaveService {

    void load(String name);
    void save(String name);
    List<PlayerInfo> getSaves();
    void saveAll();
    void loadAll();
    void removeSave(String name);
    void removeAllSaves();

}
