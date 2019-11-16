package com.codenjoy.dojo.excitebike.model;

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


import com.codenjoy.dojo.excitebike.model.items.Bike;
import com.codenjoy.dojo.services.EventListener;
import com.codenjoy.dojo.services.Point;
import com.codenjoy.dojo.services.multiplayer.GamePlayer;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;
import java.util.Random;

public class Player extends GamePlayer<Bike, GameField> {

    private final String name;
    private Bike bike;

    public Player(EventListener listener, String playerName) {
        super(listener);
        this.name = StringUtils.isNotEmpty(playerName) ? playerName : "player" + new Random().nextLong();
    }

    public Bike getHero() {
        return bike;
    }

    public void setHero(Bike bike) {
        this.bike = bike;
    }

    @Override
    public void newHero(GameField gameField) {
        if (bike != null) {
            gameField.removeFallenBike(bike);
        }
        Point freePosition = gameField.findFreePosition();
        bike = new Bike(freePosition, name);
        bike.init(gameField);
    }

    @Override
    public boolean isAlive() {
        return bike != null && bike.isAlive();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return name.equals(player.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Player{" +
                "name=" + name +
                ", bike=" + bike +
                ", listener=" + listener +
                '}';
    }
}
