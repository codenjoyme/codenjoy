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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Тут собраны только те проперти, которые важны в контроллерах.
 * Все дело в том, что я не хочу делать второго конфига который будет уметь
 * находить properties файлы вокруг приложения еще и в spring-context.xml
 * а это потому, что он не обрабатывается фильтрами maven при сборке в war.
 * Единственное место, где конфигурится *.properties - applicationContext.xml
 */
@Component
public class ConfigProperties {

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${email.hash}")
    private String emailHash;

    @Value("${dispatcher.url.create}")
    private String urlCreatePlayer;

    @Value("${dispatcher.url.remove}")
    private String urlRemovePlayer;

    @Value("${dispatcher.url.get}")
    private String urlGetPlayers;

    @Value("${dispatcher.url.clear}")
    private String urlClearScores;

    @Value("${game.type}")
    private String gameType;

    @Value("#{'${game.servers}'.split(',')}")
    private List<String> servers;

    public String getAdminPassword() {
        return adminPassword;
    }

    public String getEmailHash() {
        return emailHash;
    }

    public String getUrlCreatePlayer() {
        return urlCreatePlayer;
    }

    public String getUrlRemovePlayer() {
        return urlRemovePlayer;
    }

    public String getUrlGetPlayers() {
        return urlGetPlayers;
    }

    public String getUrlClearScores() {
        return urlClearScores;
    }

    public String getGameType() {
        return gameType;
    }

    public List<String> getServers() {
        return servers;
    }
}
