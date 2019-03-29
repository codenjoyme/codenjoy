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
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.mail.MailService;
import com.codenjoy.dojo.services.security.GameAuthorities;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequestMapping(RegistrationController.URI)
@RequiredArgsConstructor
public class RegistrationController {

    private static final String ADMIN = "/admin";
    public static final String URI = "/register";

    private final PlayerService playerService;
    private final Registration registration;
    private final MailService mailService;
    private final LinkService linkService;
    private final Validator validator;
    private final ConfigProperties properties;
    private final RoomsAliaser rooms;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final RegistrationValidator registrationValidator;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(registrationValidator);
    }

    @GetMapping
    public String register(HttpServletRequest request, Model model,
                           @RequestParam(name = "id", required = false) String id,
                           @RequestParam(name = "email", required = false) String email,
                           @RequestParam(name = "readableName", required = false) String name) {
        return openRegistrationForm(request, model, id, email, name, false);
    }

    @GetMapping(ADMIN)
    public String registerAdmin(HttpServletRequest request, Model model,
                                @RequestParam(name = "id", required = false) String id,
                                @RequestParam(name = "email", required = false) String email,
                                @RequestParam(name = "readableName", required = false) String name) {
        return openRegistrationForm(request, model, id, email, name, true);
    }

    public String openRegistrationForm(HttpServletRequest request, Model model,
                                       String id,
                                       String email,
                                       String name) {
        return openRegistrationForm(request, model, id, email, name, false);
    }

    public String openRegistrationForm(HttpServletRequest request, Model model,
                                       String id,
                                       String email,
                                       String name,
                                       boolean isAdminLogin) {
        String ip = getIp(request);

        if (!StringUtils.isEmpty(id)) {
            email = registration.getEmailById(id);
            name = registration.getNameById(id);
            if (!model.containsAttribute("bad_email")) {
                validator.checkPlayerName(email, CAN_BE_NULL);
            }
        }

        String gameName = request.getParameter(AdminController.GAME_NAME);
        if (!model.containsAttribute("bad_game")) {
            validator.checkGameName(gameName, CAN_BE_NULL);
        }

        Player player = new Player();
        player.setEmail(email);
        player.setName(id);
        player.setReadableName(name);
        player.setGameName(rooms.getAlias(gameName));
        model.addAttribute("player", player);

        player.setCallbackUrl(ip);

        model.addAttribute("adminLogin", isAdminLogin);

        return getRegister(model);
    }

    private void populateCommonRegistrationModel(Model model, boolean isAdminLogin) {
        model.addAttribute("adminLogin", isAdminLogin);
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gameNames", rooms.alises());
    }

    private String getRegister(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gameNames", rooms.alises());

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
        if (result.equals("172.28.1.1")) {
            result = request.getHeader("X-Real-IP");
        }
        return result;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerByNameOrEmail(@Valid Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            populateCommonRegistrationModel(model, false);
            return "register";
        }

        String gameName = rooms.getGameName(player.getGameName());
        player.setGameName(gameName);

        if (player.getName() == null) {
            player.setName(Hash.getRandomId());
        }
        return register(player, result, request, model);
    }

    public String register(Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return openRegistrationForm(request, model, null, null, null);
        }

        String id = player.getName();
        String email = player.getEmail();
        String name = player.getReadableName();
        String gameName = player.getGameName();
        validator.checkPlayerName(id, CANT_BE_NULL);
        validator.checkEmail(email, CANT_BE_NULL);
        validator.checkGameName(gameName, CANT_BE_NULL);

        String code;
        boolean registered = registration.registered(id);
        boolean approved = registration.approved(id);
        if (registered && approved) {
            code = registration.login(id, player.getPassword());
            if (code == null) {
                model.addAttribute("bad_pass", true);

                return openRegistrationForm(request, model, id, email, name);
            }
            registration.updateNameAndEmail(id, name, email);
        } else {
            if (!registered) {
                if (!playerService.isRegistrationOpened()) {
                    return openRegistrationForm(request, model, id, email, name);
                }
                Registration.User user = registration.register(id, player.getEmail(), player.getReadableName(), player.getPassword(), player.getData(), GameAuthorities.USER.roles());
                code = user.getCode();
            } else {
                code = registration.getCodeById(id);
            }

            if (!approved) {
                if (properties.isEmailVerificationNeeded()) {
                    LinkService.LinkStorage storage = linkService.forLink();
                    Map<String, Object> map = storage.getMap();
                    map.put("name", id);
                    map.put("code", code);
                    map.put("gameName", gameName);
                    map.put("ip", getIp(request));

                    String hostIp = properties.getServerIp(); // TODO to use server domain here
                    map.put("host", hostIp);

                    String context = CodenjoyContext.get();
                    String link = "http://" + hostIp + "/" + context + "/register?approve=" + storage.getLink();
                    try {
                        mailService.sendEmail(id, "Codenjoy регистрация",
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
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, player.getPassword(), userDetails.getAuthorities());
            authentication = authenticationManager.authenticate(authentication);
            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                model.addAttribute("bad_pass", true);
                return openRegistrationForm(request, model, id, email, name);
            }
            return "redirect:/" + register(id, player.getCode(),
                    gameName, request.getRemoteAddr());
        } else {
            model.addAttribute("wait_approve", true);
            return openRegistrationForm(request, model, id, email, name);
        }
    }

    private String register(String id, String code, String gameName, String ip) {
        Player player = playerService.register(id, ip, gameName);
        return getBoardUrl(code, player);
    }

    private String getBoardUrl(String code, Player player) {
        String playerName = player.getName();
        validator.checkPlayerName(playerName, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        return "board/player/" + playerName + "?code=" + code;
    }
}
