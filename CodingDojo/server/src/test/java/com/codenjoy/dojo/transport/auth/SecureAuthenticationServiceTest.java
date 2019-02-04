package com.codenjoy.dojo.transport.auth;

import com.codenjoy.dojo.services.dao.Registration;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecureAuthenticationServiceTest {

    private Registration registration;
    private SecureAuthenticationService service;

    @Before
    public void setUp() {
        service = new SecureAuthenticationService(){{
            SecureAuthenticationServiceTest.this.registration =
                    this.registration = mock(Registration.class);
        }};
    }

    @Test
    public void registeredUser() {
        setExists(true);
        assertAuth("user@email.com",
                "57823465983456583485", "user@email.com");

    }

    @Test
    public void notRegisteredUser() {
        setExists(false);
        assertAuth(null,
                "57823465983456583485", "user@email.com");
    }

    @Test
    public void ai_withValidName_andValidCode() {
        setExists(false);
        assertAuth("some-text-super-ai@codenjoy.com",
                "12345678901234567890", "some-text-super-ai@codenjoy.com");
    }

    @Test
    public void ai_withInvalidName_andValidCode() {
        setExists(false);
        assertAuth(null,
                "12345678901234567890", "user@email.com");
    }

    @Test
    public void ai_withValidName_andInvalidCode() {
        setExists(false);
        assertAuth(null,
                "11111111111111111110", "some-text-super-ai@codenjoy.com");
    }

    @Test
    public void ai_withInvalidName_andInvalidCode() {
        setExists(false);
        assertAuth(null,
                "11111111111111111110", "user@email.com");
    }

    private void setExists(boolean exists) {
        when(registration.checkUser(anyString(), anyString())).thenReturn(exists);
    }

    private void assertAuth(String expectedUser, String code, String user) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(user);
        when(request.getParameter("code")).thenReturn(code);


        assertEquals(expectedUser, service.authenticate(request));
    }

}