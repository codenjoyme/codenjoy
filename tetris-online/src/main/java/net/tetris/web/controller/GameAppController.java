package net.tetris.web.controller;

import net.tetris.online.service.LeaderBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * User: serhiy.zelenin
 * Date: 10/24/12
 * Time: 2:12 PM
 */
@Controller
public class GameAppController {
    @Autowired
    private LeaderBoard leaderBoard;

    @RequestMapping(value = "/")
    public String uploadGameApp() {
        return "redirect:/upload";

    }

    @RequestMapping(value = "/uploaded")
    public String gameAppUploaded(HttpServletRequest request, Model model) {
        String warFileName = (String) request.getAttribute("warFileName");

        model.addAttribute("warFileName", warFileName);
        return "uploaded";
    }

    @RequestMapping(value = "/scores")
    public String board(ModelMap model) {
        model.addAttribute("scores", leaderBoard.getScores());
        return "scores";
    }

}
