package com.codenjoy.dojo.transport.screen;

import java.util.Map;

public interface ScreenSender<TPlayer extends ScreenRecipient, TData extends ScreenData> {
    void sendUpdates(Map<TPlayer, TData> playerScreens);
}
