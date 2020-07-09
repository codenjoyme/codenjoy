package com.codenjoy.dojo.web.rest.pojo;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.services.dao.Registration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

import static com.codenjoy.dojo.services.security.GameAuthorities.splitRoles;
import static com.codenjoy.dojo.services.security.GameAuthorities.toRoles;

@NoArgsConstructor
@Setter
@Getter
public class PUser {

    private String email;
    private String id;
    private String readableName;
    private int approved;
    private String code;
    private String data;
    private String password;
    private Collection<String> roles;

    public PUser(Registration.User user) {
        email = user.getEmail();
        id = user.getId();
        readableName = user.getReadableName();
        approved = user.getApproved();
        code = user.getCode();
        data = user.getData();
        password = user.getPassword();
        roles = splitRoles(toRoles(user.getAuthorities()));
    }

    public Registration.User build() {
        return new Registration.User(
                id,
                email,
                readableName,
                approved,
                password,
                code,
                data,
                roles);
    }

}
