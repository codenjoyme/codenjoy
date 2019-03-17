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
import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.dao.*;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Igor_Petrov@epam.com
 * Created at 3/6/2019
 */
@Configuration
@SQLiteProfile
@RequiredArgsConstructor
public class SqliteDBConfig {

    private final ContextPathGetter contextPathGetter;

    @Value("${database.files.log}")
    private String logFileName;

    @Value("${database.files.saves}")
    private String savesFileName;

    @Value("${database.files.users}")
    private String usersFileName;

    @Value("${database.files.payment}")
    private String paymentFileName;

    @Value("${database.files.settings}")
    private String settingsFileName;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public ConnectionThreadPoolFactory databasePoolFactory() {
        return new SqliteConnectionThreadPoolFactory(logFileName, contextPathGetter);
    }

    @Bean
    public ActionLogger actionLogger() {
        return new ActionLogger(databasePoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory playerPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(savesFileName, contextPathGetter);
    }

    @Bean
    public PlayerGameSaver playerGameSaver() {
        return new PlayerGameSaver(playerPoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory registrationPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(usersFileName, contextPathGetter);
    }

    @Bean
    public Registration registration() {
        return new Registration(registrationPoolFactory(), passwordEncoder.encode(adminPassword));
    }

    @Bean
    public ConnectionThreadPoolFactory paymentPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(paymentFileName, contextPathGetter);
    }

    @Bean
    public Payment payment() {
        return new Payment(paymentPoolFactory());
    }

    @Bean
    public ConnectionThreadPoolFactory gameDataPoolFactory() {
        return new SqliteConnectionThreadPoolFactory(settingsFileName, contextPathGetter);
    }

    @Bean
    public GameData gameData() {
        return new GameData(gameDataPoolFactory());
    }
}
