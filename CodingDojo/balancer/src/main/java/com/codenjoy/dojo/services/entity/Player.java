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
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Player {

    public static final int APPROVED = 1;
    public static final int NOT_APPROVED = 0;

    private String id;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
    private String password;
    private String city;
    private String skills;
    private String comment;
    private String code;
    private String server;
    private String callback;
    private int approved;
    private String verificationCode;
    private String verificationType;

    public Player() {
        // do nothing
    }

    public Player(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public Player(String email, String code, String callback) {
        this.email = email;
        this.code = code;
        this.callback = callback;
    }

    public Player(String id, String email, String phone, String firstName, String lastName,
                  String password, String city, String skills,
                  String comment, String code, String server,
                  int approved, String callback, String verificationCode, String verificationType) {
        this.id = id;
        this.email = email;
        this.phone = phone;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.city = city;
        this.skills = skills;
        this.comment = comment;
        this.code = code;
        this.server = server;
        this.approved = approved;
        this.callback = callback;
        this.verificationCode = verificationCode;
        this.verificationType = verificationType;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", skills='" + skills + '\'' +
                ", comment='" + comment + '\'' +
                ", code='" + code + '\'' +
                ", server='" + server + '\'' +
                ", approved=" + approved +
                ", callback='" + callback + '\'' +
                ", verificationCode='" + verificationCode + '\'' +
                ", verificationType='" + verificationType + '\'' +
                '}';
    }

    public void resetNullFields(Player player) {
        id = StringUtils.isEmpty(id) ? player.id : id;
        email = StringUtils.isEmpty(email) ? player.email : email;
        phone = StringUtils.isEmpty(phone) ? player.phone : phone;
        firstName = StringUtils.isEmpty(firstName) ? player.firstName : firstName;
        lastName = StringUtils.isEmpty(lastName) ? player.lastName : lastName;
        password = StringUtils.isEmpty(password) ? player.password : password;
        city = StringUtils.isEmpty(city) ? player.city : city;
        skills = StringUtils.isEmpty(skills) ? player.skills : skills;
        comment = StringUtils.isEmpty(comment) ? player.comment : comment;
        code = StringUtils.isEmpty(code) ? player.code : code;
        server = StringUtils.isEmpty(server) ? player.server : server;
        approved = player.approved;

        verificationCode = StringUtils.isEmpty(verificationCode) ? player.verificationCode : verificationCode;
        verificationType = StringUtils.isEmpty(verificationType) ? player.verificationType : verificationType;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
