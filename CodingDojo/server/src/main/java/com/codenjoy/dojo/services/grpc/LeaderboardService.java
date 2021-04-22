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
import com.codenjoy.dojo.StopRequest;
import com.codenjoy.dojo.StopResponse;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import com.codenjoy.dojo.web.rest.RestBoardController;
import com.codenjoy.dojo.web.rest.pojo.PScores;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaderboardService extends LeaderboardServiceGrpc.LeaderboardServiceImplBase {

    private final UpdateHandler updateHandler;
    private final RestBoardController restBoardController;

    @Autowired
    public LeaderboardService(UpdateHandler updateHandler, RestBoardController restBoardController) {
        this.updateHandler = updateHandler;
        this.restBoardController = restBoardController;
    }

    @Override
    public void getLeaderboard(LeaderboardRequest request, StreamObserver<LeaderboardResponse> responseObserver) {

        LeaderboardResponse response = getLeaderboardOnStart(request);
        responseObserver.onNext(response);

        updateHandler.addObserver(request.getContestId(), responseObserver);
    }

    @Override
    public void stopNotifications(StopRequest request, StreamObserver<StopResponse> responseObserver) {
        String contestId = request.getContestId();

        StreamObserver<LeaderboardResponse> observer = updateHandler.removeObserver(contestId);
        observer.onCompleted();

        responseObserver.onNext(StopResponse.newBuilder().build());
        responseObserver.onCompleted();
    }

    private LeaderboardResponse getLeaderboardOnStart(LeaderboardRequest request) {
        List<PScores> players = restBoardController.getPlayersScoresForRoom(request.getContestId());

        return LeaderboardResponse.newBuilder()
                .setContestId(request.getContestId())
                .addAllParticipant(players.stream().map(player -> {
                    String id = player.getId();
                    String name = player.getName();
                    long score = player.getScore();
                    return Participant.newBuilder()
                            .setId(id)
                            .setName(name)
                            .setScore(score)
                            .build();
                }).collect(Collectors.toList()))
                .build();
    }
}
