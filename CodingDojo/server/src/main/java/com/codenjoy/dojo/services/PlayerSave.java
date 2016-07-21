package com.codenjoy.dojo.services;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2016 Codenjoy
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


import org.apache.commons.lang.StringUtils;

public class PlayerSave {

    public static final PlayerSave NULL = new PlayerSave(StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY, 0, StringUtils.EMPTY, StringUtils.EMPTY);

    private String protocol;
    private int score;
    private String callbackUrl;
    private String gameName;
    private String name;
    private String save;

    public PlayerSave(String name, String callbackUrl, String gameName, int score, String protocol, String save) {
        this.name = name;
        this.gameName = gameName;
        this.callbackUrl = callbackUrl;
        this.score = score;
        this.protocol = protocol;
        this.save = save;
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

    public String getSave() {
        return save;
    }
}
