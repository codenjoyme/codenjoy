package com.codenjoy.dojo.services;

/**
 * User: oleksandr.baglai
 * Date: 11/16/12
 * Time: 8:04 PM
 */
public class PlayerInfo extends Player {

    private boolean saved;
    private boolean active;

    public PlayerInfo() {
        //
    }

    public PlayerInfo(String name, String code, boolean saved) {
        setName(name);
        setCode(code);
        this.saved = saved;
        this.active = false;
    }

    public PlayerInfo(Player player) {
        this(player.getName(), player.getCode(), player.getCallbackUrl());
    }

    public PlayerInfo(String name, String code, String url) {
        setName(name);
        setCode(code);
        setCallbackUrl(url);
        this.saved = false;
        this.active = true;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
