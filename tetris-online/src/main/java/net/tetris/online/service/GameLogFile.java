package net.tetris.online.service;

import net.tetris.dom.Figure;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: serhiy.zelenin
 * Date: 10/27/12
 * Time: 9:08 PM
 */
public class GameLogFile {
    private static Logger logger = LoggerFactory.getLogger(GameLogFile.class);

    private ServiceConfiguration configuration;
    private String playerName;
    private String timeStamp;
    private File playerLogsDir;
    private File logFile;
    private PrintWriter printWriter;
    private boolean openWrite;
    private BufferedReader reader;
    private boolean openRead;
    private String currentLine;
    private Pattern figurePattern = Pattern.compile("\\?figure=\\s*(\\w*)");

    public GameLogFile(ServiceConfiguration configuration, String playerName, String timeStamp) {
        this.configuration = configuration;
        this.playerName = playerName;
        this.timeStamp = timeStamp;
        playerLogsDir = new File(configuration.getLogsDir(), playerName);
        logFile = new File(playerLogsDir, timeStamp);
    }

    private void openWrite() {
        playerLogsDir.mkdirs();

        try {
            printWriter = new PrintWriter(new BufferedWriter(new FileWriter(logFile)));
            openWrite = true;
        } catch (IOException e) {
            logger.error("Unable to create game log for player " + playerName, e);
        }
    }

    public void close() {
        IOUtils.closeQuietly(printWriter);
        IOUtils.closeQuietly(reader);
    }

    public void log(String request, String response) {
        if (!openWrite) {
            openWrite();
        }
        printWriter.println(request + "@" + response);
        if (printWriter.checkError()) {
            logger.error("Unable to log record: '{}' for player {}", request + "@" + response, playerName);
        }
    }

    public boolean readNextStep() throws IOException {
        if (!openRead) {
            try {
                openRead();
            } catch (FileNotFoundException e) {
                logger.warn("Requested game log file for player " + playerName + "does not exist", e);
                return false;
            }
        }
        currentLine = reader.readLine();
        return currentLine != null;
    }

    private void openRead() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader(logFile));
        openRead = true;
    }

    public String getPath() {
        return logFile.getAbsolutePath();
    }

    public Figure.Type getCurrentFigure() {
        Matcher matcher = figurePattern.matcher(currentLine);
        if (matcher.find()) {
            try {
                return Figure.Type.valueOf(matcher.group(1));
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
        return null;
    }

    public String getCurrentResponse() {
        return currentLine.split("\\@")[1];
    }
}
