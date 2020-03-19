package com.codenjoy.dojo.services.security;

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

import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.web.controller.AdminController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.codenjoy.dojo.web.controller.AdminController.GAME_NAME_KEY;
import static java.util.Optional.ofNullable;

/**
 * @author Igor Petrov
 * Created at 4/5/2019
 */
@Component
@RequiredArgsConstructor
@Slf4j
// TODO: mark as '!sso' profile
public class PlayerFormLoginSuccessAuthenticationHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final RegistrationService registrationService;

    private RequestCache requestCache = new HttpSessionRequestCache();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws ServletException, IOException {
        // reused logic from SavedRequestAwareAuthenticationSuccessHandler::onAuthenticationSuccess
        SavedRequest savedRequest = requestCache.getRequest(request, response);

        if (savedRequest == null) {
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }
        String targetUrlParameter = getTargetUrlParameter();
        if (isAlwaysUseDefaultTargetUrl()
                || (targetUrlParameter != null && StringUtils.hasText(request
                .getParameter(targetUrlParameter)))) {
            requestCache.removeRequest(request, response);
            super.onAuthenticationSuccess(request, response, authentication);

            return;
        }

        clearAuthenticationAttributes(request);
        // end of logic reuse

        if (savedRequest instanceof DefaultSavedRequest) {
            String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI();
            if (requestURI != null && requestURI.endsWith(AdminController.URI)) {
                getRedirectStrategy().sendRedirect(request, response, AdminController.URI);
                return;
            }
        }

        Registration.User principal = (Registration.User) authentication.getPrincipal();
        String gameName = obtainGameName(request, savedRequest);
        String roomName = gameName; // TODO ROOM тут надо получить roomName как-то
        // TODO #984 как воспроизвести чтобы понять зачем этот костыль нужен?
        // Мы логинимся как нормальный юзер. Потом вылогиниваемся и снова
        // залогиниваемся как админ и получаем тут ошибку. Почему? Потому
        // что у нас в requestCache какого-то фига сохоанено с прошлого раза
        // DefaultSavedRequest и мы пытаемся зайти как нормальный юзер
        // но на форме логина не было выбрано игрушки потому что там стоит
        // <c:if test="${not adminLogin}">. Как сделать так, чтобы после логаута
        // из сессии удалялся DefaultSavedRequest я не разобрался, в том и туду,
        // а пока тут повисит этот if. Надеюсь он нигде не стрельнет в делах обычных юзеров
        if (gameName == null) {
            getRedirectStrategy().sendRedirect(request, response, AdminController.URI);
            return;
        }
        String targetUrl = "/" + registrationService.register(principal.getId(), 
                principal.getCode(), roomName, gameName, request.getRemoteAddr());

        log.debug("Redirecting to  URL: " + targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String obtainGameName(HttpServletRequest request, SavedRequest savedRequest) {
        String loginFormGameName = request.getParameter(GAME_NAME_KEY);
        String[] queryParamGameParameter = ofNullable(savedRequest.getParameterValues(GAME_NAME_KEY))
                .orElse(new String[] {});
        String queryParamGameName = queryParamGameParameter.length > 0 ? queryParamGameParameter[0] : null;

        String gameName = ofNullable(queryParamGameName)
                .orElse(loginFormGameName);

        // TODO при первой загрузке если сразу залогиниться в админку то получаем gameName == null
        // все потому что там на поле с играми стоит <c:if test="${not adminLogin}">
        log.debug("Game name was chosen: {}", gameName);

        return gameName;
    }

    public void setRequestCache(RequestCache requestCache) {
        this.requestCache = requestCache;
    }
}
