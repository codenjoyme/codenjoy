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


import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.jdbc.SqliteConnectionThreadPoolFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.*;

public class RegistrationTest {

    private static Registration service;

    @Before
    public void setup() {
        service = new Registration(new SqliteConnectionThreadPoolFactory("target/users.db" + new Random().nextInt()));
    }

    @After
    public void tearDown() {
        service.removeDatabase();
    }

    @Test
    public void shouldNotExistsUser() throws InterruptedException {
        assertFalse(service.registered("not_exists"));
    }

    @Test
    public void shouldRegister() throws InterruptedException {
        String code = service.register("user", "pass", "data");

        assertTrue(service.registered("user"));
        assertFalse(service.approved("user"));

        service.approve(code);

        assertTrue(service.registered("user"));
        assertTrue(service.approved("user"));
    }

    @Test
    public void shouldRegisterWithData() throws InterruptedException {
        String code = service.register("user", "pass", "someData");

        List<Registration.User> users = service.getUsers();

        assertEquals("[User{email='user', " +
                "email_approved=0, " +
                "password='pass', " +
                "code='35993073433489', " +
                "data='someData'}]",
                users.toString());
    }

    @Test
    public void shouldUnRegisteredUserIsNotApproved() throws InterruptedException {
        assertFalse(service.approved("user"));
    }

    @Test
    public void shouldSuccessLogin() throws InterruptedException {
        service.approve(service.register("user", "pass", "data"));

        String code = service.login("user", "pass");

        assertEquals("35993073433489", code);
    }

    @Test
    public void shouldUnSuccessLogin_whenNoApproveEmail() throws InterruptedException {
        service.register("user", "pass", "data");

        String code = service.login("user", "pass");

        assertEquals(null, code);
    }

    @Test
    public void shouldUnSuccessLogin() throws InterruptedException {
        service.register("user", "pass", "data");

        String code = service.login("user", "bad_pass");

        assertNull(code);
    }

    @Test
    public void shouldGetCodeByName() throws InterruptedException {
        service.register("user", "pass", "data");

        String code = service.getCode("user");

        assertEquals("35993073433489", code);
    }

    @Test
    public void shouldGetCodeByName_ifNotFound() throws InterruptedException {
        service.register("user", "pass", "data");

        String code = service.getCode("other_user");

        assertNull(code);
    }

    @Test
    public void shouldGetEmailByCode() throws InterruptedException {
        service.register("user", "pass", "data");

        String email = service.getEmail("35993073433489");

        assertEquals("user", email);
    }

    @Test
    public void shouldGetEmailByCode_ifNotFound() throws InterruptedException {
        service.register("user", "pass", "data");

        String email = service.getEmail("bad_code");

        assertNull(email);
    }

    @Test
    public void shouldChangePasswordsToMD5() {
        service.approve(service.register("user", "pass", "data"));
        service.approve(service.register("user2", "pass2", "data2"));

        assertEquals("35993073433489", service.login("user", "pass"));
        assertEquals("111578567106438209", service.login("user2", "pass2"));

        service.changePasswordsToMD5();

        assertEquals("35993073433489", service.login("user", "1a1dc91c907325c69271ddf0c944bc72"));
        assertEquals("111578567106438209", service.login("user2", "c1572d05424d0ecb2a65ec6a82aeacbf"));
    }

}
