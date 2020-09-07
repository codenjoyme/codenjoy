package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Stream;

@Component
public class GameServers {

    @Autowired ConfigProperties config;

    private List<String> servers = new CopyOnWriteArrayList<>();
    private volatile int currentServer;
    private volatile int countRegistered;

    @PostConstruct
    public void postConstruct() {
        update(config.getGame().getServers());
    }

    // несколько потоков могут параллельно регаться, и этот инкремент по кругу
    // должeн быть многопоточнобезопасным
    public synchronized String getNextServer() {
        if (countRegistered++ % config.getGame().getRoom() == 0) {
            currentServer++;
            if (currentServer >= servers.size()) {
                currentServer = 0;
            }
        }

        return servers.get(currentServer);
    }

    public Stream<String> stream() {
        return servers.stream();
    }

    public void update(List<String> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("Game servers list is empty. Nothing to add");
        }

        servers.clear();
        servers.addAll(list);
    }
}
