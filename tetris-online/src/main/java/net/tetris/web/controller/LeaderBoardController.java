package net.tetris.web.controller;

import net.tetris.online.service.LeaderBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * User: serhiy.zelenin
 * Date: 10/26/12
 * Time: 2:00 PM
 */
@Controller
public class LeaderBoardController {
    @Autowired
    private LeaderBoard leaderBoard;

    @RequestMapping(value = "/scores")
    public String board(ModelMap model) {
        model.addAttribute("scores", leaderBoard.getScores());
        return "scores";
    }
}
