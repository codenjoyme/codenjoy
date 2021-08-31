package com.codenjoy.dojo.lemonade.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import com.codenjoy.dojo.lemonade.services.GameSettings;
import com.codenjoy.dojo.services.RandomDice;
import com.codenjoy.dojo.services.multiplayer.GameField;
import com.codenjoy.dojo.services.printer.BoardReader;

import java.util.LinkedList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Lemonade implements GameField<Player> {

    private List<Player> players;
    private static RandomDice dice;
    private static int lastRandomSeed;
    private static int newRandomSeed;

    private GameSettings settings;

    static {
        dice = new RandomDice();
    }

    public Lemonade(GameSettings settings) {
        this.settings = settings;
        players = new LinkedList<>();
    }

    @Override
    public void tick() {
        // on first tick enable new random seed generation
        if (lastRandomSeed != newRandomSeed){
            lastRandomSeed = newRandomSeed;
        }

        for (Player player : players) {
            player.checkAnswer();
        }
    }

    @Override
    public void clearScore() {
        // it is supposed, we call cleanScore synchronously, so update seed on first call
        if(lastRandomSeed == newRandomSeed)
            newRandomSeed = dice.next(Integer.MAX_VALUE);

        if(players != null && players.size() > 0){
            for (Player player : players)
            {
                player.updateSeed(newRandomSeed);
                player.newHero(this);
            }
        }
    }

    @Override
    public GameSettings settings() {
        return settings;
    }

    public List<Hero> getHeroes() {
        return players.stream()
                .map(Player::getHero)
                .collect(toList());
    }

    @Override
    public BoardReader reader() {
        return null;
    }

    @Override
    public void newGame(Player player) {
        if (!players.contains(player)) {
            players.add(player);
        } else {
            player.clearScore();
        }
        player.newHero(this);
    }

    @Override
    public void remove(Player player) {
        players.remove(player);
    }
}
