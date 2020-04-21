package com.codenjoy.dojo.config.oauth2;

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

import com.codenjoy.dojo.config.meta.SSOProfile;
import com.codenjoy.dojo.services.dao.Registration;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Igor Petrov
 * Created at 5/17/2019
 */
@Component
@RequiredArgsConstructor
@SSOProfile
public class OAuth2UserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    private final Registration registration;

    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        Authentication auth = super.extractAuthentication(map);
        if (auth != null) {
            return auth;
        }

        UserData data = new UserData(map);

        Registration.User user = registration.getOrRegister(data.id(), data.email(), data.readableName());

        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }
}
