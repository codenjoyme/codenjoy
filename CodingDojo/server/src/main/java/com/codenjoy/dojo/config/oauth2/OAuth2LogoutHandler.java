package com.codenjoy.dojo.config.oauth2;

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

import com.codenjoy.dojo.config.AppProperties;
import com.codenjoy.dojo.config.UserPrincipalService;
import com.codenjoy.dojo.config.meta.OAuth2BasedAuth;
import com.codenjoy.dojo.services.ContextPathGetter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@OAuth2BasedAuth
@RequiredArgsConstructor
public class OAuth2LogoutHandler implements LogoutHandler {

  private final UserPrincipalService principalService;
  private final AppProperties appProperties;
  private final ContextPathGetter contextPathGetter;

  @Value("${auth-server.location}")
  private String authServerLocation;

  @Value("${CLIENT_NAME}")
  private String clientName;

  @SneakyThrows
  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) {
    String idToken = principalService.userIdToken();
    String logoutUri = buildLogoutLocation();
    String logoutRedirectUri = buildPartyLocation();

    String oidcLogoutUrl = UriComponentsBuilder.fromHttpUrl(logoutUri)
        .queryParam("id_token_hint", idToken)
        .queryParam("post_logout_redirect_uri", logoutRedirectUri)
        .toUriString();
    response.sendRedirect(oidcLogoutUrl);
  }

  private String buildLogoutLocation() {
    return appProperties.getSecurity().get(clientName).getLogoutUri();
  }

  private String buildPartyLocation() {
    return contextPathGetter.getServerLocation() + "/"  + contextPathGetter.getContextPath();
  }
}
