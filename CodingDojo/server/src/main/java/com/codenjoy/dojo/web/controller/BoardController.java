package com.codenjoy.dojo.web.controller;

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


import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.GameServerService;
import com.codenjoy.dojo.services.GameServiceImpl;
import com.codenjoy.dojo.services.GameType;
import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.PlayerService;
import com.codenjoy.dojo.services.dao.FeedbackSaver;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.dao.SubscriptionSaver;
import com.codenjoy.dojo.services.grpc.QueryClient;
import com.codenjoy.dojo.services.multiplayer.MultiplayerType;
import com.codenjoy.dojo.services.nullobj.NullGameType;
import com.codenjoy.dojo.services.nullobj.NullPlayer;
import com.codenjoy.dojo.services.playerdata.QuerySubscription;
import com.codenjoy.dojo.services.security.RegistrationService;
import com.dojo.notifications.Query;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.codenjoy.dojo.web.controller.Validator.CANT_BE_NULL;
import static com.codenjoy.dojo.web.controller.Validator.CAN_BE_NULL;

@Controller
@RequestMapping(BoardController.URI)
@RequiredArgsConstructor
public class BoardController {

    public static final String URI = "/board";

    private final PlayerService playerService;
    private final Registration registration;
    private final Validator validator;
    private final ConfigProperties properties;
    private final RegistrationService registrationService;
    private final GameServerService gameServerService;
    private final PlayerGameSaver playerGameSaver;
    private final FeedbackSaver feedbackSaver;
    private final SubscriptionSaver subscriptionSaver;
    private final QueryClient queryClient;

