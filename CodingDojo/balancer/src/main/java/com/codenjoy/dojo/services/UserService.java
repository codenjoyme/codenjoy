package com.codenjoy.dojo.services;

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
        return predefinedUser;
    }

    public static User buildUserDetails(String username, String password, String... roles) {
        return new User(
                username,
                password,
                Stream.of(roles).map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
    }
}
