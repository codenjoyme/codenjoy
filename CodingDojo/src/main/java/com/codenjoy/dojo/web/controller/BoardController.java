package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.GuiPlotColorDecoder;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Collections;

@Controller
public class BoardController {
    public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>();

    @Autowired
    private PlayerService playerService;

    @Autowired
    private GameService gameService;

    @Autowired
    private ChatService chatService;

    public BoardController() {
    }

    //for unit test
    BoardController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/board/{playerName}", method = RequestMethod.GET)
    public String board(ModelMap model, @PathVariable("playerName") String playerName) {
        return board(model, playerName, null);
    }

    @RequestMapping(value = "/board/{playerName}", params = "code", method = RequestMethod.GET)
    public String board(ModelMap model, @PathVariable("playerName") String playerName, @RequestParam("code") String code) {
        Player player = playerService.get(playerName);
        if (player == null) {
            model.addAttribute("players", EMPTY_LIST);
        } else {
            model.addAttribute("players", Collections.singletonList(player));
            model.addAttribute("playerName", player.getName());
        }
        model.addAttribute("allPlayersScreen", false);

        setIsRegistered(model, playerName, code);

        gameSettings(model);
        return getBoard(model);
    }

    private void setIsRegistered(ModelMap model, String playerName, String code) {
        String registered = playerService.getByCode(code);
        boolean value = registered != null && registered.equals(playerName);
        model.addAttribute("registered", value);
        model.addAttribute("code", code);
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public String boardAll(ModelMap model) {
        return boardAll(model, null);
    }

    @RequestMapping(value = "/board", params = "code", method = RequestMethod.GET)
    public String boardAll(ModelMap model, @RequestParam("code") String code) {
        String playerName = playerService.getByCode(code);
        if (gameService.getSelectedGame().isSingleBoardGame()) {
            if (playerName == null) {
                playerName = playerService.getRandom();
            }
            if (playerName == null) {
                return "redirect:/register";
            }
            return "redirect:/board/" + playerName + ((code != null)?"?code=" + code:"");
        }

        setIsRegistered(model, playerName, code);

        gameSettings(model);
        model.addAttribute("players", playerService.getAll());
        model.addAttribute("playerName", playerName);
        model.addAttribute("allPlayersScreen", true);
        return getBoard(model);
    }

    private String getBoard(ModelMap model) {
        model.addAttribute("sprites", gameService.getSprites());
        model.addAttribute("sprites_alphabet", GuiPlotColorDecoder.GUI.toCharArray());
        return "board";
    }

    private void gameSettings(ModelMap model) {
        model.addAttribute("boardSize", gameService.getSelectedGame().getBoardSize().getValue());
        model.addAttribute("gameType", gameService.getSelectedGame().gameName());
    }

    @RequestMapping(value = "/leaderboard", method = RequestMethod.GET)
    public String leaderBoard(ModelMap model) {
        model.addAttribute("players", playerService.getAll());
        return "leaderboard";
    }

    @RequestMapping(value = "/help")
    public String help() {
        return "help";
    }

    @RequestMapping(value = "/chat", method = RequestMethod.GET)
    public String chat(@RequestParam("playerName") String name,
                       @RequestParam("code") String code,
                       @RequestParam("message") String message)
    {
        String playerName = playerService.getByCode(code);
        if (playerName != null && playerName.equals(name)) {
            chatService.chat(playerName, message);
        }
        return "ok";
    }
}
