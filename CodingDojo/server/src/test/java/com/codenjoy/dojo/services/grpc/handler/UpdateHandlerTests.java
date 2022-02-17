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
import com.codenjoy.dojo.config.meta.SQLiteProfile;
import com.codenjoy.dojo.services.GameSaver;
import com.codenjoy.dojo.services.dao.Registration;
import com.codenjoy.dojo.services.grpc.handler.UpdateHandler;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class UpdateHandlerTests {

    private final String ROOM_NAME = "room";
    private final String GITHUB_USERNAME = "username";
    private final String ID = "id";
    private final String NAME = "name";
    private final long SCORE = 5L;

    @Mock
    private GameSaver gameSaver;
    @Mock
    private Registration registration;

    @Mock
    private StreamObserver<LeaderboardResponse> streamObserver;

    private UpdateHandler updateHandler;

    @Before
    public void init() {
        this.updateHandler = new UpdateHandler(gameSaver, registration);

        updateHandler.addObserver(ROOM_NAME, streamObserver);
    }

    @Test
    public void addAndRemoveObserverTest() {
        assertEquals(streamObserver, updateHandler.removeObserver(ROOM_NAME));
    }

    @Test
    public void sendUpdateTest() {
        when(registration.getIdByGitHubUsername(GITHUB_USERNAME)).thenReturn(ID);
        when(registration.getNameById(ID)).thenReturn(NAME);

        when(gameSaver.getRoomNameByPlayerId(ID)).thenReturn(ROOM_NAME);

        updateHandler.sendUpdate(GITHUB_USERNAME, SCORE);

        verify(registration, times(1)).getIdByGitHubUsername(GITHUB_USERNAME);
        verify(registration, times(1)).getNameById(ID);
//        verify(gameSaver, times(1)).getRoomNameByPlayerId(ID);

//        verify(streamObserver, times(1)).onNext(buildResponse());
    }

    private LeaderboardResponse buildResponse() {
        Participant participant = Participant.newBuilder()
                .setId(ID)
                .setName(NAME)
                .setScore(SCORE)
                .build();
        return LeaderboardResponse.newBuilder()
                .setContestId(ROOM_NAME)
                .addParticipant(participant).build();
    }
}
