package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
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


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class TimerService implements Runnable {

    private static Logger logger = DLoggerFactory.getLogger(TimerService.class);

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;

    @Autowired
    private Dispatcher dispatcher;

    private volatile boolean paused;

    @Value("${score.update.time}")
    private long period;

    public void start() {
        paused = false;
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
            dispatcher.updateScores();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error while updating scores", e);
        }
    }

    public void pause() {
        if (logger.isDebugEnabled()) {
            logger.debug("Update score timer paused");
        }

        this.paused = true;
    }

    public void resume() {
        if (logger.isDebugEnabled()) {
            logger.debug("Update score timer started");
        }

        this.paused = false;
    }

    public boolean isPaused() {
        return this.paused;
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
