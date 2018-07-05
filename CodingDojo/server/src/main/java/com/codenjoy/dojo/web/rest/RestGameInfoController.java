package com.codenjoy.dojo.web.rest;

import com.codenjoy.dojo.services.PlayerGame;
import com.codenjoy.dojo.services.PlayerGames;
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

  @Autowired private PlayerGames playerGames;

  @RequestMapping(value = "/game/{gameName}", method = RequestMethod.GET)
  @ResponseBody
  public GameInfo getGameInfo(@PathVariable("gameName") String gameName) {
    GameInfo gameInfo = new GameInfo();
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
    gameInfo.setMap(map);
    gameInfo.setPlayerInfoList(playerInfoList);
    return gameInfo;
  }
}
