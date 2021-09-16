package com.codenjoy.dojo.services.helper;

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

import com.codenjoy.dojo.services.Deal;
import com.codenjoy.dojo.services.Deals;
import com.codenjoy.dojo.services.chat.ChatControl;
import com.codenjoy.dojo.services.controller.Controller;

import java.util.function.Consumer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

public class ChatDealsUtils {

    public static void setupChat(Deals deals, Consumer<Deal> next) {
        deals.onAdd(deal -> {
            setupChat(deal);
            if (next != null) {
                next.accept(deal);
            }
        });
    }

    public static void setupChat(Deal deal) {
        deal.setChat(mock(ChatControl.class));
    }

    public static void setupChat(Controller chatController) {
        doAnswer(inv -> {
            Deal deal = (Deal)inv.getArguments()[0];
            setupChat(deal);
            return null;
        }).when(chatController).register(any(Deal.class));
    }
}
