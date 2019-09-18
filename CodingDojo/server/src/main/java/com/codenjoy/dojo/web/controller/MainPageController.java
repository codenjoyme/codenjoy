package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.GameService;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.security.RegistrationService;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class MainPageController {

    private final PlayerService playerService;
    private final Registration registration;
    private final GameService gameService;
    private final Validator validator;
    private final ConfigProperties properties;
    private final RoomsAliaser rooms;
    private final RegistrationService registrationService;

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help(Model model) {
        model.addAttribute("gameNames", gameService.getOnlyGameNames());
        return "help";
    }

    @RequestMapping(value = "/help", params = "gameName", method = RequestMethod.GET)
    public String helpForGame(@RequestParam("gameName") String gameName) {
        validator.checkGameName(gameName, CANT_BE_NULL);

        String language = properties.getHelpLanguage();
        String suffix = (StringUtils.isEmpty(language)) ? "" : ("-" + language);
        return "redirect:resources/help/" + gameName + suffix + ".html";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request, Model model, Authentication authentication) {
        String mainPage = properties.getMainPage();
        if (StringUtils.isNotEmpty(mainPage)) {
            model.addAttribute("url", mainPage);
            return "redirect";
        }

        if (gameService.getGameNames().size() > 1) {
            return getMainPage(request, null, model);
        }

        Registration.User principal = (Registration.User) authentication.getPrincipal();
        // TODO если юзер не авторизирован, то надо вызвать борду со всеми пользователями
        if (true) {
            return "redirect:" + registrationService.getBoardUrl(principal.getCode(), principal.getId(), null);
        } else {
            return "redirect:board";
        }
    }

    @RequestMapping(value = "/", params = "code", method = RequestMethod.GET)
    public String getMainPage(HttpServletRequest request,
                              @RequestParam("code") String code,
                              Model model)
    {
        validator.checkCode(code, CAN_BE_NULL);

        String userIp = request.getRemoteAddr();
        model.addAttribute("ip", userIp);

        Player player = playerService.get(registration.getIdByCode(code));
        boolean registered = player != NullPlayer.INSTANCE;
        request.setAttribute("registered", registered);
        request.setAttribute("code", code);
        model.addAttribute("gameName",
                registered ? player.getGameName() : StringUtils.EMPTY);
        model.addAttribute("gameNames", rooms.all());
        return "main";
    }

    @RequestMapping(value = "/denied")
    public ModelAndView displayAccessDeniedPage(){
        return new ModelAndView(){{
            addObject("message", "Invalid Username or Password");
            setViewName("errorPage");
        }};
    }

}
