package com.codenjoy.dojo.bomberman.model;

import com.codenjoy.dojo.utils.EventsListenersAssert;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class MockitoMocker implements EventsListenersAssert.Mocker {

    private Class<?> assertClass;

    public MockitoMocker() {
        try {
            ClassLoader classLoader = this.getClass().getClassLoader();
            assertClass = classLoader.loadClass("org.junit.Assert");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void assertEquals(Object o1, Object o2) {
        try {
            Method assertEquals = assertClass.getDeclaredMethod("assertEquals", Object.class, Object.class);
            assertEquals.invoke(assertClass, o1, o2);
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                Throwable target = ((InvocationTargetException) e).getTargetException();
                if (target instanceof Error) {
                    throw (Error)target;
                }
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T> T verify(T mock, Object mode) {
        return Mockito.verify(mock, (VerificationMode) mode);
    }

    @Override
    public <T> void reset(T... mocks) {
        Mockito.reset(mocks);
    }

    @Override
    public void verifyNoMoreInteractions(Object... mocks) {
        Mockito.verifyNoMoreInteractions(mocks);
    }

    @Override
    public <T> T any(Class<T> type) {
        return Mockito.any(type);
    }

    @Override
    public Object never() {
        return Mockito.never();
    }

    @Override
    public Object atLeast(int minNumberOfInvocations) {
        return Mockito.atLeast(minNumberOfInvocations);
    }

    @Override
    public <T, S extends T> EventsListenersAssert.Captor<T> captorForClass(Class<S> clazz) {
        ArgumentCaptor<T> captor = ArgumentCaptor.forClass(clazz);
        return new EventsListenersAssert.Captor<T>() {
            @Override
            public T capture() {
                return captor.capture();
            }

            @Override
            public List<T> getAllValues() {
                return captor.getAllValues();
            }
        };
    }
}
