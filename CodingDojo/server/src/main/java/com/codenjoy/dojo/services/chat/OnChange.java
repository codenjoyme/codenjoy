package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.web.rest.pojo.PMessage;

import java.util.List;

public interface OnChange {

    /**
     * @param messages Удаленные сообщения.
     * @param type В каком типе чата это обновление производить.
     * @param playerId Игрок в том же чате, которого надо проинформировать.
     */
    void deleted(List<PMessage> messages, ChatType type, String playerId);

    /**
     * @param messages Созданные сообщения.
     * @param type В каком типе чата это обновление производить.
     * @param playerId Игрок в том же чате, которого надо проинформировать.
     */
    void created(List<PMessage> messages, ChatType type, String playerId);
}
