package net.tetris.online.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

/**
 * User: serhiy.zelenin
 * Date: 11/7/12
 * Time: 11:19 PM
 */
public class GameLog {
    @JsonProperty
    @JsonUnwrapped
    private String fileName;

    public GameLog(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
