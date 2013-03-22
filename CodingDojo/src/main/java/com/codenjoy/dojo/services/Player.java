package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:49 AM
 */
public class Player {
    private String name;
    private String callbackUrl;
    private PlayerScores scores;
    private Information info;

    public Player() {
    }

    public Player(String name, String callbackUrl, PlayerScores scores, Information info) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.info = info;
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

        public PlayerBuilder(String name, String callbackUrl, int scores) {
            this.name = name;
            this.callbackUrl = callbackUrl;
            this.scores = scores;
        }

        public void setScores(int scores) {
            this.scores = scores;
        }

        public void setInformation(String information) {
            this.information = information;
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
