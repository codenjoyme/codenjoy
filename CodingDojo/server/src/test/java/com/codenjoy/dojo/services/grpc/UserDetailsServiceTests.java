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

import com.codenjoy.dojo.User;
import com.codenjoy.dojo.UserDetailsIdRequest;
import com.codenjoy.dojo.UserDetailsResponse;
import com.codenjoy.dojo.UserRequest;
import com.codenjoy.dojo.UserResponse;
import com.codenjoy.dojo.services.PlayerSave;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.dao.SubscriptionSaver;
import com.codenjoy.dojo.web.rest.RestBoardController;
import com.codenjoy.dojo.web.rest.pojo.PScores;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UserDetailsServiceTests {

    private static final String USER_ID = "id";
    private static final String EMAIL = "email";
    private static final String CONTEST_ID = "kata";
    private static final String ADMIN = "ROLE_ADMIN";
    private static final String GITHUB = "github.com";

    private UserDetailsIdRequest request;
    private UserDetailsResponse response;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @Mock
    private StreamObserver<UserDetailsResponse> streamObserver;

    @Mock
    private StreamObserver<UserResponse> userResponseStreamObserver;

    @Mock
    private PlayerSave playerSave;

    @Mock
    private Registration registration;

    @Mock
    private RestBoardController restBoardController;

    @Mock
    private SubscriptionSaver subscriptionSaver;

    private UserDetailsService userDetailsService;

    @Before
    public void init() {
        this.request = UserDetailsIdRequest.newBuilder().setId(USER_ID).build();
        this.response = UserDetailsResponse.newBuilder().setId(USER_ID).setEmail(EMAIL).build();
        this.userDetailsService = new UserDetailsService(registration, restBoardController, subscriptionSaver);
        this.userRequest = UserRequest.newBuilder().setContestId(CONTEST_ID).build();
        this.userResponse = UserResponse.newBuilder()
                .addUser(User.newBuilder().setId(USER_ID).setUsername(GITHUB).setName("Player").setRole(ADMIN).build())
                .build();
    }

    @Test
    public void getUserDetailsTest() {
        when(registration.getEmailById(USER_ID)).thenReturn(EMAIL);

        userDetailsService.getUserDetailsById(request, streamObserver);

        verify(streamObserver, times(1)).onNext(response);
        verify(streamObserver, times(1)).onCompleted();
    }

    @Test
    public void getUsersForContestTest() {
        PScores pScores = new PScores(playerSave,"Player");
        when(restBoardController.getPlayersScoresForRoom(CONTEST_ID)).thenReturn(Collections.singletonList(pScores));
        when(playerSave.getId()).thenReturn(USER_ID);
        when(registration.getGitHubUsernameById(USER_ID)).thenReturn(GITHUB);
        when(registration.getRoleById(USER_ID)).thenReturn(ADMIN);

        userDetailsService.getUsersForContest(userRequest, userResponseStreamObserver);

        verify(restBoardController, times(1)).getPlayersScoresForRoom(CONTEST_ID);
        verify(playerSave, times(3)).getId();
        verify(userResponseStreamObserver, times(1)).onNext(userResponse);
        verify(userResponseStreamObserver, times(1)).onCompleted();
    }
}
