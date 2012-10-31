package net.tetris.web.controller;

import net.tetris.online.service.LeaderBoard;
import net.tetris.online.service.ReplayService;
import net.tetris.online.service.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    @Autowired
    private ReplayService replayService;

    @RequestMapping(value = "/")
    public String defaultUrl() {
        return "redirect:/upload";
    }

    @RequestMapping(value = "/upload")
    public String uploadGameApp(HttpServletRequest request) {
        return "upload";
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

    @RequestMapping(value = "/replay")
    @ResponseBody
    public String replay(HttpServletRequest request, @RequestParam("timestamp") String timestamp) {
        replayService.replay((String) request.getAttribute(SecurityFilter.LOGGED_USER), timestamp);
        return "Ok";
    }
}
