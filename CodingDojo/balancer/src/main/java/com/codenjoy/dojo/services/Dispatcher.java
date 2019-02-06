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

import com.codenjoy.dojo.services.dao.GameServer;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
public class Dispatcher {

    private static Logger logger = DLoggerFactory.getLogger(Dispatcher.class);

    @Autowired Players players;
    @Autowired Scores scores;
    @Autowired ConfigProperties config;
    @Autowired GameServers gameServers;
    @Autowired GameServer game;

    private volatile long lastTime;

    private Map<String, List<PlayerInfo>> scoresFromGameServers = new ConcurrentHashMap();
    private Map<String, List<PlayerScore>> currentScores = new ConcurrentHashMap();
    private volatile List<PlayerScore> currentFinalists = new LinkedList<>();

    private List<String> disqualified = new CopyOnWriteArrayList<>();

    @PostConstruct
    public void postConstruct() {
        // в случае если сегодня сервер потушен был
        lastTime = scores.getLastTime(now());
    }

    public ServerLocation registerNew(String email, String name, String password, String callbackUrl) {
        String server = gameServers.getNextServer();

        return registerOnServer(server, email, name, password, callbackUrl, "0", "{}");
    }

    public ServerLocation registerIfNotExists(String server, String email, String name, String code, String callbackUrl) {
        if (game.existsOnServer(server, email)) {
            return null;
        }

        // удалить с других серверов если там есть что
        gameServers.stream()
                .filter(s -> !s.equals(server))
                .filter(s -> game.existsOnServer(s, email))
                .forEach(s -> game.remove(s, email, code));

        Player player = players.get(email);
        String score = null; // будет попытка загрузиться с сейва
        String save = null;
        return registerOnServer(server, email, name, player.getPassword(), callbackUrl, score, save);
    }

    public ServerLocation registerOnServer(String server, String email, String name, String password, String callbackUrl, String score, String save) {
        if (logger.isDebugEnabled()) {
            logger.debug("User {} go to {}", email, server);
        }

        String code = game.createNewPlayer(server, email, name, password, callbackUrl, score, save);

        return new ServerLocation(
                email,
                config.getId(email),
                code,
                server
        );
    }

    public void updateScores() {
        List<PlayerInfo> players = gameServers.stream()
                .map(s -> getPlayersInfosCached(s))
                .collect(LinkedList::new, List::addAll, List::addAll);

        players.stream()
                .forEach(p -> p.setName(config.getEmail(p.getName())));

        long time = now();
        scores.saveScores(time, players);

        // теперь любой может пользоваться этим данными для считывания
        // внимание! тут нельзя ничего другого делать с переменной кроме как читать/писать
        currentScores.remove(scores.getDay(lastTime));
        lastTime = time;
    }

    private List<PlayerInfo> getPlayersInfosCached(String server) {
        try {
            List<PlayerInfo> result = game.getPlayersInfos(server);

            scoresFromGameServers.put(server, result);

            return result;
        } catch (RestClientException e) {
            logger.error("Error processing scores from server: " + server, e);

            if (scoresFromGameServers.containsKey(server)) {
                return scoresFromGameServers.get(server);
            } else {
                return Arrays.asList();
            }
        }
    }

    private long now() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public void disqualify(List<String> players) {
        disqualified.addAll(players);
    }

    public synchronized List<PlayerScore> getFinalists(int finalistsCount, String from, String to) {
        List<PlayerScore> cached = currentFinalists;
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<PlayerScore> scores = this.scores.getFinalists(from, to, lastTime, finalistsCount, disqualified);
        List<PlayerScore> result = prepareScoresForClient(scores);

        currentFinalists = result;
        return result;
    }

    public synchronized List<PlayerScore> getScores(String day) {
        List<PlayerScore> cached = currentScores.get(day);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<PlayerScore> scores = this.scores.getScores(day, lastTime);
        List<PlayerScore> result = prepareScoresForClient(scores);

        currentScores.put(day, result);
        return result;
    }

    private List<PlayerScore> prepareScoresForClient(List<PlayerScore> result) {
        List<String> emails = result.stream()
                .map(score -> score.getId())
                .collect(toList());

        Map<String, Player> playerMap = players.getPlayers(emails).stream()
                .collect(toMap(Player::getEmail, player -> player));

        result.forEach(score -> {
            String email = score.getId();
            Player player = playerMap.get(email);

            score.setId(config.getId(email));
            if (player != null) {
                score.setServer(player.getServer());
                score.setName(String.format("%s %s", player.getFirstName(), player.getLastName()));
            } else {
                score.setServer(null); // TODO убрать загрузку AI с турнира а так же фильтровать всех кто не попал в табличку
                score.setName(null);
            }
        });

        return result.stream()
                .filter(score -> score.getServer() != null)
                .collect(toList());
    }

    public List<String> clearScores() {
        return gameServers.stream()
            .map(s -> String.format("On server '%s' clear status is '%s'", s,
                    game.clearScores(s)))
            .collect(toList());
    }

    public List<String> gameEnable(boolean enable) {
        return gameServers.stream()
                .map(s -> String.format("On server '%s' enable status is '%s'", s,
                        game.gameEnable(s, enable)))
                .collect(toList());
    }


    public boolean exists(String email) {
        Player player = players.get(email);
        if (player == null) {
            return false;
        }
        return game.existsOnServer(player.getServer(), player.getEmail());
    }

    public void clearCache() {
        scoresFromGameServers.clear();
        currentScores.clear();
        disqualified.clear();
        currentFinalists.clear();
    }


    public List<String> disqualified() {
        return disqualified;
    }
}
