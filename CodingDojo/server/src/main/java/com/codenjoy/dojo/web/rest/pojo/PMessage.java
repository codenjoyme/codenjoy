package com.codenjoy.dojo.web.rest.pojo;

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

import com.codenjoy.dojo.services.dao.Chat;
import lombok.Data;

@Data
public class PMessage {

    private final int id;
    private final String text;
    private final String room;
    private final int type;
    private final Integer topicId;
    private final String playerId;
    private final String playerName;
    private final long time;

    public static PMessage from(Chat.Message message, String playerName) {
        return new PMessage(
                message.getId(),
                message.getText(),
                message.getRoom(),
                message.getType().id(),
                message.getTopicId(),
                message.getPlayerId(),
                playerName,
                message.getTime()
        );
    }
}
