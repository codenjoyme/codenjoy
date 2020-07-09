package com.codenjoy.dojo.web.security;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;

import static com.codenjoy.dojo.conf.Authority.ROLE_ADMIN;
import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

@AllArgsConstructor
@Component
public class SecurityContextAuthenticator {

    private AuthenticationManager authenticationManager;
    private ConfigProperties config;

    public void login(HttpServletRequest request, String email, String password) {
        SecurityContext context = SecurityContextHolder.getContext();
        if (isAdmin(context)) {
            return;
        }

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        Authentication auth = authenticationManager.authenticate(token);

        context.setAuthentication(auth);
        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private boolean isAdmin(SecurityContext context) {
        if (context.getAuthentication() == null) {
            return false;
        }

        Authentication authentication = context.getAuthentication();

        if (authentication instanceof UsernamePasswordAuthenticationToken) {
            UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
            if (token.getPrincipal() instanceof String) {
                return token.getPrincipal().equals(config.getAdminLogin()) &&
                        token.getCredentials().equals(config.getAdminPassword());
            }
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User)) {
            return false;
        }

        User user = (User) principal;
        if (user == null) {
            return false;
        }

        Collection<GrantedAuthority> authorities = user.getAuthorities();
        if (authorities == null) {
            return false;
        }

        return authorities.contains(ROLE_ADMIN.authority());
    }
}
