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


import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.room.RoomService;
import com.codenjoy.dojo.services.security.RegistrationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(RegistrationController.URI)
@AllArgsConstructor
public class RegistrationController {

    private static final String ADMIN = "/admin";
    public static final String URI = "/register";

    private RoomService roomService;
    private RegistrationValidator validator;
    private RegistrationService registrationService;

    @InitBinder
    private void initBinder(WebDataBinder binder) {
        binder.setValidator(validator);
    }

    @GetMapping
    public String register(HttpServletRequest request, Model model,
                           @RequestParam(name = "id", required = false) String id,
                           @RequestParam(name = "email", required = false) String email,
                           @RequestParam(name = "readableName", required = false) String name)
    {
        return registrationService.openRegistrationForm(request, model, id, email, name, false);
    }

    @GetMapping(ADMIN)
    public String registerAdmin(HttpServletRequest request, Model model,
                                @RequestParam(name = "id", required = false) String id,
                                @RequestParam(name = "email", required = false) String email,
                                @RequestParam(name = "readableName", required = false) String name)
    {
        return registrationService.openRegistrationForm(request, model, id, email, name, true);
    }

    @PostMapping()
    public String registerByName(@Valid Player player,
                                 BindingResult result,
                                 HttpServletRequest request,
                                 Model model)
    {
        if (result.hasErrors()) {
            player.dropPassword();
            return registrationService.openRegistrationForm(request, model, null, player.getEmail(), player.getReadableName());
        }

        // TODO #4FS тут проверить
        String room = player.getRoom();
        String game = roomService.game(room);
        player.setRoom(room);
        player.setGame(game);

        if (player.getId() == null) {
            player.setId(Hash.getRandomId());
        }
        return registrationService.register(player, room, result, request, model);
    }
}
