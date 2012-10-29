package net.tetris.web.controller;

/**
 * User: serhiy.zelenin
 * Date: 10/29/12
 * Time: 8:51 PM
 */
public class GameLogsData {
    private String[] fileNames;

    public GameLogsData(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String[] getFileNames() {
        return fileNames;
    }
}
