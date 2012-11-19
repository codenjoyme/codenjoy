package net.tetris.services;

import net.tetris.dom.FigureQueue;
import net.tetris.dom.Levels;

public class Player {
    private String name;
    private String callbackUrl;
    private PlayerScores scores;
    private Levels levels;
    private Information info;

    public Player() {
    }

    public Player(String name, String callbackUrl, PlayerScores scores, Levels levels, Information info) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.levels = levels;
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

    public int getTotalRemovedLines() {
        return levels.getTotalRemovedLines();
    }

    public String getMessage() {
        return info.getMessage();
    }

    public int getCurrentLevelNumber() {
        return levels.getCurrentLevelNumber();
    }

    public String getNextLevelIngoingCriteria() {
        return levels.getCurrentLevel().getNextLevelIngoingCriteria();
    }

    public class PlayerReader {

        public String getInformation() {
            return Player.this.getMessage();
        }

        public Levels.LevelsReader getLevels() {
            return levels.new LevelsReader();
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
        private FigureQueue playerQueue;
        private Levels.LevelsBuilder levelsBuilder;
        private InformationCollector informationCollector;
        private int scores;
        private String name;
        private String callbackUrl;
        private Levels levels;
        private FigureQueue queue;

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

        public Player getPlayer() {
            if (player == null) {
                player = new Player();

                playerScores = new PlayerScores(scores);
                player.scores = playerScores;

                player.name = name;
                player.callbackUrl = callbackUrl;

                informationCollector = new InformationCollector(playerScores);
                player.info = informationCollector;
                if (information != null) {
                    informationCollector.setInfo(information);
                }

                player.levels = getLevels();
                player.levels.setChangeLevelListener(informationCollector);
            }
            return player;
        }

        public PlayerScores getPlayerScores() {
            return playerScores;
        }

        public void forLevels(FigureQueue queue, Levels levels) {
            this.levels = levels;
            this.queue = queue;
        }

        public void setLevels(Levels.LevelsBuilder levels) {
            this.levelsBuilder = levels;
        }

        public FigureQueue getPlayerQueue() {
            if (queue != null) {
                return queue;
            }
            return levelsBuilder.getFigureQueue();
        }

        public InformationCollector getInformationCollector() {
            return informationCollector;
        }

        public Levels getLevels() {
            if (levels != null) {
                return levels;
            }
            return levelsBuilder.getLevels();
        }
    }
}
