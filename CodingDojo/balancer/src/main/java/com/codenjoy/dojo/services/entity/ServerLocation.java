package com.codenjoy.dojo.services.entity;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2019 Codenjoy
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

import lombok.Getter;

@Getter
public class ServerLocation {

    private String email;
    private String phone; // TODO я не уверен, что это сейчас надо тут - глянуть на фронт
    private String id;
    private String code;
    private String server;

    public ServerLocation() {
        // do nothing
    }

    public ServerLocation(Player player) {
        this(player.getEmail(),
                player.getPhone(),
                player.getId(),
                player.getCode(),
                player.getServer());
    }

    public ServerLocation(String email, String phone, String id, String code, String server) {
        this.email = email;
        this.phone = phone;
        this.id = id;
        this.code = code;
        this.server = server;
    }

    @Override
    public String toString() {
        return "ServerLocation{" +
                "email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", server='" + server + '\'' +
                '}';
    }
}
