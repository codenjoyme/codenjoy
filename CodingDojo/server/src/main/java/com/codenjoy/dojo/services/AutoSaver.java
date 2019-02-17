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


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class AutoSaver extends Suspendable implements Tickable {

    public static final int TICKS = 30;

    @Autowired private SaveService save;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private boolean justStart = true;
    private int count = 0;

    @Value("${game.save.auto}")
    public void setActive(boolean active) {
        super.setActive(active);
    }

    @Override
    public void tick() {
        if (!active) {
            return;
        }

        if (justStart) {
            justStart = false;
            save.loadAll();
            java.awt.Toolkit.getDefaultToolkit().beep();
        } else {
            count++;
            if (count % TICKS == (TICKS - 1)) {
                // executor.submit потому что sqlite тормозит при сохранении
                executor.submit(() -> save.saveAll());
            }
        }
    }
}
