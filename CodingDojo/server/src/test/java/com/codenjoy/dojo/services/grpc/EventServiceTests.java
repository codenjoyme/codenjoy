package com.codenjoy.dojo.services.grpc;

import com.codenjoy.dojo.EventsRequest;
import com.codenjoy.dojo.EventsResponse;
import com.codenjoy.dojo.services.dao.PlayerGameSaver;
import io.grpc.stub.StreamObserver;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
public class EventServiceTests {

    private EventsRequest request;
    private EventsResponse response;
    private Map<String, String> events;

    @Mock
    private StreamObserver<EventsResponse> streamObserver;

    @Mock
    private PlayerGameSaver playerGameSaver;

    private EventService eventService;

    @Before
    public void init() {
        this.request = EventsRequest.newBuilder().build();
        this.response = EventsResponse.newBuilder().build();
        this.events = new HashMap<>();
        this.eventService = new EventService(playerGameSaver);
    }

    @Test
    public void getAllEventsTest() {
        when(playerGameSaver.getEventsList()).thenReturn(events);

        eventService.getAllEvents(request, streamObserver);

        verify(streamObserver, times(1)).onNext(response);
        verify(streamObserver, times(1)).onCompleted();
    }
}
