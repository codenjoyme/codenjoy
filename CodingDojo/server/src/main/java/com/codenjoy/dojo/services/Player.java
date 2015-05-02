package com.codenjoy.dojo.services;

import com.codenjoy.dojo.transport.screen.ScreenRecipient;

public class Player implements ScreenRecipient {

    private String name;
    private String code;
    private String callbackUrl;
    private Protocol protocol;
    private String gameName;
    private String password;
    private PlayerScores scores;
    private Information info;
    private GameType gameType;

    public Player() {
    }

    public Player(String name, String callbackUrl, GameType gameType, PlayerScores scores, Information info, Protocol protocol) {
        this.name = name;
        this.callbackUrl = callbackUrl;
        this.gameType = gameType;
        this.scores = scores;
        this.info = info;
        this.protocol = protocol;
    }

    public GameType getGameType() {
        return gameType;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) return false;
        if (this == NullPlayer.INSTANCE && (o != NullPlayer.INSTANCE && o != NullPlayerGame.INSTANCE)) return false;

        if (o instanceof Player) {
            Player p = (Player)o;

            return (p.name.equals(name));
        }

        if (o instanceof PlayerGame) {
            PlayerGame pg = (PlayerGame)o;

            return pg.getPlayer().equals(this);
        }

        return false;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public int hashCode() {
        return (name + code).hashCode();
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

    public String getGameName() {
        return (gameType != null)?gameType.name():gameName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
