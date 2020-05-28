package com.codenjoy.dojo.web.controller;

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


import com.codenjoy.dojo.services.DLoggerFactory;
import com.codenjoy.dojo.services.DebugService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = DLoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private DebugService debug;

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<String> defaultErrorHandler(HttpServletRequest request, Exception e) {
        long ticket = Calendar.getInstance().getTimeInMillis();

        logger.error("[TICKET] : {}", ticket);
        logger.error("[URL] : {} {}", request.getRequestURL(), e, ticket);

        System.err.println("[TICKET] : " + ticket);
        e.printStackTrace();

        String message = (debug.isWorking())
                ? getPrintableMessage(e)
                : ("Something wrong with your request. Ticket:" + ticket);

        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static String getPrintableMessage(Exception e) {
        return e.getClass().getSimpleName() + ": " + e.getMessage();
    }

}
