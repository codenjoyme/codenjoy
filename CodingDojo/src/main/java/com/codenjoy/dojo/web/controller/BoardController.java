package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.chat.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class BoardController {
    public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>();

    @Autowired private PlayerService playerService;
    @Autowired private ChatService chatService;
    @Autowired private GameService gameService;

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
        if (player == Player.NULL) {
            return "redirect:/register?name=" + playerName;
        } else {
            model.addAttribute("players", Arrays.asList(player));
            model.addAttribute("playerName", player.getName());
        }
        model.addAttribute("allPlayersScreen", false);

        setIsRegistered(model, playerName, code);

        GameType gameType = player.getGameType();
        model.addAttribute("boardSize", gameType.getBoardSize().getValue());
        model.addAttribute("singleBoardGame", gameType.isSingleBoardGame());
        model.addAttribute("gameName", player.getGameName());
        return getBoard(model);
    }

    private void setIsRegistered(ModelMap model, String playerName, String code) {
        Player registered = playerService.getByCode(code);
        boolean value = registered != Player.NULL && registered.getName().equals(playerName);
        model.addAttribute("registered", value);
        model.addAttribute("code", code);
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public String boardAll() {
        GameType gameType = playerService.getAnyGameWithPlayers();
        if (gameType == GameType.NULL) {
            return "redirect:/register";
        }
        return "redirect:/board?gameName=" + gameType.gameName();
    }

    @RequestMapping(value = "/board", params = "gameName", method = RequestMethod.GET)
    public String boardAllGames(ModelMap model,  @RequestParam("gameName") String gameName) {
        if (gameName == null) {
            return "redirect:/board";
        }

        Player player = playerService.getRandom(gameName);
        if (player == Player.NULL) {
            return "redirect:/register?gameName=" + gameName;
        }
        GameType gameType = player.getGameType();
        if (gameType.isSingleBoardGame()) {
            return "redirect:/board/" + player.getName();
        }

        model.addAttribute("players", playerService.getAll(gameName));
        model.addAttribute("playerName", null);
        model.addAttribute("gameName", gameName);
        setIsRegistered(model, null, null);

        model.addAttribute("boardSize", gameType.getBoardSize().getValue());
        model.addAttribute("singleBoardGame", gameType.isSingleBoardGame());
        model.addAttribute("allPlayersScreen", true); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport

        return getBoard(model);
    }

    @RequestMapping(value = "/board", params = "code", method = RequestMethod.GET)
    public String boardAll(ModelMap model, @RequestParam("code") String code) {
        Player player = playerService.getByCode(code);
        if (player == Player.NULL) {
            player = playerService.getRandom(null);
        }
        if (player == Player.NULL) {
            return "redirect:/register";
        }

        if (player.getGameType().isSingleBoardGame()) {
            return "redirect:/board/" + player.getName() + ((code != null)?"?code=" + code:"");
        }

        setIsRegistered(model, player.getName(), code);

        GameType gameType = player.getGameType();
        model.addAttribute("boardSize", gameType.getBoardSize().getValue());
        model.addAttribute("singleBoardGame", gameType.isSingleBoardGame());
        model.addAttribute("gameName", player.getGameName());

        model.addAttribute("players", playerService.getAll(player.getGameName()));
        model.addAttribute("playerName", player.getName());
        model.addAttribute("allPlayersScreen", true);
        return getBoard(model);
    }

    private String getBoard(ModelMap model) {
        model.addAttribute("sprites", gameService.getSprites());
        model.addAttribute("sprites_alphabet", GuiPlotColorDecoder.GUI.toCharArray());
        return "board";
    }

    @RequestMapping(value = "/donate", method = RequestMethod.GET)
    public String donate(ModelMap model) {
        model.addAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("donateCode", "52b4f331bf4efc5f1f85981e");
        return "donate";
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
        Player player = playerService.getByCode(code);
        if (player != Player.NULL && player.getName().equals(name)) {
            chatService.chat(player.getName(), message);
        }
        return "ok";
    }
}
