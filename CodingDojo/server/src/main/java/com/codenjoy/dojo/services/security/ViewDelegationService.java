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

import com.codenjoy.dojo.web.controller.AdminController;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author Igor Petrov
 * Created at 4/24/2019
 */
@Service
public class ViewDelegationService {

    public static final String ADMIN_PAGE_PROP = "page.admin.url";

    @Autowired
    private Environment env;

    public String adminUri(String gameName) {
        return resolveProperty(gameName, ADMIN_PAGE_PROP, AdminController.URI);
    }

    public String adminView(String gameName) {
        return extractView(adminUri(gameName));
    }

    private String extractView(String uri) {
        return uri.startsWith("/") ? uri.substring(1) : uri;
    }

    private final String resolveProperty(String prefix, String key, String defaultValue) {
        String prefixedKey = prefix + "." + key;
        String property = env.containsProperty(prefixedKey)
                ? env.getProperty(prefixedKey)
                : env.getProperty(key);
        return StringUtils.hasText(property) ? property : defaultValue;
    }

    public String buildBoardParam(String gameName) {
        return StringUtils.hasText(gameName)
                ? "&gameName=" + gameName
                : "";
    }
}
