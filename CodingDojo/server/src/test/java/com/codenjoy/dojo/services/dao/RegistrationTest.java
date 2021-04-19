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
import org.apache.commons.lang3.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static com.codenjoy.dojo.services.TestUtils.assertUsersEqual;
import static com.codenjoy.dojo.services.security.GameAuthorities.ADMIN;
import static com.codenjoy.dojo.services.security.GameAuthorities.USER;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegistrationTest {

    public static final String HASH = "someHash";
    public static final String CODE_FOR_ID_AND_PASS = "4486751343675417965";
    private Registration registration;
    private static PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();
    private ConfigProperties properties;

    @Before
    public void setup() {
        String dbFile = "target/users.db" + new Random().nextInt();
        properties = mock(ConfigProperties.class);
        registration = new Registration(
                new SqliteConnectionThreadPoolFactory(false, dbFile,
                        new ContextPathGetter() {
                            @Override
                            public String getContext() {
                                return "context";
                            }
                        }), "admin", "admin", new BCryptPasswordEncoder(), properties, false);
    }

    @After
    public void tearDown() {
        registration.removeDatabase();
    }

    @Test
    public void shouldNotExistsUser() {
        assertFalse(registration.registered("not_exists"));
    }

    @Test
    public void shouldApprove_registered() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // then
        assertTrue(registration.registered("id"));
        assertFalse(registration.approved("id"));

        // when
        registration.approve(code);

        // then
        assertTrue(registration.registered("id"));
        assertTrue(registration.approved("id"));
    }

    @Test
    public void shouldRegisterWithData() {
        // when
        String code = registration.register("id", "email", "name", "pass", "someData", USER.roles()).getCode();

        // then
        assertEquals(CODE_FOR_ID_AND_PASS, code);

        Registration.User user = registration.getUserByCode(code);

        Registration.User expected = new Registration.User("id", "email", "name", 0, "pass", CODE_FOR_ID_AND_PASS, "someData", USER.roles());

        assertUsersEqual(expected, user, "pass", PASSWORD_ENCODER);
    }

    @Test
    public void shouldGetUserByCode_notExistent() {
        // when
        try {
            Registration.User user = registration.getUserByCode("bad_code");
            fail("Expected exception");
        } catch (Exception e) {
            // then
            assertEquals("org.springframework.security.core.userdetails.UsernameNotFoundException: " +
                    "User with code 'bad_code' does not exist", e.toString());
        }
    }

    @Test
    public void shouldNotApproved_notExistent() {
        // when then
        assertFalse(registration.approved("id"));
    }

    @Test
    public void shouldSuccessLogin() {
        // given
        Registration.User user = registration.register("id", "email", "name", "pass", "data", Arrays.asList());
        registration.approve(user.getCode());

        // when
        String code = registration.login("id", "pass");

        // then
        assertEquals(CODE_FOR_ID_AND_PASS, code);
    }

    @Test
    public void shouldUnSuccessLogin_whenNoApproveEmail() {
        // given
        registration.register("id", "email", "name", "pass", "data", Arrays.asList());

        // when
        String code = registration.login("id", "pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldUnSuccessLogin_whenBadPassword() {
        // given
        Registration.User user = registration.register("id", "email", "name", "pass", "data", Arrays.asList());
        registration.approve(user.getCode());

        // when
        String code = registration.login("id", "bad_pass");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetCodeById() {
        // given
        registration.register("id", "email", "name", "pass", "data", Arrays.asList());

        // when
        String code = registration.getCodeById("id");

        // then
        assertEquals(CODE_FOR_ID_AND_PASS, code);
    }

    @Test
    public void shouldGetCodeById_ifNotFound() {
        // given
        registration.register("id", "email", "name", "pass", "data", Arrays.asList());

        // when
        String code = registration.getCodeById("bad_id");

        // then
        assertEquals(null, code);
    }

    @Test
    public void shouldGetIdByCode() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", Arrays.asList()).getCode();

        // when
        String id = registration.getIdByCode(code);

        // then
        assertEquals("id", id);
    }

    @Test
    public void shouldGetNameById() {
        // given
        registration.register("id", "email", "name", "pass", "data", Arrays.asList());

        // when
        String name = registration.getNameById("id");

        // then
        assertEquals("name", name);
    }

    @Test
    public void shouldGetIdByCode_ifNotFound() {
        // given
        registration.register("id", "email", "name", "pass", "data", Arrays.asList());

        // when
        String email = registration.getIdByCode("bad_code");

        // then
        assertNull(email);
    }

    @Test
    public void shouldUpdateReadableName() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = registration.getUserByCode(code1);
        Registration.User actualUser2 = registration.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        registration.updateReadableName("id1", "updatedName1");
        actualUser1 = registration.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setReadableName("updatedName1"), actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUpdateEmail() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = registration.getUserByCode(code1);
        Registration.User actualUser2 = registration.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        registration.updateEmail("id1", "updatedEmail1");
        actualUser1 = registration.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setEmail("updatedEmail1"), actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUpdateId() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = registration.getUserByCode(code1);
        Registration.User actualUser2 = registration.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        registration.updateId("name1", "updatedUser1");
        actualUser1 = registration.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setId("updatedUser1"), actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldUpdateNameAndEmail() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();

        Registration.User expectedUser1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User expectedUser2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        Registration.User actualUser1 = registration.getUserByCode(code1);
        Registration.User actualUser2 = registration.getUserByCode(code2);

        assertUsersEqual(expectedUser1, actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);

        // when
        registration.updateNameAndEmail("id1", "updatedName1", "updatedEmail1");
        actualUser1 = registration.getUserByCode(code1);

        // then
        assertUsersEqual(expectedUser1.setReadableName("updatedName1").setEmail("updatedEmail1"),
                actualUser1, "pass1", PASSWORD_ENCODER);
        assertUsersEqual(expectedUser2, actualUser2, "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceExistingUser() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", Arrays.asList()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", Arrays.asList()).getCode();


        Registration.User user1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User user2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        Registration.User updated = new Registration.User("id1", "email1", "name1", 1, "newPassword1", "newCode1", "newData1", USER.roles());
        registration.replace(updated);

        // then
        assertUsersEqual(updated, registration.getUserByCode(updated.getCode()), "newPassword1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(user2.getCode()), "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceExistingUser_withoutCode() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", Arrays.asList()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", Arrays.asList()).getCode();

        Registration.User user1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User user2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        String noCode = null;
        Registration.User updated = new Registration.User("id1", "email1", "name1", 1, "newPassword1", noCode, "newData1", USER.roles());
        registration.replace(updated);

        // then
        assertUsersEqual(updated, registration.getUserByCode(updated.getCode()), "newPassword1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(user2.getCode()), "pass2", PASSWORD_ENCODER);
    }

    @Test
    public void shouldReplaceNonExistingUser() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", Arrays.asList()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", Arrays.asList()).getCode();

        Registration.User user1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User user2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        Registration.User updated = new Registration.User("user3", "email3", "name3", 1, "newPassword3", "newCode3", "newData3", USER.roles());
        registration.replace(updated);

        // then
        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);
        assertUsersEqual(updated, registration.getUserByCode("newCode3"), "newPassword3", PASSWORD_ENCODER);
    }

    @Test
    public void shouldRemoveUser() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", Arrays.asList()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", Arrays.asList()).getCode();

        Registration.User user1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User user2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);;

        // when
        registration.remove("id1");

        // then
        assertEquals(Collections.singletonList(user2), registration.getUsers());
    }

    @Test
    public void shouldRemoveAllUsers() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", Arrays.asList()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", Arrays.asList()).getCode();

        Registration.User user1 = new Registration.User("id1", "email1", "name1", 0, "pass1", code1, "someData1", USER.roles());
        Registration.User user2 = new Registration.User("id2", "email2", "name2", 0, "pass2", code2, "someData2", USER.roles());

        assertUsersEqual(user1, registration.getUserByCode(code1), "pass1", PASSWORD_ENCODER);
        assertUsersEqual(user2, registration.getUserByCode(code2), "pass2", PASSWORD_ENCODER);

        // when
        registration.removeAll();

        // then
        assertTrue(registration.getUsers().isEmpty());
    }

    @Test
    public void shouldRemoveAllUsers_exceptAdmins() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();
        String code3 = registration.register("admin3", "email3", "name3", "pass3", "someData3", ADMIN.roles()).getCode();
        String code4 = registration.register("admin4", "email4", "name4", "pass4", "someData4", ADMIN.roles()).getCode();

        // when
        registration.removeAll();

        // then
        assertUsers("[admin3, admin4]");
    }

    public void assertUsers(String expected) {
        assertEquals(expected,
                registration.getUsers().stream()
                        .map(Registration.User::getName)
                        .collect(toList())
                        .toString());
    }

    @Test
    public void shouldGetUsers() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();
        String code3 = registration.register("id3", "email3", "name3", "pass3", "someData3", USER.roles()).getCode();
        String code4 = registration.register("admin4", "email4", "name4", "pass4", "someData4", ADMIN.roles()).getCode();
        String code5 = registration.register("admin5", "email5", "name5", "pass5", "someData5", ADMIN.roles()).getCode();

        // when
        List<Registration.User> users = registration.getUsers();

        // then
        assertEquals("[Registration.User(email=email1, id=id1, readableName=name1, approved=0, code=4877982059660098880, data=someData1), \n" +
                        "Registration.User(email=email2, id=id2, readableName=name2, approved=0, code=9205595962804372348, data=someData2), \n" +
                        "Registration.User(email=email3, id=id3, readableName=name3, approved=0, code=8196566518765037475, data=someData3), \n" +
                        "Registration.User(email=email4, id=admin4, readableName=name4, approved=0, code=1050143549617564675, data=someData4), \n" +
                        "Registration.User(email=email5, id=admin5, readableName=name5, approved=0, code=8742437141300845912, data=someData5)]",
                users.toString().replace(", Registration", ", \nRegistration"));
    }

    @Test
    public void shouldGetUsers_byIds() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();
        String code3 = registration.register("id3", "email3", "name3", "pass3", "someData3", USER.roles()).getCode();
        String code4 = registration.register("admin4", "email4", "name4", "pass4", "someData4", ADMIN.roles()).getCode();
        String code5 = registration.register("admin5", "email5", "name5", "pass5", "someData5", ADMIN.roles()).getCode();

        // when
        List<Registration.User> users = registration.getUsers(Arrays.asList("id1", "id3", "admin5"));

        // then
        assertEquals("[Registration.User(email=email1, id=id1, readableName=name1, approved=0, code=4877982059660098880, data=someData1), \n" +
                        "Registration.User(email=email3, id=id3, readableName=name3, approved=0, code=8196566518765037475, data=someData3), \n" +
                        "Registration.User(email=email5, id=admin5, readableName=name5, approved=0, code=8742437141300845912, data=someData5)]",
                users.toString().replace(", Registration", ", \nRegistration"));
    }

    @Test
    public void shouldNotRemoveAdmins_whenRemove() {
        // given
        String code1 = registration.register("id1", "email1", "name1", "pass1", "someData1", USER.roles()).getCode();
        String code2 = registration.register("id2", "email2", "name2", "pass2", "someData2", USER.roles()).getCode();
        String code3 = registration.register("id3", "email3", "name3", "pass3", "someData3", USER.roles()).getCode();
        String code4 = registration.register("admin4", "email4", "name4", "pass4", "someData4", ADMIN.roles()).getCode();
        String code5 = registration.register("admin5", "email5", "name5", "pass5", "someData5", ADMIN.roles()).getCode();

        // when
        registration.remove("id2");
        registration.remove("admin4");

        // then
        assertUsers("[id1, id3, admin4, admin5]");
    }

    @Test
    public void shouldCheckUser_whenOnlyEmails() {
        // given
        String email = "user@email.com";
        String code = registration.register(email, "email", "name", "pass", "someData", Arrays.asList()).getCode();

        // when then
        assertEquals(email, registration.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenIdStoredOnDb_askWithEmail() {
        // given
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);
        
        String code = registration.register(id, "email", "name", "pass", "someData", Arrays.asList()).getCode();

        // when then
        assertEquals(null, registration.checkUser(email, code));
        assertEquals(id, registration.checkUser(id, code));
    }

    @Test
    public void shouldCheckUser_whenEmailStoredOnDb_askWithId() {
        // given
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);
        
        String code = registration.register(email, "email", "name", "pass", "someData", Arrays.asList()).getCode();

        // when then
        assertEquals(null, registration.checkUser(id, code));
        assertEquals(email, registration.checkUser(email, code));
    }

    @Test
    public void shouldCheckUser_whenOnlyIds() {
        // given
        String email = "user@email.com";
        String id = Hash.getId(email, HASH);
        
        String code = registration.register(id, "email", "name", "pass", "someData", Arrays.asList()).getCode();

        // when then
        assertEquals(id, registration.checkUser(id, code));
        assertEquals(null, registration.checkUser(email, code));
    }
    
    @Test
    public void shouldRegisterApprovedUser_whenIdIsEmpty() {
        // given
        String id = null;
        String email = "email";
        String readableName = "name";
        when(properties.isEmailVerificationNeeded()).thenReturn(true);

        // when
        Registration.User user = registration.registerApproved(id, email, readableName);
        
        // then
        assertEquals(false, StringUtils.isEmpty(user.getId()));
        assertUser("email", "name", 0, "{}", user);
    }

    @Test
    public void shouldRegisterApprovedUser_whenIdIsNotEmpty() {
        // given
        String id = "id";
        String email = "email";
        String readableName = "name";
        when(properties.isEmailVerificationNeeded()).thenReturn(true);

        // when
        Registration.User user = registration.registerApproved(id, email, readableName);

        // then
        assertEquals("id", user.getId());
        assertUser("email", "name", 0, "{}", user);
    }

    @Test
    public void shouldRegisterApprovedUser_whenEmailVerificationIsNotNeeded() {
        // given
        String id = "id";
        String email = "email";
        String readableName = "name";
        when(properties.isEmailVerificationNeeded()).thenReturn(false);

        // when
        Registration.User user = registration.registerApproved(id, email, readableName);

        // then
        assertEquals("id", user.getId());
        assertUser("email", "name", 1, "{}", user);
    }
    
    private void assertUser(String email, String readableName, int approved, String data, Registration.User user) {
        assertEquals(email, user.getEmail());
        assertEquals(user.getId(), user.getName());
        assertEquals(false, StringUtils.isEmpty(user.getCode()));
        assertEquals(readableName, user.getReadableName());
        assertEquals(approved, user.getApproved());
        assertEquals(data, user.getData());

        assertEquals(readableName, registration.getNameById(user.getId()));
        assertEquals(user.getId(), registration.getIdByCode(user.getCode()));
    }

    @Test
    public void shouldGetOrRegister_whenFoundById() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();
        String id = registration.getIdByCode(code);
        
        // when
        Registration.User user = registration.getOrRegister(id, null, null);

        // then
        assertEquals(code, user.getCode());
        assertUser("email", "name", 0, "data", user);
    }

    @Test
    public void shouldGetOrRegister_whenFoundByEmail() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();
        String id = registration.getIdByCode(code);

        // when
        Registration.User user = registration.getOrRegister(null, "email", null);

        // then
        assertEquals(id, user.getId());
        assertEquals(code, user.getCode());
        assertUser("email", "name", 0, "data", user);
    }

    @Test
    public void shouldGetOrRegister_createNew() {
        // given
        when(properties.isEmailVerificationNeeded()).thenReturn(false);

        // when
        Registration.User user = registration.getOrRegister("id", "email", "name");

        // then
        assertEquals(false, StringUtils.isEmpty(user.getId()));
        assertUser("email", "name", 1, "{}", user);
    }
    
    @Test
    public void shouldGetUserById_notExistent() {
        // when 
        Optional<Registration.User> user = registration.getUserById("bad_id");

        // then
        assertEquals(false, user.isPresent());
    }

    @Test
    public void shouldGetUserById_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();
        String id = registration.getIdByCode(code);
        
        // when
        Optional<Registration.User> user = registration.getUserById(id);
        
        // then 
        assertEquals(true, user.isPresent());
        
        assertEquals(id, user.get().getId());
        assertUser("email", "name", 0, "data", user.get());
    }

    @Test
    public void shouldGetIdByEmail_notExistent() {
        // when 
        String id = registration.getIdByEmail("bad_email");

        // then
        assertEquals(null, id);
    }

    @Test
    public void shouldGetIdByEmail_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // when
        String id = registration.getIdByEmail("email");

        // then
        assertEquals(false, StringUtils.isEmpty(id));
        assertEquals(true, registration.registered(id));
    }

    @Test
    public void shouldGetIdByName_notExistent() {
        // when 
        String id = registration.getIdByName("bad_name");

        // then
        assertEquals(null, id);
    }

    @Test
    public void shouldGetIdByName_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // when
        String id = registration.getIdByName("name");

        // then
        assertEquals(false, StringUtils.isEmpty(id));
        assertEquals(true, registration.registered(id));
    }

    @Test
    public void shouldGetEmailById_notExistent() {
        // when 
        String email = registration.getEmailById("bad_id");

        // then
        assertEquals(null, email);
    }

    @Test
    public void shouldGetEmailById_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // when
        String email = registration.getEmailById("id");

        // then
        assertEquals("email", email);
    }

    @Test
    public void shouldEmailIsUsed_notExistent() {
        // when 
        boolean used = registration.emailIsUsed("bad_email");

        // then
        assertEquals(false, used);
    }

    @Test
    public void shouldEmailIsUsed_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // when
        boolean used = registration.emailIsUsed("email");

        // then
        assertEquals(true, used);
    }

    @Test
    public void shouldNameIsUsed_notExistent() {
        // when 
        boolean used = registration.nameIsUsed("bad_name");

        // then
        assertEquals(false, used);
    }

    @Test
    public void shouldNameIsUsed_existent() {
        // given 
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();

        // when
        boolean used = registration.nameIsUsed("name");

        // then
        assertEquals(true, used);
    }
    
    @Test
    public void shouldCheckUserByPassword_badIdAndPassword() {
        // given
        String id = "bad_id";
        String password = "bad_password";
        
        // when
        String actualId = registration.checkUserByPassword(id, password);

        // then
        assertEquals(true, StringUtils.isEmpty(actualId));
    }

    @Test
    public void shouldCheckUserByPassword_badPassword() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();
        String id = registration.getIdByCode(code);
        String password = "bad_password";

        // when
        String actualId = registration.checkUserByPassword(id, password);

        // then
        assertEquals(true, StringUtils.isEmpty(actualId));
    }

    @Test
    public void shouldCheckUser_badId() {
        // given
        String id = "bad_id";

        // when
        String actualId = registration.checkUser(id);

        // then
        assertEquals(true, StringUtils.isEmpty(actualId));
    }

    @Test
    public void shouldCheckUser_goodId() {
        // given
        String code = registration.register("id", "email", "name", "pass", "data", USER.roles()).getCode();
        String id = registration.getIdByCode(code);

        // when
        String actualId = registration.checkUser(id);

        // then
        assertEquals(id, actualId);
    }
}
