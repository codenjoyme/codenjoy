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
import com.codenjoy.dojo.services.entity.server.Disqualified;
import com.codenjoy.dojo.services.entity.server.PParameters;
import com.codenjoy.dojo.services.entity.server.PlayerInfo;
import com.codenjoy.dojo.web.rest.dto.GameSettings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Component
public class Dispatcher {

    private static Logger logger = DLoggerFactory.getLogger(Dispatcher.class);

    @Autowired Players players;
    @Autowired Scores scores;
    @Autowired ConfigProperties config;
    @Autowired GameServers gameServers;
    @Autowired GameServer game;
    @Autowired TimerService timer;

    private volatile long lastTime;

    private Map<String, List<PlayerInfo>> scoresFromGameServers = new ConcurrentHashMap();
    private Map<String, List<PlayerScore>> currentScores = new ConcurrentHashMap();
    private volatile List<PlayerScore> currentFinalists = new LinkedList<>();

    private Set<Disqualified> disqualified = new CopyOnWriteArraySet<>();

    @PostConstruct
    public void postConstruct() {
        // в случае если сегодня сервер потушен был
        lastTime = scores.getLastTime(now());
    }

    public Player registerNew(Player player) {
        String server = gameServers.getNextServer();
        player.setServer(server);

        Player created = registerOnServer(player, "0", "{}");
        players.updateServer(player.getId(), created.getServer(), created.getCode());
        return created;
    }

    public Player registerIfNotExists(Player player) {
        if (isFinalDay() && !isFinalist(player)) {
            removeFromEveryGameServer(player.getId());
            return null;
        }

        if (game.existsOnServer(player.getServer(), player.getId())) {
            return null;
        }

        // TODO test me
        removeFromEveryGameServer(player.getId());

        String score = null; // будет попытка загрузиться с сейва
        String save = null;
        return registerOnServer(player, score, save);
    }

    private boolean isFinalDay() {
        String today = scores.getDay(Calendar.getInstance().getTimeInMillis());
        return config.getGame().getEndDay().equals(today);
    }

    private boolean isFinalist(Player player) {
        return getFinalists().stream()
                .anyMatch(finalist -> player.getId().equals(finalist.getId()));
    }

    public Map<String, Boolean> removeFromEveryGameServer(String id) {
        // удалить с других серверов если там есть что
        return gameServers.stream()
                .filter(s -> game.existsOnServer(s, id))
                .collect(Collectors.toMap(
                            s -> s,
                            s -> game.remove(s, id)));
    }

    public Map<String, Boolean> existsOnGameServers(String id) {
        // удалить с других серверов если там есть что
        return gameServers.stream()
                .collect(Collectors.toMap(
                        s -> s,
                        s -> game.existsOnServer(s, id)));
    }

    public Player registerOnServer(Player player, String score, String save) {
        if (logger.isDebugEnabled()) {
            logger.debug("User {} go to {}", player.getEmail(), player.getServer());
        }

        String code = game.createNewPlayer(
                player.getServer(),
                player.getId(),
                player.getCode(),
                player.getEmail(),
                player.getPhone(),
                player.getFullName(),
                player.getPassword(),
                player.getCallback(),
                score,
                save
        );

        if (!StringUtils.isEmpty(code) && !code.equals(player.getCode())) {
            player.setCode(code); // TODO этого не должно случиться
        }

        return player;
    }

    public void updateScores() {
        List<PlayerInfo> players = gameServers.stream()
                .map(s -> getPlayersInfosCached(s))
                .collect(LinkedList::new, List::addAll, List::addAll);

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

    public void disqualify(List<String> emailsOrIds) {
        List<Disqualified> list = emailsOrIds.stream()
                .map(emailOrId -> parse(emailOrId))
                .filter(disqualified -> disqualified != null)
                .collect(toList());

        disqualified.addAll(list);
    }

    private Disqualified parse(String emailOrId) {
        String email = emailOrId;
        String id = emailOrId;

        if (emailOrId.contains("@")) {
            Player player = players.getByEmail(email);
            if (player == null) {
                return null;
            }

            id = player.getId();
        } else {
            Player player = players.get(id);
            if (player == null) {
                return null;
            }

            email = player.getEmail();
        }

        return new Disqualified(id, email);
    }

    public synchronized List<PlayerScore> getFinalists() {
        List<PlayerScore> cached = currentFinalists;
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<PlayerScore> result = loadFinalists();

        currentFinalists = result;
        return result;
    }

    public List<PlayerScore> markWinners() {
        List<PlayerScore> result = loadFinalists();

        scores.cleanWinnerFlags();

        result.forEach(finalist -> {
            scores.setWinnerFlag(finalist, true);
            finalist.setWinner(true);
        });

        currentScores.clear();
        currentFinalists.clear();

        return result;
    }

    private List<PlayerScore> loadFinalists() {
        List<PlayerScore> list = scores.getFinalists(
                config.getGame().getStartDay(),
                config.getGame().getEndDay(),
                config.getGame().getFinalistsCount(),
                disqualifiedIds()
        );

        List<PlayerScore> result = prepareScoresForClient(list);
        return result;
    }

    public synchronized List<PlayerScore> getScores(String day) {
        List<PlayerScore> cached = currentScores.get(day);
        if (cached != null && !cached.isEmpty()) {
            return cached;
        }

        List<PlayerScore> list = scores.getScores(day, getNow());
        List<PlayerScore> result = prepareScoresForClient(list);

        currentScores.put(day, result);
        return result;
    }

    // иначе (если юзать только lastTime) в момент, когда берем сегодня утром getScores
    // вчерашнего дня, а сервер был на паузе все время со вчерашнего вечера до утра сегодня,
    // то там будут невалидные данные - последние за вчера (когда стопнули серве),
    // но не около 19:00 как было бы если сервер не тушили и lastTime согла обновиться в now()
    private long getNow() {
        return (timer.isPaused()) ? now() : lastTime;
    }

    private List<PlayerScore> prepareScoresForClient(List<PlayerScore> result) {
        List<String> ids = result.stream()
                .map(score -> score.getId())
                .collect(toList());

        Map<String, Player> playerMap = players.getPlayersMap(ids);

        result.forEach(score -> score.updateFrom(playerMap.get(score.getId())));

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

    public boolean exists(Player player) {
        return game.existsOnServer(player.getServer(), player.getId());
    }

    public void clearCache(int whatToClean) {
        if ((whatToClean & 0b0001) == 0b0001) {
            scoresFromGameServers.clear();
        }

        if ((whatToClean & 0b0010) == 0b0010) {
            currentScores.clear();
        }

        if ((whatToClean & 0b0100) == 0b0100) {
            disqualified.clear();
        }

        if ((whatToClean & 0b1000) == 0b1000) {
            currentFinalists.clear();
        }
    }

    public Collection<Disqualified> disqualified() {
        return disqualified;
    }

    private List<String> disqualifiedIds() {
        return disqualified.stream()
                .map(Disqualified::getId)
                .collect(toList());
    }

    public List<GameSettings> getGameSettings() {
        return gameServers.stream()
                .map(server -> new GameSettings(game.getGameSettings(server)))
                .collect(toList());
    }

    public void updateGameSettings(GameSettings input) {
        PParameters current = getGameSettings().iterator().next().parameters();

        input.update(current.getParameters());

        gameServers.stream()
                .forEach(server -> game.setGameSettings(server, current));
    }
}
