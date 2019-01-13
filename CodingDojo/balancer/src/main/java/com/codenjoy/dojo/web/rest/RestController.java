package com.codenjoy.dojo.web.rest;

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


import com.codenjoy.dojo.services.Dispatcher;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.web.controller.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping(value = "/rest")
public class RestController {

    @Autowired private Players players;
    @Autowired private Dispatcher dispatcher;
    @Autowired private Validator validator;


    @RequestMapping(value = "/score/day/{day}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerScore> dayScores(@PathVariable("day") String day) {
        validator.checkDay(day);

        return dispatcher.getScores(day);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation register(@RequestBody Player player, HttpServletRequest request) {
        String email = player.getEmail();
        validator.checkEmail(email, false);
        validator.checkString(player.getFirstName());
        validator.checkString(player.getLastName());
        validator.checkMD5(player.getPassword());
        validator.checkString(player.getCity());
        validator.checkString(player.getSkills());

        if (players.getCode(email) != null) {
            return unauthorized(email);
        }

        ServerLocation location = dispatcher.register(player, getIp(request));

        player.setCode(location.getCode());
        player.setServer(location.getServer());
        players.create(player);

        return location;
    }

    private String getIp(HttpServletRequest request) {
        String result = request.getRemoteAddr();
        if (result.equals("0:0:0:0:0:0:0:1")) {
            result = "127.0.0.1";
        }
        return result;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation login(@RequestBody Player player) {
        String email = player.getEmail();
        String password = player.getPassword();

        validator.checkEmail(email, false);
        validator.checkMD5(password);

        Player exist = players.get(email);
        if (exist == null || !password.equals(exist.getPassword())) {
            return unauthorized(email);
        }
        String server = players.getServer(email);

        return new ServerLocation(email, exist.getCode(), server);
    }

    private ServerLocation unauthorized(String email) {
        return new ServerLocation(email, null, null);
    }

}
