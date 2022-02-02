package com.codenjoy.dojo.config.oauth2;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2022 Codenjoy
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

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Objects;

@Component
public class OAuth2CodeExecutionClient {

    private final WebClient webClient = WebClient.builder().build();

    public String getRequest(String url) {
        return webClient.get()
                .uri(url)
                .headers(h -> h.setBearerAuth(generateAccessToken()))
                .retrieve()
                .bodyToMono(String.class).block();
    }

    private String generateAccessToken() {
        String encodedClientData =
                Base64Utils.encodeToString("client_id:client_secret".getBytes());

        return Objects.requireNonNull(webClient.post()
                        .uri("host.docker.internal:8180/auth/realms/dojo-realm/protocol/openid-connect/token")
                        .header("Authorization", "Basic " + encodedClientData)
                        .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                        .retrieve()
                        .bodyToMono(JsonNode.class)
                        .map(json -> json.get("access_token"))
                        .block())
                .toString().replace("\"", "");
    }
}