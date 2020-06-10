package com.codenjoy.dojo.web.controller;

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

import com.codenjoy.dojo.services.DebugService;
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
// TODO такой же как в Server - подумать как устранить дублирование
@Component
@Slf4j
public class ErrorTicketService {

    @Autowired
    private DebugService debug;

    private boolean printStackTrace = true;

    public ModelAndView get(String url, Exception exception) {
        String ticket = ticket();

        // TODO очень было бы здорово, если бы мы хранили все исключения и отдавали бы их на админке
        String message = printStackTrace ? exception.toString() : exception.toString();
        log.error("[TICKET:URL] {}:{} {}", ticket, url, message);
        System.err.printf("[TICKET:URL] %s:%s %s%n", ticket, url, message);

        if (printStackTrace) {
            exception.printStackTrace();
        }

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
        if (!printStackTrace) {
            exception.setStackTrace(new StackTraceElement[0]);
        }
        result.addObject("exception", exception);

        if (url.contains("/rest/")) {
            result.addObject("stackTrace", prepareJsonStackTrace(exception));
            result.setView(new MappingJackson2JsonView(){{
                setPrettyPrint(true);
            }});
            return result;
        }

        String text = prepareStackTrace(exception);
        result.addObject("stacktrace", text);

        shouldErrorPage(result);
        return result;
    }

    private String prepareJsonStackTrace(Exception exception) {
        return printStackTrace ? ExceptionUtils.getStackTrace(exception) : "";
    }

    private String prepareStackTrace(Exception exception) {
        if (printStackTrace) {
            StringWriter writer = new StringWriter();
            exception.printStackTrace(new PrintWriter(writer));
            return writer.toString()
                    .replaceAll("\\n\\r", "\n")
                    .replaceAll("\\n\\n", "\n")
                    .replaceAll("\\n", "<br>");
        }
        return "";
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

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    public static String getPrintableMessage(Exception e) {
        return e.getClass().getSimpleName() + ": " + e.getMessage();
    }
}
