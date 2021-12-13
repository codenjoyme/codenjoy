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
import com.codenjoy.dojo.services.log.DebugService;
import com.codenjoy.dojo.transport.ws.PlayerSocketCreator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class ErrorTicketService {

    private static final String ERROR_MESSAGE = "Something wrong with your request. " +
            "Please save you ticker number and ask site administrator.";
    private final static SimpleDateFormat DAY_TIME_FORMATTER = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

    private final DebugService debug;
    private final TimeService time;

    private boolean printStackTrace = true;

    private Map<String, Map<String, Object>> tickets = new ConcurrentHashMap<>();
    private Map<String, String> info = new ConcurrentSkipListMap<>();

    public ModelAndView get(String url, String contentType, Exception exception) {
        String ticket = ticket();

        String message = exception.toString();
        log.error("[TICKET:URL] {}:{} {}", ticket, url, message);
        System.err.printf("[TICKET:URL] %s:%s %s%n", ticket, url, message);

        if (printStackTrace && !skip(message)) {
            exception.printStackTrace();
        }

        Map<String, Object> info = getDetails(ticket, url, exception);
        tickets.put(ticket, info);

        ModelAndView result = new ModelAndView();
        result.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        set(result, "ticketNumber", info);

        boolean isJson = url.contains("/rest/")
                || (contentType != null && contentType.contains("application/json"));

        if (isJson) {
            shouldJsonResult(result);
        } else {
            shouldErrorPage(result);
        }

        if (!debug.isWorking()) {
            result.addObject("message", ERROR_MESSAGE);
            return result;
        }

        set(result, "message", info);
        set(result, "url", info);
        set(result, "exception", info);

        if (isJson) {
            set(result, "stackTrace", info);
            return result;
        }

        result.addObject("stackTrace", prepareStackTrace(exception));
        return result;
    }

    private void set(ModelAndView result, String name, Map<String, Object> info) {
        result.addObject(name, info.get(name));
    }

    public Map<String, Object> getDetails(String ticket, String url, Exception exception) {
        return new HashMap<>(){{
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
        return DAY_TIME_FORMATTER.format(Calendar.getInstance().getTime());
    }

    private boolean skip(String message) {
        return message.contains(PlayerSocketCreator.UNAUTHORIZED_ACCESS);
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
                time.now()));
    }

    public void setPrintStackTrace(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    public Map<String, Map<String, Object>> getErrors() {
        return tickets;
    }

    public void clear() {
        tickets.clear();
        info.clear();
    }

    public void logInfo(String message) {
        info.put(now(), message);
    }

    public Map<String, String> getInfo() {
        return info;
    }
}
