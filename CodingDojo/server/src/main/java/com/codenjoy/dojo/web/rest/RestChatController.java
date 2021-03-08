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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@RestController("/rest/chat")
@Secured(GameAuthoritiesConstants.ROLE_USER)
public class RestChatController {

    private final ChatService service;
    private final Validator validator;

    private void validate(Registration.User user, String roomId) {
        String playerId = user.getId();
        String code = user.getCode();
        validator.checkRoom(roomId, Validator.CANT_BE_NULL);
        validator.checkPlayerCode(playerId, code);
        validator.checkPlayerInRoom(playerId, roomId);
    }

    @GetMapping("/{room}/messages")
    ResponseEntity<?> getMessages(
            @PathVariable(name = "room") String room,
            @RequestParam(name = "count", required = false, defaultValue = "10") int count,
            @RequestParam(name = "after", required = false) Integer after,
            @RequestParam(name = "before", required = false) Integer before,
            @AuthenticationPrincipal Registration.User user)
    {
        validate(user, room);
        return ResponseEntity.ok(service.getMessages(room, count, after, before));
    }

    @PostMapping("/{room}/messages")
    ResponseEntity<?> postMessage(
            @PathVariable(name = "room") String room,
            @NotNull @RequestBody PMessageShort message,
            @AuthenticationPrincipal Registration.User user)
    {
        validate(user, room);
        return ResponseEntity.ok(service.postMessage(message.getText(), room, user));
    }

    @GetMapping("/{room}/messages/{id}")
    ResponseEntity<?> getMessage(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validate(user, room);
        return ResponseEntity.ok(service.getMessage(id, room));
    }

    @DeleteMapping("/{room}/messages/{id}")
    ResponseEntity<?> deleteMessage(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validate(user, room);
        return ResponseEntity.ok(service.deleteMessage(id, room, user));
    }

}