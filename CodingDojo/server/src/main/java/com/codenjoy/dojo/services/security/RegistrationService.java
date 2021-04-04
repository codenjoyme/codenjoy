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
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.web.controller.RoomsAliaser;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
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

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

/**
 * @author Igor Petrov
 * Created at 4/5/2019
 */
@Service
@AllArgsConstructor
public class RegistrationService {

    private MailService mailService;
    private LinkService linkService;
    private Registration registration;
    private Validator validator;
    private RoomsAliaser rooms;
    private PlayerService playerService;
    private RoomService roomService;
    private ConfigProperties properties;
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private ViewDelegationService viewDelegationService;

    public String register(Player player, String room, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return openRegistrationForm(request, model, null, null, null);
        }

        String id = player.getId();
        String email = player.getEmail();
        String name = player.getReadableName();
        String game = player.getGame();
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkEmail(email, CANT_BE_NULL);
        validator.checkGame(game, CANT_BE_NULL);

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
                if (!playerService.isRegistrationOpened(player.getRoom())) {
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
                    map.put("game", game);
                    map.put("ip", getIp(request));

                    String hostIp = properties.getServerIp(); // TODO to use server domain here
                    map.put("host", hostIp);

                    String context = CodenjoyContext.getContext();
                    String link = "http://" + hostIp + "/" + context + "/register?approve=" + storage.getLink();

                    sendEmail(id, "Пожалуйста, подтверди регистрацию кликом на этот линк<br>" +
                            "<a target=\"_blank\" href=\"" + link + "\">" + link + "</a><br>" +
                            "Он направит тебя к игре.<br>" +
                            "<br>" +
                            "Если тебя удивило это письмо, просто удали его.<br>" +
                            "<br>" +
                            "<a href=\"http://codenjoy.com\">Команда Codenjoy</a>", "Codenjoy регистрация");
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
            return connectRegisteredPlayer(player.getCode(), request, id, room, game);
        } else {
            model.addAttribute("wait_approve", true);
            return openRegistrationForm(request, model, id, email, name);
        }
    }

    @SneakyThrows
    private void sendEmail(String id, String body, String title) {
        mailService.sendEmail(id, title, body);
    }

    public String connectRegisteredPlayer(String code, HttpServletRequest request, String id, String room, String game) {
        return "redirect:/" + register(id, code, game, room, request.getRemoteAddr());
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
                validator.checkEmail(email, CAN_BE_NULL);
            }
        }

        String game = request.getParameter("game");
        if (!model.containsAttribute("bad_game")) {
            validator.checkGame(game, CAN_BE_NULL);
        }

        Player player = new Player();
        player.setEmail(email);
        player.setId(id);
        player.setReadableName(name);
        player.setGame(rooms.getAlias(game));
        if (!model.containsAttribute("player")) {
            model.addAttribute("player", player);
        }

        player.setCallbackUrl(ip);

        model.addAttribute("adminLogin", isAdminLogin);

        return getRegister(model);
    }

    public String register(String id, String code, String game, String room, String ip) {
        if (!roomService.isOpened(room)) {
            return "login?closed=true";
        }

        Player player = playerService.register(id, game, room, ip);
        if (player == NullPlayer.INSTANCE) {
            return "login";
        }
        return getBoardUrl(code, player.getId(), game);
    }

    public String getBoardUrl(String code, String id, String game) {
        validator.checkPlayerId(id, CAN_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        return "board/player/" + id + "?code=" + code + viewDelegationService.buildBoardParam(game);
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
        model.addAttribute("games", rooms.alises());
        return "register";
    }
}
