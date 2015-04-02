package com.codenjoy.dojo.transport.screen;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * User: serhiy.zelenin
 * Date: 9/26/13
 * Time: 2:18 PM
 */
public interface PlayerDataSerializer<TPlayer extends ScreenRecipient, TData extends ScreenData> {
    void writeValue(Writer writer, Map<TPlayer, TData> playerScreens) throws IOException;
}
