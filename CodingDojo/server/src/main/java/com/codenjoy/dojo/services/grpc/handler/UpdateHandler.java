package com.codenjoy.dojo.services.grpc.handler;

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

import com.codenjoy.dojo.LeaderboardResponse;
import com.codenjoy.dojo.Participant;
import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.dao.Registration;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UpdateHandler {

    private final GameSaver gameSaver;
    private final Registration registration;

    private final Map<String, StreamObserver<LeaderboardResponse>> streamObservers;

    public UpdateHandler(GameSaver gameSaver, Registration registration) {
        this.gameSaver = gameSaver;
        this.registration = registration;
        this.streamObservers = new HashMap<>();
    }

    public void addObserver(String roomName, StreamObserver<LeaderboardResponse> observer) {
        this.streamObservers.put(roomName, observer);
    }

    public StreamObserver<LeaderboardResponse> removeObserver(String roomName) {
        return this.streamObservers.remove(roomName);
    }

    public void sendUpdate(String username, long score) {
        LeaderboardResponse response = buildResponse(username, score);

        this.streamObservers.forEach((roomName, observer) -> {
            if (response.getContestId().equals(roomName)) {
                observer.onNext(response);
            }
        });
    }

    private LeaderboardResponse buildResponse(String username, long score) {
        String id = registration.getIdByGitHubUsername(username);
        String name = registration.getNameById(id);

        Participant participant = Participant.newBuilder()
                .setId(id)
                .setName(name)
                .setScore(score)
                .build();
        return LeaderboardResponse.newBuilder()
                .setContestId(gameSaver.getRoomNameByPlayerId(id))
                .addParticipant(participant).build();
    }
}
