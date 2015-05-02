package com.codenjoy.dojo.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface GameService {

    Set<String> getGameNames();

    Map<String, List<String>> getSprites();

    GameType getGame(String name);
}
