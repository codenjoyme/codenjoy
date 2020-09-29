package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.bomberman.services.Events;
import com.codenjoy.dojo.services.EventListener;
import org.mockito.ArgumentCaptor;
import org.mockito.exceptions.verification.NeverWantedButInvoked;
import org.mockito.exceptions.verification.WantedButNotInvoked;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

// TODO ###223 подумать как точно такой же клас в Battlecity удалить
public class EventsListenersAssert {

    private List<EventListener> listeners;

    public EventsListenersAssert(List<EventListener> listeners) {
        this.listeners = listeners;
    }

    public static String getEvents(EventListener events) {
        try {
            ArgumentCaptor<Events> captor = ArgumentCaptor.forClass(Events.class);
            verify(events, atLeast(1)).event(captor.capture());
            return captor.getAllValues().toString();
        } catch (WantedButNotInvoked e) {
            return "[]";
        } finally {
            reset(events);
        }
    }

    public static Integer[] range(int size, Integer[] indexes) {
        if (indexes.length == 0) {
            indexes = IntStream.range(0, size)
                    .boxed()
                    .collect(toList()).toArray(new Integer[0]);
        }
        return indexes;
    }

    public static void assertAll(String expected, int size, Integer[] indexes,
                           Function<Integer, String> function)
    {
        indexes = range(size, indexes);

        String actual = "";
        for (int i = 0; i < indexes.length; i++) {
            actual += function.apply(indexes[i]);
        }

        assertEquals(expected, actual);
    }

    protected void verifyNoEvents(Integer... indexes) {
        try {
            for (int i = 0; i < listeners.size(); i++) {
                if (Arrays.asList(indexes).contains(i)) {
                    verifyNoMoreInteractions(listeners.get(i));
                }
            }
        } catch (AssertionError e) {
            verifyAllEvents("", indexes);
        }
    }

    protected void verifyEvents(EventListener events, String expected) {
        if (expected.equals("[]")) {
            try {
                verify(events, never()).event(any(Events.class));
            } catch (NeverWantedButInvoked e) {
                assertEquals(expected, getEvents(events));
            }
        } else {
            assertEquals(expected, getEvents(events));
        }
        reset(events);
    }

    protected void verifyAllEvents(String expected, Integer... indexes) {
        assertAll(expected, listeners.size(), indexes, index -> {
            Object actual = getEvents(listeners.get(index));
            return String.format("listener(%s) => %s\n", index, actual);
        });
    }
}
