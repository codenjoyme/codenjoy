package com.codenjoy.dojo.web.controller;

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

import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.DebugService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;

@Component
public class ErrorTicketService {

    private static Logger logger = DLoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private DebugService debug;

    public ModelAndView get(String url, Exception e) {
        long ticket = Calendar.getInstance().getTimeInMillis();

        logger.error("[TICKET] : {}", ticket);
        logger.error("[URL] : {} {}", url, e, ticket);

        System.err.println("[TICKET] : " + ticket);
        e.printStackTrace();

        String message = (debug.isWorking())
                ? getPrintableMessage(e)
                : ("Something wrong with your request. Ticket:" + ticket);

        ModelAndView result = new ModelAndView();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        result.addObject("ticketNumber", ticket);
        result.addObject("message", message);

        return result;
    }

    public static String getPrintableMessage(Exception e) {
        return e.getClass().getSimpleName() + ": " + e.getMessage();
    }
}

