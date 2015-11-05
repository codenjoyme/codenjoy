package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Component
public class AutoSaver implements Tickable {

    @Autowired
    private SaveService saveService;

    @Autowired
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
            int saveOn = 30;
            if (count%saveOn == (saveOn - 1)) {
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        // TODO это очень тугодумная операция, потому она в отдельном потоке
                        saveService.saveAll();
                    }
                });
            }
        }
    }
}
