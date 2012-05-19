package net.tetris.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: serhiy.zelenin
 * Date: 5/9/12
 * Time: 6:36 PM
 */
public class TimerService implements Runnable {
    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private ScreenSender screenSender;

    public void init() {
        executor = new ScheduledThreadPoolExecutor(1);
        future = executor.scheduleAtFixedRate(this, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        playerService.nextStepForAllGames();
    }
}
