package com.codenjoy.dojo.services.multiplayer;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Game;
import com.codenjoy.dojo.services.settings.SettingsReader;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.services.round.RoundSettings.Keys.ROUNDS_TEAMS_PER_ROOM;

public class GameRoom {

    private final GameField field;
    private final int count;
    private final String name;
    private int wasCount;
    private final boolean disposable;
    private List<Deal> deals = new LinkedList<>();

    public GameRoom(String name, GameField field, int count, boolean disposable) {
        this.name = name;
        this.field = field;
        this.count = count;
        this.disposable = disposable;
    }

    public GameField join(Deal deal) {
        if (!containsDeal(deal)) {
            wasCount++;
            deals.add(deal);
        }
        return field;
    }

    public boolean isAvailable(Deal deal) {
        // TODO подумать тут
        SettingsReader settings = deal.getGame().getPlayer().settings;

        if (!isFree()) {
            // we have no free space
            // forbid a new player
            return false;
        }
        if (settings == null || !settings.hasParameter(ROUNDS_TEAMS_PER_ROOM.key())) {
            // somehow we don't have mandatory settings
            // that we need for further checks
            // let's allow the player
            return true;
        }
        int teams = settings.integer(ROUNDS_TEAMS_PER_ROOM);
        if (teams == 1) {
            // it's not a team-vs-team game type
            // as we have free space
            // let's allow the player
            return true;
        }
        if (!containsTeam(deal.getTeamId()) && countTeams() >= teams) {
            // it's a team-vs-team game type
            // but the player team cannot be added
            // this room contains max amount of teams
            // forbid a new player
            return false;
        }
        if (countMembers(deal.getTeamId()) >= (count / teams) + (count % teams)) {
            // we count player team members
            // it reaches the max value
            // even with the idea of disbalance
            // forbid a new player
            return false;
        }
        // there are no more checks
        // let's allow the player
        return true;
    }

    public boolean isFree() {
        if (disposable) {
            return wasCount < count;
        } else {
            return countPlayers() < count;
        }
    }

    public boolean isEmpty() {
        return deals.isEmpty();
    }

    public boolean isStuffed() {
        if (disposable) {
            return wasCount == count;
        } else {
            return true;
        }
    }

    public boolean containsPlayer(String playerId) {
        if (playerId == null) {
            return false;
        }
        return deals.stream()
                .anyMatch(deal -> deal.getPlayerId().equals(playerId));
    }

    public boolean containsDeal(Deal deal) {
        return deals.contains(deal);
    }

    public boolean containsTeam(int teamId) {
        return deals.stream()
                .anyMatch(deal -> deal.getTeamId() == teamId);
    }

    public String name() {
        return name;
    }

    public int countTeams() {
        return (int) deals.stream()
                .map(Deal::getGame)
                .map(Game::getPlayer)
                .map(GamePlayer::getTeamId)
                .distinct()
                .count();
    }

    public int countPlayers() {
        return deals.size();
    }

    public int countMembers(int teamId) {
        return (int) deals.stream()
                .filter(deal -> deal.getTeamId() == teamId)
                .distinct()
                .count();
    }

    public boolean isFor(GameField input) {
        return field.equals(input);
    }

    public Collection<Deal> deals() {
        return deals;
    }

    /**
     * @param deal Игрок который закончил играть в этой room и будет удален
     * @param sweeper поможет сообщить готовить ли некоторых оставшихся
     *        в комнате к удалению (если явно не указано wantToStay)
     * @return Все игроки этой комнаты, которых так же надо пристроить в новой room,
     *         т.к. им тут оставаться нет смысла
     */
    public List<Deal> remove(Deal deal, Sweeper sweeper) {
        List<Deal> removed = new LinkedList<>();

        deals.remove(deal);

        deals.forEach(last -> {
            if (sweeper.getApplicants().test(last, deals)) {
                if (!last.getGame().getPlayer().wantToStay()) {
                    removed.add(last);
                }
            }
        });

        deals.removeAll(removed);

        return removed;
    }

    public GameField field() {
        return field;
    }

}
