package net.tetris.web.controller;

import net.tetris.services.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;

@Controller
@RequestMapping("/board")
public class BoardController {

    @RequestMapping(method = RequestMethod.GET)
    public String board(ModelMap model) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("vasya", "http://localhost:1234"));
        model.addAttribute("players", players);
        return "board";
    }

}
