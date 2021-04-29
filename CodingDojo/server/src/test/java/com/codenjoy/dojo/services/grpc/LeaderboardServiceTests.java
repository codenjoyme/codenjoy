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

import com.codenjoy.dojo.LeaderboardRequest;
import com.codenjoy.dojo.LeaderboardResponse;
import com.codenjoy.dojo.StopRequest;
import com.codenjoy.dojo.StopResponse;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import com.codenjoy.dojo.web.rest.RestBoardController;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class LeaderboardServiceTests {

    private final static String CONTEST_ID = "id";

    private StopRequest stopRequest;
    private StopResponse stopResponse;
    private LeaderboardRequest request;
    private LeaderboardResponse response;

    @Mock
    private StreamObserver<StopResponse> stopObserver;
    @Mock
    private StreamObserver<LeaderboardResponse> streamObserver;

    @Mock
    private UpdateHandler updateHandler;
    @Mock
    private RestBoardController restBoardController;

    private LeaderboardService leaderboardService;

    @Before
    public void init() {
        this.stopRequest = StopRequest.newBuilder().setContestId(CONTEST_ID).build();
        this.stopResponse = StopResponse.newBuilder().build();

        this.request = LeaderboardRequest.newBuilder().setContestId(CONTEST_ID).build();
        this.response = LeaderboardResponse.newBuilder().setContestId(CONTEST_ID).build();

        this.leaderboardService = new LeaderboardService(updateHandler, restBoardController);
    }

    @Test
    public void getLeaderboardTest() {
        when(restBoardController.getPlayersScoresForRoom(CONTEST_ID)).thenReturn(new ArrayList<>());

        leaderboardService.getLeaderboard(request, streamObserver);

        verify(restBoardController, times(1)).getPlayersScoresForRoom(CONTEST_ID);
        verify(streamObserver, times(1)).onNext(response);
        verify(updateHandler, times(1)).addObserver(CONTEST_ID, streamObserver);
    }

    @Test
    public void stopNotificationsTest() {
        when(updateHandler.removeObserver(CONTEST_ID)).thenReturn(streamObserver);

        leaderboardService.stopNotifications(stopRequest, stopObserver);

        verify(updateHandler, times(1)).removeObserver(CONTEST_ID);
        verify(streamObserver, times(1)).onCompleted();

        verify(stopObserver, times(1)).onNext(stopResponse);
        verify(stopObserver, times(1)).onCompleted();
    }
}
