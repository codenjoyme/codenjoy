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

import com.codenjoy.dojo.config.meta.PostgreSQLProfile;
import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.dao.*;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.PostgreSQLConnectionThreadPoolFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Igor Petrov
 * Created at 3/6/2019
 */
@Configuration
@PostgreSQLProfile
public class PostgresDBConfig {

    @Value("${database.host}:${database.port}/${database.name:postgres}?user=${database.user}&password=${database.password}")
    private String jdbcString;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfigProperties properties;

    @Bean
    public ConnectionThreadPoolFactory connectionThreadPollFactory() {
        return new PostgreSQLConnectionThreadPoolFactory(jdbcString);
    }

    @Bean
    public ActionLogger actionLogger() {
        return new ActionLogger(connectionThreadPollFactory());
    }

    @Bean
    public PlayerGameSaver playerGameSaver() {
        return new PlayerGameSaver(connectionThreadPollFactory());
    }

    @Bean
    public Registration registration(@Value("${admin.login}") String adminLogin,
                                     @Value("${admin.password}") String adminPassword) {
        return new Registration(connectionThreadPollFactory(), adminLogin, adminPassword,
                passwordEncoder, properties, true);
    }

    @Bean
    public Payment payment() {
        return new Payment(connectionThreadPollFactory());
    }

    @Bean
    public GameData gameData() {
        return new GameData(connectionThreadPollFactory());
    }
}
