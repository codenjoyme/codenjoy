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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class UpdateHandler {

    private final GameSaver gameSaver;
    private final Registration registration;

    private final Map<String, StreamObserver<LeaderboardResponse>> streamObservers;

    private final ExecutorService executorService;
    private final BlockingQueue<Participant> participants;

    public UpdateHandler(GameSaver gameSaver, Registration registration) {
        this.gameSaver = gameSaver;
        this.registration = registration;
        this.streamObservers = new HashMap<>();

        this.executorService = Executors.newSingleThreadExecutor();
        this.participants = new LinkedBlockingQueue<>(10);
    }

    public void addObserver(String contestId, StreamObserver<LeaderboardResponse> observer) {
        this.streamObservers.put(contestId, observer);
    }

    public StreamObserver<LeaderboardResponse> removeObserver(String contestId) {
        return this.streamObservers.remove(contestId);
    }

    public void sendUpdate(String username, long score) {
        Participant participant = buildParticipant(username, score);
        try {
            this.participants.put(participant);
        } catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        executorService.submit(() -> {
            try {
                Map<String, LeaderboardResponse.Builder> responseBuilders = drainQueue();
                responseBuilders.values().forEach(this::buildResponseAndSend);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        });
    }

    private Participant buildParticipant(String username, long score) {
        String id = registration.getIdByGitHubUsername(username);
        String name = registration.getNameById(id);
        return Participant.newBuilder()
                .setId(id)
                .setName(name)
                .setScore(score)
                .build();
    }

    private Map<String, LeaderboardResponse.Builder> drainQueue() throws InterruptedException {
        List<Participant> participantList = new ArrayList<>();
        participantList.add(participants.take());
        participants.drainTo(participantList);

        Map<String, LeaderboardResponse.Builder> responses = new HashMap<>();
        for (Participant participant : participantList) {
            String contestId = gameSaver.getRoomNameByPlayerId(participant.getId());
            if (responses.containsKey(contestId)) {
                responses.get(contestId).addParticipant(participant);
            } else {
                responses.put(contestId, LeaderboardResponse.newBuilder().setContestId(contestId).addParticipant(participant));
            }
        }
        return responses;
    }

    private void buildResponseAndSend(LeaderboardResponse.Builder response) {
        this.streamObservers.forEach((contestId, observer) -> {
            if (contestId.equals(response.getContestId())) {
                observer.onNext(response.build());
            }
        });
    }
}
