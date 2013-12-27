package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.screen.ScreenRecipient;
import org.apache.commons.lang.StringUtils;

/**
 * User: oleksandr.baglai
 * Date: 10/1/12
 * Time: 3:49 AM
 */
public class Player implements ScreenRecipient {
    public static final Player NULL = new NullPlayer();

    private String name;
    private String code;
    private String callbackUrl;
    private Protocol protocol;
    private PlayerScores scores;
    private Information info;
    private String password;

    public Player() {
    }

    public Player(String name, String password, String callbackUrl, PlayerScores scores, Information info, Protocol protocol) {
        this.name = name;
        this.code = makeCode(name, password);
        this.callbackUrl = callbackUrl;
        this.scores = scores;
        this.info = info;
        this.protocol = protocol;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == Player.NULL && (o != Player.NULL && o != PlayerGame.NULL)) return false;

        if (o instanceof Player) {
            Player p = (Player)o;

            return (p.name.equals(name) && p.code.equals(code));
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            return pg.getPlayer().equals(this);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (name + code).hashCode();
    }

    public void setPassword(String password) {
        this.password = password;
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

    public int clearScore() {
        return scores.clear();
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

    public void setCode(String code) {
        this.code = code;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getCode() {
        return code;
    }

    public static String makeCode(String name, String password) {
        return "" + Math.abs(name.hashCode()) + Math.abs(password.hashCode());
    }

    public String getPassword() {
        return password;
    }

    public boolean itsMe(String password) {
        return !StringUtils.isEmpty(code) && password != null &&
                code.equals(Player.makeCode(name, password));
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

        public String getCode() {
            return Player.this.getCode();
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
        private String code;
        private String callbackUrl;
        private Game game;
        private String protocol;

        public PlayerBuilder() {
            // do nothing
        }

        public PlayerBuilder(String name, String password, String callbackUrl, int scores, String protocol) {
            this.name = name;
            this.code = makeCode(name, password);
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

        public void setCode(String code) {
            this.code = code;
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
                player.code = code;
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
