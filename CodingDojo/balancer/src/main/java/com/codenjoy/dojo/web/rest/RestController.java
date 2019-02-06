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


import com.codenjoy.dojo.services.*;
import com.codenjoy.dojo.services.dao.GameServer;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.DispatcherSettings;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.web.controller.GlobalExceptionHandler;
import com.codenjoy.dojo.web.controller.LoginException;
import com.codenjoy.dojo.web.controller.Validator;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static Logger logger = DLoggerFactory.getLogger(RestController.class);

    @Autowired private Players players;
    @Autowired private TimerService timer;
    @Autowired private Dispatcher dispatcher;
    @Autowired private Validator validator;
    @Autowired private DebugService debug;
    @Autowired private GameServer game;
    @Autowired private ConfigProperties properties;
    @Autowired private DispatcherSettings settings;

    @RequestMapping(value = "/score/day/{day}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerScore> dayScores(@PathVariable("day") String day) {
        validator.checkDay(day);

        return dispatcher.getScores(day);
    }

    @RequestMapping(value = "/score/finalists/{from}/{to}/{count}", method = RequestMethod.GET)
    @ResponseBody
    public List<PlayerScore> finalistsScores(@PathVariable("from") String from,
                                             @PathVariable("to") String to,
                                             @PathVariable("count") int count)
    {
        validator.checkDay(from);
        validator.checkDay(to);
        validator.checkPositiveInteger(count);

        return dispatcher.getFinalists(count, from, to);
    }

    @RequestMapping(value = "/score/disqualify/{player}/{adminPassword}", method = RequestMethod.POST)
    @ResponseBody
    public boolean disqualify(@RequestBody List<String> players,
                                       @PathVariable("adminPassword") String adminPassword)
    {
        validator.checkIsAdmin(adminPassword);
        players.stream().forEach(email -> validator.checkEmail(email, Validator.CANT_BE_NULL));

        dispatcher.disqualify(players);

        return true;
    }

    @RequestMapping(value = "/score/disqualified/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> disqualified(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        return dispatcher.disqualified();
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation register(@RequestBody Player player, HttpServletRequest request) {
        String email = player.getEmail();
        validator.checkEmail(email, false);
        validator.checkString(player.getFirstName());
        validator.checkString(player.getLastName());
        validator.checkMD5(player.getPassword(), Validator.CANT_BE_NULL);
        validator.checkString(player.getCity());
        validator.checkString(player.getSkills());

        if (players.getCode(email) != null) {
            throw new IllegalArgumentException("User already registered");
        }

        return doIt(new DoItOnServers<ServerLocation>() {
            @Override
            public ServerLocation onGame() {
                return dispatcher.registerNew(
                        player.getEmail(),
                        getFullName(player),
                        player.getPassword(),
                        getIp(request)
                );
            }

            @Override
            public ServerLocation onBalancer(ServerLocation location) {
                if (location != null) {
                    player.setCode(location.getCode());
                    player.setServer(location.getServer());
                    players.create(player);
                }
                return location;
            }
        });
    }

    private String getFullName(Player player) {
        return player.getFirstName() + " " + player.getLastName();
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

    interface DoItOnServers<T> {
        T onGame();

        T onBalancer(T data);
    }

    @RequestMapping(value = "/player/{player}/active/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean login(@PathVariable("player") String email,
                         @PathVariable("code") String code)
    {
        Player player = validator.checkPlayerCode(email, code);

        return dispatcher.exists(player.getEmail());
    }

    @RequestMapping(value = "/player/{player}/join/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean joinToGameServer(@PathVariable("player") String email,
                                      @PathVariable("code") String code,
                                      HttpServletRequest request)
    {
        Player player = validator.checkPlayerCode(email, code);

        ServerLocation location = dispatcher.registerIfNotExists(
                player.getServer(),
                player.getEmail(),
                getFullName(player),
                player.getPassword(),
                getIp(request));
        return location != null;
    }

    @RequestMapping(value = "/player/{player}/exit/{code}", method = RequestMethod.GET)
    @ResponseBody
    public boolean exitFromGameServer(@PathVariable("player") String email,
                                    @PathVariable("code") String code)
    {
        Player player = validator.checkPlayerCode(email, code);

        return game.remove(
                player.getServer(),
                player.getEmail(),
                player.getCode());
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation login(@RequestBody Player player, HttpServletRequest request) {
        return tryLogin(player, new OnLogin<ServerLocation>(){

            @Override
            public ServerLocation onSuccess(ServerLocation data) {
                return recreatePlayerIfNeeded(data, player.getEmail(), getIp(request));
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
               throw new LoginException("User name or password/code is incorrect");
            }
        });
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public ServerLocation changePassword(@RequestBody Player player, HttpServletRequest request) {
        return tryLogin(player, new OnLogin<ServerLocation>(){

            @Override
            public ServerLocation onSuccess(ServerLocation location) {
                String server = location.getServer();
                String email = location.getEmail();
                Player old = players.get(email);

                if (StringUtils.isNotEmpty(player.getPassword())
                        && !player.getPassword().equals(old.getPassword()))
                {
                    if (game.existsOnServer(server, email)) {
                        game.remove(server, email, location.getCode());
                    }

                    String newPassword = player.getPassword();
                    player.setCode(Hash.getCode(email, newPassword));
                }

                player.resetNullFileds(old);
                players.update(player);

                return recreatePlayerIfNeeded(location, email, getIp(request));
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
                throw new LoginException("User name or password/code is incorrect");
            }
        });
    }

    private ServerLocation recreatePlayerIfNeeded(ServerLocation current, String email, String callback) {
        return doIt(new DoItOnServers<ServerLocation>() {
            @Override
            public ServerLocation onGame() {
                return dispatcher.registerIfNotExists(
                        current.getServer(),
                        current.getEmail(),
                        getFullName(players.get(current.getEmail())),
                        current.getCode(),
                        callback);
            }

            @Override
            public ServerLocation onBalancer(ServerLocation updated) {
                if (updated == null) {
                    return current;
                }

                players.updateServer(email, updated.getServer(), updated.getCode());

                return updated;
            }
        });
    }

    private <T> T tryLogin(Player player, OnLogin<T> onLogin) {
        String email = player.getEmail();
        String password = player.getPassword();
        String code = player.getCode();

        validator.checkEmail(email, false);
        validator.checkMD5(password, Validator.CAN_BE_NULL);
        validator.checkCode(code, Validator.CAN_BE_NULL);

        Player exist = players.get(email);
        if (!isValid(exist, password, code)) {
            return onLogin.onFailed(new ServerLocation(email, null, null, null));
        }

        String server = players.getServer(email);

        return onLogin.onSuccess(
                new ServerLocation(email,
                        Hash.getId(email, properties.getEmailHash()),
                        exist.getCode(),
                        server
                ));
    }

    private boolean isValid(Player exist, String password, String code) {
        if (exist == null) {
            return false;
        }

        return exist.getPassword().equals(password)
                || exist.getCode().equals(code);
    }

    private <T> T doIt(DoItOnServers<T> action) {
        List<String> errors = new LinkedList<>();
        T result = null;

        try {
            result = action.onGame();
        } catch (Exception e) {
            logger.error("Error at game server", e);
            errors.add("At game server: " + GlobalExceptionHandler.getPrintableMessage(e));
        }

        try {
            result = action.onBalancer(result);
        } catch (Exception e) {
            logger.error("Error at balancer", e);
            errors.add("At balancer: " + GlobalExceptionHandler.getPrintableMessage(e));
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }

        return result;
    }

    @RequestMapping(value = "/remove/{player}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean remove(@PathVariable("player") String email,
                          @PathVariable("adminPassword") String adminPassword)
    {
        validator.checkIsAdmin(adminPassword);

        Player player = players.get(email);
        if (player == null) {
            throw new IllegalArgumentException("Attempt to delete non-existing user");
        }

        return doIt(new DoItOnServers<Boolean>() {
            @Override
            public Boolean onGame() {
                Boolean result = game.remove(player.getServer(), player.getEmail(), player.getCode());
                return result != null && result;
            }

            @Override
            public Boolean onBalancer(Boolean removed) {
                if (removed != null && removed) {
//                    scores.delete(email);
                    players.remove(email);
                }
                return removed;
            }
        });
    }

    @RequestMapping(value = "/players/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public List<Player> getPlayers(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

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
        validator.checkIsAdmin(adminPassword);

        this.settings.updateFrom(settings);

        return true;
    }

    @RequestMapping(value = "/settings/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public DispatcherSettings getSettings(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        return settings;
    }

    @RequestMapping(value = "/debug/get/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean getDebug(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        return debug.isWorking();
    }

    @RequestMapping(value = "/debug/set/{enabled}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean setDebug(@PathVariable("adminPassword") String adminPassword,
                                          @PathVariable("enabled") boolean enabled)
    {
        validator.checkIsAdmin(adminPassword);

        debug.setDebugEnable(enabled);

        return debug.isWorking();
    }

    @RequestMapping(value = "/contest/enable/set/{enabled}/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public List<String> startContestStarted(@PathVariable("adminPassword") String adminPassword,
                                     @PathVariable("enabled") boolean enabled)
    {
        validator.checkIsAdmin(adminPassword);

        List<String> status = new LinkedList<>();
        if (enabled) {
            status.addAll(dispatcher.clearScores());
            timer.resume();
        } else {
            timer.pause();
        }

        status.addAll(dispatcher.gameEnable(enabled));
        status.add("On balancer contest is " + (timer.isPaused() ? "paused" : "started"));

        return status;
    }


    @RequestMapping(value = "/contest/enable/get/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean getContestStarted(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        return timer.isPaused();
    }

    @RequestMapping(value = "/cache/clear/{adminPassword}", method = RequestMethod.GET)
    @ResponseBody
    public boolean invalidateCache(@PathVariable("adminPassword") String adminPassword) {
        validator.checkIsAdmin(adminPassword);

        dispatcher.clearCache();

        return true;
    }

}
