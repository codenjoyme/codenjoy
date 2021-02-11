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

import com.codenjoy.dojo.services.GameProperties;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Igor Petrov
 * Created at 4/8/2019
 */
@Configuration
@EnableConfigurationProperties(GameProperties.class)
public class AppConf {

    @Bean
    public ScheduledThreadPoolExecutor restSenderExecutorService(@Value("${sender.pool.size}") int poolSize) {
        return new ScheduledThreadPoolExecutor(poolSize);
    }

    @Bean
    public Scores scores(@Qualifier("scoresPoolFactory") ConnectionThreadPoolFactory scoresPoolFactory) {
        return new Scores(scoresPoolFactory);
    }

    @Bean
    public Players players(@Qualifier("playersPoolFactory") ConnectionThreadPoolFactory playersPoolFactory) {
        return new Players(playersPoolFactory);
    }
}
