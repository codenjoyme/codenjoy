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
import com.codenjoy.dojo.services.entity.Player;
import com.codenjoy.dojo.services.entity.PlayerScore;
import com.codenjoy.dojo.services.entity.ServerLocation;
import com.codenjoy.dojo.services.entity.server.Disqualified;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.web.controller.ErrorTicketService;
import com.codenjoy.dojo.web.controller.LoginException;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.dto.*;
import com.codenjoy.dojo.web.security.SecurityContextAuthenticator;
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
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

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

    private static Logger logger = DLoggerFactory.getLogger(RestController.class);

    @Autowired private Players players;
    @Autowired private TimerService timer;
    @Autowired private Dispatcher dispatcher;
    @Autowired private Validator validator;
    @Autowired private DebugService debug;
    @Autowired private GameServer game;
    @Autowired private GameServers gameServers;
    @Autowired private ConfigProperties config;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private SecurityContextAuthenticator securityContextAuthenticator;
    @Autowired private RegistrationService registrationService;
    @Autowired private SmsService smsService;

    // TODO test me
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
    @ResponseBody
    public boolean disqualify(@RequestBody PlayersDTO input) {
        List<String> players = input.getPlayers();
        players.forEach(email -> validator.checkEmailOrId(email, CANT_BE_NULL));

        dispatcher.disqualify(players);

        return true;
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
        String email = player.getEmail();

        validator.all(
                () -> validator.checkEmail(email, CANT_BE_NULL),
                () -> validator.checkPhoneNumber(player.getPhone(), CANT_BE_NULL),
                () -> validator.checkString("FirstName", player.getFirstName()),
                () -> validator.checkString("LastName", player.getLastName()),
                () -> validator.checkString("Password", player.getPassword()),
                () -> validator.checkString("City", player.getCity()),
                () -> validator.checkString("Skills", player.getSkills())
        );

        String phone = validator.phoneNormalizer(player.getPhone());

        if (players.getCode(email) != null || players.getByPhone(phone).isPresent()) {
            throw new IllegalArgumentException("User already registered");
        }

        return doIt(new DoItOnServers<ServerLocation>() {
            @Override
            public ServerLocation onGame() {
                return dispatcher.registerNew(
                        player.getEmail(),
                        phone,
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
                    String verificationCode = registrationService.generateVerificationCode();
                    players.create(new Player(
                            player.getEmail(),
                            phone,
                            player.getFirstName(),
                            player.getLastName(),
                            passwordEncoder.encode(player.getPassword()),
                            player.getCity(),
                            player.getSkills(),
                            player.getComment(),
                            player.getCode(),
                            player.getServer(),
                            Player.NOT_APPROVED,
                            verificationCode,
                            RegistrationService.VerificationType.REGISTRATION.name()
                    ));
                    smsService.sendSmsTo(player.getPhone(), verificationCode, SmsService.SmsType.REGISTRATION);
                }
                securityContextAuthenticator.login(request, player.getEmail(), player.getPassword());
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

    @GetMapping(PLAYER + "/{player}/active/{code}")
    @ResponseBody
    public boolean isJoinedToGameServer(@PathVariable("player") String email,
                         @PathVariable("code") String code)
    {
        Player player = validator.checkPlayerCode(email, code); // TODO test me

        // TODO test me when not found on balancer
        return dispatcher.exists(player.getEmail());
    }

    @GetMapping(PLAYER + "/{player}/join/{code}")
    @ResponseBody
    public ServerLocation joinToGameServer(@PathVariable("player") String email,
                                      @PathVariable("code") String code,
                                      HttpServletRequest request)
    {
        ServerLocation location = tryLogin(new Player(email, code), true, new OnLogin<ServerLocation>() {
            @Override
            public ServerLocation onSuccess(ServerLocation data) {
                ServerLocation serverLocation = recreatePlayerIfNeeded(data, email, getIp(request));
                return serverLocation;
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
                // TODO test me
                throw new LoginException("User email or password/code is incorrect");
            }
        });

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

        return game.remove(
                player.getServer(),
                player.getEmail(),
                player.getCode());
    }

    @PostMapping(LOGIN)
    @ResponseBody
    public ServerLocation login(@RequestBody Player player, HttpServletRequest request) {
        return tryLogin(player, false, new OnLogin<ServerLocation>(){

            @Override
            public ServerLocation onSuccess(ServerLocation data) {
                ServerLocation serverLocation = recreatePlayerIfNeeded(data, player.getEmail(), getIp(request));
                securityContextAuthenticator.login(request, player.getEmail(), player.getPassword());
                return serverLocation;
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
                // TODO test me
                throw new LoginException("User email or password/code is incorrect");
            }
        });
    }

    // TODO test me
    @PostMapping(UPDATE)
    @ResponseBody
    public ServerLocation update(@RequestBody Player player, HttpServletRequest request) {
        return tryLogin(player, false, new OnLogin<ServerLocation>(){

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

                player.resetNullFields(old);
                players.update(player);

                return recreatePlayerIfNeeded(location, email, getIp(request));
            }

            @Override
            public ServerLocation onFailed(ServerLocation data) {
                throw new LoginException("User email or password/code is incorrect");
            }
        });
    }

    private ServerLocation recreatePlayerIfNeeded(ServerLocation current, String email, String callback) {
        return doIt(new DoItOnServers<ServerLocation>() {
            @Override
            public ServerLocation onGame() {
                // TODO test not exists - remove from other and create
                return dispatcher.registerIfNotExists(
                        current.getServer(),
                        current.getEmail(),
                        current.getPhone(),
                        getFullName(players.get(current.getEmail())),
                        current.getCode(),
                        callback);
            }

            @Override
            public ServerLocation onBalancer(ServerLocation updated) {
                if (updated == null) {
                    // TODO test me
                    return current;
                } else {
                    // TODO test me
                }

                players.updateServer(email, updated.getServer(), updated.getCode());

                return updated;
            }
        });
    }

    private <T> T tryLogin(Player player, boolean ignorePass, OnLogin<T> onLogin) {
        String email = player.getEmail();
        String phone = player.getPhone();
        String password = player.getPassword();
        String code = player.getCode();

        validator.checkEmail(email, CANT_BE_NULL); // TODO test me
        validator.checkCode(code, CAN_BE_NULL); // TODO test me

        Player exist = players.get(email);
        if (!isValid(exist, password, code, ignorePass)) {
            return onLogin.onFailed(new ServerLocation(email, phone, null, null, null));
        }

        String server = players.getServerByEmail(email);

        return onLogin.onSuccess(
                new ServerLocation(email,
                        phone,
                        Hash.getId(email, config.getEmailHash()),
                        exist.getCode(),
                        server
                ));
    }

    private boolean isValid(Player exist, String password, String code, boolean ignorePass) {
        if (exist == null || exist.getApproved() == Player.NOT_APPROVED) {
            return false;
        }

        if (ignorePass) { // TODO test me
            validator.checkNull("Password", password);
        } else {
            validator.checkString("Password", password);

            if (passwordEncoder.matches(password, exist.getPassword())) {
                return true;
            }
        }

        return exist.getCode().equals(code);
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

    @GetMapping(REMOVE + "/{player}")
    @ResponseBody
    public boolean remove(@PathVariable("player") String email) {

        Player player = players.get(email);
        if (player == null) {
            // TODO test me
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
                } else {
                    // TODO test me
                }
                return removed;
            }
        });
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
        return new ResponseEntity<>(ErrorTicketService.getPrintableMessage(e),
                HttpStatus.BAD_REQUEST);
    }

    // 401 for bad login
    @ExceptionHandler({LoginException.class})
    public ResponseEntity<String> handleFailedLoginException(LoginException e) {
        return new ResponseEntity<>(ErrorTicketService.getPrintableMessage(e),
                HttpStatus.UNAUTHORIZED);
    }

    // TODO test me
    @PostMapping(SETTINGS)
    @ResponseBody
    public boolean saveSettings(@RequestBody ConfigProperties config) {

        this.config.updateFrom(config);
        gameServers.update(config.getGame().getServers());

        return true;
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
    @ResponseBody
    public boolean saveGameSettings(@RequestBody GameSettings gameSettings) {
        dispatcher.updateGameSettings(gameSettings);

        return true;
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
    @ResponseBody
    public boolean invalidateCache(@PathVariable("mask") int whatToClean) {
        dispatcher.clearCache(whatToClean);
        return true;
    }

    @PostMapping(REGISTER + "/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<ServerLocation> confirmRegistration(@RequestBody PhoneCodeDTO input) {
        return ResponseEntity.ok(registrationService.
                confirmRegistration(phoneValidateNormalize(input.getPhone()), input.getCode()));
    }

    @GetMapping(CONFIRM + "/{player}/code")
    @ResponseBody
    public VerificationDTO getVerificationCode(@PathVariable("player") String email) {
        return new VerificationDTO(players.get(email));
    }

    @PostMapping(REGISTER + "/resend")
    @ResponseStatus(HttpStatus.OK)
    public void resendRegistrationCode(@RequestBody PhoneDTO input) {
        String phone = phoneValidateNormalize(input.getPhone());
        registrationService.resendConfirmRegistrationCode(phone);
    }

    @PostMapping(REGISTER + "/reset")
    @ResponseStatus(HttpStatus.OK)
    public void sendResetPasswordCode(@RequestBody PhoneDTO input) {
        String phone = phoneValidateNormalize(input.getPhone());
        registrationService.resendResetPasswordCode(phone);
    }

    @PostMapping(REGISTER + "/validate-reset")
    public ResponseEntity<String> validateResetPasswordCode(@RequestBody PhoneCodeDTO input) {
        String phone = phoneValidateNormalize(input.getPhone());
        boolean isResetAllowed = registrationService
                .validateCodeResetPassword(phone, input.getCode());
        if(isResetAllowed) {
            Player player = players.getByPhone(phone).orElseThrow(() -> new IllegalArgumentException("User not found"));
            if (game.existsOnServer(player.getServer(), player.getEmail())) {
                game.remove(player.getServer(), player.getEmail(), player.getCode());
            }
            registrationService.resetPassword(phone);
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.badRequest().body("Invalid verification code");
        }
    }

    private String phoneValidateNormalize(String phone) {
         validator.checkPhoneNumber(phone, CANT_BE_NULL);
         return validator.phoneNormalizer(phone);
    }
}
