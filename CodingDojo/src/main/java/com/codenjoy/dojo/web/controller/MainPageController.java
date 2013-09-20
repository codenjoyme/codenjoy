package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

/**
 * User: apofig
 * Date: 9/20/12
 * Time: 1:37 PM
 */
@Controller
public class MainPageController {

    @Autowired
    private PlayerService playerService;

    public MainPageController() {
    }

    //for unit test
    MainPageController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(Model model) {
        model.addAttribute("game", playerService.getGameType());
        return "help";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request, Model model) {
        String userIp = request.getRemoteAddr();
        model.addAttribute("ip", userIp);

        String playerName = (String) request.getSession().getAttribute("playerName");
        request.setAttribute("registered", playerName != null);

//        Player player = playerService.findPlayerByIp(userIp);  // TODO реализовать через регистрацию с паролем
//        model.addAttribute("user", player.getName());

        return "main";
    }

}
