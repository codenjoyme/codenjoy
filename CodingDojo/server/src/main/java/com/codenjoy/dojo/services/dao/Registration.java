package com.codenjoy.dojo.services.dao;

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


import com.codenjoy.dojo.services.ConfigProperties;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.jdbc.ConnectionThreadPoolFactory;
import com.codenjoy.dojo.services.jdbc.CrudConnectionThreadPool;
import com.codenjoy.dojo.services.security.GameAuthorities;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static com.codenjoy.dojo.services.dao.Registration.User.APPROVED;
import static com.codenjoy.dojo.services.dao.Registration.User.NOT_APPROVED;
import static com.codenjoy.dojo.services.security.GameAuthoritiesConstants.ROLE_ADMIN;
import static com.codenjoy.dojo.services.security.GameAuthoritiesConstants.ROLE_USER;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

public class Registration {

    public static final int ADMIN_USER_ID = 0;

    private CrudConnectionThreadPool pool;
    private PasswordEncoder passwordEncoder;
    private ConfigProperties properties;

    public Registration(ConnectionThreadPoolFactory factory, String adminEmail, String adminPassword, PasswordEncoder passwordEncoder, ConfigProperties properties, boolean initAdminUser) {
        this.passwordEncoder = passwordEncoder;
        this.properties = properties;
        adminPassword = passwordEncoder.encode(Hash.md5(adminPassword));
        List<String> initialScripts = new ArrayList<>();
        initialScripts.add("CREATE TABLE IF NOT EXISTS users (" +
                "email varchar(255), " +
                "id varchar(255), " +
                "readable_name varchar(255), " +
                "email_approved int, " +
                "password varchar(255)," +
                "code varchar(255)," +
                "data varchar(255)," +
                "roles varchar(255));");
        if (initAdminUser) {
            initialScripts.add(String.format("INSERT INTO users (id, email, readable_name, email_approved, password, code, data, roles)" +
                    " select '%s', '%s', '%s', %s,  '%s', '%s', '{}', '%s, %s'" +
                    " where not exists (select 1 from users where id = '%s')",
                    ADMIN_USER_ID, adminEmail, "admin", APPROVED, adminPassword, "000000000000", ROLE_ADMIN, ROLE_USER,
                    ADMIN_USER_ID));
        }
        pool = factory.create(initialScripts.toArray(new String[initialScripts.size()]));
    }

    void removeDatabase() {
        pool.removeDatabase();
    }

    public boolean approved(String id) {
        return pool.select("SELECT * FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() && rs.getInt("email_approved") == APPROVED
        );
    }

    public boolean registered(String id) {
        return pool.select("SELECT count(*) AS total FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> exists(rs, " id " + id)
        );
    }

    private Boolean exists(ResultSet rs, String details) throws SQLException {
        if (!rs.next()) {
            return false;
        }
        int count = rs.getInt("total");
        if (count > 1) {
            throw new IllegalStateException("Found more than one user with " + details);
        }
        return count > 0;
    }

    public User getOrRegister(String id, String email, String readableName) {
        Registration.User result = getUserById(id)
                .orElseGet(() -> getUserByEmail(email)
                    .orElseGet(() -> registerApproved(id, email, readableName)));
        return result;
    }
    
    public User registerApproved(String id, String email, String readableName) {
        if (StringUtils.isEmpty(id)) {
            id = Hash.getRandomId();
        }
        String password = passwordEncoder.encode(randomAlphanumeric(properties.getAutoGenPasswordLen()));

        User user = register(id, email, readableName,
                password, "{}", GameAuthorities.USER.roles());

        if (!properties.isEmailVerificationNeeded()) {
            approve(user.getCode());
            user.setApproved(APPROVED);
        }

        return user;
    }

    public User register(String id, String email, String readableName, String password, String data, Collection<String> roles) {
        roles = roles.isEmpty() ? GameAuthorities.USER.roles() : roles;
        String code = Hash.getCode(id, password);
        password = passwordEncoder.encode(password);
        
        pool.update("INSERT INTO users (id, email, readable_name, email_approved, password, code, data, roles) VALUES (?,?,?,?,?,?,?,?);",
                new Object[]{id, email, readableName, NOT_APPROVED, password, code, data, GameAuthorities.joinRoles(roles)});
        
        return getUserByCode(code);
    }

    public String login(String id, String password) {
        return pool.select("SELECT code, password FROM users WHERE id = ? AND email_approved = ?;",
                new Object[]{id, APPROVED},
                rs -> {
                    if (!rs.next()) {
                        return null;
                    }
                    String encodedPassword = rs.getString("password");
                    if (passwordEncoder.matches(password, encodedPassword)) {
                        return rs.getString("code");
                    }

                    return null;
                }
        );
    }
    
    public String checkUser(String id) {
        if (getCodeById(id) != null) {
            return id;
        } else {
            return null;
        }
    }

    public String checkUser(String id, String code) {
        String stored = getIdByCode(code);

        if (stored == null) {
            return null;
        }

        if (stored.equals(id)) {
            return id;
        }

        return null;
    }

    public String checkUserByPassword(String id, String password) {
        return checkUser(id, Hash.getCode(id, password));
    }