    @GetMapping("/player/{player}")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam(name = "only", required = false) Boolean justBoard) {
        validator.checkPlayerId(id, CANT_BE_NULL);

        return boardPlayer(model, id, null, justBoard, (String) model.get("game"));
    }

    @GetMapping(value = "/player/{player}", params = {"code", "remove"})
    public String removePlayer(@PathVariable("player") String id, @RequestParam("code") String code) {
        validator.checkPlayerCode(id, code);

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        playerService.remove(player.getId());
        return "redirect:/";
    }

    @GetMapping(value = "/player/{player}", params = "code")
    public String boardPlayer(ModelMap model,
                              @PathVariable("player") String id,
                              @RequestParam("code") String code,
                              @RequestParam(name = "only", required = false) Boolean justBoard,
                              @RequestParam(name = "game", required = false, defaultValue = "") String game) {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);
        validator.checkGame(game, CAN_BE_NULL); // TODO а зачем тут вообще game?

        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register?id=" + id;
        }

        populateBoardAttributes(model, code, player, false);

        justBoard = justBoard != null && justBoard;
        model.addAttribute("justBoard", justBoard);
        return justBoard ? "board-only" : "board";
    }

    @GetMapping("/rejoining/{game}")
    public String rejoinGame(ModelMap model, @PathVariable("game") String game,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user) {
        validator.checkGame(game, CANT_BE_NULL);

        if (user == null) {
            return "redirect:/login?" + "game" + "=" + game;
        }

        // TODO ROOM а надо ли тут этот метод вообще, ниже есть более универсальный?
        // TODO ROOM так как есть rest методы то может вообще убрать отсюда этих двоих?
        String room = game;

        registrationService.register(user.getId(), user.getCode(), game, room, request.getRemoteAddr(), user.getGitHubUsername(), user.getSlackEmail());

        List<Query> allActiveQueries = queryClient.getQueriesForContest(game);
        List<String> userQueryIds = subscriptionSaver.getUserQueriesForContest(user.getId(), game);

        subscribeToNewQueries(user, allActiveQueries, userQueryIds, game);
        removeOldQueries(user, allActiveQueries, userQueryIds, game);

        return rejoinGame(model, game, room, request, user);
    }

    @GetMapping("/rejoining/{game}/room/{room}")
    public String rejoinGame(ModelMap model, @PathVariable("game") String game,
                             @PathVariable("room") String room,
                             HttpServletRequest request,
                             @AuthenticationPrincipal Registration.User user) {
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);
        String repositoryUrl = gameServerService.createOrGetRepositoryWithGame(user.getGitHubUsername(), game);

        Player player = playerService.get(user.getCode());
        if (player == NullPlayer.INSTANCE) {
            return registrationService.connectRegisteredPlayer(user.getCode(), request, user.getId(), room, game, user.getGitHubUsername(), player.getSlackEmail());
        }

        populateBoardAttributes(model, player.getCode(), player, false);
        return "board";
    }

    private void populateBoardAttributes(ModelMap model, String code, Player player, boolean allPlayersScreen) {
        populateBoardAttributes(model, code, player.getGame(), player.getRoom(), player.getGameOnly(), player.getId(),
                player.getReadableName(), player.getGitHubUsername(), allPlayersScreen);
    }

    private void populateBoardAttributes(ModelMap model, String code, String game, String room, String gameOnly,
                                         String playerId, String readableName, String github, boolean allPlayersScreen) {
        model.addAttribute("code", code);
        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("allPlayersScreen", false);
        model.addAttribute("game", game);
        model.addAttribute("gameOnly", gameOnly);
        model.addAttribute("playerId", playerId);
        model.addAttribute("readableName", readableName);
        model.addAttribute("github", github);
        model.addAttribute("allPlayersScreen", allPlayersScreen); // TODO так клиенту припрутся все доски и даже не из его игры, надо фиксить dojo transport
        model.addAttribute("playerScoreCleanupEnabled", properties.isPlayerScoreCleanupEnabled());
        model.addAttribute("subscribed", getQueriesForGame(playerId, game));
        model.addAttribute("repositoryURL", playerGameSaver.getRepositoryByPlayerIdForGame(playerId, game));
    }

    @GetMapping(value = "/log/player/{player}", params = {"game", "room"})
    public String boardPlayerLog(ModelMap model, @PathVariable("player") String id,
                                 @RequestParam("game") String game,
                                 @RequestParam("room") String room) {
        validator.checkPlayerId(id, CANT_BE_NULL);
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkRoom(room, CANT_BE_NULL);

        Optional<Registration.User> user = registration.getUserById(id);
        if (!user.isPresent()) {
            return "redirect:/register?id=" + id;
        }

        model.addAttribute("game", game);
        model.addAttribute("room", room);
        model.addAttribute("gameOnly", GameServiceImpl.removeNumbers(game));
        model.addAttribute("playerId", user.get().getId());
        model.addAttribute("readableName", user.get().getReadableName());
        model.addAttribute("github", user.get().getGitHubUsername());

        return "board-log";
    }

    @GetMapping("/")
    public String boardAll() {
        GameType gameType = playerService.getAnyGameWithPlayers();
        if (gameType == NullGameType.INSTANCE) {
            return "redirect:/register";
        }
        return "redirect:/board/game/" + gameType.name();
    }

    @GetMapping("/game/{game}")
    public String boardAllGames(ModelMap model,
                                @PathVariable("game") String game,
                                @RequestParam(value = "code", required = false) String code,
                                @AuthenticationPrincipal Registration.User user) {
        // TODO возможно тут CAN_BE_NULL, иначе проверка (game == null) никогда не true
        validator.checkGame(game, CANT_BE_NULL);
        validator.checkCode(code, CAN_BE_NULL);

        if (game == null) {
            return "redirect:/board" + code(code);
        }

        String room = game; // TODO закончить с room

        Player player = playerService.getRandom(game);
        if (player == NullPlayer.INSTANCE) {
            // TODO а это тут вообще надо?
            return "redirect:/register?" + "game" + "=" + game;
        }
        GameType gameType = player.getGameType(); // TODO а тут точно сеттинги румы а не игры?
        if (gameType.getMultiplayerType(gameType.getSettings()) == MultiplayerType.MULTIPLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        if (user != null && code == null) {
            code = user.getCode();
        }

        populateBoardAttributes(model, code, game, room, player.getGameOnly(), null, null, null, true);
        return "board";
    }

    @GetMapping(value = "/", params = "code")
    public String boardAll(ModelMap model, @RequestParam("code") String code) {
        validator.checkCode(code, CAN_BE_NULL);

        String id = registration.getIdByCode(code);
        Player player = playerService.get(id);
        if (player == NullPlayer.INSTANCE) {
            player = playerService.getRandom(null);
        }
        if (player == NullPlayer.INSTANCE) {
            return "redirect:/register";
        }
        GameType gameType = player.getGameType(); // TODO а тут точно сеттинги румы а не игры?
        if (gameType.getMultiplayerType(gameType.getSettings()) != MultiplayerType.SINGLE) {
            return "redirect:/board/player/" + player.getId() + code(code);
        }

        model.addAttribute("code", code);
        model.addAttribute("game", player.getGame());
        model.addAttribute("room", player.getRoom());
        model.addAttribute("gameOnly", player.getGameOnly());
        model.addAttribute("playerId", player.getId());
        model.addAttribute("readableName", player.getReadableName());
        model.addAttribute("github", player.getGitHubUsername());
        model.addAttribute("allPlayersScreen", true);
        return "board";
    }

    @PostMapping("/feedback")
    public String subscribeOrUnsubscribe(HttpServletRequest request) {
        String playerId = request.getParameter("playerId").replace("\"", "");
        String game = request.getParameter("game").replace("\"", "");
        String feedbackText = request.getParameter("feedback");
        System.out.println(feedbackText);

//        if (!feedbackText.equals("")) {
//            System.out.println(feedbackText);
        List<Query> queries = queryClient.getQueriesForContest(game);
        for (int i = 1; i <= queries.size(); i++) {
            subscriptionSaver.updateEmailSubscription(playerId, String.valueOf(i), getCheckBoxValue(i, "email", request), game);
            subscriptionSaver.updateSlackSubscription(playerId, String.valueOf(i), getCheckBoxValue(i, "slackEmail", request), game);
        }
        feedbackSaver.saveFeedback(playerId, game, feedbackText);
//        }

        String code = request.getParameter("code").replace("\"", "");
        return "redirect:/board/player/" + playerId + code(code);
    }

    private String code(@RequestParam("code") String code) {
        return (code != null) ? "?code=" + code : "";
    }

    private List<QuerySubscription> getQueriesForGame(String playerId, String game){
        List<Query> queries = queryClient.getQueriesForContest(game);

        return queries.stream()
                .map(query -> new QuerySubscription(query,
                        subscriptionSaver.getEmailValueForQuery(playerId, String.valueOf(query.getId()), game),
                        subscriptionSaver.getSlackValueForQuery(playerId, String.valueOf(query.getId()), game)))
                .collect(Collectors.toList());

    }

    private boolean getCheckBoxValue(int queryId, String forWhichCheckBox, HttpServletRequest request) {
//        if(request.getParameter("email" + queryId) == null){
//            return true;
//        }
        return Boolean.parseBoolean(request.getParameter(forWhichCheckBox + queryId));
    }

    private void subscribeToNewQueries(Registration.User user, List<Query> allActiveQueries, List<String> userQueryIds, String game){
        String slackEmail = registration.getSlackEmailById(user.getId());
        allActiveQueries.stream()
                .filter(query -> !userQueryIds.contains(String.valueOf(query.getId())))
                .forEach(query -> subscriptionSaver.saveSubscription(user.getId(), query.getId(), true, !(slackEmail.equals("")), game));

    }

    private void removeOldQueries(Registration.User user, List<Query> allActiveQueries, List<String> userQueryIds, String game){
        List<String> allActiveIds = allActiveQueries.stream().map(query -> String.valueOf(query.getId())).collect(Collectors.toList());
        userQueryIds.stream().filter(s -> !allActiveIds.contains(s)).forEach(query -> subscriptionSaver.tryDeleteSubscription(user.getId(),String.valueOf(query), game));
    }
}
