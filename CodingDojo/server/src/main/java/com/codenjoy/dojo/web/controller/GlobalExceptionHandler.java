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
import com.codenjoy.dojo.services.hash.Hash;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

/**
 * Created by Oleksandr_Baglai on 2018-06-26.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = DLoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        String url = req.getRequestURL().toString();
        String ticket = ticket();
        logger.error("[TICKET:URL] {}:{} {}", ticket, url, e);
        System.err.printf("[TICKET:URL] %s:%s\n", ticket, url);
        e.printStackTrace();

        ModelAndView result = new ModelAndView();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        // TODO сдеалть тут какуй-то пропертю
        if (true) {
            result.addObject("message", "Something wrong with your request. " +
                    "Please ask site administrator. Your ticket number is: " + ticket);
            if (url.contains("/rest/")) {
                shouldJsonResult(result);
            } else {
                shouldErrorPage(result);
            }
            return result;
        }

        if (url.contains("/rest/")) {
            result.addObject("message", e.getMessage());
            result.addObject("stackTrace", ExceptionUtils.getStackTrace(e));
            result.addObject("url", url);
            result.setView(new MappingJackson2JsonView(){{
                setPrettyPrint(true);
            }});
            return result;
        }

        result.addObject("exception", e);

        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        String text = writer.toString()
                .replaceAll("\\n\\r", "\n")
                .replaceAll("\\n\\n", "\n")
                .replaceAll("\\n", "<br>");
        result.addObject("stacktrace", text);

        result.addObject("message", e.getClass().getName() + ": " + e.getMessage());

        result.addObject("url", url);

        shouldErrorPage(result);

        return result;
    }

    private void shouldJsonResult(ModelAndView result) {
        result.setView(new MappingJackson2JsonView(){{
            setPrettyPrint(true);
        }});
    }

    private void shouldErrorPage(ModelAndView result) {
        result.setViewName("error");
    }

    private String ticket() {
        return Hash.md5("anotherSoul" + Hash.md5("someSoul" +
                Calendar.getInstance().getTimeInMillis()));
    }

}
