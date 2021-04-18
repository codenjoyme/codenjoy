package com.codenjoy.dojo.utils.events;

import java.util.List;

public interface Testing {

    void assertEquals(Object o1, Object o2);

    <T> T verify(T mock, Object mode);

    <T> void reset(T... mocks);

    void verifyNoMoreInteractions(Object... mocks);

    <T> T any(Class<T> type);

    Object never();

    Object atLeast(int minNumberOfInvocations);

    <T, S extends T> Captor<T> captorForClass(Class<S> clazz);

    interface Captor<T> {
        T capture();

        List<T> getAllValues();
    }
}
