package com.codenjoy.dojo.services.security;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import com.codenjoy.dojo.web.controller.AdminController;
import com.codenjoy.dojo.web.controller.RoomsAliaser;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/5/2019
 */
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final MailService mailService;
    private final LinkService linkService;
    private final Registration registration;
    private final Validator validator;
    private final RoomsAliaser rooms;
    private final PlayerService playerService;
    private final ConfigProperties properties;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final ViewDelegationService viewDelegationService;
    private final GameService gameService;

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

                    String context = CodenjoyContext.getContext();
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
            return connectRegisteredPlayer(player.getCode(), request, id, gameName);
        } else {
            model.addAttribute("wait_approve", true);
            return openRegistrationForm(request, model, id, email, name);
        }
    }

    public String connectRegisteredPlayer(String code, HttpServletRequest request, String id, String gameName) {
        return "redirect:/" + register(id, code,
                gameName, request.getRemoteAddr());
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

        String gameName = request.getParameter(AdminController.GAME_NAME_FORM_KEY);
        if (!model.containsAttribute("bad_game")) {
            validator.checkGameName(gameName, CAN_BE_NULL);
        }

        Player player = new Player();
        player.setEmail(email);
        player.setName(id);
        player.setReadableName(name);
        player.setGameName(rooms.getAlias(gameName));
        if (!model.containsAttribute("player")) {
            model.addAttribute("player", player);
        }

        player.setCallbackUrl(ip);

        model.addAttribute("adminLogin", isAdminLogin);

        return getRegister(model);
    }

    public String register(String id, String code, String gameName, String ip) {
        // TODO #984 вот тут дополнительная защита на всякий
        if (gameName == null) {
            gameName = gameService.getDefaultGame();
        }
        Player player = playerService.register(id, ip, gameName);
        return getBoardUrl(code, player.getName(), gameName);
    }

    public String getBoardUrl(String code, String playerName, String gameName) {
        validator.checkPlayerName(playerName, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        return "board/player/" + playerName + "?code=" + code + viewDelegationService.buildBoardParam(gameName);
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

    private String getRegister(Model model) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
        model.addAttribute("gameNames", rooms.alises());
        return "register";
    }
}
