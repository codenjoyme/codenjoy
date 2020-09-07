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
import com.codenjoy.dojo.services.dao.Players;
import com.codenjoy.dojo.services.dao.Scores;
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.entity.server.Disqualified;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.web.controller.ErrorTicketService;
import com.codenjoy.dojo.web.controller.LoginException;
import com.codenjoy.dojo.web.controller.BalancerValidator;
import com.codenjoy.dojo.web.rest.dto.*;
import com.codenjoy.dojo.web.security.SecurityContextAuthenticator;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static com.codenjoy.dojo.web.controller.BalancerValidator.CANT_BE_NULL;

@Controller
@RequestMapping(value = RestController.URI)
public class RestController {

    public static final String URI = "/rest";
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String PLAYER = "/player";
    public static final String PLAYERS = PLAYER + "s";
    public static final String SETTINGS = "/settings";
    public static final String DEBUG = "/debug";
    public static final String CONTEST = "/contest";
    public static final String CACHE = "/cache";
    public static final String REMOVE = "/remove";
    public static final String CONFIRM = "/confirm";
    public static final String UPDATE = "/update";
    public static final String SCORE = "/score";
    public static final String GAME_SETTINGS = "/game/settings";
    public static final String VERSION = "/version";
    public static final String LOGS = "/logs";

    private static Logger logger = DLoggerFactory.getLogger(RestController.class);

    @Autowired private Players players;
    @Autowired private Scores scores;
    @Autowired private TimerService timer;
    @Autowired private ErrorTicketService ticket;
    @Autowired private Dispatcher dispatcher;
    @Autowired private BalancerValidator validator;
    @Autowired private DebugService debug;
    @Autowired private GameServer game;
    @Autowired private GameServers gameServers;
    @Autowired private ConfigProperties config;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SecurityContextAuthenticator authenticator;
    @Autowired private SmsService sms;
    @Autowired private Generator generator;

    @GetMapping(VERSION)
    @ResponseBody
    public String version() {
        return VersionReader.version(Arrays.asList("engine", "balancer")).toString();
    }

    @GetMapping(LOGS + "/info")
    @ResponseBody
    public Map<String, String> getInfoLogs() {
        return ticket.getInfo();
    }

    @GetMapping(LOGS + "/error")
    @ResponseBody
    public Map<String, Map<String, Object>> getTickets(
            @RequestParam(value = "ticket", required = false) String ticketId)
    {
        final Map<String, Map<String, Object>> tickets = ticket.getErrors();

        if (StringUtils.isEmpty(ticketId)) {
            return tickets;
        } else {
            return new HashMap<String, Map<String, Object>>(){{
                put(ticketId, tickets.get(ticketId));
            }};
        }
    }

    @GetMapping(SCORE + "/day/{day}")
    @ResponseBody
    public List<PlayerScore> dayScores(@PathVariable("day") String day) {
        validator.checkDay(day);

        return dispatcher.getScores(day);
    }

    // TODO test me
    @GetMapping(SCORE + "/finalists")
    @ResponseBody
    public List<PlayerScore> finalistsScores() {
        return dispatcher.getFinalists();
    }

    @GetMapping(SCORE + "/winners")
    @ResponseBody
    public List<PlayerScore> markWinners() {
        return dispatcher.markWinners();
    }

    // TODO test me
    @PostMapping(SCORE + "/disqualify")
    @ResponseStatus(HttpStatus.OK)
    public void disqualify(@RequestBody PlayersDTO input) {
        List<String> players = input.getPlayers();
        players.forEach(email -> validator.checkEmailOrId(email, CANT_BE_NULL));

        dispatcher.disqualify(players);
    }

    // TODO test me
    @GetMapping(SCORE + "/disqualified")
    @ResponseBody
    public Collection<Disqualified> disqualified() {
        return dispatcher.disqualified();
    }

