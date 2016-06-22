package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class AutoSaver implements Tickable {

    public static final int TICKS = 30;

    @Autowired
    private SaveService saveService;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private boolean justStart = true;
    private int count = 0;

    @Override
    public void tick() {
        if (justStart) {
            justStart = false;
            saveService.loadAll();
        } else {
            count++;
            if (count % TICKS == (TICKS - 1)) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        saveService.saveAll();
                    }
                });
            }
        }
    }
}
