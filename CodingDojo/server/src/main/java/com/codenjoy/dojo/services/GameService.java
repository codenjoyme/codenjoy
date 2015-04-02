package com.codenjoy.dojo.services;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:48
 */
public interface GameService {

    Set<String> getGameNames();

    Map<String, List<String>> getSprites();

    GameType getGame(String name);
}
