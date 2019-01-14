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


import com.codenjoy.dojo.services.DebugService;
import com.codenjoy.dojo.services.Dispatcher;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.entity.DispatcherSettings;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.web.controller.GlobalExceptionHandler;
import com.codenjoy.dojo.web.controller.LoginException;
import com.codenjoy.dojo.web.controller.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(value = "/rest")
public class RestController {

    @Autowired private Players players;
    @Autowired private Dispatcher dispatcher;
    @Autowired private Validator validator;
    @Autowired private DebugService debug;

    @Value("${admin.password}")
    private String adminPassword;

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
            throw new IllegalArgumentException("User already registered");
        }

        final ServerLocation[] location = {null};
        doIt(new DoItOnServers() {
            @Override
            public void onBalancer() {
                location[0] = dispatcher.register(player, getIp(request));
            }

            @Override
            public void onGame() {
                player.setCode(location[0].getCode());
                player.setServer(location[0].getServer());
                players.create(player);
            }
        });

        return location[0];
    }

    private String getIp(HttpServletRequest request) {
        String result = request.getRemoteAddr();
        if (result.equals("0:0:0:0:0:0:0:1")) {
            result = "127.0.0.1";
        }
        return result;
    }

    interface OnLogin<T> {
        T onSuccess(ServerLocation data);

        T onFailed(ServerLocation data);
    }

    interface DoItOnServers {
        void onBalancer();

        void onGame();
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation login(@RequestBody Player player) {
        return tryLogin(player, new OnLogin<ServerLocation>(){

            @Override
            public ServerLocation onSuccess(ServerLocation data) {
                return data;
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
               throw new LoginException("User name or password is incorrect");
            }
        });
    }

    private <T> T tryLogin(Player player, OnLogin<T> onLogin) {
        String email = player.getEmail();
        String password = player.getPassword();

        validator.checkEmail(email, false);
        validator.checkMD5(password);

        Player exist = players.get(email);
        if (exist == null || !password.equals(exist.getPassword())) {
            return onLogin.onFailed(new ServerLocation(email, null, null));
        }
        String server = players.getServer(email);

        return onLogin.onSuccess(new ServerLocation(email, exist.getCode(), server));
    }

    private void doIt(DoItOnServers action) {
        List<String> errors = new LinkedList<>();
        try {
            action.onBalancer();
        } catch (Exception e) {
            errors.add("At balancer: " + GlobalExceptionHandler.getPrintableMessage(e));
        }

        try {
            action.onGame();
        } catch (Exception e) {
            errors.add("At game server: " +GlobalExceptionHandler.getPrintableMessage(e));
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }
    }

    @RequestMapping(value = "/remove/{player}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public void remove(@PathVariable("player") String email,
                          @PathVariable("adminPassword") String adminPassword)
    {
        validator.validateAdmin(this.adminPassword, adminPassword);

        Player player = players.get(email);
        if (player == null) {
            throw new IllegalArgumentException("Attempt to delete non-existing user");
        }

        doIt(new DoItOnServers() {
            @Override
            public void onBalancer() {
                players.remove(email);
            }

            @Override
            public void onGame() {
                dispatcher.remove(player.getServer(), player.getEmail(), player.getCode());
            }
        });
    }

    @RequestMapping(value = "/players/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable("adminPassword") String adminPassword) {
        validator.validateAdmin(this.adminPassword, adminPassword);

        return players.getPlayersDetails();
    }

    // 400 for bad registration and validation error
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(GlobalExceptionHandler.getPrintableMessage(e),
                HttpStatus.BAD_REQUEST);
    }

    // 401 for bad login
    @ExceptionHandler({LoginException.class})
    public ResponseEntity<String> handleFailedLoginException(LoginException e) {
        return new ResponseEntity<>(GlobalExceptionHandler.getPrintableMessage(e),
                HttpStatus.UNAUTHORIZED);
    }

    @RequestMapping(value = "/settings/{adminPassword}", method = RequestMethod.POST)
    @ResponseBody
    public boolean saveSettings(@PathVariable("adminPassword") String adminPassword,
                                   @RequestBody DispatcherSettings settings)
    {
        validator.validateAdmin(this.adminPassword, adminPassword);

        dispatcher.saveSettings(settings);

        return true;
    }

    @RequestMapping(value = "/settings/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public DispatcherSettings getSettings(@PathVariable("adminPassword") String adminPassword) {
        validator.validateAdmin(this.adminPassword, adminPassword);

        return dispatcher.getSettings();
    }

    @RequestMapping(value = "/debug/get/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean getDebug(@PathVariable("adminPassword") String adminPassword) {
        validator.validateAdmin(this.adminPassword, adminPassword);

        return debug.isWorking();
    }

    @RequestMapping(value = "/debug/set/{enabled}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean setDebug(@PathVariable("adminPassword") String adminPassword,
                                          @PathVariable("enabled") boolean enabled)
    {
        validator.validateAdmin(this.adminPassword, adminPassword);

        debug.setDebugEnable(enabled);

        return debug.isWorking();
    }

}
