package net.tetris.online.web.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 8:51 PM
 */
public class GameLogData {
    private List<String> fileNames;

    public GameLogData(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<String> getRows() {
        return fileNames;
    }
}
