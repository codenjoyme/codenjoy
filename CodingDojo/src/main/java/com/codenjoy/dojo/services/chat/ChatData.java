package com.codenjoy.dojo.services.chat;

import com.codenjoy.dojo.services.Player;

/**
 * User: sanja
 * Date: 23.09.13
 * Time: 20:53
 */
public class ChatData extends Player { // TODO это просто маркер интерфейс, я хотел запулить клиентам Чат информацию некрасиво, а что делать?
    @Override
    public String toString() {
        return "chatLog";
    }
}