    @PostMapping(REGISTER)
    @ResponseBody
    public ServerLocation register(@RequestBody Player player, HttpServletRequest request) {
        player.setCallback(getIp(request));

        validator.all(
                () -> validator.checkEmail(player.getEmail(), CANT_BE_NULL),
                () -> validator.checkPhoneNumber(player.getPhone()),
                () -> validator.checkName("firstName", player.getFirstName()),
                () -> validator.checkName("lastName", player.getLastName()),
                () -> validator.checkMD5(player.getPassword(), CANT_BE_NULL),
                () -> validator.checkName("city", player.getCity()),
                () -> validator.checkString("skills", player.getSkills())
        );

        player.setPhone(validator.phoneNormalize(player.getPhone()));

        validator.checkNotRegisteredEmail(player.getEmail());
        validator.checkNotRegisteredPhone(player.getPhone());

        String md5Password = player.getPassword();

        Player result = doIt(new DoItOnServers<Player>() {
            @Override
            public Player onGame() {
                // ничего не делаем, ведь надо дождаться верификации по смс в confirmRegistration
                return player;
            }

            @Override
            public Player onBalancer(Player player) {
                player.setId(generator.id());
                player.setPassword(passwordEncoder.encode(md5Password));
                // code = code(md5(bcrypt(password)))
                player.setCode(Hash.getCode(player.getId(), player.getPassword()));

                String verificationCode = generator.verificationCode();
                player.setApproved(Player.NOT_APPROVED);
                player.setVerificationCode(verificationCode);
                player.setVerificationType(VerificationType.REGISTRATION.name());

                players.create(player);

                sms.sendSmsTo(player.getPhone(), verificationCode,
                        SmsService.SmsType.REGISTRATION);

                ticket.logInfo(String.format("Send registration verification code '%s' for %s",
                        verificationCode, new ServerLocation(player)));

                authenticator.login(request, player.getEmail(), md5Password);

                return player;
            }
        });

        return new ServerLocation(result);
    }

    private String getIp(HttpServletRequest request) {
        String result = request.getHeader("X-Forwarded-For");
        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        result = request.getHeader("X-Real-IP");
        if (!StringUtils.isEmpty(result)) {
            return result;
        }

        result = request.getRemoteAddr();
        if (result.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }
        return result;
    }

    interface OnLogin {
        Player onSuccess(Player player);
    }

    interface DoItOnServers<T> {
        T onGame();

        T onBalancer(T data);
    }

    @GetMapping(PLAYER + "/{player}/active/{code}")
    @ResponseBody
    public boolean isJoinedToGameServer(@PathVariable("player") String email,
                         @PathVariable("code") String code)
    {
        Player player = validator.checkPlayerCode(email, code); // TODO test me

        // TODO test me when not found on balancer
        return dispatcher.exists(player);
    }

    @GetMapping(PLAYER + "/{player}/join/{code}")
    @ResponseBody
    public ServerLocation joinToGameServer(@PathVariable("player") String id,
                                      @PathVariable("code") String code,
                                      HttpServletRequest request)
    {
        Player player = new Player(id, code, getIp(request));

        ServerLocation location = tryLogin(player, true,
                current -> recreatePlayerIfNeeded(current));

        if (code.equals(location.getCode())) {
            return null;
        } else {
            return location;
        }
    }

    @GetMapping(PLAYER + "/{player}/exit/{code}")
    @ResponseBody
    public boolean exitFromGameServer(@PathVariable("player") String email,
                                    @PathVariable("code") String code)
    {
        Player player = validator.checkPlayerCode(email, code); // TODO test me

        return game.remove(player.getServer(), player.getId());
    }

    @PostMapping(LOGIN)
    @ResponseBody
    public ServerLocation login(@RequestBody Player player, HttpServletRequest request) {
        player.setCallback(getIp(request));

        String md5Password = player.getPassword();

        return tryLogin(player, false, p -> {
            Player result = recreatePlayerIfNeeded(p);

            authenticator.login(request, p.getEmail(), md5Password);

            return result;
        });
    }

    // TODO test me
    @PostMapping(UPDATE)
    @ResponseBody
    public ServerLocation update(@RequestBody Player player, HttpServletRequest request) {
        player.setCallback(getIp(request));

        return tryLogin(player, false, current -> {
            String server = current.getServer();
            String id = current.getId();

            if (StringUtils.isNotEmpty(player.getPassword())
                    && !player.getPassword().equals(current.getPassword()))
            {
                if (game.existsOnServer(server, id)) {
                    game.remove(server, id);
                }

                // тут пароль в md5 виде приведенном в такое состояние фронтом
                String md5 = player.getPassword();
                validator.checkMD5(md5, CANT_BE_NULL);
                // мы его еще разочек захешируем
                String hashed = passwordEncoder.encode(md5);
                // и подсчитаем code(md5(bcrypt(password)))
                player.setCode(Hash.getCode(id, hashed));
            }

            player.resetNullFields(current);
            players.update(player);

            // TODO проверить как обновляется code при смене пароля
            return recreatePlayerIfNeeded(player);
        });
    }

