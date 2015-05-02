package com.codenjoy.dojo.services.dao;

import com.codenjoy.dojo.services.jdbc.ObjectMapper;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPool;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

@Component
public class Payment {

    private SqliteConnectionThreadPool pool;

    public Payment(String dbFile) {
        pool = new SqliteConnectionThreadPool(dbFile,
                "CREATE TABLE IF NOT EXISTS payments (" +
                        "email varchar(255), " +
                        "game_type varchar(255), " +
                        "till int);");
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public boolean canPlay(final String email, final String gameType) {
        return pool.select("SELECT till FROM payments WHERE email = ? AND game_type = ?;",
                new Object[]{email, gameType},
                new ObjectMapper<Boolean>() {
                    @Override
                    public Boolean mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getLong("till") > Calendar.getInstance().getTime().getTime();
                        } else {
                            return false;
                        }
                    }
                }
        );
    }

    public void buy(final String email, final String gameType, int days) {
        final Calendar calendar = Calendar.getInstance();
        long till = till(email, gameType);
        if (till != 0) {
            calendar.setTimeInMillis(till);
        }
        calendar.add(Calendar.DAY_OF_MONTH, days);

        if (till == 0) {
            pool.update("INSERT INTO payments (email, game_type, till) VALUES (?,?,?);",
                    new Object[]{email, gameType, calendar.getTime().getTime()});
        } else {
            pool.update("UPDATE payments SET till = ? WHERE email = ? AND game_type = ?;",
                    new Object[]{calendar.getTime().getTime(), email, gameType});
        }
    }

    public long till(String email, String gameType) {
        return pool.select("SELECT till FROM payments WHERE email = ? AND game_type = ?;",
                new Object[]{email, gameType},
                new ObjectMapper<Long>() {
                    @Override
                    public Long mapFor(ResultSet resultSet) throws SQLException {
                        if (resultSet.next()) {
                            return resultSet.getLong("till");
                        } else {
                            return 0L;
                        }
                    }
                }
        );
    }
}