    public String getIdByCode(String code) {
        return pool.select("SELECT id FROM users WHERE code = ?;",
                new Object[]{code},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    public boolean emailIsUsed(String email) {
        return pool.select("SELECT count(*) AS total FROM users WHERE email = ?;",
                new Object[]{email},
                rs -> exists(rs, " email " + email)
        );
    }

    public boolean nameIsUsed(String name) {
        return pool.select("SELECT count(*) AS total FROM users WHERE readable_name = ?;",
                new Object[]{name},
                rs -> exists(rs, " name " + name)
        );
    }

    public String getEmailById(String id) {
        return pool.select("SELECT email FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("email") : null
        );
    }

    public String getIdByName(String name) {
        return pool.select("SELECT id FROM users WHERE readable_name = ?;",
                new Object[]{name},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    public String getIdByEmail(String email) {
        return pool.select("SELECT id FROM users WHERE email = ?;",
                new Object[]{email},
                rs -> rs.next() ? rs.getString("id") : null
        );
    }

    public String getNameById(String id) {
        return pool.select("SELECT readable_name FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("readable_name") : null
        );
    }

    public String getCodeById(String id) {
        return pool.select("SELECT code FROM users WHERE id = ?;",
                new Object[]{id},
                rs -> rs.next() ? rs.getString("code") : null
        );
    }

    public void approve(String code) {
        pool.update("UPDATE users SET email_approved = ? WHERE code = ?;",
                new Object[]{APPROVED, code});
    }

    public void updateReadableName(String id, String name) {
        pool.update("UPDATE users SET readable_name = ? WHERE id = ?;",
                new Object[]{name, id});
    }

    public void updateId(String name, String id) {
        pool.update("UPDATE users SET id = ? WHERE readable_name = ?;",
                new Object[]{id, name});
    }

    public void updateNameAndEmail(String id, String name, String email) {
        pool.update("UPDATE users SET readable_name = ?, email = ? WHERE id = ?;",
                new Object[]{name, email, id});
    }

    @Data
    @ToString(of = { "id", "email", "readableName", "approved", "code", "data" })
    @EqualsAndHashCode(callSuper = true)
    @Accessors(chain = true)
    public static class User extends org.springframework.security.core.userdetails.User implements OAuth2User {

        public static final int APPROVED = 1;
        public static final int NOT_APPROVED = 0;

        private String email;
        private String id;
        private String readableName;
        private int approved;
        private String code;
        private String data;

        public User() {
            super("anonymous", "", Collections.emptyList());
        }

        public User(String id, String email, String readableName, int approved, String password, String code, String data, Collection<String> roles) {
            super(email, password, GameAuthorities.toGranted(roles));
            this.id = id;
            this.email = email;
            this.readableName = readableName;
            this.approved = approved;
            this.code = code;
            this.data = data;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public Map<String, Object> getAttributes() {
            return Collections.emptyMap();
        }

        @Override
        public String getName() {
            return id;
        }
    }

    public User getUserByCode(String code) {
        return pool.select("SELECT * FROM users where code = ?", new Object[] {code}, rs -> {
            if (!rs.next()) {
                throw new UsernameNotFoundException(String.format("User with code '%s' does not exist", code));
            }
            return extractUser(rs);
        });
    }

    public Optional<User> getUserByEmail(String email) {
        return pool.select("SELECT * FROM users where email = ?", new Object[] {email}, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(extractUser(rs));
        });
    }

    public Optional<User> getUserById(String id) {
        return pool.select("SELECT * FROM users where id = ?", new Object[] {id}, rs -> {
            if (!rs.next()) {
                return Optional.empty();
            }
            return Optional.of(extractUser(rs));
        });
    }

    public List<User> getUsers() {
        return pool.select("SELECT * FROM users;", rs -> {
            List<User> result = new LinkedList<>();
            while (rs.next()) {
                result.add(extractUser(rs));
            }
            return result;
        });
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getString("id"),
                rs.getString("email"),
                rs.getString("readable_name"),
                rs.getInt("email_approved"),
                rs.getString("password"),
                rs.getString("code"),
                rs.getString("data"),
                GameAuthorities.splitRoles(rs.getString("roles")));
    }

    public void replace(User user) {
        String code = user.getCode();

        String password = user.getPassword();
        // если пришло md5, а не bcrypt(md5)
        if (!password.contains("$")) {
            password = passwordEncoder.encode(password);
        }

        if (StringUtils.isEmpty(code)) {
            // code = code(bcrypt(md5))
            code = Hash.getCode(user.getEmail(), password);
            user.setCode(code);
        }

        Object[] parameters = {
                user.getReadableName(),
                user.getEmail(),
                APPROVED,
                password,
                code,
                user.getData(),
                GameAuthorities.toRoles(user.getAuthorities()),
                user.getId()
        };

        if (getCodeById(user.getId()) == null) {
            pool.update("INSERT INTO users (readable_name, email, email_approved, password, code, data, roles, id) VALUES (?,?,?,?,?,?,?,?);",
                    parameters);
        } else {
            pool.update("UPDATE users SET readable_name = ?, email = ?, email_approved = ?, password = ?, code = ?, data = ?, roles = ? WHERE id = ?;",
                    parameters);
        }
    }

    public void remove(String id) {
        pool.update("DELETE FROM users WHERE id = ?;",
                new Object[]{id});
    }

    public void removeAll() {
        pool.update("DELETE FROM users WHERE roles NOT LIKE '%" + ROLE_ADMIN + "%';");
    }

}