    private Player recreatePlayerIfNeeded(Player current) {
        return doIt(new DoItOnServers<Player>() {
            @Override
            public Player onGame() {
                return dispatcher.registerIfNotExists(current);
            }

            @Override
            public Player onBalancer(Player updated) {
                if (updated == null) {
                    return current;
                }

                // TODO test me
                players.updateServer(current.getId(),
                        updated.getServer(), updated.getCode());
                return updated;
            }
        });
    }

    private ServerLocation tryLogin(Player player, boolean ignorePass, OnLogin onLogin) {
        String email = player.getEmail();
        String password = player.getPassword();
        String code = player.getCode();

        Player exist = validator.checkPlayerLogin(email, code, password, ignorePass);

        Player result = onLogin.onSuccess(exist);
        return new ServerLocation(result);
    }

    private <T> T doIt(DoItOnServers<T> action) {
        List<String> errors = new LinkedList<>();
        T result = null;

        try {
            result = action.onGame();
        } catch (Exception e) {
            logger.error("Error at game server", e);
            errors.add("At game server: " + ErrorTicketService.getPrintableMessage(e));
        }

        try {
            result = action.onBalancer(result);
        } catch (Exception e) {
            logger.error("Error at balancer", e);
            errors.add("At balancer: " + ErrorTicketService.getPrintableMessage(e));
        }

        if (!errors.isEmpty()) {
            throw new RuntimeException(errors.toString());
        }

        return result;
    }

    // TODO test me
    @GetMapping(REMOVE + "/{player}/on/{whereToRemove}")
    @ResponseBody
    public List<String> remove(@PathVariable("player") String id,
                               @PathVariable("whereToRemove") int whereToRemove)
    {
        List<String> status = new LinkedList<>(); validator.checkId(id, CANT_BE_NULL);

        tryRemoveFromGame((whereToRemove & 0b0001) == 0b0001, id, status);
        tryRemoveFromBalancer((whereToRemove & 0b0010) == 0b0010, id, status);

        return status;
    }

    private void tryRemoveFromBalancer(boolean remove, String id, List<String> status) {
        String message = "At balancer server: ";
        try {
            Player player = players.get(id);

            if (remove && player != null) {
                scores.remove(player.getId());
                players.remove(player.getId());

                message = message + "removed {true} ";
            } else {
                message = message + "removed {} ";
            }

            boolean exists = players.get(id) != null;
            message = message + "exists: " + exists;

        } catch (Exception e) {
            message = message + ErrorTicketService.getPrintableMessage(e);
        }
        status.add(message);
    }

    private void tryRemoveFromGame(boolean remove, String id, List<String> status) {
        String message = "At game server: ";
        try {
            if (remove) {
                Map<String, Boolean> map = dispatcher.removeFromEveryGameServer(id);
                message = message + "removed: " + map.toString() + " ";
            } else {
                message = message + "removed: {} ";
            }

            Map<String, Boolean> map = dispatcher.existsOnGameServers(id);
            message = message + "exists: " + map;
        } catch (Exception e) {
            message = message + ErrorTicketService.getPrintableMessage(e);
        }
        status.add(message);
    }

    // TODO test me
    @GetMapping(PLAYERS)
    @ResponseBody
    public List<Player> getPlayers() {
        return players.getPlayersDetails();
    }

