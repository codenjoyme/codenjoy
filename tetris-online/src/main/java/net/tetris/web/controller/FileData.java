package net.tetris.web.controller;

/**
 * User: serhiy.zelenin
 * Date: 10/28/12
 * Time: 12:41 PM
 */
public class FileData {
    private String timestamp;
    private String fileName;

    public FileData(String timestamp, String fileName) {
        this.timestamp = timestamp;
        this.fileName = fileName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getFileName() {
        return fileName;
    }

}
