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


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class AutoSaver extends Suspendable implements Tickable {

    @Value("${game.save.ticks}")
    private int ticks = 30;

    @Value("${game.save.load-on-start}")
    private boolean loadOnStart = true;

    private final SaveService save;

    private ExecutorService executor = Executors.newSingleThreadExecutor();

    private int count = 0;

    @Value("${game.save.auto}")
    public void setActive(boolean active) {
        super.setActive(active);
    }

    public int ticks() {
        return ticks;
    }

    @Override
    public void tick() {
        if (!active) {
            return;
        }

        if (loadOnStart) {
            loadOnStart = false;
            save.loadAll();
            java.awt.Toolkit.getDefaultToolkit().beep();
            return;
        }

        count++;
        if (count % ticks == (ticks - 1)) {
            // executor.submit потому что sqlite тормозит при сохранении
            executor.submit(() -> save.saveAll());
        }
    }
}
