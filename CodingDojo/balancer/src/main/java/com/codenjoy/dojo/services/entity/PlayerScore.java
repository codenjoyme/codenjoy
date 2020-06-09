package com.codenjoy.dojo.services.entity;

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

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlayerScore {

    private String id;
    private String name;
    private int score;
    private String day;
    private String time;
    private String server;
    private boolean winner;

    public PlayerScore() {
        // do nothing
    }

    public PlayerScore(String id, int score, String time, boolean isWinner) {
        this.id = id;
        this.score = score;
        this.time = time;
        this.winner = isWinner;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", score=" + score +
                ", day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", server='" + server + '\'' +
                ", winner=" + winner +
                '}';
    }

    public void updateFrom(Player player) {
        if (player == null) {
            server = null;
            name = null;
            return;
        }

        server = player.getServer();
        name = player.getFullName();
    }
}
