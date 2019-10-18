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

import com.codenjoy.dojo.services.ErrorTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Igor_Petrov@epam.com
 * Created at 5/23/2019
 */
@Controller
@RequestMapping(ErrorController.URI)
public class ErrorController {

    public static final String URI = "/error";

    @Autowired private ErrorTicketService ticket;

    @GetMapping(params = "message")
    public String error(HttpServletRequest req, ModelMap model, @RequestParam("message") String message) {
        String url = req.getRequestURL().toString();

        ModelAndView view = ticket.get(url, new IllegalAccessException(message));
        view.addObject("message", message);
        model.mergeAttributes(view.getModel());

        return view.getViewName();
    }
}
