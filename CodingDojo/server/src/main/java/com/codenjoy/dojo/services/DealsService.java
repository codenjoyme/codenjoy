package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2023 Codenjoy
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

import com.codenjoy.dojo.services.controller.Controller;
import com.codenjoy.dojo.services.hero.HeroData;
import com.codenjoy.dojo.services.multiplayer.LevelProgress;
import com.codenjoy.dojo.services.multiplayer.Sweeper;
import com.codenjoy.dojo.services.playerdata.PlayerData;
import com.codenjoy.dojo.transport.screen.ScreenData;
import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

@Slf4j
@Component
public class DealsService {

    private Map<Player, Pair> boards = new HashMap<>();

    @Autowired protected LockService lock;
    @Autowired protected Deals deals;
    @Autowired protected DealsView dealsView;

    @Autowired
    @Qualifier("playerController")
    protected Controller playerController;

    @Autowired
    @Qualifier("screenController")
    protected Controller screenController;

    @PostConstruct
    public void init() {
        deals.init(lock.get());
        deals.onAdd(deal -> {
            playerController.register(deal);
            screenController.register(deal);
        });
        deals.onRemove(deal -> {
            playerController.unregister(deal);
            screenController.unregister(deal);
        });
    }

    // TODO cant convert to rest because of consumer
    public void onAdd(Consumer<Deal> consumer) {
        deals.onAdd(consumer);
    }

    // TODO cant convert to rest because of consumer
    public void onRemove(Consumer<Deal> consumer) {
        deals.onRemove(consumer);
    }

    public Deal get(String id) {
        return deals.get(id);
    }

    // TODO cant convert to rest because of gameType
    public Deal deal(PlayerSave save, String room, String id, String callbackUrl, GameType gameType, long now) {
        deals.remove(id, Sweeper.on());
        return deals.deal(save, room, id, callbackUrl, gameType, now);
    }

    // TODO cant convert to rest because of predicate
    public List<Deal> getAll(Predicate<Deal> filter) {
        return deals.getAll(filter);
    }

    // TODO cant convert to rest because of sweeper
    public void remove(String id, Sweeper on) {
        deals.remove(id, on);
    }

    public void setLevel(String id, JSONObject jsonObject) {
        deals.setLevel(id, jsonObject);
    }

    public void changeRoom(String id, String game, String newRoom) {
        deals.changeRoom(id, game, newRoom);
    }

    // TODO cant convert to rest because of filter
    public Pair<Integer, Integer> tick(Predicate<Deal> filter) {
        deals.tick(filter);
        int screenUpdates = sendScreenUpdates();
        int requestControls = requestControls(filter);
        return Pair.of(screenUpdates, requestControls);
    }

    public void clear() {
        deals.clear();
    }

    public List<List<String>> getGroupsByField() {
        return dealsView.getGroupsByField();
    }

    public void reloadAll(boolean shuffle) {
        deals.reloadAll(shuffle);
    }

    public Map<String, Object> getScores() {
        return dealsView.getScores();
    }

    // TODO this method breaks encapsulation
    public List<Deal> all() {
        return deals.all();
    }

    public Map<String, List<String>> getGroupsMap() {
        return dealsView.getGroupsMap();
    }

    public boolean changeLevel(String id, int level) {
        return deals.changeLevel(id, level);
    }

    // TODO cant convert to rest because of deal and sweeper
    public void reload(Deal deal, Sweeper sweeper) {
        deals.reload(deal, sweeper);
    }

    public LevelProgress getLevel(String id) {
        return deals.getLevel(id);
    }

    public List<PScoresOf> getScoresForGame(String game) {
        return dealsView.getScoresForGame(game);
    }

    public List<PScoresOf> getScoresForRoom(String room) {
        return dealsView.getScoresForRoom(room);
    }

    // TODO cant convert to rest because of filter
    public void reloadAll(boolean shuffle, Predicate<Deal> filter) {
        deals.reloadAll(shuffle, filter);
    }

    public int size() {
        return deals.size();
    }

    // TODO cant convert to rest because of filter
    private int requestControls(Predicate<Deal> filter) {
        int requested = 0;

        for (Deal deal : getAll(filter)) {
            Player player = deal.getPlayer();
            try {
                Object clientBoard = boards.get(player).getRight();
                // TODO в конце концов если if (pair == null || pair.noSockets()) то
                //      ничего не отправляется, и зря гоняли но вроде как из кеша берем,
                //      так что проблем быть не должно.
                requested += playerController.requestControl(player, clientBoard);
            } catch (Exception e) {
                log.error("Unable to send control request to player " + player.getId() +
                        " URL: " + player.getCallbackUrl(), e);
            }
        }
        return requested;
    }

    private int sendScreenUpdates() {
        Map<ScreenRecipient, ScreenData> map = buildScreenData();
        return sendScreenForWebSockets(map);
    }

    private Map<ScreenRecipient, ScreenData> buildScreenData() {
        Map<ScreenRecipient, ScreenData> map = new HashMap<>();
        boards.clear();

        Map<String, GameData> gameDataMap = dealsView.getGamesDataMap();
        for (Deal deal : deals) {
            Game game = deal.getGame();
            Player player = deal.getPlayer();
            try {
                String gameType = deal.getGameType().name();
                GameData gameData = gameDataMap.get(player.getId());
                GuiPlotColorDecoder decoder = gameData.getDecoder();

                ImmutablePair data = decoder.buildBoards(game, player.getId());
                boards.put(player, data);
                Object screenBoard = data.getLeft();

                int boardSize = gameData.getBoardSize();
                Object score = player.getScore();
                String message = player.getMessage();
                Map<String, Object> scores = gameData.getScores();
                Map<String, Integer> teams = gameData.getTeams();
                List<String> group = gameData.getGroup();
                Map<String, HeroData> coordinates = gameData.getCoordinates();
                Map<String, String> readableNames = gameData.getReadableNames();
                map.put(player, new PlayerData(boardSize,
                        screenBoard,
                        gameType,
                        score,
                        message,
                        scores,
                        teams,
                        coordinates,
                        readableNames,
                        group));

            } catch (Exception e) {
                log.error("Unable to send screen updates to player " + player.getId() +
                        " URL: " + player.getCallbackUrl(), e);
                e.printStackTrace();
            }
        }

        return map;
    }

    private int sendScreenForWebSockets(Map<ScreenRecipient, ScreenData> map) {
        try {
            return screenController.requestControlToAll(map);
        } catch (Exception e) {
            log.error("Unable to send screen updates to all players", e);
            e.printStackTrace();
        }
        return 0;
    }

    // TODO cant convert to rest because of players result
    public List<Player> players() {
        return deals.players();
    }

    // TODO cant convert to rest because of players result
    public List<Player> getPlayersByRoom(String room) {
        return deals.getPlayersByRoom(room);
    }

    // TODO cant convert to rest because of players result
    public List<Player> getPlayersByGame(String game) {
        return deals.getPlayersByGame(game);
    }
}
