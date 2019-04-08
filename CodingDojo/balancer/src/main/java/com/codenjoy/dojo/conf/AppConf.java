package com.codenjoy.dojo.conf;

import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Igor_Petrov@epam.com
 * Created at 4/8/2019
 */
@Configuration
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
