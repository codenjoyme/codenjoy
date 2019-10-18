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

import com.codenjoy.dojo.services.hash.Hash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;

@Component
@Slf4j
public class ErrorTicketService {

    @Autowired private DebugService debug;

    public ModelAndView get(String url, Exception exception) {
        String ticket = ticket();

        log.error("[TICKET:URL] {}:{} {}", ticket, url, exception);
        System.err.printf("[TICKET:URL] %s:%s%n", ticket, url);
        exception.printStackTrace();

        ModelAndView result = new ModelAndView();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        result.addObject("ticketNumber", ticket);
        result.addObject("message", "Something wrong with your request. " +
                "Please save you ticker number and ask site administrator.");

        if (!debug.isWorking()) {
            if (url.contains("/rest/")) {
                shouldJsonResult(result);
            } else {
                shouldErrorPage(result);
            }
            return result;
        }

        result.addObject("message", exception.getClass().getName() + ": " + exception.getMessage());
        result.addObject("url", url);
        result.addObject("exception", exception);

        if (url.contains("/rest/")) {
            result.addObject("stackTrace", ExceptionUtils.getStackTrace(exception));
            result.setView(new MappingJackson2JsonView(){{
                setPrettyPrint(true);
            }});
            return result;
        }

        StringWriter writer = new StringWriter();
        exception.printStackTrace(new PrintWriter(writer));
        String text = writer.toString()
                .replaceAll("\\n\\r", "\n")
                .replaceAll("\\n\\n", "\n")
                .replaceAll("\\n", "<br>");
        result.addObject("stacktrace", text);

        shouldErrorPage(result);
        return result;
    }

    private void shouldJsonResult(ModelAndView result) {
        result.setView(new MappingJackson2JsonView(){{
            setPrettyPrint(true);
        }});
    }

    private void shouldErrorPage(ModelAndView result) {
        result.setViewName("errorPage");
    }

    private String ticket() {
        return Hash.md5("anotherSoul" + Hash.md5("someSoul" +
                Calendar.getInstance().getTimeInMillis()));
    }
}
