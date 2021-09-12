package com.codenjoy.dojo.services.controller.chat;

import lombok.Getter;

@Getter
public class Error {

    private final String error;
    private final String message;

    public Error(Exception exception) {
        this.error = exception.getClass().getSimpleName();
        this.message = exception.getMessage();
    }

    public interface OnError {
        void on(Error error);
    }
}
