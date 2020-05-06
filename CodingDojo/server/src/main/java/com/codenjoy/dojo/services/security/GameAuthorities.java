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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.codenjoy.dojo.services.security.GameAuthoritiesConstants.ROLE_ADMIN;
import static com.codenjoy.dojo.services.security.GameAuthoritiesConstants.ROLE_USER;
import static java.util.stream.Collectors.toList;

public enum GameAuthorities {

    ADMIN(ROLE_ADMIN, ROLE_USER),
    USER(ROLE_USER);

    GameAuthorities(String... roles) {
        this.roles = Stream.of(roles).collect(Collectors.toSet());
    }

    private Set<String> roles;

    public Collection<? extends GrantedAuthority> authorities() {
        return toGranted(this.roles);
    }

    public Set<String> roles() {
        return roles;
    }

    public static String joinRoles(Collection<String> roles) {
        return String.join(",", roles);
    }

    public static List<String> splitRoles(String roles) {
        return Stream.of(roles.split(","))
                .map(String::trim)
                .collect(toList());
    }

    public static Collection<? extends GrantedAuthority> toGranted(Collection<String> roles) {
        return roles.stream()
                .map(role -> (GrantedAuthority) () -> role)
                .collect(toList());
    }

    public static String toRoles(Collection<GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private Collection<? extends GrantedAuthority> toGranted(Set<String> roles) {
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(toList());
    }
}
