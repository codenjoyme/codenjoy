package com.codenjoy.dojo.services.dao;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.GameBoardData;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class BoardData {

    private CrudConnectionThreadPool pool;

    public BoardData(ConnectionThreadPoolFactory factory) {
        pool = factory.create(
                "CREATE TABLE IF NOT EXISTS board_data (" +
                        "game_name varchar(255)," +
                        "template_url varchar(255)," +
                        "welcome_text varchar(1025)," +
                        "image varchar(255));");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public void saveBoardGame(String game, String templateUrl, String welcomeText, String image) {
        pool.update("INSERT INTO board_data " +
                        "(game_name, template_url, welcome_text, image) " +
                        "VALUES (?,?,?,?);",
                new Object[]{
                        game,
                        templateUrl,
                        welcomeText,
                        image
                });
    }

    public List<String> getAllGames() {
        return pool.select("SELECT DISTINCT game_name FROM board_data;",
                rs -> {
                    List<String> result = new ArrayList<>();
                    while (rs.next()) {
                        String gameName = rs.getString("game_name");
                        result.add(gameName);
                    }
                    return result;
                }
        );
    }

    public Set<Map.Entry<String, String>> allGames() {
        return pool.select("SELECT game_name FROM board_data ",
                rs -> {
                    Set<Map.Entry<String, String>> result = new HashSet<>();
                    while (rs.next()) {
                        String name = rs.getString("game_name");
                        result.add(new AbstractMap.SimpleEntry<>(name, name));
                    }
                    return result;
                }
        );
    }

    public GameBoardData getInfoForGame(String gameName) {
        return pool.select("SELECT template_url, welcome_text, image FROM board_data " +
                        "WHERE game_name = ?",
                new Object[]{gameName},
                rs -> {
                    GameBoardData gameBoardData = new GameBoardData();
                    while (rs.next()) {
                        gameBoardData.setGameName(gameName);
                        gameBoardData.setTemplateUrl(rs.getString("template_url"));
                        gameBoardData.setWelcomeText(rs.getString("welcome_text"));
                        gameBoardData.setImage(rs.getString("image"));
                    }
                    return gameBoardData;
                }
        );
    }

    public String getTemplateURLForGame(String gameName) {
        return pool.select("SELECT template_url FROM board_data " +
                        "WHERE game_name = ?",
                new Object[]{gameName},
                rs -> {
                    if(rs.next()){
                        return rs.getString("template_url");
                    }else {
                        throw new IllegalArgumentException("Invalid game name");
                    }
                }
        );
    }
}
