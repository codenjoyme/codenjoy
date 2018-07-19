package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2018 Codenjoy
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

import com.codenjoy.dojo.bomberman.model.Bomberman;
import com.codenjoy.dojo.bomberman.model.Level;
import com.codenjoy.dojo.bomberman.model.MeatChopper;
import com.codenjoy.dojo.bomberman.services.GameRunner;
import com.codenjoy.dojo.loderunner.model.Enemy;
import com.codenjoy.dojo.loderunner.model.Loderunner;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
import com.codenjoy.dojo.services.lock.LockedGameType;
import com.codenjoy.dojo.web.rest.pojo.Chopper;
import com.codenjoy.dojo.web.rest.pojo.GameInfo;
import com.codenjoy.dojo.web.rest.pojo.PlayerInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest")
public class RestGameInfoController {

  @Autowired private GameService gameService;

  @Autowired private PlayerGames playerGames;

  private final String BOMBERMAN = "bomberman";

  private final String LODERUNNER = "loderunner";

  @RequestMapping(value = "/game/{gameName}", method = RequestMethod.GET)
  @ResponseBody
  public GameInfo getGameInfo(@PathVariable("gameName") String gameName) {
    GameInfo gameInfo = new GameInfo();
    List<Chopper> choppers = null;
    List<PlayerGame> playerGameList = playerGames.getAll(gameName);
    List<PlayerInfo> playerInfoList =
        playerGameList
            .stream()
            .map(
                playerGame -> {
                  PlayerInfo playerInfo = new PlayerInfo();
                  playerInfo.setName(playerGame.getPlayer().getName());
                  playerInfo.setScore((Integer) playerGame.getPlayer().getScore());
                  playerInfo.setX(playerGame.getGame().getHero().getCoordinate().getX());
                  playerInfo.setY(playerGame.getGame().getHero().getCoordinate().getY());
                  return playerInfo;
                })
            .collect(Collectors.toList());
    String map = (String) playerGameList.get(0).getGame().getBoardAsString();
    map = map.replaceAll("\\n","");
    gameInfo.setMap(map);
    gameInfo.setPlayerInfoList(playerInfoList);
    choppers = getChoppers(gameName);
    gameInfo.setChoppers(choppers);
    return gameInfo;
  }

  private List<Chopper> getChoppers(String gameName) {
    if (BOMBERMAN.equals(gameName)) {
      GameRunner gameRunner = (GameRunner) gameService.getGame(gameName).getGame();
      Bomberman bomberman = (Bomberman) gameRunner.getGame();
      Level level = bomberman.getLevel();
      List<MeatChopper> meatChoppers = level.getChoppers();
      return meatChoppers
          .stream()
          .map(
              meatChopper -> {
                Chopper chopper = new Chopper();
                chopper.setX(meatChopper.getX());
                chopper.setY(meatChopper.getY());
                return chopper;
              })
          .collect(Collectors.toList());

    } else if (LODERUNNER.equals(gameName)) {
      LockedGameType lockedGameType = (LockedGameType) gameService.getGame(gameName);
      com.codenjoy.dojo.loderunner.services.GameRunner gameRunner =
          (com.codenjoy.dojo.loderunner.services.GameRunner) lockedGameType.getGame();
      Loderunner loderunner = (Loderunner) gameRunner.getGame();
      List<Enemy> enemies = loderunner.getEnemies();
      return enemies
          .stream()
          .map(
              enemy -> {
                Chopper chopper = new Chopper();
                chopper.setX(enemy.getX());
                chopper.setY(enemy.getY());
                return chopper;
              })
          .collect(Collectors.toList());
    }
    return null;
  }
}
