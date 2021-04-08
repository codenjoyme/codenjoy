package com.codenjoy.dojo.services.grpc;

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
