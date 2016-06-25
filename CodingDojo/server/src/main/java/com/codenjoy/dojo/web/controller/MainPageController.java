package com.codenjoy.dojo.web.controller;

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import org.eclipse.jetty.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class MainPageController {

    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private GameService gameService;
    @Autowired private Statistics statistics;

    @Value("${mainPage}")
    private String mainPage;

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
        if (StringUtils.isEmpty(mainPage)) {
            return getMainPage(request, null, model);
        } else {
            model.addAttribute("url", mainPage);
            return "redirect";
        }
    }

    @RequestMapping(value = "/", params = "code", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request,
                              @RequestParam("code") String code,
                              Model model)
    {
        String userIp = request.getRemoteAddr();
        model.addAttribute("ip", userIp);

        Player player = playerService.get(registration.getEmail(code));
        request.setAttribute("registered", player != NullPlayer.INSTANCE);
        request.setAttribute("code", code);
        model.addAttribute("gameNames", gameService.getGameNames());
        model.addAttribute("statistics", statistics.getPlayers(Statistics.WAIT_TICKS_LESS, 3));
        return "main";
    }

}
