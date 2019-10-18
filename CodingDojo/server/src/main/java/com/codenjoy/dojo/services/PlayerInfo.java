package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 Codenjoy
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */


public class PlayerInfo extends Player {

    private boolean saved;
    private boolean active;
    private boolean hidden;
    private boolean hasAI;

    public PlayerInfo() {
        initScores();
    }

    public PlayerInfo(String name, String readableName, String code, String url, String gameName, boolean saved) {
        setName(name);
        setReadableName(readableName);
        setCode(code);
        setCallbackUrl(url);
        setGameName(gameName);
        this.saved = saved;
        this.active = false;
        this.hidden = false;
    }

    public PlayerInfo(Player player) {
        this(player.getName(), player.getCode(), player.getCallbackUrl(), player.getGameName());
    }

    public PlayerInfo(String name, String code, String url, String gameName) {
        setName(name);
        setCode(code);
        setCallbackUrl(url);
        setGameName(gameName);
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isAiPlayer() {
        return hasAI;
    }

    public void setAIPlayer(boolean hasAI) {
        this.hasAI = hasAI;
    }
}
