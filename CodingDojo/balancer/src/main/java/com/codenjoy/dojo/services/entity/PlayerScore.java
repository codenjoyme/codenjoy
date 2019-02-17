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

    private String id;
    private String name;
    private int score;
    private String day;
    private String server;

    public PlayerScore() {
        // do nothing
    }

    public PlayerScore(String id, int score) {
        this.id = id;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public String getServer() {
        return server;
    }

    @Override
    public String toString() {
        return "PlayerScore{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ((day != null) ? (", day='" + day + '\'') : "") +
                ", score='" + score + '\'' +
                ", server='" + server + '\'' +
                '}';
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }
}
