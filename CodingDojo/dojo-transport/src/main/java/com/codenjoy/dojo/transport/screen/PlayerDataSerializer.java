package com.codenjoy.dojo.transport.screen;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public interface PlayerDataSerializer<TPlayer extends ScreenRecipient, TData extends ScreenData> {
    void writeValue(Writer writer, Map<TPlayer, TData> playerScreens) throws IOException;
}
