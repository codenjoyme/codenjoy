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


import com.codenjoy.dojo.client.CodenjoyContext;
import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.mail.MailService;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired private PlayerService playerService;
    @Autowired private Registration registration;
    @Autowired private GameService gameService;
    @Autowired private MailService mailService;
    @Autowired private LinkService linkService;
    @Autowired private Validator validator;
    @Autowired private ConfigProperties properties;

    public RegistrationController() {
    }

    //for unit test
    RegistrationController(PlayerService playerService, Validator validator) {
        this.playerService = playerService;
        this.validator = validator;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String openRegistrationForm(HttpServletRequest request, Model model) {
        String ip = getIp(request);

        String playerName = request.getParameter("name");
        String playerReadableName = request.getParameter("readableName");
        if (StringUtils.isEmpty(playerReadableName)) {
            playerReadableName = registration.getReadableName(playerName);
        }
        String gameName = request.getParameter(AdminController.GAME_NAME);
        validator.checkPlayerName(playerName, CAN_BE_NULL);
        validator.checkGameName(gameName, CAN_BE_NULL);

        Player player = new Player();
        player.setName(playerName);
        player.setReadableName(playerReadableName);
        player.setGameName(gameName);
        model.addAttribute("player", player);

        model.addAttribute("by_email", properties.isEmailVerificationNeeded());

        player.setCallbackUrl("http://" + ip + ":80");

        return getRegister(model);
    }

    private String getRegister(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gameNames", gameService.getGameNames());

        if (StringUtils.isEmpty(properties.getRegistrationPage())) {
            return "register";
        } else {
            model.addAttribute("url", properties.getRegistrationPage());
            return "redirect";
        }
    }

    private String getIp(HttpServletRequest request) {
        String result = request.getRemoteAddr();
        if (result.equals("0:0:0:0:0:0:0:1")) {
            result = "127.0.0.1";
        }
        return result;
    }

    private String getLocalIp(HttpServletRequest request) {
        return request.getLocalAddr();
    }

//    @RequestMapping(params = "approve", method = RequestMethod.GET)
    public String approveEmail(Model model, @RequestParam("approve") String link) {
        validator.checkMD5(link);

        Map<String, Object> data = linkService.getData(link);
        if (data == null) {
            throw new IllegalStateException("Ошибка регистрации. Повтори еще раз.");
        }
        String code = (String) data.get("code");
        String name = (String) data.get("name");
        String ip = (String) data.get("ip");
        String gameName = (String) data.get("gameName");
        registration.approve(code);
        return "redirect:/" + register(name, code, gameName, ip);
    }

//    @RequestMapping(params = "approved", method = RequestMethod.GET)
    public @ResponseBody String isEmailApproved(@RequestParam("approved") String email) throws InterruptedException {
        validator.checkPlayerName(email, CANT_BE_NULL);

        while (!registration.approved(email)) {
            Thread.sleep(2000);
        }
        Player player = null;
        while ((player = playerService.get(email)) == NullPlayer.INSTANCE) {
            Thread.sleep(2000);
        }
        String code = registration.getCode(email);
        return getBoardUrl(code, player);
    }

//    @RequestMapping(params = "remove_me", method = RequestMethod.GET)
    public String removeUserFromGame(@RequestParam("code") String code) {
        validator.checkCode(code, CANT_BE_NULL);

        String name = registration.getEmail(code);
        Player player = playerService.get(name);
        playerService.remove(player.getName());
        return "redirect:/";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerByReadableName(Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return openRegistrationForm(request, model);
        }

        if (!StringUtils.isEmpty(player.getName())) {
            return registerByEmail(player, result, request, model);
        }

        String playerReadableName = player.getReadableName();
        String gameName = player.getGameName();
        try {
            validator.checkReadableName(playerReadableName);
            validator.checkGameName(gameName, CANT_BE_NULL);
        } catch (IllegalArgumentException e) {
            model.addAttribute("bad_name", true);
            model.addAttribute("message", e.getMessage());

            return openRegistrationForm(request, model);
        }

        String playerId = registration.getEmailByReadableName(playerReadableName);

        if (StringUtils.isEmpty(playerId)) {
            playerId = RandomStringUtils.random(20, "abcdefghijklmnopqrstuvwxyz1234567890");
            player.setName(playerId);

            return registerByEmail(player, result, request, model);
        }

        if (StringUtils.isEmpty(registration.checkUserByPassword(playerId, player.getPassword()))) {
            model.addAttribute("bad_pass", true);

            return openRegistrationForm(request, model);
        }

        player.setName(playerId);
        String code = registration.getCode(playerId);
        return "redirect:/" + getBoardUrl(code, player);
    }

    public String registerByEmail(Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return openRegistrationForm(request, model);
        }

        String playerName = player.getName();
        String gameName = player.getGameName();
        validator.checkPlayerName(playerName, CANT_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        String code = "";
        boolean registered = registration.registered(playerName);
        boolean approved = registration.approved(playerName);
        if (registered && approved) {
            code = registration.login(playerName, player.getPassword());
            if (code == null) {
                model.addAttribute("bad_pass", true);

                return openRegistrationForm(request, model);
            }
        } else {
            if (!registered) {
                code = registration.register(playerName, player.getReadableName(), player.getPassword(), player.getData());
            } else {
                code = registration.getCode(playerName);
            }

            if (!approved) {
                if (properties.isEmailVerificationNeeded()) {
                    LinkService.LinkStorage storage = linkService.forLink();
                    Map<String, Object> map = storage.getMap();
                    String email = playerName;
                    map.put("name", email);
                    map.put("code", code);
                    map.put("gameName", gameName);
                    map.put("ip", request.getRemoteAddr());
                    map.put("host", gameName);

                    String host = getLocalIp(request); // TODO test me on prod
                    String context = CodenjoyContext.get();
                    String link = "http://" + host + "/" + context + "/register?approve=" + storage.getLink();
                    try {
                        mailService.sendEmail(email, "Codenjoy регистрация",
                                "Пожалуйста, подтверди регистрацию кликом на этот линк<br>" +
                                        "<a target=\"_blank\" href=\"" + link + "\">" + link + "</a><br>" +
                                        "Он направит тебя к игре.<br>" +
                                        "<br>" +
                                        "Если тебя удивило это письмо, просто удали его.<br>" +
                                        "<br>" +
                                        "<a href=\"http://codenjoy.com\">Команда Codenjoy</a>");
                    } catch (MessagingException e) {
                        throw new RuntimeException("Error sending email", e);
                    }
                } else {
                    registration.approve(code);
                    approved = true;
                }
            }
        }
        player.setCode(code);

        if (approved) {
            return "redirect:/" + register(playerName, player.getCode(),
                    gameName, request.getRemoteAddr());
        } else {
            model.addAttribute("wait_approve", true);
            return openRegistrationForm(request, model);
        }
    }

    private String register(String name, String code, String gameName, String ip) {
        Player player = playerService.register(name, ip, gameName);
        return getBoardUrl(code, player);
    }

    private String getBoardUrl(String code, Player player) {
        String playerName = player.getName();
        validator.checkPlayerName(playerName, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        return "board/player/" + playerName + "?code=" + code;
    }
}
