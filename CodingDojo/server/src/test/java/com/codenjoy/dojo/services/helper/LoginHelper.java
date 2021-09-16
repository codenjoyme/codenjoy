package com.codenjoy.dojo.services.helper;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.nullobj.NullDeal;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.security.GameAuthorities;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.codenjoy.dojo.stuff.SmartAssert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.reset;

@AllArgsConstructor
public class LoginHelper {

    private ConfigProperties config;
    private PlayerService players;
    private Registration registration;
    private Deals deals;

    public void removeAll() {
        players.removeAll();
        registration.removeAll();
    }

    public void asAdmin() {
        login(new UsernamePasswordAuthenticationToken(
                config.getAdminLogin(),
                Hash.md5(config.getAdminPassword()))
        );
    }

    public void asUser(String playerId, String password) {
        Player player = players.get(playerId);
        if (player == NullPlayer.INSTANCE) {
            fail("Expected: Player with id = " + playerId +
                    " But was: NullPlayer");
        }

        Registration.User user = registration.getUserById(playerId).orElse(null);
        if (user == null) {
            fail("Expected: Registered user with id = " + playerId +
                    " But was: Registration not found");
        }


        login(new UsernamePasswordAuthenticationToken(
                user,
                Hash.md5(password)
        ));
    }

    private void login(UsernamePasswordAuthenticationToken token) {
        SecurityContextHolder.getContext().setAuthentication(token);
    }

    public void asNone() {
        login(null);
    }

    public Deal register(String id, String ip, String room, String game) {
        String password = Hash.md5(id);
        String readableName = id + "-name";
        registration.register(id, id, readableName, password, "", GameAuthorities.USER.roles());
        players.register(id, game, room, ip);
        Deal deal = deals.get(id);
        if (deal == NullDeal.INSTANCE) {
            registration.remove(id); // удаляем если не можем создать
        } else {
            resetMocks(deal);
        }
        return deal;
    }

    private void resetMocks(Deal deal) {
        reset(deal.getField());
        reset(deal.getGame().getPlayer());
    }

    public void assertPlayerInRoom(String id, String room) {
        Player player = players.get(id);
        assertEquals(room, player.getRoom());
    }

    public void join(String id, String room) {
        Player player = new Player(id);
        player.setRoom(room);
        players.update(player);
    }
}
