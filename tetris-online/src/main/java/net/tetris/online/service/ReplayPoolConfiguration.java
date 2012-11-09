package net.tetris.online.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * User: serhiy.zelenin
 * Date: 11/9/12
 * Time: 4:21 PM
 */
@Configuration("replayPoolConfiguration")
public class ReplayPoolConfiguration {

    @Bean(name = "replayExecutor")
    public ScheduledThreadPoolExecutor replayExecutor() {
        return new ScheduledThreadPoolExecutor(1);
    }
}
