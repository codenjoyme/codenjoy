package com.codenjoy.dojo.services;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class RegistrationTest {

    private static Registration service;

    @Before
    public void setup() {
        service = new Registration("target/users.db" + new Random().nextInt());
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
        String code = service.register("user", "pass");

        assertTrue(service.registered("user"));
        assertFalse(service.approved("user"));

        service.approve(code);

        assertTrue(service.registered("user"));
        assertTrue(service.approved("user"));
    }

    @Test
    public void shouldUnRegisteredUserIsNotApproved() throws InterruptedException {
        assertFalse(service.approved("user"));
    }

    @Test
    public void shouldSuccessLogin() throws InterruptedException {
        service.approve(service.register("user", "pass"));

        String code = service.login("user", "pass");

        assertEquals("35993073433489", code);
    }

    @Test
    public void shouldUnSuccessLogin_whenNoApproveEmail() throws InterruptedException {
        service.register("user", "pass");

        String code = service.login("user", "pass");

        assertEquals(null, code);
    }

    @Test
    public void shouldUnSuccessLogin() throws InterruptedException {
        service.register("user", "pass");

        String code = service.login("user", "bad_pass");

        assertNull(code);
    }

    @Test
    public void shouldGetCodeByName() throws InterruptedException {
        service.register("user", "pass");

        String code = service.getCode("user");

        assertEquals("35993073433489", code);
    }

    @Test
    public void shouldGetCodeByName_ifNotFound() throws InterruptedException {
        service.register("user", "pass");

        String code = service.getCode("other_user");

        assertNull(code);
    }

    @Test
    public void shouldGetEmailByCode() throws InterruptedException {
        service.register("user", "pass");

        String email = service.getEmail("35993073433489");

        assertEquals("user", email);
    }

    @Test
    public void shouldGetEmailByCode_ifNotFound() throws InterruptedException {
        service.register("user", "pass");

        String email = service.getEmail("bad_code");

        assertNull(email);
    }
}
