package com.codenjoy.dojo.services.controller.chat;

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

import com.codenjoy.dojo.services.Tickable;
import com.codenjoy.dojo.services.chat.ChatAuthority;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
public class LazyChatCommand extends ChatCommand implements Tickable {

    private final List<ChatRequest> requests;
    private final Error.OnError onError;

    public LazyChatCommand(ChatAuthority chat, Error.OnError onError) {
        super(chat);
        requests = new CopyOnWriteArrayList<>();
        this.onError = onError;
    }

    public void process(ChatRequest request) {
        requests.add(request);
    }

    @Override
    public synchronized void tick() {
        for (ChatRequest request : requests) {
            try {
                super.process(request);
            } catch (Exception exception) {
                log.error("Error during chat request: " + request, exception);
                onError.on(new Error(exception));
            }
        }
        requests.clear();
    }
}