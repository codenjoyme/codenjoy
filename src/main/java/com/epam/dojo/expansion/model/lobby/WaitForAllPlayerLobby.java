package com.epam.dojo.expansion.model.lobby;

/*-
 * #%L
 * expansion - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 - 2017 EPAM
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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.Tickable;
import com.epam.dojo.expansion.model.GameFactory;
import com.epam.dojo.expansion.model.Player;
import com.epam.dojo.expansion.model.PlayerBoard;
import com.epam.dojo.expansion.model.levels.items.Hero;
import org.slf4j.Logger;

import java.util.*;

import static com.epam.dojo.expansion.services.SettingsWrapper.data;

/**
 * Created by Oleksandr_Baglai on 2017-09-11.
 */
public class WaitForAllPlayerLobby implements PlayerLobby, Tickable {

    private static Logger logger = DLoggerFactory.getLogger(WaitForAllPlayerLobby.class);

    private GameFactory factory;

    private List<Player> all = new LinkedList<>();
    private List<Player> waiting = new LinkedList<>();

    public WaitForAllPlayerLobby(GameFactory factory) {
        this.factory = factory;
    }

    @Override
    public void saveStateTo(PlayerLobby lobby) { // TODO this code smells
        if (lobby instanceof WaitForAllPlayerLobby) {
            WaitForAllPlayerLobby another = (WaitForAllPlayerLobby) lobby;
            another.all = new LinkedList<>(all);
            another.waiting = new LinkedList<>(waiting);
        } else if (lobby instanceof NotWaitPlayerLobby) {
            NotWaitPlayerLobby another = (NotWaitPlayerLobby) lobby;
            this.factory = another.factory;
            this.letThemGo();
            all.clear();
        }
    }

    @Override
    public void remove(Player player) {
        if (logger.isDebugEnabled()) {
            logger.debug("Removed player {} from Lobby", player.lg.id());
        }
        all.remove(player);
        waiting.remove(player);
    }

    @Override
    public void addPlayer(Player player) {
        if (logger.isDebugEnabled()) {
            logger.debug("Registered player {} on Lobby", player.lg.id());
        }
        all.add(player);
    }

    @Override
    public PlayerBoard start(Player player) {
        if (logger.isDebugEnabled()) {
            logger.debug("Player {} waits on Lobby", player.lg.id());
        }
        waiting.add(player);
        return new LobbyPlayerBoard(waiting) { // there are all important methods
            @Override
            public void remove(Player player) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Player {} removed from lobby", player.lg.id());
                }
                player.destroyHero();
                // because of after finish level we don't want
                // to null Hero, just destroy, but there we
                // should renew lobby Hero instance
                player.setHero(null);
            }

            @Override
            public void newGame(Player player) {
                Hero hero = new Hero() {
                    @Override
                    public Point getPosition() {
                        return NULL_POINT;
                    }

                    @Override
                    public void tick() {
                        // do nothing
                    }

                    @Override
                    public void message(String command) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("Player {} lobby hero wants to execute command {}",
                                    player.lg.id(), command);
                        }
                    }

                    @Override
                    public String toString() {
                        return "HLB@" + Integer.toHexString(this.hashCode());
                    }
                };
                player.setHero(hero);
                hero.setField(this);
            }
        };
    }

    @Override
    public void tick() {
        if (!isLetThemGo()) {
            return;
        }

        letThemGo();
    }

    private void letThemGo() {
        if (data.shufflePlayers()) {
            Collections.shuffle(waiting);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Players on Lobby will start new game {}", Player.lg(waiting));
        }
        PlayerBoard room = factory.newMultiple();
        for (Player p : waiting) {
            room.loadLevel(0);
            if (!room.isFree()) {
                room = factory.newMultiple();
            }
            p.setPlayerBoard(room);
        }
        waiting.clear();
    }

    private boolean isLetThemGo() {
        if (data.lobbyCapacity() == -1) {
            return all.size() != 1 && all.size() == waiting.size();
        } else {
            return waiting.size() >= data.lobbyCapacity();
        }
    }
}
