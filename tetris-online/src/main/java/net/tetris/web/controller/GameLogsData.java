package net.tetris.web.controller;

import org.apache.commons.lang.ArrayUtils;

import java.util.Arrays;
import java.util.List;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 8:51 PM
 */
public class GameLogsData {
    private List<String> fileNames;

    public GameLogsData(List<String> fileNames) {
        this.fileNames = fileNames;
    }

    public List<String> getFileNames() {
        return fileNames;
    }
}
