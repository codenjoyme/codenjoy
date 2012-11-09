package net.tetris.online.service;

/**
* User: serhiy.zelenin
* Date: 11/9/12
* Time: 10:36 PM
*/
class ReplayRequest {
    private String playerName;
    private String timestamp;
    private int replayId;
    private GameLogFile gameLogFile;

    public ReplayRequest(ServiceConfiguration configuration, String playerName, String timestamp, int replayId) {
        this.setPlayerName(playerName);
        this.setTimestamp(timestamp);
        this.setReplayId(replayId);
        this.gameLogFile = new GameLogFile(configuration, playerName, timestamp);
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getReplayId() {
        return replayId;
    }

    public void setReplayId(int replayId) {
        this.replayId = replayId;
    }

    public GameLogFile getGameLogFile() {
        return gameLogFile;
    }
}
