package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.chat.ChatService;
import com.codenjoy.dojo.services.dao.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import static com.codenjoy.dojo.web.controller.AdminController.GAME_NAME;

@Controller
public class BoardController {
    public static final ArrayList<Object> EMPTY_LIST = new ArrayList<Object>();

    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private ChatService chatService;
    @Autowired private GameService gameService;

    @Value("${donate.code}")
    private String donateCode;

    public BoardController() {
    }

    //for unit test
    BoardController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/board/player/{playerName:.+}", method = RequestMethod.GET)
    public String board(ModelMap model, @PathVariable("playerName") String playerName) {
        return board(model, playerName, null);
    }

    @RequestMapping(value = "/board/player/{playerName:.+}", params = "code", method = RequestMethod.GET)
    public String board(ModelMap model, @PathVariable("playerName") String playerName, @RequestParam("code") String code) {
        Player player = playerService.get(playerName);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?name=" + playerName;
        }

        model.addAttribute("code", code);
        model.addAttribute(GAME_NAME, player.getGameName());
        model.addAttribute("players", Arrays.asList(player));
        model.addAttribute("playerName", player.getName());
        model.addAttribute("allPlayersScreen", false);
        return "board";
    }

    @RequestMapping(value = "/board", method = RequestMethod.GET)
    public String boardAll() {
        GameType gameType = playerService.getAnyGameWithPlayers();
        if (gameType == NullGameType.INSTANCE) {
            return "redirect:/register";
        }
        return "redirect:/board/game/" + gameType.name();
    }

    @RequestMapping(value = "/board/game/{gameName}", method = RequestMethod.GET)
    public String boardAllGames(ModelMap model,  @PathVariable("gameName") String gameName) {
        if (gameName == null) {
            return "redirect:/board";
        }

        Player player = playerService.getRandom(gameName);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?" + GAME_NAME + "=" + gameName;
        }
        GameType gameType = player.getGameType();
        if (gameType.isSingleBoard()) {
            return "redirect:/board/player/" + player.getName();
        }

        model.addAttribute("code", null);
        model.addAttribute(GAME_NAME, gameName);
        model.addAttribute("players", playerService.getAll(gameName));
        model.addAttribute("playerName", null);
        model.addAttribute("allPlayersScreen", true); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport
        return "board";
    }

    @RequestMapping(value = "/board", params = "code", method = RequestMethod.GET)
    public String boardAll(ModelMap model, @RequestParam("code") String code) {
        String name = registration.getEmail(code);
        Player player = playerService.get(name);
        if (player == NullPlayer.INSTANCE) {
            player = playerService.getRandom(null);
        }
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register";
        }

        if (player.getGameType().isSingleBoard()) {
            return "redirect:/board/player/" + player.getName() + ((code != null)?"?code=" + code:"");
        }

        model.addAttribute("code", code);
        model.addAttribute(GAME_NAME, player.getGameName());
        model.addAttribute("players", playerService.getAll(player.getGameName()));
        model.addAttribute("playerName", player.getName());
        model.addAttribute("allPlayersScreen", true);
        return "board";
    }

    @RequestMapping(value = "/donate", method = RequestMethod.GET)
    public String donate(ModelMap model) {
        model.addAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        model.addAttribute("donateCode", donateCode);
        return "donate-form";
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
        Player player = playerService.get(registration.getEmail(code));
        if (player != NullPlayer.INSTANCE && player.getName().equals(name)) {
            chatService.chat(player.getName(), message);
        }
        return "ok";
    }
}
