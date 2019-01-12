package com.codenjoy.dojo.services;

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

import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class Main {
    public static void main(String[] args) {
//        Dispatcher dispatcher = setup();

        // create client on game server
//        System.out.println(dispatcher.createNewPlayer(
//                dispatcher.getNextServer(),
//                "test@gmail.com",
//                "698d51a19d8a121ce581499d7b701668",
//                "callback"
//        ));

        // get scores from game server
//        System.out.println(dispatcher.getScores("2019-01-12"));

        // register user on balancer
        String email = "test@gmail.com";
        String firstName = "firstname1";
        String lastName = "lastname1";
        String password = "698d51a19d8a121ce581499d7b701668";
        String city = "Kyiv";
        String skills = "Si Senior";
        String comment = null;
        String code = null;
        String server = null;

        System.out.println(registerPlayer("127.0.0.1:8080",
                new Player(email, firstName,
                        lastName, password, city, skills, comment, code, server)));

        // login user on balancer
        System.out.println(loginPlayer("127.0.0.1:8080",
                new Player(email, password)));


    }

    private static ServerLocation registerPlayer(String server, Player player) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<ServerLocation> entity = rest.postForEntity(
                "http://" + server + "/codenjoy-balancer/rest/register",
                player,
                ServerLocation.class);

        return entity.getBody();
    }

    private static ServerLocation loginPlayer(final String server, Player player) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<ServerLocation> entity = rest.postForEntity(
                "http://" + server + "/codenjoy-balancer/rest/login",
                player,
                ServerLocation.class);

        return entity.getBody();
    }

    private static Dispatcher setup() {
        Dispatcher dispatcher = new Dispatcher();
        ConnectionThreadPoolFactory pool1 = new SqliteConnectionThreadPoolFactory(
                "balancer/database/scores.db", new ContextPathGetter(){
            @Override
            public String getContext() {
                return "ctx";
            }
        });
        dispatcher.scores = new Scores(pool1);
        ConnectionThreadPoolFactory pool2 = new SqliteConnectionThreadPoolFactory(
                "balancer/database/players.db", new ContextPathGetter(){
            @Override
            public String getContext() {
                return "ctx";
            }
        });
        dispatcher.players = new Players(pool2);
        return dispatcher;
    }
}
