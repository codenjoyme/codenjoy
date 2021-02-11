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
import com.codenjoy.dojo.services.properties.Messages;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

// TODO такой же как в Server - подумать как устранить дублирование
@Component
@Slf4j
public class ErrorTicketService {

    private static final String ERROR_MESSAGE = "Something wrong with your request. " +
            "Please save you ticker number and ask site administrator.";
    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    @Autowired
    private DebugService debug;

    @Autowired
    private Messages messages;

    private Map<String, Map<String, Object>> tickets = new ConcurrentHashMap<>();
    private Map<String, String> info = new ConcurrentSkipListMap<>();

    private boolean printStackTrace = true;

    public ModelAndView get(String url, Exception exception) {
        String ticket = ticket();

        String message = printStackTrace ? exception.toString() : exception.toString();
        log.error("[TICKET:URL] {}:{} {}", ticket, url, message);
        System.err.printf("[TICKET:URL] %s:%s %s%n", ticket, url, message);

        if (printStackTrace && !skip(message)) {
            exception.printStackTrace();
        }

        Map<String, Object> info = getDetails(ticket, url, exception);
        tickets.put(ticket, info);

        ModelAndView result = new ModelAndView();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        copy("ticketNumber", info, result);

        if (!debug.isWorking()) {
            result.addObject("message", getMessage());

            if (url.contains("/rest/")) {
                shouldJsonResult(result);
            } else {
                shouldErrorPage(result);
            }
            return result;
        }

        copy("message", info, result);
        copy("url", info, result);
        copy("exception", info, result);

        if (url.contains("/rest/")) {
            copy("stackTrace", info, result);

            result.setView(new MappingJackson2JsonView(){{
                setPrettyPrint(true);
            }});

            return result;
        }

        result.addObject("stackTrace", prepareStackTrace(exception));

        shouldErrorPage(result);
        return result;
    }

    private String getMessage() {
        String message = messages.getSomethingWrong();
        if (StringUtils.isEmpty(message)) {
            message = ERROR_MESSAGE;
        }
        return message;
    }

    private void copy(String name, Map<String, Object> info, ModelAndView model) {
        model.addObject(name, info.get(name));
    }

    public Map<String, Object> getDetails(String ticket, String url, Exception exception) {
        return new HashMap<String, Object>(){{
            put("ticketNumber", ticket);
            put("time", now());
            put("message", exception.getClass().getName() + ": " + exception.getMessage());
            put("url", url);
            if (!printStackTrace) {
                exception.setStackTrace(new StackTraceElement[0]);
            }
            put("exception", exception);
            put("stackTrace", prepareJsonStackTrace(exception));
        }};
    }

    private String now() {
        return format.format(Calendar.getInstance().getTime());
    }

    private boolean skip(String message) {
        return message.contains("java.lang.IllegalAccessException: Unauthorized");
    }

    private List<String> prepareJsonStackTrace(Exception exception) {
        if (printStackTrace) {
            return Arrays.asList(
                    ExceptionUtils.getStackTrace(exception)
                            .replaceAll("[\r\t]", "")
                            .split("\n"));
        } else {
            return Arrays.asList();
        }
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

    public Map<String, Map<String, Object>> getErrors() {
        return tickets;
    }

    public void logInfo(String message) {
        info.put(now(), message);
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
