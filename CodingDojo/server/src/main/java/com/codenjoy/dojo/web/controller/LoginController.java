package com.codenjoy.dojo.web.controller;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2012 - 2022 Codenjoy
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

import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.room.RoomService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(LoginController.URI)
@AllArgsConstructor
public class LoginController {

    public static final String URI = "/login";
    private static final String ADMIN = "/admin";
    private static final String OAUTH = "/oauth";
    public static final String ADMIN_URI =  URI + ADMIN;

    private PlayerService playerService;
    private RoomService roomService;

    @GetMapping
    public String login(Model model) {
        populateModel(model, false);
        return "login";
    }

    @GetMapping(ADMIN)
    public String loginAdmin(Model model) {
        populateModel(model, true);
        return "login";
    }

    private void populateModel(Model model, boolean isAdmin) {
        model.addAttribute("opened", playerService.isRegistrationOpened());
        // TODO #4FS тут проверить
        model.addAttribute("gamesRooms", roomService.openedGamesRooms());
        model.addAttribute("adminLogin", isAdmin);
    }

    @GetMapping(OAUTH)
    public String loginOauth(){
        return "ok";
    }
}
