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

@Slf4j
@Component
public class SimpleProfiler {

    private long time;
    private String message;

    @Autowired
    private PlayerGames playerGames;

    public synchronized void start(String message) {
        this.message = message;

        log.debug("==================================================================================");
        log.debug(message + " starts");
        time = System.currentTimeMillis();
    }

    public synchronized void end() {
        if (log.isDebugEnabled()) {
            time = System.currentTimeMillis() - time;
            log.debug(message + " for all {} games is {} ms",
                    playerGames.size(), time);
        }

    }
}