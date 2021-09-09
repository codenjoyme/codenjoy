package com.codenjoy.dojo.transport.auth;

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


import com.codenjoy.dojo.client.WebSocketRunner;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.web.controller.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class SecureAuthenticationService implements AuthenticationService {

    @Autowired
    protected Registration registration;

    @Autowired
    protected Validator validator;

    @Override
    public String authenticate(HttpServletRequest request) {
        String id = request.getParameter("user");
        String code = request.getParameter("code");

        if (isAi(id)){
            log.debug("User {} with code {} logged in as AI", id, code);

            return id;
        }

        try {
            String result = validator.checkPlayerCode(id, code);
            log.debug("User {} with code {} logged as {}", id, code, result);
            return result;
        } catch (IllegalArgumentException exception) {
            log.error(String.format("Error during check user on authenticate " +
                    "for user %s with code %s. We will drop this user.", id, code), exception);
            return null;
        }
    }

    private boolean isAi(String id) {
        return id.endsWith(WebSocketRunner.BOT_ID_SUFFIX);
    }
}
