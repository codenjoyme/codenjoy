package com.codenjoy.dojo.services.httpclient;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.GameProperties;
import com.codenjoy.dojo.services.hash.Hash;
import feign.Feign;
import feign.Logger.Level;
import feign.Response;
import feign.auth.BasicAuthRequestInterceptor;
import feign.codec.ErrorDecoder;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Game-server http-client service locator
 * <p>
 * New servers could be added by appending new server's locations and basic auth details in <tt>applicantio.yml</tt>'s
 * <tt>game.servers</tt> section
 */
@Component
public class GameClientResolver {

    private final Map<String, GameServerClient> clientsByLocation = new ConcurrentHashMap<>();

    @Autowired
    private GameProperties gameProperties;

    @PostConstruct
    void init() {
        gameProperties.getServers().forEach(server -> clientsByLocation.put(server, buildGameServerClient(server)));
    }

    public GameServerClient resolveClient(String location) {
        return clientsByLocation.get(location);
    }

    private GameServerClient buildGameServerClient(String server) {
        return Feign.builder()
                .client(new OkHttpClient())
                .encoder(new JacksonEncoder())
                .decoder(new JacksonDecoder())
                .errorDecoder(new ClientErrorDecoder())
                .logger(new Slf4jLogger(GameServerClient.class))
                .logLevel(Level.BASIC)
                .requestInterceptor(new BasicAuthRequestInterceptor(gameProperties.getBasicAuthUser(),
                        Hash.md5(gameProperties.getBasicAuthPassword())))
                .target(GameServerClient.class, gameProperties.getSchema() + "://" + server);
    }

    static class ClientErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            try {
                String body = IOUtils.toString(response.body().asInputStream());
                return new GameServerClientException(body, response.status());
            } catch (IOException e) {
                return new GameServerClientException(response.reason(), response.status());
            }
        }
    }
}
