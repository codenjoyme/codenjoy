package com.codenjoy.dojo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutoSaver implements Tickable {

    @Autowired
    private SaveService saveService;

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
                saveService.saveAll();
            }
        }
    }
}
