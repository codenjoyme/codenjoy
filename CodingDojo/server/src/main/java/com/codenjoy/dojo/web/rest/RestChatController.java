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

/**
 * Rest сервис для работы с чатом.
 *
 * Все запросы отсуществляются от лица пользователя
 * с проверкой находится ли пользователь в заявленной комнате чатом которой интересуется,
 * и может ли пользователь осуществлять эти действия.
 *
 * Если пользователь - админ системы, то он может осуществлять все действия с чатом.
 */
@RestController
@Secured(GameAuthoritiesConstants.ROLE_USER)
@RequestMapping(RestChatController.URI)
@AllArgsConstructor
public class RestChatController {

    public static final String URI = "/rest/chat";
    private static final String DEFAULT_COUNT = "10";

    private Validator validator;
    private ChatService chat;

    /**
     * Возвращает сообщения для room-чата с проверкой пользователя.
     * Если пользователь не содержится в этом чате, сообщений он не получит.
     * Возвращается заданное в фильтре (count, afterId, beforeId, inclusive)
     * количество сообщений. Все параметры опциональны - в зависимости от
     * их комбинации будет возвращено то или иное количество.
     *
     * @param room Имя комнаты, сообщения чата которой интересуют.
     * @param count Количество сообщений, если указазаны afterId | beforeId
     *              но не оба одновременно.
     * @param afterId Получать сообщения после сообщения с этой id.
     * @param beforeId Получать сообщения до сообщения с этой id.
     * @param inclusive Включать ли в запрос сообщения с id afterId |& beforeId.
     * @param user Пользователь осуществляющий запрос.
     *             Если пользователя нет в комнате - сообщения получить неудастся.
     * @return Заданное количество сообщений из room-чата.
     */
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

    /**
     * Метод для отправки сообщений в room-чат от имени конкретного пользователя.
     *
     * @param room Имя комнаты в чат которой отправится сообщение.
     * @param message POJO с сообщением.
     * @param user Пользователь осуществляющтий запрос.
     *              Если пользователя нет в комнате - сообщение в чат не доставится.
     * @return В случае успеха вернется опубликованное сообщение с новой id и
 *              другими полями PMessage.
     */
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

    /**
     * Метод для отправки сообщений в thread-чат от имени конкретного пользователя.
     * Thread-чат - это все reply сообщения на любое конкретное сообщение в любом чате.
     *
     * @param room Имя комнаты в чат которой отправится сообщение.
     * @param id Указывает на родительское сообщение, reply к которому являются все
     *           сообщения этого thread-чата.
     * @param message POJO с сообщением.
     * @param user Пользователь осуществляющтий запрос.
     *              Если пользователя нет в комнате - сообщение в чат не доставится.
     * @return В случае успеха вернется опубликованное сообщение с новой id,
     *              ссылкой на topicId и другими полями PMessage.
     */
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

    /**
     * Получение конкретного сообщения room-chat по его id.
     *
     * @param room Имя комнаты, сообщения чата которой интересуют
     * @param id Идентификатор сообщения, которым интересуются.
     * @param user Пользователь осуществляющтий запрос.
     *            Если пользователя нет в комнате - сообщение получить неудастся.
     * @return Найденное сообщение со своими полями размещенными в PMessage.
     */
    @GetMapping("/{room}/messages/{id}")
    public ResponseEntity<?> getMessage(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.getMessage(id, room, user.getId()));
    }

    /**
     * Получение всех thread-chat сообщений, привязанных к конкретному сообщению
     * room-chat. Thread-чат - это все reply сообщения на любое конкретное
     * сообщение в любом чате.
     *
     * @param room Имя комнаты, сообщения чата которой интересуют.
     * @param id Идентификатор сообщения, reply-сообщениями которого интересуются.
     * @param user Пользователь осуществляющтий запрос.
     *            Если пользователя нет в комнате - сообщение получить неудастся.
     * @return Список всех найденных сообщений со своими полями размещенными в PMessage.
     */
    @GetMapping("/{room}/messages/{id}/replies")
    public ResponseEntity<?> getMessagesForTopic(
            @PathVariable(name = "room") String room,
            @PathVariable(name = "id") int id,
            @AuthenticationPrincipal Registration.User user)
    {
        validator.checkUser(user);

        return ResponseEntity.ok(chat.getTopicMessages(id, room, user.getId()));
    }

    /**
     * Удаление сообщения в room-chat или thread-chat от имени автора
     * этого сообщения.
     *
     * @param room Имя комнаты, в чате которой удаляют сообщение.
     * @param id Указывает на удаляемое сообщение.
     * @param user Пользователь осуществляющтий запрос.
     *           Если пользователя нет в комнате - сообщение удалить неудастся,
     *             ровно как если пользователь не является автором этого сообщения.
     * @return true - если сообщение успешно удалено.
     */
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