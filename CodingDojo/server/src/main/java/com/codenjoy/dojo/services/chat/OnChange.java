package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.web.rest.pojo.PMessage;

import java.util.List;

public interface OnChange {

    /**
     * @param messages Удаленные сообщения
     * @param playerId Игрок в том же чате, которого надо проинформировать.
     */
    void deleted(List<PMessage> messages, String playerId);

    /**
     * @param messages Созданные сообщения.
     * @param playerId Игрок в том же чате, которого надо проинформировать.
     */
    void created(List<PMessage> messages, String playerId);
}
