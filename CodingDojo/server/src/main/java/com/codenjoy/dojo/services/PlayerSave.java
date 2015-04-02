package com.codenjoy.dojo.services;

import org.apache.commons.lang.StringUtils;

public class PlayerSave {

    public static final PlayerSave NULL = new PlayerSave(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, 0, StringUtils.EMPTY);

    private String protocol;
    private int score;
    private String callbackUrl;
    private String gameName;
    private String name;

    public PlayerSave(String name, String callbackUrl, String gameName, int score, String protocol) {
        this.name = name;
        this.gameName = gameName;
        this.callbackUrl = callbackUrl;
        this.score = score;
        this.protocol = protocol;
    }

    public String getProtocol() {
        return protocol;
    }

    public int getScore() {
        return score;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public String getGameName() {
        return gameName;
    }

    public String getName() {
        return name;
    }

}
