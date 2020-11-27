package com.codenjoy.dojo.bomberman.client.simple;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import java.util.Arrays;
import java.util.List;

public class Message {
    
    private String message;
    private List<Object> data;
    private String type;
    
    protected Message(String type, String message, Object... data) {
        this.type = type;
        this.message = message;
        this.data = Arrays.asList(data);
    }

    public static Message get(String message, Object... data) {
        return new Message("MESSAGE", message, data);
    }

    public static Message error(String message, Object... data) {
        return new Message("ERROR", message, data);
    }
    
    @Override
    public String toString() {
        switch (data.size()) {
            case 0 : return String.format("[%s] %s",
                    type, message);

            case 1 : return String.format("[%s] %s: '%s'",
                    type, message, data.get(0));

            case 3 : return String.format("[%s] %s: '%s' at %s:%s",
                    type, message, data.get(2), data.get(0), data.get(1));

            case 4 : return String.format("[%s] %s: '%s' at %s:%s",
                    type, String.format(message, data.get(3)), data.get(2), data.get(0), data.get(1));

            default: return String.format("[%s] %s: %s",
                    type, message, data.toString());
        }
    }
}
