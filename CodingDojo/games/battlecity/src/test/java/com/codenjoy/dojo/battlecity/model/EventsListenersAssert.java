package com.codenjoy.dojo.battlecity.model;

/*-
 * #%L
 * Codenjoy - it's a dojo-like platform from developers to developers.
 * %%
 * Copyright (C) 2018 - 2020 Codenjoy
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

import com.codenjoy.dojo.battlecity.services.Events;
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

// TODO ###223 подумать как точно такой же клас в Bomberman удалить
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
                if (indexes.length == 0 || Arrays.asList(indexes).contains(i)) {
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
