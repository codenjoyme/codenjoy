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

import org.apache.commons.lang.StringUtils;

public class Player {

    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String city;
    private String skills;
    private String comment;
    private String code;
    private String server;

    public Player() {
        // do nothing
    }

    public Player(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Player(String email, String code, String server) {
        this.email = email;
        this.code = code;
        this.server = server;
    }

    public Player(String email, String firstName, String lastName,
                  String password, String city, String skills,
                  String comment, String code, String server)
    {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.city = city;
        this.skills = skills;
        this.comment = comment;
        this.code = code;
        this.server = server;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getCode() {
        return code;
    }

    public String getServer() {
        return server;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCity() {
        return city;
    }

    public String getSkills() {
        return skills;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public String toString() {
        return "Player{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", password='" + password + '\'' +
                ", city='" + city + '\'' +
                ", skills='" + skills + '\'' +
                ", comment='" + comment + '\'' +
                ", code='" + code + '\'' +
                ", server='" + server + '\'' +
                '}';
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public void resetNullFileds(Player player) {
        email = StringUtils.isEmpty(email) ? player.email : email;
        firstName = StringUtils.isEmpty(firstName) ? player.firstName : firstName;
        lastName = StringUtils.isEmpty(lastName) ? player.lastName : lastName;
        password = StringUtils.isEmpty(password) ? player.password : password;
        city = StringUtils.isEmpty(city) ? player.city : city;
        skills = StringUtils.isEmpty(skills) ? player.skills : skills;
        comment = StringUtils.isEmpty(comment) ? player.comment : comment;
        code = StringUtils.isEmpty(code) ? player.code : code;
        server = StringUtils.isEmpty(server) ? player.server : server;
    }
}
