package com.codenjoy.dojo.services;

import org.springframework.stereotype.Component;

import java.io.File;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

/**
 * User: sanja
 * Date: 11.01.14
 * Time: 2:26
 */
@Component("actionLogger")
public class ActionLogger {

    private static final String DATABASE_FILE = "log.db";
    private Connection connection;

    public ActionLogger() {
        connection = null;
        Statement stmt = null;
        try {
            File file = new File(DATABASE_FILE);
            file.delete();

            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_FILE);
            connection.setAutoCommit(false);
            System.out.println("Opened database successfully");

            stmt = connection.createStatement();

            stmt.execute("CREATE TABLE player_boards (player_name varchar(255), game_type varchar(2000), score int, board varchar(255));");

            connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void log(PlayerGames playerGames) {
        for (PlayerGame playerGame : playerGames) {
            Statement stmt = null;
            try {
                Player player = playerGame.getPlayer();

                stmt = connection.createStatement();
                stmt.execute(String.format("INSERT INTO player_boards (player_name,game_type,score,board) VALUES ('%s','%s',%s,'%s');",
                        player.getName(),
                        player.getGameName(),
                        player.getScore(),
                        playerGame.getGame().getBoardAsString()));

                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public List<BoardLog> getAll() {
        List<BoardLog> logs = new LinkedList<BoardLog>();

        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            ResultSet resultSet = stmt.executeQuery("SELECT * FROM player_boards;");
            while (resultSet.next()) {
                logs.add(new BoardLog(resultSet));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return logs;
    }
}

