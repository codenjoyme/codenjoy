package com.codenjoy.dojo.services.entity.server;

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

public class User {

    private String email;
    private String readableName;
    private int approved;
    private String password;
    private String code;
    private String data;

    public User() {
        // do nothing
    }

    public User(String email, String readableName, int approved, String password, String code, String data) {
        this.email = email;
        this.readableName = readableName;
        this.approved = approved;
        this.password = password;
        this.code = code;
        this.data = data;
    }

    public String getEmail() {
        return email;
    }

    public String getReadableName() {
        return readableName;
    }

    public int getApproved() {
        return approved;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public String getData() {
        return data;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", readable_name=" + readableName +
                ", email_approved=" + approved +
                ", password='" + password + '\'' +
                ", code='" + code + '\'' +
                ", data='" + data + '\'' +
                '}';
    }
}
