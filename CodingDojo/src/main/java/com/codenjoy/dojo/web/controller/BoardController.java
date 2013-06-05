package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
public class BoardController {
    public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>();

    @Autowired
    private PlayerService playerService;

    public BoardController() {
    }

    //for unit test
    BoardController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/board/{playerName}",method = RequestMethod.GET)
    public String board(ModelMap model, @PathVariable("playerName") String playerName) {
        Player player = playerService.findPlayer(playerName);
        if (player == null) {
            model.addAttribute("players", EMPTY_LIST);
        }else{
            model.addAttribute("players", Collections.singletonList(player));
            model.addAttribute("playerName", player.getName());
        }
        model.addAttribute("allPlayersScreen", false);
        gameSettings(model);
        return "board";
    }

    @RequestMapping(value = "/board",method = RequestMethod.GET)
    public String boardAll(ModelMap model) {
        gameSettings(model);
        model.addAttribute("players", playerService.getPlayers());
        model.addAttribute("allPlayersScreen", true);
        return "board";
    }

    private void gameSettings(ModelMap model) {
        model.addAttribute("boardSize", playerService.getBoardSize());
        model.addAttribute("gameType", playerService.getGameType());
    }

    @RequestMapping(value = "/leaderboard",method = RequestMethod.GET)
    public String leaderBoard(ModelMap model) {
        List<Player> players = new ArrayList<Player>(playerService.getPlayers());
        Collections.sort(players, new Comparator<Player>() {
            @Override
            public int compare(Player player1, Player player2) {
                return player2.getScore() - player1.getScore();
            }
        });

        model.addAttribute("players", players);
        return "leaderboard";
    }

    @RequestMapping(value = "/help")
    public String help() {
        return "help";
    }
}
