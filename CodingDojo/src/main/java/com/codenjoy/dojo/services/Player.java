package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:49 AM
 */
public class Player {
    private String name;
    private String callbackUrl;
    private Protocol protocol;
    private PlayerScores scores;
    private Information info;

    public Player() {
    }

    public Player(String name, String callbackUrl, PlayerScores scores, Information info, Protocol protocol) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.info = info;
        this.protocol = protocol;
    }

    public String getName() {
        return name;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getScore() {
        return scores.getScore();
    }

    public String getMessage() {
        return info.getMessage();
    }

    public int getCurrentLevel() {
        return 0;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public class PlayerReader {

        public String getInformation() {
            return Player.this.getMessage();
        }

        public int getScores() {
            return Player.this.getScore();
        }

        public String getName() {
            return Player.this.getName();
        }

        public String getCallbackUrl() {
            return Player.this.getCallbackUrl();
        }

        public String getProtocol() {
            return Player.this.getProtocol().name();
        }
    }

    public static class PlayerBuilder {
        private Player player;
        private String information;
        private PlayerScores playerScores;
        private InformationCollector informationCollector;
        private int scores;
        private String name;
        private String callbackUrl;
        private Game game;
        private String protocol;

        public PlayerBuilder() {
            // do nothing
        }

        public PlayerBuilder(String name, String callbackUrl, int scores, String protocol) {
            this.name = name;
            this.callbackUrl = callbackUrl;
            this.scores = scores;
            this.protocol = protocol;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public void setInformation(String information) {
            this.information = information;
        }

        public void setProtocol(String protocol) {
            this.protocol = protocol;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setCallbackUrl(String callbackUrl) {
            this.callbackUrl = callbackUrl;
        }

        public Player getPlayer(GameType gameType) {
            if (player == null) {
                player = new Player();

                playerScores = gameType.getPlayerScores(scores);
                player.scores = playerScores;

                player.name = name;
                player.callbackUrl = callbackUrl;
                player.protocol = Protocol.valueOf(protocol.toUpperCase());

                informationCollector = new InformationCollector(playerScores);
                player.info = informationCollector;

                if (information != null) {
                    informationCollector.setInfo(information);
                }

                game = gameType.newGame(informationCollector);
            }
            return player;
        }

        public PlayerScores getPlayerScores() {
            return playerScores;
        }

        public InformationCollector getInformationCollector() {
            return informationCollector;
        }

        public Game getGame() {
            return game;
        }
    }

}
