package com.codenjoy.dojo.transport.screen.async;

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
import org.springframework.web.HttpRequestHandler;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

public class ScreenController implements HttpRequestHandler {

    @Autowired
    private RestScreenSender screenSender;

    public ScreenController() {
    }

    public ScreenController(RestScreenSender screenSender) {
        this.screenSender = screenSender;
    }

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        if ("true".equals(request.getParameter("allPlayersScreen"))) {
            screenSender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, true, null));
        } else {
            Set<String> playersToUpdate = request.getParameterMap().keySet();
            screenSender.scheduleUpdate(new PlayerScreenUpdateRequest(asyncContext, false, playersToUpdate));
        }
    }
}
