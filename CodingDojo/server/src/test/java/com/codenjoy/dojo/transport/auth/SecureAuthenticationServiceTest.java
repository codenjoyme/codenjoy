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
import com.codenjoy.dojo.web.controller.Validator;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
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
            this.validator = new Validator(){{
                this.registration = SecureAuthenticationServiceTest.this.registration;
            }};
        }};
    }

    @Test
    public void user_registered() {
        // given
        сheckUser("userid");

        // when then
        assertAuth("userid",
                "57823465983456583485", "user");

    }

    @Test
    public void user_notRegistered() {
        // given
        сheckUser(null);

        // when then
        assertAuthFail("57823465983456583485", "user");
    }

    @Test
    public void user_badId() {
        // given
        сheckUser("userid");

        // when then
        assertAuthFail("12345678901234567890", "user$");
    }

    @Test
    public void user_badCode() {
        // given
        сheckUser("userid");

        // when then
        assertAuthFail("12345678901234567890__", "user");
    }

    @Test
    public void ai_withValidName_andValidCode() {
        // given
        сheckUser(null);

        // when then
        assertAuthSuccess("12345678901234567890", "some-text-super-ai");
    }

    @Test
    public void ai_withInvalidName_andValidCode() {
        // given
        сheckUser(null);

        // when then
        assertAuthFail("12345678901234567890", "user-ai");
    }

    @Test
    public void ai_withValidName_andInvalidCode_ignoreCode() {
        // given
        сheckUser(null);

        // when then
        assertAuthSuccess("1111111111111111111__", "some-text-super-ai");
    }

    @Test
    public void ai_withInvalidName_andInvalidCode_ignoreCode() {
        // given
        сheckUser(null);

        // when then
        assertAuthFail("11111111111111111110", "user");
    }

    private void сheckUser(String exists) {
        when(registration.checkUser(anyString(), anyString())).thenReturn(exists);
    }

    private void assertAuthFail(String code, String id) {
        assertAuth(null, code, id);
    }

    private void assertAuthSuccess(String code, String id) {
        assertAuth(id, code, id);
    }
    
    private void assertAuth(String expectedUser, String code, String id) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("user")).thenReturn(id);
        when(request.getParameter("code")).thenReturn(code);

        assertEquals(expectedUser, service.authenticate(request));
    }

}
