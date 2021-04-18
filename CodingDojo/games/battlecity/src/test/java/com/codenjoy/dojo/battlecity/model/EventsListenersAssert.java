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

import com.codenjoy.dojo.services.EventListener;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class EventsListenersAssert {

    private List<EventListener> listeners;
    private Class eventsClass;
    private BiConsumer<Object, Object> assertor;
    private Mocker mocker;

    public interface Mocker {
        <T> T verify(T mock, VerificationMode mode);
    }

    public EventsListenersAssert(List<EventListener> listeners,
                                 Class eventsClass,
                                 BiConsumer<Object, Object> assertor,
                                 Mocker mocker)
    {
        this.listeners = listeners;
        this.eventsClass = eventsClass;
        this.assertor = assertor;
        this.mocker = mocker;
    }

    private String getEvents(EventListener events) {
        try {
            ArgumentCaptor captor = ArgumentCaptor.forClass(eventsClass);
            Mockito.verify(events, Mockito.atLeast(1)).event(captor.capture());
            return captor.getAllValues().toString();
        } catch (Throwable e) {
            if (is(e, "WantedButNotInvoked")) {
                return "[]";
            } else {
                throw e;
            }
        } finally {
            Mockito.reset(events);
        }
    }

    private boolean is(Throwable e, String exception) {
        return e.getClass().getSimpleName().equals(exception);
    }

    private Integer[] range(int size, Integer[] indexes) {
        if (indexes.length == 0) {
            indexes = IntStream.range(0, size)
                    .boxed()
                    .collect(toList()).toArray(new Integer[0]);
        }
        return indexes;
    }

    private void assertAll(String expected, int size, Integer[] indexes,
                           Function<Integer, String> function)
    {
        indexes = range(size, indexes);

        String actual = "";
        for (int i = 0; i < indexes.length; i++) {
            actual += function.apply(indexes[i]);
        }

        assertor.accept(expected, actual);
    }

    public void verifyNoEvents(Integer... indexes) {
        try {
           for (int i = 0; i < listeners.size(); i++) {
                if (indexes.length == 0 || Arrays.asList(indexes).contains(i)) {
                    Mockito.verifyNoMoreInteractions(listeners.get(i));
                }
            }
        } catch (Throwable e) {
            if (is(e, "AssertionError")) {
                verifyAllEvents("", indexes);
            } else {
                throw e;
            }
        }
    }

    public void verifyEvents(EventListener events, String expected) {
        if (expected.equals("[]")) {
            try {
                Mockito.verify(events, Mockito.never()).event(Mockito.any(eventsClass));
            } catch (Throwable e) {
                if (is(e, "NeverWantedButInvoked")) {
                    assertor.accept(expected, getEvents(events));
                } else {
                    throw e;
                }
            }
        } else {
            assertor.accept(expected, getEvents(events));
        }
        Mockito.reset(events);
    }

    public void verifyAllEvents(String expected, Integer... indexes) {
        assertAll(expected, listeners.size(), indexes, index -> {
            Object actual = getEvents(listeners.get(index));
            return String.format("listener(%s) => %s\n", index, actual);
        });
    }
}
