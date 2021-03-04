package com.codenjoy.dojo.web.rest;

import com.codenjoy.dojo.services.ChatService;
import com.codenjoy.dojo.web.controller.Validator;
import com.codenjoy.dojo.web.rest.pojo.PMessageShort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController("/rest/chat")
public class RestChatController {
    private final ChatService service;
    private final Validator validator;

    @GetMapping("/{roomId}/player/{playerId}/code/{code}/message/{fromTime}/all")
    ResponseEntity<?> getMessages(@PathVariable String roomId,
                                  @PathVariable String playerId,
                                  @PathVariable String code,
                                  @PathVariable LocalDateTime fromTime,
                                  @RequestParam(required = false, defaultValue = "5") int count) {
        validator.checkRoom(roomId, Validator.CANT_BE_NULL);
        validator.checkPlayerCode(playerId, code);
        validator.checkPlayerInRoom(playerId, roomId);
        return ResponseEntity.ok(service.getConversation(roomId, fromTime, count));
    }

    @PostMapping("/{roomId}/player/{playerId}/code/{code}/message")
    ResponseEntity<?> postMessage(
            @PathVariable String roomId,
            @PathVariable String playerId,
            @PathVariable String code,
            @RequestBody PMessageShort message) {
        validator.checkRoom(roomId, Validator.CANT_BE_NULL);
        validator.checkNotNull("message", message);
        validator.checkNotEmpty("message", message.getText());
        validator.checkPlayerCode(playerId, code);
        validator.checkPlayerInRoom(playerId, roomId);
        return ResponseEntity.ok(service.postMessage(roomId, playerId, message.getText()));
    }

    @DeleteMapping("/{roomId}/player/{playerId}/code/{code}/message/{messageId}")
    ResponseEntity<?> deleteMessage(
            @PathVariable String roomId,
            @PathVariable String playerId,
            @PathVariable String code,
            @PathVariable String messageId) {
        validator.checkRoom(roomId, Validator.CANT_BE_NULL);
        validator.checkPlayerCode(playerId, code);
        validator.checkPlayerInRoom(playerId, roomId);
        validator.checkNotEmpty("messageId", messageId);
        return ResponseEntity.ok(service.deleteMessage(messageId));
    }

}