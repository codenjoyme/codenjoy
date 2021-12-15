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


import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.security.GameAuthorities;
import com.codenjoy.dojo.services.security.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collection;

@Controller
@RequestMapping(RegistrationController.URI)
@AllArgsConstructor
public class RegistrationController {

    public static final String URI = "/register";

    private static final String ADMIN = "/admin";
    private static final String LOGIN_PAGE = "redirect:/login";

    private PlayerService playerService;
    private Registration registration;
    private RegistrationValidator registrationValidator;
    private RegistrationService registrationService;

    @InitBinder
    private void initBinder(WebDataBinder webDataBinder) {
        webDataBinder.setValidator(registrationValidator);
    }

    @GetMapping
    public String register(HttpServletRequest request, Model model,
                           @RequestParam(name = "id", required = false) String id,
                           @RequestParam(name = "email", required = false) String email,
                           @RequestParam(name = "fullName", required = false) String fullName,
                           @RequestParam(name = "readableName", required = false) String name,
                           @RequestParam(name = "gitHubUsername", required = false) String github,
                           @RequestParam(name = "slackEmail", required = false) String slackId) {
        return registrationService.openRegistrationForm(request, model, id, email, fullName, name, github, slackId, false);
    }

    @GetMapping(ADMIN)
    public String registerAdmin(HttpServletRequest request, Model model,
                                @RequestParam(name = "id", required = false) String id,
                                @RequestParam(name = "email", required = false) String email,
                                @RequestParam(name = "fullName", required = false) String fullName,
                                @RequestParam(name = "readableName", required = false) String name,
                                @RequestParam(name = "gitHubUsername", required = false) String github,
                                @RequestParam(name = "slackEmail", required = false) String slackId) {
        return registrationService.openRegistrationForm(request, model, id, email, fullName, name, github, slackId, true);
    }

    @PostMapping()
    public String registerByName(@Valid Player player, BindingResult result, HttpServletRequest request, Model model) {
        if (noValidationErrors(result) && hasUniqueGithubUsername(player.getGitHubUsername())) {

            if (player.getId() == null) {
                player.setId(Hash.getRandomId());
            }

            boolean successfulRegistration = registerPlayer(player.getId(), player.getEmail(), player.getFullName(), player.getReadableName(),
                    player.getPassword(), player.getData(), GameAuthorities.USER.roles(), player.getGitHubUsername(), player.getSlackEmail());

            if (successfulRegistration) {
                return LOGIN_PAGE;
            }

            return redirectToRegistrationForm(request, model, player, null, player.getEmail(),
                    player.getFullName(), player.getReadableName(), player.getGitHubUsername(), player.getSlackEmail());
        }

        return redirectToRegistrationForm(request, model, player, null, player.getEmail(),
                player.getFullName(), player.getReadableName(), player.getGitHubUsername(), player.getSlackEmail());
    }

    private boolean registerPlayer(String id, String email, String fullName, String readableName,
                                   String password, String data, Collection<String> roles, String gitHubUsername, String slackEmail) {
        registration.register(id, email, fullName, readableName, password, data, roles, gitHubUsername, slackEmail);
        return registration.cacheContainsGithubUsername(gitHubUsername);
    }

    private boolean hasUniqueGithubUsername(String gitHubUsername) {
        return registration.checkCacheIfUniqueGithubUsername(gitHubUsername);
    }

    private boolean noValidationErrors(BindingResult result) {
        return !result.hasErrors();
    }

    private String redirectToRegistrationForm(HttpServletRequest request, Model model, Player player,
                                              String id,
                                              String email,
                                              String fullName,
                                              String name,
                                              String github,
                                              String slackEmail) {
        populateCommonRegistrationModel(model, false);
        player.dropPassword();
        return registrationService.openRegistrationForm(request, model, id, email, fullName, name, github, slackEmail);
    }

    private void populateCommonRegistrationModel(Model model, boolean isAdminLogin) {
        model.addAttribute("adminLogin", isAdminLogin);
        model.addAttribute("opened", playerService.isRegistrationOpened());
    }
}