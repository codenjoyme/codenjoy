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

public class PlayerScore {

    private String day;
    private long time;
    private String email;
    private int score;

    public PlayerScore() {
        // do nothing
    }

    public PlayerScore(String day, long time, String email, int score) {
        this.day = day;
        this.time = time;
        this.email = email;
        this.score = score;
    }

    public String getDay() {
        return day;
    }

    public long getTime() {
        return time;
    }

    public String getEmail() {
        return email;
    }

    public int getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "day='" + day + '\'' +
                ", time='" + time + '\'' +
                ", email='" + email + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
