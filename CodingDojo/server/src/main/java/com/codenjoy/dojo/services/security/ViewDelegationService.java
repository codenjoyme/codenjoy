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

import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.web.controller.AdminController;
import com.codenjoy.dojo.web.controller.LoginController;
import com.codenjoy.dojo.web.controller.RegistrationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.function.Supplier;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/24/2019
 */
@Service
@RequiredArgsConstructor
public class ViewDelegationService {

    private final ConfigProperties properties;

    public String loginUri() {
        return getUri(properties::getLoginPage, LoginController.URI);
    }

    public String loginView() {
        return extractView(loginUri());
    }

    public String registrationUri() {
        return getUri(properties::getRegistrationPage, RegistrationController.URI);
    }

    public String registrationView() {
        return extractView(registrationUri());
    }

    public String adminUri() {
        return getUri(properties::getAdminPage, AdminController.URI);
    }

    public String adminView() {
        return extractView(adminUri());
    }

    public boolean isCustomAdminView() {
        return StringUtils.hasText(adminUri());
    }

    private String extractView(String uri) {
        return uri.startsWith("/") ? uri.substring(1) : uri;
    }

    private String getUri(Supplier<String> uriSupplier, String defaultUri) {
        String uri = uriSupplier.get();
        return StringUtils.hasText(uri) ? uri : defaultUri;
    }
}
