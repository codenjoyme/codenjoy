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
import com.codenjoy.dojo.LeaderboardServiceGrpc;
import com.codenjoy.dojo.Participant;
import com.codenjoy.dojo.StartRequest;
import com.codenjoy.dojo.StartResponse;
import com.codenjoy.dojo.StopRequest;
import com.codenjoy.dojo.StopResponse;
import com.codenjoy.dojo.UpdateRequest;
import com.codenjoy.dojo.UpdateResponse;
import com.codenjoy.dojo.UserInfo;
import com.codenjoy.dojo.web.rest.RestBoardController;
import com.codenjoy.dojo.web.rest.pojo.PScoresOf;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService extends LeaderboardServiceGrpc.LeaderboardServiceImplBase {

    private final List<String> subscriptions;
    private final RestBoardController restBoardController;

    @Autowired
    public LeaderboardService(RestBoardController restBoardController) {
        this.restBoardController = restBoardController;
        this.subscriptions = new ArrayList<>();
    }


    @Override
    public void getLeaderboard(LeaderboardRequest request, StreamObserver<LeaderboardResponse> responseObserver) {
        List<PScoresOf> players = restBoardController.getPlayersScoresForGame(request.getContestId());
        LeaderboardResponse response = LeaderboardResponse.newBuilder()
                .addAllParticipant(players.stream().map(player -> {
                    String id = player.getId();
                    String name = player.getName();
                    long score = player.getScore();
                    return Participant.newBuilder()
                            .setUser(UserInfo.newBuilder()
                                    .setId(id)
                                    .setName(name).build())
                            .setScore(score)
                            .build();
                }).collect(Collectors.toList()))
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void startNotifications(StartRequest request, StreamObserver<StartResponse> responseObserver) {
        String contestId = request.getContestId();
        if (!subscriptions.contains(contestId)) {
            subscriptions.add(contestId);
        }

        responseObserver.onNext(StartResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void stopNotifications(StopRequest request, StreamObserver<StopResponse> responseObserver) {
        String contestId = request.getContestId();
        subscriptions.remove(contestId);
        responseObserver.onNext(StopResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void getUpdate(UpdateRequest request, StreamObserver<UpdateResponse> responseObserver) {
        super.getUpdate(request, responseObserver);
    }
}
