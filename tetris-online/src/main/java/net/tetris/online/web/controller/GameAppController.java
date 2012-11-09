package net.tetris.online.web.controller;

import net.tetris.online.service.LeaderBoard;
import net.tetris.online.service.ReplayService;
import net.tetris.online.service.Score;
import net.tetris.online.service.SecurityFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;

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
    private ScheduledExecutorService restSenderExecutorService;

    @Autowired
    private ReplayService replayService;

    @Autowired
    private JsonSerializer serializer;

    @RequestMapping(value = "/")
    public String defaultUrl() {
        return "redirect:/upload";
    }

    @RequestMapping(value = "/upload")
    public String uploadGameApp(HttpServletRequest request) {
        return "upload";
    }

    @RequestMapping(value = "/scores", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public String getScores() throws IOException {
        StringWriter writer = new StringWriter();
        serializer.serialize(writer, new ScoresData(leaderBoard.getScores()));
        return writer.toString();
    }

    @RequestMapping(value = "/replay")
    @ResponseBody
    public int replay(final HttpServletRequest request,
                         @RequestParam("timestamp") String timestamp,
                         @RequestParam("player") String player) {

        return replayService.replay(player, timestamp);
    }

    @RequestMapping(value = "/cancel")
    @ResponseBody
    public String cancel(@RequestParam("replayId") int replayId) {
        replayService.cancelReplay(replayId);
        return "Ok";
    }

}
