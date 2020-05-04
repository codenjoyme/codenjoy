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


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class TimerService implements Runnable {

    private ScheduledThreadPoolExecutor executor;
    private ScheduledFuture<?> future;

    @Autowired
    private PlayerService playerService;

    private volatile boolean paused;
    private volatile long period;

    public void start() {
        period = 1000;
        paused = true;
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
            log.error("Error while processing next step", e);
        }
    }

    public void pause() {
        this.paused = true;
    }

    public void resume() {
        this.paused = false;
    }

    public boolean isPaused() {
        return paused;
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
