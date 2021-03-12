package com.codenjoy.dojo.web.rest;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.services.ChatService;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.security.GameAuthoritiesConstants;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessageShort;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RestController
@Secured(GameAuthoritiesConstants.ROLE_USER)
@RequestMapping(RestChatController.URI)
@AllArgsConstructor
public class RestChatController {

    public static final String URI = "/rest/chat";
    private static final String DEFAULT_COUNT = "10";

    private Validator validator;
    private ChatService chat;

    @GetMapping("/{room}/messages")
    public ResponseEntity<?> getMessages(
            @PathVariable(name = "room") String room,
            @RequestParam(name = "count", required = false, defaultValue = DEFAULT_COUNT) int count,
            @RequestParam(name = "afterId", required = false) Integer afterId,
            @RequestParam(name = "beforeId", required = false) Integer beforeId,
            @RequestParam(name = "inclusive", required = false, defaultValue = "false") boolean inclusive,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.getMessages(room, count,
                afterId, beforeId, inclusive, user.getId()));
    }

    @PostMapping("/{room}/messages")
    public ResponseEntity<?> postMessage(
            @PathVariable(name = "room") String room,
            @NotNull @RequestBody PMessageShort message,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.postMessage(null, message.getText(),
                room, user.getId()));
    }

    @PostMapping("/{room}/messages/{id}/replies")
    public ResponseEntity<?> postMessageForTopic(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @NotNull @RequestBody PMessageShort message,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.postMessage(id, message.getText(),
                room, user.getId()));
    }

    @GetMapping("/{room}/messages/{id}")
    public ResponseEntity<?> getMessage(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.getMessage(id, room, user.getId()));
    }

    @GetMapping("/{room}/messages/{id}/replies")
    public ResponseEntity<?> getMessagesForTopic(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.getTopicMessages(id, room, user.getId()));
    }

    @DeleteMapping("/{room}/messages/{id}")
    public ResponseEntity<?> deleteMessage(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.deleteMessage(id, room, user.getId()));
    }

}