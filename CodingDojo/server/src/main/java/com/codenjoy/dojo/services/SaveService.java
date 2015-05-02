package com.codenjoy.dojo.services;

import java.util.List;

public interface SaveService {

    void load(String name);
    void save(String name);
    List<PlayerInfo> getSaves();
    void saveAll();
    void loadAll();
    void removeSave(String name);
    void removeAllSaves();

}
