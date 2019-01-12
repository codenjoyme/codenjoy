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
import com.codenjoy.dojo.services.entity.server.PlayerDetailInfo;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.entity.server.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Dispatcher {

    @Autowired private Players players;
    @Autowired private Scores scores;

    private List<String> servers = new CopyOnWriteArrayList<>();
    private String urlCreatePlayer;
    private String urlGetPlayers;
    private String gameType;

    public Dispatcher() {
        // TODO move to admin
        urlGetPlayers = "http://%s/codenjoy-contest/rest/game/%s/players";
        urlCreatePlayer = "http://%s/codenjoy-contest/rest/player/create";
        gameType = "snakebattle";
        servers.add("codenjoy.juja.com.ua");
//        servers.add("server2.codenjoy.juja.com.ua");
//        servers.add("server3.codenjoy.juja.com.ua");
    }

    public ServerLocation register(Player player, String callbackUrl) {
        String server = getNextServer();

        String code = createNewPlayer(
                server,
                player.getEmail(),
                player.getPassword(),
                callbackUrl);

        return new ServerLocation(player.getEmail(), code, server);
    }

    private String createNewPlayer(String server, String email,
                                   String password, String callbackUrl)
    {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<String> entity = rest.postForEntity(
                createPlayerUrl(server),
                new PlayerDetailInfo(
                        email,
                        callbackUrl,
                        gameType,
                        "0",
                        "{}",
                        new User(
                                email,
                                1,
                                password,
                                null,
                                null)

                ),
                String.class);
        return entity.getBody();
    }

    private String getNextServer() {
        return servers.get(0); // TODO impelment me
    }

    public void updateScores() {
        List<PlayerInfo> playersInfos = servers.stream()
                .map(s -> getPlayersInfos(s))
                .collect(LinkedList::new, List::addAll, List::addAll);

        long time = Calendar.getInstance().getTimeInMillis();
        playersInfos.forEach(it -> scores.saveScore(time, it.getName(), Integer.valueOf(it.getScore())));
    }

    private List<PlayerInfo> getPlayersInfos(String server) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<List<PlayerInfo>> entity = rest.exchange(
                getPlayersUrl(server),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PlayerInfo>>(){});
        return entity.getBody();
    }

    private String getPlayersUrl(String server) {
        return String.format(urlGetPlayers,
                server,
                gameType);
    }

    private String createPlayerUrl(String server) {
        return String.format(urlCreatePlayer,
                server);
    }

    public static void main(String[] args) {
        Dispatcher dispatcher = new Dispatcher();
        System.out.println(dispatcher.createNewPlayer(dispatcher.getNextServer(), "apofig@gmail.com", "342859181532890275408", "127.0.0.2"));
    }
}
