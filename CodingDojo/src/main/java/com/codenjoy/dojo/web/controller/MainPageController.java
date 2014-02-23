package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.Statistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 1:37 PM
 */
@Controller
public class MainPageController {

    @Autowired private PlayerService playerService;
    @Autowired private GameService gameService;
    @Autowired private Statistics statistics;

    public MainPageController() {
    }

    //for unit test
    MainPageController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(Model model) {
        model.addAttribute("gameNames", gameService.getGameNames());
        return "help";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request, Model model) {
        return getMainPage(request, null, model);
    }

    @RequestMapping(value = "/", params = "code", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request,
                              @RequestParam("code") String code,
                              Model model)
    {
        String userIp = request.getRemoteAddr();
        model.addAttribute("ip", userIp);

        Player player = playerService.getByCode(code);
        request.setAttribute("registered", player != Player.NULL);
        request.setAttribute("code", code);
        model.addAttribute("gameNames", gameService.getGameNames());
        model.addAttribute("statistics", statistics.getActivePlayers());
        return "main";
    }

}
