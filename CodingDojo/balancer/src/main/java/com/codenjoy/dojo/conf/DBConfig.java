package com.codenjoy.dojo.conf;

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

import com.codenjoy.dojo.conf.meta.PostgreSQLProfile;
import com.codenjoy.dojo.conf.meta.SQLiteProfile;
import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.PostgreSQLConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/8/2019
 */
@Configuration
public class DBConfig {

    @Configuration
    @SQLiteProfile
    public static class SQLiteConf {

        @Bean
        public ConnectionThreadPoolFactory scoresPoolFactory(@Value("${database.scores}") String scoresFile,
                                                             ContextPathGetter contextPathGetter) {
            return new SqliteConnectionThreadPoolFactory(scoresFile, contextPathGetter);
        }

        @Bean
        public ConnectionThreadPoolFactory playersPoolFactory(@Value("${database.players}") String playersFile,
                                                              ContextPathGetter contextPathGetter) {
            return new SqliteConnectionThreadPoolFactory(playersFile, contextPathGetter);
        }
    }

    @Configuration
    @PostgreSQLProfile
    public static class PostgresConf {

        @Value("${database.url}/${database.name}?user=${database.user}&amp;password=${database.password}")
        private String jdbcString;

        @Bean
        public ConnectionThreadPoolFactory postgresPoolFactory() {
            return new PostgreSQLConnectionThreadPoolFactory(jdbcString);
        }

        @Bean
        public ConnectionThreadPoolFactory scoresPoolFactory() {
            return postgresPoolFactory();
        }

        @Bean
        public ConnectionThreadPoolFactory playersPoolFactory() {
            return postgresPoolFactory();
        }

    }
}
