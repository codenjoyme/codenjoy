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

import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.web.controller.AdminController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
// TODO: mark as '!sso' profile
public class PlayerFormLoginSuccessAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final RegistrationService registrationService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        Registration.User principal = (Registration.User) authentication.getPrincipal();
        String game = request.getParameter("game");
        String room = game; // TODO ROOM тут надо получить room как-то
        String repositoryUrl = registrationService.getRepository(principal.getGitHubUsername());

        if (game == null) {
            getRedirectStrategy().sendRedirect(request, response, AdminController.URI);
            return;
        }

        String targetUrl = "/" + registrationService.register(principal.getId(),
                principal.getCode(), game, room, request.getRemoteAddr(), repositoryUrl, principal.getSlackId());

        log.debug("Redirecting to  URL: " + targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
