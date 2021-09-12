package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.services.Player;
import com.codenjoy.dojo.services.dao.Chat;
import com.codenjoy.dojo.services.multiplayer.Spreader;
import com.codenjoy.dojo.web.rest.pojo.PMessage;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

import static com.codenjoy.dojo.services.chat.ChatService.exception;
import static com.codenjoy.dojo.services.chat.ChatType.ROOM;
import static com.codenjoy.dojo.services.chat.ChatType.TOPIC;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

public class ChatControlImpl implements ChatControl {

    private final ChatService service;
    private final Chat chat;
    private final Spreader spreader;

    private final List<Player> player;
    private final String playerId;
    private final ChatControl.OnChange listener;

    public ChatControlImpl(ChatService service, Chat chat, Spreader spreader, String playerId, OnChange listener) {
        this.service = service;
        this.chat = chat;
        this.spreader = spreader;
        this.playerId = playerId;
        this.player = Arrays.asList(new Player(playerId));
        this.listener = listener;
    }

    private void inform(List<Player> players,
                        BiConsumer<List<PMessage>, String> listener,
                        List<PMessage> messages)
    {
        if (messages.isEmpty()) return;
        players.forEach(player -> listener.accept(messages, player.getId()));
    }

    private void informCreated(List<Player> players, List<PMessage> messages) {
        inform(players, listener::created, messages);
    }

    private void informDeleted(List<Player> players, List<PMessage> messages) {
        inform(players, listener::deleted, messages);
    }

    private List<Player> players(String room) {
        return spreader.players(room);
    }

    private List<Player> players(int topicId) {
        return spreader.players(topicId);
    }

    @Override
    public List<PMessage> getAllRoom(Filter filter) {
        List<PMessage> messages = service.getRoomMessages(playerId, filter);
        informCreated(player, messages);
        return messages;
    }

    @Override
    public List<PMessage> getAllTopic(int topicId, Filter filter) {
        List<PMessage> messages = service.getTopicMessages(topicId, playerId, filter);
        informCreated(player, messages);
        return messages;
    }

    @Override
    public List<PMessage> getAllField(Filter filter) {
        List<PMessage> messages = service.getFieldMessages(playerId, filter);
        informCreated(player, messages);
        return messages;
    }

    @Override
    public PMessage get(int id, String room) {
        PMessage message = service.getMessage(id, room, playerId);
        informCreated(player, asList(message));
        return message;
    }

    @Override
    public PMessage postRoom(String text, String room) {
        PMessage message = service.postMessageForRoom(text, room, playerId);
        informCreated(players(room), asList(message));
        return message;
    }

    @Override
    public PMessage postTopic(int topicId, String text, String room) {
        PMessage message = service.postMessageForTopic(topicId, text, room, playerId);
        informCreated(players(room), asList(message));
        return message;
    }

    @Override
    public PMessage postField(String text, String room) {
        PMessage message = service.postMessageForField(text, room, playerId);
        informCreated(players(message.getTopicId()), asList(message));
        return message;
    }

    @Override
    public boolean delete(int id, String room) {
        // TODO тут несколько запросов делается, не совсем оптимально
        Chat.Message message = chat.getMessageById(id);
        boolean deleted = service.deleteMessage(id, room, playerId);
        if (deleted) {
            informDeleted(asList(service.wrap(message)), room, rootFor(message));
        }
        return deleted;
    }

    private Chat.Message rootFor(Chat.Message message) {
        while (message.getType() == TOPIC) {
            Integer topicId = message.getTopicId();
            if (topicId == null) {
                throw exception("Topic message with null topic_id: " + message.getId());
            }
            message = chat.getAnyMessageById(topicId);
            if (message == null) {
                return null;
            }
        }
        return message;
    }

    private void informDeleted(List<PMessage> messages, String room, Chat.Message root) {
        ChatType type = (root == null)
                // TODO очень мало вероятно что такое случится,
                //   но если так - то информируем всех в комнате
                ? ROOM
                : root.getType();

        switch (type) {
            case ROOM:
                informDeleted(players(room), messages);
                break;
            case FIELD:
                informDeleted(players(root.getTopicId()), messages);
                break;
            default:
                throw exception("Should be only ROOM or FIELD: " +
                        messages.stream()
                                .map(PMessage::getId)
                                .map(Object::toString)
                                .collect(joining(",")));
        }
    }

}