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
import com.codenjoy.dojo.services.ContextPathGetter;
import com.codenjoy.dojo.services.hash.Hash;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Random;

import static com.codenjoy.dojo.services.TestUtils.assertUsersEqual;
import static com.codenjoy.dojo.services.security.GameAuthorities.ADMIN;
import static com.codenjoy.dojo.services.security.GameAuthorities.USER;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class RegistrationTest {

    public static final String HASH = "someHash";
    private static Registration service;
    private static PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Before
    public void setup() {
        String dbFile = "target/users.db" + new Random().nextInt();
        service = new Registration(
                new SqliteConnectionThreadPoolFactory(dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }), "admin", "admin", new BCryptPasswordEncoder(), mock(ConfigProperties.class), false);
    }

    @After
    public void tearDown() {
        service.removeDatabase();
    }

    @Test
    public void shouldNotExistsUser() {
        assertFalse(service.registered("not_exists"));
    }

    @Test
    public void shouldRegister() {
        // given
        String code = service.register("user", "email", "name", "pass", "data", USER.roles()).getCode();

        // then
        assertTrue(service.registered("user"));
        assertFalse(service.approved("user"));

        // when
        service.approve(code);

        // then
        assertTrue(service.registered("user"));
        assertTrue(service.approved("user"));
    }

    @Test
    public void shouldRegisterWithData() {
        // when
        String code = service.register("user", "email", "name", "pass", "someData", USER.roles()).getCode();

        // then
        assertEquals("3514017434644657823", code);

        Registration.User user = service.getUserByCode(code);

        Registration.User expected = new Registration.User("user", "email", "name", 0, "pass", "3514017434644657823", "someData", USER.roles());

        assertUsersEqual(expected, user, "pass", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUnRegisteredUserIsNotApproved() {
        // when then
        assertFalse(service.approved("user"));
    }

    @Test
    public void shouldSuccessLogin() {
        // given
        service.approve(service.register("user", "email", "name", "pass", "data").getCode());

        // when
        String code = service.login("user", "pass");

        // then
        assertEquals("3514017434644657823", code);
    }

    @Test
    public void shouldUnSuccessLogin_whenNoApproveEmail() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.login("user", "pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldUnSuccessLogin() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.login("user", "bad_pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetCodeByName() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.getCodeById("user");

        // then
        assertEquals("3514017434644657823", code);
    }

    @Test
    public void shouldGetCodeByName_ifNotFound() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String code = service.getCodeById("other_user");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetEmailByCode() {
        // given
        String code = service.register("user", "email", "name", "pass", "data").getCode();

        // when
        String email = service.getIdByCode(code);

        // then
        assertEquals("user", email);
    }

    @Test
    public void shouldGetReadableNameByEmail() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String name = service.getNameById("user");

        // then
        assertEquals("name", name);
    }

    @Test
    public void shouldGetEmailByCode_ifNotFound() {
        // given
        service.register("user", "email", "name", "pass", "data");

        // when
        String email = service.getIdByCode("bad_code");

        // then
        assertNull(email);
    }

    @Test
    public void shouldUpdateReadableName() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = service.getUserByCode(code1);
        Registration.User actualUser2 = service.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        service.updateReadableName("user1", "updatedName1");
        actualUser1 = service.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setReadableName("updatedName1"), actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUpdateId() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = service.getUserByCode(code1);
        Registration.User actualUser2 = service.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        service.updateId("name1", "updatedUser1");
        actualUser1 = service.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setId("updatedUser1"), actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUpdateNameAndEmail() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = service.getUserByCode(code1);
        Registration.User actualUser2 = service.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        service.updateNameAndEmail("user1", "updatedName1", "updatedEmail1");
        actualUser1 = service.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setReadableName("updatedName1").setEmail("updatedEmail1"),
                actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceExistingUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1").getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2").getCode();


        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        Registration.User newExpectedUser1 = new Registration.User("user1", "email1", "name1", 1, "newPassword1", "newCode1", "newData1", USER.roles());
        service.replace(newExpectedUser1);

        // then
        assertUsersEqual(newExpectedUser1, service.getUserByCode(newExpectedUser1.getCode()), "newPassword1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(expectedUser2.getCode()), "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceExistingUser_withoutCode() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1").getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2").getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        String noCode = null;
        Registration.User newExpectedUser1 = new Registration.User("user1", "email1", "name1", 1, "newPassword1", noCode, "newData1", USER.roles());
        service.replace(newExpectedUser1);

        // then
        assertUsersEqual(newExpectedUser1, service.getUserByCode(newExpectedUser1.getCode()), "newPassword1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(expectedUser2.getCode()), "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceNonExistingUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1").getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2").getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        Registration.User expectedUser3 = new Registration.User("user3", "email3", "name3", 1, "newPassword3", "newCode3", "newData3", USER.roles());
        service.replace(expectedUser3);

        // then
        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser3, service.getUserByCode("newCode3"), "newPassword3", PASSWORD_ENCODER);
    }

    @Test
    public void shouldRemoveUser() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1").getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2").getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);;

        // when
        service.remove("user1");

        // then
        assertEquals(Collections.singletonList(expectedUser2), service.getUsers());
    }

    @Test
    public void shouldRemoveAllUsers() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1").getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2").getCode();

        Registration.User expectedUser1 = new Registration.User("user1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("user2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(expectedUser1, service.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, service.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        service.removeAll();

        // then
        assertTrue(service.getUsers().isEmpty());
    }

    @Test
    public void shouldRemoveAllUsers_exceptAdmins() {
        // given
        String code1 = service.register("user1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = service.register("user2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();
        String code3 = service.register("admin3", "email3", "name3", "pass3", "someData3", ADMIN.roles()).getCode();
        String code4 = service.register("admin4", "email4", "name4", "pass4", "someData4", ADMIN.roles()).getCode();

        // when
        service.removeAll();

        // then
        assertEquals("[admin3, admin4]", service.getUsers()
                            .stream()
                            .map(Registration.User::getName)
                            .collect(toList())
                        .toString());
    }

    @Test
    public void shouldCheckUser_whenOnlyEmails() {
        String email = "user@email.com";

        String code = service.register(email, "email", "name", "pass", "someData").getCode();

        assertEquals(email, service.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenIdStoredOnDb_askWithEmail() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(id, "email", "name", "pass", "someData").getCode();

        assertEquals(null, service.checkUser(email, code));
        assertEquals(id, service.checkUser(id, code));
    }

    @Test
    public void shouldCheckUser_whenEmailStoredOnDb_askWithId() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(email, "email", "name", "pass", "someData").getCode();

        assertEquals(null, service.checkUser(id, code));
        assertEquals(email, service.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenOnlyIds() {
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);

        String code = service.register(id, "email", "name", "pass", "someData").getCode();

        assertEquals(id, service.checkUser(id, code));
        assertEquals(null, service.checkUser(email, code));
    }
}
