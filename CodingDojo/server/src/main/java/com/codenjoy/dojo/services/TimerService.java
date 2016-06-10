package com.codenjoy.dojo.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TimerService implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(TimerService.class);

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;

    @Autowired
    private PlayerService playerService;

    private volatile boolean paused;
    private long period;

    public void init() {
        executor = new ScheduledThreadPoolExecutor(1);
        schedule();
    }

    private void schedule() {
        future = executor.scheduleAtFixedRate(this, period, period, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        if (paused) {
            return;
        }

        try {
            playerService.tick();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while processing next step", e);
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public void setPeriod(long period) {
        this.period = period;
    }

    public void changePeriod(long period) {
        this.period = period;
        if (period > 0){
            if (future != null){
                future.cancel(true);
            }

            schedule();
        }
    }

    public long getPeriod() {
        return period;
    }
}