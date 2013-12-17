package com.codenjoy.dojo.services;

import java.util.List;
import java.util.Map;

/**
 * User: sanja
 * Date: 17.12.13
 * Time: 18:48
 */
public interface GameService {

    List<String> getGameNames();

    Map<String, List<String>> getSprites();

    List<Class<? extends GameType>> getGames();
}
