package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.screen.ScreenRecipient;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 20:53
 */
public class ChatData implements ScreenRecipient {

    @Override
    public String toString() {
        return "chatLog";
    }

    @Override
    public String getName() {
        return "chatLog";
    }
}
