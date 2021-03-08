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

    private void validateUserAndRoom(Registration.User user, String roomId) {
        String playerId = user.getId();
        String code = user.getCode();
        validator.checkRoom(roomId, Validator.CANT_BE_NULL);
        validator.checkPlayerCode(playerId, code);
        validator.checkPlayerInRoom(playerId, roomId);
    }

    @GetMapping("/{roomId}/messages")
    ResponseEntity<?> getMessages(@PathVariable String roomId,
                                  @RequestParam(required = false, defaultValue = "10") int count,
                                  @RequestParam(required = false) Integer afterId,
                                  @RequestParam(required = false) Integer beforeId,
                                  @AuthenticationPrincipal Registration.User user) {
        validateUserAndRoom(user, roomId);
        return ResponseEntity.ok(service.getMessages(roomId, count, afterId, beforeId));
    }

    @PostMapping("/{roomId}/messages")
    ResponseEntity<?> postMessage(
            @PathVariable String roomId,
            @NotNull @RequestBody PMessageShort message,
            @AuthenticationPrincipal Registration.User user) {
        validateUserAndRoom(user, roomId);
        return ResponseEntity.ok(service.postMessage(message.getText(), roomId, user));
    }

    @GetMapping("/{roomId}/messages/{messageId}")
    ResponseEntity<?> getMessage(@PathVariable String roomId,
                                 @PathVariable int messageId,
                                 @AuthenticationPrincipal Registration.User user) {
        validateUserAndRoom(user, roomId);
        return ResponseEntity.ok(service.getMessage(messageId, roomId));
    }

    @DeleteMapping("/{roomId}/messages/{messageId}")
    ResponseEntity<?> deleteMessage(
            @PathVariable String roomId,
            @PathVariable int messageId,
            @AuthenticationPrincipal Registration.User user) {
        validateUserAndRoom(user, roomId);
        return ResponseEntity.ok(service.deleteMessage(messageId, roomId, user));
    }

}
