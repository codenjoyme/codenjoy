package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.dao.Players;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserService implements UserDetailsService {

    private final Map<String, User> predefinedUsers;
    private final Players players;

    public UserService(Map<String, User> predefinedUsers, Players players) {
        this.predefinedUsers = predefinedUsers;
        this.players = players;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User predefinedUser = predefinedUsers.get(username);
        if (predefinedUser == null) {
            return Optional.ofNullable(players.get(username))
                    .map(player -> buildUserDetails(player.getEmail(), player.getPassword(), "ROLE_USER"))
                    .orElse(null);
        }
        return new User(predefinedUser.getUsername(), predefinedUser.getPassword(), predefinedUser.getAuthorities());
    }

    public static User buildUserDetails(String username, String password, String... roles) {
        return new User(
                username,
                password,
                Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
