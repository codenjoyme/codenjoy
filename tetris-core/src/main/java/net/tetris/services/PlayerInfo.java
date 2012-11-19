package net.tetris.services;

import net.tetris.services.Player;

/**
 * User: oleksandr.baglai
 * Date: 11/16/12
 * Time: 8:04 PM
 */
public class PlayerInfo extends Player {

    private boolean saved;
    private boolean active;

    public PlayerInfo(String name, boolean saved) {
        setName(name);
        this.saved = saved;
        this.active = false;
    }

    public PlayerInfo(Player player) {
        this(player.getName(), player.getCallbackUrl());
    }

    public PlayerInfo(String name, String url) {
        setName(name);
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
