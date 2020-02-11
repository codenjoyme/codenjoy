package com.codenjoy.dojo.bomberman.client.simple;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ErrorMessage {
    
    private String message;
    private List<Object> data;
    
    public ErrorMessage(String message, Object... data) {
        this.message = message;
        this.data = Arrays.asList(data);
    }
    
    @Override
    public String toString() {
        switch (data.size()) {
            case 0 : return String.format("[ERROR] %s", message);
            case 1 : return String.format("[ERROR] %s: '%s'", message, data.get(0));
            case 3 : return String.format("[ERROR] %s: '%s' at %s:%s",
                    message, data.get(2), data.get(0), data.get(1));
            case 4 : return String.format("[ERROR] %s: '%s' at %s:%s",
                    String.format(message, data.get(3)), data.get(2), data.get(0), data.get(1));
            default: return String.format("[ERROR] %s: %s", message, data.toString());
        }
    }
}
