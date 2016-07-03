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
