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
import com.codenjoy.dojo.services.entity.DispatcherSettings;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.entity.server.PlayerDetailInfo;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.services.entity.server.User;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class Dispatcher {

    private static Logger logger = DLoggerFactory.getLogger(Dispatcher.class);

    @Autowired Players players;
    @Autowired Scores scores;

    private List<String> servers = new CopyOnWriteArrayList<>();
    private volatile DispatcherSettings settings;
    private volatile long lastTime;
    private volatile int currentServer;

    @PostConstruct
    public void postConstruct() {
        // в случае если сегодня сервер потушен был
        lastTime = scores.getLastTime(now());
    }

    public Dispatcher() {
        settings = new DispatcherSettings(
            "http://%s/codenjoy-contest/rest/player/create",
            "http://%s/codenjoy-contest/rest/player/%s/remove/%s",
            "http://%s/codenjoy-contest/rest/game/%s/players",
            "snakebattle",
            Arrays.asList("codenjoy.juja.com.ua")
        );
        servers.addAll(settings.getServers());
        currentServer = 0;
    }

    public ServerLocation register(Player player, String callbackUrl) {
        String server = getNextServer();

        if (logger.isDebugEnabled()) {
            logger.debug("User {} go to {}", player.getEmail(), server);
        }

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
                        settings.getGameType(),
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

    // несколько потоков могут параллельно регаться, и этот инкремент по кругу
    // должн быть многопоточнобезопасным
    private synchronized String getNextServer() {
        String result = servers.get(currentServer);
        currentServer++;
        if (currentServer >= servers.size()) {
            currentServer = 0;
        }
        return result;
    }

    public void updateScores() {
        List<PlayerInfo> playersInfos = servers.stream()
                .map(s -> getPlayersInfos(s))
                .collect(LinkedList::new, List::addAll, List::addAll);

        long time = now();
        playersInfos.forEach(it -> scores.saveScore(time, it.getName(), Integer.valueOf(it.getScore())));

        // теперь любой может пользоваться этим данными для считывания
        // внимание! тут нельзя ничего другого делать с перменной кроме как читать/писать
        lastTime = time;
    }

    private long now() {
        return Calendar.getInstance().getTimeInMillis();
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
        return String.format(settings.getUrlGetPlayers(),
                server,
                settings.getGameType());
    }

    private String createPlayerUrl(String server) {
        return String.format(settings.getUrlCreatePlayer(),
                server);
    }

    private String removePlayerUrl(String server, String email, String code) {
        return String.format(settings.getUrlRemovePlayer(),
                server,
                email,
                code);
    }

    public List<PlayerScore> getScores(String day) {
        List<PlayerScore> result = scores.getScores(day, lastTime);

        // TODO вот тут надо оптимизнуть хорошенько и не делать N+1 запрос
        result.forEach(score -> score.setServer(players.getServer(score.getEmail())));

        return result;
    }

    public Boolean remove(String server, String email, String code) {
        RestTemplate rest = new RestTemplate();
        ResponseEntity<Boolean> entity = rest.exchange(
                removePlayerUrl(server, email, code),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Boolean>(){});
        return entity.getBody();
    }

    public void saveSettings(DispatcherSettings settings) {
        this.settings = settings;

        if (settings.getServers() != null) {
            servers.clear();
            servers.addAll(settings.getServers());
        }
    }

    public DispatcherSettings getSettings() {
        return settings;
    }
}
