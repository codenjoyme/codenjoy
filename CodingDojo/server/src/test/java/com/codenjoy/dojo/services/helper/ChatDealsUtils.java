package com.codenjoy.dojo.services.helper;

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
