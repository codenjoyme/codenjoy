package com.codenjoy.dojo.services.grpc;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2021 Codenjoy
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

import com.codenjoy.dojo.ByIdRequest;
import com.codenjoy.dojo.UserDetailsResponse;
import com.codenjoy.dojo.services.dao.Registration;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTests {

    private static final String USER_ID = "id";
    private static final String EMAIL = "email";

    private ByIdRequest request;
    private UserDetailsResponse response;

    @Mock
    private StreamObserver<UserDetailsResponse> streamObserver;

    @Mock
    private Registration registration;

    private UserDetailsService userDetailsService;

    @Before
    public void init() {
        this.request = ByIdRequest.newBuilder().setId(USER_ID).build();
        this.response = UserDetailsResponse.newBuilder().setId(USER_ID).setEmail(EMAIL).build();
        this.userDetailsService = new UserDetailsService(registration);
    }

    @Test
    public void getUserDetailsTest() {
        when(registration.getEmailById(USER_ID)).thenReturn(EMAIL);

        userDetailsService.getUserDetailsById(request, streamObserver);

        verify(streamObserver, times(1)).onNext(response);
        verify(streamObserver, times(1)).onCompleted();
    }
}
