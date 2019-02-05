package com.codenjoy.dojo.transport.auth;

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
    public void user_registered() {
        shouldCheckuserIs("userid");
        assertAuth("userid",
                "57823465983456583485", "user@email.com");

    }

    @Test
    public void user_notRegistered() {
        shouldCheckuserIs(null);
        assertAuth(null,
                "57823465983456583485", "user@email.com");
    }

    @Test
    public void ai_withValidName_andValidCode() {
        shouldCheckuserIs(null);
        assertAuth("some-text-super-ai@codenjoy.com",
                "12345678901234567890", "some-text-super-ai@codenjoy.com");
    }

    @Test
    public void ai_withInvalidName_andValidCode() {
        shouldCheckuserIs(null);
        assertAuth(null,
                "12345678901234567890", "user@email.com");
    }

    @Test
    public void ai_withValidName_andInvalidCode() {
        shouldCheckuserIs(null);
        assertAuth(null,
                "11111111111111111110", "some-text-super-ai@codenjoy.com");
    }

    @Test
    public void ai_withInvalidName_andInvalidCode() {
        shouldCheckuserIs(null);
        assertAuth(null,
                "11111111111111111110", "user@email.com");
    }

    private void shouldCheckuserIs(String exists) {
        when(registration.checkUser(anyString(), anyString())).thenReturn(exists);
    }

    private void assertAuth(String expectedUser, String code, String user) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(user);
        when(request.getParameter("code")).thenReturn(code);


        assertEquals(expectedUser, service.authenticate(request));
    }

}
