package com.codenjoy.dojo.config;

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

import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.dao.*;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Igor Petrov
 * Created at 3/6/2019
 */
@Configuration
@SQLiteProfile
@RequiredArgsConstructor
public class SqliteDBConfig {

    private final ContextPathGetter contextPathGetter;
    private final SQLiteFilesProperties database;
    private final PasswordEncoder passwordEncoder;
    private final ConfigProperties properties;

    @Bean
    public ConnectionThreadPoolFactory databasePoolFactory() {
        return new SqliteConnectionThreadPoolFactory(
                database.isMemory(),
                database.getFiles().getLog(),
                contextPathGetter
        );
    }

    @Bean
    public ActionLogger actionLogger() {
        return new ActionLogger(databasePoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory playerPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(
                database.isMemory(),
                database.getFiles().getSaves(),
                contextPathGetter
        );
    }

    @Bean
    public PlayerGameSaver playerGameSaver() {
        return new PlayerGameSaver(playerPoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory registrationPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(
                database.isMemory(),
                database.getFiles().getUsers(),
                contextPathGetter
        );
    }

    @Bean
    public Registration registration(@Value("${admin.login}") String adminLogin,
                                     @Value("${admin.password}") String adminPassword) {
        return new Registration(registrationPoolFactory(), adminLogin, adminPassword,
                passwordEncoder, properties, true);
    }

    @Bean
    public ConnectionThreadPoolFactory paymentPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(
                database.isMemory(),
                database.getFiles().getPayment(),
                contextPathGetter
        );
    }

    @Bean
    public Payment payment() {
        return new Payment(paymentPoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory gameDataPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(
                database.isMemory(),
                database.getFiles().getSettings(),
                contextPathGetter
        );
    }

    @Bean
    public GameData gameData() {
        return new GameData(gameDataPoolFactory());
    }
}
