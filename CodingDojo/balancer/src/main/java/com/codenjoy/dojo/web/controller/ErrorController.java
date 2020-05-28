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

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.server.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.firewall.FirewalledRequest;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

// TODO почти такой же как в Server - подумать как устранить дублирование
@Controller
@RequestMapping(ErrorController.URI)
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    public static final String URI = "/error";
    public static final String JAVAX_SERVLET_ERROR_MESSAGE = "javax.servlet.error.message";
    public static final String JAVAX_SERVLET_ERROR_EXCEPTION = "javax.servlet.error.exception";

    @Autowired
    private ErrorTicketService ticket;

    @RequestMapping()
    public ResponseEntity<ModelMap> error(HttpServletRequest req, ModelMap model) {
        Exception throwable = (Exception)req.getAttribute(JAVAX_SERVLET_ERROR_EXCEPTION);
        if (throwable != null) {
            return error(throwable, req, model);
        }

        String message = (String) req.getAttribute(JAVAX_SERVLET_ERROR_MESSAGE);
        if (!StringUtils.isEmpty(message)) {
            return error(message, req, model);
        }

        return error("Something wrong", req, model);
    }

    @GetMapping(params = "message")
    public ResponseEntity<ModelMap> error(@RequestParam("message") String message, HttpServletRequest req, ModelMap model) {
        IllegalAccessException exception = new IllegalAccessException(message);
        return error(exception, req, model);
    }

    private ResponseEntity<ModelMap> error(Exception exception, HttpServletRequest req, ModelMap model) {
        String url = req.getRequestURL().toString();

        // для "not found" запросов вытаскиваем доп инфо
        if (req instanceof SecurityContextHolderAwareRequestWrapper) {
            ServletRequest request = ((SecurityContextHolderAwareRequestWrapper) req).getRequest();
            if (request instanceof FirewalledRequest) {
                ServletRequest request2 = ((FirewalledRequest) request).getRequest();
                if (request2 instanceof Request) {
                    url = String.format("%s [%s]",
                            url, ((Request)request2).getOriginalURI());
                }
            }
        }

        ModelAndView view = ticket.get(url, exception);
        model.mergeAttributes(view.getModel());

        return new ResponseEntity<>(model,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public String getErrorPath() {
        return URI;
    }
}
