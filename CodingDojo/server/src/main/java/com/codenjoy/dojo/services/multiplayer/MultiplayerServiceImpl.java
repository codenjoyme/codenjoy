package com.codenjoy.dojo.services.multiplayer;

import com.codenjoy.dojo.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by indigo on 2018-07-01.
 */
@Component("multiplayerService")
public class MultiplayerServiceImpl implements MultiplayerService{

    private PrinterFactory printer = new PrinterFactoryImpl();

    @Autowired
    private PlayerGames playerGames;

    @Override
    public PlayerGame playerWantsToPlay(GameType gameType, Player player, String save) {
        Game game = gameType.newGame(player.getEventListener(), printer, save, player.getName());
        return playerGames.add(player, game);
    }
}