    // 400 for bad registration and validation error
    @ExceptionHandler({IllegalArgumentException.class, UsernameNotFoundException.class})
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 401 for bad login
    @ExceptionHandler({LoginException.class})
    public ResponseEntity<String> handleFailedLoginException(LoginException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    // TODO test me
    @PostMapping(SETTINGS)
    @ResponseStatus(HttpStatus.OK)
    public void saveSettings(@RequestBody ConfigProperties input) {
        config.updateFrom(input);

        gameServers.update(input.getGame().getServers());
    }

    // TODO test me
    @GetMapping(SETTINGS)
    @ResponseBody
    public ConfigProperties getSettings() {
        return config;
    }

    // TODO test me
    @GetMapping(GAME_SETTINGS + "/get")
    @ResponseBody
    public List<GameSettings> getGameSettings() {
        return dispatcher.getGameSettings();
    }

    // TODO test me
    @PostMapping(GAME_SETTINGS + "/set")
    @ResponseStatus(HttpStatus.OK)
    public void saveGameSettings(@RequestBody GameSettings gameSettings) {
        dispatcher.updateGameSettings(gameSettings);
    }

    // TODO test me
    @GetMapping(DEBUG + "/get")
    @ResponseBody
    public boolean getDebug() {
        return debug.isWorking();
    }

    // TODO test me
    @GetMapping(DEBUG + "/set/{enabled}")
    @ResponseBody
    public boolean setDebug(@PathVariable("enabled") boolean enabled) {
        debug.setDebugEnable(enabled);
        return debug.isWorking();
    }

    // TODO test me
    @GetMapping(CONTEST + "/enable/set/{enabled}")
    @ResponseBody
    public List<String> startContestStarted(@PathVariable("enabled") boolean enabled) {
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


    // TODO test me
    @GetMapping(CONTEST + "/enable/get")
    @ResponseBody
    public boolean getContestStarted() {
        return !timer.isPaused();
    }

    // TODO test me
    @GetMapping(CACHE + "/clear/{mask}")
    @ResponseStatus(HttpStatus.OK)
    public void invalidateCache(@PathVariable("mask") int whatToClean) {
        dispatcher.clearCache(whatToClean);
    }

    @GetMapping(CONFIRM + "/{player}/code")
    @ResponseBody
    public VerificationDTO getVerificationCode(@PathVariable("player") String email) {
        Player player = validator.checkPlayerByEmail(email, false);

        return new VerificationDTO(player);
    }

    @PostMapping(REGISTER + "/resend")
    @ResponseStatus(HttpStatus.OK)
    public void resendRegistrationCode(@RequestBody PhoneDTO input) {
        Player player = validator.checkPlayerByPhone(input.getPhone());
        validator.checkNotApproved(player);

        String code = generator.verificationCode();
        players.updateVerificationCode(player, code, VerificationType.REGISTRATION);

        sms.sendSmsTo(player.getPhone(), code, SmsService.SmsType.REGISTRATION);

        ticket.logInfo(String.format("Resend registration code '%s' for %s",
                code, new ServerLocation(player)));
    }

    @PostMapping(REGISTER + "/reset")
    @ResponseStatus(HttpStatus.OK)
    public void sendResetPasswordCode(@RequestBody PhoneEmailDTO input) {
        Player player = validator.checkPlayerByEmailAndPhone(input.getEmail(), input.getPhone());
        validator.checkApproved(player);

        String code = generator.verificationCode();
        players.updateVerificationCode(player, code, VerificationType.PASSWORD_RESET);

        sms.sendSmsTo(player.getPhone(), code, SmsService.SmsType.PASSWORD_RESET);

        ticket.logInfo(String.format("Send password reset verification code '%s' for %s",
                code, new ServerLocation(player)));
    }

    @PostMapping(REGISTER + "/confirm")
    @ResponseBody
    public ServerLocation confirmRegistration(@RequestBody PhoneCodeDTO input) {
        Player player = validator.checkPlayerByPhone(input.getPhone());
        validator.checkNotApproved(player);
        validator.checkVerificationCode(player,
                VerificationType.REGISTRATION, input.getCode());

        players.approve(player.getId());
        players.updateVerificationCode(player, null, null);

        Player createdOnGame = dispatcher.registerNew(player);

        return new ServerLocation(createdOnGame);
    }

    @PostMapping(REGISTER + "/validate-reset")
    @ResponseStatus(HttpStatus.OK)
    public void validateResetPasswordCode(@RequestBody PhoneCodeDTO input) {
        Player player = validator.checkPlayerByPhone(input.getPhone());
        validator.checkApproved(player);
        validator.checkVerificationCode(player,
                VerificationType.PASSWORD_RESET, input.getCode());

        players.updateVerificationCode(player, null, null);

        if (game.existsOnServer(player.getServer(), player.getId())) {
            game.remove(player.getServer(), player.getId());
        }

        regeneratePlayerPassword(player);
    }

    private void regeneratePlayerPassword(Player player) {
        String generated = generator.password();
        // это делает фронтенд, но у нас тут чистый пароль
        String md5 = DigestUtils.md5Hex(generated);
        // еще раз захешируем
        String hashed = passwordEncoder.encode(md5);

        player.setPassword(hashed);
        // и подсчитаем code(md5(bcrypt(password)))
        player.setCode(Hash.getCode(player.getId(), hashed));

        players.update(player);

        sms.sendSmsTo(player.getPhone(), generated,
                SmsService.SmsType.NEW_PASSWORD);

        ticket.logInfo(String.format("Updated password '%s' for %s",
                generated, new ServerLocation(player)));
    }
}
